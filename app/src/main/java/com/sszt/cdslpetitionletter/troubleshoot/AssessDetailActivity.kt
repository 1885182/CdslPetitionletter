package com.sszt.cdslpetitionletter.troubleshoot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.requestForBody
import com.sszt.basis.ext.router
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivityAssessDetailBinding
import com.sszt.cdslpetitionletter.viewmodel.request.TroubleshootRequestViewModel
import com.sszt.resources.IRoute

/**
 * 评估详情
 */

@Route(path = IRoute.assess_detail)
class AssessDetailActivity : BaseActivity<PublicViewModel,ActivityAssessDetailBinding>() {

    override fun layoutId() = R.layout.activity_assess_detail
    val request by lazy { TroubleshootRequestViewModel() }

    var title = ""
    override fun initView(savedInstanceState: Bundle?) {
        title = intent.getStringExtra("title")?:""
        bind.titleLayout.titleTitle.text = title
        bind.titleLayout.setOnBackClick{finish()}

        if (title == "评估详情"){
            bind.dissolveMemoTip.visibility = View.GONE
            bind.dissolveMemo.visibility = View.GONE
        }

        request.getTroubleshootDetail(requestForBody("uuid" to intent.getStringExtra("uuid")))



        request.getTroubleshootDetailResult.observe(this){
            bind.dissolveXian.text = it.dissolveXian
            bind.dissolveOrg.text = it.dissolveOrg
            bind.dissolvePhone.text = it.dissolvePhone
            bind.dissolveCase.text = it.dissolveCase
            bind.dissolveName.text = it.dissolveName
            bind.dissolveOk.text = it.dissolveOk
            //bind.dissolveMemo.text = it.dissolveMemo
        }
    }
}