package com.sszt.cdslpetitionletter.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivityTestBinding
import com.sszt.resources.IRoute

@Route(path = IRoute.test_test)
class TestActivity : BaseActivity<PublicViewModel,ActivityTestBinding>() {

    override fun layoutId() = R.layout.activity_test

    override fun initView(savedInstanceState: Bundle?) {

    }
}