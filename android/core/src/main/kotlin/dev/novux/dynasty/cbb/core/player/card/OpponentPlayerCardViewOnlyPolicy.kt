package dev.novux.dynasty.cbb.core.player.card

/** MEGA queue batch 36 — [MEGA-COACH-146]. */
object OpponentPlayerCardViewOnlyPolicy {
    const val MEGA_ID = "MEGA-COACH-146"
    fun allowEdit(isOnUserRoster: Boolean): Boolean = isOnUserRoster
}
