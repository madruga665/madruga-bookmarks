package com.madruga665.bookmarks.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow

import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight

@Composable
fun NeobrutalistButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = NeobrutalismTheme.colors.surface,
    borderColor: Color = NeobrutalismTheme.colors.border,
    shadowColor: Color = NeobrutalismTheme.colors.shadow,
    shape: Shape = RoundedCornerShape(12.dp),
    borderWidth: Dp = 2.5.dp,
    shadowOffset: Dp = 4.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val currentShadowOffset by animateDpAsState(
        targetValue = if (isPressed) 1.dp else shadowOffset,
        label = "pressShadowOffset"
    )

    val currentContentOffset by animateDpAsState(
        targetValue = if (isPressed) (shadowOffset - 1.dp) else 0.dp,
        label = "pressContentOffset"
    )

    Box(
        modifier = modifier
            .offset(x = currentContentOffset, y = currentContentOffset)
            .neobrutalistShadow(
                shadowColor = shadowColor,
                borderColor = borderColor,
                borderWidth = borderWidth,
                shadowOffset = currentShadowOffset,
                shape = shape
            )
            .background(containerColor, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(10.dp),
        contentAlignment = Alignment.Center,
        content = content
    )
}

@Composable
fun NeobrutalistButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = NeobrutalismTheme.colors.surface,
    contentColor: Color = if (containerColor == NeobrutalismTheme.colors.accentYellow) NeobrutalismTheme.colors.border else NeobrutalismTheme.colors.onSurface,
    borderColor: Color = NeobrutalismTheme.colors.border,
    shadowColor: Color = NeobrutalismTheme.colors.shadow,
    shape: Shape = RoundedCornerShape(12.dp),
    borderWidth: Dp = 2.5.dp,
    shadowOffset: Dp = 4.dp
) {
    NeobrutalistButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = containerColor,
        borderColor = borderColor,
        shadowColor = shadowColor,
        shape = shape,
        borderWidth = borderWidth,
        shadowOffset = shadowOffset
    ) {
        Text(
            text = text,
            style = NeobrutalismTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = contentColor
        )
    }
}

