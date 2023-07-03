package com.sszt.cdslpetitionletter.sentiment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.ext.view.getHintStr
import com.sszt.basis.ext.view.isTrimEmpty
import com.sszt.basis.ext.view.textString
import com.sszt.basis.network.UploadFileRequest
import com.sszt.basis.util.GetFilePathFromUri
import com.sszt.basis.util.Utils
import com.sszt.basis.util.chat.FileUtils
import com.sszt.basis.weight.DataDialog
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.activity.AddressCheckActivity
import com.sszt.cdslpetitionletter.databinding.ActivitySentimentInsertBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.resources.IRoute
import java.io.File


/**
 * 民情收集-添加
 */
@Route(path = IRoute.sentiment_insert)
class SentimentInsertActivity : BaseActivity<PublicViewModel, ActivitySentimentInsertBinding>() {


    override fun layoutId() = R.layout.activity_sentiment_insert
    private val listType = ArrayList<String>()
    private val listVillage = ArrayList<String>()

    private val request by lazy { MainRequestViewModel() }
    private val fileRequest by lazy { UploadFileRequest() }
    private var enclosure: String = ""
    var receiverState = ""
    var receiverCity = ""
    var receiverDistrict = ""
    var receiverFour = ""


    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }

        //附件
        bind.sentimentInsertFile.setOnClickListener {
            FileUtils.checkFile(this)
        }

        initData()

        //选择反应类型
        bind.sentimentInsertType.setOnClickListener {
            DataDialog(this, listType) {
                bind.sentimentInsertType.text = listType[it]
            }.show(supportFragmentManager, "dialogData")
        }

        //选择所在街镇
        bind.sentimentInsertTown.setOnClickListener {
            startActivityForResult(Intent(this, AddressCheckActivity::class.java), 666)
        }

        //选择所在村社
        bind.sentimentInsertVillage.setOnClickListener {
            if (listVillage.size == 0) {
                toast("请先选择所在街镇")
                return@setOnClickListener
            }
            DataDialog(this, listVillage) {
                bind.sentimentInsertVillage.text = listVillage[it]
            }.show(supportFragmentManager, "dialogData")
        }

        //保存
        bind.toSave.setOnClickListener {
            when {
                bind.sentimentInsertName.isTrimEmpty() -> toast(bind.sentimentInsertName.getHintStr())
                !Utils.isTelOrPhone(bind.sentimentInsertPhone.textString()) -> toast("联系电话格式错误")
                bind.sentimentInsertType.isTrimEmpty() -> toast(bind.sentimentInsertType.getHintStr())
                bind.sentimentInsertTown.isTrimEmpty() -> toast(bind.sentimentInsertTown.getHintStr())
                bind.sentimentInsertVillage.isTrimEmpty() -> toast(bind.sentimentInsertVillage.getHintStr())
                bind.sentimentInsertAddressInfo.isTrimEmpty() -> toast(bind.sentimentInsertAddressInfo.getHintStr())
                bind.sentimentInsertDescribe.isTrimEmpty() -> toast(bind.sentimentInsertDescribe.getHintStr())
                else -> save()
            }
        }




        request.insertSentimentResult.observe(this) {
            if (it.code == 2000) {
                successDialog(it.msg) {
                    finish()
                }
            } else {
                errorDialog(it.msg)
            }
        }

        fileRequest.fileResult.observe(this) {
            parseState(it, {
                successDialog("上传成功") {
                    enclosure = it.uuid
                    bind.sentimentInsertFile.text = it.title
                }
            }, {
                toast(it.errorMsg)
            })
        }

        request.getVillageListResult.observe(this) {
            for (i in 0 until it.size) {
                listVillage.add(it[i].villageName)
            }
        }
    }

    private fun save() {
        showLoading()
        request.insertSentiment(
            requestForBody(
                "accountName" to bind.sentimentInsertName.textString(),
                "phoneNum" to bind.sentimentInsertPhone.textString(),
                "accountType" to bind.sentimentInsertType.textString(),
                "streetTownName" to bind.sentimentInsertTown.textString(),
                "villageCommunity" to bind.sentimentInsertVillage.textString(),
                "detailedAddress" to bind.sentimentInsertAddressInfo.textString(),
                "helpContent" to bind.sentimentInsertDescribe.textString(),
                "enclosure" to enclosure,
                "streetTown" to arrayListOf(receiverState,receiverCity,receiverDistrict,receiverFour)

            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FileUtils.CHECK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            val uri: Uri? = data?.data
            val path = GetFilePathFromUri.getFileAbsolutePath(this, uri)

            val file = File(path)

            if (file.length() > 1024 * 1024 * 40) {
                toast("文件过大,暂不支持")
                return
            }
            tipDialog("上传中")
            fileRequest.uploadFile(requestFileAll(file))
        }
        if (requestCode == 666 && resultCode == 999) {
            receiverState = data!!.getStringExtra("receiverState") ?: ""
            receiverCity = data.getStringExtra("receiverCity") ?: ""
            receiverDistrict = data.getStringExtra("receiverDistrict") ?: ""
            receiverFour = data.getStringExtra("receiverFour") ?: ""
            bind.sentimentInsertTown.text =
                receiverState + receiverCity + receiverDistrict + receiverFour

            listVillage.clear()
            request.getVillageList(requestForBody("townsName" to receiverFour))
        }
    }

    private fun initData() {
        listType.add("农村农业")
        listType.add("国土资源")
        listType.add("城乡建设")
        listType.add("劳动和社会保障")
        listType.add("卫生计生")
        listType.add("教育文体")
        listType.add("民政")
        listType.add("政法")
        listType.add("经济管理")
        listType.add("交通运输")
        listType.add("商贸旅游")
        listType.add("科技与信息产业")
        listType.add("环境保护")
        listType.add("党务政务")
        listType.add("组织人事")
        listType.add("纪检监察")
        listType.add("其他")
    }
}