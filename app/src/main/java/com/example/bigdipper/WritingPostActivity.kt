package com.example.bigdipper

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.bigdipper.databinding.ActivityWritingPostBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class WritingPostActivity : AppCompatActivity() {
    lateinit var binding:ActivityWritingPostBinding
    val userManager = UserDataManager.getInstance()
    val CurUserData = userManager.getUserData()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityWritingPostBinding.inflate(layoutInflater)
        val bookClubData = intent.getSerializableExtra("club") as BookClubData
        super.onCreate(savedInstanceState)

        binding.backBtn.setOnClickListener { finish() }

        binding.submitBtn.setOnClickListener {
            //제출 버튼
            val content = binding.postContent.text.toString()
            val title = binding.postTitle.text.toString()

            val database = FirebaseDatabase.getInstance()
            val reference = database.getReference("clubs") // 북클럽 데이터의 경로를 지정합니다

            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val clubData = snapshot.getValue(BookClubData::class.java)
                        val clubName = clubData?.clubName

                        if (clubName == bookClubData?.clubName) {
                            // 해당 북클럽의 clubName 값과 내 문자열이 일치하는 경우에 대한 작업 수행
                            val newPost = HashMap<String, Any>()
                            newPost["author"] = CurUserData?.nickName!!
                            newPost["content"] = content
                            newPost["likes"] = 0
                            newPost["title"] = title
                            val newComment = ArrayList<CommentData>()
                            newComment.add(CommentData("관리자", "올바른 댓글 문화를 지켜나가요!", 0, "06/21 04:33"))
                            newPost["comments"] = newComment
                            val clubRef = snapshot.ref
                            val newPostData = PostData(CurUserData?.nickName!!, content, title, 0, newComment, "06/21 08:02")
                            val pl = clubData?.postList
                            pl?.add(newPostData)
                            clubRef.child("postList").setValue(pl)

                            val intent = Intent(this@WritingPostActivity, ForumActivity::class.java)

                            intent.putExtra("update", newPostData)
                            Log.i("newPost", newPostData.title)
                            setResult(Activity.RESULT_OK, intent)
                            finish()
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@WritingPostActivity, "DB 오류", Toast.LENGTH_SHORT).show()
                }
            })

        }
        setContentView(binding.root)
    }
}