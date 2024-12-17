package com.example.levysample.data.vehicle

data class CustomFields (
    val start_cursor: String = "",
    val next_cursor: String = "",
    val per_page: Int = 0,
    val estimated_remaining_count: Int = 0,
    val filteredBy: FilteredBy = FilteredBy(),
    val sortedBy: SortedBy = SortedBy(),
    val records: List<Record> = emptyList()
)