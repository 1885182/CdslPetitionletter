package com.sszt.cdslpetitionletter.adapter

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.basis.bean.FileBean
import com.sszt.cdslpetitionletter.R

/**
 * <b>标题：</b>  帮扶记录新增文件适配器 <br>
 * <b>描述：</b>  <br>
 * <b>设计：</b>zcr<br>
 * <b>创建：</b>2023/5/18<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author zcr
 * @version V1.0.0
 */
class AssistRecordInsertFileAdapter(data: ArrayList<FileBean>) :
    BaseQuickAdapter<FileBean, BaseViewHolder>(
        R.layout.item_assist_record_detail_file_layout, data
    ) {

    override fun convert(holder: BaseViewHolder, item: FileBean) {
        holder.setText(R.id.recordDetailFile,item.title)
        holder.getView<ImageView>(R.id.adapter_del).visibility = View.VISIBLE
    }

}
