package com.sszt.cdslpetitionletter.assist

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
import com.sszt.cdslpetitionletter.adapter.AssistPeopleListAdapter
import com.sszt.cdslpetitionletter.adapter.SentimentListAdapter
import com.sszt.cdslpetitionletter.databinding.ActivityAssistPeopleListBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.resources.IRoute

/**
 * 帮扶人员列表
 */
@Route(path = IRoute.assist_people_list)
class AssistPeopleListActivity : BaseActivity<PublicViewModel, ActivityAssistPeopleListBinding>() {


    override fun layoutId() = R.layout.activity_assist_people_list

    private val request by lazy { MainRequestViewModel() }
    private var pageIndex = 1
    private var isRefresh = true
    private var keyWord = ""

    private val mAdp by lazy { AssistPeopleListAdapter(arrayListOf()) }

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }

        bind.recycler.init(LinearLayoutManager(this), mAdp)

        val type = intent.getIntExtra("type", 0)

        requestHttp()

        //监听输入框
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

        request.getAssistPeopleListResult.observe(this) {
            loadListData(it, mAdp, bind.smartRefresh)
        }

        //列表点击
        mAdp.setOnItemClickListener { _, _, position ->
            if (type == 199) {
                setResult(1999, Intent().putExtra("uuid",mAdp.data[position].uuid).putExtra("name",mAdp.data[position].popName))
                finish()
            } else {
                router(IRoute.assist_people_detail, "uuid" to mAdp.data[position].uuid)
            }
        }
    }

    private fun requestHttp() {
        request.getAssistPeopleList(
            requestForBody(
                "pageIndex" to pageIndex,
                "pageSize" to 10,
                "popName" to keyWord
            ), isRefresh,
            pageIndex
        )
    }
}