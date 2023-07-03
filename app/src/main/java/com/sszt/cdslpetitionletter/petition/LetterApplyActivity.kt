package com.sszt.cdslpetitionletter.petition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.ext.view.getHintStr
import com.sszt.basis.ext.view.isTrimEmpty
import com.sszt.basis.ext.view.textString
import com.sszt.basis.network.Url
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivityLetterApplyBinding
import com.sszt.cdslpetitionletter.databinding.ActivityLetterManageBinding
import com.sszt.cdslpetitionletter.viewmodel.request.PetitionLetterRequestViewModel
import com.sszt.resources.IRoute

/**
 * 作废/复查/复核申请
 */
@Route(path = IRoute.letter_apply)
class LetterApplyActivity : BaseActivity<PublicViewModel, ActivityLetterApplyBinding>() {


    override fun layoutId() = R.layout.activity_letter_manage

    private val request by lazy { PetitionLetterRequestViewModel() }
    private var xfAcceptInfoUuid = ""
    private var exDepartUuid = ""
    var title = ""
    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }
        title = intent.getStringExtra("title") ?: ""
        xfAcceptInfoUuid = intent.getStringExtra("xfAcceptInfoUuid") ?: ""
        exDepartUuid = intent.getStringExtra("exDepartUuid") ?: ""
        bind.titleLayout.titleTitle.text = title


        bind.toSave.setOnClickListener {
            when {
                bind.letterApplyContent.isTrimEmpty() -> toast(bind.letterApplyContent.getHintStr())
                else -> {
                    save()
                }
            }
        }
        request.addCaseApplyResult.observe(this) {
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

    private fun save() {

        tipDialog()
        when (title) {
            "作废申请" -> {
                request.addCaseApply(
                    Url.agent + "applets/cancel/applyCancel",
                    requestForBody(
                        "xfAcceptInfoUuid" to xfAcceptInfoUuid,
                        "applyCacel" to bind.letterApplyContent.textString()
                    )
                )
            }
            "复查申请" -> {
                request.addCaseApply(
                    Url.agent + "applets/fucha/review",
                    requestForBody(
                        "xfAcceptInfoUuid" to xfAcceptInfoUuid,
                        "reExplain" to bind.letterApplyContent.textString()
                    )
                )
            }
            "复核申请" -> {
                request.addCaseApply(
                    Url.agent + "applets/fuhe/toReview",
                    requestForBody(
                        "xfAcceptInfoUuid" to xfAcceptInfoUuid,
                        "exDepartUuid" to exDepartUuid,
                        "reExplain" to bind.letterApplyContent.textString()
                    )
                )
            }
        }

    }
}