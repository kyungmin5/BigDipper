package com.example.bigdipper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bigdipper.databinding.ActivityForumBinding

class Forum : AppCompatActivity() {
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
    }

    private fun initData() {
        writeList.add(Write("뻘글6", "안녕하세요", 5))
        writeList.add(Write("뻘글2", "안녕하세요", 4))
        writeList.add(Write("뻘글3", "안녕하세요", 3))
        writeList.add(Write("뻘글4", "안녕하세요", 2))
        writeList.add(Write("뻘글5", "안녕하세요", 1))
    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        adapter = WriteListAdapter2(writeList)
        adapter.itemClickListener = null
        binding.recyclerView.adapter = adapter
    }
}