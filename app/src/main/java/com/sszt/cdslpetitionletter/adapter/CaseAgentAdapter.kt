package com.sszt.cdslpetitionletter.adapter

import android.text.Editable
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.basis.ext.view.textString
import com.sszt.basis.util.CustomTextWatcher
import com.sszt.basis.weight.EditTipView
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.CaseAgentBean
import com.sszt.cdslpetitionletter.cases.CaseInsertProposerActivity

/**
 * <b>标题：</b>  代理人 <br>
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
class CaseAgentAdapter(data: ArrayList<CaseAgentBean>) :
    BaseQuickAdapter<CaseAgentBean, BaseViewHolder>(
        R.layout.item_case_agent_layout, data
    ) {

    override fun convert(holder: BaseViewHolder, item: CaseAgentBean) {

        holder.setText(R.id.addProposerAgentTitle, "代理人${holder.adapterPosition + 1}信息")
        holder.setText(R.id.addProposerAgentType, item.agentType)
        holder.setText(R.id.addProposerEntrustType, item.agentEntrust)
        holder.setText(R.id.addProposerAgentType, item.agentType)
        holder.setText(R.id.addProposerAgentName, item.agentName)
        holder.setText(R.id.addProposerAgentPhone, item.agentPhone)
        holder.setText(R.id.addProposerAgentIDCard, item.agentIdentity)
        holder.setText(
            R.id.addProposerAgentRelation,
            item.agentRelation.replace("\"", "").replace("[", "").replace("]", "").replace(",", "-")
        )
        holder.setText(R.id.addProposerAgentEntrustUp, item.sss)

        holder.getView<EditTipView>(R.id.addProposerAgentName)
            .addTextChangedListener(object : CustomTextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    (context as CaseInsertProposerActivity).updatas(
                        holder.adapterPosition,
                        1,
                        holder.getView<EditTipView>(R.id.addProposerAgentName).textString()
                    )
                }
            })
        holder.getView<EditTipView>(R.id.addProposerAgentPhone)
            .addTextChangedListener(object : CustomTextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    (context as CaseInsertProposerActivity).updatas(
                        holder.adapterPosition,
                        2,
                        holder.getView<EditTipView>(R.id.addProposerAgentPhone).textString()
                    )
                }
            })
        holder.getView<EditTipView>(R.id.addProposerAgentIDCard)
            .addTextChangedListener(object : CustomTextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    (context as CaseInsertProposerActivity).updatas(
                        holder.adapterPosition,
                        3,
                        holder.getView<EditTipView>(R.id.addProposerAgentIDCard).textString()
                    )
                }
            })

    }

}
