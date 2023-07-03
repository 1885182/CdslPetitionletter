package com.sszt.cdslpetitionletter.bean

data class MsgBean(
    val creatTime: String,
    val id: Int,
    val message: String,
    val messageStatus: String,
    var messageRead: String,
    val uuid: String
)

