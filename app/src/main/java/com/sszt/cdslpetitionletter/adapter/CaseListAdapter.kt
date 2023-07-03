package com.sszt.cdslpetitionletter.adapter

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.CaseListItemBean

/**
 * <b>标题：</b>  案件列表适配器 <br>
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
class CaseListAdapter(data: ArrayList<CaseListItemBean>) :
    BaseQuickAdapter<CaseListItemBean, BaseViewHolder>(
        R.layout.item_case_list_layout, data
    ) {

    override fun convert(holder: BaseViewHolder, item: CaseListItemBean) {
        holder.setText(R.id.adapter_service_number,"业务编号："+item.uuid)
        holder.setText(R.id.adapter_time,item.createDate)
        holder.setText(R.id.adapter_dispute_type,item.caseType)
        holder.setText(R.id.adapter_proposer_name_type,item.applyName)
        holder.setText(R.id.adapter_id_card,item.applyIdentity)
        holder.setText(R.id.adapter_mediate,item.caseOrganization)
        holder.setText(R.id.adapter_state,if (item.caseStatus == 1) "待受理" else if (item.caseStatus == 2) "待调查" else if (item.caseStatus == 3) "待调解" else if (item.caseStatus == 0) "调解中止" else "")


        if (item.caseStatus == 2){
            holder.getView<TextView>(R.id.adapter_to_finish).visibility = View.VISIBLE
            holder.getView<TextView>(R.id.adapter_to_record_word).visibility = View.VISIBLE
        }else if (item.caseStatus == 3){
            holder.getView<TextView>(R.id.adapter_to_agreement).visibility = View.VISIBLE
            holder.getView<TextView>(R.id.adapter_to_termination).visibility = View.VISIBLE
        }
        holder.setIsRecyclable(false)
    }
}
