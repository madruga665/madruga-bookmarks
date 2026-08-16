package com.madruga665.bookmarks.ui.utils

import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

object HapticFeedbackHelper {

    fun performHaptic(
        hapticFeedback: HapticFeedback?,
        type: HapticFeedbackType = HapticFeedbackType.LongPress,
        isEnabled: Boolean = true
    ) {
        if (isEnabled && hapticFeedback != null) {
            try {
                hapticFeedback.performHapticFeedback(type)
            } catch (_: Exception) {
                // Ignore device vibration errors gracefully
            }
        }
    }

    fun performClickHaptic(
        hapticFeedback: HapticFeedback?,
        isEnabled: Boolean = true
    ) {
        performHaptic(
            hapticFeedback = hapticFeedback,
            type = HapticFeedbackType.TextHandleMove,
            isEnabled = isEnabled
        )
    }
}
