package com.example.gogreen.data

data class StartChargingRequest(
    val station_id: String,
    val required_energy: Int,
    val my_wallet_address: String
)
