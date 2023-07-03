package com.sszt.cdslpetitionletter.bean

data class AssistRecordBean(
    val endRow: Int,
    val firstPage: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val lastPage: Int,
    val list: ArrayList<AssistRecordItemBean>,
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

data class AssistRecordItemBean(
    val createTime: String,
    val helpAddr: String,
    val helpAddrName: String,
    val helpEffect: String,
    val helpFile: String,
    val helpFileList: ArrayList<AssistRecordFileBean>,
    val helpMeasures: String,
    val helpTime: String,
    val id: Int,
    val personnelName: String,
    val personnelUuid: String,
    val uuid: String
)

data class AssistRecordFileBean(
    val title : String,
    val fileUrl : String,
    val uuid : String,
)