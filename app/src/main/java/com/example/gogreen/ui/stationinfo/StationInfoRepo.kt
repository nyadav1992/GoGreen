package com.example.gogreen.ui.stationinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gogreen.api.ApiInterface
import com.example.gogreen.api.Response
import com.example.gogreen.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class StationInfoRepo(private val apiInterface: ApiInterface) {

    private var chargeStatusLiveData = MutableLiveData<StartChargingResponse>()

    val chargeStatusData: LiveData<StartChargingResponse>
        get() {
            return chargeStatusLiveData
        }

    fun getChargingStatusInfo(req: StartChargingRequest){
        try {
            MainScope().launch(Dispatchers.IO) {
                var response: StartChargingResponse
                try {
                    response = apiInterface.startCharging(req)
                    if (response.status){
                        chargeStatusLiveData.postValue(response)
                    } else {
                        chargeStatusLiveData.postValue(StartChargingResponse(response.status, response.message))
                    }
                } catch (e: Exception){
                    chargeStatusLiveData.postValue(StartChargingResponse(false, e.message))
                }
            }
        } catch (e: Exception){
            chargeStatusLiveData.postValue(StartChargingResponse(false, e.message))
        }
    }

}