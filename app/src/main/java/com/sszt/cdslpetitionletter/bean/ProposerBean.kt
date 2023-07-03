package com.sszt.cdslpetitionletter.bean

import android.os.Parcel
import java.io.Serializable

data class ProposerBean(
    val applyType: String,//申请人类型:1.自然人 2.法人 3.非法人组织
    val applyName: String,//申请人姓名
    val applyUsci: String,//社会信用代码(申请人)
    val applyLegalRepresentative: String, //法定代表人(申请人)
    val applyPhone: String,//联系电话
    val applyOtherType: String,//其他联系方式:1.邮箱 2.QQ 3.微信
    val applyOtherNum: String,//其他联系方式账号
    val applyIdentity: String, //身份证
    val applyAddress: String,//常住地址
    /*val applyStreet: String,//详细地址
    val applyOrganRepresentative: String//机构代表人(申请人)*/
): Serializable