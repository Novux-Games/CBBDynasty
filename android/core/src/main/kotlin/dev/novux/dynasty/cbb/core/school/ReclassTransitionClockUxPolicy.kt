package dev.novux.dynasty.cbb.core.school

/** Reclass programs show Y1–Y4 transition clock on school hub ([MEGA-GDD-145], [MEGA-WORLD-092]). */
object ReclassTransitionClockUxPolicy {
    const val MEGA_ID: String = "MEGA-GDD-145"
    const val WORLD_ID: String = "MEGA-WORLD-092"

    fun transitionLabel(transitionStartYear: Int?, currentSeasonIndex: Int): String? {
        val start = transitionStartYear ?: return null
        val yearInProgram = (currentSeasonIndex - start + 1).coerceIn(1, 4)
        return "Provisional Y$yearInProgram of 4"
    }

    fun showOnHub(transitionStartYear: Int?): Boolean = transitionStartYear != null
}