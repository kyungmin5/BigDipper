package com.example.bigdipper

import java.io.Serializable

data class UserData(val Uid:String, val NickName:String, val explain:String,val lv:Int,
                    val bookClubList:ArrayList<BookClubData>, val readedList:ArrayList<String>,
                    val PostList:ArrayList<PostData>
                    ): Serializable {
    constructor() : this("","","",1, arrayListOf(), arrayListOf(), arrayListOf())

}