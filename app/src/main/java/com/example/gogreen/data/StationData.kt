package com.example.gogreen.data

data class StationData(
    val available_energy: Int,
    val energy_rate: Int,
    val energy_source: String,
    val energy_type: String,
    val id: String,
    val station_address: String,
    val station_name: String,
    val total_cost: Int,
    val energy_unit: String,
    val transferRate: Int,
    val current: Int,
    val voltage: Int
)