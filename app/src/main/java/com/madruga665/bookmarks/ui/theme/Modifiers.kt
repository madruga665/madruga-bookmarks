package com.madruga665.bookmarks.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Custom Neobrutalism modifier applying a crisp 2.5dp border and a zero-blur, hard offset shadow.
 */
fun Modifier.neobrutalistShadow(
    shadowColor: Color = Color.Black,
    borderColor: Color = Color.Black,
    borderWidth: Dp = 2.5.dp,
    shadowOffset: Dp = 4.dp,
    shape: Shape = RoundedCornerShape(12.dp)
): Modifier = this
    .graphicsLayer { shadowElevation = 0f }
    .drawBehind {
        val shadowPx = shadowOffset.toPx()
        val outline = shape.createOutline(size, layoutDirection, this)
        drawContext.canvas.save()
        drawContext.canvas.translate(shadowPx, shadowPx)
        drawOutline(outline = outline, color = shadowColor)
        drawContext.canvas.restore()
    }
    .border(borderWidth, borderColor, shape)

/**
 * Custom Neobrutalism modifier applying a subtle repeating square grid pattern.
 *
 * @param gridColor The color of the grid lines (typically [NeobrutalismColors.gridLine]).
 * @param gridSize The spacing between grid lines in density-independent pixels. Default is 24.dp.
 * @param strokeWidth The width of the grid line strokes. Default is 1.dp.
 */
fun Modifier.neobrutalistGridBackground(
    gridColor: Color,
    gridSize: Dp = 24.dp,
    strokeWidth: Dp = 1.dp
): Modifier = this.drawBehind {
    val stepPx = gridSize.toPx()
    val strokePx = strokeWidth.toPx()
    if (stepPx <= 0f) return@drawBehind

    val width = size.width
    val height = size.height

    var x = 0f
    while (x <= width) {
        drawLine(
            color = gridColor,
            start = Offset(x, 0f),
            end = Offset(x, height),
            strokeWidth = strokePx
        )
        x += stepPx
    }

    var y = 0f
    while (y <= height) {
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(width, y),
            strokeWidth = strokePx
        )
        y += stepPx
    }
}
