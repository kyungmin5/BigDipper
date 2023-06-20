package com.example.bigdipper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bigdipper.databinding.ActivityPostBinding

class PostActivity : AppCompatActivity() {
    lateinit var binding:ActivityPostBinding
    lateinit var adapter:CommentAdapter
    var commentList = arrayListOf<Comment>()
    var post = intent.getSerializableExtra("post") as? PostData
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPostBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        initData()
        initRecyclerView()

        setContentView(binding.root)
    }

    private fun initRecyclerView() {
        binding.commentsView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        adapter = CommentAdapter(commentList)
        adapter.itemClickListener = object : CommentAdapter.OnItemClickListener {
            override fun OnItemClick(data: Comment, position:Int) {
                data.thumbCnt += 1
                adapter.notifyItemChanged(position)
            }
        }
        binding.commentsView.adapter = adapter
    }

    private fun initData() {
        commentList.add(Comment("eogns47", "안녕하세요.", "06/17 11:21", 2))
        commentList.add(Comment("eogns48", "안녕하세요.", "06/17 11:21", 1))
        commentList.add(Comment("eogns49", "안녕하세요.", "06/17 11:21", 3))
        commentList.add(Comment("eogns50", "안녕하세요.", "06/17 11:21", 4))
    }
}