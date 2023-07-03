package com.sszt.cdslpetitionletter.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.FileUtils
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.download.downFileEx
import com.sszt.basis.ext.download.downloadDialogEx
import com.sszt.basis.ext.download.getDownloadFilePath
import com.sszt.basis.ext.init
import com.sszt.basis.util.OpenFileUtils
import com.sszt.basis.weight.DialogView
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.TestListAdapter
import com.sszt.cdslpetitionletter.bean.TestListBean
import com.sszt.cdslpetitionletter.databinding.ActivityTestListBinding
import com.sszt.resources.IRoute

@Route(path = IRoute.view_list)
class TestListActivity : BaseActivity<PublicViewModel, ActivityTestListBinding>() {


    private val mAda by lazy {
        TestListAdapter(
            arrayListOf(),
            intent.getStringExtra("title") ?: ""
        )
    }

    override fun layoutId() = R.layout.activity_test_list

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }
        bind.recycler.init(LinearLayoutManager(this), mAda)

        if (intent.getStringExtra("title") == "案件办理历程") {
            bind.titleLayout.titleTitle.text = "案件办理历程"
            mAda.setList(
                arrayListOf(
                    TestListBean(
                        "2023-04-13 14:44:32",
                        "操作人：杜怀友",
                        "办理内容：道路两旁被挖开无人管",
                        "信访状态：受理",
                        ""
                    ),
                    TestListBean(
                        "2023-04-13 15:10:00",
                        "操作人：赵爱国",
                        "办理类型：转办",
                        "转办意见：请及时处理",
                        "信访状态：待处理"
                    ),
                    TestListBean(
                        "2023-04-13 16:00:00",
                        "操作人：朱文瑞",
                        "办理类型：自办",
                        "评价情况：已安排专业施工队，对道路两旁进行填补",
                        "信访状态：处理"
                    ),
                    TestListBean(
                        "2023-04-14 17:00:00",
                        "操作人：王晓伟",
                        "办理类型：复核",
                        "评价情况：确认，允许结案！",
                        "信访状态：完结"
                    ),
                    TestListBean(
                        "2023-04-14 17:30:00",
                        "操作人：杜怀友",
                        "办理类型：评价",
                        "满意度：非常满意",
                        "评价情况：非常满意，相关单位就解决了此事效率特别好"
                    ),
                )
            )

            mAda.setOnItemClickListener { adapter, view, position ->
                if (position == 3) {
                    DialogView.showConfirmNotCancelBtnDialog(
                        content = "调查情况：经调查，村里两条道路两侧被挖开，已经挖开几个月，至今还无人管。\n处理意见：此事关系群众切身利益，已安排专业施工队，妥善处理",
                        title = "提示",
                        confirmText = "确认"
                    ) {

                    }
                }
            }
            /**
             * 时间：2023-03-09 14:37:27
            操作人：杜怀友
            核查情况：完成的很好
            信访状态：评价

            时间：2023-03-09 10:05:58
            操作人：朱文瑞
            办理类型：自办
            评价情况：已完成处理
            信访状态：已完结

            时间：2023-03-08 17:15:22
            操作人：王晓伟
            办理类型：自办
            信访状态：待处理

            时间：2023-03-08 17:00:00
            操作人：李银柱
            办理类型：上报
            上报内容：机动车交通事故责任纠纷
             */
        } else if (intent.getStringExtra("title") == "事项办理") {
            bind.titleLayout.titleTitle.text = "事项办理"
            mAda.setList(
                arrayListOf(
                    TestListBean(
                        "2023-03-09 14:37:27",
                        "操作人：杜怀友",
                        "核查情况：完成的很好",
                        "信访状态：评价",
                        ""
                    ),
                    TestListBean(
                        "2023-03-09 10:05:58",
                        "操作人：朱文瑞",
                        "办理类型：自办",
                        "评价情况：已完成处理",
                        "信访状态：已完结"
                    ),
                    TestListBean(
                        "2023-03-08 17:15:22",
                        "操作人：王晓伟",
                        "办理类型：自办",
                        "信访状态：待处理",
                        ""
                    ),
                    TestListBean(
                        "2023-03-08 17:00:00",
                        "操作人：李银柱",
                        "办理类型：上报",
                        "上报内容：机动车交通事故责任纠纷",
                        ""
                    ),
                )
            )


        } else if (intent.getStringExtra("title") == "全县重点事件") {
            bind.titleLayout.titleTitle.text = "全县重点事件"

            mAda.setList(
                arrayListOf(
                    TestListBean(
                        "2023-04-20 17:00:25",
                        "投诉标题：村委会在村民不知情的情况下强买强卖",
                        "事件详情：焦庄村村委会在村民不知情的情况下强买强卖，晚上把村民种的即将成熟玉米用农机给粉碎拉",
                        "调查情况：当事人反映确有其事，要及时反馈处理情况，并做好满意度评价工作。",
                        "处理意见：综治中心1号管理员已与当事人电话沟通，当事人系袁庄村人，已妥善处理。"
                    ),
                    TestListBean(
                        "2023-04-09 10:13:41",
                        "投诉标题：村民养老保险",
                        "事件详情：王某养老保险绑定微信上一直没有收到保险金到账。",
                        "调查情况：打印流水发放正常",
                        "调查情况：打印流水发放正常"
                    ),
                    TestListBean(
                        "2023-03-30 16:02:08",
                        "投诉标题：宅基地矛盾纠纷",
                        "事件详情：侯某与胡某宅基地矛盾纠纷",
                        "调查情况：侯某与胡某因宅基地边界问题发生矛盾纠纷",
                        "处理意见：已调解双方就宅基地问题达成一致"
                    ),
                    TestListBean(
                        "2023-03-15 07:42:21",
                        "投诉标题：参战退伍军人待遇问题",
                        "事件详情：袁秋长，越南自卫反击战退伍军人，政策性待遇都已到位但其提出要求办理低保，否则联系其他战友一起找信访局。",
                        "调查情况：不符合申请办理低保条件，袁秋长不应该去上访",
                        "处理意见：讲政策，政策内所有待遇已到位，其有孩子且生活质量较高，不应该申请办理低保"
                    ),
                    TestListBean(
                        "2023-03-09 11:20:25",
                        "投诉标题：违建厂房内生产小米",
                        "事件详情：乔堡村东违建厂房内生产小米，环保部门不管吗？夜里生产，希望领导落实",
                        "调查情况：此事件情况属实，正在办理中",
                        "处理意见：感谢您的关注和支持，此件正在办理中，系多条重复反映，经审核，予以办结。请做出满意度评价。"
                    ),
                )
            )

            mAda.setOnItemClickListener { adapter, view, position ->
                DialogView.showConfirmNotCancelBtnDialog(
                    content = mAda.data[position].three + "\n" + mAda.data[position].four,
                    title = "提示",
                    confirmText = "确认"
                ) {

                }

            }

        }

/*
*
* 时间：2023-04-13 14:44:32
操作人：杜怀友
办理内容：道路两旁被挖开无人管
信访状态：受理

时间：2023-04-13 15:10:00
操作人：赵爱国
办理类型：转办
转办意见：请及时处理
信访状态：待处理

时间：2023-04-13 16:00:00
操作人：朱文瑞
办理类型：自办
评价情况：已安排专业施工队，对道路两旁进行填补
信访状态：处理

时间：2023-04-14 17:00:00
操作人：王晓伟
办理类型：复核
评价情况：确认，允许结案！
信访状态：完结

时间：2023-04-14 17:30:00
操作人：杜怀友
办理类型：评价
满意度：非常满意
评价情况：非常满意，相关单位就解决了此事效率特别好
* */

    }
}