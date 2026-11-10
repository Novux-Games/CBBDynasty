package dev.novux.dynasty.cbb.core.preseason.tournament

/** RS cap math ([planning/preseason.md#8--schedule-regular-season-games]). */
data class RegularSeasonSlotBudget(
    val totalRsCap: Int = RS_CAP,
    val eventRsGames: Int,
    val builderSlots: Int,
) {
    companion object {
        const val RS_CAP: Int = 32

        fun fromResolution(resolution: TournamentInviteResolution): RegularSeasonSlotBudget {
            val eventGames = when (resolution) {
                is TournamentInviteResolution.Accepted -> resolution.event.rsGameSpots
                else -> 0
            }
            return RegularSeasonSlotBudget(
                eventRsGames = eventGames,
                builderSlots = (RS_CAP - eventGames).coerceAtLeast(0),
            )
        }
    }
}
