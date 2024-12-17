/*
* VehicleDetailView.kt
* © 2024 Fleetio
* */

package com.example.levysample.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.levysample.common.Tag
import com.example.levysample.data.status.VehicleStatusValues
import com.example.levysample.ui.theme.FleetioLightBlue
import com.example.levysample.ui.theme.FleetioMediumBlue
import com.example.levysample.viewmodel.VehiclesViewModel

@Composable
fun VehicleDetailView(
    modifier: Modifier = Modifier,
    viewModel: VehiclesViewModel,
    onBackButtonClicked: () -> Unit
) {
    val record = viewModel.vehicle.observeAsState()

    Column(
        modifier = modifier
            .padding(20.dp)
            .background(FleetioMediumBlue)
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Button for going back to the main list.
        ElevatedButton(
            modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp),
            shape = MaterialTheme.shapes.small,
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary,
                disabledContainerColor = Color.White,
                disabledContentColor = Color.Black
            ),
            onClick = onBackButtonClicked
        ) {
            Text("Back to list")
        }

        // Texts displaying items of detail for a vehicle.
        if (record.value != null) {

            // Based on observing a correlation between the status name
            // and the status color, the name text gets colored accordingly.
            Text(
                text = "Status: ${record.value!!.vehicle_status_name}",
                color = when (record.value!!.vehicle_status_color) {
                    "red" -> MaterialTheme.colorScheme.error
                    "green" -> Color.Green
                    "blue" -> Color.Cyan
                    else -> MaterialTheme.colorScheme.onSecondary
                }
            )

            Text(
                text = "Status ID: ${record.value!!.vehicle_status_id}",
                color = MaterialTheme.colorScheme.onSecondary
            )

            if (record.value?.in_service_date != null) {
                Text(
                    text = "In service: ${record.value?.in_service_date!!}",
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

            Text(
                text = record.value!!.vehicle_type_name ?: "Name unknown",
                color = MaterialTheme.colorScheme.onSecondary
            )

            Text(
                text = "VIN: ${record.value!!.vin}",
                color = MaterialTheme.colorScheme.onSecondary
            )

            Text(
                text = "Primary meter value: ${record.value!!.primary_meter_value}",
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        // Three buttons for new status, only two of which should be visible.
        // (It makes no sense, for example, to change status from Active to Active.)

        // "Make active" button.
        if (record.value!!.vehicle_status_color
            != VehicleStatusValues.STATUS_GREEN.status_name
        ) {
            ChangeStatusButton(
                modifier = modifier,
                viewModel = viewModel,
                desiredStatus = VehicleStatusValues.STATUS_GREEN
            )
        }

        // "Make inactive" button.
        if (record.value!!.vehicle_status_color
            != VehicleStatusValues.STATUS_BLUE.status_name
        ) {
            ChangeStatusButton(
                modifier = modifier,
                viewModel = viewModel,
                desiredStatus = VehicleStatusValues.STATUS_BLUE
            )
        }

        // "Go out of service" button.
        if (record.value!!.vehicle_status_color
            != VehicleStatusValues.STATUS_RED.status_name
        ) {
            ChangeStatusButton(
                modifier = modifier,
                viewModel = viewModel,
                desiredStatus = VehicleStatusValues.STATUS_RED
            )
        }

        // TODO Not sure of color mapping, upon discovery of various other statuses.
    }

    Spacer(modifier.heightIn(15.dp))
}

@Composable
fun ChangeStatusButton(
    modifier: Modifier = Modifier,
    viewModel: VehiclesViewModel,
    desiredStatus: VehicleStatusValues,
) {
    val record = viewModel.vehicle.observeAsState()

    ElevatedButton(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 8.dp),
        shape = MaterialTheme.shapes.small,
        colors = ButtonColors(
            containerColor = when (desiredStatus) {
                VehicleStatusValues.STATUS_GREEN -> MaterialTheme.colorScheme.tertiary
                VehicleStatusValues.STATUS_BLUE -> MaterialTheme.colorScheme.secondary
                VehicleStatusValues.STATUS_RED -> MaterialTheme.colorScheme.error
            },
            contentColor = when (desiredStatus) {
                VehicleStatusValues.STATUS_GREEN -> MaterialTheme.colorScheme.onTertiary
                VehicleStatusValues.STATUS_BLUE -> MaterialTheme.colorScheme.onSecondary
                VehicleStatusValues.STATUS_RED -> MaterialTheme.colorScheme.onError
            },
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ),
        onClick = {
            val updateRecord = record.value?.copy(
                vehicle_status_color = when (desiredStatus) {
                    VehicleStatusValues.STATUS_GREEN -> VehicleStatusValues.STATUS_RED.status_name
                    VehicleStatusValues.STATUS_BLUE -> VehicleStatusValues.STATUS_RED.status_name
                    VehicleStatusValues.STATUS_RED -> VehicleStatusValues.STATUS_RED.status_name
                }
            )
            if (updateRecord != null) {
                viewModel.patchVehicleStatus(
                    id = record.value!!.id ?: 0,
                    newRecord = updateRecord
                )
                // TODO Nice try.
                // TODO However, per https://developer.fleetio.com/docs/api/v-1-vehicles-update,
                // TODO that second parameter needs to be a data class based on the data-raw
                // TODO cited in the curl command. My guess is that every single field therein
                // TODO must be populated with data from its counterpart in record
                // TODO ---where possible--
                // TODO the one change being the new value for vehicle_status_color.
                // TODO As it is, these three buttons spawn a 422.
            }
        }
    ) {
        Text(
            text = when (desiredStatus) {
                VehicleStatusValues.STATUS_GREEN -> "Make active"
                VehicleStatusValues.STATUS_BLUE -> "Make inactive"
                VehicleStatusValues.STATUS_RED -> "Go out of service"
            }
        )
    }
}
