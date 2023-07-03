package com.sszt.cdslpetitionletter.viewmodel.request

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
 * <b>标题：</b> 民情收集/人员帮扶/排查登记 viewModel <br>
 * <b>描述：</b> 用于分离view与data <br>
 * <b>设计：</b>zcr<br>
 * <b>创建：</b>2023/05/17<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 */
class MainRequestViewModel : BaseViewModel() {


    /**
     * 民情列表
     */
    val getSentimentListResult = MutableLiveData<ListDataUiState<SentimentListItemBean>>()

    fun getSentimentList(requestBody: RequestBody, isRefresh: Boolean, pageIndexInt: Int) {
        request({ apiService.getSentimentList(requestBody) }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isFirstEmpty = it.list?.isEmpty() == true && pageIndexInt == 1,
                    isEmpty = it.list?.isEmpty() == true,
                    hasMore = (it.total ?: 0 < it.pages ?: 0),
                    isRefresh = isRefresh,
                    listData = it.list ?: arrayListOf()
                )
            getSentimentListResult.value = listDataUiState
        }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    isRefresh = isRefresh,
                    listData = arrayListOf<SentimentListItemBean>(),
                    errMessage = it.errorMsg
                )
            getSentimentListResult.value = listDataUiState
        })
    }

    /**
     * 民情-选择村社
     */
    val getVillageListResult = MutableLiveData<ArrayList<VillageBean>>()
    fun getVillageList(requestBody: RequestBody) {
        request(
            { apiService.getVillageList(requestBody) },
            { getVillageListResult.value = it },
        )
    }

    /**
     * 民情详情
     */
    val getSentimentDetailResult = MutableLiveData<SentimentListItemBean>()
    fun getSentimentDetail(requestBody: RequestBody) {
        request(
            { apiService.getSentimentDetail(requestBody) },
            { getSentimentDetailResult.value = it },
        )
    }


    /**
     * 民情提交
     */
    val insertSentimentResult = MutableLiveData<PublicBean>()
    fun insertSentiment(requestBody: RequestBody) {
        requestNoCheck({ apiService.insertSentiment(requestBody) },
            { insertSentimentResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                insertSentimentResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)

            })
    }


    /**
     * 重点人员帮扶列表
     */
    val getAssistPeopleListResult = MutableLiveData<ListDataUiState<AssistPeopleListItemBean>>()

    fun getAssistPeopleList(requestBody: RequestBody, isRefresh: Boolean, pageIndexInt: Int) {
        request({ apiService.getAssistPeopleList(requestBody) }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isFirstEmpty = it.list?.isEmpty() == true && pageIndexInt == 1,
                    isEmpty = it.list?.isEmpty() == true,
                    hasMore = (it.total ?: 0 < it.pages ?: 0),
                    isRefresh = isRefresh,
                    listData = it.list ?: arrayListOf()
                )
            getAssistPeopleListResult.value = listDataUiState
        }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AssistPeopleListItemBean>(),
                    errMessage = it.errorMsg
                )
            getAssistPeopleListResult.value = listDataUiState
        })
    }


    /**
     * 重点人员帮扶详情
     */
    val getAssistPeopleDetailResult = MutableLiveData<AssistPeopleListItemBean>()
    fun getAssistPeopleDetail(requestBody: RequestBody) {
        request(
            { apiService.getAssistPeopleDetail(requestBody) },
            { getAssistPeopleDetailResult.value = it },
        )
    }


    /**
     * 帮扶记录列表
     */
    val getAssistRecordListResult = MutableLiveData<ListDataUiState<AssistRecordItemBean>>()

    fun getAssistRecordList(requestBody: RequestBody, isRefresh: Boolean, pageIndexInt: Int) {
        request({ apiService.getAssistRecordList(requestBody) }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isFirstEmpty = it.list?.isEmpty() == true && pageIndexInt == 1,
                    isEmpty = it.list?.isEmpty() == true,
                    hasMore = (it.total ?: 0 < it.pages ?: 0),
                    isRefresh = isRefresh,
                    listData = it.list ?: arrayListOf()
                )
            getAssistRecordListResult.value = listDataUiState
        }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AssistRecordItemBean>(),
                    errMessage = it.errorMsg
                )
            getAssistRecordListResult.value = listDataUiState
        })
    }


    /**
     * 帮扶记录详情
     */
    val getAssistRecordDetailResult = MutableLiveData<AssistRecordItemBean>()
    fun getAssistRecordDetail(requestBody: RequestBody) {
        request(
            { apiService.getAssistRecordDetail(requestBody) },
            { getAssistRecordDetailResult.value = it },
        )
    }


    /**
     * 添加帮扶记录
     */
    val insertAssistRecordResult = MutableLiveData<PublicBean>()
    fun insertAssistRecord(requestBody: RequestBody) {
        requestNoCheck({ apiService.insertAssistRecord(requestBody) },
            { insertAssistRecordResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                insertAssistRecordResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)

            })
    }


    /**
     * 排查登记列表
     */
    val getEventListResult = MutableLiveData<ListDataUiState<EventListItemBean>>()

    fun getEventList(requestBody: RequestBody, isRefresh: Boolean, pageIndexInt: Int) {
        request({ apiService.getEventList(requestBody) }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isFirstEmpty = it.list?.isEmpty() == true && pageIndexInt == 1,
                    isEmpty = it.list?.isEmpty() == true,
                    hasMore = (it.total ?: 0 < it.pages ?: 0),
                    isRefresh = isRefresh,
                    listData = it.list ?: arrayListOf()
                )
            getEventListResult.value = listDataUiState
        }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    isRefresh = isRefresh,
                    listData = arrayListOf<EventListItemBean>(),
                    errMessage = it.errorMsg
                )
            getEventListResult.value = listDataUiState
        })
    }


    /**
     * 排查登记详情
     */
    val getEventDetailResult = MutableLiveData<EventListItemBean>()
    fun getEventDetail(requestBody: RequestBody) {
        request(
            { apiService.getEventDetail(requestBody) },
            { getEventDetailResult.value = it },
        )
    }


    /**
     * 添加排查登记
     */
    val insertEventResult = MutableLiveData<PublicBean>()
    fun insertEvent(requestBody: RequestBody) {
        requestNoCheck({ apiService.insertEvent(requestBody) },
            { insertEventResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                insertEventResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)

            })
    }


    /**
     * 修改密码
     */
    val updatePasResult = MutableLiveData<PublicBean>()
    fun updatePas(requestBody: RequestBody) {
        requestNoCheck({ apiService.updatePas(requestBody) },
            { updatePasResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                updatePasResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)
            })
    }


    /**
     * 忘记密码
     */
    val forgetPasResult = MutableLiveData<PublicBean>()
    fun forgetPas(requestBody: RequestBody) {
        requestNoCheck({ apiService.forgetPas(requestBody) },
            { forgetPasResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                forgetPasResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)
            })
    }


    /**
     * 获取验证码
     */
    val getVerificationCodeResult = MutableLiveData<PublicBean>()
    fun getVerificationCode(requestBody: RequestBody) {
        requestNoCheck({ apiService.getVerificationCode(requestBody) },
            { getVerificationCodeResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                getVerificationCodeResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)
            })
    }


    /**
     * 验证手机号
     */
    val checkPhoneResult = MutableLiveData<PublicBean>()
    fun checkPhone(requestBody: RequestBody) {
        requestNoCheck({ apiService.checkPhone(requestBody) },
            { checkPhoneResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                checkPhoneResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)
            })
    }




    /**
     * 验证码登录
     */
    val loginCodeResult = MutableLiveData<PublicBean>()
    fun loginCode(requestBody: RequestBody) {
        request(
            { apiService.loginCode(requestBody) },
            {

                SPUtils.getInstance().put("token", it.accessToken ?: "")
                SPUtils.getInstance().put("imgUrl", it.imgUrl ?: "")
                SPUtils.getInstance().put("name", it.name ?: "")
                SPUtils.getInstance().put("userName", it.userName ?: "")
                SPUtils.getInstance().put("mobile", it.mobile ?: "")
                loginCodeResult.value = PublicBean(2000,"登录成功")
            }, {
                loginCodeResult.value = PublicBean(it.errCode,it.errorMsg)
            }
        )
    }


    /**
     * 通知公告列表
     */
    val getMsgNotyListResult = MutableLiveData<ListDataUiState<MsgNotyItemBean>>()

    fun getMsgNotyList(requestBody: RequestBody, isRefresh: Boolean, pageIndexInt: Int) {
        request({ apiService.getMsgNotyList(requestBody) }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isFirstEmpty = it.list?.isEmpty() == true && pageIndexInt == 1,
                    isEmpty = it.list?.isEmpty() == true,
                    hasMore = (it.total ?: 0 < it.pages ?: 0),
                    isRefresh = isRefresh,
                    listData = it.list ?: arrayListOf()
                )
            getMsgNotyListResult.value = listDataUiState
        }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    isRefresh = isRefresh,
                    listData = arrayListOf<MsgNotyItemBean>(),
                    errMessage = it.errorMsg
                )
            getMsgNotyListResult.value = listDataUiState
        })
    }


    /**
     * 消息中心列表
     */
    val getMsgListResult = MutableLiveData<ListDataUiState<MsgBean>>()

    fun getMsgList(requestBody: RequestBody, isRefresh: Boolean, pageIndexInt: Int) {
        request({ apiService.getMsgList(requestBody) }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isFirstEmpty = it?.isEmpty() == true && pageIndexInt == 1,
                    isEmpty = it?.isEmpty() == true,
                    hasMore = false,
                    isRefresh = isRefresh,
                    listData = it ?: arrayListOf()
                )
            getMsgListResult.value = listDataUiState
        }, {
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    isRefresh = isRefresh,
                    listData = arrayListOf<MsgBean>(),
                    errMessage = it.errorMsg
                )
            getMsgListResult.value = listDataUiState
        })
    }


    /**
     * 修改消息未读
     */
    val updateMsgResult = MutableLiveData<PublicBean>()
    fun updateMsg(requestBody: RequestBody) {
        requestNoCheck({ apiService.updateMsg(requestBody) },
            { updateMsgResult.value = PublicBean(code = it.code, msg = it.msg) },
            {
                updateMsgResult.value = PublicBean(code = it.errCode, msg = it.errorMsg)
            })
    }

}