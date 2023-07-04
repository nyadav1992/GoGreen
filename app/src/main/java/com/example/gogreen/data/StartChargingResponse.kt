package com.example.gogreen.data

data class StartChargingResponse(
    val status: Boolean = true,
    val message: String? = "",
    val energyConsumed: Int? = 0,
    val price: Int? = 0,
    val requiredEnergy: Int? = 0,
    val timePassed: Int? = 0,
    val totalTime: Int? = 0,
    val transferRate: Int? = 0,
    val totalPrice: Int? = 0
)