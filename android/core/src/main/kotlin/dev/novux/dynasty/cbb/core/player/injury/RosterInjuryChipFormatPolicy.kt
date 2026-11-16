package dev.novux.dynasty.cbb.core.player.injury

/** MEGA queue batch 36 — [MEGA-COACH-179]. */
object RosterInjuryChipFormatPolicy {
    const val MEGA_ID = "MEGA-COACH-179"
    fun chip(label: String, weeks: Int): String = "OUT · $label · ${weeks}w"
}
