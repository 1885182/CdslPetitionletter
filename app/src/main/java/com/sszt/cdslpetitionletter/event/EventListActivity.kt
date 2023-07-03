package com.sszt.cdslpetitionletter.event

import android.content.Intent
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
import com.sszt.cdslpetitionletter.adapter.AssistRecordListAdapter
import com.sszt.cdslpetitionletter.adapter.EventListAdapter
import com.sszt.cdslpetitionletter.databinding.ActivityAssistRecordListBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.resources.IRoute


/**
 * 排查登记列表
 */
@Route(path = IRoute.event_list)
class EventListActivity : BaseActivity<PublicViewModel, ActivityAssistRecordListBinding>() {


    override fun layoutId() = R.layout.activity_assist_record_list

    private val request by lazy { MainRequestViewModel() }
    private var pageIndex = 1
    private var isRefresh = true
    private var keyWord = ""

    private val mAdp by lazy { EventListAdapter(arrayListOf()) }

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }
        bind.titleLayout.titleTitle.text = "排查登记"

        bind.recycler.init(LinearLayoutManager(this), mAdp)

        requestHttp()

        bind.caseSearchET.addTextChangedListener(object : CustomTextWatcher {
            override fun afterTextChanged(s: Editable?) {
                keyWord = bind.caseSearchET.textString()
            }
        })

        //创建
        bind.toInsert.setOnClickListener {
            routerResultCode(IRoute.event_insert, 2000)
        }


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

        request.getEventListResult.observe(this) {
            loadListData(it, mAdp, bind.smartRefresh)
        }

        //详情
        mAdp.setOnItemClickListener { _, _, position ->
            router(IRoute.event_detail,"uuid" to mAdp.data[position].uuid)
        }
    }

    private fun requestHttp() {
        request.getEventList(
            requestForBody(
                "pageIndex" to pageIndex,
                "pageSize" to 10,
                "partyName" to keyWord
            ), isRefresh,
            pageIndex
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2000 && resultCode == 1999) {
            pageIndex = 1
            isRefresh = true
            requestHttp()
        }
    }
}