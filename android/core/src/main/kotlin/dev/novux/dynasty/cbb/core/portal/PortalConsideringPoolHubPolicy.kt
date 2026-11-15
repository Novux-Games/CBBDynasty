package dev.novux.dynasty.cbb.core.portal

/**
 * Portal simulation rolls a 1k–1.4k considering pool; recruiting hub surfaces the count when the window
 * is open ([MEGA-RECR-062]).
 */
object PortalConsideringPoolHubPolicy {
    const val MEGA_ID: String = "MEGA-RECR-062"

    fun hubSummaryLine(consideringPoolSize: Int, windowOpen: Boolean): String? {
        if (!windowOpen || consideringPoolSize <= 0) return null
        return "Considering pool: $consideringPoolSize transfers (simulated)"
    }
}