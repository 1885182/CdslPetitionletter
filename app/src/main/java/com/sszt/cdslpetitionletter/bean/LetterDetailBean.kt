package com.sszt.cdslpetitionletter.bean

import com.sszt.basis.bean.FileBean

data class LetterDetailBean(
    val xfAcceptInfoEntity: XfAcceptInfoEntity,
    val xfAcceptStateRecordList: List<XfAcceptStateRecord>,
    val xfApplyerInfoEntity: XfApplyerInfoEntity,
    val xfApplyerReplyList: List<XfApplyerReplyBean>,//评价
    val xfCancelEntityList: List<XfCancelEntity>,
    val xfDubanInfoList: List<Any>,
    val xfReviewEntityList: List<XfReviewEntity>,
    val xfToReviewEntityList: List<XfReviewEntity>,
    val xfDocMakeDTOList: ArrayList<XfDocMakeDTOEntity>,
    val xfVisitInfoEntity: XfVisitInfoEntity
)

data class XfAcceptInfoEntity(
    val acceptCompany: String,
    val acceptCompanyUuid: String,
    val belongSys: String,
    val businessNumber: String,
    val chengbanCompany: Any,
    val chengbanCompanyUuid: Any,
    val createTime: String,
    val dengjiChengbanCompany: Any,
    val dengjiChengbanCompanyUuid: Any,
    val dengjiDepartName: Any,
    val dengjiDepartUuid: Any,
    val dengjiHandleMode: String,
    val dengjiJiaobanCompany: Any,
    val dengjiJiaobanCompanyUuid: Any,
    val dengjiUserName: Any,
    val dengjiUserUuid: Any,
    val dengjiXingshi: String,
    val dengjiZibanCompany: Any,
    val dengjiZibanCompanyUuid: Any,
    val diaochaQingk: Any,
    val handleCompany: String,
    val handleCompanyUuid: String,
    val handleMode: String,
    val handleOpinion: Any,
    val handlePeople: String,
    val handlePeopleUuid: String,
    val handleStatus: String,
    val id: Int,
    val infoGrade: String,
    val jianHuajie: Any,
    val jiaobanCompany: Any,
    val jiaobanCompanyUuid: Any,
    val jinjiChengdu: String,
    val lettersContent: Any,
    val lettersTitle: Any,
    val operMode: Any,
    val operTime: Any,
    val overtimeRemind: String,
    val relAttachmentList: Any,
    val relAttachments: Any,
    val shouliCompanyName: Any,
    val shouliCompanyUuid: Any,
    val statusFlag: Any,
    val supHandleOpinion: Any,
    val supHandleStatus: String,
    val uuid: String,
    val visitNature: String,
    val visitType: String,
    val xfApplyerInfoUuid: String,
    val yuejiYuanyin: Any,
    val zhiliuDays: Any,
    val zhujiZoufang: Any,
    val zhuzhiXingshi: Any
)

data class XfAcceptStateRecord(
    val diaochaQingk: Any,
    val handleOpinion: Any,
    val handleState: String,
    val handleType: String,
    val id: Int,
    val letterContent: Any,
    val letterTitle: Any,
    val operDepartName: String,
    val operDepartUuid: String,
    val operDoc: Any,
    val operMode: String,
    val operTime: String,
    val operUser: String,
    val operUserUuid: String,
    val relAttachmentList: Any,
    val relAttachments: Any,
    val uuid: String,
    val xfAcceptInfoUuid: String
)

data class XfApplyerInfoEntity(
    val address: String,
    val applyName: String,
    val area: String,
    val attachmentUuid: Any,
    val createTime: String,
    val email: Any,
    val id: Int,
    val idCard: String,
    val isLink: String,
    val phone: Any,
    val uuid: String,
    val xfApplyerLinkEntityList: List<XfApplyerLinkBean>
)

data class XfApplyerLinkBean(
    val uuid: String,
    val xfApplyerInfoUuid: String,
    val linkName: String
)

data class XfDocMakeDTOEntity(
    val docName: String,
    val docTemplate: String,
    val applyName: String,
    val completionDate: String,
    val currentDate: String,
    val noAcceptReason: String,
    val extensionDays: String,
    val applyDate: String,
    val createDate: String,
    val problemDescription: String,
    val diaochaQingk: String,
    val handleOpinion: String,
    val orgName: String,
    val parentOrgName: String,
    val name: String,
    val num: String,
    val type: String,
    val address: String,
    val deliveryTime: String,
    val opinions: String,
    val reason: String,
    val recipientSign: String,
    val notes: String,
    val createName: String,
    val userName: String,
)

data class XfApplyerReplyBean(
    val oneEvaluate: String,
    val oneReply: String,
    val twoEvaluate: String,
    val twoReply: String
)
data class XfReviewEntity(
    val uuid: String,
    val reExplain: String,
    val reResult: String,
    val exDepartUuid: String,
    val reOpinion: String
)

data class XfCancelEntity(
    val id: Int,
    val uuid: String,
    val xfAcceptInfoUuid: String,
    val applyCacel: String,
    val applyUserUuid: String,
    val applyUserName: String,
    val applyDepartUuid: String,
    val applyDepartName: String,
    val cancelState: String,
    val exUserUuid: String,
    val exUserName: String,
    val exDepartUuid: String,
    val exDepartName: String,
    val createTime: String,
    val isUse: String,
)

data class XfVisitInfoEntity(
    val attachmentUuids: Any,
    val beiCompany: Any,
    val beiLevel: Any,
    val beiZhiwei: Any,
    val commAttachmentList: ArrayList<FileBean>,
    val complainCi: String,
    val complainTitle: String,
    val createTime: String,
    val hotProblem: String,
    val id: Int,
    val isFayuan: Any,
    val isFuhe: Any,
    val isFuyi: Any,
    val isZhongcai: Any,
    val jianyiNeirong: Any,
    val jianyiTitle: Any,
    val problemAddress: String,
    val problemArea: String,
    val problemFenlei: String,
    val problemTime: String,
    val registMode: String,
    val shouXinren: String,
    val submitUserName: Any,
    val submitUserUuid: Any,
    val tousuNeirong: String,
    val uuid: String,
    val xfApplyerInfoUuid: String,
    val xinfangMudi: String
)