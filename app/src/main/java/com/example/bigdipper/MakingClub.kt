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

            submitBtn.setOnClickListener {
                var clubImg="imgstr"
                var currentBook = bookInput.text.toString()
                var clubName = clubNameInput.text.toString()
                var tags = arrayListOf("Self-Improvement/Hobby", "Culture/Art")
                var clubDetails =introInput.text.toString()
                var memberNum = "1"
                var totalMemberNum = personnel.text.toString()
                var clubRules = rule.text.toString()
                var theme=binding.spinner1.selectedItem as String
                var theme2:String= binding.spinner2.selectedItem as String
                var themeArray: ArrayList<String> = ArrayList()
                themeArray.add(theme)
                themeArray.add(theme2)
                var creator = "북클럽 생성자"
                Log.i("ddd",currentBook)
                if(currentBook==""){
                    Toast.makeText(this@MakingClub,"읽을책 작성해주세요",Toast.LENGTH_SHORT).show()
                }
                else if(clubName.isEmpty()){
                    Toast.makeText(this@MakingClub,"북클럽 이름좀",Toast.LENGTH_SHORT).show()

                }
                else if(clubDetails.isEmpty()){
                    Toast.makeText(this@MakingClub,"북클럽 설명좀",Toast.LENGTH_SHORT).show()
                }
                else if(totalMemberNum.isEmpty()){
                    Toast.makeText(this@MakingClub,"북클럽 총 인원좀",Toast.LENGTH_SHORT).show()

                }
                else if(clubRules.isEmpty()){
                    Toast.makeText(this@MakingClub,"북클럽규칙좀",Toast.LENGTH_SHORT).show()

                }
                else {

                    val selectedRadioButtonId = binding.ageGroup.checkedRadioButtonId
                    if (selectedRadioButtonId == -1) {
                        Toast.makeText(this@MakingClub, "선호 연령대를 선택해주세요", Toast.LENGTH_SHORT).show()
                    } else{
                    val selectedRadioButton: RadioButton =
                        binding.ageGroup.findViewById(selectedRadioButtonId)
                    var targetAge: String? = null
                    if (selectedRadioButton == adult) {
                        targetAge = "Adult"
                    } else if (selectedRadioButton == teen) {
                        targetAge = "Adolescent"
                    } else if (selectedRadioButton == dontCare) {
                        targetAge = "All ages"
                    }
                        setTime()
                        val club = BookClubData(
                            clubImg = clubImg,
                            currentBook = currentBook,
                            clubName = clubName,
                            tags = tags,
                            ageGroup = targetAge!!,
                            clubDetails = clubDetails,
                            memberNum = "1",
                            createdAt = time!!,
                            totalMemberNum = personnel.text.toString(),
                            clubRules = rule.text.toString(),
                            booksHaveRead = arrayListOf(),
                            creator = "만쥬",
                            userList = arrayListOf("만쥬"),
                            postList = arrayListOf(PostData("", "", "", 0,
                                arrayListOf( CommentData("","",0,"")),""))
                        )
                        val database = FirebaseDatabase.getInstance()
                        val reference = database.reference.child("clubs")
                        reference.push().setValue(club)
                        finish()
                    }
                }


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