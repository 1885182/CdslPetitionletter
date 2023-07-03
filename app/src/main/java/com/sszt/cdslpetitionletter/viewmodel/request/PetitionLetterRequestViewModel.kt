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
 * <b>标题：</b> 信访 viewmodel <br>
 * <b>描述：</b> 用于分离view与data <br>
 * <b>设计：</b>zcr<br>
 * <b>创建：</b>2023/05/17<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 */
class PetitionLetterRequestViewModel : BaseViewModel() {


    /**
     * 信访列表
     */
    val getPetitionLetterListResult = MutableLiveData<ListDataUiState<PetitionLetterListItemBean>>()

    fun getPetitionLetterList(
        url: String, requestBody: RequestBody, isRefresh: Boolean, pageIndexInt: Int
    ) {
        request({ apiService.getPetitionLetterList(url, requestBody) }, {
            val listDataUiState = ListDataUiState(
                isSuccess = true,
                isFirstEmpty = it.list?.isEmpty() == true && pageIndexInt == 1,
                isEmpty = it.list?.isEmpty() == true,
                hasMore = (it.total ?: 0 < it.pages ?: 0),
                isRefresh = isRefresh,
                listData = it.list ?: arrayListOf()
            )
            getPetitionLetterListResult.value = listDataUiState
        }, {
            val listDataUiState = ListDataUiState(
                isSuccess = false,
                isRefresh = isRefresh,
                listData = arrayListOf<PetitionLetterListItemBean>(),
                errMessage = it.errorMsg
            )
            getPetitionLetterListResult.value = listDataUiState
        })
    }

    /**
     * 信访详情
     */
    val getLetterDetailResult = MutableLiveData<LetterDetailBean>()
    val getLetterDetailError = MutableLiveData<PublicBean>()
    fun getLetterDetail(requestBody: RequestBody) {
        request({ apiService.getLetterDetail(requestBody) },
            { getLetterDetailResult.value = it },
            { getLetterDetailError.value = PublicBean(it.errCode, it.errorMsg) })
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