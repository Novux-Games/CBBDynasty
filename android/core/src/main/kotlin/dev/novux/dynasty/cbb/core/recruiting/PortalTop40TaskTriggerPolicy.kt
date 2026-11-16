package dev.novux.dynasty.cbb.core.recruiting

/** MEGA queue batch 36 — [MEGA-RECR-044]. */
object PortalTop40TaskTriggerPolicy {
    const val MEGA_ID = "MEGA-RECR-044"
    fun shouldEmit(week: Int, locked: Boolean): Boolean = week == 2 && locked
}
