package com.example.bigdipper

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// 북클럽 가입 여부에 따른 flag
private var join = false;

class HomeFragment: Fragment() {
    // TODOLIST: Rename and change types of parameters
    lateinit var adapter: BookClubAdapter
    val userManager = UserDataManager.getInstance()
    val CurUserData = userManager.getUserData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        if (!CurUserData?.bookClubList.isNullOrEmpty()) {
            join = true
        }

        if(join){
            val view = inflater.inflate(R.layout.fragment_home, container, false)
            view.findViewById<TextView>(R.id.exploreDetails).text = CurUserData?.NickName + "님, 독서하기 좋은 하루입니다!! \uD83C\uDFC3\uD83C\uDFFB\uD83D\uDCD6"
            val joinedBookClubList = view.findViewById<RecyclerView>(R.id.joinedBookClubList)
            joinedBookClubList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter =  BookClubAdapter(CurUserData!!.bookClubList)
            adapter.itemClickListener = object:BookClubAdapter.onItemClickListener{
                override fun onItemClick(data: BookClubData, position: Int) {
                    val intent = Intent(activity, BookClubPage::class.java)
                    intent.putExtra("data", data)
                    startActivity(intent)
                }
            }
            joinedBookClubList.adapter = adapter
            return view
        }else{
            val view = inflater.inflate(R.layout.fragment_home_unjoin, container, false)
            val exploreButton = view.findViewById<Button>(R.id.exploreBookClubBtn)
            exploreButton.setOnClickListener {
                val exploreFragment = ExploreFragment()
                (activity as MainActivity).replaceFragment(exploreFragment)
                // ExploreFragment로 전환되었을 때, 메뉴 선택 상태 변경
                (activity as MainActivity).binding.mainBottomNavigation.menu.findItem(R.id.explore).isChecked = true
            }
            return view
        }

    }
}