package com.example.bigdipper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bigdipper.databinding.WriteRowBinding
import java.lang.Integer.min

//게시판 전체보기 액티비티를 위한 어댑터
class WriteListAdapter2(val writeList: ArrayList<PostData>?): RecyclerView.Adapter<WriteListAdapter2.WriteViewHolder>(){
    interface OnItemClickListener {
        fun OnItemClick(data: PostData){

        }
    }

    var itemClickListener: OnItemClickListener?=null

    inner class WriteViewHolder(val binding: WriteRowBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.title.setOnClickListener {
                itemClickListener?.OnItemClick(writeList!![adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WriteListAdapter2.WriteViewHolder {
        val view = WriteRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: WriteViewHolder, position: Int) {
        var data = writeList!![position]
        holder.binding.title.text = data.title
        holder.binding.content.text = data.content
        holder.binding.boomUp.text = "추천 ${data.likes}개"
    }

    override fun getItemCount(): Int {
        if(writeList==null){
            return 0
        }
        return writeList!!.size
    }
}