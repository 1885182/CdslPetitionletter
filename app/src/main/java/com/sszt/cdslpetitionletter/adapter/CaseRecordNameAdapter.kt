package com.sszt.cdslpetitionletter.adapter

import android.text.Editable
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.basis.ext.view.textString
import com.sszt.basis.util.CustomTextWatcher
import com.sszt.basis.weight.EditTipView
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.CaseRecordNameBean
import com.sszt.cdslpetitionletter.cases.CaseRecordWordActivity

/**
 * <b>标题：</b>  调查笔录-调查人 <br>
 * <b>描述：</b>  <br>
 * <b>设计：</b>zcr<br>
 * <b>创建：</b>2023/5/15<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author zcr
 * @version V1.0.0
 */
class CaseRecordNameAdapter(data: ArrayList<CaseRecordNameBean>) :
    BaseQuickAdapter<CaseRecordNameBean, BaseViewHolder>(
        R.layout.item_case_record_name_layout, data
    ) {

    override fun convert(holder: BaseViewHolder, item: CaseRecordNameBean) {
        holder.setText(R.id.adapter_name,item.researchPerson)
        holder.setText(R.id.adapter_name_tip,"调查人${holder.adapterPosition + 2}：")
        holder.getView<EditTipView>(R.id.adapter_name)
            .addTextChangedListener(object : CustomTextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    (context as CaseRecordWordActivity).updatas(
                        holder.adapterPosition,
                        holder.getView<EditTipView>(R.id.adapter_name).textString()
                    )
                }
            })
    }
}
