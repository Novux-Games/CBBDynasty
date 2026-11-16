package dev.novux.dynasty.cbb.core.sim

/**
 * Optional plus-minus column from points scored during floor time ([MEGA-SIM-134]).
 * Simplified: share of team points while on floor vs opponent share in same window.
 */
object PlayerBoxPlusMinusPolicy {
    const val MEGA_ID = "MEGA-SIM-134"

    fun estimate(
        player: PlayerBoxScore,
        teamBox: TeamBoxScore,
        oppBox: TeamBoxScore,
    ): Int {
        if (player.minutesSeconds <= 0) return 0
        val teamTotal = (teamBox.points + oppBox.points).coerceAtLeast(1)
        val share = player.points.toDouble() / teamBox.points.coerceAtLeast(1)
        val oppShare = 1.0 - share
        val net = (teamBox.points * share - oppBox.points * oppShare).toInt()
        return (net * player.minutesSeconds / teamTotal).coerceIn(-30, 30)
    }
}