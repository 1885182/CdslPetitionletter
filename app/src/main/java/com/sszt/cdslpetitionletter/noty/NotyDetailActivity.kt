package com.sszt.cdslpetitionletter.noty

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivityNotyDetailBinding
import com.sszt.resources.IRoute

/**
 * 消息详情
 */
@Route(path = IRoute.message_detail)
class NotyDetailActivity : BaseActivity<PublicViewModel, ActivityNotyDetailBinding>() {


    override fun layoutId() = R.layout.activity_noty_detail

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }
        bind.title.text = intent.getStringExtra("title") ?: ""
        bind.time.text = intent.getStringExtra("time") ?: ""
        bind.desc.text = intent.getStringExtra("content") ?: ""
    }
}