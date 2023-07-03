package com.sszt.cdslpetitionletter.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.SpanUtils
import com.sszt.basis.base.fragment.BaseFragment
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.MsgListAdapter
import com.sszt.cdslpetitionletter.adapter.MsgNotyListAdapter
import com.sszt.cdslpetitionletter.databinding.FragmentMessageBinding
import com.sszt.cdslpetitionletter.databinding.FragmentMineBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.resources.IRoute
import org.greenrobot.eventbus.EventBus

/**
 * <b>标题：</b>  消息中心 <br>
 * <b>描述：</b> 消息中心 <br>
 * <b>设计：</b>zcr<br>
 * <b>创建：</b>2023/5/24 11:05<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author zcr
 * @version V1.0.0
 */
@Route(path = IRoute.message_fragment)
class MessageFragment : BaseFragment<PublicViewModel, FragmentMessageBinding>() {


    override fun layoutId() = R.layout.fragment_message
    private val request by lazy { MainRequestViewModel() }
    private var pageIndex = 1
    private var isRefresh = true

    private val mAdp by lazy { MsgListAdapter(arrayListOf()) }

    override fun initView(savedInstanceState: Bundle?) {

        BarUtils.addMarginTopEqualStatusBarHeight(bind.titleLayout)
        bind.msgNoty.setOnClickListener { router(IRoute.message_noty) }
        bind.recycler.init(LinearLayoutManager(activity), mAdp)
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

        val isCeShi = SPUtils.getInstance().getBoolean("ceshiyemian")
        if (isCeShi) {
            bind.linear.visibility = View.VISIBLE
            bind.msgNoty.visibility = View.GONE
            bind.dianji.setOnClickListener {
                bind.titleLayout.titleTitle.text = "通知公告"
                bind.linear.visibility = View.GONE
            }

        }

        mAdp.setOnItemClickListener { adapter, view, position ->
            mAdp.data[position].messageRead = "1"
            mAdp.notifyItemChanged(position)

            request.updateMsg(requestForBody(
                "uuid" to mAdp.data[position].uuid,
                "messageRead" to "1"
            ))


            router(
                IRoute.message_detail,
                "title" to mAdp.data[position].messageStatus,
                "content" to mAdp.data[position].message,
                "time" to mAdp.data[position].creatTime
            )
        }



        request.getMsgListResult.observe(this) {
            loadListData(it, mAdp, bind.smartRefresh)
        }

        request.updateMsgResult.observe(this){
            EventBus.getDefault().post("未读消息变更")
        }
    }

    private fun requestHttp() {
        request.getMsgList(
            requestForBody(
                "pageNum" to pageIndex,
                "pageSize" to 10,
            ), isRefresh,
            pageIndex
        )
    }
}