package com.madruga665.bookmarks.ui.utils

import android.content.Context
import android.content.Intent
import com.madruga665.bookmarks.data.local.BookmarkEntity
import com.madruga665.bookmarks.data.local.CollectionEntity

object ShareUtils {
    fun shareCollection(
        context: Context,
        collection: CollectionEntity,
        bookmarks: List<BookmarkEntity> = emptyList()
    ) {
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

        val shareIntent = Intent.createChooser(sendIntent, "Share Collection via")
        context.startActivity(shareIntent)
    }
}
