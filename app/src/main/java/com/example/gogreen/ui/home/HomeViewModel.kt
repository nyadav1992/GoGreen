package com.example.gogreen.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gogreen.api.Response
import com.example.gogreen.data.StationData
import com.example.gogreen.data.StationInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: HomeRepo) : ViewModel() {

    val stationData: LiveData<StationInfo> = repo.stationData

    fun getStationInfo(id: String): Job {
        return repo.getChargingStationInfo(id)
    }

    private var _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    var text: LiveData<String> = _text
}