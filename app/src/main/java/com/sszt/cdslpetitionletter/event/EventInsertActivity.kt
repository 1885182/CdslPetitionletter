package com.sszt.cdslpetitionletter.event

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ResourceUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.bean.FileBean
import com.sszt.basis.ext.*
import com.sszt.basis.ext.view.getHintStr
import com.sszt.basis.ext.view.isTrimEmpty
import com.sszt.basis.ext.view.textString
import com.sszt.basis.ext.view.textStringTrim
import com.sszt.basis.network.UploadFileRequest
import com.sszt.basis.util.GetFilePathFromUri
import com.sszt.basis.util.Utils
import com.sszt.basis.util.chat.FileUtils
import com.sszt.basis.weight.DataDialog
import com.sszt.basis.weight.DialogView
import com.sszt.basis.weight.TimeDialog
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.AssistRecordInsertFileAdapter
import com.sszt.cdslpetitionletter.bean.NationBean
import com.sszt.cdslpetitionletter.databinding.ActivityAssistRecordInsertBinding
import com.sszt.cdslpetitionletter.databinding.ActivityEventInsertBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.resources.IRoute
import java.io.File

/**
 * 添加排查登记
 */
@Route(path = IRoute.event_insert)
class EventInsertActivity :
    BaseActivity<PublicViewModel, ActivityEventInsertBinding>() {


    override fun layoutId() = R.layout.activity_event_insert

    private val request by lazy { MainRequestViewModel() }

    private var lat: String = ""
    private var lng: String = ""

    private val listType = ArrayList<String>()
    private val listScale = ArrayList<String>()
    private val listSource = ArrayList<String>()
    private val listCardType = ArrayList<String>()
    private val listPopSex = ArrayList<String>()
    private val listPopEducation = ArrayList<String>()
    private val listPeopleType = ArrayList<String>()
    private val listDissolveOk = ArrayList<String>()
    private val listDissolveCase = ArrayList<String>()

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }

        initData()

        lng = SPUtils.getInstance().getString("mCurrentLon")
        lat = SPUtils.getInstance().getString("mCurrentLat")
        bind.eventAddress.text = SPUtils.getInstance().getString("mAddress")

        //事件类别
        bind.eventType.setOnClickListener {
            DataDialog(this, listType) {
                bind.eventType.text = listType[it]
            }.show(supportFragmentManager, "dialogData")
        }
        //事件规模
        bind.eventScale.setOnClickListener {
            DataDialog(this, listScale) {
                bind.eventScale.text = listScale[it]
            }.show(supportFragmentManager, "dialogData")
        }
        //事件来源
        bind.eventSource.setOnClickListener {
            DataDialog(this, listSource) {
                bind.eventSource.text = listSource[it]
            }.show(supportFragmentManager, "dialogData")
        }
        //证件代码
        bind.cardType.setOnClickListener {
            DataDialog(this, listCardType) {
                bind.cardType.text = listCardType[it]
            }.show(supportFragmentManager, "dialogData")
        }
        //性别
        bind.popSex.setOnClickListener {
            DataDialog(this, listPopSex) {
                bind.popSex.text = listPopSex[it]
            }.show(supportFragmentManager, "dialogData")
        }
        //民族
        bind.popNation.setOnClickListener {
            DataDialog(this, getNation()) {
                bind.popNation.text = getNation()[it]
            }.show(supportFragmentManager, "dialogData")
        }
        //学历
        bind.popEducation.setOnClickListener {
            DataDialog(this, listPopEducation) {
                bind.popEducation.text = listPopEducation[it]
            }.show(supportFragmentManager, "dialogData")
        }
        //人员类别
        bind.peopleType.setOnClickListener {
            DataDialog(this, listPeopleType) {
                bind.peopleType.text = listPeopleType[it]
            }.show(supportFragmentManager, "dialogData")
        }
        //化解时限
        bind.dissolveXian.setOnClickListener {
            TimeDialog(
                this,
                "化解时限",
                booleanArrayOf(true, true, true, true, true, true),
                bind.dissolveXian.textStringTrim()
            ) {
                bind.dissolveXian.text =
                    TimeUtils.date2String(it, DialogView.timeFormat24)
            }.show(supportFragmentManager, "dialog")
        }
        //化解方式
        bind.dissolveCase.setOnClickListener {
            DataDialog(this, listDissolveCase) {
                bind.dissolveCase.text = listDissolveCase[it]
            }.show(supportFragmentManager, "dialogData")
        }
        //化解是否成功
        bind.dissolveOk.setOnClickListener {
            DataDialog(this, listDissolveOk) {
                bind.dissolveOk.text = listDissolveOk[it]
            }.show(supportFragmentManager, "dialogData")
        }


        //选择地址
        bind.eventAddress.setOnClickListener {
            routerResultCode(
                IRoute.map_select,
                20,
                "lat" to lat,
                "lng" to lng
            )
        }

        //时间
        bind.eventTime.setOnClickListener {
            TimeDialog(
                this,
                "选择时间",
                booleanArrayOf(true, true, true, true, true, true),
                bind.eventTime.textStringTrim()
            ) {
                if (it.time > System.currentTimeMillis()) {
                    ToastUtils.showShort("选择时间不能大于当前时间")
                    return@TimeDialog
                }
                bind.eventTime.text =
                    TimeUtils.date2String(it, DialogView.timeFormat24)
            }.show(supportFragmentManager, "dialog")
        }



        request.insertEventResult.observe(this) {
            if (it.code == 2000) {
                successDialog(it.msg) {
                    setResult(1999)
                    finish()
                }
            } else {
                errorDialog(it.msg)
            }
        }


        //保存
        bind.toSave.setOnClickListener {
            when {
                bind.eventName.isTrimEmpty() -> toast(bind.eventName.getHintStr())
                bind.eventType.isTrimEmpty() -> toast(bind.eventType.getHintStr())
                bind.eventScale.isTrimEmpty() -> toast(bind.eventScale.getHintStr())
                bind.eventSource.isTrimEmpty() -> toast(bind.eventSource.getHintStr())
                bind.eventTime.isTrimEmpty() -> toast(bind.eventTime.getHintStr())
                bind.eventNum.isTrimEmpty() -> toast(bind.eventNum.getHintStr())
                bind.eventUnit.isTrimEmpty() -> toast(bind.eventUnit.getHintStr())
                bind.eventAddress.isTrimEmpty() -> toast(bind.eventAddress.getHintStr())
                bind.eventContent.isTrimEmpty() -> toast(bind.eventContent.getHintStr())
                bind.cardType.isTrimEmpty() -> toast(bind.cardType.getHintStr())
                bind.cardNum.isTrimEmpty() -> toast(bind.cardNum.getHintStr())
                !Utils.isIDCard(bind.cardNum.textString()) -> toast("身份证号格式错误")
                bind.popName.isTrimEmpty() -> toast(bind.popName.getHintStr())
                bind.popSex.isTrimEmpty() -> toast(bind.popSex.getHintStr())
                bind.popNation.isTrimEmpty() -> toast(bind.popNation.getHintStr())
                bind.popEducation.isTrimEmpty() -> toast(bind.popEducation.getHintStr())
                bind.popAddr.isTrimEmpty() -> toast(bind.popAddr.getHintStr())
                bind.peopleType.isTrimEmpty() -> toast(bind.peopleType.getHintStr())
                bind.dissolveXian.isTrimEmpty() -> toast(bind.dissolveXian.getHintStr())
                bind.dissolveOrg.isTrimEmpty() -> toast(bind.dissolveOrg.getHintStr())
                !Utils.isTelOrPhone(bind.dissolvePhone.textString()) -> toast("联系电话格式错误")
                bind.dissolveCase.isTrimEmpty() -> toast(bind.dissolveCase.getHintStr())
                bind.dissolveName.isTrimEmpty() -> toast(bind.dissolveName.getHintStr())
                bind.dissolveOk.isTrimEmpty() -> toast(bind.dissolveOk.getHintStr())
                bind.dissolveMemo.isTrimEmpty() -> toast(bind.dissolveMemo.getHintStr())
                else -> save()
            }
        }
    }

    private fun save() {
        request.insertEvent(
            requestForBody(
                "caseName" to bind.eventName.textString(),
                "caseType" to bind.eventType.textString(),
                "caseScale" to bind.eventScale.textString(),
                "caseSource" to bind.eventSource.textString(),
                "caseTime" to bind.eventTime.textString(),
                "unitsNum" to bind.eventNum.textString(),
                "unitsInvolved" to bind.eventUnit.textString(),
                "placeOccurrenceName" to bind.eventAddress.textString(),
                "placeOccurrence" to "${lng},${lat}",
                "caseMemo" to bind.eventContent.textString(),
                "cardType" to bind.cardType.textString(),
                "cardNum" to bind.cardNum.textString(),
                "popName" to bind.popName.textString(),
                "popSex" to bind.popSex.textString(),
                "popAddr" to bind.popAddr.textString(),
                "popNation" to bind.popNation.textString(),
                "popEducation" to bind.popEducation.textString(),
                "popType" to bind.peopleType.textString(),
                "dissolveXian" to bind.dissolveXian.textString(),
                "dissolveOrg" to bind.dissolveOrg.textString(),
                "dissolvePhone" to bind.dissolvePhone.textString(),
                "dissolveCase" to bind.dissolveCase.textString(),
                "dissolveName" to bind.dissolveName.textString(),
                "dissolveOk" to bind.dissolveOk.textString(),
                "dissolveMemo" to bind.dissolveMemo.textString(),
            )
        )
    }

    private fun initData() {
        listType.add("民间借贷")
        listType.add("涉房纠纷")
        listType.add("征地拆迁")
        listType.add("人格权纠纷")
        listType.add("涉侨纠纷")
        listType.add("金融纠纷")
        listType.add("涉企纠纷")
        listType.add("保险纠纷")
        listType.add("校园纠纷")
        listType.add("环保纠纷")
        listType.add("婚姻家事")
        listType.add("婚姻继承")
        listType.add("消费维权")
        listType.add("劳动争议")
        listType.add("借贷纠纷")
        listType.add("物业纠纷")
        listType.add("相邻关系")
        listType.add("知识产权")
        listType.add("房屋买卖")
        listType.add("房屋租赁")
        listType.add("合同纠纷")
        listType.add("交通事故")
        listType.add("医疗纠纷")
        listType.add("侵权纠纷")
        listType.add("物权纠纷")
        listType.add("合伙联营")
        listType.add("公司业务")
        listType.add("证券票据")
        listType.add("涉外商事")
        listType.add("行政纠纷")
        listType.add("刑事自诉")
        listType.add("名誉侵权")
        listType.add("电子商务")
        listType.add("其他纠纷")


        listScale.add("个体性事件")
        listScale.add("一般群体性事件")
        listScale.add("重大群体性事件")


        listSource.add("网格员巡查")
        listSource.add("群众上报")
        listSource.add("物联网感知")
        listSource.add("上级指示")


        listCardType.addAll(
            arrayListOf(
                "居民身份证",
                "临时身份证",
                "户口簿",
                "中国人民解放车车关证",
                "中国人民武装警察都机警官证",
                "暂住证",
                "出生医学证明",
                "法官证",
                "警官证",
                "检察官证",
                "律师证",
                "记者证",
                "工作证",
                "学生证",
                "出入证",
                "临时出入证",
                "住宿证",
                "医疗证",
                "劳保证",
                "献血证",
            )
        )

        listPopSex.addAll(arrayListOf(
            "未知的性别",
            "男",
            "女",
            "未说明的性别"
        ))

        listPopEducation.addAll(arrayListOf(
            "博士研究生毕业",
            "博士研究生结业",
            "博士研究生肄业",
            "硕士研究生毕业",
            "硕士研究生结业",
            "硕士研究生肄业",
            "研究生毕业",
            "研究生结业",
            "研究生肄业",
            "大学本科毕业",
            "大学本科结业",
            "大学本科肄业",
            "大学普通班毕业",
            "大学专科毕业",
            "大学专科结业",
            "大学专科肄业",
            "中等专科毕业",
            "中等专科结业",
            "中等专科肄业",
            "职业高中毕业",
            "职业高中结业",
            "职业高中肄业",
            "技工学校毕业",
            "技工学校结业",
            "技工学校肄业",
            "普通高中毕业",
            "普通高中结业",
            "普通高中肄业",
            "初中毕业",
            "初中肄业",
            "小学毕业",
            "小学肄业",
            "其他",
        ))

        listPeopleType.addAll(arrayListOf(
            "机关企事业单位从业人员",
            "农林牧渔从业人员",
            "个体或自由职业人员",
            "军人",
            "离退休人员",
            "学生",
            "无业人员",
            "其他人员",
            "涉企业事单位改制人员",
            "军队退役人员",
            "原民办、国企教师",
            "国有企业退休职工",
            "返城知青",
            "失独人员",
            "农村进城务工人员",
            "其他群体性诉求人员"
        ))

        listDissolveCase.addAll(arrayListOf(
            "司法调解",
            "行政调解",
            "人民调解",
            "仲裁调解",
            "商事调解",
            "行业调解",
            "律师调解",
            "民商事仲裁",
            "农村土地承包经营纠纷仲裁",
            "劳动人事争议仲裁",
            "行政复议",
            "行政裁决",
            "诉讼",
            "协商和解",
            "其他"
        ))

        listDissolveOk.addAll(arrayListOf("是","否"))

    }

    private val nationStringList = java.util.ArrayList<String>()

    fun getNation(): ArrayList<String> {
        if (nationStringList.isNotEmpty()) {

            return nationStringList
        }
        val readAssets2String = ResourceUtils.readAssets2String("nation.json")

        val fromJson = Gson().fromJson<ArrayList<NationBean>>(
            readAssets2String,
            object : TypeToken<List<NationBean>>() {}.getType()
        )
        fromJson?.forEach {
            nationStringList.add(it.value)
        }

        return nationStringList

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 20 && resultCode == RESULT_OK) {
            bind.eventAddress.text = data?.getStringExtra("address")
            lat = data?.getStringExtra("lat") ?: ""
            lng = data?.getStringExtra("lng") ?: ""
        }

    }

}