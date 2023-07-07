package com.example.gogreen.data

data class StationData(
    val available_energy: Double,
    val energy_rate: Double,
    val energy_source: String,
    val energy_type: String,
    val id: String,
    val station_address: String,
    val station_name: String,
    val total_cost: Double,
    val energy_unit: String,
    val transferRate: Double,
    val current: Double,
    val voltage: Double
)