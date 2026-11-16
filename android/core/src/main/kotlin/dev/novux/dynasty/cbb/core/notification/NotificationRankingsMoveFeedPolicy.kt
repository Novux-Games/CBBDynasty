package dev.novux.dynasty.cbb.core.notification

/** User-team NET jumps after week advance feed ([MEGA-SHELL-153]). */
object NotificationRankingsMoveFeedPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-153"

    const val MIN_ABS_RANK_DELTA: Int = 3

    fun feedLine(rankBefore: Int?, rankAfter: Int?): String? {
        if (rankBefore == null || rankAfter == null) return null
        if (rankBefore == rankAfter) return null
        val delta = kotlin.math.abs(rankBefore - rankAfter)
        if (delta < MIN_ABS_RANK_DELTA) return null
        return "Your program moved from NET #$rankBefore to #$rankAfter."
    }
}