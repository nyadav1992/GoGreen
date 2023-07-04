package com.example.gogreen.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gogreen.data.StationInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repo: ProfileRepo) : ViewModel() {

    val profileData: LiveData<StationInfo> = repo.profileData

    fun getProfileInfo(id: String): Job {
        return repo.getProfileInfo(id)
    }

    private val _text = MutableLiveData<String>().apply {
        value = "Michael"
    }
    val text: LiveData<String> = _text
}