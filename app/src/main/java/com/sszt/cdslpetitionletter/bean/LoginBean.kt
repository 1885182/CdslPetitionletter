package com.sszt.cdslpetitionletter.bean

data class LoginBean(
    val menus: List<Menu>,
    val token: String,
    val user: User
)


data class Menu(
    val children: List<Children>,
    val icon: String,
    val iconColor: String,
    val id: String,
    val isHeader: Boolean,
    val isOpen: Boolean,
    val targetType: Any,
    val text: String,
    val url: Any
)

data class User(
    val address: String,
    val baseOrgName: String,
    val baseOrgUuid: String,
    val baseRoleNames: String,
    val baseRoleSyms: String,
    val baseRoleUuids: String,
    val descr: String,
    val email: String,
    val excludeColumns: Any,
    val fields: Any,
    val gender: Int,
    val gmtCreate: String,
    val gmtModified: String,
    val id: Int,
    val idCardNo: String,
    val imgUrl: String,
    val mobile: String,
    val name: String,
    val offsetNum: Int,
    val openUser: Int,
    val orderBy: Any,
    val orderByField: String,
    val orderDirection: String,
    val orgRootUuid: String,
    val pageIndex: Int,
    val pageSize: Int,
    val password: String,
    val phone: String,
    val purorgnames: Any,
    val purorgs: Any,
    val purpowers: Any,
    val purpowersyms: Any,
    val purpowertitle: Any,
    val responsibleOrgName: String,
    val responsibleOrgUuid: String,
    val schoolRootUuid: String,
    val selectColumns: Any,
    val sortNo: Int,
    val sym: String,
    val sysSkin: String,
    val tableName: Any,
    val title: String,
    val username: String,
    val uuid: String,
    val `where`: String
)

data class UserBean(

    val name: String,
    val imgUrl: String,
    val userName: String,
    val mobile: String,
    val accessToken: String
)

data class ForgetPas(val username:String,val password:String)
data class UpdatePhone(val username:String,val mobile:String)

data class Children(
    val children: List<ChildrenX>,
    val icon: String,
    val iconColor: String,
    val id: String,
    val isHeader: Boolean,
    val isOpen: Boolean,
    val targetType: String,
    val text: String,
    val url: String
)

data class ChildrenX(
    val children: Any,
    val icon: String,
    val iconColor: String,
    val id: String,
    val isHeader: Boolean,
    val isOpen: Boolean,
    val targetType: String,
    val text: String,
    val url: String
)