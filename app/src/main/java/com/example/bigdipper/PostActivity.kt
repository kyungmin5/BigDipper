package com.example.bigdipper

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bigdipper.databinding.ActivityPostBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class PostActivity : AppCompatActivity() {
    lateinit var binding:ActivityPostBinding
    lateinit var adapter:CommentAdapter
    val userManager = UserDataManager.getInstance()
    val CurUserData = userManager.getUserData()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPostBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        var post = intent?.getSerializableExtra("post") as? PostData
        var club = intent?.getSerializableExtra("club") as? BookClubData
        var commentList = post?.comments

        initLayout(post)
        initRecyclerView(commentList)

        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }

        binding.sendBtn.setOnClickListener {
            val content = binding.editText.text.toString()
            val newComment = CommentData(CurUserData!!.nickName, content, 0, "06/21 04:47")
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
                            val innerRef = clubRef.child("postList")
                            innerRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot2: DataSnapshot) {
                                    for(snapshot3 in snapshot2.children) {
                                        val postListData = snapshot3.getValue(PostData::class.java)
                                        val postTitleData = postListData?.title

                                        if (postTitleData == post?.title) {
                                            innerRef.child("comments").setValue(commentList)
                                        }
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(this@PostActivity, "DB 오류", Toast.LENGTH_SHORT).show()
                                }
                            })
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
    private fun initLayout(post:PostData?) {
        binding.title.text = post?.title
        binding.handle.text = post?.author
        binding.content.text = post?.content
        binding.date.text = post?.date
        binding.thumbCnt.text = post?.likes.toString() + "개"
    }

    private fun initRecyclerView(commentList: ArrayList<CommentData>?) {
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