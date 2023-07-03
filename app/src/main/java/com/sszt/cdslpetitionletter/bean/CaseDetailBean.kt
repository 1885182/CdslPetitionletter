package com.sszt.cdslpetitionletter.bean

data class CaseDetailBean(
    val applyAddress: String,
    val applyIdentity: String,
    val applyLegalRepresentative: String,
    val applyName: String,
    val applyOrganRepresentative: Any,
    val applyOtherNum: String,
    val applyOtherType: Int,
    val applyPhone: String,
    val applyStreet: Any,
    val applyType: Int,
    val applyUsci: String,
    val lastProcessMode: Int,
    val caseAddress: String,
    val caseAgentList: List<CaseAgentBeans>,
    val caseDate: String,
    val caseDesc: String,
    val caseOrganization: String,
    val caseProcessList: List<CaseProcessBean>,
    val caseRecordList: List<CaseRecordBean>,
    val caseStatus: Int,
    val caseStreet: String,
    val caseType: String,
    val createDate: String,
    val createUserName: String,
    val createUserUuid: String,
    val id: Int,
    val institutionName: String,
    val respondentAddress: String,
    val respondentIdentity: String,
    val respondentLegalRepresentative: String,
    val respondentName: String,
    val respondentOrganRepresentative: Any,
    val respondentOtherNum: String,
    val respondentOtherType: Int,
    val respondentPhone: String,
    val respondentStreet: Any,
    val respondentType: Int,
    val respondentUsci: String,
    val uuid: String
)

data class CaseRecordBean(
    val id: Int,
    val uuid: String,
    val caseUuid: String,
    val researchDate: String,
    val researchDateStart: String,
    val researchDateEnd: String,
    val researchAddress: String,
    val researchPerson: String,
    val researchRespondent: String,
    val researchRecorder: String,
    val researchRecord: String,
    val researchRespondentSign: String,
    val researchPersonSign: String,
    val researchRecorderSign: String,
    val researchUrl: String,
    val caseRecordSignEntities: String,
    val signList: List<CaseRecordNameBean>,
)

data class CaseAgentBeans(
    val agentAuthorization: String,
    val agentEntrust: Int,
    val agentIdentity: String,
    val agentName: String,
    val agentPhone: String,
    val agentRelation: String,
    val agentSort: Int,
    val agentType: Int,
    val caseUuid: String,
    val commAttachment: AssistRecordFileBean,
    val id: Int
)

data class CaseProcessBean(
    val caseStatus: Int,
    val caseUuid: String,
    val id: Int,
    val processDate: String,
    val processMode: Int,
    val processType: Int,
    val processUserName: String,
    val processUserUuid: String,
    val uuid: String
)

data class CaseRecordNameBean(
    var researchPerson: String,
    var researchRespondentSign: String
)

data class CaseWritBean(
    var writCode: String,
    var writItems: String,
    var writText: String,
    var writType: Int,
    var applySign: String,
    var respondentSign: String,
)
data class CaseWritDetailBean(
    var writCode: String,
    var writItems: String,
    var writText: String,
    var applySign: String,
    var respondentSign: String,
    var writType: Int,
)