package com.sszt.cdslpetitionletter.bean

import android.os.Parcel
import java.io.Serializable

data class ByProposerBean(
    val respondentType: String,//申请人类型:1.自然人 2.法人 3.非法人组织
    val respondentName: String,//申请人姓名
    val respondentUsci: String,//社会信用代码(申请人)
    val respondentLegalRepresentative: String, //法定代表人(申请人)
    val respondentPhone: String,//联系电话
    val respondentOtherType: String,//其他联系方式:1.邮箱 2.QQ 3.微信
    val respondentOtherNum: String,//其他联系方式账号
    val respondentIdentity: String, //身份证
    val respondentAddress: String,//常住地址
    /*val applyStreet: String,//详细地址
    val applyOrganRepresentative: String//机构代表人(申请人)*/
): Serializable