package com.sszt.cdslpetitionletter.bean

data class PetitionLetterListBean(
    val endRow: Int,
    val firstPage: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val lastPage: Int,
    val list: ArrayList<PetitionLetterListItemBean>,
    val navigateFirstPage: Int,
    val navigateLastPage: Int,
    val navigatePages: Int,
    val navigatepageNums: List<Int>,
    val nextPage: Int,
    val pageNum: Int,
    val pageSize: Int,
    val pages: Int,
    val prePage: Int,
    val size: Int,
    val startRow: Int,
    val total: Int
)


data class PetitionLetterListItemBean(
    val applyName: String,
    val businessNumber: String?,
    val cancelState: Any,
    val chengbanCompanyName: Any,
    val complainTitle: String,
    val createTime: String,
    val dengjiHandleMode: Any,
    val dengjiXingshi: String,
    val exDepartUuid: Any,
    val handleStatus: String,
    val idCard: String,
    val jiaobanCompany: Any,
    val overtimeRemind: Any,
    val phone: String,
    val problemFenlei: String,
    val replyState: String?,
    val supHandleStatus: Any,
    val xfAcceptInfoUuid: String,
    val xfApplyerInfoUuid: String,
    val xfApplyerReplyUuid: Any,
    val xfCancelUuid: Any,
    val xfReviewUuid: Any,
    val xfToReviewUuid: Any,
    val xfVisitInfoUuid: String,
    val xinfangMudi: String
)