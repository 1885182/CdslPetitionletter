package com.sszt.cdslpetitionletter.cases

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.ext.view.getHintStr
import com.sszt.basis.ext.view.isTrimEmpty
import com.sszt.basis.ext.view.textString
import com.sszt.basis.ext.view.textStringTrim
import com.sszt.basis.weight.DialogView
import com.sszt.basis.weight.TimeDialog
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.CaseRecordNameAdapter
import com.sszt.cdslpetitionletter.adapter.CaseRecordSIgnNameAdapter
import com.sszt.cdslpetitionletter.bean.CaseRecordNameBean
import com.sszt.cdslpetitionletter.databinding.ActivityCaseRecordWordBinding
import com.sszt.cdslpetitionletter.viewmodel.request.CaseRequestViewModel
import com.sszt.resources.IRoute

/**
 * 添加笔录
 */
@Route(path = IRoute.case_record_word)
class CaseRecordWordActivity : BaseActivity<PublicViewModel, ActivityCaseRecordWordBinding>() {

    override fun layoutId() = R.layout.activity_case_record_word

    private val request by lazy { CaseRequestViewModel() }

    private val mAdpSignName by lazy { CaseRecordSIgnNameAdapter(arrayListOf()) }

    private val mAdpName by lazy { CaseRecordNameAdapter(arrayListOf()) }

    var list = ArrayList<CaseRecordNameBean>()
    private var researchPersonSign: String = ""
    private var researchRespondentSign: String = ""
    private var researchRecorderSign: String = ""

    fun updatas(position: Int, value: String) {
        mAdpName.data[position].researchPerson = value
        list[position].researchPerson = value
    }


