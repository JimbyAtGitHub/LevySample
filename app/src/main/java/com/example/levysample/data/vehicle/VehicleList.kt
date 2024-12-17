/*
* VehicleList.kt
* © 2024 Fleetio
* */
package com.example.levysample.data.vehicle

data class VehicleList(
    val estimated_remaining_count: Int = 0,
    val filtered_by: List<FilteredBy> = emptyList(),
    val next_cursor: String? = null,
    val per_page: Int = 0,
    val records: List<Record> = emptyList(),
    val sorted_by: List<SortedBy> = emptyList(),
    val start_cursor: String = ""
)

