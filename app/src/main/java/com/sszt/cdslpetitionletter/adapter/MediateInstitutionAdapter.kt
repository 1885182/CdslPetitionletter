package com.sszt.cdslpetitionletter.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.ListBean

/**
 * <b>标题：</b>  调解机构适配器 <br>
 * <b>描述：</b>  <br>
 * <b>设计：</b>zcr<br>
 * <b>创建：</b>2023/5/10<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author zcr
 * @version V1.0.0
 */
class MediateInstitutionAdapter(data: ArrayList<ListBean>) :
    BaseQuickAdapter<ListBean, BaseViewHolder>(
        R.layout.item_mediate_institution_layout, data
    ) {

    override fun convert(holder: BaseViewHolder, item: ListBean) {
        holder.setText(R.id.adapter_mediate_name,item.institutionName?:"")
        holder.setText(R.id.adapter_mediate_address,item.institutionAddress?:"")
    }

}
