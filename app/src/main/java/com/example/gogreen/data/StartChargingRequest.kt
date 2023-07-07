package com.example.gogreen.data

data class StartChargingRequest(
    val station_id: String,
    val my_wallet_address: String,
    val required_energy: Double
)
