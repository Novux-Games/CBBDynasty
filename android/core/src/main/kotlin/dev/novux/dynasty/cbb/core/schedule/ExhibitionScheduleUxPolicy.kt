package dev.novux.dynasty.cbb.core.schedule

/** Preseason task 7 — exhibition builder ([planning/preseason.md#7--schedule-exhibition-games], [MEGA-FRAME-075]). */
object ExhibitionScheduleUxPolicy {
    const val MEGA_FRAME_ID: String = "MEGA-FRAME-075"

    val exhibitionWeekIndices: IntRange = 1..PreseasonScheduleConstants.MAX_EXHIBITION_GAMES

    fun validateDraft(count: Int, drafts: List<ExhibitionGameDraft>): Boolean {
        if (count == 0) return true
        if (drafts.size != count) return false
        if (drafts.any { it.opponentTeamId.isBlank() }) return false
        val weeks = drafts.map { it.exhibitionWeekIndex }
        return weeks.all { it in exhibitionWeekIndices } && weeks.distinct().size == weeks.size
    }

    fun draftsToStubs(drafts: List<ExhibitionGameDraft>): List<ScheduledGameStub> =
        drafts
            .sortedBy { it.exhibitionWeekIndex }
            .map {
                ScheduledGameStub(
                    kind = ScheduleGameKind.Exhibition,
                    opponentTeamId = it.opponentTeamId,
                    opponentDisplayName = it.opponentDisplayName,
                    homeGame = it.homeGame,
                    rsWeekIndex = it.exhibitionWeekIndex,
                )
            }
}

data class ExhibitionGameDraft(
    val opponentTeamId: String = "",
    val opponentDisplayName: String = "",
    val homeGame: Boolean = true,
    val exhibitionWeekIndex: Int = 1,
)
