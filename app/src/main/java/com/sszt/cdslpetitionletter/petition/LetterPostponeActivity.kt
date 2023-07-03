package com.sszt.cdslpetitionletter.petition

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.ext.view.getHintStr
import com.sszt.basis.ext.view.isTrimEmpty
import com.sszt.basis.ext.view.textString
import com.sszt.basis.ext.view.textStringTrim
import com.sszt.basis.weight.DialogView
import com.sszt.basis.weight.TimeDialog
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivityLetterPostponeBinding
import com.sszt.cdslpetitionletter.viewmodel.request.PetitionLetterRequestViewModel
import com.sszt.resources.IRoute

/**
 * 延期申请
 */
@Route(path = IRoute.letter_postpone)
class LetterPostponeActivity : BaseActivity<PublicViewModel, ActivityLetterPostponeBinding>() {


    override fun layoutId() = R.layout.activity_letter_postpone
    private val request by lazy { PetitionLetterRequestViewModel() }
    var baseOrgUuid = ""
    var xfAcceptInfoUuid = ""
    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }

        xfAcceptInfoUuid = intent.getStringExtra("xfAcceptInfoUuid") ?: ""

        request.getParentOrg()

        request.getParentOrgResult.observe(this) {
            bind.postponeDepartment.text = it?.baseOrgName
            baseOrgUuid = it?.baseOrgUuid ?: ""
        }

        bind.postponeTime.setOnClickListener {
            TimeDialog(
                this,
                "选择日期",
                booleanArrayOf(true, true, true, true, true, false),
                bind.postponeTime.textStringTrim()
            ) {
                bind.postponeTime.text = TimeUtils.date2String(it, DialogView.timeFormat24)
            }.show(supportFragmentManager, "dialog")
        }

        bind.toSave.setOnClickListener {
            when {
                bind.postponeTime.isTrimEmpty() -> toast(bind.postponeTime.getHintStr())
                bind.postponeReason.isTrimEmpty() -> toast(bind.postponeReason.getHintStr())
                else -> {
                    tipDialog()
                    request.postPone(
                        requestForBody(
                            "extendsionTerm" to bind.postponeTime.textString(),
                            "checkOrgName" to bind.postponeDepartment.textString(),
                            "extendsionReson" to bind.postponeReason.textString(),
                            "checkOrgUuid" to baseOrgUuid,//部门uuid
                            "xfAcceptInfoUuid" to xfAcceptInfoUuid,
                        )
                    )
                }
            }
        }

        request.postponeResult.observe(this) {
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