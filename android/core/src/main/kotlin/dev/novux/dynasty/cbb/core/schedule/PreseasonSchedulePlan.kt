package dev.novux.dynasty.cbb.core.schedule

import dev.novux.dynasty.cbb.core.preseason.tournament.RegularSeasonSlotBudget

data class PreseasonSchedulePlan(
    val exhibitions: List<ScheduledGameStub>,
    val regularSeason: List<ScheduledGameStub>,
    val conferenceGameCount: Int,
    val nonConferenceGameCount: Int,
    val earlyEventGameCount: Int,
) {
    val totalRsGames: Int = regularSeason.count {
        it.kind == ScheduleGameKind.Conference ||
            it.kind == ScheduleGameKind.NonConference ||
            it.kind == ScheduleGameKind.EarlyEvent
    }

    fun isValidFor(budget: RegularSeasonSlotBudget): Boolean =
        PreseasonScheduleValidator.validate(this, budget).isValid
}
