package com.example.bigdipper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bigdipper.databinding.ActivityForumBinding

class ForumActivity : AppCompatActivity() {
    lateinit var binding:ActivityForumBinding
    var writeList = arrayListOf<Write>()
    lateinit var adapter:WriteListAdapter2
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityForumBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)

        initData()
        initRecyclerView()
        initLayout()

        setContentView(binding.root)
    }

    private fun initLayout() {
        binding.backBtn.setOnClickListener { finish() }
        binding.writingBtn.setOnClickListener {
            val intent = Intent(this, WritingPostActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initData() {
        writeList.add(Write("뻘글6", "안녕하세요", "06/20 21:30",5))
        writeList.add(Write("뻘글2", "안녕하세요", "06/20 21:30",4))
        writeList.add(Write("뻘글3", "안녕하세요", "06/20 21:30",3))
        writeList.add(Write("뻘글4", "안녕하세요", "06/20 21:30",2))
        writeList.add(Write("뻘글5", "안녕하세요", "06/20 21:30",1))
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        adapter = WriteListAdapter2(writeList)
        adapter.itemClickListener = object : WriteListAdapter2.OnItemClickListener {
            override fun OnItemClick(data: Write) {
                val intent = Intent(this@ForumActivity, PostActivity::class.java)
                intent.putExtra("post", data)
                startActivity(intent)
            }
        }
        binding.recyclerView.adapter = adapter
    }
}