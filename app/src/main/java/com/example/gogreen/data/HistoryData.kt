package com.example.gogreen.data

data class HistoryData(
    val message: String? = "",
    val myOrders: List<MyOrder>? = null,
    val status: Boolean,
    val value: String? = "",
    val code: String? = "",
    val version: String = ""
)