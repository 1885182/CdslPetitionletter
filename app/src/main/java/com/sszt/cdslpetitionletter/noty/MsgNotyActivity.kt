package com.sszt.cdslpetitionletter.noty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.init
import com.sszt.basis.ext.loadAndRefresh
import com.sszt.basis.ext.loadListData
import com.sszt.basis.ext.requestForBody
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.MsgNotyListAdapter
import com.sszt.cdslpetitionletter.databinding.ActivityMsgNotyBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.resources.IRoute

/**
 * 通知公告
 */
@Route(path = IRoute.message_noty)
class MsgNotyActivity : BaseActivity<PublicViewModel, ActivityMsgNotyBinding>() {


    override fun layoutId() = R.layout.activity_msg_noty


    private val request by lazy { MainRequestViewModel() }
    private var pageIndex = 1
    private var isRefresh = true

    private val mAdp by lazy { MsgNotyListAdapter(arrayListOf()) }

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }


        bind.recycler.init(LinearLayoutManager(this),mAdp)
        requestHttp()
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

        request.getMsgNotyListResult.observe(this){
            loadListData(it, mAdp, bind.smartRefresh)
        }
    }

    private fun requestHttp() {
        request.getMsgNotyList(
            requestForBody(
                "pageIndex" to pageIndex,
                "pageSize" to 10,
            ), isRefresh,
            pageIndex
        )
    }
}