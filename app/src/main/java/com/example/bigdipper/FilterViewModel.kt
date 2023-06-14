package com.example.bigdipper



import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilterViewModel:ViewModel() {
    var age = MutableLiveData<String>()
    var field = MutableLiveData<String>()
}