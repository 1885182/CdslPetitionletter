package com.sszt.cdslpetitionletter.cases

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.router
import com.sszt.basis.ext.routerResultCode
import com.sszt.basis.ext.toast
import com.sszt.basis.util.get
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.bean.ByProposerBean
import com.sszt.cdslpetitionletter.bean.CaseAgentBean
import com.sszt.cdslpetitionletter.bean.ProposerBean
import com.sszt.cdslpetitionletter.databinding.ActivityCaseRegisterBinding
import com.sszt.resources.IRoute

/**
 * 案件登记
 */
@Route(path = IRoute.case_register)
class CaseRegisterActivity : BaseActivity<PublicViewModel, ActivityCaseRegisterBinding>() {

    private var beanProposer: ProposerBean? = null
    private var beanByProposer: ByProposerBean? = null
    private lateinit var listProposer: MutableList<CaseAgentBean>
    private lateinit var listByProposer: MutableList<CaseAgentBean>

    override fun layoutId() = R.layout.activity_case_register

    override fun initView(savedInstanceState: Bundle?) {
        ARouter.getInstance().inject(this)
        bind.caseRegisterTL.setOnBackClick { finish() }


        bind.toInsertProposer.setOnClickListener {
            routerResultCode(
                IRoute.case_insert_proposer,
                2001,
                "type" to 0
            )
        }
        bind.toInsertByProposer.setOnClickListener {
            routerResultCode(
                IRoute.case_insert_proposer,
                2002,
                "type" to 1
            )
        }
        bind.toNext.setOnClickListener {
            if (beanProposer == null) {
                toast("请添加申请人")
                return@setOnClickListener
            }
            if (beanByProposer == null) {
                toast("请添加被申请人")
                return@setOnClickListener
            }
            listProposer.addAll(listByProposer)
            routerResultCode(
                IRoute.case_insert,2000,
                "beanProposer" to beanProposer,
                "beanByProposer" to beanByProposer,
                "list" to listProposer
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == 2000) {
            when (requestCode) {
                2001 -> {
                    beanProposer = data?.getSerializableExtra("bean") as ProposerBean
                    listProposer = data?.getSerializableExtra("list") as MutableList<CaseAgentBean>
                    if (beanProposer != null) {
                        bind.addProposerRL.visibility = View.VISIBLE
                        bind.toInsertProposer.visibility = View.GONE
                        bind.addProposerType.text = beanProposer?.applyType
                        bind.addProposerName.text =
                            beanProposer?.applyName + "(" + beanProposer?.applyPhone + ")"
                    }
                }
                2002 -> {
                    beanByProposer = data?.getSerializableExtra("bean") as ByProposerBean
                    listByProposer =
                        data?.getSerializableExtra("list") as MutableList<CaseAgentBean>
                    if (beanByProposer != null) {
                        bind.addProposerAgentRL.visibility = View.VISIBLE
                        bind.toInsertByProposer.visibility = View.GONE
                        bind.addProposerAgentType.text = beanByProposer?.respondentType
                        bind.addProposerAgentName.text =
                            beanByProposer?.respondentName + "(" + beanByProposer?.respondentPhone + ")"
                    }
                }
            }
        }else if (resultCode == 1999 && requestCode == 2000){
            finish()
        }
    }
}