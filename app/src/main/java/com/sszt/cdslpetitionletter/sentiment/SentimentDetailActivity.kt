package com.sszt.cdslpetitionletter.sentiment

import android.os.Bundle
import android.os.FileUtils
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.requestForBody
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivitySentimentDetailBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.resources.IRoute

/**
 * 民情详情
 */
@Route(path = IRoute.sentiment_detail)
class SentimentDetailActivity : BaseActivity<PublicViewModel, ActivitySentimentDetailBinding>() {


    override fun layoutId() = R.layout.activity_sentiment_detail

    private val request by lazy { MainRequestViewModel() }

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }

        request.getSentimentDetail(requestForBody("uuid" to intent.getStringExtra("uuid")))

        request.getSentimentDetailResult.observe(this) {
            var sentimentInsertTown = ""
            if (it.streetTown != null) {
                for (i in 0 until it.streetTown.size) {
                    sentimentInsertTown += it.streetTown[i]
                }
            }

            bind.sentimentInsertName.text = it.accountName
            bind.sentimentInsertPhone.text = it.phoneNum
            bind.sentimentInsertAddressInfo.text = it.detailedAddress
            bind.sentimentInsertType.text = it.accountType
            bind.sentimentInsertTown.text = sentimentInsertTown
            bind.sentimentInsertVillage.text = it.villageCommunity
            bind.sentimentInsertDescribe.text = it.helpContent

            bind.sentimentInsertFile.text = it.commAttachment.title
            bind.sentimentInsertFile.setOnClickListener { it1->
                com.sszt.basis.util.chat.FileUtils.openFile(this,it.commAttachment.title,it.commAttachment.fileUrl)
            }
        }

    }
}