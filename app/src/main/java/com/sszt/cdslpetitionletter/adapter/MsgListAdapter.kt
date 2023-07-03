package com.sszt.cdslpetitionletter.adapter

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.MsgBean

/**
 * <b>标题：</b>  消息中心 <br>
 * <b>描述：</b>  <br>
 * <b>设计：</b>zcr<br>
 * <b>创建：</b>2023/5/11<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author zcr
 * @version V1.0.0
 */
class MsgListAdapter(data: ArrayList<MsgBean>) :
    BaseQuickAdapter<MsgBean, BaseViewHolder>(
        R.layout.item_msg_noty_layout, data
    ) {

    override fun convert(holder: BaseViewHolder, item: MsgBean) {

        holder.setText(R.id.adapter_title, item.messageStatus)
        holder.setText(R.id.adapter_content, item.message)
        holder.setText(R.id.adapter_time, item.creatTime)
        holder.getView<ImageView>(R.id.adapter_is_read).visibility = if (item.messageRead == "0") View.VISIBLE else View.GONE
    }

}
