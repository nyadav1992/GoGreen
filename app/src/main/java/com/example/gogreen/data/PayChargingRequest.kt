package com.example.gogreen.data

data class PayChargingRequest(
    val order_id: String,
    val is_pay_success: Boolean = true,
    val transaction_id: String
)
