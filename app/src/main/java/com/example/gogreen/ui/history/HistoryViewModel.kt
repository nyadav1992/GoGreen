package com.example.gogreen.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gogreen.data.HistoryData
import com.example.gogreen.data.StationInfo
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val historyRepo: HistoryRepo) : ViewModel() {

    val historyData: LiveData<HistoryData> = historyRepo.historyData

    fun getHistoryData(id: String) {historyRepo.getHistoryData(id)}


    private val _text = MutableLiveData<String>().apply {
        value = "Charge History"
    }
    val text: LiveData<String> = _text
}