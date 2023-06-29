package com.example.gogreen.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : ViewModel() {

    val showBottomNavigation = MutableLiveData<Boolean>()
    val message = MutableLiveData<String>()

    fun sendMessage(text: String) {
        message.postValue(text)
    }

    fun getMessage() {
        message
    }

    fun setNavigationVisibility(boolean: Boolean) {
        showBottomNavigation.value = boolean
    }

    fun getNavigationVisibility() {
        showBottomNavigation
    }

}