package com.example.bigdipper

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bigdipper.databinding.FragmentExploreBinding


class ExploreFragment : Fragment() {
    lateinit var adapter: BookClubAdapter
    lateinit var binding: FragmentExploreBinding
    private lateinit var filterViewModel: FilterViewModel

    var items = dummyGenerator() // 초기 데이터
    var filteredData = items

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
                                item.clubName.contains(newText, ignoreCase = true) || item.currentBook.contains(newText, ignoreCase = true)
                            } as ArrayList<BookClubData>
                            adapter.updateData(queryFilteredData)
                        }
                    }
                    return true
                }
            })
        }

        return binding.root
    }

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


    private fun dummyGenerator(): ArrayList<BookClubData>{
        var dummy = ArrayList<BookClubData>();
        var item1 = BookClubData("imgstr", "마케터, 마케팅을 말하다", "마케팅 북클럽" ,
            arrayListOf<String>("PM/Marketing", "Business/Economics"), "Adult", "마케팅을 공부하는 북클럽입니다. 관심 있는 분 많이 오세요.", "10",  "2023-01-01T09:00:00Z",
            "20",
            "Please be respectful and participate actively.",
            arrayListOf("Book A", "Book B", "Book C"), "책장수")

        var item2 = BookClubData("imgstr", "Do it! 점프 투 파이썬", "창업/컴퓨터 북클럽" ,
            arrayListOf<String>("IT/Computer", "Business/Economics"), "Adolescent","창업/컴퓨터를 공부하는 북클럽입니다. 관심 있는 분 많이 오세요.", "7", "2023-02-01T14:30:00Z",
            "15",
            "Share your thoughts and ideas openly.",
            arrayListOf("Book X", "Book Y"), "리딩마스터")

        var item3 = BookClubData("imgstr", "세이노의 가르침", "자기계발 북클럽" ,
            arrayListOf<String>("Self-Improvement/Hobby"), "Adult","자기계발 책 읽으면서 꾸준히 할 사람들이 모인 모임입니다. 많이 오세요.", "3",  "2023-03-10T19:45:00Z",
            "25",
            "Let's dive into the mysteries together.",
            arrayListOf("Book M", "Book N"), "문답사")

        var item4 = BookClubData("imgstr", "아쿠아리움이 문을 닫으면", "소설 북클럽" ,
            arrayListOf<String>("Poetry/Novel"), "Adolescent","소설 위주로 읽는 북클럽입니다 관심 있는 분 많이 오세요.", "5","2023-04-20T12:15:00Z",
            "10",
            "Share your favorite romance novels with us.",
            arrayListOf("Book R", "Book S", "Book T"), "독서왕")

        var item5 = BookClubData("imgstr", "마음의 자유", "철학 북클럽" ,
            arrayListOf<String>("Classics", "Culture/Art"), "All ages","철학 관련된 책을 읽으며 사색하는 북클럽입니다. 관심 있는 분 많이 오세요.", "12", "2023-05-05T10:30:00Z",
            "18",
            "Let's discuss the impact of history on literature.",
            arrayListOf("Book H"), "북마스터")

        var item6 = BookClubData("imgstr", "마음의 치유", "철학과 생각" ,
            arrayListOf<String>("Self-Improvement/Hobby", "Culture/Art"), "All ages","철학 관련 책을 읽으며 토론을 주로 하는 북클럽입니다. 관심 있는 분 많이 오세요.", "12", "2023-06-15T16:45:00Z",
            "30",
            "Immerse yourself in the world of fantasy.",
            arrayListOf("Book F", "Book G", "Book J"), "리더북웜")

        dummy.add(item1)
        dummy.add(item2)
        dummy.add(item3)
        dummy.add(item4)
        dummy.add(item5)
        dummy.add(item6)

        return dummy
    }
}