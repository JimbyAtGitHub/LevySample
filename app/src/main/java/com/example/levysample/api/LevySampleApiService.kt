/*
* LevySampleApiService.kt
* © 2024 Fleetio
* */

package com.example.levysample.api

import com.example.levysample.data.vehicle.FilteredBy
import com.example.levysample.data.vehicle.Record
import com.example.levysample.data.vehicle.SortedBy
import com.example.levysample.data.vehicle.VehicleList
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface LevySampleApiService {

    @GET("/api/v1/vehicles")
    suspend fun getVehicles(
        @HeaderMap headers: Map<String, String>,
        @Query("start_cursor") startCursor: String? = null,
        @Query("per_page") perPage: Int,
        @Query("filter") filter: FilteredBy? = null,
        @Query("sort") sort: SortedBy? = null
    ): VehicleList

    @GET("/api/v1/vehicles/{id}")
    suspend fun getVehicleRecord(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int
    ): Record

    @PATCH("/api/v1/vehicles/{id}")
    suspend fun patchVehicleStatus(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int,
        @Body newRecord: Record
    ): Record
}
