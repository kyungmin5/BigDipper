package com.example.bigdipper

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
    val CurUserData = userManager.getUserData()
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
                val databaseReference = FirebaseDatabase.getInstance().reference
                val usersRef = databaseReference.child("users")
                Log.i("asdaasdsd",usersRef.toString())
                usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (userDataSnapshot in dataSnapshot.children) {
                            val user = userDataSnapshot.getValue(UserData::class.java)
                            Log.i("asdaasdsd",user.toString())
                            Log.i("asdaasdsd",CurUserData.toString())
                            if (user?.NickName == CurUserData.NickName) {
                                // 닉네임이 일치하는 사용자를 찾았을 경우 북클럽리스트 정보를 수정
                                val bookClubListRef = userDataSnapshot.child("bookClubList")
                                val bookUpdate = HashMap<String,Any>()
                                bookUpdate["/bookClublist/${bookData!!.clubName}"] = bookData
                                usersRef.updateChildren(bookUpdate)
                                Log.i("asdasd",bookUpdate.toString())
                                Log.i("asdaasdsd",user.toString())

                                // 파이어베이스에 사용자 정보 업데이트
                                userDataSnapshot.ref.setValue(user)

                                break
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })

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