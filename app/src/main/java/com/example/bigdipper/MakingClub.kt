package com.example.bigdipper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import com.example.bigdipper.databinding.ActivityMakingClubBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MakingClub : AppCompatActivity() {
    lateinit var binding:ActivityMakingClubBinding
    var time:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMakingClubBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        Log.i("ddddddd","asd")
        init()
    }

    fun setTime(){
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        time = currentDateTime.format(formatter)
//        val currentDateTime = LocalDateTime.now()
//        val formatter = DateTimeFormatter.ISO_DATE_TIME
//        time = currentDateTime.format(formatter)

    }

    private fun init(){
        binding.apply {
            Log.i("ddddddd","qqqqqqqqqqqqq")
            submitBtn.setOnClickListener {
                val selectedRadioButtonId = binding.ageGroup.checkedRadioButtonId
                val selectedRadioButton: RadioButton = binding.ageGroup.findViewById(selectedRadioButtonId)
                var age=ageGroup.checkedRadioButtonId
                Log.i("ddddddd",age.toString())

                var targetAge:String?=null
                if(selectedRadioButton==adult){
                    targetAge="Adult"
                }
                else if(selectedRadioButton==teen){
                    targetAge="Adolescent"
                }
                else if(selectedRadioButton==dontCare){
                    targetAge="All ages"
                }
                setTime()
                val club = BookClubData(
                    clubImg = "imgstr",
                    currentBook = bookInput.text.toString(),
                    clubName = clubNameInput.text.toString(),
                    tags = arrayListOf("Self-Improvement/Hobby", "Culture/Art"),
                    ageGroup = targetAge!!,
                    clubDetails =introInput.text.toString(),
                    memberNum = "1",
                    createdAt = time!!,
                    totalMemberNum = personnel.text.toString(),
                    clubRules = rule.text.toString(),
                    booksHaveRead = arrayListOf(),
                    creator = "북클럽 생성자"
                )
                val database = FirebaseDatabase.getInstance()
                val reference = database.reference.child("clubs")
                reference.push().setValue(club)
                Log.i("ddddddd",time.toString())
                Toast.makeText(this@MakingClub,"asd",Toast.LENGTH_SHORT).show()
            }
        }
    }



//        createBookClubFab.setOnClickListener {
//
//        // 북클럽 정보 생성
//        val club = BookClubData(
//            clubImg = "imgstr",
//            currentBook = "마음의 치유",
//            clubName = "철학과 생각",
//            tags = arrayListOf("Self-Improvement/Hobby", "Culture/Art"),
//            ageGroup = "All ages",
//            clubDetails = "철학 관련 책을 읽으며 토론을 주로 하는 북클럽입니다. 관심 있는 분 많이 오세요.",
//            memberNum = "12",
//            createdAt = "2023-06-15T16:45:00Z",
//            totalMemberNum = "30",
//            clubRules = "북클럽 규칙",
//            booksHaveRead = arrayListOf("읽은 책1", "읽은 책2"),
//            creator = "북클럽 생성자"
//        )
//
//        val database = FirebaseDatabase.getInstance()
//        val reference = database.reference.child("clubs")
//        reference.push().setValue(club)
//    }
}