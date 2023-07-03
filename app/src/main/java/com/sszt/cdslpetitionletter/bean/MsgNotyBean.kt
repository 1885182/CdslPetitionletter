package com.sszt.cdslpetitionletter.bean

data class MsgNotyBean(
    val endRow: Int,
    val firstPage: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val lastPage: Int,
    val list: ArrayList<MsgNotyItemBean>,
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

data class MsgNotyItemBean(
    val announcementContent: String,
    val announcementTime: String,
    val announcementTitle: String,
    val id: Int,
    val uuid: String
)