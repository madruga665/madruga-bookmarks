package com.madruga665.bookmarks.ui.components

import androidx.compose.ui.geometry.Offset
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Pure Kotlin mathematical object providing trigonometry for dynamic arc geometry,
 * boundary adaptation, and touch hit-testing for the arc actions menu.
 */
object ArcGeometryCalculator {

    private const val BOUNDARY_MARGIN_EXTRA = 60f

    /**
     * Computes the sector `(startAngleDegrees, sweepAngleDegrees)` based on the anchor
     * position relative to screen bounds to prevent arc items from going off-screen.
     *
     * @param anchor Anchor position in screen pixels (where long-press or touch occurred).
     * @param screenWidth Screen width in pixels.
     * @param screenHeight Screen height in pixels.
     * @param radius Arc radius in pixels.
     * @return A [Pair] containing `startAngleDegrees` to `sweepAngleDegrees`.
     */
    fun calculateSector(
        anchor: Offset,
        screenWidth: Float,
        screenHeight: Float,
        radius: Float
    ): Pair<Float, Float> {
        val margin = radius + BOUNDARY_MARGIN_EXTRA
        val isNearRight = (screenWidth - anchor.x) < margin
        val isNearLeft = anchor.x < margin
        val isNearTop = anchor.y < margin
        val isNearBottom = (screenHeight - anchor.y) < margin

        return when {
            isNearRight -> when {
                isNearBottom -> 180f to 90f // Top-left quadrant
                isNearTop -> 90f to 90f     // Bottom-left quadrant
                else -> 120f to 120f        // Points left
            }
            isNearLeft -> when {
                isNearBottom -> 270f to 90f // Top-right quadrant
                isNearTop -> 0f to 90f      // Bottom-right quadrant
                else -> -60f to 120f        // Points right
            }
            isNearBottom -> 200f to 140f    // Points upward
            isNearTop -> 20f to 140f        // Points downward
            anchor.x > screenWidth / 2f -> 120f to 120f // Center-right default: points left/inward
            else -> -60f to 120f                        // Center-left default: points right/inward
        }
    }

    /**
     * Calculates the layout positions for each arc item given the arc parameters.
     *
     * @param anchor Anchor position in screen coordinates.
     * @param itemCount Total number of items to distribute along the arc.
     * @param radius Arc radius in pixels.
     * @param startAngle Starting angle of the arc in degrees.
     * @param sweepAngle Angular span of the arc in degrees.
     * @return List of [ArcItemPosition] with relative offsets, angles, and badge orientations.
     */
    fun calculateItemPositions(
        anchor: Offset,
        itemCount: Int,
        radius: Float,
        startAngle: Float,
        sweepAngle: Float
    ): List<ArcItemPosition> {
        if (itemCount <= 0) return emptyList()

        if (itemCount == 1) {
            val angle = startAngle + sweepAngle / 2f
            val rad = Math.toRadians(angle.toDouble())
            val rawDx = (radius * cos(rad)).toFloat()
            val rawDy = (radius * sin(rad)).toFloat()
            val dx = if (kotlin.math.abs(rawDx) < 1e-4f) 0f else rawDx
            val dy = if (kotlin.math.abs(rawDy) < 1e-4f) 0f else rawDy
            val badgeOnLeft = dx < -0.001f
            return listOf(ArcItemPosition(offset = Offset(dx, dy), angleDegrees = angle, badgeOnLeft = badgeOnLeft))
        }

        val step = sweepAngle / (itemCount - 1)
        return List(itemCount) { index ->
            val angle = startAngle + index * step
            val rad = Math.toRadians(angle.toDouble())
            val rawDx = (radius * cos(rad)).toFloat()
            val rawDy = (radius * sin(rad)).toFloat()
            val dx = if (kotlin.math.abs(rawDx) < 1e-4f) 0f else rawDx
            val dy = if (kotlin.math.abs(rawDy) < 1e-4f) 0f else rawDy
            val badgeOnLeft = dx < -0.001f
            ArcItemPosition(
                offset = Offset(dx, dy),
                angleDegrees = angle,
                badgeOnLeft = badgeOnLeft
            )
        }
    }

    /**
     * Determines which action item (if any) is currently hovered based on the user's touch coordinate.
     *
     * @param touchPosition Current touch position in screen pixels.
     * @param anchor Anchor center in screen pixels.
     * @param itemPositions Relative positions of the action items.
     * @param buttonRadius Base button radius in pixels (default 30f).
     * @param hitPadding Extra touch padding for easy selection (default 40f).
     * @return The 0-based index of the hovered item, or null if touch is outside all hit zones.
     */
    fun findHoveredItemIndex(
        touchPosition: Offset,
        anchor: Offset,
        itemPositions: List<ArcItemPosition>,
        buttonRadius: Float = 30f,
        hitPadding: Float = 40f
    ): Int? {
        val maxDistance = buttonRadius + hitPadding
        var bestIndex: Int? = null
        var minDistance = Float.MAX_VALUE

        for (index in itemPositions.indices) {
            val item = itemPositions[index]
            val itemCenter = anchor + item.offset
            val distance = hypot(touchPosition.x - itemCenter.x, touchPosition.y - itemCenter.y)
            if (distance <= maxDistance && distance < minDistance) {
                minDistance = distance
                bestIndex = index
            }
        }
        return bestIndex
    }
}
