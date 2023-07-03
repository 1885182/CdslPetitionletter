package com.sszt.cdslpetitionletter.bean

data class WorkbenchBean(
    val name: String,
    val child: ArrayList<WorkbenchChildBean>
)

data class WorkbenchChildBean(
    val name: String,
    val pic: Int,
    val path: String,
    val url: String? = null
)