package com.madruga665.bookmarks.ui.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.madruga665.bookmarks.R

object UrlUtils {

    /**
     * Normalizes a raw URL string by trimming whitespace and ensuring
     * an HTTP or HTTPS scheme is present.
     */
    fun normalizeUrl(rawUrl: String?): String {
        if (rawUrl.isNullOrBlank()) return ""
        val trimmed = rawUrl.trim()
        return if (!trimmed.startsWith("http://", ignoreCase = true) &&
            !trimmed.startsWith("https://", ignoreCase = true)
        ) {
            "https://$trimmed"
        } else {
            trimmed
        }
    }

    /**
     * Safely opens a URL in the default web browser application.
     * Guarantees scheme normalization, task flags for non-activity contexts, and exception handling.
     */
    fun openBrowserUrl(context: Context, url: String) {
        val normalizedUrl = normalizeUrl(url)
        if (normalizedUrl.isBlank()) {
            Toast.makeText(
                context,
                context.getString(R.string.bookmark_url_error_fmt, url),
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(normalizedUrl)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                context.getString(R.string.bookmark_url_error_fmt, url),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
