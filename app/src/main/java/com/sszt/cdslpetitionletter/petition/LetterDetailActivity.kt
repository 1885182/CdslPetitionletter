package com.sszt.cdslpetitionletter.petition

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.SPUtils
import com.sszt.basis.base.activity.BaseActivity
import com.sszt.basis.base.viewmodel.PublicViewModel
import com.sszt.basis.ext.*
import com.sszt.basis.ext.view.isTrimEmpty
import com.sszt.basis.ext.view.textString
import com.sszt.basis.util.chat.FileUtils
import com.sszt.cdslpetitionletter.R
import com.sszt.cdslpetitionletter.adapter.LetterDetailContactsAdapter
import com.sszt.cdslpetitionletter.adapter.LetterPostponeAdapter
import com.sszt.cdslpetitionletter.databinding.ActivityLetterDetailBinding
import com.sszt.cdslpetitionletter.viewmodel.request.PetitionLetterRequestViewModel
import com.sszt.resources.IRoute
import java.util.*

/**
 * 信访详情
 */
@Route(path = IRoute.letter_detail)
class LetterDetailActivity : BaseActivity<PublicViewModel, ActivityLetterDetailBinding>() {


    override fun layoutId() = R.layout.activity_letter_detail

    private val request by lazy { PetitionLetterRequestViewModel() }
    var cuuid = ""
    var auuid = ""
    private val mAdp by lazy { LetterDetailContactsAdapter(arrayListOf()) }
    private val mAdpPostpone by lazy { LetterPostponeAdapter(arrayListOf()) }

