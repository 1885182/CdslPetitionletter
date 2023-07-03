package com.sszt.cdslpetitionletter.bean

data class MediateInstitutionBean(
    val endRow: Int,
    val firstPage: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val lastPage: Int,
    val list: ArrayList<ListBean>,
    val navigateFirstPage: Int,
    val navigateLastPage: Int,
    val navigatePages: Int,
    val navigatepageNums: List<Any>,
    val nextPage: Int,
    val pageNum: Int,
    val pageSize: Int,
    val pages: Int,
    val prePage: Int,
    val size: Int,
    val startRow: Int,
    val total: Int
)


data class ListBean(
    val caseMediationPeopleList: Any,
    val createTime: Any,
    val createUserName: Any,
    val createUserUuid: Any,
    val gridName: Any,
    val gridUuid: Any,
    val id: Any,
    val institutionAddress: String?,
    val institutionCharge: String,
    val institutionIntroduction: Any,
    val institutionLat: Any,
    val institutionLong: Any,
    val institutionName: String?,
    val institutionPhone: Any,
    val institutionType: Int,
    val peopleNum: Int,
    val uuid: String
)