    override fun initView(savedInstanceState: Bundle?) {
        bind.caseListTL.setOnBackClick { finish() }

        bind.toCancel.setOnClickListener { finish() }

        val caseUuid = intent.getStringExtra("caseUuid")

        //调查人
        bind.recordWordNameSignTv.setOnClickListener {
            routerResultCode(
                IRoute.main_signature,
                1001
            )
        }
        //被调查人
        bind.recordWordNameSignByTv.setOnClickListener {
            routerResultCode(
                IRoute.main_signature,
                1002
            )
        }
        //记录人
        bind.recordWordRecordNameSignTv.setOnClickListener {
            routerResultCode(
                IRoute.main_signature,
                1003
            )
        }

        bind.recyclerNameSign.init(LinearLayoutManager(this), mAdpSignName)
        bind.recyclerName.init(LinearLayoutManager(this), mAdpName)

        //添加调查人
        bind.recordWordNameAddName.setOnClickListener {
            list.add(CaseRecordNameBean("", ""))
            mAdpName.data.add(CaseRecordNameBean("", ""))
            mAdpSignName.data.add(CaseRecordNameBean("", ""))
            mAdpName.notifyDataSetChanged()
            mAdpSignName.notifyDataSetChanged()
        }

        //删除调查人
        mAdpName.addChildClickViewIds(R.id.adapter_name_del)
        mAdpName.setOnItemChildClickListener { adapter, view, position ->
            if (R.id.adapter_name_del == view.id) {
                list.removeAt(position)
                mAdpName.data.removeAt(position)
                mAdpSignName.data.removeAt(position)
                mAdpName.notifyDataSetChanged()
                mAdpSignName.notifyDataSetChanged()
            }
        }

        mAdpSignName.addChildClickViewIds(R.id.adapter_sign_name_tv)
        mAdpSignName.setOnItemChildClickListener { adapter, view, position ->
            if (view.id == R.id.adapter_sign_name_tv) {
                routerResultCode(
                    IRoute.main_signature,
                    1004,
                    "index" to position
                )
            }
        }

        //选择日期
        bind.recordWordSurveyTimeStart.setOnClickListener {
            TimeDialog(
                this,
                "选择开始日期",
                booleanArrayOf(true, true, true, false, false, false),
                bind.recordWordSurveyTimeStart.textStringTrim()
            ) {
                if (it.time > System.currentTimeMillis()){
                    ToastUtils.showShort("选择时间不能大于当前时间")
                    return@TimeDialog
                }
                bind.recordWordSurveyTimeStart.text =
                    TimeUtils.date2String(it, DialogView.timeFormatDate)
            }.show(supportFragmentManager, "dialog")
        }
        //选择日期
        bind.recordWordSurveyTimeEnd.setOnClickListener {
            TimeDialog(
                this,
                "选择结束日期",
                booleanArrayOf(true, true, true, false, false, false),
                bind.recordWordSurveyTimeEnd.textStringTrim()
            ) {
                if (it.time > System.currentTimeMillis()){
                    ToastUtils.showShort("选择时间不能大于当前时间")
                    return@TimeDialog
                }
                bind.recordWordSurveyTimeEnd.text =
                    TimeUtils.date2String(it, DialogView.timeFormatDate)
            }.show(supportFragmentManager, "dialog")
        }

        //提交
        bind.toSave.setOnClickListener {
            when {
                caseUuid == null -> toast("数据异常,请稍后再试")
                bind.recordWordSurveyTimeStart.isTrimEmpty() -> toast("请选择开始日期")
                bind.recordWordSurveyTimeEnd.isTrimEmpty() -> toast("请选择结束日期")
                bind.recordWordAddress.isTrimEmpty() -> toast(bind.recordWordAddress.getHintStr())
                bind.recordWordName.isTrimEmpty() -> toast(bind.recordWordName.getHintStr())
                bind.recordWordNameBy.isTrimEmpty() -> toast(bind.recordWordNameBy.getHintStr())
                bind.recordWordRecordName.isTrimEmpty() -> toast(bind.recordWordRecordName.getHintStr())
                bind.recordWordRecordWord.isTrimEmpty() -> toast(bind.recordWordRecordWord.getHintStr())
                researchPersonSign.isEmpty() -> toast("请完成调查人签名")
                researchRespondentSign.isEmpty() -> toast("请完成被调查人签名")
                researchRecorderSign.isEmpty() -> toast("请完成记录人签名")

                else -> {
                    tipDialog()
                    request.addCaseRecord(
                        requestForBody(
                            "caseUuid" to caseUuid,
                            "researchDateStart" to bind.recordWordSurveyTimeStart.textString(),
                            "researchDateEnd" to bind.recordWordSurveyTimeEnd.textString(),
                            "researchAddress" to bind.recordWordAddress.textString(),
                            "researchPerson" to bind.recordWordName.textString(),
                            "researchRespondent" to bind.recordWordNameBy.textString(),
                            "researchRecorder" to bind.recordWordRecordName.textString(),
                            "researchRecord" to bind.recordWordRecordWord.textString(),
                            "researchPersonSign" to researchPersonSign,
                            "researchRespondentSign" to researchRespondentSign,
                            "researchRecorderSign" to researchRecorderSign,
                            "signList" to if(list.size == 0) null else list
                        )
                    )
                }
            }

        }

        request.addCaseRecordResult.observe(this) {
            if (it.code == 2000) {
                successDialog(it.msg) {
                    finish()
                }
            } else {
                errorDialog(it.msg)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 2000) {
            when (requestCode) {
                1001 -> {
                    researchPersonSign = data?.getStringExtra("file") ?: ""
                    Glide.with(this).load(data?.getStringExtra("file")).into(bind.recordWordNameSign)
                }
                1002 -> {
                    researchRespondentSign = data?.getStringExtra("file") ?: ""
                    Glide.with(this).load(data?.getStringExtra("file")).into(bind.recordWordNameSignBy)
                }
                1003 -> {
                    researchRecorderSign = data?.getStringExtra("file") ?: ""
                    Glide.with(this).load(data?.getStringExtra("file")).into(bind.recordWordRecordNameSign)
                }
                1004 -> {
                    list[data?.getIntExtra("index",0)?:0].researchRespondentSign = data?.getStringExtra("file") ?: ""
                    mAdpSignName.data[data?.getIntExtra("index",0)?:0].researchRespondentSign = data?.getStringExtra("file") ?: ""
                    mAdpSignName.notifyDataSetChanged()
                }
            }
        }
    }

}