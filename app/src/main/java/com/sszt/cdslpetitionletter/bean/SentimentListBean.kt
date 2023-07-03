package com.sszt.cdslpetitionletter.bean

data class SentimentListBean(
    val endRow: Int,
    val firstPage: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val lastPage: Int,
    val list: ArrayList<SentimentListItemBean>,
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

data class SentimentListItemBean(
    val accountName: String,
    val accountType: String,
    val dengjiTime: String,
    val detailedAddress: String,
    val enclosure: String,
    val helpContent: String,
    val id: Any,
    val laiyuan: String,
    val phoneNum: String,
    val streetTown: ArrayList<String>,
    val streetTownName: String,
    val uuid: String,
    val villageCommunity: String,
    val commAttachment: AssistRecordFileBean
)