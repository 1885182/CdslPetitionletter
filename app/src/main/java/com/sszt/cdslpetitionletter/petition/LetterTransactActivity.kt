package com.sszt.cdslpetitionletter.petition

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.bumptech.glide.Glide
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
import com.sszt.cdslpetitionletter.databinding.ActivityLetterTransactBinding
import com.sszt.cdslpetitionletter.viewmodel.request.PetitionLetterRequestViewModel
import com.sszt.resources.IRoute
import java.io.File

/**
 * 办理情况
 */
@Route(path = IRoute.letter_transact)
class LetterTransactActivity : BaseActivity<PublicViewModel, ActivityLetterTransactBinding>() {

    override fun layoutId() = R.layout.activity_letter_transact
    private val fileRequest by lazy { UploadFileRequest() }
    private val request by lazy { PetitionLetterRequestViewModel() }
    private var url: String = ""

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }
        val xfacceptInfoUuid = intent.getStringExtra("xfacceptInfoUuid")

        bind.toSave.setOnClickListener {
            when {
                bind.letterTransactTop.isTrimEmpty() -> toast(bind.letterTransactTop.getHintStr())
                bind.letterTransactBot.isTrimEmpty() -> toast(bind.letterTransactBot.getHintStr())
                else -> {
                    //保存
                    tipDialog()
                    request.addLetterTransact(
                        requestForBody(
                            "xfAcceptInfoUuid" to xfacceptInfoUuid,
                            "diaochaQingk" to bind.letterTransactTop.textString(),
                            "handleOpinion" to bind.letterTransactBot.textString(),
                            "relAttachments" to url,
                        )
                    )
                }
            }
        }

        request.addLetterTransactResult.observe(this){
            if (it.code == 2000) {
                successDialog(it.msg) {
                    setResult(1999)
                    finish()
                }
            } else {
                errorDialog(it.msg)
            }
        }

        bind.letterTransactFile.setOnClickListener {
            FileUtils.checkFile(this)
        }

        bind.letterTransactImg.setOnClickListener {
            FileUtils.openFile(this,bind.letterTransactFile.textString(),bind.letterTransactFile.textString())
        }

        fileRequest.fileResult.observe(this) {
            parseState(it, {
                successDialog("上传成功") {
                    if (FileUtils.getFileOpenWith(it.title) == 3){
                        bind.letterTransactFile.visibility = View.GONE
                        Glide.with(this).load(it.url).into(bind.letterTransactImg)
                        bind.letterTransactFile.text = it.url
                    }else {
                        bind.letterTransactFile.text = it.title
                    }
                    url = it.uuid
                }
            }, {
                toast(it.errorMsg)
            })
        }
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
            tipDialog()
            fileRequest.uploadFile(requestFileAll(file))
        }
    }
}