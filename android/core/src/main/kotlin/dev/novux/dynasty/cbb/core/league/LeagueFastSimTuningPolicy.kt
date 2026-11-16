package dev.novux.dynasty.cbb.core.league

/** App week-advance always supplies tuning for batch possession sim ([MEGA-WORLD-068]). */
object LeagueFastSimTuningPolicy {
    const val MEGA_ID = "MEGA-WORLD-068"

    fun usePossessionEngine(tuningPresent: Boolean): Boolean = tuningPresent
}