    override fun initView(savedInstanceState: Bundle?) {
        bind.titleLayout.setOnBackClick { finish() }

        if (SPUtils.getInstance().getBoolean("ceshiyemian")) {
            bind.ceshiDay.visibility = View.VISIBLE
        }

        val title = intent.getStringExtra("title")
        request.getLetterDetail(
            requestForBody(
                "applyerInfoUuid" to intent.getStringExtra("applyerInfoUuid"),
                "visitInfoUuid" to intent.getStringExtra("visitInfoUuid"),
                "acceptInfoUuid" to intent.getStringExtra("acceptInfoUuid"),
            )
        )

        bind.recycler.init(LinearLayoutManager(this), mAdp)

        bind.toCancels.setOnClickListener { addCaseApply("驳回") }

        request.getLetterDetailError.observe(this) {
            if (it.code == 9000) {
                toast(it.msg ?: "")
            }
        }

        bind.toList.setOnClickListener {
            finish()
        }

        bind.letterFile.setOnClickListener {
            if (!bind.letterFile.isTrimEmpty()) {
                FileUtils.openFile(this, bind.letterFile.textString(), bind.letterFile.textString())
            }
        }

        mAdpPostpone.setOnItemClickListener { adapter, view, position ->
            router(
                IRoute.letter_web_view,
                "name" to mAdpPostpone.data[position].docName,
                "url" to getDecoder(mAdpPostpone.data[position].docTemplate, position)
            )
        }

        request.getLetterDetailResult.observe(this) {

            bind.letterName.text = it.xfApplyerInfoEntity.applyName
            bind.letterContactsNumber.text =
                it.xfApplyerInfoEntity.xfApplyerLinkEntityList.size.toString()
            mAdp.data.addAll(it.xfApplyerInfoEntity.xfApplyerLinkEntityList)
            mAdp.notifyDataSetChanged()

            bind.letterProblemAddress.text = it.xfVisitInfoEntity.problemArea
            bind.letterAddressInfo.text = it.xfVisitInfoEntity.problemAddress
            bind.letterTime.text = it.xfVisitInfoEntity.problemTime
            bind.letterProblemType.text = it.xfVisitInfoEntity.problemFenlei
            bind.letterKeyWord.text = it.xfVisitInfoEntity.complainCi
            bind.letterHotProblem.text = it.xfVisitInfoEntity.hotProblem
            bind.letterComplaintTitle.text = it.xfVisitInfoEntity.complainTitle
            bind.letterComplaintContent.text = it.xfVisitInfoEntity.tousuNeirong


            if (it.xfVisitInfoEntity.commAttachmentList != null && it.xfVisitInfoEntity.commAttachmentList.size > 0) {
                bind.letterFile.text = it.xfVisitInfoEntity.commAttachmentList[0].title
            }

            //it.xfVisitInfoEntity.commAttachmentList//附件


            //复查
            if (it.xfReviewEntityList.isNotEmpty()) {
                bind.letterCheckView.visibility = View.VISIBLE
                bind.letterCheckV.visibility = View.VISIBLE
                bind.letterCheckLL.visibility = View.VISIBLE
                bind.letterCheckCL.visibility = View.VISIBLE

                bind.letterCheckApply.text = it.xfReviewEntityList[0].reExplain
                bind.letterCheckResult.text = it.xfReviewEntityList[0].reResult
                bind.letterCheckOpinion.text = it.xfReviewEntityList[0].reOpinion
            }

            //复核
            if (it.xfToReviewEntityList.isNotEmpty()) {
                bind.letterCheckViews.visibility = View.VISIBLE
                //bind.letterCheckVs.visibility = View.VISIBLE
                bind.letterCheckLLs.visibility = View.VISIBLE
                bind.letterCheckCLs.visibility = View.VISIBLE
                bind.letterCheckApplys.text = it.xfToReviewEntityList[0].reExplain
                bind.letterCheckResults.text = it.xfToReviewEntityList[0].reResult
                bind.letterCheckOpinions.text = it.xfToReviewEntityList[0].reOpinion
            }

            //作废
            if (it.xfCancelEntityList.isNotEmpty()) {
                bind.letterCancelCLs.visibility = View.VISIBLE
                bind.letterCancelLine.visibility = View.VISIBLE
                bind.letterCancelLLs.visibility = View.VISIBLE
                bind.letterCancel.text = it.xfCancelEntityList[0].applyCacel
                bind.letterCancelResults.text = it.xfCancelEntityList[0].cancelState
            }

            //业务文书
            if (it.xfDocMakeDTOList.isNotEmpty()) {
                bind.writLine.visibility = View.VISIBLE
                bind.writLinear.visibility = View.VISIBLE
                bind.recyclerRecord.init(LinearLayoutManager(this), mAdpPostpone)
                mAdpPostpone.data.addAll(it.xfDocMakeDTOList)
                mAdpPostpone.notifyDataSetChanged()
            }

            if (title == "作废审核") {
                auuid = it.xfAcceptInfoEntity.uuid
                cuuid = it.xfCancelEntityList[0].uuid
            }


            bind.toPostpone.setOnClickListener { _ ->
                routerResultCode(
                    IRoute.letter_postpone,
                    2000,
                    "xfAcceptInfoUuid" to it.xfAcceptInfoEntity.uuid
                )
            }
            //申请/审核
            bind.toApply.setOnClickListener { _ ->
                if (title == "作废审核") {
                    addCaseApply("通过")
                    return@setOnClickListener
                }
                if (title?.contains("申请") == true) {
                    routerResultCode(
                        IRoute.letter_apply,
                        2000,
                        "title" to title,
                        "xfAcceptInfoUuid" to it.xfAcceptInfoEntity.uuid,
                        "exDepartUuid" to if (it.xfReviewEntityList.isNotEmpty()) it.xfReviewEntityList[0].exDepartUuid else ""
                    )
                } else if (title?.contains("审核") == true) {
                    routerResultCode(
                        IRoute.letter_check,
                        2000,
                        "title" to title,
                        "xfAcceptInfoUuid" to it.xfAcceptInfoEntity.uuid,
                        "xfReviewUuid" to if (it.xfReviewEntityList.isEmpty()) "" else it.xfReviewEntityList[0]?.uuid
                            ?: "",
                        "xfToReviewUuid" to if (it.xfToReviewEntityList.isEmpty()) "" else it.xfToReviewEntityList[0]?.uuid
                            ?: "",
                    )
                }
            }

            when (title) {
                "信访办理" -> {
                    if (it.xfAcceptInfoEntity.handleStatus == "待处理") {
                        bind.toTransactLL.visibility = View.VISIBLE
                    }
                }
                "信访查询" -> {}
                "作废审核" -> {
                    bind.toApply.text = "通过"
                    bind.toApply.visibility = View.VISIBLE
                    bind.toCancels.visibility = View.VISIBLE
                }
                "复查申请" -> {
                    if (it.xfReviewEntityList == null || it.xfReviewEntityList.isEmpty()) {
                        bind.toApply.text = title
                        bind.toApply.visibility = View.VISIBLE
                    }
                }
                "复查审核" -> {
                    if (it.xfReviewEntityList.isNotEmpty()) {
                        bind.toApply.text = title
                        bind.toApply.visibility = View.VISIBLE
                        try {
                            if (it.xfReviewEntityList[0].reOpinion != null && it.xfReviewEntityList[0].reOpinion != ""){
                                bind.toApply.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                        }
                    }
                }
                "复核审核" -> {
                    if (it.xfToReviewEntityList.isNotEmpty()) {
                        bind.toApply.text = title
                        bind.toApply.visibility = View.VISIBLE
                        try {
                            if (it.xfToReviewEntityList[0].reOpinion != null && it.xfToReviewEntityList[0].reOpinion != ""){
                                bind.toApply.visibility = View.GONE
                            }
                        } catch (e: Exception) {
                        }
                    }
                }
                "复核申请" -> {
                    if (it.xfToReviewEntityList == null || it.xfToReviewEntityList.isEmpty()) {
                        bind.toApply.text = title
                        bind.toApply.visibility = View.VISIBLE
                    }
                }
                else -> {
                    bind.toApply.text = title
                    bind.toApply.visibility = View.VISIBLE
                }
            }
//办理情况
            bind.toTransact.setOnClickListener { it1 ->
                routerResultCode(
                    IRoute.letter_transact,
                    2000,
                    "xfacceptInfoUuid" to it.xfAcceptInfoEntity.uuid
                )
            }


        }


        //转办
        //bind.toTurn.setOnClickListener { router(IRoute.letter_manage, "type" to 1) }
        //交办
        //bind.toAssign.setOnClickListener { router(IRoute.letter_manage, "type" to 2) }
        request.addCaseApplyResult.observe(this) {
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

    private fun addCaseApply(str: String) {
        showLoading()
        request.addCaseApply(
            com.sszt.basis.network.Url.agent + "applets/cancel/checkCancel",
            requestForBody(
                "xfAcceptInfoUuid" to auuid,
                "xfCancelUuid" to cuuid,
                "cancelState" to str,
            )
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 1999) {
            setResult(1999)
            finish()
        }
    }

    private fun getDecoder(ss: String, i: Int): String? {
        var decodedBytes: ByteArray? = ByteArray(0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            decodedBytes = Base64.getDecoder().decode(ss)
        }
        return String(decodedBytes!!)?.replace(
            "#{applyName}",
            mAdpPostpone.data[i].applyName ?: ""
        )?.replace(
            "#{completionDate}",
            mAdpPostpone.data[i].completionDate ?: ""
        )?.replace(
            "#{noAcceptReason}",
            mAdpPostpone.data[i].noAcceptReason ?: ""
        )?.replace(
            "#{extensionDays}",
            mAdpPostpone.data[i].extensionDays ?: ""
        )?.replace(
            "#{applyDate}",
            mAdpPostpone.data[i].applyDate ?: ""
        )?.replace(
            "#{currentDate}",
            mAdpPostpone.data[i].currentDate ?: ""
        )?.replace(
            "#{createDate}",
            mAdpPostpone.data[i].createDate ?: ""
        )?.replace(
            "#{problemDescription}",
            mAdpPostpone.data[i].problemDescription ?: ""
        )?.replace(
            "#{diaochaQingk}",
            mAdpPostpone.data[i].diaochaQingk ?: ""
        )?.replace(
            "#{handleOpinion}",
            mAdpPostpone.data[i].handleOpinion ?: ""
        )?.replace(
            "#{orgName}",
            mAdpPostpone.data[i].orgName ?: ""
        )?.replace(
            "#{orgName}",
            mAdpPostpone.data[i].orgName ?: ""
        )?.replace(
            "#{name}",
            mAdpPostpone.data[i].name ?: ""
        )?.replace(
            "#{num}",
            mAdpPostpone.data[i].num ?: ""
        )?.replace(
            "#{type}",
            mAdpPostpone.data[i].type ?: ""
        )?.replace(
            "#{address}",
            mAdpPostpone.data[i].address ?: ""
        )?.replace(
            "#{deliveryTime}",
            mAdpPostpone.data[i].deliveryTime ?: ""
        )?.replace(
            "#{opinions}",
            mAdpPostpone.data[i].opinions ?: ""
        )?.replace(
            "#{reason}",
            mAdpPostpone.data[i].reason ?: ""
        )?.replace(
            "#{recipientSign}",
            mAdpPostpone.data[i].recipientSign ?: ""
        )?.replace(
            "#{notes}",
            mAdpPostpone.data[i].notes ?: ""
        )?.replace(
            "#{createName}",
            mAdpPostpone.data[i].createName ?: ""
        )?.replace(
            "#{parentOrgName}",
            mAdpPostpone.data[i].parentOrgName ?: ""
        )?.replace(
            "#{userName}",
            mAdpPostpone.data[i].userName ?: ""
        )
    }
}