package com.sszt.cdslpetitionletter.mine

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.SPUtils
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.ext.view.getHintStr
import com.sszt.basis.ext.view.isTrimEmpty
import com.sszt.basis.ext.view.textString
import com.sszt.basis.ext.view.textStringTrim
import com.sszt.basis.util.Utils
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.ForgetPas
import com.sszt.cdslpetitionletter.databinding.ActivityUpdatePhoneBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.resources.IRoute

/**
 * 修改手机号
 */
@Route(path = IRoute.mine_update_phone)
class UpdatePhoneActivity : BaseActivity<PublicViewModel, ActivityUpdatePhoneBinding>() {


    override fun layoutId() = R.layout.activity_update_phone

    private var downTimer: CountDownTimer? = null

    private val mainRequest by lazy { MainRequestViewModel() }

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnClickListener { finish() }

        //提交
        bind.toSave.setOnClickListener {
            when{
                bind.oldPhone.textString()!= SPUtils.getInstance().getString("mobile") -> toast("原手机号码错误")
                !Utils.isPhone(bind.phone.textString()) -> toast("手机号码格式错误")
                bind.verificationCode.isTrimEmpty() -> toast(bind.verificationCode.getHintStr())
                else ->{
                    tipDialog()
                    mainRequest.checkPhone(requestForBody(
                        "phoneNum" to bind.phone.textString(),
                        "msgCode" to bind.verificationCode.textString()
                    ))
                }
            }
        }

        //获取验证码
        bind.getVerificationCode.setOnClickListener {
            if (!Utils.isPhone(bind.phone.textString())) {
                toast("手机号码格式错误")
                return@setOnClickListener
            }
            downTimer = object : CountDownTimer(60 * 1000 - 1, 1000) {
                override fun onFinish() {
                    bind.getVerificationCode.text = "重新获取"
                    bind.getVerificationCode.setTextColor(Color.parseColor("#ffffff"))
                    bind.getVerificationCode.isClickable = true
                    bind.getVerificationCode.background = getDrawable(com.sszt.moduleresources.R.drawable.r8_main_shape)
                    bind.getVerificationCode.isEnabled = true
                    cancel()
                }

                override fun onTick(millisUntilFinished: Long) {
                    var s = millisUntilFinished / 1000
                    bind.getVerificationCode.isClickable = false
                    bind.getVerificationCode.isEnabled = false
                    bind.getVerificationCode.text = "${s}s"
                    bind.getVerificationCode.background = getDrawable(com.sszt.moduleresources.R.drawable.r8_eee_shape)
                    bind.getVerificationCode.setTextColor(Color.parseColor("#333333"))

                }
            }.start()
            tipDialog()
            mainRequest.getVerificationCode(requestForBody("phoneNum" to bind.phone.textString()))
        }

        mainRequest.getVerificationCodeResult.observe(this) {
            if (it.code == 2000) {
                successDialog("验证码获取成功") {
                }
            } else {
                errorDialog(it.msg)
            }
        }

        mainRequest.forgetPasResult.observe(this){
            if (it.code == 2000) {
                successDialog(it.msg) {
                    finish()
                }
            } else {
                errorDialog(it.msg)
            }
        }

        mainRequest.checkPhoneResult.observe(this){
            if (it.code == 2000) {
                mainRequest.forgetPas(
                    requestForParameter(
                        "user" to ForgetPas(
                            SPUtils.getInstance().getString("userName"),
                            bind.phone.textStringTrim()
                        )
                    )
                )
            } else {
                errorDialog(it.msg)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (downTimer != null) {
            downTimer?.cancel()
        }

    }
}