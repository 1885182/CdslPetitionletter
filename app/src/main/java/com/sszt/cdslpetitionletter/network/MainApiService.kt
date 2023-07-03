package com.sszt.cdslpetitionletter.network


import com.sszt.basis.bean.PublicBean
import com.sszt.basis.network.ApiResponse
import com.sszt.basis.network.Url
import com.sszt.cdslpetitionletter.bean.*
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*
import kotlin.collections.ArrayList

/**
 * <b>标题：</b>  工作管理模块网络API <br>
 * <b>描述：</b> 工作管理模块网络API <br>
 * <b>设计：</b>mengwenyue<br>
 * <b>创建：</b>2021/11/12 11:05<br>
 * <b>更新：</b>时间： 更新人： 更新内容：<br>
 * <b>审查：</b>时间： 审查人： 审查情况：<br>
 * <b>抽查：</b>时间： 抽查人： 抽查情况：<br>
 *
 * @author mengwenyue
 * @version V1.0.0
 */
interface MainApiService {

    companion object {

        val SERVER_URL = Url.url


    }


    /**
     * 登录
     */
    @POST(Url.agent + "auth/login")
    suspend fun login(
        @Body requestBody: RequestBody
    ): ApiResponse<LoginBean>


    /**
     * 修改密码
     */
    @POST(Url.agent + "base/user/modifyPwd")
    suspend fun updatePas(
        @Body requestBody: RequestBody
    ): ApiResponse<PublicBean>


    /**
     * 忘记密码
     */
    @POST(Url.agent + "base/user/updateInfo")
    suspend fun forgetPas(
        @Body requestBody: RequestBody
    ): ApiResponse<PublicBean>



    /**
     * 获取验证码
     */
    @POST(Url.agent + "auth/sms")
    suspend fun getVerificationCode(
        @Body requestBody: RequestBody
    ): ApiResponse<PublicBean>





    /**
     * 验证手机号
     */
    @POST(Url.agent + "auth/checkYzm")
    suspend fun checkPhone(
        @Body requestBody: RequestBody
    ): ApiResponse<PublicBean>




    /**
     * 验证码登录
     */
    @POST(Url.agent + "auth/mobileLogin")
    suspend fun loginCode(
        @Body requestBody: RequestBody
    ): ApiResponse<UserBean>



    /**
     * 机构列表
     */
    @POST(Url.agent + "case/mediation/institution/list")
    suspend fun getMediateList(
        @Body requestBody: RequestBody
    ): ApiResponse<MediateInstitutionBean>

    /**
     * 案件列表
     */
    @POST(Url.agent + "case/info/getCaseInfoList")
    suspend fun getCaseList(
        @Body requestBody: RequestBody
    ): ApiResponse<CaseListBean>

    /**
     * 添加案件
     */
    @POST(Url.agent + "case/info/addCaseInfo")
    suspend fun caseSave(
        @Body requestBody: RequestBody
    ): PublicBean

    /**
     * 添加笔录
     */
    @POST(Url.agent + "case/record/addCaseRecord")
    suspend fun addCaseRecord(
        @Body requestBody: RequestBody
    ): PublicBean

    /**
     * 信访办理
     */
    @POST(Url.agent + "applets/xfInfoQuery/ownHandle")
    suspend fun addLetterTransact(
        @Body requestBody: RequestBody
    ): PublicBean

    /**
     * 信访评价
     */
    @POST(Url.agent + "applet/reply/doReply")
    suspend fun addLetterComment(
        @Body requestBody: RequestBody
    ): PublicBean

    /**
     * 信访申请
     */
    @POST
    suspend fun addCaseApply(
        @retrofit2.http.Url url: String?,
        @Body requestBody: RequestBody
    ): PublicBean

    /**
     * 延期申请
     */
    @POST(Url.agent + "applets/xfInfoQuery/applyExtension")
    suspend fun postPone(
        @Body requestBody: RequestBody
    ): PublicBean

    /**
     * 延期部门
     */
    @POST(Url.agent + "applets/xfInfoQuery/getParentOrg")
    suspend fun getParentOrg(): ApiResponse<ParentOrgBean>

    /**
     * 案件受理
     */
    @POST(Url.agent + "case/process/handleCaseInfo")
    suspend fun acceptCase(
        @Body requestBody: RequestBody
    ): PublicBean

    /**
     * 获取案件文书
     */
    @GET(Url.agent + "case/wrid/get")
    suspend fun getCaseWrit(
        @Query("uuid") uuid: String
    ): ApiResponse<CaseWritDetailBean>


    /**
     * 案件详情
     */
    @GET(Url.agent + "case/info/getCaseInfoVo")
    suspend fun getCaseDetail(
        @Query("uuid") uuid: String
    ): ApiResponse<CaseDetailBean>

    /**
     * 查看笔录
     */
    @GET(Url.agent + "case/record/getCaseRecordByCaseUuid")
    suspend fun getCaseRecord(
        @Query("caseUuid") uuid: String
    ): ApiResponse<CaseRecordBean>


    /**
     * 信访列表
     */
    @POST
    suspend fun getPetitionLetterList(
        @retrofit2.http.Url url: String?,
        @Body requestBody: RequestBody
    ): ApiResponse<PetitionLetterListBean>

    /**
     * 信访详情
     */
    @POST(Url.agent + "xfInfo/needDo/getInfoList")
    suspend fun getLetterDetail(
        @Body requestBody: RequestBody
    ): ApiResponse<LetterDetailBean>




