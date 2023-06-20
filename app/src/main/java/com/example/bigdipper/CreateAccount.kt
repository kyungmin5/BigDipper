package com.example.bigdipper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.bigdipper.databinding.ActivityCreateAccountBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CreateAccount : AppCompatActivity() {
    lateinit var binding: ActivityCreateAccountBinding
    lateinit var databaseReference:DatabaseReference
    private var btnCreateAccount: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        databaseReference = FirebaseDatabase.getInstance().reference.child("users")
        initLayout()
    }
    private fun initLayout(){

        val intent = Intent(this, MainActivity::class.java)

        btnCreateAccount = binding.btnSignUp
        btnCreateAccount!!.setOnClickListener(View.OnClickListener { view: View? ->
            var userId = binding.etUserId.text.toString()
            var userPassword = binding.etPassword.text.toString()
            var userNickname = binding.etNickname.text.toString()
            var userStatus = binding.etStatus.text.toString()
            Log.i("id",userId)
            Log.i("pw",userPassword)
            val userManager = UserDataManager.getInstance() // 싱글톤 객체 가져오기


            var readed = arrayListOf<String>()
            readed.add("dummy")
            var postarray= arrayListOf<PostData>()
            var postData=PostData("제목","작성자","내용",0, arrayListOf<CommentData>(), "06/21 05:01")
            postarray.add(postData)
            var bookarray= arrayListOf<BookClubData>()
            val bookclub = BookClubData(
                    clubImg = "imgstr",
                    currentBook = "마음의 치유",
                    clubName = "철학과 생각",
                    tags = arrayListOf("Self-Improvement/Hobby", "Culture/Art"),
                    ageGroup = "All ages",
                    clubDetails = "철학 관련 책을 읽으며 토론을 주로 하는 북클럽입니다. 관심 있는 분 많이 오세요.",
                    memberNum = "12",
                    createdAt = "2023-06-15T16:45:00Z",
                    totalMemberNum = "30",
                    clubRules = "북클럽 규칙",
                    booksHaveRead = arrayListOf("읽은 책1", "읽은 책2"),
                    creator = "북클럽 생성자",
                    userList = arrayListOf(""),
                    postarray
                )
            bookarray.add(bookclub)

            // 유저 데이터 설정
            val userData = UserData("uid123",userId,userPassword, userNickname, userStatus, 1, bookarray, readed, postarray)
            val bookClubList: ArrayList<BookClubData>? = null // 빈 배열 대신 null 값으로 초기화

// 데이터베이스에 bookClubList 값을 할당
            databaseReference.child("users").child("bookClubList").setValue(bookClubList)
                .addOnSuccessListener {
                    // 값 저장 성공
                }
                .addOnFailureListener { error ->
                    // 값 저장 실패
                }
            val databaseReference = FirebaseDatabase.getInstance().reference.child("users")
            val userQuery = databaseReference.orderByChild("NickName").equalTo(userNickname)
            userManager.setUserData(userData)
            Log.i("asdasdasd",userData.toString())
            userQuery.addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                    } else {
                        // 동일한 UID가 없는 경우 새로운 사용자를 생성하고 데이터를 추가합니다.
                        databaseReference.child(userNickname).setValue(userData)
                            .addOnSuccessListener {
                                Log.i("asd","asd")
                                // 데이터 추가 성공
                            }
                            .addOnFailureListener { error ->
                                // 데이터 추가 실패
                            }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    // 쿼리 취소 또는 오류 처리
                }
            })
            startActivity(intent)
        })
    }
}