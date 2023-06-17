package com.example.bigdipper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bigdipper.databinding.WriteRowBinding
import java.lang.Integer.min

//게시판 미리보기를 위한 어댑터
class WriteListAdapter(val writeList: ArrayList<Write>): RecyclerView.Adapter<WriteListAdapter.WriteViewHolder>(){
    interface OnItemClickListener {
        fun OnItemClick(data: Write)
    }

    var itemClickListener: OnItemClickListener?=null

    inner class WriteViewHolder(val binding: WriteRowBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.title.setOnClickListener {
                itemClickListener?.OnItemClick(writeList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WriteViewHolder {
        val view = WriteRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return min(writeList.size, 3)
    }

    override fun onBindViewHolder(holder: WriteViewHolder, position: Int) {
        var data = writeList[position]
        holder.binding.title.text = data.title
        holder.binding.content.text = data.content
        holder.binding.boomUp.text = "추천 ${data.boomUp}개"
    }
}