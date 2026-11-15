package dev.novux.dynasty.cbb.core.discipline

import dev.novux.dynasty.cbb.core.roster.RosterPlayer

/** Suspended players greyed like injuries on Strategy ([MEGA-SHELL-067]). */
object StrategySuspendedVisualPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-067"

    fun greyOutPlayer(player: RosterPlayer): Boolean =
        DisciplineResolver.isIneligibleForLineup(player)
}