    /**
     * 评价详情
     */
    @POST(Url.agent + "applet/reply/getInfoList")
    suspend fun getLetterComment(
        @Body requestBody: RequestBody
    ): ApiResponse<LetterDetailBean>


    /**
     * 添加民情
     */
    @POST(Url.agent + "xfpopular/account/insertPopularAccount")
    suspend fun insertSentiment(
        @Body requestBody: RequestBody
    ): PublicBean




    /**
     * 民情列表
     */
    @POST(Url.agent + "xfpopular/account/getPageList")
    suspend fun getSentimentList(
        @Body requestBody: RequestBody
    ): ApiResponse<SentimentListBean>

    /**
     * 民情详情
     */
    @POST(Url.agent + "xfpopular/account/getXfPopularAccount")
    suspend fun getSentimentDetail(
        @Body requestBody: RequestBody
    ): ApiResponse<SentimentListItemBean>


    /**
     * 民情-选择街镇
     */
    @POST(Url.agent + "base/org/listTree")
    suspend fun getTownDetail(
        @Body requestBody: RequestBody
    ): ApiResponse<SentimentListItemBean>




    /**
     * 民情-选择村社
     */
    @POST(Url.agent + "applets/xfpopular/account/getChongQingVillageNameList")
    suspend fun getVillageList(
        @Body requestBody: RequestBody
    ): ApiResponse<ArrayList<VillageBean>>






    /**
     * 人员帮扶列表
     */
    @POST(Url.agent + "personnel/management/getPageList")
    suspend fun getAssistPeopleList(
        @Body requestBody: RequestBody
    ): ApiResponse<AssistPeopleListBean>


    /**
     * 人员帮扶详情
     */
    @POST(Url.agent + "personnel/management/getEntity")
    suspend fun getAssistPeopleDetail(
        @Body requestBody: RequestBody
    ): ApiResponse<AssistPeopleListItemBean>




    /**
     * 帮扶记录列表
     */
    @POST(Url.agent + "help/record/getPageList")
    suspend fun getAssistRecordList(
        @Body requestBody: RequestBody
    ): ApiResponse<AssistRecordBean>



    /**
     * 帮扶记录详情
     */
    @POST(Url.agent + "help/record/getEntity")
    suspend fun getAssistRecordDetail(
        @Body requestBody: RequestBody
    ): ApiResponse<AssistRecordItemBean>


    /**
     * 添加帮扶记录
     */
    @POST(Url.agent + "help/record/insert")
    suspend fun insertAssistRecord(
        @Body requestBody: RequestBody
    ): PublicBean


    /**
     * 排查登记列表
     */
    @POST(Url.agent + "screening/registration/getPageList")
    suspend fun getEventList(
        @Body requestBody: RequestBody
    ): ApiResponse<EventListBean>


    /**
     * 排查登记详情
     */
    @POST(Url.agent + "screening/registration/getEntity")
    suspend fun getEventDetail(
        @Body requestBody: RequestBody
    ): ApiResponse<EventListItemBean>


    /**
     * 添加排查登记
     */
    @POST(Url.agent + "screening/registration/insert")
    suspend fun insertEvent(
        @Body requestBody: RequestBody
    ): PublicBean


    /**
     * 通知公告列表
     */
    @POST(Url.agent + "notice/announcement/getPageList")
    suspend fun getMsgNotyList(
        @Body requestBody: RequestBody
    ): ApiResponse<MsgNotyBean>


    /**
     * 消息中心列表
     */
    @POST(Url.agent + "XfMessage/getPageList")
    suspend fun getMsgList(
        @Body requestBody: RequestBody
    ): ApiResponse<ArrayList<MsgBean>>





    /**
     * 修改消息未读
     */
    @POST(Url.agent + "XfMessage/insertEntity")
    suspend fun updateMsg(
        @Body requestBody: RequestBody
    ): ApiResponse<PublicBean>






    /**
     * 信访隐患排查列表
     */
    @POST
    suspend fun getTroubleshootList(
        @retrofit2.http.Url url: String,
        @Body requestBody: RequestBody
    ): ApiResponse<TroubleshootListBean>



    /**
     * 信访隐患排查-添加
     */
    @POST(Url.agent + "screening/registration/insert")
    suspend fun insertTroubleshoot(
        @Body requestBody: RequestBody
    ): PublicBean



    /**
     * 信访隐患排查-评估审核
     */
    @POST(Url.agent + "applet/reply/doReply")
    suspend fun insertAssessCheck(
        @Body requestBody: RequestBody
    ): PublicBean




    /**
     * 信访隐患排查-挂牌
     */
    @POST(Url.agent + "screening/registration/saveGPBrand")
    suspend fun insertHangApply(
        @Body requestBody: RequestBody
    ): PublicBean


    /**
     * 信访隐患排查-摘牌
     */
    @POST(Url.agent + "screening/registration/saveZPBrand")
    suspend fun insertPickApply(
        @Body requestBody: RequestBody
    ): PublicBean




    /**
     * 信访隐患排查-摘牌审核
     */
    @POST(Url.agent + "screening/registration/saveSHBrand")
    suspend fun insertPickCheck(
        @Body requestBody: RequestBody
    ): PublicBean




    /**
     * 信访隐患排查-详情
     */
    @POST(Url.agent + "screening/registration/getEntity")
    suspend fun getTroubleshootDetail(
        @Body requestBody: RequestBody
    ): ApiResponse<TroubleshootListItemBean>


}