package com.sszt.cdslpetitionletter.activity

import android.os.Bundle
import com.blankj.utilcode.util.SPUtils
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.router
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivityLoginBinding
import com.sszt.resources.IRoute

/**
 * 启动页
 */
class StartActivity : BaseActivity<PublicViewModel,ActivityLoginBinding>() {


    override fun layoutId() = R.layout.activity_start

    override fun initView(savedInstanceState: Bundle?) {


        //是否登录
        if (SPUtils.getInstance().getString("token").isEmpty()){
            router(IRoute.login_login)
        } else {
            router(IRoute.main_main)
        }
        finish()
    }
}