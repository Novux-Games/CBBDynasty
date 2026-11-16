package dev.novux.dynasty.cbb.core.player.scout

import dev.novux.dynasty.cbb.core.player.scout.ScoutDepth

/** MEGA queue batch 36 — [MEGA-COACH-145]. */
object PlayerScoutDepthFullCardPolicy {
    const val MEGA_ID = "MEGA-COACH-145"
    fun depthForFullCard(isUserRoster: Boolean) = if (isUserRoster) ScoutDepth.Full else ScoutDepth.Light
}
