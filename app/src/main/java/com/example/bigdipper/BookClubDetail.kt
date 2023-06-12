package com.example.bigdipper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bigdipper.databinding.ActivityBookClubDetailBinding

class BookClubDetail : AppCompatActivity() {
    lateinit var binding: ActivityBookClubDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookClubDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLayout()
    }

    private fun initLayout(){

    }
}