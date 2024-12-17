package com.example.levysample.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FleetioColorScheme = lightColorScheme(
    primary = FleetioLightGray,
    onPrimary = Color.Black,
    secondary = FleetioDarkBlue,
    onSecondary = Color.White,
    tertiary = FleetioGreen,
    onTertiary = Color.White,
    error = Color.Red,
    onError = Color.White
)

@Composable
fun LevySampleTheme(
   content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FleetioColorScheme,
        typography = Typography,
        content = content
    )
}