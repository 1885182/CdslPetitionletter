package com.sszt.cdslpetitionletter.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.basis.ext.init
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.WorkbenchBean
import com.sszt.cdslpetitionletter.bean.WorkbenchChildBean

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
class WorkbenchAdapter(data: ArrayList<WorkbenchBean>) :
    BaseQuickAdapter<WorkbenchBean, BaseViewHolder>(
        R.layout.item_workbench_layout, data
    ) {

    private var onItemClick: OnItemClick? = null
    override fun convert(holder: BaseViewHolder, item: WorkbenchBean) {

        holder.setText(R.id.itemWorkbenchTv, item.name)


        val mAdp by lazy { WorkbenchChildAdapter(item.child) }
        val recyclerView = holder.getView<RecyclerView>(R.id.itemChileRV)
        recyclerView.init(
            object : GridLayoutManager(context, 3) {
                override fun canScrollVertically() = false
            }, mAdp
        )

        mAdp.setOnItemClickListener { _, _, position ->
            setOnItemClicks(
                item.child[position].path,
                item.child[position].name,
                item.child[position].url ?: ""
            )
        }


    }

    private fun setOnItemClicks(path: String, name: String, url: String) {
        onItemClick?.onClick(path, name, url)
    }

    fun setOnItemClicksListener(onItemClick: OnItemClick) {
        this.onItemClick = onItemClick
    }

    interface OnItemClick {
        fun onClick(path: String, name: String, url: String)
    }
}

class WorkbenchChildAdapter(data: ArrayList<WorkbenchChildBean>) :
    BaseQuickAdapter<WorkbenchChildBean, BaseViewHolder>(
        R.layout.item_workbench_child_layout, data
    ) {
    override fun convert(holder: BaseViewHolder, item: WorkbenchChildBean) {
        holder.setImageResource(R.id.itemWorkChildIv, item.pic)

        holder.setText(R.id.itemWorkChildTv, item.name)
    }
}