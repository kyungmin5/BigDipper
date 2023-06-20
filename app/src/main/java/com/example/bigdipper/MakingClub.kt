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
    val userManager = UserDataManager.getInstance()
    val CurUserData = userManager.getUserData()

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
                if(theme!="테마 선택") {
                    themeArray.add(theme)
                }
                if(theme2!="테마 선택") {
                    themeArray.add(theme2)
                }

                var creator = "북클럽 생성자"
                Log.i("ddd",currentBook)
                if(currentBook==""){
                    Toast.makeText(this@MakingClub,"읽을책 작성해주세요",Toast.LENGTH_SHORT).show()
                }
                else if(theme=="테마 선택" && theme2=="테마 선택"){
                    Toast.makeText(this@MakingClub,"테마를 선택해주세요",Toast.LENGTH_SHORT).show()

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
                            tags = themeArray,
                            ageGroup = targetAge!!,
                            clubDetails = clubDetails,
                            memberNum = "1",
                            createdAt = time!!,
                            totalMemberNum = personnelInput.text.toString(),
                            clubRules = rule.text.toString(),
                            booksHaveRead = arrayListOf(),
                            creator = CurUserData!!.NickName,
                            userList = arrayListOf(creator),
                            postList = arrayListOf(PostData("", "", "", 0,

                                arrayListOf( CommentData("","",0, "")), ""))
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




}