package com.example.bigdipper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bigdipper.databinding.CommentRowBinding
import com.example.bigdipper.databinding.WriteRowBinding

class CommentAdapter(val comments:ArrayList<CommentData>) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    interface OnItemClickListener {
        fun OnItemClick(data: CommentData, position: Int)
    }

    var itemClickListener: OnItemClickListener?=null

    inner class CommentViewHolder(val binding: CommentRowBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.thumbBtn.setOnClickListener {
                itemClickListener?.OnItemClick(comments[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentAdapter.CommentViewHolder {
        val view = CommentRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentViewHolder, position: Int) {
        var data = comments[position]
        holder.binding.handle.text = data.author
        holder.binding.commentContent.text = data.content
        holder.binding.commentDate.text = data.date
        holder.binding.thumbCnt.text = data.likes.toString()
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}