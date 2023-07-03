package com.sszt.cdslpetitionletter.fragment

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.SPUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.sszt.basis.base.fragment.BaseFragment
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.router
import com.sszt.basis.ext.toast
import com.sszt.cdslpetitionletter.BuildConfig
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.FragmentMineBinding
import com.sszt.resources.IRoute

/**
 * <b>标题：</b>  个人中心 <br>
 * <b>描述：</b> 个人中心 <br>
 * <b>设计：</b>zcr<br>
 * <b>创建：</b>2023/5/24 11:05<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author zcr
 * @version V1.0.0
 */
@Route(path = IRoute.mine_fragment)
class MineFragment : BaseFragment<PublicViewModel, FragmentMineBinding>() {


    override fun layoutId() = R.layout.fragment_mine


    override fun initView(savedInstanceState: Bundle?) {

        if (BuildConfig.FLAVOR == "chanpin"){
            bind.minePhone.visibility = View.GONE
            bind.phoneLine.visibility = View.GONE
        }


        bind.name.text = SPUtils.getInstance().getString("name")
        Glide.with(this).load(SPUtils.getInstance().getString("imgUrl")).apply(RequestOptions.bitmapTransform(
            CircleCrop()
        )).error(com.sszt.moduleresources.R.mipmap.ic_avatar).into(bind.avatar)

        bind.avatar.setOnClickListener {  }

        //退出登录
        bind.mineLogout.setOnClickListener {
            SPUtils.getInstance().clear()
            ActivityUtils.finishAllActivities()
            router(IRoute.login_login)
        }

        bind.minePas.setOnClickListener { router(IRoute.update_pas) }
        bind.minePhone.setOnClickListener { router(IRoute.mine_update_phone) }
    }
}