package com.example.gogreen.data

data class StationInfo(
    val message: String? = "",
    val error: String? = "",
    val station_data: StationData? = null,
    val status: Boolean,
    val value: String? = "",
    val code: String? = "",
    val version: String = ""
)