package com.example.tadeos.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = DarkTerracotta,
    secondary = DarkSage,
    tertiary = WarmCopper,
    background = Color(0xFF171210),
    surface = DarkSurface,
    surfaceVariant = Color(0xFF3B2B24),
    onPrimary = Color(0xFF3A1305),
    onSecondary = Color(0xFF172313),
    onBackground = Color(0xFFF4E9E1),
    onSurface = Color(0xFFF4E9E1),
    onSurfaceVariant = Color(0xFFE2CEC2)
)

private val LightColorScheme = lightColorScheme(
    primary = TerracottaClay,
    secondary = MutedSage,
    tertiary = WarmCopper,
    background = MorningCream,
    surface = MorningCream,
    surfaceVariant = WarmSurface,
    surfaceContainerHighest = NeutralSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = InkBrown,
    onSurface = InkBrown,
    onSurfaceVariant = MutedBrown
)

@Composable
fun TadeosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
