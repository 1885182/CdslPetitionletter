package com.sszt.cdslpetitionletter.troubleshoot

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.errorDialog
import com.sszt.basis.ext.requestForBody
import com.sszt.basis.ext.successDialog
import com.sszt.basis.ext.toast
import com.sszt.basis.ext.view.getHintStr
import com.sszt.basis.ext.view.isTrimEmpty
import com.sszt.basis.ext.view.textString
import com.sszt.basis.util.Utils
import com.sszt.basis.weight.DataDialog
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivityPickCheckBinding
import com.sszt.cdslpetitionletter.viewmodel.request.TroubleshootRequestViewModel
import com.sszt.resources.IRoute

/**
 * 摘牌审核
 */
@Route(path = IRoute.pick_check)
class PickCheckActivity : BaseActivity<PublicViewModel, ActivityPickCheckBinding>() {


    override fun layoutId() = R.layout.activity_pick_check

    private var listPickCheck = ArrayList<String>()
    private val request by lazy { TroubleshootRequestViewModel() }

    var uuid = ""

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }

        listPickCheck.addAll(arrayListOf("通过", "驳回"))


        //摘牌审核
        bind.pickCheck.setOnClickListener {
            DataDialog(this, listPickCheck) {
                bind.pickCheck.text = listPickCheck[it]
            }.show(supportFragmentManager, "dialogData")
        }

        request.getTroubleshootDetail(requestForBody("uuid" to intent.getStringExtra("uuid")))



        request.getTroubleshootDetailResult.observe(this) {
            if (it.zpList?.isNotEmpty() == true) {
                uuid = it.zpList.last().uuid
            } else {
                toast("暂未提交摘牌信息")
                finish()
            }
        }

        //保存
        bind.toSave.setOnClickListener {
            when {
                bind.pickCheck.isTrimEmpty() -> toast(bind.pickCheck.getHintStr())
                bind.checkOpinion.isTrimEmpty() -> toast(bind.checkOpinion.getHintStr())
                else -> {
                    request.insertPickCheck(
                        requestForBody(
                            "uuid" to uuid,
                            "screeningUuid" to intent.getStringExtra("uuid"),
                            "examineState" to bind.pickCheck.textString(),
                            "examineContent" to bind.checkOpinion.textString()
                        )
                    )
                }
            }
        }

        request.insertPickCheckResult.observe(this) {
            if (it.code == 2000) {
                successDialog(it.msg) {
                    setResult(1999)
                    finish()
                }
            } else {
                errorDialog(it.msg)
            }
        }


    }
}