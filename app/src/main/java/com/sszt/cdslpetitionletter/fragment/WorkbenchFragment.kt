package com.sszt.cdslpetitionletter.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.SPUtils
import com.sszt.basis.base.fragment.BaseFragment
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.network.Url
import com.sszt.cdslpetitionletter.BuildConfig
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.WorkbenchAdapter
import com.sszt.cdslpetitionletter.bean.WorkbenchBean
import com.sszt.cdslpetitionletter.bean.WorkbenchChildBean
import com.sszt.cdslpetitionletter.databinding.FragmentWorkbenchBinding
import com.sszt.resources.IRoute

/**
 * <b>标题：</b>  工作管理 <br>
 * <b>描述：</b> 工作管理 <br>
 * <b>设计：</b>zcr<br>
 * <b>创建：</b>2023/5/12 11:05<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author mengwenyue
 * @version V1.0.0
 */
@Route(path = IRoute.workbench_fragment)
class WorkbenchFragment : BaseFragment<PublicViewModel, FragmentWorkbenchBinding>() {


    private val mAdp by lazy { WorkbenchAdapter(arrayListOf()) }


    /**
     * <b>标题：</b> 获取布局 <br>
     * <b>描述：</b> <br>
     * <b>创建：</b>2023/5/15 19:09<br>
     * <b>更新：</b>时间： 更新人： 更新内容：
     * @param
     * @return
     * @author mengwenyue
     * @version V1.0.0
     */
    override fun layoutId() = R.layout.fragment_workbench

