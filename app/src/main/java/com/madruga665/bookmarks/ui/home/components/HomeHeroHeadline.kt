package com.madruga665.bookmarks.ui.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.ui.theme.NeobrutalismTheme

@Composable
fun HomeHeroHeadline(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.home_hero_headline),
            style = NeobrutalismTheme.typography.displayLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 32.sp,
                lineHeight = 38.sp
            ),
            color = NeobrutalismTheme.colors.onSurface
        )
    }
}
