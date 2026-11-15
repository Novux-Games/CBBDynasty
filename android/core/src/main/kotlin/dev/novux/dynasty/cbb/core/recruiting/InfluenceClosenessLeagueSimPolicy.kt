package dev.novux.dynasty.cbb.core.recruiting

/**
 * League week advance / AI spend nudges influence closeness when user leads or trails
 * ([MEGA-RECR-088]).
 */
object InfluenceClosenessLeagueSimPolicy {
    const val MEGA_ID: String = "MEGA-RECR-088"

    fun adjustAfterLeagueWeek(board: ProspectInfluenceBoard, leagueWeeksElapsed: Int): ProspectInfluenceBoard {
        if (leagueWeeksElapsed <= 0) return board
        val perWeek = when (board.userRank) {
            1 -> 2
            2, 3 -> 0
            else -> -2
        }
        val delta = (perWeek * leagueWeeksElapsed).coerceIn(-12, 12)
        return board.copy(closeness = (board.closeness + delta).coerceIn(0, 100))
    }
}