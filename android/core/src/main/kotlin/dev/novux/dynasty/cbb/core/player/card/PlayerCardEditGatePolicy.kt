package dev.novux.dynasty.cbb.core.player.card

/** Edit affordance only for players on the user roster ([MEGA-COACH-071], [planning/player-cards.md#edit-mode]). */
object PlayerCardEditGatePolicy {
    const val MEGA_ID: String = "MEGA-COACH-071"

    fun allowEditOnPreview(isOnUserRoster: Boolean): Boolean = isOnUserRoster

    fun allowEditOnFullCard(isOnUserRoster: Boolean): Boolean = isOnUserRoster
}