package com.sszt.cdslpetitionletter.assist

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
import com.sszt.basis.ext.*
import com.sszt.basis.ext.view.getHintStr
import com.sszt.basis.ext.view.isTrimEmpty
import com.sszt.basis.ext.view.textString
import com.sszt.basis.ext.view.textStringTrim
import com.sszt.basis.network.UploadFileRequest
import com.sszt.basis.util.GetFilePathFromUri
import com.sszt.basis.util.chat.FileUtils
import com.sszt.basis.weight.DialogView
import com.sszt.basis.weight.TimeDialog
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.AssistRecordInsertFileAdapter
import com.sszt.cdslpetitionletter.databinding.ActivityAssistRecordInsertBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.resources.IRoute
import java.io.File

/**
 * 添加帮扶记录
 */
@Route(path = IRoute.assist_record_insert)
class AssistRecordInsertActivity :
    BaseActivity<PublicViewModel, ActivityAssistRecordInsertBinding>() {


    override fun layoutId() = R.layout.activity_assist_record_insert

    private val request by lazy { MainRequestViewModel() }
    private val fileRequest by lazy { UploadFileRequest() }

    private val mAda by lazy { AssistRecordInsertFileAdapter(arrayListOf()) }
    private var personnelUuid: String = ""
    private var fileUuids: String = ""
    private var lat: String = ""
    private var lng: String = ""

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }


        bind.recyclerFile.init(LinearLayoutManager(this), mAda)

        lng = SPUtils.getInstance().getString("mCurrentLon")
        lat = SPUtils.getInstance().getString("mCurrentLat")
        bind.recordInsertAddress.text = SPUtils.getInstance().getString("mAddress")

        //帮扶对象
        bind.recordInsertName.setOnClickListener {
            routerResultCode(
                IRoute.assist_people_list,
                2000,
                "type" to 199
            )
        }

        bind.recordInsertAddress.setOnClickListener {
            routerResultCode(
                IRoute.map_select,
                20,
                "lat" to lat,
                "lng" to lng
            )
        }

        //时间
        bind.recordInsertTime.setOnClickListener {
            TimeDialog(
                this,
                "选择时间",
                booleanArrayOf(true, true, true, true, true, true),
                bind.recordInsertTime.textStringTrim()
            ) {
                if (it.time > System.currentTimeMillis()){
                    ToastUtils.showShort("选择时间不能大于当前时间")
                    return@TimeDialog
                }
                bind.recordInsertTime.text =
                    TimeUtils.date2String(it, DialogView.timeFormat24)
            }.show(supportFragmentManager, "dialog")
        }

        //选择文件
        bind.assistRecordFileInsert.setOnClickListener {
            FileUtils.checkFile(this)
        }

        fileRequest.fileResult.observe(this) {
            parseState(it, {
                successDialog("上传成功") {
                    mAda.data.add(it)
                    mAda.notifyDataSetChanged()
                }
            }, {
                toast(it.errorMsg)
            })
        }

        mAda.addChildClickViewIds(R.id.adapter_del, R.id.recordDetailFile)
        mAda.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.adapter_del -> {
                    mAda.data.removeAt(position)
                    mAda.notifyDataSetChanged()
                }
                R.id.recordDetailFile -> FileUtils.openFile(this,mAda.data[position].title,mAda.data[position].url)
            }
        }

        request.insertAssistRecordResult.observe(this) {
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
                bind.recordInsertName.isTrimEmpty() -> toast(bind.recordInsertName.getHintStr())
                bind.recordInsertTime.isTrimEmpty() -> toast(bind.recordInsertTime.getHintStr())
                bind.recordInsertAddress.isTrimEmpty() -> toast(bind.recordInsertAddress.getHintStr())
                bind.recordInsertMeasures.isTrimEmpty() -> toast(bind.recordInsertMeasures.getHintStr())
                bind.recordInsertEffect.isTrimEmpty() -> toast(bind.recordInsertEffect.getHintStr())
                else -> save()
            }
        }
    }

    private fun save() {
        for (i in 0 until mAda.data.size) {
            fileUuids = fileUuids + "," + mAda.data[i].uuid
        }
        request.insertAssistRecord(
            requestForBody(
                "personnelUuid" to personnelUuid,
                "personnelName" to bind.recordInsertName.textString(),
                "helpTime" to bind.recordInsertTime.textString(),
                "helpAddrName" to bind.recordInsertAddress.textString(),
                "helpAddr" to "${lng},${lat}",
                "helpMeasures" to bind.recordInsertMeasures.textString(),
                "helpEffect" to bind.recordInsertEffect.textString(),
                "helpFile" to fileUuids
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2000 && resultCode == 1999) {
            personnelUuid = data?.getStringExtra("uuid") ?: ""
            bind.recordInsertName.text = data?.getStringExtra("name")
        }
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
        } else if (requestCode == 20 && resultCode == RESULT_OK) {
            bind.recordInsertAddress.text = data?.getStringExtra("address")
            lat = data?.getStringExtra("lat") ?: ""
            lng = data?.getStringExtra("lng") ?: ""
        }

    }

}