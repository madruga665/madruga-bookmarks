package com.madruga665.bookmarks.ui.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.ui.settings.UsageStatistics
import com.madruga665.bookmarks.ui.theme.LightAccentYellow
import com.madruga665.bookmarks.ui.theme.neobrutalistShadow

@Composable
fun UsageHeroCard(
    usageStatistics: UsageStatistics,
    modifier: Modifier = Modifier
) {
    val cardShape = RoundedCornerShape(16.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tag_settings_usage_hero_card")
            .neobrutalistShadow(
                shadowColor = Color.Black,
                borderColor = Color.Black,
                borderWidth = 2.5.dp,
                shadowOffset = 4.dp,
                shape = cardShape
            )
            .background(LightAccentYellow, cardShape)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top Header: App Icon Badge + Title (Madruga Bookmarks)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // App icon badge (white circle with black border)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, CircleShape)
                        .border(2.dp, Color.Black, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Bookmark,
                        contentDescription = "App Badge",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // App Title
                Text(
                    text = stringResource(R.string.app_hero_title),
                    style = androidx.compose.ui.text.TextStyle(
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Horizontal Divider
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.Black)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 3 Metric Boxes: Total Links, Links Today, Collections
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Metric 1: Total Links
                MetricBox(
                    title = stringResource(R.string.usage_total_links),
                    value = usageStatistics.totalBookmarks,
                    modifier = Modifier.weight(1f)
                )

                // Metric 2: Links Today
                MetricBox(
                    title = stringResource(R.string.usage_links_today),
                    value = usageStatistics.bookmarksToday,
                    modifier = Modifier.weight(1f)
                )

                // Metric 3: Collections
                MetricBox(
                    title = stringResource(R.string.usage_collections),
                    value = usageStatistics.totalCollections,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun MetricBox(
    title: String,
    value: Int,
    modifier: Modifier = Modifier
) {
    val boxShape = RoundedCornerShape(10.dp)

    Column(
        modifier = modifier
            .background(Color.White, boxShape)
            .border(2.dp, Color.Black, boxShape)
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black.copy(alpha = 0.75f)
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "$value",
            style = androidx.compose.ui.text.TextStyle(
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black
            )
        )
    }
}
