package com.sszt.cdslpetitionletter.adapter

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.EventListItemBean

/**
 * <b>标题：</b>  排查登记列表适配器 <br>
 * <b>描述：</b>  <br>
 * <b>设计：</b>zcr<br>
 * <b>创建：</b>2023/5/19<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author zcr
 * @version V1.0.0
 */
class EventListAdapter(data: ArrayList<EventListItemBean>) :
    BaseQuickAdapter<EventListItemBean, BaseViewHolder>(
        R.layout.item_sentiment_list_layout, data
    ) {

    override fun convert(holder: BaseViewHolder, item: EventListItemBean) {
        holder.setText(R.id.adapter_name,item.popName)
        holder.setText(R.id.adapter_phone,item.caseName)
        holder.setText(R.id.adapter_type,item.caseScale)
        holder.setText(R.id.adapter_time,item.caseTime)
        holder.setText(R.id.adapter_name_tip,"当事人:")
        holder.setText(R.id.adapter_phone_tip,"事件名称:")
        holder.setText(R.id.adapter_type_tip,"事件规模:")
        holder.setText(R.id.adapter_time_tip,"登记时间:")
        holder.setText(R.id.adapter_service_number,"业务编号："+item.caseNum)

        holder.getView<TextView>(R.id.adapter_service_number).visibility = View.VISIBLE
        holder.getView<View>(R.id.adapter_line1).visibility = View.VISIBLE
    }

}
