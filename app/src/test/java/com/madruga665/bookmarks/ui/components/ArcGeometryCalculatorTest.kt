package com.madruga665.bookmarks.ui.components

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

class ArcGeometryCalculatorTest {

    private val screenWidth = 1080f
    private val screenHeight = 2400f
    private val radius = 120f
    // margin = radius + 60f = 180f

    // ---------------------------------------------------------------------------------------------
    // calculateSector tests
    // ---------------------------------------------------------------------------------------------

    @Test
    fun calculateSector_nearRightAndBottom_returnsTopLeftQuadrant() {
        val anchor = Offset(screenWidth - 50f, screenHeight - 50f)
        val (startAngle, sweepAngle) = ArcGeometryCalculator.calculateSector(anchor, screenWidth, screenHeight, radius)
        assertEquals(180f, startAngle, 0.01f)
        assertEquals(90f, sweepAngle, 0.01f)
    }

    @Test
    fun calculateSector_nearRightAndTop_returnsBottomLeftQuadrant() {
        val anchor = Offset(screenWidth - 50f, 50f)
        val (startAngle, sweepAngle) = ArcGeometryCalculator.calculateSector(anchor, screenWidth, screenHeight, radius)
        assertEquals(90f, startAngle, 0.01f)
        assertEquals(90f, sweepAngle, 0.01f)
    }

    @Test
    fun calculateSector_nearRightEdge_returnsPointingLeft() {
        val anchor = Offset(screenWidth - 50f, screenHeight / 2f)
        val (startAngle, sweepAngle) = ArcGeometryCalculator.calculateSector(anchor, screenWidth, screenHeight, radius)
        assertEquals(120f, startAngle, 0.01f)
        assertEquals(120f, sweepAngle, 0.01f)
    }

    @Test
    fun calculateSector_nearLeftAndBottom_returnsTopRightQuadrant() {
        val anchor = Offset(50f, screenHeight - 50f)
        val (startAngle, sweepAngle) = ArcGeometryCalculator.calculateSector(anchor, screenWidth, screenHeight, radius)
        assertEquals(270f, startAngle, 0.01f)
        assertEquals(90f, sweepAngle, 0.01f)
    }

    @Test
    fun calculateSector_nearLeftAndTop_returnsBottomRightQuadrant() {
        val anchor = Offset(50f, 50f)
        val (startAngle, sweepAngle) = ArcGeometryCalculator.calculateSector(anchor, screenWidth, screenHeight, radius)
        assertEquals(0f, startAngle, 0.01f)
        assertEquals(90f, sweepAngle, 0.01f)
    }

    @Test
    fun calculateSector_nearLeftEdge_returnsPointingRight() {
        val anchor = Offset(50f, screenHeight / 2f)
        val (startAngle, sweepAngle) = ArcGeometryCalculator.calculateSector(anchor, screenWidth, screenHeight, radius)
        assertEquals(-60f, startAngle, 0.01f)
        assertEquals(120f, sweepAngle, 0.01f)
    }

    @Test
    fun calculateSector_nearBottomEdge_returnsPointingUpwards() {
        val anchor = Offset(screenWidth / 2f, screenHeight - 50f)
        val (startAngle, sweepAngle) = ArcGeometryCalculator.calculateSector(anchor, screenWidth, screenHeight, radius)
        assertEquals(200f, startAngle, 0.01f)
        assertEquals(140f, sweepAngle, 0.01f)
    }

    @Test
    fun calculateSector_nearTopEdge_returnsPointingDownwards() {
        val anchor = Offset(screenWidth / 2f, 50f)
        val (startAngle, sweepAngle) = ArcGeometryCalculator.calculateSector(anchor, screenWidth, screenHeight, radius)
        assertEquals(20f, startAngle, 0.01f)
        assertEquals(140f, sweepAngle, 0.01f)
    }

