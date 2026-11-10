package dev.novux.dynasty.cbb.core.preview

object HeadToHeadHistory {
    fun summarize(meetings: List<HeadToHeadMeeting>): HeadToHeadRecord {
        var wins = 0
        var losses = 0
        meetings.forEach { m ->
            if (m.userScore > m.opponentScore) wins++ else if (m.userScore < m.opponentScore) losses++
        }
        return HeadToHeadRecord(
            userWins = wins,
            userLosses = losses,
            meetings = meetings.sortedByDescending { it.seasonLabel },
        )
    }
}
