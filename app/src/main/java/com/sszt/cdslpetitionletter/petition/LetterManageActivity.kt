package com.sszt.cdslpetitionletter.petition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.toast
import com.sszt.basis.ext.view.getHintStr
import com.sszt.basis.ext.view.isTrimEmpty
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivityLetterManageBinding
import com.sszt.resources.IRoute

/**
 * 转办/交办
 */
@Route(path = IRoute.letter_manage)
class LetterManageActivity : BaseActivity<PublicViewModel,ActivityLetterManageBinding>() {


    override fun layoutId() = R.layout.activity_letter_manage

    override fun initView(savedInstanceState: Bundle?) {
        val type = intent.getIntExtra("type", 0)
        if (type == 1){
            bind.titleLayout.titleTitle.text = "转办"
            bind.letterManageUnitTip.text = "承办单位"
            bind.letterManageUnit.hint = "请选择承办单位"
        }else if (type == 2){
            bind.titleLayout.titleTitle.text = "交办"
            bind.letterManageUnitTip.text = "交办单位"
            bind.letterManageUnit.hint = "请选择交办单位"
        }


        bind.toSave.setOnClickListener {
            when{
                bind.letterManageUnit.isTrimEmpty() -> toast(bind.letterManageUnit.getHintStr())
                bind.letterManageTitle.isTrimEmpty() -> toast(bind.letterManageTitle.getHintStr())
                bind.letterManageContent.isTrimEmpty() -> toast(bind.letterManageContent.getHintStr())
                else -> {}
            }
        }
    }
}