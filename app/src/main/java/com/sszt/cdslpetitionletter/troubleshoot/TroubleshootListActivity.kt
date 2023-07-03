package com.sszt.cdslpetitionletter.troubleshoot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.ext.view.textString
import com.sszt.basis.util.CustomTextWatcher
import com.sszt.basis.util.get
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.PetitionLetterListAdapter
import com.sszt.cdslpetitionletter.adapter.TroubleshootListAdapter
import com.sszt.cdslpetitionletter.databinding.ActivityTroubleshootListBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.cdslpetitionletter.viewmodel.request.TroubleshootRequestViewModel
import com.sszt.resources.IRoute

/**
 * 信访隐患排查-列表
 */

@Route(path = IRoute.troubleshooting_list)
class TroubleshootListActivity : BaseActivity<PublicViewModel, ActivityTroubleshootListBinding>() {


    override fun layoutId() = R.layout.activity_troubleshoot_list


    private val request by lazy { TroubleshootRequestViewModel() }
    private var pageIndex = 1
    private var isRefresh = true
    private var keyWord = ""

    private val mAdp by lazy {
        TroubleshootListAdapter(
            arrayListOf(),
            intent.get("title", "信访办理") ?: ""
        )
    }

    var title = ""
    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }
        title = intent.get("title", "风险等级评估") ?: "风险等级评估"
        bind.titleLayout.titleTitle.text = title

        bind.recycler.init(LinearLayoutManager(this), mAdp)

        if (title == "风险等级评估") {
            bind.toInsert.visibility = View.VISIBLE
        }


        requestHttp()

        bind.toInsert.setOnClickListener {
            routerResultCode(IRoute.troubleshooting_insert, 2000)
        }

        mAdp.setOnItemClickListener { adapter, view, position ->
            routerResultCode(
                IRoute.troubleshooting_detail,
                2000,
                "uuid" to mAdp.data[position].uuid
            )
        }

        //监听输入框
        bind.caseSearchET.addTextChangedListener(object : CustomTextWatcher {
            override fun afterTextChanged(s: Editable?) {
                keyWord = bind.caseSearchET.textString()
            }
        })

        mAdp.addChildClickViewIds(
            R.id.adapter_to_detail,
            R.id.adapter_to_assess_check,
            R.id.adapter_to_assess_detail,
            R.id.adapter_to_hang_apply,
            R.id.adapter_to_hang_detail,
            R.id.adapter_to_pick_apply,
            R.id.adapter_to_pick_detail,
            R.id.adapter_to_pick_check,
        )

        mAdp.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.adapter_to_detail -> {//详情
                    routerResultCode(
                        IRoute.troubleshooting_detail,
                        2000,
                        "uuid" to mAdp.data[position].uuid
                    )
                }
                R.id.adapter_to_assess_check -> {//评估审核
                    routerResultCode(
                        IRoute.assess_check,
                        2000,
                        "title" to "评估审核",
                        "uuid" to mAdp.data[position].uuid
                    )
                }
                R.id.adapter_to_assess_detail -> {//评估详情
                    routerResultCode(
                        IRoute.assess_detail,
                        2000,
                        "title" to "评估详情",
                        "uuid" to mAdp.data[position].uuid
                    )
                }
                R.id.adapter_to_hang_apply -> {//挂牌申请
                    routerResultCode(
                        IRoute.hang_detail, 2000, "type" to 1,
                        "uuid" to mAdp.data[position].uuid
                    )
                }
                R.id.adapter_to_hang_detail -> {//挂牌详情
                    routerResultCode(
                        IRoute.hang_detail, 2000, "type" to 2,
                        "uuid" to mAdp.data[position].uuid
                    )
                }
                R.id.adapter_to_pick_apply -> {//摘牌申请
                    routerResultCode(
                        IRoute.assess_check,
                        2000,
                        "title" to "摘牌申请",
                        "uuid" to mAdp.data[position].uuid
                    )
                }
                R.id.adapter_to_pick_detail -> {//摘牌详情
                    routerResultCode(
                        IRoute.pick_detail,
                        2000,
                        "title" to "摘牌详情",
                        "uuid" to mAdp.data[position].uuid
                    )
                }
                R.id.adapter_to_pick_check -> {//摘牌审核
                    routerResultCode(
                        IRoute.pick_check, 2000,
                        "uuid" to mAdp.data[position].uuid
                    )
                }
            }
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

        request.getTroubleshootListResult.observe(this) {
            loadListData(it, mAdp, bind.smartRefresh)
        }

    }

    private fun requestHttp() {
        request.getTroubleshootList(
            intent.getStringExtra("url") ?: "",
            requestForBody(
                "pageIndex" to pageIndex,
                "pageSize" to 10,
                "partyName" to keyWord,
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