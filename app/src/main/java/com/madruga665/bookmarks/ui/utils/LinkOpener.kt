package com.madruga665.bookmarks.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.madruga665.bookmarks.R

object LinkOpener {
    fun openUrl(context: Context, url: String?) {
        val trimmed = url?.trim()
        if (trimmed.isNullOrBlank()) {
            Toast.makeText(context, R.string.bookmark_toast_invalid_url, Toast.LENGTH_SHORT).show()
            return
        }

        val formattedUrl = if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            "https://$trimmed"
        } else {
            trimmed
        }

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(formattedUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, R.string.bookmark_url_error, Toast.LENGTH_SHORT).show()
        }
    }
}
