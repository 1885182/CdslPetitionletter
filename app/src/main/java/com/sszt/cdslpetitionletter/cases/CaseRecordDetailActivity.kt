package com.sszt.cdslpetitionletter.cases

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.init
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.CaseRecordDetailNameAdapter
import com.sszt.cdslpetitionletter.databinding.ActivityCaseRecordDetailBinding
import com.sszt.cdslpetitionletter.viewmodel.request.CaseRequestViewModel
import com.sszt.resources.IRoute

/**
 * 笔录详情
 */
@Route(path = IRoute.case_record_detail)
class CaseRecordDetailActivity : BaseActivity<PublicViewModel, ActivityCaseRecordDetailBinding>() {


    override fun layoutId() = R.layout.activity_case_record_detail

    private val request by lazy { CaseRequestViewModel() }

    private val mAda by lazy { CaseRecordDetailNameAdapter(arrayListOf()) }

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }
        request.getCaseRecord(intent.getStringExtra("uuid") ?: "")

        bind.recyclerName.init(LinearLayoutManager(this),mAda)

        request.getCaseRecordResult.observe(this) {
            bind.addProposerType.text = it.researchDate
            bind.addProposerName.text = it.researchAddress
            bind.addProposerSocial.text = it.researchPerson
            bind.addProposerLegalPerson.text = it.researchRespondent

            bind.addProposerPhone.text = it.researchRecorder
            bind.addProposerCall.text = it.researchRecord

            Glide.with(this).load(it.researchPersonSign).into(bind.addProposerCallValue)
            Glide.with(this).load(it.researchRespondentSign).into(bind.addProposerIDCard)
            Glide.with(this).load(it.researchRecorderSign).into(bind.addProposerAddress)

            mAda.data.addAll(it.signList)
            mAda.notifyDataSetChanged()

        }

    }
}