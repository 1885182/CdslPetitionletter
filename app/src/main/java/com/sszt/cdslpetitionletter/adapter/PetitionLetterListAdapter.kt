package com.sszt.cdslpetitionletter.adapter

import com.blankj.utilcode.util.SPUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.PetitionLetterListItemBean

/**
 * <b>标题：</b>  信访列表适配器 <br>
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
class PetitionLetterListAdapter(data: ArrayList<PetitionLetterListItemBean>, title: String) :
    BaseQuickAdapter<PetitionLetterListItemBean, BaseViewHolder>(
        R.layout.item_petition_letter_list_layout,
        data
    ) {

    var title = ""

    override fun convert(holder: BaseViewHolder, item: PetitionLetterListItemBean) {

        holder.setText(R.id.adapter_service_number, "业务编号：" + item.businessNumber)
        holder.setText(R.id.adapter_dispute_type, item.problemFenlei)
        holder.setText(R.id.adapter_proposer_name_type, item.applyName)
        holder.setText(R.id.adapter_id_card, item.idCard)
        holder.setText(R.id.adapter_mediate, item.phone)
        holder.setText(R.id.adapter_state, item.handleStatus)
        holder.setText(R.id.adapter_time, item.createTime.substring(0, 10))

        if (SPUtils.getInstance().getBoolean("ceshiyemian")){
            holder.setVisible(R.id.adapter_jia_tip,true)
            holder.setVisible(R.id.adapter_jia,true)
        }

        if (title == "信访评价") {
            holder.setText(R.id.adapter_state, item.replyState ?: "待评价")
            holder.setText(R.id.adapter_state_tip, "评价状态")
        }

    }

    init {
        this.title = title
    }

}
