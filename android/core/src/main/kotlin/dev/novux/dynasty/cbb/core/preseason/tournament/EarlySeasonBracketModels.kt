package dev.novux.dynasty.cbb.core.preseason.tournament

import kotlinx.serialization.Serializable

@Serializable
data class EarlySeasonBracketSnapshot(
    val eventTemplateId: String,
    val seasonIndex: Int,
    /** Seed 1 = index 0 ([planning/ncba-branding.md] 16-team events). */
    val seeds: List<String>,
    val eliminatedAtRsWeek: Map<String, Int> = emptyMap(),
    val completedRsWeeks: Set<Int> = emptySet(),
    val roundWinners: List<List<String>> = emptyList(),
) {
    val event: EarlySeasonEvent?
        get() = EarlySeasonEventCatalog.byId(eventTemplateId)

    fun isParticipant(teamId: String): Boolean = teamId in seeds

    fun eliminated(teamId: String): Boolean = teamId in eliminatedAtRsWeek
}

@Serializable
data class EarlySeasonBracketSeason(
    val seasonIndex: Int,
    val brackets: Map<String, EarlySeasonBracketSnapshot> = emptyMap(),
)

enum class EarlySeasonWeekSlot {
    /** Team is alive and has a bracket game this RS week. */
  HasGame,

    /** Eliminated earlier — bye on remaining event weeks ([planning/time-system.md]). */
  EliminationBye,

    /** Not in this event. */
  NotInEvent,
}

data class EarlySeasonBracketMatchup(
    val homeTeamId: String,
    val awayTeamId: String,
    val rsWeekIndex: Int,
    val eventTemplateId: String,
)
