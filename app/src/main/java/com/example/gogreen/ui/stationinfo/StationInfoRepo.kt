package com.example.gogreen.ui.stationinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gogreen.api.ApiInterface
import com.example.gogreen.api.Response
import com.example.gogreen.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class StationInfoRepo(private val apiInterface: ApiInterface) {

    private var chargeStatusLiveData = MutableLiveData<StartChargingResponse>()

    val chargeStatusData: LiveData<StartChargingResponse>
        get() {
            return chargeStatusLiveData
        }

    private var stopChargeLiveData = MutableLiveData<StopChargingResponse>()

    val stopChargeData: LiveData<StopChargingResponse>
        get() {
            return stopChargeLiveData
        }

    private var payChargeLiveData = MutableLiveData<StopChargingResponse>()

    val payChargeData: LiveData<StopChargingResponse>
        get() {
            return payChargeLiveData
        }

    private var verifyLiveData = MutableLiveData<VerifyIssuerResponse>()

    val verifyStatusData: LiveData<VerifyIssuerResponse>
        get() {
            return verifyLiveData
        }

    fun getChargingStatusInfo(req: StartChargingRequest): Job {
        var job: Job? = null
        try {
            job = MainScope().launch(Dispatchers.IO) {
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
            return job
        } catch (e: Exception){
            chargeStatusLiveData.postValue(StartChargingResponse(false, e.message))
        }
        return job!!
    }

    fun verifyIssuer(req: String): Job {
        var job: Job? = null
        try {
            job = MainScope().launch(Dispatchers.IO) {
                var response: VerifyIssuerResponse
                try {
                    response = apiInterface.verifyIssuer(req)
                    if (response.status){
                        verifyLiveData.postValue(response)
                    } else {
                        verifyLiveData.postValue(VerifyIssuerResponse(null, false, response.message))
                    }
                } catch (e: Exception){
                    verifyLiveData.postValue(VerifyIssuerResponse(null, false, e.message!!))
                }
            }
            return job
        } catch (e: Exception){
            verifyLiveData.postValue(VerifyIssuerResponse(null, false, e.message!!))
        }
        return job!!
    }

    fun stopCharging(req: StopChargingRequest): Job {
        var job: Job? = null
        try {
            job = MainScope().launch(Dispatchers.IO) {
                var response: StopChargingResponse
                try {
                    response = apiInterface.stopCharging(req)
                    if (response.status){
                        stopChargeLiveData.postValue(response)
                    } else {
                        stopChargeLiveData.postValue(StopChargingResponse(false, response.message))
                    }
                } catch (e: Exception){
                    stopChargeLiveData.postValue(StopChargingResponse(false, e.message))
                }
            }
            return job
        } catch (e: Exception){
            stopChargeLiveData.postValue(StopChargingResponse(false, e.message))
        }
        return job!!
    }

    fun payCharging(req: PayChargingRequest): Job {
        var job: Job? = null
        try {
            job = MainScope().launch(Dispatchers.IO) {
                var response: StopChargingResponse
                try {
                    response = apiInterface.payCharging(req)
                    if (response.status){
                        payChargeLiveData.postValue(response)
                    } else {
                        payChargeLiveData.postValue(StopChargingResponse(false, response.message))
                    }
                } catch (e: Exception){
                    payChargeLiveData.postValue(StopChargingResponse(false, e.message))
                }
            }
            return job
        } catch (e: Exception){
            payChargeLiveData.postValue(StopChargingResponse(false, e.message))
        }
        return job!!
    }

    fun getProgress(walletAddress: String, stationId: String): Job{
        var job: Job? = null
        try {
            job =MainScope().launch(Dispatchers.IO) {
                var response: StartChargingResponse
                try {
                    response = apiInterface.getProgress(walletAddress, stationId)
                    if (response.status){
                        chargeStatusLiveData.postValue(response)
                    } else {
                        chargeStatusLiveData.postValue(StartChargingResponse(response.status, response.message))
                    }
                } catch (e: Exception){
                    chargeStatusLiveData.postValue(StartChargingResponse(false, e.message))
                }
            }
            return job
        } catch (e: Exception){
            chargeStatusLiveData.postValue(StartChargingResponse(false, e.message))
        }
        return job!!
    }

}