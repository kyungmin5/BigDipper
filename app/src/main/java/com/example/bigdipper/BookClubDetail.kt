package com.example.bigdipper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.bigdipper.databinding.ActivityBookClubDetailBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Suppress("DEPRECATION")
class BookClubDetail : AppCompatActivity() {
    lateinit var binding: ActivityBookClubDetailBinding
    val userManager = UserDataManager.getInstance()
    private val CurUserData = userManager.getUserData()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookClubDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout(){
        val bookData = intent.getSerializableExtra("clickedData") as? BookClubData
        binding.apply {
            backBtn.setOnClickListener {
                finish()
            }
            joinBookClubBtn.setOnClickListener {
                // 북클럽 가입하기 버튼 이벤트 리스너
                CurUserData?.bookClubList?.add(bookData!!)
                userManager.setUserData(CurUserData!!)
                val databaseReference = FirebaseDatabase.getInstance().reference.child("users")
                val userNickname = CurUserData.nickName // 대조할 사용자의 닉네임

                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        for (data in dataSnapshot.children) {
                            var user = data.getValue(UserData::class.java)
                            if (user!!.nickName == userNickname) {
                                if (user?.bookClubList == null) {
                                    user?.bookClubList = ArrayList() // 새로운 ArrayList로 초기화
                                }
                                user?.bookClubList?.add(bookData!!)
                                data.ref.setValue(user)
                            }
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        // 쿼리 취소 또는 오류 처리
                    }
                })
                val databaseReference1 = FirebaseDatabase.getInstance().reference.child("clubs")
                    databaseReference1.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (data in dataSnapshot.children) {
                                var bookClub = data.getValue(BookClubData::class.java)
                                if(bookClub?.clubName==bookData?.clubName) {
                                    var intNum = bookClub!!.memberNum!!.toInt()
                                    intNum=intNum+1
                                    bookClub.memberNum=intNum.toString()
                                    bookClub?.userList?.add(userNickname)
                                    data.ref.setValue(bookClub)
                                }
                            }
                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            // 쿼리 취소 또는 오류 처리
                        }
                    })
                val intent = Intent(this@BookClubDetail, MainActivity::class.java)

                startActivity(intent)



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