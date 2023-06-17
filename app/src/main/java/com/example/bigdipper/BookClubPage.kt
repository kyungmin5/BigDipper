package com.example.bigdipper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.bigdipper.databinding.ActivityBookClubPageBinding
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class BookClubPage : AppCompatActivity() {
    lateinit var binding: ActivityBookClubPageBinding
    var writeList = arrayListOf<Write>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBookClubPageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initData()
        val writeAdapter = WriteListAdapter(this, writeList)
        binding.writeView.adapter = writeAdapter
        initLayout()
    }

    private fun initData() {
        writeList.add(Write("뻘글", "안녕하세요", 5))
    }

    private fun initLayout(){
        val bookData = intent.getSerializableExtra("clickedData") as? BookClubData
        binding.apply {
            backBtn.setOnClickListener {
                finish()
            }
            if (bookData != null) {
                // 북클럽 이름과 소개
                bookClubName.text = bookData.clubName
                bookClubDetails.text = bookData.clubDetails

                // 북클럽 정보 (개설일, 멤버 명수와 정원, 선호 연령대)
                createdAt.text = ISOStringToDateInKorean(bookData.createdAt)
                members.text = bookData.memberNum + " / " + bookData.totalMemberNum
                preferedAge.text = ageFilterInKorean(bookData.ageGroup)

                // 클럽 규칙
                bookClubRules.text = bookData.clubRules

                // 리더 소개
                bookClubLeader.text = bookData.creator

                // 함께 읽은 책
                currentBookTitle.text = "📕 ${bookData.currentBook}"
                val booksStringArr = bookData.booksHaveRead.map{
                    "📘 $it"
                }
                booksReadTogether.text = booksStringArr.joinToString("\n")
            }

        }
    }

    fun ISOStringToDateInKorean(isoString: String): String{
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일", Locale.getDefault())
        val dateTime = ZonedDateTime.parse(isoString)
        val formattedString = dateTime.format(formatter)
        return formattedString
    }
    private fun ageFilterInKorean(value: String): String {
        return FilterOptionSingleton.getInstance().ageGroupFilterNames.find { it.filterValue == value }?.filterName!!
    }
}