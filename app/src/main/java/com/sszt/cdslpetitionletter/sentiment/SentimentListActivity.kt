package com.sszt.cdslpetitionletter.sentiment

import android.os.Bundle
import android.text.Editable
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.ext.view.textString
import com.sszt.basis.util.CustomTextWatcher
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.SentimentListAdapter
import com.sszt.cdslpetitionletter.databinding.ActivitySentimentListBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.resources.IRoute

/**
 * 民情台账
 */
@Route(path = IRoute.sentiment_list)
class SentimentListActivity : BaseActivity<PublicViewModel,ActivitySentimentListBinding>() {


    override fun layoutId() = R.layout.activity_sentiment_list


    private val request by lazy { MainRequestViewModel() }
    private var pageIndex = 1
    private var isRefresh = true
    private var keyWord = ""


    private val mAdp by lazy { SentimentListAdapter(arrayListOf()) }

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }

        bind.recycler.init(LinearLayoutManager(this), mAdp)

        requestHttp()

        bind.caseSearchET.addTextChangedListener(object : CustomTextWatcher {
            override fun afterTextChanged(s: Editable?) {
                keyWord = bind.caseSearchET.textString()
            }
        })


        //搜索
        bind.toSearch.setOnClickListener {
            pageIndex = 1
            isRefresh = true
            requestHttp()
        }

        //刷新
        bind.smartRefresh.loadAndRefresh({
            pageIndex++
            isRefresh = false
            requestHttp()
        }, {
            pageIndex = 1
            isRefresh = true
            requestHttp()
        })

        request.getSentimentListResult.observe(this) {
            loadListData(it, mAdp, bind.smartRefresh)
        }

        mAdp.setOnItemClickListener { _, _, position ->
            router(IRoute.sentiment_detail,"uuid" to mAdp.data[position].uuid)
        }
    }

    private fun requestHttp() {
        request.getSentimentList(
            requestForBody(
                "pageNum" to pageIndex,
                "pageSize" to 10,
                "accountName" to keyWord
            ), isRefresh,
            pageIndex
        )
    }
}