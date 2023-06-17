package com.example.bigdipper

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class WriteListAdapter(val context: Context, val writeList : ArrayList<Write>): BaseAdapter() {
    override fun getCount(): Int {
        return writeList.size
    }

    override fun getItem(position: Int): Any {
        return writeList[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("MissingInflatedId")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.write_row, null)

        /* 위에서 생성된 view를 res-layout-main_lv_item.xml 파일의 각 View와 연결하는 과정이다. */
        val title = view.findViewById<TextView>(R.id.title)
        val content = view.findViewById<TextView>(R.id.content)
        val boomUp = view.findViewById<TextView>(R.id.boomUp)

        /* ArrayList<Dog>의 변수 dog의 이미지와 데이터를 ImageView와 TextView에 담는다. */
        val write = writeList[position]
        title.text = write.title
        content.text = write.content
        boomUp.text = "추천 " + write.boomUp.toString()

        return view
    }
}