package com.example.bigdipper

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bigdipper.databinding.FragmentExploreBinding
import com.example.bigdipper.databinding.FragmentFilterSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import org.w3c.dom.Text

class FilterSheet : BottomSheetDialogFragment() {
    lateinit var binding: FragmentFilterSheetBinding
    lateinit var filterViewModel: FilterViewModel
    var ageRadioChecked: Int = -1
    var fieldRadioChecked: Int = -1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        filterViewModel = ViewModelProvider(requireActivity()).get(FilterViewModel::class.java)
        if(filterViewModel.age.value != null){
            ageRadioChecked = filterIndex("age", filterViewModel.age.value!!)
            filterCheck("age", ageRadioChecked)
        }
        if(filterViewModel.field.value != null){
            fieldRadioChecked = filterIndex("field", filterViewModel.field.value!!) + 10
            filterCheck("field", fieldRadioChecked)
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val primaryColor = ContextCompat.getColor(requireContext(), com.google.android.material.R.color.design_default_color_primary)
        binding = FragmentFilterSheetBinding.inflate(inflater, container, false)
        binding.apply {
            resetBtn.setOnClickListener {
                if(ageRadioChecked!= -1)  filterClear("age", ageRadioChecked)
                if(fieldRadioChecked!= -1)  filterClear("field", fieldRadioChecked)
                filterViewModel.age.value = null
                filterViewModel.field.value = null
                dismiss()
            }
            FilterOptionSingleton.getInstance().ageGroupFilterNames.mapIndexed {
                index, it ->
                val tagView = inflater.inflate(R.layout.filter_layout, fieldFilter, false)
                val tagTextView = tagView.findViewById<TextView>(R.id.filterName)
                tagTextView.id = index
                tagTextView.text = it.filterName
                tagTextView.setOnClickListener {view ->
                    if(ageRadioChecked !=-1){
                        val textColor = ContextCompat.getColor(requireContext(), R.color.black_main)
                        val preTagTextView =  ageFilter.findViewById<TextView>(ageRadioChecked)
                        preTagTextView.setBackgroundColor(Color.parseColor("#D9D9D9"))
                        preTagTextView.setTextColor(textColor)
                        if(ageRadioChecked == view.id){
                            filterViewModel.age.value = null
                            ageRadioChecked = -1
                            return@setOnClickListener
                        }
                    }
                    view.setBackgroundColor(primaryColor)
                    (view as TextView).setTextColor(Color.WHITE)
                    ageRadioChecked = view.id
                    filterViewModel.age.value = ageFilterValue(view.text.toString())
                }
                ageFilter.addView(tagView)
            }
            FilterOptionSingleton.getInstance().fieldFilterNames.mapIndexed {
                index, it ->
                val tagView = inflater.inflate(R.layout.filter_layout, fieldFilter, false)
                val tagTextView = tagView.findViewById<TextView>(R.id.filterName)
                tagTextView.id = index + 10
                tagTextView.text = it.filterName
                tagTextView.setOnClickListener {view ->
                    if(fieldRadioChecked !=-1){
                        val textColor = ContextCompat.getColor(requireContext(), R.color.black_main)
                        val preTagTextView =  fieldFilter.findViewById<TextView>(fieldRadioChecked)
                        preTagTextView.setBackgroundColor(Color.parseColor("#D9D9D9"))
                        preTagTextView.setTextColor(textColor)
                        if(fieldRadioChecked == view.id){
                            filterViewModel.field.value = null
                            fieldRadioChecked = -1
                            return@setOnClickListener
                        }
                    }
                    view.setBackgroundColor(primaryColor)
                    (view as TextView).setTextColor(Color.WHITE)
                    fieldRadioChecked = view.id
                    filterViewModel.field.value = fieldFilterValue(view.text.toString())
                }
                fieldFilter.addView(tagView)
            }
        }
        return binding.root
    }

    private fun ageFilterValue(value: String): String {
        return FilterOptionSingleton.getInstance().ageGroupFilterNames.find { it.filterName == value }?.filterValue!!
    }

    private fun fieldFilterValue(value: String): String {
        return FilterOptionSingleton.getInstance().fieldFilterNames.find { it.filterName == value }?.filterValue!!
    }

    private fun filterIndex(type: String, value:String):Int{
        if(type == "age"){
            val data =FilterOptionSingleton.getInstance().ageGroupFilterNames.find { it.filterValue == value }
            return FilterOptionSingleton.getInstance().ageGroupFilterNames.indexOf(data)
        }else{
            val data =FilterOptionSingleton.getInstance().fieldFilterNames.find { it.filterValue == value }
            return FilterOptionSingleton.getInstance().fieldFilterNames.indexOf(data)
        }
    }

    private fun filterCheck(type: String, id :Int){
        val primaryColor = ContextCompat.getColor(requireContext(), com.google.android.material.R.color.design_default_color_primary)
        when (type) {
            "age" -> {
                val ageTagView =  binding.ageFilter.findViewById<TextView>(id)
                ageTagView.setTextColor(Color.WHITE)
                ageTagView.setBackgroundColor(primaryColor)
            }
            "field" -> {
                val fieldTagView =  binding.fieldFilter.findViewById<TextView>(id)
                fieldTagView.setTextColor(Color.WHITE)
                fieldTagView.setBackgroundColor(primaryColor)
            }
            else -> {
                return
            }
        }
    }

    private fun filterClear(type: String, id :Int){
        val textColor = ContextCompat.getColor(requireContext(), R.color.black_main)
        when (type) {
            "age" -> {
                val ageTagView =  binding.ageFilter.findViewById<TextView>(id)
                ageTagView.setBackgroundColor(Color.parseColor("#D9D9D9"))
                ageTagView.setTextColor(textColor)
                ageRadioChecked = -1
            }
            "field" -> {
                val fieldTagView =  binding.fieldFilter.findViewById<TextView>(id)
                fieldTagView.setBackgroundColor(Color.parseColor("#D9D9D9"))
                fieldTagView.setTextColor(textColor)
                fieldRadioChecked = -1
            }
            else -> {
                return
            }
        }
    }

}