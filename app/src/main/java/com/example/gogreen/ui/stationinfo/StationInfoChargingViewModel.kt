package com.example.gogreen.ui.stationinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.gogreen.data.PayChargingRequest
import com.example.gogreen.data.StartChargingRequest
import com.example.gogreen.data.StartChargingResponse
import com.example.gogreen.data.StopChargingRequest
import com.example.gogreen.data.StopChargingResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class StationInfoChargingViewModel @Inject constructor(private val repo: StationInfoRepo) : ViewModel() {
    val chargingStatusData: LiveData<StartChargingResponse> = repo.chargeStatusData
    val chargingStopData: LiveData<StopChargingResponse> = repo.stopChargeData
    val chargingPayData: LiveData<StopChargingResponse> = repo.payChargeData

    fun startCharging(req: StartChargingRequest): Job {
        return repo.getChargingStatusInfo(req)
    }

    fun stopCharging(req: StopChargingRequest): Job {
        return repo.stopCharging(req)
    }

    fun payCharging(req: PayChargingRequest): Job {
        return repo.payCharging(req)
    }
    fun getProgress(walletAddress: String, stationId: String): Job {
        return repo.getProgress(walletAddress, stationId)
    }
}