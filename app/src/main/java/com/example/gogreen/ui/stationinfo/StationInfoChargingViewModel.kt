package com.example.gogreen.ui.stationinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.gogreen.data.StartChargingRequest
import com.example.gogreen.data.StartChargingResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StationInfoChargingViewModel @Inject constructor(private val repo: StationInfoRepo) : ViewModel() {
    val chargingStatusData: LiveData<StartChargingResponse> = repo.chargeStatusData

    fun getStationInfo(req: StartChargingRequest) {repo.getChargingStatusInfo(req)}
}