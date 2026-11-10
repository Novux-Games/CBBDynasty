package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.calendar.PostseasonCalendar

/** NCI / Heritage week indices aligned with NCBA on the league postseason calendar. */
object PostseasonParallelCalendar {
    const val NCI_TOURNAMENT_ID = "NCI"
    const val HERITAGE_TOURNAMENT_ID = "HC"

    val NCI_ROUNDS: Int = 5
    val HERITAGE_ROUNDS: Int = 4

    fun nciRoundWeek(roundIndex: Int): Int {
        require(roundIndex in 0 until NCI_ROUNDS)
        return PostseasonCalendar.OPENING_ROUND_WEEK + 1 + roundIndex
    }

    fun heritageRoundWeek(roundIndex: Int): Int {
        require(roundIndex in 0 until HERITAGE_ROUNDS)
        return PostseasonCalendar.OPENING_ROUND_WEEK + 1 + roundIndex
    }
}
