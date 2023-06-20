package com.example.bigdipper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.bigdipper.databinding.ActivityCreateAccountBinding

class CreateAccount : AppCompatActivity() {
    lateinit var binding: ActivityCreateAccountBinding
    val userManager = UserDataManager.getInstance()
    private var btnCreateAccount: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }
    private fun initLayout(){
        val userId = binding.etUserId.text.toString()
        val userPassword = binding.etPassword.text.toString()
        val userNickname = binding.etNickname.text.toString()
        val userStatus = binding.etStatus.text.toString()

        val intent = Intent(this, MainActivity::class.java)

        btnCreateAccount = binding.btnSignUp
        btnCreateAccount!!.setOnClickListener(View.OnClickListener { view: View? ->
            startActivity(intent)
        })
    }
}