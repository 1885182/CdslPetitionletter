package com.sszt.cdslpetitionletter.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.*

/**
 * <b>标题：</b>  摘牌详情适配器 <br>
 * <b>描述：</b>  <br>
 * <b>设计：</b>zcr<br>
 * <b>创建：</b>2023/6/8<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author zcr
 * @version V1.0.0
 */
class PickDetailAdapter(data: ArrayList<TroubleshootGPBean>) :
    BaseQuickAdapter<TroubleshootGPBean, BaseViewHolder>(
        R.layout.item_pick_detail_layout, data
    ) {

    override fun convert(holder: BaseViewHolder, item: TroubleshootGPBean) {
        holder.setText(R.id.dissolveOrg, item.dissolveOrg)
        holder.setText(R.id.dissolveCase, item.dissolveCase)
        holder.setText(R.id.dissolveName, item.dissolveName)
        holder.setText(R.id.dissolvePhone, item.dissolvePhone)
        holder.setText(R.id.dissolveOk, item.dissolveOk)
        holder.setText(R.id.dissolveMemo, item.dissolveContent)
        if (item.zpFileList != null && item.zpFileList.size > 0) {
            holder.setText(R.id.dissolveFile, item.zpFileList[item.zpFileList.size - 1].title)
        }


        holder.setText(R.id.pickTime, item.examineTime)
        holder.setText(R.id.pickCheck, item.examineState)
        holder.setText(R.id.checkOpinion, item.examineContent)
        holder.setText(R.id.checkName, item.examineName)
    }

}
