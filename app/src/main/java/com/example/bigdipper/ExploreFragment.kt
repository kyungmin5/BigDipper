package com.example.bigdipper

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bigdipper.databinding.FragmentExploreBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ExploreFragment : Fragment() {
    lateinit var adapter: BookClubAdapter
    lateinit var binding: FragmentExploreBinding
    private lateinit var filterViewModel: FilterViewModel

    lateinit var items:ArrayList<BookClubData>
    val itemList = mutableListOf<BookClubData>()


    // itemList을 리사이클러뷰에 설정
    //lateinit var items: ArrayList<BookClubData>
    lateinit var filteredData : ArrayList<BookClubData>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val primaryColor = ContextCompat.getColor(
            requireContext(),
            com.google.android.material.R.color.design_default_color_primary
        )
        filterViewModel = ViewModelProvider(requireActivity()).get(FilterViewModel::class.java)
        filterViewModel.age.observe(viewLifecycleOwner) {
            applyFilters(primaryColor)
        }

        filterViewModel.field.observe(viewLifecycleOwner) {
            applyFilters(primaryColor)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onResume() {
        super.onResume()
        binding.search.clearFocus()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val primaryColor = ContextCompat.getColor(
            requireContext(),
            com.google.android.material.R.color.design_default_color_primary
        )
        binding = FragmentExploreBinding.inflate(inflater, container, false)
        val databaseReference = FirebaseDatabase.getInstance().reference
        val clubListRef = databaseReference.child("clubs")
        clubListRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val club = data.getValue(BookClubData::class.java)
                    club?.let {
                        itemList.add(club)
                    }
                }
                items=ArrayList(itemList)
                filteredData=items
                init()
            }
            override fun onCancelled(error: DatabaseError) {
                // 데이터베이스 조회 실패 시 처리
            }
        })

        return binding.root
    }
    fun init() {
        val primaryColor = ContextCompat.getColor(
            requireContext(),
            com.google.android.material.R.color.design_default_color_primary
        )

        binding.apply {

            bookClubList.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = BookClubAdapter(items)
            adapter.itemClickListener = object : BookClubAdapter.onItemClickListener {
                override fun onItemClick(data: BookClubData, position: Int) {
                    val intent = Intent(context, BookClubDetail::class.java)
                    intent.putExtra("clickedData", data)
                    startActivity(intent)
                }
            }
            bookClubList.adapter = adapter
            exploreFilter.setOnClickListener {
                FilterSheet().show(requireActivity().supportFragmentManager, "filter")
            }
            search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        if (newText.isBlank()) { // 빈 문자열인 경우 필터만 반영된 아이템을 보여줍니다.
                            applyFilters(primaryColor)
                        } else {
                            val queryFilteredData = filteredData.filter { item ->
                                item.clubName.contains(
                                    newText,
                                    ignoreCase = true
                                ) || item.currentBook.contains(newText, ignoreCase = true)
                            } as ArrayList<BookClubData>
                            adapter.updateData(queryFilteredData)
                        }
                    }
                    return true
                }
            })
            createBookClubFab.setOnClickListener {
                val intent = Intent(activity, MakingClub::class.java)
                startActivity(intent)
            }
        }
    }

//            createBookClubFab.setOnClickListener {
//
//                // 북클럽 정보 생성
//                val club = BookClubData(
//                    clubImg = "imgstr",
//                    currentBook = "마음의 치유",
//                    clubName = "철학과 생각",
//                    tags = arrayListOf("Self-Improvement/Hobby", "Culture/Art"),
//                    ageGroup = "All ages",
//                    clubDetails = "철학 관련 책을 읽으며 토론을 주로 하는 북클럽입니다. 관심 있는 분 많이 오세요.",
//                    memberNum = "12",
//                    createdAt = "2023-06-15T16:45:00Z",
//                    totalMemberNum = "30",
//                    clubRules = "북클럽 규칙",
//                    booksHaveRead = arrayListOf("읽은 책1", "읽은 책2"),
//                    creator = "북클럽 생성자"
//                )
//
//                val database = FirebaseDatabase.getInstance()
//                val reference = database.reference.child("clubs")
//                reference.push().setValue(club)
//
//
//



    // 더미 데이터 생성 함수


    private fun applyFilters(primaryColor: Int) {
        val age = filterViewModel.age.value
        val field = filterViewModel.field.value

        if (age == null && field == null) {
            filteredData = items
            adapter.updateData(filteredData)
            binding.exploreFilter.setColorFilter(Color.parseColor("#989898"))
            binding.bookClubList.visibility = View.VISIBLE
            binding.notFound.visibility = View.GONE
            if (filteredData.size == 0) {
                binding.bookClubList.visibility = View.GONE
                binding.notFound.visibility = View.VISIBLE
            } else {
                binding.bookClubList.visibility = View.VISIBLE
                binding.notFound.visibility = View.GONE
            }
            return
        }

        binding.exploreFilter.setColorFilter(primaryColor)

        filteredData = items.filter { item ->
            (age == null || item.ageGroup == age) && (field == null || item.tags.contains(field))
        } as ArrayList<BookClubData>

        adapter.updateData(filteredData)

        if (filteredData.size == 0) {
            binding.bookClubList.visibility = View.GONE
            binding.notFound.visibility = View.VISIBLE
        } else {
            binding.bookClubList.visibility = View.VISIBLE
            binding.notFound.visibility = View.GONE
        }
    }



}