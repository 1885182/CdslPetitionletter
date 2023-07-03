package com.sszt.cdslpetitionletter.test

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
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
import com.sszt.basis.util.chat.FileUtils
import com.sszt.basis.weight.DataDialog
import com.sszt.basis.weight.DialogView
import com.sszt.basis.weight.TimeDialog
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.AssistRecordInsertFileAdapter
import com.sszt.cdslpetitionletter.databinding.ActivityAssistRecordInsertBinding
import com.sszt.cdslpetitionletter.databinding.ActivityEventInsertBinding
import com.sszt.cdslpetitionletter.databinding.ActivityTestInsertBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.resources.IRoute
import java.io.File

/**
 * 添加
 */
@Route(path = IRoute.view_add)
class TestInsertActivity :
    BaseActivity<PublicViewModel, ActivityTestInsertBinding>() {


    override fun layoutId() = R.layout.activity_test_insert

    private val request by lazy { MainRequestViewModel() }

    private var lat: String = ""
    private var lng: String = ""
    private val fileRequest by lazy { UploadFileRequest() }

    private val listType = ArrayList<String>()
    private val listScale = ArrayList<String>()
    private val listSource = ArrayList<String>()
    var type = 0

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }

        initData()


        lng = SPUtils.getInstance().getString("mCurrentLon")
        lat = SPUtils.getInstance().getString("mCurrentLat")
        bind.eventAddress.text = SPUtils.getInstance().getString("mAddress")



        //选择地址
        bind.eventAddress.setOnClickListener {
            routerResultCode(
                IRoute.map_select,
                20,
                "lat" to lat,
                "lng" to lng
            )
        }

        fileRequest.fileResult.observe(this) {
            parseState(it, {
                successDialog("上传成功"){
                    if (type == 1) {
                        bind.eventFile.text = it.title
                    }else if (type == 2){
                        bind.eventImage.text = it.title
                    }
                }
            }, {
                toast(it.errorMsg)
            })
        }

        //时间
        bind.eventTime.setOnClickListener {
            TimeDialog(
                this,
                "选择时间",
                booleanArrayOf(true, true, true, true, true, true),
                bind.eventTime.textStringTrim()
            ) {
                if (it.time > System.currentTimeMillis()){
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

        bind.eventFile.setOnClickListener {
            type = 1
            FileUtils.checkFile(this)
        }
        bind.eventImage.setOnClickListener {
            type = 2
            FileUtils.checkFile(this)
        }


        //保存
        bind.toSave.setOnClickListener {
            when {
                bind.eventName.isTrimEmpty() -> toast(bind.eventName.getHintStr())
                bind.eventEvent.isTrimEmpty() -> toast(bind.eventEvent.getHintStr())

                bind.eventTime.isTrimEmpty() -> toast(bind.eventTime.getHintStr())

                bind.eventAddress.isTrimEmpty() -> toast(bind.eventAddress.getHintStr())
                else -> save()
            }
        }
    }
    private fun save() {
        successDialog("成功") {

        }
    }

    private fun initData(){
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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 20 && resultCode == RESULT_OK) {
            bind.eventAddress.text = data?.getStringExtra("address")
            lat = data?.getStringExtra("lat") ?: ""
            lng = data?.getStringExtra("lng") ?: ""
        }
        if (requestCode == FileUtils.CHECK_FILE_REQUEST_CODE && resultCode == RESULT_OK){
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

    }

}