package com.example.bigdipper

import java.io.Serializable

data class UserData(
    val Uid: String = "",
    val userId: String = "",
    val userPW: String = "",
    val NickName: String = "",
    val explain: String = "",
    var lv: Int = 1,
    val bookClubList: ArrayList<BookClubData> = ArrayList(),
    val readedList: ArrayList<String> = ArrayList(),
    val PostList: ArrayList<PostData> = ArrayList()
) : Serializable {
    constructor() : this("", "", "", "", "", 1, ArrayList(), ArrayList(), ArrayList())
}
