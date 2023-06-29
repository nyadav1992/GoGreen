package com.example.gogreen.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gogreen.api.ApiInterface
import com.example.gogreen.data.HistoryData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class HistoryRepo(private val apiInterface: ApiInterface) {

    private var historyLiveData = MutableLiveData<HistoryData>()

    val historyData: LiveData<HistoryData>
        get() {
            return historyLiveData
        }

    fun getHistoryData(id: String){
        try {
            MainScope().launch(Dispatchers.IO) {
                val response: HistoryData
                try {
                    response = apiInterface.getHistoryData(id)
                    if (response.status){
                        historyLiveData.postValue(response)
                    } else {
                        historyLiveData.postValue(HistoryData(response.message, response.myOrders, true))
                    }
                } catch (e: Exception){
                    historyLiveData.postValue(HistoryData("Something went wrong",null, false))
                }
            }
        } catch (e: Exception){
            historyLiveData.postValue(HistoryData("Something went wrong",null, false, ""))
        }
    }

}