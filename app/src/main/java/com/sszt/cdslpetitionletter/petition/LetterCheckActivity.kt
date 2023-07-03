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
import com.sszt.basis.weight.DataDialog
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivityLetterApplyBinding
import com.sszt.cdslpetitionletter.databinding.ActivityLetterCheckBinding
import com.sszt.cdslpetitionletter.databinding.ActivityLetterManageBinding
import com.sszt.cdslpetitionletter.viewmodel.request.PetitionLetterRequestViewModel
import com.sszt.resources.IRoute

/**
 * 作废/复查/复核申请
 */
@Route(path = IRoute.letter_check)
class LetterCheckActivity : BaseActivity<PublicViewModel, ActivityLetterCheckBinding>() {


    override fun layoutId() = R.layout.activity_letter_check

    private val request by lazy { PetitionLetterRequestViewModel() }
    private val listResult = ArrayList<String>()
    private var xfAcceptInfoUuid = ""
    private var xfReviewUuid = ""
    private var xfToReviewUuid = ""

    var titles = ""
    override fun initView(savedInstanceState: Bundle?) {
        titles = intent.getStringExtra("title")?:""
        xfAcceptInfoUuid = intent.getStringExtra("xfAcceptInfoUuid") ?: ""
        xfReviewUuid = intent.getStringExtra("xfReviewUuid") ?: ""
        xfToReviewUuid = intent.getStringExtra("xfToReviewUuid") ?: ""
        bind.titleLayout.titleTitle.text = titles



        if (titles == "复核审核"){
            bind.letterCheckResultTip.text = "复核结果:"
            bind.letterCheckResult.hint = "请选择复核结果"
        }

        initData()

        //选择反应类型
        bind.letterCheckResult.setOnClickListener {
            DataDialog(this, listResult) {
                bind.letterCheckResult.text = listResult[it]
            }.show(supportFragmentManager, "dialogData")
        }

        bind.toSave.setOnClickListener {
            when {
                bind.letterCheckResult.isTrimEmpty() -> toast(bind.letterCheckResult.getHintStr())
                bind.letterCheckContent.isTrimEmpty() -> toast(bind.letterCheckContent.getHintStr())
                else -> {
                    save()
                }
            }
        }

        request.addCaseApplyResult.observe(this){
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
        when (titles) {
            "复查审核" -> {
                request.addCaseApply(
                    Url.agent + "applets/fucha/doReviewToCheck",
                    requestForBody(
                        "xfAcceptInfoUuid" to xfAcceptInfoUuid,
                        "xfReviewUuid" to xfReviewUuid,
                        "reResult" to bind.letterCheckResult.textString(),
                        "reOpinion" to bind.letterCheckContent.textString(),
                    )
                )
            }
            "复核审核" -> {
                request.addCaseApply(
                    Url.agent + "applets/fuhe/doReview",
                    requestForBody(
                        "xfAcceptInfoUuid" to xfAcceptInfoUuid,
                        "xfToReviewUuid" to xfToReviewUuid,
                        "reResult" to bind.letterCheckResult.textString(),
                        "reOpinion" to bind.letterCheckContent.textString(),
                    )
                )
            }
        }

    }

    private fun initData() {
        listResult.add("责令重办")
        listResult.add("维持不变")
        listResult.add("不予受理")
    }
}