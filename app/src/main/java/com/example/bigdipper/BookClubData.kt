package com.example.bigdipper

import java.io.Serializable

data class BookClubData(val clubImg: String, val currentBook: String,  val clubName:String,
                        val tags: ArrayList<String>, val ageGroup: String, val clubDetails: String,
                        val memberNum: String, val createdAt: String, val totalMemberNum: String, val clubRules:String,
                        val booksHaveRead: ArrayList<String>, val creator: String): Serializable {
}