    @Test
    fun calculateSector_centerRight_returnsPointingLeft() {
        // screenWidth = 1080, x = 700 is > 540, and (1080 - 700) = 380 > 180 (not near right edge)
        val anchor = Offset(700f, screenHeight / 2f)
        val (startAngle, sweepAngle) = ArcGeometryCalculator.calculateSector(anchor, screenWidth, screenHeight, radius)
        assertEquals(120f, startAngle, 0.01f)
        assertEquals(120f, sweepAngle, 0.01f)
    }

    @Test
    fun calculateSector_centerLeft_returnsPointingRight() {
        // screenWidth = 1080, x = 300 is <= 540, and 300 > 180 (not near left edge)
        val anchor = Offset(300f, screenHeight / 2f)
        val (startAngle, sweepAngle) = ArcGeometryCalculator.calculateSector(anchor, screenWidth, screenHeight, radius)
        assertEquals(-60f, startAngle, 0.01f)
        assertEquals(120f, sweepAngle, 0.01f)
    }

    // ---------------------------------------------------------------------------------------------
    // calculateItemPositions tests
    // ---------------------------------------------------------------------------------------------

    @Test
    fun calculateItemPositions_emptyItemCount_returnsEmptyList() {
        val positions = ArcGeometryCalculator.calculateItemPositions(
            anchor = Offset(500f, 500f),
            itemCount = 0,
            radius = 100f,
            startAngle = 0f,
            sweepAngle = 90f
        )
        assertTrue(positions.isEmpty())
    }

    @Test
    fun calculateItemPositions_singleItem_placesAtMidAngle() {
        val positions = ArcGeometryCalculator.calculateItemPositions(
            anchor = Offset(500f, 500f),
            itemCount = 1,
            radius = 100f,
            startAngle = 0f,
            sweepAngle = 90f
        )
        assertEquals(1, positions.size)
        val item = positions[0]
        assertEquals(45f, item.angleDegrees, 0.01f)
        val expectedDx = (100f * kotlin.math.cos(Math.toRadians(45.0))).toFloat()
        val expectedDy = (100f * kotlin.math.sin(Math.toRadians(45.0))).toFloat()
        assertEquals(expectedDx, item.offset.x, 0.01f)
        assertEquals(expectedDy, item.offset.y, 0.01f)
        assertFalse(item.badgeOnLeft)
    }

    @Test
    fun calculateItemPositions_threeItems_distributesCorrectly() {
        val positions = ArcGeometryCalculator.calculateItemPositions(
            anchor = Offset(500f, 500f),
            itemCount = 3,
            radius = 100f,
            startAngle = 0f,
            sweepAngle = 180f
        )
        assertEquals(3, positions.size)

        // Item 0: 0 degrees -> dx = 100, dy = 0 (points right -> badge on outside/right)
        assertEquals(0f, positions[0].angleDegrees, 0.01f)
        assertEquals(100f, positions[0].offset.x, 0.01f)
        assertEquals(0f, positions[0].offset.y, 0.01f)
        assertFalse(positions[0].badgeOnLeft)

        // Item 1: 90 degrees -> dx = 0, dy = 100
        assertEquals(90f, positions[1].angleDegrees, 0.01f)
        assertTrue(abs(positions[1].offset.x) < 0.01f)
        assertEquals(100f, positions[1].offset.y, 0.01f)
        assertFalse(positions[1].badgeOnLeft)

        // Item 2: 180 degrees -> dx = -100, dy = 0 (points left -> badge on outside/left)
        assertEquals(180f, positions[2].angleDegrees, 0.01f)
        assertEquals(-100f, positions[2].offset.x, 0.01f)
        assertTrue(abs(positions[2].offset.y) < 0.01f)
        assertTrue(positions[2].badgeOnLeft)
    }

