package com.example.gogreen.data

data class StartChargingResponse(
    val status: Boolean = true,
    val message: String? = "",
    val energyConsumed: Double? = 0.0,
    val price: Double? = 0.0,
    val requiredEnergy: Double? = 0.0,
    val timePassed: Int? = 0,
    val totalTime: Int? = 0,
    val transferRate: Int? = 0,
    val totalPrice: Double? = 0.0
)