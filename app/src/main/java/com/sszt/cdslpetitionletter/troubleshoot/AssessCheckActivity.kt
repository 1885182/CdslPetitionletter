package com.sszt.cdslpetitionletter.troubleshoot

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.TimeUtils
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
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
import com.sszt.cdslpetitionletter.databinding.ActivityAssessCheckBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.cdslpetitionletter.viewmodel.request.TroubleshootRequestViewModel
import com.sszt.resources.IRoute
import java.io.File

/**
 * 评估审核/摘牌申请
 */

@Route(path = IRoute.assess_check)
class AssessCheckActivity : BaseActivity<PublicViewModel, ActivityAssessCheckBinding>() {


    override fun layoutId() = R.layout.activity_assess_check

    private val request by lazy { TroubleshootRequestViewModel() }
    private val fileRequest by lazy { UploadFileRequest() }

    var title = ""
    var uuid = ""
    var fileUuid = ""

    private val listDissolveOk = ArrayList<String>()
    private val listDissolveCase = ArrayList<String>()
    override fun initView(savedInstanceState: Bundle?) {
        title = intent.getStringExtra("title") ?: ""
        uuid = intent.getStringExtra("uuid") ?: ""
        bind.titleLayout.titleTitle.text = title
        bind.titleLayout.setOnBackClick { finish() }

        if (title == "评估审核") {
            bind.dissolveMemoTip.visibility = View.GONE
            bind.dissolveMemo.visibility = View.GONE
            bind.pickFileTip.visibility = View.GONE
            bind.pickFile.visibility = View.GONE
        } else if (title == "摘牌申请") {
            bind.dissolveXianTip.visibility = View.GONE
            bind.dissolveXian.visibility = View.GONE
        }

        initData()

        bind.pickFile.setOnClickListener {
            FileUtils.checkFile(this)
        }

        fileRequest.fileResult.observe(this) {
            parseState(it, {
                successDialog("上传成功") {
                    fileUuid = it.uuid
                    bind.pickFile.text = it.title
                }
            }, {
                toast(it.errorMsg)
            })
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


        //保存
        bind.toSave.setOnClickListener {
            when {
                bind.dissolveOrg.isTrimEmpty() -> toast(bind.dissolveOrg.getHintStr())
                !Utils.isTelOrPhone(bind.dissolvePhone.textString()) -> toast("联系电话格式错误")
                bind.dissolveCase.isTrimEmpty() -> toast(bind.dissolveCase.getHintStr())
                bind.dissolveName.isTrimEmpty() -> toast(bind.dissolveName.getHintStr())
                bind.dissolveOk.isTrimEmpty() -> toast(bind.dissolveOk.getHintStr())
                //bind.dissolveMemo.isTrimEmpty() -> toast(bind.dissolveMemo.getHintStr())
                else -> save()
            }
        }



        request.insertTroubleshootResult.observe(this) {
            if (it.code == 2000) {
                successDialog(it.msg) {
                    setResult(1999)
                    finish()
                }
            } else {
                errorDialog(it.msg)
            }
        }

        request.insertPickApplyResult.observe(this) {
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

    private fun save() {
        if (title == "评估审核") {
            if (bind.dissolveXian.isTrimEmpty()) {
                toast(bind.dissolveXian.getHintStr())
                return
            }
            request.insertTroubleshoot(
                requestForBody(
                    "uuid" to uuid,
                    "dissolveXian" to bind.dissolveXian.textString(),
                    "dissolveOrg" to bind.dissolveOrg.textString(),
                    "dissolvePhone" to bind.dissolvePhone.textString(),
                    "dissolveCase" to bind.dissolveCase.textString(),
                    "dissolveName" to bind.dissolveName.textString(),
                    "dissolveOk" to bind.dissolveOk.textString(),
                )
            )
        } else if (title == "摘牌申请") {
            if (bind.dissolveMemo.isTrimEmpty()) {
                toast(bind.dissolveMemo.getHintStr())
                return
            }
            request.insertPickApply(
                requestForBody(
                    "screeningUuid" to uuid,
                    "dissolveOrg" to bind.dissolveOrg.textString(),
                    "dissolvePhone" to bind.dissolvePhone.textString(),
                    "dissolveCase" to bind.dissolveCase.textString(),
                    "dissolveName" to bind.dissolveName.textString(),
                    "dissolveOk" to bind.dissolveOk.textString(),
                    "dissolveContent" to bind.dissolveMemo.textString(),
                    "zpFile" to fileUuid
                    //"dissolveMemo" to bind.dissolveMemo.textString(),
                )
            )
        }
    }


    private fun initData() {

        listDissolveCase.addAll(
            arrayListOf(
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
            )
        )

        listDissolveOk.addAll(arrayListOf("是", "否"))

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
    }
}