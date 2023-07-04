package com.example.gogreen.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gogreen.api.ApiInterface
import com.example.gogreen.api.Response
import com.example.gogreen.data.StationData
import com.example.gogreen.data.StationInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class HomeRepo(private val apiInterface: ApiInterface) {

    private lateinit var job: Job
    private var stationLiveData = MutableLiveData<StationInfo>()

    val stationData: LiveData<StationInfo>
        get() {
            return stationLiveData
        }

    fun getChargingStationInfo(id: String): Job{
        try {
            job = MainScope().launch(Dispatchers.IO) {
                var response: StationInfo
                try {
                    response = apiInterface.getStationInfo(id)
                    if (response.status){
                        stationLiveData.postValue(response)
                    } else {
                        stationLiveData.postValue(StationInfo(response.message, response.error, null, response.status, response.value, response.code, response.version))
                    }
                } catch (e: Exception){
                    stationLiveData.postValue(StationInfo("Something went wrong","", null, false))
                }
            }
            return job
        } catch (e: Exception){
            stationLiveData.postValue(StationInfo("Something went wrong","", null, false))
        }
        return job
    }

}