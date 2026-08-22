package com.madruga665.bookmarks.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// ==========================================
// Light Theme Palette (Reference Screenshot)
// ==========================================
val LightBackground = Color(0xFFF8F8F8)
val LightSurface = Color(0xFFFFFFFF)
val LightOnSurface = Color(0xFF000000)
val LightSubtext = Color(0xFF555555)
val LightBorder = Color(0xFF000000)
val LightShadow = Color(0xFF000000)
val LightGridLine = Color(0xFFE2E2E2)

val LightAccentYellow = Color(0xFFFFD600) // #FFD600 / #FFC107
val LightAccentPurple = Color(0xFF7C5CFF) // #7C5CFF
val LightAccentOrange = Color(0xFFFF6B00) // #FF6B00
val LightAccentBlue = Color(0xFF3399FF)

// ==========================================
// Catppuccin Mocha Dark Theme Palette
// ==========================================
val MochaBase = Color(0xFF1E1E2E)      // Main background
val MochaSurface0 = Color(0xFF313244)  // Cards / Container surface
val MochaText = Color(0xFFCDD6F4)      // Primary text
val MochaSubtext0 = Color(0xFFA6ADC8)  // Secondary text
val MochaOverlay0 = Color(0xFF6C7086)
val MochaCrust = Color(0xFF11111B)     // Dark stroke / shadow outline
val MochaGridLine = Color(0xFF28283D)

val MochaYellow = Color(0xFFF9E2AF)    // Catppuccin Yellow accent
val MochaMauve = Color(0xFFCBA6F7)     // Catppuccin Mauve/Purple accent
val MochaPeach = Color(0xFFFAB387)     // Catppuccin Peach/Orange accent
val MochaBlue = Color(0xFF89B4FA)      // Catppuccin Blue accent
val MochaGreen = Color(0xFFA6E3A1)     // Catppuccin Green accent

@Immutable
data class NeobrutalismColors(
    val background: Color,
    val surface: Color,
    val onSurface: Color,
    val subtext: Color,
    val border: Color,
    val shadow: Color,
    val accentYellow: Color,
    val accentPurple: Color,
    val accentOrange: Color,
    val accentBlue: Color,
    val gridLine: Color
)

val LightNeobrutalismColors = NeobrutalismColors(
    background = LightBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    subtext = LightSubtext,
    border = LightBorder,
    shadow = LightShadow,
    accentYellow = LightAccentYellow,
    accentPurple = LightAccentPurple,
    accentOrange = LightAccentOrange,
    accentBlue = LightAccentBlue,
    gridLine = LightGridLine
)

val MochaDarkNeobrutalismColors = NeobrutalismColors(
    background = MochaBase,
    surface = MochaSurface0,
    onSurface = MochaText,
    subtext = MochaSubtext0,
    border = MochaCrust,
    shadow = MochaCrust,
    accentYellow = MochaYellow,
    accentPurple = MochaMauve,
    accentOrange = MochaPeach,
    accentBlue = MochaBlue,
    gridLine = MochaGridLine
)
