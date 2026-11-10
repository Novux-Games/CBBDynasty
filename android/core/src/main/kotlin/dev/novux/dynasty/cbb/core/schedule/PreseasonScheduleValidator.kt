package dev.novux.dynasty.cbb.core.schedule

import dev.novux.dynasty.cbb.core.preseason.tournament.RegularSeasonSlotBudget

data class PreseasonScheduleValidation(
    val isValid: Boolean,
    val message: String? = null,
)

object PreseasonScheduleValidator {
    fun validate(plan: PreseasonSchedulePlan, budget: RegularSeasonSlotBudget): PreseasonScheduleValidation {
        if (plan.exhibitions.size > PreseasonScheduleConstants.MAX_EXHIBITION_GAMES) {
            return PreseasonScheduleValidation(false, "Too many exhibition games")
        }
        if (plan.totalRsGames > budget.totalRsCap) {
            return PreseasonScheduleValidation(false, "Over regular-season cap")
        }
        val builderUsed = plan.conferenceGameCount + plan.nonConferenceGameCount
        if (builderUsed > budget.builderSlots) {
            return PreseasonScheduleValidation(false, "Over builder slot budget")
        }
        if (plan.earlyEventGameCount + builderUsed > budget.totalRsCap) {
            return PreseasonScheduleValidation(false, "Event plus builder exceeds cap")
        }
        return PreseasonScheduleValidation(true)
    }
}
