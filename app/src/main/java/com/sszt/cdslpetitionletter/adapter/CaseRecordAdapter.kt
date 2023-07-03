package com.sszt.cdslpetitionletter.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.*

/**
 * <b>标题：</b>  调查笔录 <br>
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
class CaseRecordAdapter(data: ArrayList<CaseRecordBean>) :
    BaseQuickAdapter<CaseRecordBean, BaseViewHolder>(
        R.layout.item_case_record_layout, data
    ) {

    override fun convert(holder: BaseViewHolder, item: CaseRecordBean) {
        holder.setText(R.id.adapter_content, "关于${item.researchRespondent}的调查记录")
        holder.setText(R.id.adapter_title, "调查笔录：")
    }

}
