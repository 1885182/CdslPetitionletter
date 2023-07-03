package com.sszt.cdslpetitionletter.troubleshoot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.init
import com.sszt.basis.ext.requestForBody
import com.sszt.basis.ext.toast
import com.sszt.basis.util.chat.FileUtils
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.PickDetailAdapter
import com.sszt.cdslpetitionletter.databinding.ActivityPickDetailBinding
import com.sszt.cdslpetitionletter.viewmodel.request.TroubleshootRequestViewModel
import com.sszt.resources.IRoute

/**
 * 摘牌详情
 */
@Route(path = IRoute.pick_detail)
class PickDetailActivity : BaseActivity<PublicViewModel, ActivityPickDetailBinding>() {


    override fun layoutId() = R.layout.activity_pick_detail
    val request by lazy { TroubleshootRequestViewModel() }
    val mAdp by lazy { PickDetailAdapter(arrayListOf()) }


    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }

        bind.recycler.init(LinearLayoutManager(this),mAdp)

        //获取数据
        request.getTroubleshootDetail(requestForBody("uuid" to intent.getStringExtra("uuid")))



        //展示数据
        request.getTroubleshootDetailResult.observe(this){
            if (it.zpList != null && it.zpList.size > 0){
                mAdp.setList(it.zpList)
            }else{
                toast("暂未提交摘牌信息")
            }
        }

        //
        mAdp.addChildClickViewIds(R.id.dissolveFile)
        mAdp.setOnItemChildClickListener { adapter, view, position ->
            FileUtils.openFile(this,mAdp.data[position].zpFileList?.last()?.title,mAdp.data[position].zpFileList?.last()?.fileUrl,)
        }

    }
}