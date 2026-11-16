package dev.novux.dynasty.cbb.core.coach

/** Jobless coaches may spectate league games without a program ([MEGA-GDD-122]). */
object UnemployedSpectateGamePolicy {
    const val MEGA_ID: String = "MEGA-GDD-122"

    fun spectateAllowed(employed: Boolean): Boolean = !employed
}