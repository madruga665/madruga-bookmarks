package com.madruga665.bookmarks.ui.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.madruga665.bookmarks.R
import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.local.CollectionEntity

object ShareUtils {

    fun shareBookmark(context: Context, bookmark: BookmarkEntity) {
        val displayTitle = BookmarkDisplayUtils.getDisplayTitle(bookmark.title, bookmark.url)
        val textToShare = "$displayTitle\n${bookmark.url}"

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, textToShare)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, context.getString(R.string.bookmark_share_chooser_title)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        try {
            context.startActivity(shareIntent)
        } catch (e: Exception) {
            Toast.makeText(context, R.string.bookmark_share_error, Toast.LENGTH_SHORT).show()
        }
    }

    fun shareBookmark(
        context: Context,
        title: String?,
        url: String
    ) {
        try {
            val normalizedUrl = UrlUtils.normalizeUrl(url)
            val shareText = if (!title.isNullOrBlank()) {
                "$title\n$normalizedUrl"
            } else {
                normalizedUrl
            }

            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, title?.takeIf { it.isNotBlank() } ?: context.getString(R.string.app_name))
                putExtra(Intent.EXTRA_TEXT, shareText)
            }

            val chooser = Intent.createChooser(
                sendIntent,
                context.getString(R.string.bookmark_share_chooser_title)
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                context.getString(R.string.bookmark_share_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun shareCollection(
        context: Context,
        collection: CollectionEntity,
        bookmarks: List<BookmarkEntity> = emptyList()
    ) {
        try {
            val body = StringBuilder()
            body.append("📁 ${collection.name}\n\n")

            if (bookmarks.isNotEmpty()) {
                body.append("Links (${bookmarks.size}):\n")
                bookmarks.forEachIndexed { index, bookmark ->
                    val title = bookmark.title?.takeIf { it.isNotBlank() } ?: bookmark.url
                    body.append("${index + 1}. $title\n${bookmark.url}\n\n")
                }
            } else {
                body.append("https://tuckii.app/c/${collection.id}\n\n")
            }

            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, body.toString().trim())
                putExtra(Intent.EXTRA_TITLE, "Share Collection: ${collection.name}")
                type = "text/plain"
            }

            val chooser = Intent.createChooser(sendIntent, "Share Collection via").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(chooser)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                context.getString(R.string.bookmark_share_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
