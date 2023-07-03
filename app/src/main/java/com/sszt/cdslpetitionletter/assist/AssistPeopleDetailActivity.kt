package com.sszt.cdslpetitionletter.assist

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.requestForBody
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivityAssistPeopleDetailBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.resources.IRoute

/**
 * 人员帮扶详情
 */
@Route(path = IRoute.assist_people_detail)
class AssistPeopleDetailActivity : BaseActivity<PublicViewModel,ActivityAssistPeopleDetailBinding>() {

    override fun layoutId() = R.layout.activity_assist_people_detail
    private val request by lazy { MainRequestViewModel() }

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }

        request.getAssistPeopleDetail(requestForBody("uuid" to intent.getStringExtra("uuid")))

        request.getAssistPeopleDetailResult.observe(this){
            bind.peopleDetailName.text = it.popName
            bind.peopleDetailCardCode.text = it.cardType
            bind.peopleDetailIdCard.text = it.cardId
            bind.peopleDetailSex.text = it.popSex
            bind.peopleDetailNation.text = it.popNation
            bind.peopleDetailPhone.text = it.popPhone
            bind.peopleDetailHealth.text = it.popHealthy
            bind.peopleDetailEducation.text = it.popEducation
            bind.peopleDetailMarriage.text = it.popMarriage
            bind.peopleDetailLabour.text = it.popLabour
            bind.peopleDetailAddressInfo.text = it.popAddr
        }
    }
}