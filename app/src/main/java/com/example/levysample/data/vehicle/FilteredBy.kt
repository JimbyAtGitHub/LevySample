/*
* FilteredBy.kt
* © 2024 Fleetio
* */
package com.example.levysample.data.vehicle

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

data class FilteredBy(
    val created_at: CreatedAt = CreatedAt(lt = ""),
    val external_id: ExternalId = ExternalId(like = ""),
    val labels: Labels = Labels(include = ""),
    val license_plate: LicensePlate = LicensePlate(like = ""),
    val name: Name = Name(like = ""),
    val updated_at: UpdatedAt = UpdatedAt(lt = ""),
    val vin: Vin = Vin(like = "")
)

data class CreatedAt(
    val lt: String
)

data class ExternalId(
    val like: String
)

data class Labels(
    val include: String
)

data class LicensePlate(
    val like: String
)

data class Name(
    val like: String
)

data class UpdatedAt(
    val lt: String
)

data class Vin(
    val like: String
)
