package com.madruga665.bookmarks.ui.settings.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow

@Composable
fun HapticPreferenceCard(
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    testTag: String? = null
) {
    val shape = RoundedCornerShape(12.dp)
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(if (testTag != null) Modifier.testTag(testTag) else Modifier)
            .neobrutalistShadow(
                shadowColor = NeobrutalismTheme.colors.shadow,
                borderColor = NeobrutalismTheme.colors.border,
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp,
                shape = shape
            )
            .background(NeobrutalismTheme.colors.surface, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onToggle(!isEnabled) }
            )
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(NeobrutalismTheme.colors.accentYellow, RoundedCornerShape(10.dp))
                        .border(2.dp, NeobrutalismTheme.colors.border, RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Vibration,
                        contentDescription = stringResource(R.string.pref_haptic_title),
                        tint = NeobrutalismTheme.colors.border,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = stringResource(R.string.pref_haptic_title),
                        style = NeobrutalismTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        ),
                        color = NeobrutalismTheme.colors.onSurface
                    )
                    Text(
                        text = stringResource(R.string.pref_haptic_desc),
                        style = NeobrutalismTheme.typography.bodySmall.copy(
                            fontSize = 12.sp
                        ),
                        color = NeobrutalismTheme.colors.subtext
                    )
                }
            }

            // Neobrutalist Switch
            NeobrutalistSwitch(
                checked = isEnabled,
                onCheckedChange = onToggle
            )
        }
    }
}

@Composable
fun NeobrutalistSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val trackWidth = 48.dp
    val trackHeight = 26.dp
    val thumbSize = 18.dp
    val thumbPadding = 3.dp

    val thumbOffset by animateDpAsState(
        targetValue = if (checked) (trackWidth - thumbSize - thumbPadding - 2.dp) else thumbPadding,
        label = "thumbOffset"
    )

    val trackColor = if (checked) NeobrutalismTheme.colors.accentYellow else NeobrutalismTheme.colors.background
    val thumbColor = if (checked) NeobrutalismTheme.colors.border else NeobrutalismTheme.colors.onSurface

    Box(
        modifier = modifier
            .size(width = trackWidth, height = trackHeight)
            .border(2.dp, NeobrutalismTheme.colors.border, RoundedCornerShape(13.dp))
            .background(trackColor, RoundedCornerShape(13.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { onCheckedChange(!checked) }
            )
            .padding(vertical = thumbPadding),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbSize)
                .background(thumbColor, CircleShape)
                .border(1.5.dp, NeobrutalismTheme.colors.border, CircleShape)
        )
    }
}
