package dev.novux.dynasty.cbb.core.coach.skill

import dev.novux.dynasty.cbb.core.calendar.CalendarPosition
import dev.novux.dynasty.cbb.core.coach.CoachArchetype
import kotlinx.serialization.Serializable

@Serializable
data class CoachSkillPointState(
    val unspentBank: Int = 0,
    val lastGrantSeasonIndex: Int = 0,
    val lastGrantPhaseOrdinal: Int = -1,
    val lastGrantWeekIndex: Int = 0,
    val skillLevels: Map<String, Int> = emptyMap(),
    val autoSpendEnabled: Boolean = false,
) {
    fun alreadyGrantedFor(position: CalendarPosition): Boolean =
        lastGrantSeasonIndex == position.seasonIndex &&
            lastGrantPhaseOrdinal == position.phase.ordinal &&
            lastGrantWeekIndex == position.weekIndex

    fun withSundayGrant(position: CalendarPosition, points: Int): CoachSkillPointState =
        copy(
            unspentBank = unspentBank + points,
            lastGrantSeasonIndex = position.seasonIndex,
            lastGrantPhaseOrdinal = position.phase.ordinal,
            lastGrantWeekIndex = position.weekIndex,
        )
}

object CoachSkillPointStateReducer {
    fun ensureSundayGrant(
        current: CoachSkillPointState,
        position: CalendarPosition,
        archetype: CoachArchetype = CoachArchetype.Balanced,
    ): CoachSkillPointState {
        if (!CoachSundaySkillPointPolicy.grantsOnWeekEntry(position.phase)) return current
        if (current.alreadyGrantedFor(position)) return current
        var next = current.withSundayGrant(position, CoachSundaySkillPointPolicy.POINTS_PER_SUNDAY)
        if (next.autoSpendEnabled) {
            next = CoachAutoSpendEngine.applyAll(next, archetype)
        }
        return next
    }
}
