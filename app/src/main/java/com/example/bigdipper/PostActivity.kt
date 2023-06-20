package com.example.bigdipper

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bigdipper.databinding.ActivityPostBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostActivity : AppCompatActivity() {
    lateinit var binding:ActivityPostBinding
    lateinit var adapter:CommentAdapter
    val userManager = UserDataManager.getInstance()
    val CurUserData = userManager.getUserData()
    var post = intent.getSerializableExtra("post") as? PostData
    var club = intent.getSerializableExtra("club") as? BookClubData
    var commentList = post?.comments
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPostBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        initLayout()
        initRecyclerView()

        setContentView(binding.root)

        binding.sendBtn.setOnClickListener {
            val content = binding.editText.text.toString()
            val newComment = CommentData(CurUserData!!.NickName, content, 0, "06/21 04:47")
            commentList?.add(newComment)
            adapter.notifyItemInserted(commentList!!.size)

            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("clubs") // 북클럽 데이터의 경로를 지정합니다

            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val clubData = snapshot.getValue(BookClubData::class.java)
                        val clubName = clubData?.clubName

                        if (clubName == club?.clubName) {
                            // 해당 북클럽의 clubName 값과 내 문자열이 일치하는 경우에 대한 작업 수행
                            val clubRef = snapshot.ref
                            clubRef.child("postList/comments").push().setValue(newComment)
                            break
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@PostActivity, "DB 오류", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initLayout() {
        binding.title.text = post?.title
        binding.handle.text = post?.author
        binding.content.text = post?.content
        binding.date.text = post?.date
        binding.thumbCnt.text = post?.likes.toString() + "개"
    }

    private fun initRecyclerView() {
        binding.commentsView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        adapter = CommentAdapter(commentList!!)
        adapter.itemClickListener = object : CommentAdapter.OnItemClickListener {
            override fun OnItemClick(data: CommentData, position:Int) {
                data.likes += 1
                adapter.notifyItemChanged(position)
            }
        }
        binding.commentsView.adapter = adapter
    }
}