package com.example.gogreen.data

data class MyOrder(
    val energy_amount: Int,
    val energy_source: String,
    val energy_type: String,
    val from_prosumer: String,
    val order_id: String,
    val status: Int,
    val total_cost: Int,
    val txn_id: String
)