    @Test
    fun calculateItemPositions_fourItems_distributesCorrectly() {
        val positions = ArcGeometryCalculator.calculateItemPositions(
            anchor = Offset(500f, 500f),
            itemCount = 4,
            radius = 120f,
            startAngle = 120f,
            sweepAngle = 120f
        )
        assertEquals(4, positions.size)

        // Step = 120 / 3 = 40 deg
        assertEquals(120f, positions[0].angleDegrees, 0.01f)
        assertEquals(160f, positions[1].angleDegrees, 0.01f)
        assertEquals(200f, positions[2].angleDegrees, 0.01f)
        assertEquals(240f, positions[3].angleDegrees, 0.01f)

        // All angles 120 to 240 have cos(angle) < 0 -> dx < 0 -> badgeOnLeft = true (outside)
        positions.forEach { pos ->
            assertTrue(pos.offset.x < 0f)
            assertTrue(pos.badgeOnLeft)
        }
    }

    // ---------------------------------------------------------------------------------------------
    // findHoveredItemIndex tests
    // ---------------------------------------------------------------------------------------------

    @Test
    fun findHoveredItemIndex_touchDirectlyOnItem_returnsMatchingIndex() {
        val anchor = Offset(500f, 500f)
        val positions = ArcGeometryCalculator.calculateItemPositions(
            anchor = anchor,
            itemCount = 3,
            radius = 100f,
            startAngle = 0f,
            sweepAngle = 180f
        )

        // Item 0 is at (500 + 100, 500 + 0) = (600, 500)
        val touch0 = Offset(600f, 500f)
        assertEquals(0, ArcGeometryCalculator.findHoveredItemIndex(touch0, anchor, positions))

        // Item 1 is at (500 + 0, 500 + 100) = (500, 600)
        val touch1 = Offset(500f, 600f)
        assertEquals(1, ArcGeometryCalculator.findHoveredItemIndex(touch1, anchor, positions))

        // Item 2 is at (500 - 100, 500 + 0) = (400, 500)
        val touch2 = Offset(400f, 500f)
        assertEquals(2, ArcGeometryCalculator.findHoveredItemIndex(touch2, anchor, positions))
    }

    @Test
    fun findHoveredItemIndex_touchWithinPadding_returnsItemIndex() {
        val anchor = Offset(500f, 500f)
        val positions = ArcGeometryCalculator.calculateItemPositions(
            anchor = anchor,
            itemCount = 3,
            radius = 100f,
            startAngle = 0f,
            sweepAngle = 180f
        )

        // Item 0 is at (600, 500). Button radius = 30, hitPadding = 40 -> total hit zone radius = 70.
        // Touch at (650, 500) is 50px away -> within 70px threshold
        val touchNear0 = Offset(650f, 500f)
        assertEquals(0, ArcGeometryCalculator.findHoveredItemIndex(touchNear0, anchor, positions))
    }

    @Test
    fun findHoveredItemIndex_touchOutsideAllHitZones_returnsNull() {
        val anchor = Offset(500f, 500f)
        val positions = ArcGeometryCalculator.calculateItemPositions(
            anchor = anchor,
            itemCount = 3,
            radius = 100f,
            startAngle = 0f,
            sweepAngle = 180f
        )

        // Touch at anchor (500, 500) is 100px away from all items (threshold = 70px)
        val touchAnchor = Offset(500f, 500f)
        assertNull(ArcGeometryCalculator.findHoveredItemIndex(touchAnchor, anchor, positions))

        // Touch far away
        val touchFar = Offset(0f, 0f)
        assertNull(ArcGeometryCalculator.findHoveredItemIndex(touchFar, anchor, positions))
    }

    @Test
    fun findHoveredItemIndex_picksClosestWhenInOverlappingHitZone() {
        val anchor = Offset(500f, 500f)
        val positions = listOf(
            ArcItemPosition(offset = Offset(50f, 0f), angleDegrees = 0f, badgeOnLeft = true),
            ArcItemPosition(offset = Offset(90f, 0f), angleDegrees = 0f, badgeOnLeft = true)
        )

        // Item 0 is at (550, 500), Item 1 is at (590, 500)
        // Touch at (565, 500) is distance 15 from Item 0 and distance 25 from Item 1.
        // Both are <= 70, but Item 0 is closer.
        val touch = Offset(565f, 500f)
        assertEquals(0, ArcGeometryCalculator.findHoveredItemIndex(touch, anchor, positions))
    }
}
