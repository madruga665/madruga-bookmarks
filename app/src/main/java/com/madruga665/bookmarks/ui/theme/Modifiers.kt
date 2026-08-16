package com.madruga665.bookmarks.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
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
