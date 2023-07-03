package com.sszt.cdslpetitionletter.bean

data class AssistPeopleListBean(
    val endRow: Int,
    val firstPage: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val lastPage: Int,
    val list: ArrayList<AssistPeopleListItemBean>,
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

data class AssistPeopleListItemBean(
    val cardId: String,
    val cardType: String,
    val createTime: String,
    val helpMethod: String,
    val helpOrg: String,
    val helpPhone: String,
    val helpPop: String,
    val helpShuxing: String,
    val id: Int,
    val popAddr: String,
    val popEducation: String,
    val popHealthy: String,
    val popLabour: String,
    val popMarriage: String,
    val popName: String,
    val popNation: String,
    val popPhone: String,
    val popSex: String,
    val state: String,
    val stateShuoMing: Any,
    val uuid: String
)