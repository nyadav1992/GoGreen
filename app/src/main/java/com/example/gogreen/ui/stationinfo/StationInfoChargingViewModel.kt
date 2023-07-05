package com.example.gogreen.ui.stationinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.gogreen.data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject

@HiltViewModel
class StationInfoChargingViewModel @Inject constructor(private val repo: StationInfoRepo) : ViewModel() {
    val chargingStatusData: LiveData<StartChargingResponse> = repo.chargeStatusData
    val chargingStopData: LiveData<StopChargingResponse> = repo.stopChargeData
    val chargingPayData: LiveData<StopChargingResponse> = repo.payChargeData
    val verifyIssuerData: LiveData<VerifyIssuerResponse> = repo.verifyStatusData

    fun startCharging(req: StartChargingRequest): Job {
        return repo.getChargingStatusInfo(req)
    }

    fun verifyIssuer(req: String): Job {
        return repo.verifyIssuer(req)
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