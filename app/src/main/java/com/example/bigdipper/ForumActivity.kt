package com.example.bigdipper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bigdipper.databinding.ActivityForumBinding

@Suppress("DEPRECATION")
class ForumActivity : AppCompatActivity() {
    lateinit var binding:ActivityForumBinding

    lateinit var adapter:WriteListAdapter2
    var bookData:BookClubData?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityForumBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        bookData=intent.getSerializableExtra("bookData") as? BookClubData
        initRecyclerView()
        initLayout()

        setContentView(binding.root)
    }

    private fun initLayout() {
        binding.backBtn.setOnClickListener { finish() }
        binding.writingBtn.setOnClickListener {
            val intent = Intent(this, WritingPostActivity::class.java)
            intent.putExtra("club", bookData)
            startActivity(intent)
        }
    }


    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        adapter = WriteListAdapter2(bookData?.postList)
        adapter.itemClickListener = object : WriteListAdapter2.OnItemClickListener {
            override fun OnItemClick(data: PostData) {
                val intent = Intent(this@ForumActivity, PostActivity::class.java)
                intent.putExtra("post", data)
                intent.putExtra("club", bookData)
                startActivity(intent)
            }
        }
        binding.recyclerView.adapter = adapter
    }
}