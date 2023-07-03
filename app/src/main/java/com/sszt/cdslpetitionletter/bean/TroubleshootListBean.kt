package com.sszt.cdslpetitionletter.bean

import com.sszt.basis.bean.FileBean

data class TroubleshootListBean(
    val endRow: Int,
    val firstPage: Int,
    val hasNextPage: Boolean,
    val hasPreviousPage: Boolean,
    val isFirstPage: Boolean,
    val isLastPage: Boolean,
    val lastPage: Int,
    val list: ArrayList<TroubleshootListItemBean>,
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


data class TroubleshootListItemBean(
    val cardNum: String,
    val cardType: String,
    val caseLatitude: Any,
    val gpList: ArrayList<TroubleshootGPBean>?,
    val zpList: ArrayList<TroubleshootGPBean>?,
    val caseLongitude: Any,
    val caseMemo: String,
    val caseName: String,
    val caseNum: String,
    val caseScale: String,
    val caseSource: String,
    val caseTime: String,
    val caseType: String,
    val dissolveCase: String,
    val dissolveMemo: String,
    val dissolveName: String,
    val dissolveOk: String,
    val dissolveOrg: String,
    val dissolvePhone: String,
    val dissolveXian: String,
    val examineState: String,
    val fashengTime: String,
    val id: Int,
    val placeOccurrence: String,
    val placeOccurrenceName: String,
    val popAddr: String,
    val popEducation: String,
    val popName: String,
    val popNation: String,
    val popSex: String,
    val popType: String,
    val unitsInvolved: String,
    val unitsNum: Int,
    val uuid: String,
)

data class TroubleshootGPBean(
    val gpFileList :ArrayList<FileBean>?,
    val zpFileList :ArrayList<FileBean>?,
    val gpContent: String,


    val dissolveCase: String,
    val dissolveOrg: String,
    val dissolveName: String,
    val dissolvePhone: String,
    val dissolveOk: String,
    val dissolveMemo: String,
    val dissolveContent: String,
    val examineState: String,
    val examineName: String,
    val examineUuid: String,
    val examineTime: String,
    val examineContent: String,
    val uuid: String,
)