package com.sszt.cdslpetitionletter.cases

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivityCaseWritDetailBinding
import com.sszt.cdslpetitionletter.viewmodel.request.CaseRequestViewModel
import com.sszt.resources.IRoute

/**
 * 案件文书详情
 */
@Route(path = IRoute.case_writ_detail)
class CaseWritDetailActivity : BaseActivity<PublicViewModel, ActivityCaseWritDetailBinding>() {


    override fun layoutId() = R.layout.activity_case_writ_detail



    private val request by lazy { CaseRequestViewModel() }

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }
        var type = intent.getIntExtra("type", 0)
        var uuid = intent.getStringExtra("uuid") ?: ""
        if (type == 1) {
            bind.titleLayout.titleTitle.text = "人民调解终止书"
            bind.addProposerName.visibility = View.GONE
            bind.addProposerNameTip.visibility = View.GONE
            bind.addPopulationTypeLine.visibility = View.GONE
            bind.addPopulationNameTip.text = "正文："
        } else if (type == 2) {
            bind.titleLayout.titleTitle.text = "人民调解协议书"
        }
        request.getCaseWrit(uuid)

        request.getCaseWritResult.observe(this){
            if (it != null){
                bind.addProposerType.text = it.writCode
                bind.addProposerName.text = it.writItems
                bind.addProposerPhone.text = it.writText
                Glide.with(this).load(it.applySign).into(bind.addProposerCallValue)
                Glide.with(this).load(it.respondentSign).into(bind.addProposerIDCard)
            }
        }
    }
}