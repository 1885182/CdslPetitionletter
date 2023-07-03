package com.sszt.cdslpetitionletter.cases

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.ext.view.textString
import com.sszt.basis.util.chat.FileUtils
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.CaseDetailAgentAdapter
import com.sszt.cdslpetitionletter.adapter.CaseRecordAdapter
import com.sszt.cdslpetitionletter.databinding.ActivityCaseDetailBinding
import com.sszt.cdslpetitionletter.viewmodel.request.CaseRequestViewModel
import com.sszt.resources.IRoute

/**
 * 案件详情
 */
@Route(path = IRoute.case_detail)
class CaseDetailActivity : BaseActivity<PublicViewModel, ActivityCaseDetailBinding>() {


    override fun layoutId() = R.layout.activity_case_detail

    private val mAdp by lazy { CaseDetailAgentAdapter(arrayListOf()) }
    private val mAdpBy by lazy { CaseDetailAgentAdapter(arrayListOf()) }
    private val mAdpRecord by lazy { CaseRecordAdapter(arrayListOf()) }

    private val request by lazy { CaseRequestViewModel() }

    override fun initView(savedInstanceState: Bundle?) {
        bind.caseListTL.setOnBackClick { finish() }
        var uuid = intent.getStringExtra("uuid") ?: ""
        request.getCaseDetail(intent.getStringExtra("uuid") ?: "")

        bind.recyclerAgent.init(LinearLayoutManager(this), mAdp)
        bind.recyclerAgentBy.init(LinearLayoutManager(this), mAdpBy)
        bind.recyclerRecord.init(LinearLayoutManager(this), mAdpRecord)


        //予以受理
        bind.toAllow.setOnClickListener {
            if (uuid != null) {
                showLoading()
                request.acceptCase(
                    requestForBody(
                        "caseUuid" to uuid,
                        "processMode" to 2
                    )
                )
            }
        }
        //不予受理
        bind.toNotAllow.setOnClickListener {
            if (uuid != null) {
                showLoading()
                request.acceptCase(
                    requestForBody(
                        "caseUuid" to uuid,
                        "processMode" to 3
                    )
                )
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

        mAdpRecord.setOnItemClickListener { _, _, position ->
            router(IRoute.case_record_detail, "uuid" to mAdpRecord.data[position].uuid)
        }

        mAdp.addChildClickViewIds(R.id.addProposerAgentEntrustUp)
        mAdpBy.addChildClickViewIds(R.id.addProposerAgentEntrustUp)
        mAdp.setOnItemChildClickListener { adapter, view, position ->
            if (mAdp.data[position].commAttachment != null) {
                FileUtils.openFile(
                    this,
                    mAdp.data[position].commAttachment.title,
                    mAdp.data[position].commAttachment.fileUrl
                )
            }
        }
        mAdpBy.setOnItemChildClickListener { adapter, view, position ->
            if (mAdpBy.data[position].commAttachment != null) {
                FileUtils.openFile(
                    this,
                    mAdpBy.data[position].commAttachment.title,
                    mAdpBy.data[position].commAttachment.fileUrl
                )
            }
        }


        request.getCaseDetailResult.observe(this) {

            //申请人
            bind.addProposerType.text =
                if (it.applyType == 1) "自然人" else if (it.applyType == 2) "法人" else if (it.applyType == 3) "非法人组织" else ""

            bind.addProposerName.text = it.applyName
            if (it.applyType != 1) {
                bind.addProposerCL.visibility = View.VISIBLE
                bind.addProposerSocial.text = it.applyUsci
                bind.addProposerLegalPerson.text = it.applyLegalRepresentative
            }
            bind.addProposerPhone.text = it.applyPhone
            if (it.applyOtherType != 0) {
                bind.addProposerCall.text =
                    if (it.applyOtherType == 2) "QQ" else if (it.applyOtherType == 1) "邮箱" else if (it.applyOtherType == 3) "微信" else ""
                bind.addProposerCallValue.text = it.applyOtherNum
                bind.addProposerCallValueTip.text = bind.addProposerCall.textString() + ":"
                bind.addProposerCallValueTip.visibility = View.VISIBLE
                bind.addProposerCallValue.visibility = View.VISIBLE
                bind.addProposerCallValueLine.visibility = View.VISIBLE
            }
            bind.addProposerIDCard.text = it.applyIdentity
            bind.addProposerAddress.text = it.applyAddress


            //被申请人
            bind.addProposerTypeBy.text =
                if (it.respondentType == 1) "自然人" else if (it.respondentType == 2) "法人" else if (it.respondentType == 3) "非法人组织" else ""

            bind.addProposerNameBy.text = it.respondentName
            if (it.respondentType != 1) {
                bind.addProposerCLBy.visibility = View.VISIBLE
                bind.addProposerSocialBy.text = it.respondentUsci
                bind.addProposerLegalPersonBy.text = it.respondentLegalRepresentative
            }
            bind.addProposerPhoneBy.text = it.respondentPhone
            if (it.respondentOtherType != 0) {
                bind.addProposerCallBy.text =
                    if (it.respondentOtherType == 2) "QQ" else if (it.respondentOtherType == 1) "邮箱" else if (it.respondentOtherType == 3) "微信" else ""
                bind.addProposerCallValueBy.text = it.respondentOtherNum
                bind.addProposerCallValueTipBy.text = bind.addProposerCall.textString() + ":"
                bind.addProposerCallValueTipBy.visibility = View.VISIBLE
                bind.addProposerCallValueBy.visibility = View.VISIBLE
                bind.addProposerCallValueLineBy.visibility = View.VISIBLE
            }
            bind.addProposerIDCardBy.text = it.respondentIdentity
            bind.addProposerAddressBy.text = it.respondentAddress


            //代理人
            for (i in 0 until it.caseAgentList.size) {
                if (it.caseAgentList[i].agentSort == 1) {
                    mAdp.data.add(it.caseAgentList[i])
                } else if (it.caseAgentList[i].agentSort == 2) {
                    mAdpBy.data.add(it.caseAgentList[i])
                }
            }
            mAdp.notifyDataSetChanged()
            mAdpBy.notifyDataSetChanged()


            //矛盾纠纷信息
            bind.addProposerDisputeType.text = it.caseType
            bind.contradictionAddress.text = it.caseAddress
            bind.contradictionAddressInfo.text = it.caseStreet
            bind.contradictionTime.text = it.caseDate
            bind.contradictionInstitution.text = it.institutionName
            bind.contradictionDescribe.text = it.caseDesc


            bind.caseState.text =
                if (it.caseStatus == 1) "待受理" else if (it.caseStatus == 2) "待调查" else if (it.caseStatus == 3) "待调解" else if (it.caseStatus == 0) "调解中止" else ""

            if (it.caseStatus == 1) {
                bind.toAllowLL.visibility = View.VISIBLE
            }

            //显示业务文书
            if (it.caseRecordList.isNotEmpty()) {
                mAdpRecord.data.addAll(it.caseRecordList)
                mAdpRecord.notifyDataSetChanged()
                bind.writLine.visibility = View.VISIBLE
                bind.writLinear.visibility = View.VISIBLE
            }

            if (it.lastProcessMode == 5) {
                bind.caseDetailWrit.text = "人民调解协议书"
                bind.caseDetailWritTip.text = "人民调解协议书"
            } else if (it.lastProcessMode == 6) {
                bind.caseDetailWrit.text = "人民调解终止书"
                bind.caseDetailWritTip.text = "人民调解终止书"
            } else if (it.lastProcessMode == 3) {
                bind.writLine.visibility = View.VISIBLE
                bind.writLinear.visibility = View.VISIBLE
                bind.caseDetailWritLL.visibility = View.VISIBLE
                bind.caseDetailWrit.text = "不予受理告知书"
                bind.caseDetailWritTip.text = "不予受理告知书"
            }

            request.getCaseWrit(uuid)
        }

        request.getCaseWritResult.observe(this) {
            if (it != null) {
                bind.writLine.visibility = View.VISIBLE
                bind.writLinear.visibility = View.VISIBLE
                bind.caseDetailWritLL.visibility = View.VISIBLE

            }
        }

        bind.caseDetailWritLL.setOnClickListener {

            if(!bind.caseDetailWrit.textString().contains("人民调解")){
                return@setOnClickListener
            }
            router(
                IRoute.case_writ_detail,
                "uuid" to uuid,
                "type" to if (bind.caseDetailWrit.textString() == "人民调解协议书") 2 else if (bind.caseDetailWrit.textString() == "人民调解终止书") 1 else 0,
            )

        }


    }
}