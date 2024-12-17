/*
* VehicleStatus.kt
* © 2024 Fleetio
* */
package com.example.levysample.data.status

/**
 * This corresponds to the vehicle_status_color
 * found in any vehicle...which seems to correlate
 * with the status names of, respectively,
 * out of service, in shop, in service, and inactive.
 *
 * @property status_name
 */
enum class VehicleStatusValues(val status_name: String) {
    STATUS_RED("red"),
    STATUS_GREEN("green"),
    STATUS_BLUE("blue")
    // TODO Admittedly not complete.
}