    /**
     * <b>标题：</b> 初始化view操作 <br>
     * <b>描述：</b> <br>
     * <b>创建：</b>2023/5/15 19:09<br>
     * <b>更新：</b>时间： 更新人： 更新内容：
     * @param
     * @return
     * @author mengwenyue
     * @version V1.0.0
     */
    override fun initView(savedInstanceState: Bundle?) {


        BarUtils.addMarginTopEqualStatusBarHeight(bind.titleLayout)
        bind.workbenchRv.init(LinearLayoutManager(activity), mAdp)

        var onclick = object : WorkbenchAdapter.OnItemClick {

            override fun onClick(path: String, name: String, url: String) {
                if (path == null || path == "") return
                router(path, "title" to name, "url" to url)
            }
        }
        mAdp.setOnItemClicksListener(onclick)


        mAdp.setList(
            arrayListOf(
                WorkbenchBean(
                    "信访业务", arrayListOf(
                        WorkbenchChildBean(
                            "信访办理",
                            com.sszt.moduleresources.R.mipmap.ic_work_xf_bl,
                            IRoute.petition_letter_list,
                            Url.agent + "applets/xfInfoQuery/getPageList"
                        ),
                        WorkbenchChildBean(
                            "信访查询",
                            com.sszt.moduleresources.R.mipmap.ic_work_xf_cx,
                            IRoute.petition_letter_list,
                            Url.agent + "applets/xfInfoQuery/getPageList"
                        ),
                        WorkbenchChildBean(
                            "作废申请",
                            com.sszt.moduleresources.R.mipmap.ic_work_zf_sq,
                            IRoute.petition_letter_list,
                            Url.agent + "applets/cancel/getPageList"
                        ),
                        WorkbenchChildBean(
                            "作废审核",
                            com.sszt.moduleresources.R.mipmap.ic_work_zf_sh,
                            IRoute.petition_letter_list,
                            Url.agent + "applets/cancel/getPageListShenHe"
                        ),
                        WorkbenchChildBean(//
                            "复查申请",
                            com.sszt.moduleresources.R.mipmap.ic_work_fc_sq,
                            IRoute.petition_letter_list,
                            Url.agent + "applets/fucha/getPageListToReviewCheck"
                        ),
                        WorkbenchChildBean(
                            "复查审核",
                            com.sszt.moduleresources.R.mipmap.ic_work_fc_sh,
                            IRoute.petition_letter_list,
                            Url.agent + "applets/fucha/getPageListToReviewCheckShenHe"
                        ),
                        WorkbenchChildBean(
                            "复核申请",
                            com.sszt.moduleresources.R.mipmap.ic_work_fh_sq,
                            IRoute.petition_letter_list,
                            Url.agent + "applets/fuhe/getPageList"
                        ),
                        WorkbenchChildBean(
                            "复核审核",
                            com.sszt.moduleresources.R.mipmap.ic_work_fh_sh,
                            IRoute.petition_letter_list,
                            Url.agent + "applets/fuhe/getPageListShenHe"
                        ),
                        WorkbenchChildBean(
                            "信访评价",
                            com.sszt.moduleresources.R.mipmap.ic_work_xf_pj,
                            IRoute.petition_letter_list,
                            Url.agent + "applet/reply/getPageList"
                        )
                    )
                ),
                WorkbenchBean(
                    "综治调解", arrayListOf(
                        WorkbenchChildBean(
                            "案件登记",
                            com.sszt.moduleresources.R.mipmap.ic_work_aj_dj,
                            IRoute.case_register,
                        ),
                        WorkbenchChildBean(
                            "案件受理",
                            com.sszt.moduleresources.R.mipmap.ic_work_aj_sl,
                            IRoute.case_list,
                        ),
                        WorkbenchChildBean(
                            "案件调查",
                            com.sszt.moduleresources.R.mipmap.ic_work_aj_dc,
                            IRoute.case_list,
                        ),
                        WorkbenchChildBean(
                            "案件调解",
                            com.sszt.moduleresources.R.mipmap.ic_work_aj_tj,
                            IRoute.case_list,
                        ),
                        WorkbenchChildBean(
                            "立卷归档",
                            com.sszt.moduleresources.R.mipmap.ic_work_lj_gd,
                            IRoute.case_list,
                        )
                    )
                ),
                WorkbenchBean(
                    "民情收集业务", arrayListOf(
                        WorkbenchChildBean(
                            "民情收集",
                            com.sszt.moduleresources.R.mipmap.ic_work_mq_sj,
                            IRoute.sentiment_insert,
                        ),
                        WorkbenchChildBean(
                            "民情台账",
                            com.sszt.moduleresources.R.mipmap.ic_work_mq_tz,
                            IRoute.sentiment_list,
                        )
                    )
                ),



                )
        )

        if (BuildConfig.FLAVOR == "xiangmu") {
            mAdp.data.add(
                WorkbenchBean(
                    "信访隐患排查", arrayListOf(
                        WorkbenchChildBean(
                            "风险等级评估",
                            com.sszt.moduleresources.R.mipmap.ic_work_fx_dj_pg,
                            IRoute.troubleshooting_list,
                            Url.agent + "screening/registration/getPageList"
                        ),
                        WorkbenchChildBean(
                            "评估审核",
                            com.sszt.moduleresources.R.mipmap.ic_work_pg_sh,
                            IRoute.troubleshooting_list,
                            Url.agent + "screening/registration/getPgshList"
                        ),
                        WorkbenchChildBean(
                            "风险挂牌",
                            com.sszt.moduleresources.R.mipmap.ic_work_fx_gp,
                            IRoute.troubleshooting_list,
                            Url.agent + "screening/registration/getFxgpList"
                        ),
                        WorkbenchChildBean(
                            "风险摘牌",
                            com.sszt.moduleresources.R.mipmap.ic_work_fx_zp,
                            IRoute.troubleshooting_list,
                            Url.agent + "screening/registration/getFxzpList"
                        ),
                        WorkbenchChildBean(
                            "摘牌审核",
                            com.sszt.moduleresources.R.mipmap.ic_work_zp_sh,
                            IRoute.troubleshooting_list,
                            Url.agent + "screening/registration/getZpshList"
                        ),
                        WorkbenchChildBean(
                            "台账查询",
                            com.sszt.moduleresources.R.mipmap.ic_work_tz_cx,
                            IRoute.troubleshooting_list,
                            Url.agent + "screening/registration/getPageList"
                        )
                    )
                )
            )
            mAdp.notifyDataSetChanged()
        }else if (BuildConfig.FLAVOR == "chanpin"){
            mAdp.data.add(
                WorkbenchBean(
                    "人员帮扶业务", arrayListOf(
                        WorkbenchChildBean(
                            "重点人员帮扶",
                            com.sszt.moduleresources.R.mipmap.ic_work_zd_bf,
                            IRoute.assist_people_list,
                        ),
                        WorkbenchChildBean(
                            "帮扶记录",
                            com.sszt.moduleresources.R.mipmap.ic_work_bf_jl,
                            IRoute.assist_record_list,
                        )
                    )
                )
            )
            mAdp.data.add(
                WorkbenchBean(
                    "信访隐患排查业务", arrayListOf(
                        WorkbenchChildBean(
                            "排查登记",
                            com.sszt.moduleresources.R.mipmap.ic_work_pc_dj,
                            IRoute.event_list,
                        )
                    )
                )
            )
            mAdp.notifyDataSetChanged()
        }

        if (SPUtils.getInstance().getBoolean("ceshiyemian")) {

            mAdp.data.add(
                WorkbenchBean(
                    "新加页面", arrayListOf(
                        WorkbenchChildBean(
                            "提交",
                            com.sszt.moduleresources.R.mipmap.ic_work_zd_bf,
                            IRoute.view_add,
                        ),
                        WorkbenchChildBean(
                            "案件办理历程",
                            com.sszt.moduleresources.R.mipmap.ic_work_bf_jl,
                            IRoute.view_list,
                        ),
                        WorkbenchChildBean(
                            "全县重点事件",
                            com.sszt.moduleresources.R.mipmap.ic_work_bf_jl,
                            IRoute.view_list,
                        ),
                        WorkbenchChildBean(
                            "事项办理",
                            com.sszt.moduleresources.R.mipmap.ic_work_bf_jl,
                            IRoute.view_list,
                        )
                    )
                )
            )
            mAdp.notifyDataSetChanged()
        }


    }
}