package com.sszt.cdslpetitionletter.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.cdslpetitionletter.R

/**
 * <b>标题：</b>  工作台 <br>
 * <b>描述：</b>  <br>
 * <b>设计：</b>zcr<br>
 * <b>创建：</b>2023/5/9<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author zcr
 * @version V1.0.0
 */
class CaseInsertFileAdapter(data: ArrayList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(
        R.layout.item_case_insert_file_layout, data
    ) {

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.adapterCaseInsertName,item)

    }

}
