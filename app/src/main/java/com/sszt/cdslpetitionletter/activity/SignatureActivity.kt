package com.sszt.cdslpetitionletter.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.network.UploadFileRequest
import com.sszt.basis.weight.SignatureView
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivitySignatureBinding
import com.sszt.resources.IRoute
import java.io.*

/**
 * 手写签名上传
 */
@Route(path = IRoute.main_signature)
class SignatureActivity : BaseActivity<PublicViewModel, ActivitySignatureBinding>() {


    override fun layoutId() = R.layout.activity_signature

    private val request by lazy { UploadFileRequest() }
    override fun initView(savedInstanceState: Bundle?) {
        val mView = SignatureView(this)
        bind.flView.addView(mView)
        mView.requestFocus()
        //重置
        bind.btnClear.setOnClickListener { mView.clear() }

        //上传
        bind.btnOk.setOnClickListener {
            tipDialog()
            request.uploadFile(requestFileAll(bitmapToFile(mView.cachebBitmap)))
        }

        //返回
        bind.btnCancel.setOnClickListener { finish() }


        //上传文件回调
        request.fileResult.observe(this) { it ->
            parseState(it, {
                successDialog("上传成功"){
                    setResult(2000, Intent().putExtra("file",it.url+"").putExtra("index",intent.getIntExtra("index",0)))
                    finish()
                }
            }, {
                toast(it.errorMsg)
            })
        }

    }

    //bitmap转图片
    private fun bitmapToFile(bm: Bitmap): File? {
        var file: File? = null
        try {
            val path: String =
                getExternalFilesDir("/document/")!!.path
            val dirFile = File(path)
            if (!dirFile.exists()) {
                dirFile.mkdir()
            }
            file = File(path + "/" + System.currentTimeMillis() + ".png")
            val bos: BufferedOutputStream?
            bos = BufferedOutputStream(FileOutputStream(file))
            bm.compress(
                Bitmap.CompressFormat.PNG, 100, bos
            )
            bos.flush()
            bos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }
}