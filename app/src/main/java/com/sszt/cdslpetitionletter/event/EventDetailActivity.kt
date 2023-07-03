package com.sszt.cdslpetitionletter.event

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.init
import com.sszt.basis.ext.requestForBody
import com.sszt.basis.ext.router
import com.sszt.basis.ext.view.isTrimEmpty
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.AssistRecordDetailFileAdapter
import com.sszt.cdslpetitionletter.databinding.ActivityAssistRecordDetailBinding
import com.sszt.cdslpetitionletter.databinding.ActivityEventDetailBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.resources.IRoute

/**
 * 排查登记详情
 */
@Route(path = IRoute.event_detail)
class EventDetailActivity : BaseActivity<PublicViewModel, ActivityEventDetailBinding>() {

    override fun layoutId() = R.layout.activity_assist_people_detail
    private val request by lazy { MainRequestViewModel() }

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }


        request.getEventDetail(requestForBody("uuid" to intent.getStringExtra("uuid")))

        request.getEventDetailResult.observe(this){
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
            bind.popAddr.text = it.popAddr
            bind.popName.text = it.popName
            bind.popSex.text = it.popSex
            bind.popNation.text = it.popNation
            bind.popEducation.text = it.popEducation
            bind.peopleType.text = it.popType
            bind.dissolveXian.text = it.dissolveXian
            bind.dissolveOrg.text = it.dissolveOrg
            bind.dissolvePhone.text = it.dissolvePhone
            bind.dissolveCase.text = it.dissolveCase
            bind.dissolveName.text = it.dissolveName
            bind.dissolveOk.text = it.dissolveOk
            bind.dissolveMemo.text = it.dissolveMemo

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