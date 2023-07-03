package com.sszt.cdslpetitionletter.troubleshoot

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.ext.view.getHintStr
import com.sszt.basis.ext.view.isTrimEmpty
import com.sszt.basis.ext.view.textString
import com.sszt.basis.network.UploadFileRequest
import com.sszt.basis.util.GetFilePathFromUri
import com.sszt.basis.util.chat.FileUtils
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivityHangApplyBinding
import com.sszt.cdslpetitionletter.viewmodel.request.TroubleshootRequestViewModel
import com.sszt.resources.IRoute
import java.io.File

/**
 * 挂牌申请/详情
 */
@Route(path = IRoute.hang_detail)
class HangApplyActivity : BaseActivity<PublicViewModel, ActivityHangApplyBinding>() {


    override fun layoutId() = R.layout.activity_hang_apply

    var url = ""
    var fileUuid = ""
    private val fileRequest by lazy { UploadFileRequest() }
    private val request by lazy { TroubleshootRequestViewModel() }

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }
        val type = intent.getIntExtra("type", 0)
        if (type == 1) {
            bind.titleLayout.titleTitle.text = "挂牌"
        } else if (type == 2) {
            bind.titleLayout.titleTitle.text = "挂牌详情"
            bind.hangOpinion.isEnabled = false
            bind.toSave.visibility = View.GONE
            bind.hangOpinion.hint = ""
            bind.hangOpinion.setText("")
            bind.hangFile.hint = ""

            //获取数据
            request.getTroubleshootDetail(requestForBody("uuid" to intent.getStringExtra("uuid")))

            //展示数据
            request.getTroubleshootDetailResult.observe(this) {
                it.gpList?.let { gpList ->
                    if (gpList.isNotEmpty()) {
                        val latestGp = gpList.last()
                        bind.hangOpinion.setText(latestGp.gpContent)
                        val fileList = latestGp.gpFileList
                        if (fileList?.isNotEmpty() == true) {
                            val latestFile = fileList.last()
                            bind.hangFile.text = latestFile.title
                            url = latestFile.fileUrl
                        }
                    }
                }
            }
        }


        //打开/选择文件
        bind.hangFile.setOnClickListener {
            if (type == 1) {
                FileUtils.checkFile(this)
            } else if (type == 2) {
                if (url == ""){
                    return@setOnClickListener
                }
                FileUtils.openFile(this, url, url)
            }
        }



        //上传文件回调
        fileRequest.fileResult.observe(this) {
            parseState(it, {
                successDialog("上传成功") {
                    fileUuid = it.uuid
                    bind.hangFile.text = it.title
                }
            }, {
                toast(it.errorMsg)
            })
        }

        //添加挂牌回调
        request.insertHangApplyResult.observe(this) {
            if (it.code == 2000) {
                successDialog(it.msg) {
                    setResult(1999)
                    finish()
                }
            } else {
                errorDialog(it.msg)
            }
        }

        //提交
        bind.toSave.setOnClickListener {
            when {
                bind.hangOpinion.isTrimEmpty() -> toast(bind.hangOpinion.getHintStr())
                else -> {
                    request.insertHangApply(
                        requestForBody(
                            "gpContent" to bind.hangOpinion.textString(),
                            "gpFile" to fileUuid,
                            "screeningUuid" to intent.getStringExtra("uuid")
                        )
                    )
                }
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //选择文件回调
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
    }
}