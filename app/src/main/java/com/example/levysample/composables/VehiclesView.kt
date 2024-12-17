/*
* VehiclesView.kt
* © 2024 Fleetio
* */

package com.example.levysample.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.levysample.common.Tag
import com.example.levysample.data.vehicle.Record
import com.example.levysample.viewmodel.VehiclesViewModel

@Composable
fun VehiclesView(
    modifier: Modifier = Modifier,
    viewModel: VehiclesViewModel,
    onClickForDetails: (Int) -> Unit,
) {
    val vehicularData = viewModel.vehicleList.observeAsState()
    val records = vehicularData.value?.records ?: emptyList()

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth(),
    ) {
        val nextCursor = vehicularData.value?.next_cursor
        var startCursor: String = ""

        // Title text
        Text(
            modifier = modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 20.dp),
            text = "Vehicles",
            color = MaterialTheme.colorScheme.onPrimary,
            style = TextStyle(
                fontSize = 24.sp
            )

        )

        Row(
            modifier = modifier.fillMaxWidth()
        ) {
            // Button for previous page.
            ElevatedButton(
                modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                enabled = true, // TODO disable if we're at the beginning.
                shape = MaterialTheme.shapes.medium,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.Black
                ),
                onClick = {
                    Log.d(Tag.VIEW, "P start is $startCursor")
                    Log.d(Tag.VIEW, "P next is  $nextCursor")
                    // Get the previous page.
                    viewModel.getVehicles(
                        startCursor = startCursor,
                        perPage = 5,
                        filter = null,
                        sort = null
                    )
                }
            ) {
                Text("Previous page")
            }

            Spacer(modifier = Modifier.weight(1f))  // This spacer takes all available space

            // Button for next page.
            ElevatedButton(
                modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                enabled = true, // TODO disable if we're at the end.
                shape = MaterialTheme.shapes.medium,
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.Black
                ),
                onClick = {
                    Log.d(Tag.VIEW, "N start is $startCursor")
                    Log.d(Tag.VIEW, "N next is  $nextCursor")
                    // Get the next page.
                    if (nextCursor != null) {
                        startCursor = nextCursor // for going to previous
                    }
                    viewModel.getVehicles(
                        startCursor = nextCursor,
                        perPage = 5,
                        filter = null,
                        sort = null
                    )
                }
            ) {
                Text("Next page")
            }
        }

        // Column of tiles, one for each vehicle.
        if (vehicularData.value != null) {
            LazyColumn(modifier = modifier.padding(vertical = 4.dp)) {
                items(records) { record ->
                    VehicleTileView(
                        record = record,
                        onClick = { onClickForDetails(record.id?: 0) }
                    )
                }
            }
        }
    }
}

@Composable
fun VehicleTileView(
    modifier: Modifier = Modifier,
    record: Record,
    onClick: () -> Unit
) {
    // Tile-as-a-button, displaying summary information for a vehicle.
    ElevatedButton(
        modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.Black
        ),
        onClick = onClick
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = record.vehicle_type_name?: "Vehicle type name unknown",
                color = MaterialTheme.colorScheme.tertiary,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                    )
            )
            Text(record.make?: "Make unknown")
            Text(record.year?: "Year unknown")
            Text("${record.fuel_type_name?: ""} (${record.fuel_volume_units})")
            Text(record.vin?: "VIN unknown")
        }
    }
}
