package com.example.bigdipper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bigdipper.databinding.ActivityEmailLoginBinding
import com.google.firebase.auth.FirebaseAuth

class EmailLogin : AppCompatActivity() {

    lateinit var binding: ActivityEmailLoginBinding
    //nullable한 FirebaseAuth 객체 선언
    var auth: FirebaseAuth? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = Intent(this, MainActivity::class.java)

        auth = FirebaseAuth.getInstance()


    }
}