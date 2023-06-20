package com.example.bigdipper

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bigdipper.databinding.ActivityBookClubPageBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

@Suppress("DEPRECATION")
class BookClubPage : AppCompatActivity() {
    lateinit var binding: ActivityBookClubPageBinding
    lateinit var adapter:WriteListAdapter
    var bookData:BookClubData?=null
    val userManager = UserDataManager.getInstance()
    val CurUserData = userManager.getUserData()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBookClubPageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        initLayout()
    }

    private fun initRecyclerView() {
        binding.writeView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        val iterator = bookData?.postList?.iterator()
        while (iterator?.hasNext() == true) {
            val post = iterator.next()
            if (post.title == "") {
                iterator.remove()
            }
        }
        adapter = WriteListAdapter(bookData?.postList)
        adapter.itemClickListener = null
        binding.writeView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initLayout(){
        binding.apply {
            backBtn.setOnClickListener {
                finish()
            }
            bookData = intent.getSerializableExtra("data") as? BookClubData
            initRecyclerView()
            if (bookData != null) {
                // 북클럽 이름과 소개
                bookClubName.text = bookData?.clubName
                bookClubDetails.text = bookData?.clubDetails

                // 북클럽 정보 (개설일, 멤버 명수와 정원, 선호 연령대)
                createdAt.text = ISOStringToDateInKorean(bookData!!.createdAt)
                members.text = bookData?.memberNum + " / " + bookData?.totalMemberNum + "명"
                preferedAge.text = ageFilterInKorean(bookData!!.ageGroup)

                // 클럽 규칙
                bookClubRules.text = bookData?.clubRules

                // 리더 소개
                bookClubLeader.text = bookData?.creator

                // 함께 읽은 책
                currentBookTitle.text = "📕 ${bookData?.currentBook}"
                val booksStringArr = bookData?.booksHaveRead?.map{
                    "📘 $it"
                }
                booksReadTogether.text = booksStringArr?.joinToString("\n")
            }
            adapter.notifyDataSetChanged()

            SeeAllWrite.setOnClickListener {
                val intent = Intent(this@BookClubPage, ForumActivity::class.java)
                intent.putExtra("bookData", bookData)
                startActivity(intent)

            }


            setting.setOnClickListener {
                if (CurUserData?.nickName == bookData?.creator) {
                    setBook()
                }
                else {
                    Toast.makeText(this@BookClubPage, "관리자만 접근 가능합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setBook() {
        if (bookData?.currentBook == "없음") { // 읽고 있는 책이 없는 경우 => 읽을 책 세팅
            val builder = AlertDialog.Builder(this)
            builder.setTitle("책을 읽읍시다 📚")
            val inputEditText = EditText(this)
            builder.setView(inputEditText)
            builder.setPositiveButton("확인") { dialog, which ->
                val userInputText = inputEditText.text.toString()
                val database = FirebaseDatabase.getInstance()
                val reference = database.getReference("clubs")

                val clubRef = reference.child(bookData!!.clubName)

                clubRef.child("currentBook").setValue(userInputText)
            }
            builder.setNegativeButton("취소") { dialog, which ->
                dialog.dismiss()
            }
            builder.show()
        }
        else { // 읽고 있는 책이 있는 경우 => 끝내기?
            val builder = AlertDialog.Builder(this)
            builder.setTitle("완독")
                .setMessage("${bookData?.currentBook}을 다 읽으셨나요?")
                .setPositiveButton("확인") { dialog: DialogInterface, which: Int ->
                    val database = FirebaseDatabase.getInstance()
                    val reference = database.getReference("clubs")

                    val clubRef = reference.child(bookData!!.clubName)
                    val databaseReference = FirebaseDatabase.getInstance().reference.child("clubs")
                    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (data in dataSnapshot.children) {
                                var book = data.getValue(BookClubData::class.java)
                                if (book!!.clubName == bookData!!.clubName) {
                                    book.currentBook = "없음"

                                    for (user in book.userList) {
                                        val userReference = database.getReference("users")
                                        var oldValue: Int = 0
                                        userReference.addListenerForSingleValueEvent(object :
                                            ValueEventListener {
                                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                for (data in dataSnapshot.children) {
                                                    var users = data.getValue(UserData::class.java)
                                                    if (users!!.nickName == user) {
                                                        users.lv += 150
                                                        CurUserData!!.lv+=150
                                                    }
                                                    data.ref.setValue(users)
                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                TODO("Not yet implemented")
                                            }
                                        })
                                    }
                                    data.ref.setValue(book)
                                    break
                                }

                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                    val intent = Intent(this@BookClubPage, MainActivity::class.java)

                    startActivity(intent)

                }
                .setNegativeButton("취소") { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    fun ISOStringToDateInKorean(isoString: String): String{
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일", Locale.getDefault())
        val dateTime = ZonedDateTime.parse(isoString)
        val formattedString = dateTime.format(formatter)
        return formattedString
    }
    private fun ageFilterInKorean(value: String): String {
        return FilterOptionSingleton.getInstance().ageGroupFilterNames.find { it.filterValue == value }?.filterName!!
    }
}