package com.example.gogreen.data

data class MyOrder(
    val current: Double,
    val energyTransferTimeInSeconds: Double,
    val energy_amount: Double,
    val order_id: String,
    val seller: String,
    val seller_id: String,
    val seller_location: String,
    val seller_name: String,
    val status: Double,
    val total_cost: Double,
    val transferRate: Double,
    val txn_id: String,
    val updationDate: String,
    var transactionDate: String,
    val voltage: Double,
    var chargeDuration: String,
    val energy_unit: String
)