package dev.novux.dynasty.cbb.core.sim

/**
 * Live possession log layout with jersey labels ([MEGA-SIM-010]).
 */
object PossessionLogFormatPolicy {
    const val MEGA_ID: String = "MEGA-SIM-010"

    fun formatLine(
        clockLabel: String,
        offenseLabel: String,
        usedSeconds: Int,
        shotClockBefore: Int,
        shotClockAfter: Int,
        scoreLine: String,
        playerAttribution: String?,
        foulNote: String? = null,
        lateGameTag: String? = null,
        subPhaseChain: String? = null,
    ): String {
        val foulSuffix = foulNote?.let { " | $it" }.orEmpty()
        val lateSuffix = lateGameTag?.let { " | $it" }.orEmpty()
        val phaseSuffix = subPhaseChain?.let { " | $it" }.orEmpty()
        val trip = "${usedSeconds}s trip | SC $shotClockBefore→$shotClockAfter"
        return if (playerAttribution != null) {
            "$clockLabel | $offenseLabel | $playerAttribution | $trip$foulSuffix$lateSuffix$phaseSuffix"
        } else {
            "$clockLabel | $offenseLabel | $trip | $scoreLine$foulSuffix$lateSuffix$phaseSuffix"
        }
    }
}