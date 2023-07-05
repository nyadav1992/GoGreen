package com.example.gogreen.data

data class Invoice(
    val current: Int,
    val energyTransferTimeInSeconds: Int,
    val energy_amount: Int,
    val order_id: String,
    val seller: String,
    val seller_id: String,
    val seller_location: String,
    val seller_name: String,
    val status: Int,
    val total_cost: Int,
    val transferRate: Int,
    val txn_id: String,
    val updationDate: String,
    val voltage: Int,
    var timeString: String = "",
    var energy_unit: String = ""
)