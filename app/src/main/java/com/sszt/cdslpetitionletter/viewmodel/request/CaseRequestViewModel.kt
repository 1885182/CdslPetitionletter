package com.sszt.cdslpetitionletter.viewmodel.request

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.SPUtils
import com.sszt.basis.base.viewmodel.BaseViewModel
import com.sszt.basis.ext.request
import com.sszt.basis.ext.requestNoCheck
import com.sszt.basis.network.ListDataUiState
import com.sszt.basis.bean.PublicBean
import com.sszt.cdslpetitionletter.bean.*
import com.sszt.cdslpetitionletter.network.apiService
import okhttp3.RequestBody

/**
 * <b>标题：</b> 矛盾纠纷 viewmodel <br>
 * <b>描述：</b> 用于分离view与data <br>
 * <b>设计：</b>zcr<br>
 * <b>创建：</b>2023/05/11<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 */
class CaseRequestViewModel : BaseViewModel() {

    /**
     * 登录
     */
    val loginResul = MutableLiveData<PublicBean>()

    fun login(requestBody: RequestBody) {
        request({ apiService.login(requestBody) }, {
            SPUtils.getInstance().put("token", it.token ?: "")
            SPUtils.getInstance().put("imgUrl", it.user.imgUrl ?: "")
            SPUtils.getInstance().put("name", it.user.name ?: "")
            SPUtils.getInstance().put("userName", it.user.username ?: "")
            SPUtils.getInstance().put("mobile", it.user.mobile ?: "")
            loginResul.value = PublicBean(2000, "登陆成功")
        }, {
            loginResul.value = PublicBean(it.errCode, it.errorMsg)
        })
    }

    /**
     * 机构列表
     */
    val getMediateListResult = MutableLiveData<ListDataUiState<ListBean>>()

    fun getMediateList(requestBody: RequestBody, isRefresh: Boolean, pageIndexInt: Int) {
        request({ apiService.getMediateList(requestBody) }, {
            val listDataUiState = ListDataUiState(
                isSuccess = true,
                isFirstEmpty = it.list?.isEmpty() == true && pageIndexInt == 1,
                isEmpty = it.list?.isEmpty() == true,
                hasMore = (it.total ?: 0 < it.pages ?: 0),
                isRefresh = isRefresh,
                listData = it.list ?: arrayListOf()
            )
            getMediateListResult.value = listDataUiState
        }, {
            val listDataUiState = ListDataUiState(
                isSuccess = false,
                isRefresh = isRefresh,
                listData = arrayListOf<ListBean>(),
                errMessage = it.errorMsg
            )
            getMediateListResult.value = listDataUiState
        })
    }

    /**
     * 案件列表
     */
    val getCaseResult = MutableLiveData<ListDataUiState<CaseListItemBean>>()
    fun getCaseList(requestBody: RequestBody, isRefresh: Boolean, pageIndexInt: Int) {
        request({ apiService.getCaseList(requestBody) }, {
            val listDataUiState = ListDataUiState(
                isSuccess = true,
                isFirstEmpty = it.list?.isEmpty() == true && pageIndexInt == 1,
                isEmpty = it.list?.isEmpty() == true,
                hasMore = (it.total ?: 0 < it.pages ?: 0),
                isRefresh = isRefresh,
                listData = it.list ?: arrayListOf()
            )
            getCaseResult.value = listDataUiState
        }, {
            val listDataUiState = ListDataUiState(
                isSuccess = false,
                isRefresh = isRefresh,
                listData = arrayListOf<CaseListItemBean>(),
                errMessage = it.errorMsg
            )
            getCaseResult.value = listDataUiState
        })
    }


    /**
     * 案件提交
     */
    val saveResult = MutableLiveData<PublicBean>()
    fun caseSave(requestBody: RequestBody) {
        requestNoCheck({ apiService.caseSave(requestBody) },
            { saveResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                saveResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)

            })
    }

    /**
     * 添加笔录
     */
    val addCaseRecordResult = MutableLiveData<PublicBean>()
    fun addCaseRecord(requestBody: RequestBody) {
        requestNoCheck({ apiService.addCaseRecord(requestBody) },
            { addCaseRecordResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                addCaseRecordResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)
            })
    }


    /**
     * 案件详情
     */
    val getCaseDetailResult = MutableLiveData<CaseDetailBean>()
    fun getCaseDetail(uuid: String) {
        request({ apiService.getCaseDetail(uuid) }, { getCaseDetailResult.value = it })
    }

    /**
     * 查看笔录
     */
    val getCaseRecordResult = MutableLiveData<CaseRecordBean>()
    fun getCaseRecord(uuid: String) {
        request(
            { apiService.getCaseRecord(uuid) },
            { getCaseRecordResult.value = it },
        )
    }


    /**
     * 案件受理
     */
    val acceptCaseResult = MutableLiveData<PublicBean>()
    fun acceptCase(requestBody: RequestBody) {
        requestNoCheck({ apiService.acceptCase(requestBody) },
            { acceptCaseResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                acceptCaseResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)
            })
    }


    /**
     * 获取案件文书
     */
    val getCaseWritResult = MutableLiveData<CaseWritDetailBean>()
    fun getCaseWrit(uuid: String) {
        request(
            { apiService.getCaseWrit(uuid) },
            { getCaseWritResult.value = it },
        )
    }


}