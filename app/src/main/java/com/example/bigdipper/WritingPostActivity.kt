package com.example.bigdipper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bigdipper.databinding.ActivityWritingPostBinding

class WritingPostActivity : AppCompatActivity() {
    lateinit var binding:ActivityWritingPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWritingPostBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        binding.backBtn.setOnClickListener { finish() }

        binding.submitBtn.setOnClickListener {
            //제출 버튼
        }

        setContentView(binding.root)
    }
}