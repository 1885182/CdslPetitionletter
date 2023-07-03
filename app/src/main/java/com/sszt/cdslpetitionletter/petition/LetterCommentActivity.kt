package com.sszt.cdslpetitionletter.petition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.KeyboardUtils
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.ext.view.isTrimEmpty
import com.sszt.basis.ext.view.textString
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.databinding.ActivityLetterCommentBinding
import com.sszt.cdslpetitionletter.viewmodel.request.MainRequestViewModel
import com.sszt.cdslpetitionletter.viewmodel.request.PetitionLetterRequestViewModel
import com.sszt.resources.IRoute

/**
 * 信访评价
 */
@Route(path = IRoute.letter_comment)
class LetterCommentActivity : BaseActivity<PublicViewModel, ActivityLetterCommentBinding>() {


    private val request by lazy { PetitionLetterRequestViewModel() }
    override fun layoutId() = R.layout.activity_letter_comment

    override fun initView(savedInstanceState: Bundle?) {
        val xfAcceptInfoUuid = intent.getStringExtra("xfAcceptInfoUuid")
        val applyerInfoUuid = intent.getStringExtra("applyerInfoUuid")
        val visitInfoUuid = intent.getStringExtra("visitInfoUuid")
        bind.titleLayout.setOnBackClick { finish() }


        if (intent.getIntExtra("type", 0) == 1) {

        } else if (intent.getIntExtra("type", 0) == 2) {
            request.getLetterComment(
                requestForBody(
                    "acceptInfoUuid" to xfAcceptInfoUuid,
                    "applyerInfoUuid" to applyerInfoUuid,
                    "visitInfoUuid" to visitInfoUuid
                )
            )

            bind.toSave.visibility = View.GONE

            bind.letterCommentPersonData.visibility = View.VISIBLE
            bind.letterCommentPersonData1.visibility = View.VISIBLE
            bind.letterCommentUnitData.visibility = View.VISIBLE
            bind.letterCommentUnitData1.visibility = View.VISIBLE


            bind.letterCommentPersonRG.visibility = View.GONE
            bind.letterCommentPerson.visibility = View.GONE
            bind.letterCommentUnitRG.visibility = View.GONE
            bind.letterCommentUnit.visibility = View.GONE
        }



        request.getLetterCommentResult.observe(this) {
            if (it.xfApplyerReplyList.isNotEmpty()) {
                bind.letterCommentPersonData.text = it.xfApplyerReplyList[0].oneReply
                bind.letterCommentPersonData1.text = it.xfApplyerReplyList[0].oneEvaluate
                bind.letterCommentUnitData.text = it.xfApplyerReplyList[0].twoReply
                bind.letterCommentUnitData1.text = it.xfApplyerReplyList[0].twoEvaluate
            }

        }


        bind.toSave.setOnClickListener {
            val personId = bind.letterCommentPersonRG.checkedRadioButtonId
            val unitId = bind.letterCommentUnitRG.checkedRadioButtonId

            when {
                unitId == -1 -> toast("请选择满意度")
                personId == -1 -> toast("请选择满意度")
                bind.letterCommentPerson.isTrimEmpty() -> toast("请输入留言")
                bind.letterCommentUnit.isTrimEmpty() -> toast("请输入留言")
                else -> {
                    tipDialog()
                    request.addLetterComment(
                        requestForBody(
                            "xfAcceptInfoUuid" to xfAcceptInfoUuid,
                            "oneReply" to findViewById<RadioButton>(personId).textString(),
                            "twoReply" to findViewById<RadioButton>(unitId).textString(),
                            "oneEvaluate" to bind.letterCommentPerson.textString(),
                            "twoEvaluate" to bind.letterCommentUnit.textString(),
                        )
                    )
                }
            }
        }

        request.addLetterCommentResult.observe(this) {
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
}