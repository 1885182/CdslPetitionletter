package com.sszt.cdslpetitionletter.assist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.init
import com.sszt.basis.ext.requestForBody
import com.sszt.basis.ext.router
import com.sszt.basis.util.chat.FileUtils
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.AssistRecordDetailFileAdapter
import com.sszt.cdslpetitionletter.databinding.ActivityAssistPeopleDetailBinding
import com.sszt.cdslpetitionletter.databinding.ActivityAssistRecordDetailBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.resources.IRoute

/**
 * 帮扶记录详情
 */
@Route(path = IRoute.assist_record_detail)
class AssistRecordDetailActivity : BaseActivity<PublicViewModel, ActivityAssistRecordDetailBinding>() {

    override fun layoutId() = R.layout.activity_assist_people_detail
    private val request by lazy { MainRequestViewModel() }
    private val mAda by lazy { AssistRecordDetailFileAdapter(arrayListOf()) }

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }

        bind.recyclerFile.init(LinearLayoutManager(this),mAda)

        request.getAssistRecordDetail(requestForBody("uuid" to intent.getStringExtra("uuid")))

        request.getAssistRecordDetailResult.observe(this){
            bind.recordDetailName.text = it.personnelName
            bind.recordDetailTime.text = it.helpTime
            bind.recordDetailAddress.text = it.helpAddrName
            bind.recordDetailMeasures.text = it.helpMeasures
            bind.recordDetailEffect.text = it.helpEffect
            if (it.helpFileList != null && it.helpFileList.size > 0) {
                mAda.data.addAll(it.helpFileList)
                mAda.notifyDataSetChanged()
            }
            val split = it.helpAddr.split(",")

            if (split.size < 2){
                return@observe
            }

            //展示位置
            bind.recordDetailAddress.setOnClickListener {
                router(IRoute.map_show_loc, "lng" to split[0], "lat" to split[1])
            }
        }

        mAda.setOnItemClickListener { adapter, view, position ->
            FileUtils.openFile(this,mAda.data[position].title,mAda.data[position].fileUrl)
        }

    }
}