package com.madruga665.bookmarks.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

val LocalNeobrutalismColors = staticCompositionLocalOf { LightNeobrutalismColors }

object NeobrutalismTheme {
    val colors: NeobrutalismColors
        @Composable
        @ReadOnlyComposable
        get() = LocalNeobrutalismColors.current

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography
}

@Composable
fun NeobrutalismTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val neobrutalismColors = if (darkTheme) MochaDarkNeobrutalismColors else LightNeobrutalismColors

    val materialColors = if (darkTheme) {
        darkColorScheme(
            background = neobrutalismColors.background,
            surface = neobrutalismColors.surface,
            onSurface = neobrutalismColors.onSurface
        )
    } else {
        lightColorScheme(
            background = neobrutalismColors.background,
            surface = neobrutalismColors.surface,
            onSurface = neobrutalismColors.onSurface
        )
    }

    CompositionLocalProvider(
        LocalNeobrutalismColors provides neobrutalismColors
    ) {
        MaterialTheme(
            colorScheme = materialColors,
            typography = NeobrutalismTypography,
            content = content
        )
    }
}
