package com.sszt.cdslpetitionletter.cases

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.ext.view.getHintStr
import com.sszt.basis.ext.view.isTrimEmpty
import com.sszt.basis.ext.view.textString
import com.sszt.basis.ext.view.textStringTrim
import com.sszt.basis.util.GetFilePathFromUri
import com.sszt.basis.util.GetJsonDataUtil
import com.sszt.basis.util.PickerUtils
import com.sszt.basis.util.chat.FileUtils
import com.sszt.basis.util.get
import com.sszt.basis.weight.DataDialog
import com.sszt.basis.weight.DialogView
import com.sszt.basis.weight.TimeDialog
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.CaseInsertFileAdapter
import com.sszt.cdslpetitionletter.bean.ByProposerBean
import com.sszt.cdslpetitionletter.bean.CaseAgentBean
import com.sszt.cdslpetitionletter.bean.ProposerBean
import com.sszt.cdslpetitionletter.databinding.ActivityCaseInsertBinding
import com.sszt.cdslpetitionletter.viewmodel.request.CaseRequestViewModel
import com.sszt.resources.IRoute
import java.io.File

/**
 * 案件登记-提交
 */
@Route(path = IRoute.case_insert)
class CaseInsertActivity : BaseActivity<PublicViewModel, ActivityCaseInsertBinding>() {


    override fun layoutId() = R.layout.activity_case_insert

    private val request by lazy { CaseRequestViewModel() }

    private val mAdp by lazy { CaseInsertFileAdapter(arrayListOf()) }

    private lateinit var beanProposer: ProposerBean
    private lateinit var beanByProposer: ByProposerBean
    private lateinit var listAgent: MutableList<CaseAgentBean>

    private val listDisputeType = ArrayList<String>()
    private var listAddress = ArrayList<PickerUtils.RailroadGuardAreaListBean>()

    var caseOrganization = ""

    private var lat: String = ""
    private var lng: String = ""

    override fun initView(savedInstanceState: Bundle?) {

        bind.caseInsertTL.setOnBackClick { finish() }

        initData()

        //bind.recyclerFile.init(LinearLayoutManager(this), mAdp)

        beanByProposer = intent.getSerializableExtra("beanByProposer") as ByProposerBean
        beanProposer = intent.getSerializableExtra("beanProposer") as ProposerBean
        listAgent = intent?.getSerializableExtra("list") as MutableList<CaseAgentBean>

        lng = SPUtils.getInstance().getString("mCurrentLon")
        lat = SPUtils.getInstance().getString("mCurrentLat")
        bind.caseInsertAddressInfo.text = SPUtils.getInstance().getString("mAddress")

        mAdp.addChildClickViewIds(R.id.adapterCaseInsertName, R.id.adapterCaseInsertDel)
        mAdp.setOnItemChildClickListener { ada, view, position ->
            if (view.id == R.id.adapterCaseInsertName) {
                toast("打开")
            } else if (view.id == R.id.adapterCaseInsertDel) {
                mAdp.data.removeAt(position)
                mAdp.notifyDataSetChanged()
            }
        }

        //选择类型
        bind.caseInsertDisputeType.setOnClickListener {
            DataDialog(this, listDisputeType) {
                bind.caseInsertDisputeType.text = listDisputeType[it]
            }.show(supportFragmentManager, "dialogData")
        }


        //选择地址
        bind.caseInsertAddressInfo.setOnClickListener {
            routerResultCode(
                IRoute.map_select,
                20,
                "lat" to lat,
                "lng" to lng
            )
        }


        //选择日期
        bind.caseInsertTime.setOnClickListener {
            TimeDialog(
                this,
                "选择日期",
                booleanArrayOf(true, true, true, true, true, false),
                bind.caseInsertTime.textStringTrim()
            ) {
                if (it.time > System.currentTimeMillis()){
                    ToastUtils.showShort("选择时间不能大于当前时间")
                    return@TimeDialog
                }
                bind.caseInsertTime.text = TimeUtils.date2String(it, DialogView.timeFormat24)
            }.show(supportFragmentManager, "dialog")
        }


        //选择地址
        bind.caseInsertAddress.setOnClickListener {
            PickerUtils.showRailroadGuardArea(
                this,
                listAddress
            ) {
                bind.caseInsertAddress.text = it[0] + "/" + it[1] + "/" + it[2]
            }
        }

        //选择机构
        bind.caseInsertInstitution.setOnClickListener {
            routerResultCode(IRoute.case_mediate_institution, 2001)
        }

        //选择附件
        /*bind.addFile.setOnClickListener {
            if (mAdp.data.size > 3) {
                toast("最多上传5个附件")
                return@setOnClickListener
            }
            FileUtils.checkFile(this)
        }*/

        //提交
        bind.toSave.setOnClickListener {
            when {
                bind.caseInsertDisputeType.isTrimEmpty() -> toast(bind.caseInsertDisputeType.getHintStr())
                bind.caseInsertAddress.isTrimEmpty() -> toast(bind.caseInsertAddress.getHintStr())
                bind.caseInsertAddressInfo.isTrimEmpty() -> toast(bind.caseInsertAddressInfo.getHintStr())
                bind.caseInsertTime.isTrimEmpty() -> toast(bind.caseInsertTime.getHintStr())
                bind.caseInsertInstitution.isTrimEmpty() -> toast(bind.caseInsertInstitution.getHintStr())
                bind.caseInsertDescribe.isTrimEmpty() -> toast(bind.caseInsertDescribe.getHintStr())
                else -> {
                    save()
                }
            }
        }

        request.saveResult.observe(this){
            if (it.code == 2000){
                successDialog(it.msg){
                    setResult(1999)
                    finish()
                }
            }else{
                errorDialog(it.msg)
            }
        }


    }

