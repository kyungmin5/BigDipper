package com.example.bigdipper

import java.io.Serializable

data class BookClubData(val clubImg: String, var currentBook: String,
                        val clubName:String, val tags: ArrayList<String>,
                        val ageGroup: String, val clubDetails: String,
                        val memberNum: String, val createdAt: String,
                        val totalMemberNum: String, val clubRules:String,
                        val booksHaveRead: ArrayList<String>, val creator: String,
                        val userList: ArrayList<String>,
                        val postList: ArrayList<PostData>,): Serializable {
    constructor() : this("", "", "", ArrayList(), "", "", "", "", "", "", ArrayList(), "",ArrayList(),
        ArrayList()
    )
}

data class PostData(
    val author: String,
    val content: String,
    val title: String,
    val likes: Int,
    val comments: ArrayList<CommentData>

): Serializable{
    constructor() : this("", "", "", 0, arrayListOf())
}

data class CommentData(
    val author: String,
    val content: String,
    val likes: Int
): Serializable{
    constructor() : this("", "", 0)
}