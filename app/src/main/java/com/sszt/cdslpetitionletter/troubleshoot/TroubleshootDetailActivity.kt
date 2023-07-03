package com.sszt.cdslpetitionletter.troubleshoot

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.requestForBody
import com.sszt.basis.ext.router
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivityTroubleshootDetailBinding
import com.sszt.cdslpetitionletter.viewmodel.request.TroubleshootRequestViewModel
import com.sszt.resources.IRoute

/**
 * 信访隐患排查-详情
 */
@Route(path = IRoute.troubleshooting_detail)
class TroubleshootDetailActivity :
    BaseActivity<PublicViewModel, ActivityTroubleshootDetailBinding>() {


    override fun layoutId() = R.layout.activity_troubleshoot_detail
    val request by lazy { TroubleshootRequestViewModel() }

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }


        request.getTroubleshootDetail(requestForBody("uuid" to intent.getStringExtra("uuid")))

        request.getTroubleshootDetailResult.observe(this){
            bind.eventName.text = it.caseName
            bind.eventType.text = it.caseType
            bind.eventScale.text = it.caseScale
            bind.eventSource.text = it.caseSource
            bind.eventTime.text = it.caseTime
            bind.eventNum.text = it.unitsNum.toString()
            bind.eventUnit.text = it.unitsInvolved
            bind.eventAddress.text = it.placeOccurrenceName
            bind.eventContent.text = it.caseMemo
            bind.cardType.text = it.cardType
            bind.cardNum.text = it.cardNum
            bind.popName.text = it.popName
            bind.popSex.text = it.popSex
            bind.popNation.text = it.popNation
            bind.popEducation.text = it.popEducation
            bind.peopleType.text = it.popType
            bind.popAddr.text = it.popAddr

            val split = it.placeOccurrence.split(",")

            if (split.size < 2){
                return@observe
            }
            bind.eventAddress.setOnClickListener {
                router(IRoute.map_show_loc, "lng" to split[0], "lat" to split[1])
            }
        }
    }
}