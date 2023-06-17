package com.example.bigdipper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bigdipper.databinding.ActivityMakingClubBinding

class MakingClub : AppCompatActivity() {
    lateinit var binding:ActivityMakingClubBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMakingClubBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_making_club)
    }
}