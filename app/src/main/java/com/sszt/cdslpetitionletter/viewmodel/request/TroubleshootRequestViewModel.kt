package com.sszt.cdslpetitionletter.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.ActivityUtils
import com.sszt.basis.base.viewmodel.BaseViewModel
import com.sszt.basis.ext.request
import com.sszt.basis.ext.requestNoCheck
import com.sszt.basis.network.ListDataUiState
import com.sszt.basis.bean.PublicBean
import com.sszt.basis.ext.router
import com.sszt.cdslpetitionletter.bean.*
import com.sszt.cdslpetitionletter.network.apiService
import com.sszt.resources.IRoute
import okhttp3.RequestBody

/**
 * <b>标题：</b> 信访隐患排查 viewmodel <br>
 * <b>描述：</b> 用于分离view与data <br>
 * <b>设计：</b>zcr<br>
 * <b>创建：</b>2023/06/2<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 */
class TroubleshootRequestViewModel : BaseViewModel() {


    /**
     * 信访隐患排查-列表
     */
    val getTroubleshootListResult = MutableLiveData<ListDataUiState<TroubleshootListItemBean>>()

    fun getTroubleshootList(
        url:String,requestBody: RequestBody, isRefresh: Boolean, pageIndexInt: Int
    ) {
        request({ apiService.getTroubleshootList(url,requestBody) }, {
            val listDataUiState = ListDataUiState(
                isSuccess = true,
                isFirstEmpty = it.list?.isEmpty() == true && pageIndexInt == 1,
                isEmpty = it.list?.isEmpty() == true,
                hasMore = (it.total ?: 0 < it.pages ?: 0),
                isRefresh = isRefresh,
                listData = it.list ?: arrayListOf()
            )
            getTroubleshootListResult.value = listDataUiState
        }, {
            val listDataUiState = ListDataUiState(
                isSuccess = false,
                isRefresh = isRefresh,
                listData = arrayListOf<TroubleshootListItemBean>(),
                errMessage = it.errorMsg
            )
            getTroubleshootListResult.value = listDataUiState
        })
    }


    /**
     * 信访隐患排查-添加
     */
    val insertTroubleshootResult = MutableLiveData<PublicBean>()
    fun insertTroubleshoot(requestBody: RequestBody) {
        requestNoCheck({ apiService.insertTroubleshoot(requestBody) },
            { insertTroubleshootResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                insertTroubleshootResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)
            })
    }

    /**
     * 信访隐患排查-评估审核
     */
    val insertAssessCheckResult = MutableLiveData<PublicBean>()
    fun insertAssessCheck(requestBody: RequestBody) {
        requestNoCheck({ apiService.insertAssessCheck(requestBody) },
            { insertAssessCheckResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                insertAssessCheckResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)
            })
    }

    /**
     * 信访隐患排查-挂牌
     */
    val insertHangApplyResult = MutableLiveData<PublicBean>()
    fun insertHangApply(requestBody: RequestBody) {
        requestNoCheck({ apiService.insertHangApply(requestBody) },
            { insertHangApplyResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                insertHangApplyResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)
            })
    }



    /**
     * 信访隐患排查-摘牌
     */
    val insertPickApplyResult = MutableLiveData<PublicBean>()
    fun insertPickApply(requestBody: RequestBody) {
        requestNoCheck({ apiService.insertPickApply(requestBody) },
            { insertPickApplyResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                insertPickApplyResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)
            })
    }



    /**
     * 信访隐患排查-摘牌审核
     */
    val insertPickCheckResult = MutableLiveData<PublicBean>()
    fun insertPickCheck(requestBody: RequestBody) {
        requestNoCheck({ apiService.insertPickCheck(requestBody) },
            { insertPickCheckResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                insertPickCheckResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)
            })
    }





    /**
     * 信访隐患排查详情
     */
    val getTroubleshootDetailResult = MutableLiveData<TroubleshootListItemBean>()
    fun getTroubleshootDetail(requestBody: RequestBody) {
        request({ apiService.getTroubleshootDetail(requestBody) },
            { getTroubleshootDetailResult.value = it }
           )
    }

    /**
     * 评价详情
     */
    val getLetterCommentResult = MutableLiveData<LetterDetailBean>()
    fun getLetterComment(requestBody: RequestBody) {
        request(
            { apiService.getLetterComment(requestBody) },
            { getLetterCommentResult.value = it },
        )
    }


    /**
     * 信访评价
     */
    val addLetterCommentResult = MutableLiveData<PublicBean>()
    fun addLetterComment(requestBody: RequestBody) {
        requestNoCheck({ apiService.addLetterComment(requestBody) },
            { addLetterCommentResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                addLetterCommentResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)
            })
    }


    /**
     * 信访办理
     */
    val addLetterTransactResult = MutableLiveData<PublicBean>()
    fun addLetterTransact(requestBody: RequestBody) {
        requestNoCheck({ apiService.addLetterTransact(requestBody) },
            { addLetterTransactResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                addLetterTransactResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)
            })
    }


    /**
     * 信访申请
     */
    val addCaseApplyResult = MutableLiveData<PublicBean>()
    fun addCaseApply(url: String, requestBody: RequestBody) {
        requestNoCheck({ apiService.addCaseApply(url, requestBody) },
            { addCaseApplyResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                addCaseApplyResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)
            })
    }

    /**
     * 延期申请
     */
    val postponeResult = MutableLiveData<PublicBean>()
    fun postPone(requestBody: RequestBody) {
        requestNoCheck({ apiService.postPone(requestBody) },
            { postponeResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                postponeResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)
            })
    }


    /**
     * 延期部门
     */
    val getParentOrgResult = MutableLiveData<ParentOrgBean?>()
    fun getParentOrg() {
        requestNoCheck({ apiService.getParentOrg() },
            { getParentOrgResult.value = it.data })
    }


}