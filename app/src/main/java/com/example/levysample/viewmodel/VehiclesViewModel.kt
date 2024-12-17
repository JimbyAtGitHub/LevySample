/*
* VehiclesViewModel.kt
* © 2024 Fleetio
* */

package com.example.levysample.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.levysample.api.RetrofitClient
import com.example.levysample.common.Tag
import com.example.levysample.data.vehicle.FilteredBy
import com.example.levysample.data.vehicle.Record
import com.example.levysample.data.vehicle.SortedBy
import com.example.levysample.data.vehicle.VehicleList
import kotlinx.coroutines.launch

class VehiclesViewModel : ViewModel() {

    private fun getHeadersMap(): Map<String, String> {
        val headers = mutableMapOf<String, String>()
        headers["Accept"] = "application/json"
        headers["Authorization"] = "Token a97bc2ca83bd92336f6d3789c709386016891820"
        headers["Account-Token"] = "fb153cfec9"
        headers["X-Api-Version"] = "2024-06-30"
        return headers
    }

    private fun patchHeadersMap(): Map<String, String> {
        val headers = mutableMapOf<String, String>()
        headers["Content-Type"] = "application/json"
        headers["Accept"] = "application/json"
        headers["Authorization"] = "Token a97bc2ca83bd92336f6d3789c709386016891820"
        headers["Account-Token"] = "fb153cfec9"
        headers["X-Api-Version"] = "2024-06-30"
        return headers
    }


    // Vehicles

    private val _vehicleList = MutableLiveData(
        VehicleList()
    )
    val vehicleList: LiveData<VehicleList> get() = _vehicleList

    /**
     * GET secure.fleetio.com/api/v1/vehicles
     * plus the following query parameters.
     *
     * @param startCursor indicates the first record to be supplied.
     * @param perPage indicates the number of records to be supplied in this page.
     * @param filter used to limit records.
     * @param sort used to sort records.
     */
    fun getVehicles(
        startCursor: String?,
        perPage: Int,
        filter: FilteredBy?,
        sort: SortedBy?
    ) {
        viewModelScope.launch {
            Log.i(Tag.VIEW, "getVehicles()")
            try {
                _vehicleList.value = RetrofitClient.levySampleApiService.getVehicles(
                    headers = getHeadersMap(),
                    startCursor = startCursor,
                    perPage = perPage,
                    filter = filter,
                    sort = sort
                )
                Log.i(Tag.VIEW, "There are ${vehicleList.value?.records?.count()} vehicles in this page.")
            } catch (e: Exception) {
                Log.e(Tag.VIEW, "stacktrace is ${e.stackTraceToString()}")
            }
        }
    }

    // A particular Vehicle

    private val _vehicle = MutableLiveData(
        Record()
    )
    val vehicle: LiveData<Record> get() = _vehicle

    /**
     * GET secure.fleetio.com/api/v1/vehicles/number
     * where number is the parameter below.
     *
     * @param id unique identifier for the vehicle sought.
     */
    fun getVehicleRecord(
        id: Int
    ) {
        viewModelScope.launch {
            Log.i(Tag.VIEW, "getVehicleRecord()")
            try {
                _vehicle.value = RetrofitClient.levySampleApiService.getVehicleRecord(
                    headers = getHeadersMap(),
                    id = id
                )
            } catch (e: Exception) {
                Log.e(Tag.VIEW, "stacktrace is ${e.stackTraceToString()}")
            }
        }
    }

    /**
     * PATCH secure.fleetio.com/api/v1/vehicles/number
     * where number is the parameter below.
     *
     * @param id unique identifier for the vehicle to be updated.
     * @param newRecord an HTTP body citing the changed parameter(s).
     */
    fun patchVehicleStatus(
        id: Int,
        newRecord: Record
    ) {
        viewModelScope.launch {
            try {
                _vehicle.value = RetrofitClient.levySampleApiService.patchVehicleStatus(
                    headers = patchHeadersMap(),
                    id = id,
                    newRecord = newRecord
                )
            } catch (e: Exception) {
                Log.e(Tag.VIEW, "stacktrace is ${e.stackTraceToString()}")
            }
        }
    }

    // Initialization

    init {
        Log.d(Tag.VIEW, "init {...}")
        viewModelScope.launch {
            // Observe status code live data.
            RetrofitClient.getStatusCodeLiveData().observeForever { statusCode ->
                if (statusCode == 200) {
                    Log.d(Tag.VIEW, "Received status code: 200")
                } else {
                    Log.e(Tag.VIEW, "Received status code: $statusCode")
                }
            }

            // Get the vehicles list
            getVehicles(
                startCursor = null,
                perPage = 5,
                filter = null,
                sort = null
            )
        }
    }

}