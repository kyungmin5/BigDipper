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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CreateAccount : AppCompatActivity() {
    lateinit var binding: ActivityCreateAccountBinding

    private var btnCreateAccount: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
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

            // 유저 데이터 설정
            val userData = UserData("uid123",userId,userPassword, userNickname, userStatus, 1, arrayListOf(), arrayListOf(), arrayListOf())
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