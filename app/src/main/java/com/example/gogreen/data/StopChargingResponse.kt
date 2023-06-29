package com.example.gogreen.data

data class StopChargingResponse(
    val status: Boolean,
    val message: String? = "",
    val invoice: Invoice? = null,
)