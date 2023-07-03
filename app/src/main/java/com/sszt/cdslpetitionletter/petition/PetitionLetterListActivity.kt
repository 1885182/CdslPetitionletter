package com.sszt.cdslpetitionletter.petition

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
import com.sszt.cdslpetitionletter.adapter.PetitionLetterListAdapter
import com.sszt.cdslpetitionletter.databinding.ActivityCaseListBinding
import com.sszt.cdslpetitionletter.viewmodel.request.PetitionLetterRequestViewModel
import com.sszt.resources.IRoute

/**
 * 信访列表
 */
@Route(path = IRoute.petition_letter_list)
class PetitionLetterListActivity : BaseActivity<PublicViewModel, ActivityCaseListBinding>() {


    override fun layoutId() = R.layout.activity_case_list

    private val request by lazy { PetitionLetterRequestViewModel() }
    private var pageIndex = 1
    private var isRefresh = true
    private var keyWord = ""
    private val mAdp by lazy { PetitionLetterListAdapter(arrayListOf(),intent.get("title", "信访办理")?:"") }

    override fun initView(savedInstanceState: Bundle?) {

        bind.caseListTL.setOnBackClick { finish() }
        bind.caseListTL.titleTitle.text = intent.get("title", "信访办理")



        bind.recycler.init(LinearLayoutManager(this), mAdp)



        mAdp.setOnItemClickListener { _, _, position ->
            if (intent.get("title", "信访办理") == "信访评价"){
                if (mAdp.data[position].replyState == "" || mAdp.data[position].replyState == null){
                    routerResultCode(
                        IRoute.letter_comment,
                        2000,
                        "type" to 1,
                        "xfAcceptInfoUuid" to mAdp.data[position].xfAcceptInfoUuid
                    )
                }else if(mAdp.data[position].replyState == "超期未评"){
                    toast("无评价数据")
                }else {
                    routerResultCode(
                        IRoute.letter_comment,
                        2000,
                        "type" to 2,
                        "xfAcceptInfoUuid" to mAdp.data[position].xfAcceptInfoUuid,
                        "applyerInfoUuid" to mAdp.data[position].xfApplyerInfoUuid,
                        "visitInfoUuid" to mAdp.data[position].xfVisitInfoUuid,
                    )
                }
                return@setOnItemClickListener
            }
            routerResultCode(
                IRoute.letter_detail,
                2000,
                "title" to intent.get("title", "信访办理"),
                "applyerInfoUuid" to mAdp.data[position].xfApplyerInfoUuid,
                "visitInfoUuid" to mAdp.data[position].xfVisitInfoUuid,
                "acceptInfoUuid" to mAdp.data[position].xfAcceptInfoUuid,
            )
        }
        requestHttp()

        request.getPetitionLetterListResult.observe(this) {
            loadListData(it, mAdp, bind.smartRefresh)
        }

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


    }

    private fun requestHttp() {
        request.getPetitionLetterList(
            intent.getStringExtra("url") ?: "",
            requestForBody(
                "pageIndex" to pageIndex,
                "pageSize" to 10,
                "businessNumber" to keyWord,
                "handleMode" to if (intent.get("title", "信访办理") == "信访办理" || intent.get(
                        "title",
                        "信访办理"
                    ) == "信访查询"
                ) "自办" else ""
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