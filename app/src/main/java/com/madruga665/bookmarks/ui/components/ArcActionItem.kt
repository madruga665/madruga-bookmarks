package com.madruga665.bookmarks.ui.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents an individual action item rendered along the arc menu.
 *
 * @param id Unique identifier or enum representing the action.
 * @param icon The ImageVector icon displayed in the action button.
 * @param label The textual label for accessibility or badge display.
 * @param contentDescription Accessibility description for screen readers.
 * @param activeColor Accent color used when this action is highlighted/hovered.
 * @param onClick Action callback executed when this item is selected/clicked.
 */
data class ArcActionItem<T>(
    val id: T,
    val icon: ImageVector,
    val label: String,
    val contentDescription: String,
    val activeColor: Color,
    val onClick: () -> Unit
)

/**
 * Calculated positioning data for an action item on the circular arc.
 *
 * @param offset Pixel coordinates (dx, dy) relative to the anchor position.
 * @param angleDegrees Absolute angle in degrees along the arc geometry.
 * @param badgeOnLeft Whether a label/badge should be placed to the left of the action button.
 */
data class ArcItemPosition(
    val offset: Offset,
    val angleDegrees: Float,
    val badgeOnLeft: Boolean
)

/**
 * Geometry configuration parameters for calculating arc layout and bounds adaptation.
 *
 * @param radiusPx Radial distance in pixels from the anchor center to action button centers.
 * @param startAngleDegrees Starting angle of the arc sector in degrees.
 * @param sweepAngleDegrees Angular span of the arc sector in degrees.
 * @param screenWidthPx Screen width in pixels for boundary collision detection.
 * @param screenHeightPx Screen height in pixels for boundary collision detection.
 * @param hitPaddingPx Radial padding added around action buttons for touch drag hit-testing.
 */
data class ArcGeometryConfig(
    val radiusPx: Float,
    val startAngleDegrees: Float,
    val sweepAngleDegrees: Float,
    val screenWidthPx: Float,
    val screenHeightPx: Float,
    val hitPaddingPx: Float = 40f
)
