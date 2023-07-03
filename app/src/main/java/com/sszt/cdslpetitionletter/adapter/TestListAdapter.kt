package com.sszt.cdslpetitionletter.adapter

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.TestListBean

class TestListAdapter (data: ArrayList<TestListBean>,type :String) :
    BaseQuickAdapter<TestListBean, BaseViewHolder>(
        R.layout.item_test_list_layout, data
    ) {

    var type = ""
    override fun convert(holder: BaseViewHolder, item: TestListBean) {
        holder.setText(R.id.itemEventDetailedReportName,item.one)
        holder.setText(R.id.itemEventDetailedReportTime,item.two)
        holder.setText(R.id.itemEventDetailedGridName,item.time)


        when (type) {
            "全县重点事件" -> {
                holder.getView<TextView>(R.id.itemEventDetailedFour).visibility = View.GONE
                holder.getView<TextView>(R.id.itemEventDetailedReportGo).visibility = View.GONE
            }
            "案件办理历程" -> {
                holder.setText(R.id.itemEventDetailedReportGo,item.three)
                if(item.four == ""){
                    holder.getView<TextView>(R.id.itemEventDetailedFour).visibility = View.GONE
                }
                holder.setText(R.id.itemEventDetailedFour,item.four)
            }
            "事项办理" -> {
                holder.setText(R.id.itemEventDetailedReportGo,item.three)
                if(item.four == ""){
                    holder.getView<TextView>(R.id.itemEventDetailedFour).visibility = View.GONE
                }
                holder.setText(R.id.itemEventDetailedFour,item.four)
            }
        }
    }

    init {
        this.type = type
    }

}
