package dev.novux.dynasty.cbb.core.coach.employment

/** Unemployed coaches spectate league games from shell calendar ([MEGA-GDD-122]). */
object UnemployedSpectateGamePolicy {
    const val MEGA_ID: String = "MEGA-GDD-122"

    fun calendarSpectateWhileUnemployed(): Boolean = true
}