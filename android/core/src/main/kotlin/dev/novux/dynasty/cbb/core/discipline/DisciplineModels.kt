package dev.novux.dynasty.cbb.core.discipline

import kotlinx.serialization.Serializable

enum class DisciplineIncidentKind {
    Conduct,
    Academic,
}

enum class DisciplineResolutionChoice {
    Dismiss,
    SuspendGames,
    ConferenceDraw,
    AcademicSweep,
    AcademicSuspendGames,
    AcademicHoldSupport,
}

enum class ConferenceDrawOutcome {
    Cleared,
    ShortSuspension,
    LongSuspension,
    Dismissed,
}

@Serializable
data class PendingDisciplineIncident(
    val playerId: String,
    val playerDisplayName: String,
    val kind: DisciplineIncidentKind,
    val headline: String,
    val flavorBody: String,
    val conductTemplateId: String? = null,
    val gpaCurrent: Double? = null,
    val recordedAtRsWeek: Int,
)

@Serializable
data class TeamDisciplineSeasonState(
    val seasonIndex: Int,
    val eventsResolved: Int = 0,
    val pending: PendingDisciplineIncident? = null,
)

data class DisciplineResolutionResult(
    val players: List<dev.novux.dynasty.cbb.core.roster.RosterPlayer>,
    val cutPlayerIds: List<String> = emptyList(),
    val conferenceOutcome: ConferenceDrawOutcome? = null,
    val gamesSuspended: Int? = null,
)
