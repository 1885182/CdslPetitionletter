package com.sszt.cdslpetitionletter.cases

import android.content.Intent
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
import com.sszt.cdslpetitionletter.adapter.MediateInstitutionAdapter
import com.sszt.cdslpetitionletter.databinding.ActivityMediateInstitutionBinding
import com.sszt.cdslpetitionletter.viewmodel.request.CaseRequestViewModel
import com.sszt.resources.IRoute

/**
 * 调解机构
 */
@Route(path = IRoute.case_mediate_institution)
class MediateInstitutionActivity :
    BaseActivity<PublicViewModel, ActivityMediateInstitutionBinding>() {


    override fun layoutId() = R.layout.activity_mediate_institution

    private var pageIndex = 1
    private var isRefresh = true
    private var keyWord = ""

    private val mAdp by lazy { MediateInstitutionAdapter(arrayListOf()) }
    private val request by lazy { CaseRequestViewModel() }

    override fun initView(savedInstanceState: Bundle?) {

        bind.caseMediateTL.setOnBackClick { finish() }

        bind.recycler.init(LinearLayoutManager(this), mAdp)
        requestHttp()

        bind.smartRefresh.loadAndRefresh({
            pageIndex++
            isRefresh = false
            requestHttp()
        }, {
            pageIndex = 1
            isRefresh = true
            requestHttp()
        })

        request.getMediateListResult.observe(this) {
            loadListData(it, mAdp, bind.smartRefresh)
        }


        //选择机构返回数据
        mAdp.setOnItemClickListener{_,_,position->
            var intent = Intent().apply {
                putExtra("name", mAdp.data[position].institutionName?:"")
                putExtra("uuid", mAdp.data[position].uuid?:"")
            }
            setResult(2002,intent)
            finish()
        }

    }


    private fun requestHttp() {
        request.getMediateList(
            requestForBody(
                "pageNo" to pageIndex,
                "pageSize" to 10,
                "businessNumber" to keyWord,
            ), isRefresh,
            pageIndex
        )
    }
}