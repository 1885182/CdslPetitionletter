package com.sszt.cdslpetitionletter.cases

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
import com.sszt.basis.util.get
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.CaseListAdapter
import com.sszt.cdslpetitionletter.databinding.ActivityCaseListBinding
import com.sszt.cdslpetitionletter.viewmodel.request.CaseRequestViewModel
import com.sszt.resources.IRoute

/**
 * 案件列表(受理/调查/调解/归档)
 */
@Route(path = IRoute.case_list)
class CaseListActivity : BaseActivity<PublicViewModel, ActivityCaseListBinding>() {


    override fun layoutId() = R.layout.activity_case_list


    private val request by lazy { CaseRequestViewModel() }
    private var pageIndex = 1
    private var isRefresh = true
    private var keyWord = ""
    private var caseStatus = 0

    private val mAdp by lazy { CaseListAdapter(arrayListOf()) }

    override fun initView(savedInstanceState: Bundle?) {

        bind.caseListTL.setOnBackClick { finish() }
        bind.caseListTL.titleTitle.text = intent.get("title", "案件受理")

        when (intent.get("title", "案件受理")) {
            "案件受理" -> caseStatus = 1
            "案件调查" -> caseStatus = 2
            "案件调解" -> caseStatus = 3
            "立卷归档" -> caseStatus = 0
        }



        bind.recycler.init(LinearLayoutManager(this), mAdp)

        requestHttp()

        bind.caseSearchET.addTextChangedListener(object : CustomTextWatcher {
            override fun afterTextChanged(s: Editable?) {
                keyWord = bind.caseSearchET.textString()
            }
        })


        bind.toSearch.setOnClickListener {
            pageIndex = 1
            isRefresh = true
            requestHttp()
        }

        bind.smartRefresh.loadAndRefresh({
            pageIndex++
            isRefresh = false
            requestHttp()
        }, {
            pageIndex = 1
            isRefresh = true
            requestHttp()
        })

        mAdp.addChildClickViewIds(
            R.id.adapter_to_record_word,
            R.id.adapter_to_finish,
            R.id.adapter_to_agreement,
            R.id.adapter_to_termination
        )
        mAdp.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.adapter_to_record_word -> router(
                    IRoute.case_record_word,
                    "caseUuid" to mAdp.data[position].uuid
                )//笔录
                R.id.adapter_to_finish -> {//完成调查
                    showLoading()
                    request.acceptCase(
                        requestForBody(
                            "caseUuid" to mAdp.data[position].uuid,
                            "processMode" to 4
                        )
                    )
                }
                R.id.adapter_to_agreement -> routerResultCode(
                    IRoute.case_writ,
                    2000,
                    "uuid" to mAdp.data[position].uuid,
                    "type" to 2,
                )//人民调解协议书
                R.id.adapter_to_termination -> routerResultCode(
                    IRoute.case_writ,
                    2000,
                    "uuid" to mAdp.data[position].uuid,
                    "type" to 1
                )//人民调解终止书
            }
        }

        mAdp.setOnItemClickListener { _, _, position ->
            routerResultCode(IRoute.case_detail, 2000, "uuid" to mAdp.data[position].uuid)
        }

        request.getCaseResult.observe(this) {
            loadListData(it, mAdp, bind.smartRefresh)
        }


        request.acceptCaseResult.observe(this) {
            if (it.code == 2000) {
                successDialog("操作成功") {
                    pageIndex = 1
                    isRefresh = true
                    requestHttp()
                }
            } else {
                errorDialog(it.msg ?: "失败")
            }
        }


    }

    private fun requestHttp() {
        request.getCaseList(
            requestForBody(
                "pageNum" to pageIndex,
                "pageSize" to 10,
                "uuid" to keyWord,
                "caseStatus" to caseStatus,
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