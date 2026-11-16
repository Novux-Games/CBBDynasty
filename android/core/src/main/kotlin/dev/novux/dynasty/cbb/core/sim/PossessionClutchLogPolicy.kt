package dev.novux.dynasty.cbb.core.sim

/** Last-possession clarity tag when game is close late ([MEGA-SIM-116]). */
object PossessionClutchLogPolicy {
    const val MEGA_ID = "MEGA-SIM-116"

    private const val LAST_POSSESSION_SECONDS = 30
    private const val CLOSE_MARGIN = 5

    fun isLastPossessionContext(
        gameClockSeconds: Int,
        offenseScore: Int,
        defenseScore: Int,
    ): Boolean {
        if (gameClockSeconds > LAST_POSSESSION_SECONDS) return false
        return kotlin.math.abs(offenseScore - defenseScore) <= CLOSE_MARGIN
    }

    fun logTag(
        gameClockSeconds: Int,
        offenseScore: Int,
        defenseScore: Int,
    ): String? =
        if (isLastPossessionContext(gameClockSeconds, offenseScore, defenseScore)) {
            "CLUTCH"
        } else {
            null
        }
}