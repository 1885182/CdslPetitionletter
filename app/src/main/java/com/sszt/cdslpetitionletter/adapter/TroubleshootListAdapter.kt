package com.sszt.cdslpetitionletter.adapter

import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.SPUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.PetitionLetterListItemBean
import com.sszt.cdslpetitionletter.bean.TroubleshootListItemBean

/**
 * <b>标题：</b>  信访隐患排查适配器 <br>
 * <b>描述：</b>  <br>
 * <b>设计：</b>zcr<br>
 * <b>创建：</b>2023/6/2<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author zcr
 * @version V1.0.0
 */
class TroubleshootListAdapter(data: ArrayList<TroubleshootListItemBean>, title: String) :
    BaseQuickAdapter<TroubleshootListItemBean, BaseViewHolder>(
        R.layout.item_troubleshoot_list_layout,
        data
    ) {

    var title = ""

    override fun convert(holder: BaseViewHolder, item: TroubleshootListItemBean) {

        holder.setText(R.id.adapter_service_number, "业务编号：" + item.caseNum)
        holder.setText(R.id.adapter_name, item.popName)
        holder.setText(R.id.adapter_event_name, item.caseName)
        holder.setText(R.id.adapter_event_scale, item.caseScale)
        holder.setText(R.id.adapter_time, item.caseTime)


        when (title) {
            "风险等级评估" -> {
                holder.setText(R.id.adapter_state, item.dissolveOk)
                holder.setText(R.id.adapter_state_tip, "是否化解:")
            }
            "评估审核" -> {
                holder.setText(R.id.adapter_state, item.examineState)
                holder.setText(R.id.adapter_state_tip, "状态:")
                holder.getView<TextView>(R.id.adapter_to_assess_check).visibility = View.VISIBLE
            }
            "风险挂牌" -> {
                holder.getView<TextView>(R.id.adapter_state).visibility = View.GONE
                holder.getView<TextView>(R.id.adapter_state_tip).visibility = View.GONE
                holder.getView<TextView>(R.id.adapter_to_assess_detail).visibility = View.VISIBLE
                holder.getView<TextView>(R.id.adapter_to_hang_apply).visibility = View.VISIBLE
            }
            "风险摘牌" -> {
                holder.setText(R.id.adapter_state, item.examineState)
                holder.setText(R.id.adapter_state_tip, "状态:")
                holder.getView<TextView>(R.id.adapter_to_assess_detail).visibility = View.VISIBLE
                holder.getView<TextView>(R.id.adapter_to_hang_detail).visibility = View.VISIBLE
                holder.getView<TextView>(R.id.adapter_to_pick_apply).visibility = View.VISIBLE
            }
            "摘牌审核" -> {
                holder.setText(R.id.adapter_state, item.examineState)
                holder.setText(R.id.adapter_state_tip, "状态:")
                holder.getView<TextView>(R.id.adapter_to_assess_detail).visibility = View.VISIBLE
                holder.getView<TextView>(R.id.adapter_to_hang_detail).visibility = View.VISIBLE
                holder.getView<TextView>(R.id.adapter_to_pick_detail).visibility = View.VISIBLE
                holder.getView<TextView>(R.id.adapter_to_pick_check).visibility = View.VISIBLE
            }
            "台账查询" -> {
                holder.getView<TextView>(R.id.adapter_state).visibility = View.GONE
                holder.getView<TextView>(R.id.adapter_state_tip).visibility = View.GONE
                holder.getView<TextView>(R.id.adapter_to_assess_detail).visibility = View.VISIBLE
                holder.getView<TextView>(R.id.adapter_to_hang_detail).visibility = View.VISIBLE
                holder.getView<TextView>(R.id.adapter_to_pick_detail).visibility = View.VISIBLE
            }
            else -> {
                holder.getView<TextView>(R.id.adapter_state).visibility = View.GONE
                holder.getView<TextView>(R.id.adapter_state_tip).visibility = View.GONE
            }
        }

    }

    init {
        this.title = title
    }

}