    private fun save() {

        if (listAgent.size > 0 ) {
            for (i in 0 until listAgent.size) {
                if (listAgent[i].agentType == "委托代理人") {
                    listAgent[i].agentType = "1"
                } else if (listAgent[i].agentType == "法定/指定代理人") {
                    listAgent[i].agentType = "2"
                }
                when (listAgent[i].agentEntrust) {
                    "一般代理人" -> {
                        listAgent[i].agentEntrust = "1"
                    }
                    "特被授权代理人" -> {
                        listAgent[i].agentEntrust = "2"
                    }
                    "未成年人" -> {
                        listAgent[i].agentEntrust = "3"
                    }
                    "无民事行为能力" -> {
                        listAgent[i].agentEntrust = "4"
                    }
                }
            }
        }

        tipDialog()
        request.caseSave(
            requestForBody(
                "applyType" to if (beanProposer.applyType == "自然人") 1 else if (beanProposer.applyType == "法人") 2 else if (beanProposer.applyType == "非法人组织") 3 else 1,
                "applyName" to beanProposer.applyName,
                "applyUsci" to beanProposer.applyUsci,
                "applyLegalRepresentative" to beanProposer.applyLegalRepresentative,
                "applyPhone" to beanProposer.applyPhone,
                "applyOtherType" to if (beanProposer.applyOtherType == "QQ") 2 else if (beanProposer.applyOtherType == "邮箱") 1 else if (beanProposer.applyOtherType == "微信") 3 else 0,
                "applyOtherNum" to beanProposer.applyOtherNum,
                "applyIdentity" to beanProposer.applyIdentity,
                "applyAddress" to beanProposer.applyAddress,

                "respondentType" to if (beanByProposer.respondentType == "自然人") 1 else if (beanByProposer.respondentType == "法人") 2 else if (beanByProposer.respondentType == "非法人组织") 3 else 1,
                "respondentName" to beanByProposer.respondentName,
                "respondentUsci" to beanByProposer.respondentUsci,
                "respondentLegalRepresentative" to beanByProposer.respondentLegalRepresentative,
                "respondentPhone" to beanByProposer.respondentPhone,
                "respondentOtherType" to if (beanByProposer.respondentOtherType == "QQ") 2 else if (beanProposer.applyOtherType == "邮箱") 1 else if (beanProposer.applyOtherType == "微信") 3 else 0,
                "respondentIdentity" to beanByProposer.respondentIdentity,
                "respondentAddress" to beanByProposer.respondentAddress,
                "caseAgentList" to listAgent,
                "institutionName" to bind.caseInsertInstitution.textString(),
                "caseType" to bind.caseInsertDisputeType.text.toString(),
                "caseAddress" to bind.caseInsertAddress.text.toString(),
                "caseLatitude" to lat,
                "caseLongitude" to lng,
                "caseStreet" to bind.caseInsertAddressInfo.text.toString(),
                "caseDate" to bind.caseInsertTime.text.toString(),
                "caseOrganization" to caseOrganization,
                "caseDesc" to bind.caseInsertDescribe.text.toString(),

            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2001 && resultCode == 2002) {
            bind.caseInsertInstitution.text = data?.get("name", "") ?: ""
            caseOrganization = data?.get("uuid", "") ?: ""
        } else if (requestCode == FileUtils.CHECK_FILE_REQUEST_CODE) {
            //选择文件返回
            val uri = data!!.data
            val path: String = GetFilePathFromUri.getFileAbsolutePath(this, uri)
            val file = File(path)
            if (file.length() > 1024 * 1024 * 10) {
                toast("文件过大,暂不支持")
                return
            }
            val fileName: String = FileUtils.getFileName(uri, this)
            mAdp.data.add(fileName)
            mAdp.notifyDataSetChanged()

        }else if (requestCode == 20 && resultCode == RESULT_OK) {
            bind.caseInsertAddressInfo.text = data?.getStringExtra("address")
            lat = data?.getStringExtra("lat") ?: ""
            lng = data?.getStringExtra("lng") ?: ""
        }
    }

    private fun initData() {

        val remarkJson: String =
            GetJsonDataUtil.getJson(this, "city_list.json")
        val gson = Gson()
        listAddress = gson.fromJson<ArrayList<PickerUtils.RailroadGuardAreaListBean>>(
            remarkJson,
            object : TypeToken<ArrayList<PickerUtils.RailroadGuardAreaListBean?>?>() {}.type
        )


        listDisputeType.add("民间借贷")
        listDisputeType.add("涉房纠纷")
        listDisputeType.add("征地拆迁")
        listDisputeType.add("人格权纠纷")
        listDisputeType.add("涉侨纠纷")
        listDisputeType.add("金融纠纷")
        listDisputeType.add("涉企纠纷")
        listDisputeType.add("保险纠纷")
        listDisputeType.add("校园纠纷")
        listDisputeType.add("环保纠纷")
        listDisputeType.add("婚姻家事")
        listDisputeType.add("婚姻继承")
        listDisputeType.add("消费维权")
        listDisputeType.add("劳动争议")
        listDisputeType.add("借贷纠纷")
        listDisputeType.add("物业纠纷")
        listDisputeType.add("相邻关系")
        listDisputeType.add("知识产权")
        listDisputeType.add("房屋买卖")
        listDisputeType.add("房屋租赁")
        listDisputeType.add("合同纠纷")
        listDisputeType.add("交通事故")
        listDisputeType.add("医疗纠纷")
        listDisputeType.add("侵权纠纷")
        listDisputeType.add("物权纠纷")
        listDisputeType.add("合伙联营")
        listDisputeType.add("公司业务")
        listDisputeType.add("证券票据")
        listDisputeType.add("涉外商事")
        listDisputeType.add("行政纠纷")
        listDisputeType.add("刑事自诉")
        listDisputeType.add("名誉侵权")
        listDisputeType.add("电子商务")
        listDisputeType.add("其他纠纷")


    }
}