package com.sszt.cdslpetitionletter.adapter

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.*

/**
 * <b>标题：</b>  帮扶记录列表适配器 <br>
 * <b>描述：</b>  <br>
 * <b>设计：</b>zcr<br>
 * <b>创建：</b>2023/5/18<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author zcr
 * @version V1.0.0
 */
class AssistRecordListAdapter(data: ArrayList<AssistRecordItemBean>) :
    BaseQuickAdapter<AssistRecordItemBean, BaseViewHolder>(
        R.layout.item_sentiment_list_layout, data
    ) {

    override fun convert(holder: BaseViewHolder, item: AssistRecordItemBean) {
        holder.setText(R.id.adapter_phone,item.createTime)
        holder.setText(R.id.adapter_name,item.personnelName)
        holder.setText(R.id.adapter_name_tip,"帮扶对象:")
        holder.setText(R.id.adapter_phone_tip,"帮扶时间:")

        holder.getView<TextView>(R.id.adapter_type).visibility = View.GONE
        holder.getView<TextView>(R.id.adapter_time).visibility = View.GONE
        holder.getView<TextView>(R.id.adapter_type_tip).visibility = View.GONE
        holder.getView<TextView>(R.id.adapter_time_tip).visibility = View.GONE
    }

}
