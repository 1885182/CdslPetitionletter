package com.sszt.cdslpetitionletter.cases

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.ext.view.getHintStr
import com.sszt.basis.ext.view.isTrimEmpty
import com.sszt.basis.ext.view.textString
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.CaseWritBean
import com.sszt.cdslpetitionletter.databinding.ActivityCaseWritBinding
import com.sszt.cdslpetitionletter.viewmodel.request.CaseRequestViewModel
import com.sszt.resources.IRoute

/**
 * 案件文书添加
 */
@Route(path = IRoute.case_writ)
class CaseWritActivity : BaseActivity<PublicViewModel, ActivityCaseWritBinding>() {


    override fun layoutId() = R.layout.activity_case_writ


    private var researchPersonSign: String = ""
    private var researchRespondentSign: String = ""

    private val request by lazy { CaseRequestViewModel() }

    override fun initView(savedInstanceState: Bundle?) {
        bind.caseWritTL.setOnBackClick { finish() }

        var type = intent.getIntExtra("type", 0)
        var uuid = intent.getStringExtra("uuid") ?: ""
        if (type == 1) {
            bind.caseWritTL.titleTitle.text = "人民调解终止书"
            bind.recordWordRecordNameTip.visibility = View.GONE
            bind.recordWordRecordName.visibility = View.GONE
            bind.recordWordRecordWordTip.text = "正文："
            bind.recordWordRecordWord.hint = "请输入正文"
        } else if (type == 2) {
            bind.caseWritTL.titleTitle.text = "人民调解协议书"
        }

        bind.toCancel.setOnClickListener { finish() }

        //申请人
        bind.recordWordNameSignTv.setOnClickListener {
            routerResultCode(
                IRoute.main_signature,
                1001
            )
        }
        //被申请人
        bind.recordWordNameSignByTv.setOnClickListener {
            routerResultCode(
                IRoute.main_signature,
                1002
            )
        }

        //提交
        bind.toSave.setOnClickListener {
            when {
                bind.recordWordNameBy.isTrimEmpty() -> toast(bind.recordWordNameBy.getHintStr())

                bind.recordWordRecordWord.isTrimEmpty() -> toast(bind.recordWordRecordWord.getHintStr())

                researchPersonSign.isEmpty() -> toast("请完成申请人签名")
                researchRespondentSign.isEmpty() -> toast("请完成被申请人签名")
                type == 2 -> {
                    if (bind.recordWordRecordName.isTrimEmpty()) {
                        toast(bind.recordWordRecordName.getHintStr())
                        return@setOnClickListener
                    }
                    save(uuid, type)
                }
                else -> save(uuid, type)
            }

        }


        //接口返回
        request.acceptCaseResult.observe(this) {
            if (it.code == 2000) {
                successDialog("操作成功") {
                    setResult(1999)
                    finish()
                }
            } else {
                errorDialog(it.msg ?: "失败")
            }
        }

    }

    private fun save(uuid: String, type: Int) {
        showLoading()
        request.acceptCase(
            requestForBody(
                "caseUuid" to uuid,
                "processMode" to if (type == 1) 6 else if (type == 2) 5 else 0,
                "caseWrid" to CaseWritBean(
                    bind.recordWordNameBy.textString(),
                    bind.recordWordRecordName.textString(),
                    bind.recordWordRecordWord.textString(),
                    if (type == 1) 2 else if (type == 2) 1 else 0,
                    researchPersonSign,
                    researchRespondentSign
                )
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 2000) {
            when (requestCode) {
                1001 -> {
                    researchPersonSign = data?.getStringExtra("file") ?: ""
                    Glide.with(this).load(data?.getStringExtra("file")).into(bind.recordWordNameSign)
                }
                1002 -> {
                    researchRespondentSign = data?.getStringExtra("file") ?: ""
                    Glide.with(this).load(data?.getStringExtra("file")).into(bind.recordWordNameSignBy)
                }
            }
        }
    }


}