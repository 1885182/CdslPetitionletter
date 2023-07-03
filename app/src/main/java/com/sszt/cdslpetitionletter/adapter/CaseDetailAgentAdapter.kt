package com.sszt.cdslpetitionletter.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.CaseAgentBeans

/**
 * <b>标题：</b>  详情代理人 <br>
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
class CaseDetailAgentAdapter(data: ArrayList<CaseAgentBeans>) :
    BaseQuickAdapter<CaseAgentBeans, BaseViewHolder>(
        R.layout.item_case_detail_agent_layout, data
    ) {

    override fun convert(holder: BaseViewHolder, item: CaseAgentBeans) {

        holder.setText(R.id.addProposerAgentTitle, "代理人${holder.adapterPosition + 1}信息")
        holder.setText(
            R.id.addProposerAgentType,
            if (item.agentType == 1) "委托代理人" else if (item.agentType == 2) "法定/指定代理人" else ""
        )
        holder.setText(
            R.id.addProposerEntrustType,
            if (item.agentEntrust == 1) "一般代理人" else if (item.agentEntrust == 2) "特被授权代理人" else if (item.agentEntrust == 3) "未成年人" else if (item.agentEntrust == 4) "无民事行为能力" else ""
        )
        holder.setText(R.id.addProposerAgentName, item.agentName)
        holder.setText(R.id.addProposerAgentPhone, item.agentPhone)
        holder.setText(R.id.addProposerAgentIDCard, item.agentIdentity)
        holder.setText(
            R.id.addProposerAgentRelation,
            item.agentRelation.replace("\"", "").replace("[", "").replace("]", "").replace(",", "-")
        )
        holder.setText(R.id.addProposerAgentEntrustUp, item.commAttachment?.title ?: "")


    }

}
