package com.example.bigdipper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.bigdipper.databinding.ActivityCommonLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.log

class CommonLogin : AppCompatActivity() {
    lateinit var binding: ActivityCommonLoginBinding

    private var btnLogin: Button? = null
    private var btnCreateAccount: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommonLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLayout()
    }
    private fun initLayout() {
        val intent = Intent(this, MainActivity::class.java)
        val intent2 = Intent(this, CreateAccount::class.java)
        btnLogin = binding.btnSignUp

        btnLogin?.setOnClickListener { view: View? ->
            val userId = binding.etUserId.text.toString()
            val userPassword = binding.etPassword.text.toString()

            if (userId.isBlank() || userPassword.isBlank()) {
                Toast.makeText(this@CommonLogin, "아이디와 패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                val userManager = UserDataManager.getInstance() // 싱글톤 객체 가져오기
                val databaseReference = FirebaseDatabase.getInstance().reference.child("users")

                val userQuery = databaseReference.orderByChild("userId").equalTo(userId)
                userQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (userSnapshot in dataSnapshot.children) {
                                val user = userSnapshot.getValue(UserData::class.java)
                                if (user != null && user.userPW == userPassword) {
                                    // 로그인 성공
                                    userManager.setUserData(user)
                                    Toast.makeText(this@CommonLogin, "로그인 성공", Toast.LENGTH_SHORT).show()
                                    startActivity(intent)
                                    return
                                }
                            }
                            Toast.makeText(this@CommonLogin, "사용자 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@CommonLogin, "사용자 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // 쿼리 취소 또는 오류 처리
                        Toast.makeText(this@CommonLogin, "사용자 정보가 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
        btnCreateAccount = binding.createAccount
        btnCreateAccount!!.setOnClickListener(View.OnClickListener { view: View? ->
            startActivity(intent2)
        })
    }

}