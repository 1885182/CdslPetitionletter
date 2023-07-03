package com.sszt.cdslpetitionletter.mine

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.SPUtils
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.ext.view.textStringTrim
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.ForgetPas
import com.sszt.cdslpetitionletter.databinding.ActivityUpdatePasBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.resources.IRoute

/**
 * 修改密码
 */
@Route(path = IRoute.update_pas)
class UpdatePasActivity : BaseActivity<PublicViewModel, ActivityUpdatePasBinding>() {


    override fun layoutId() = R.layout.activity_update_pas

    private val request by lazy { MainRequestViewModel() }
    override fun initView(savedInstanceState: Bundle?) {
        bind.securitySettingTitle.setOnBackClick { finish() }

        val type = intent.getIntExtra("type", 0)
        if (type == 2) {
            bind.securitySettingOldTip.visibility = View.GONE
            bind.securitySettingOld.visibility = View.GONE
            bind.securitySettingOldImg.visibility = View.GONE
        }

        //提交
        bind.securitySettingSave.setOnClickListener {


            when {
                type != 2 -> if (bind.securitySettingOld.textStringTrim().isEmpty()) {
                    toast("请输入旧密码")
                    return@setOnClickListener
                }

                //bind.securitySettingNew.textStringTrim().length<6 -> toast("密码须6-12个字符")
                bind.securitySettingNew.textStringTrim().isEmpty() -> toast("请输入新密码")
                bind.securitySettingAgain.textStringTrim().isEmpty() -> toast("请输入确认新密码")
                bind.securitySettingNew.textStringTrim() != bind.securitySettingAgain.textStringTrim() -> toast(
                    "两次密码输入不一致"
                )
                else -> {
                    tipDialog()
                    if (type == 2) {
                        request.forgetPas(
                            requestForParameter(
                                "user" to ForgetPas(
                                    SPUtils.getInstance().getString("userName"),
                                    bind.securitySettingAgain.textStringTrim()
                                )
                            )
                        )
                    } else {
                        request.updatePas(
                            requestForParameter(
                                "userName" to SPUtils.getInstance().getString("userName"),
                                "oldPwd" to bind.securitySettingOld.textStringTrim(),
                                "newPwd" to bind.securitySettingAgain.textStringTrim()
                            )
                        )
                    }
                }
            }
        }



        request.updatePasResult.observe(this) {
            if (it.code == 2000) {
                successDialog(it.msg) {
                    finish()
                }
            } else {
                errorDialog(it.msg)
            }
        }


        request.forgetPasResult.observe(this) {
            if (it.code == 2000) {
                successDialog(it.msg) {
                    setResult(1999)
                    finish()
                }
            } else {
                errorDialog(it.msg)
            }
        }
    }
}