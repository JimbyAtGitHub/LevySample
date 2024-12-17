/*
* MainActivity.kt
* © 2024 Fleetio
* */

package com.example.levysample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.levysample.common.Tag
import com.example.levysample.composables.VehicleDetailView
import com.example.levysample.composables.VehiclesView
import com.example.levysample.ui.theme.LevySampleTheme
import com.example.levysample.viewmodel.VehiclesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LevySampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainView()
                }
            }
        }
    }
}

@Composable
fun MainView(
    modifier: Modifier = Modifier,
    viewModel: VehiclesViewModel = VehiclesViewModel(),
) {
    Log.d(Tag.VIEW, "Greeting composable")

    var showDetails by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier
        .fillMaxSize()
        .background(
            color = MaterialTheme.colorScheme.primary
        )
    ) {
        // The list of vehicles.
        AnimatedVisibility(
            visible = !showDetails
        ) {
            VehiclesView(
                viewModel = viewModel,
                onClickForDetails = { id ->
                    Log.d(Tag.MAIN, "Requested ID is: $id")
                    showDetails = true
                    viewModel.getVehicleRecord(
                        id = id
                    )
                },
            )
        }

        // If showing details, slide them in front of the vehicles list.
        AnimatedVisibility(
            visible = showDetails,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> fullHeight }
            ),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> fullHeight }
            )
        ) {
            VehicleDetailView(
                modifier = modifier
                    .padding(20.dp)
                    .wrapContentHeight(),
                viewModel = viewModel,
                onBackButtonClicked = { showDetails = false }

            )
        }

    }
}
