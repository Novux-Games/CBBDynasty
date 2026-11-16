package dev.novux.dynasty.cbb.core.coach.skill

import dev.novux.dynasty.cbb.core.coach.CoachArchetype

/** Archetype → starting skill tree bias applied at wizard sign ([MEGA-COACH-026]). */
object CoachArchetypeStartingSkillBiasPolicy {
    const val MEGA_ID = "MEGA-COACH-026"
    const val STARTING_POINTS = 2

    fun seedAtSign(archetype: CoachArchetype): CoachSkillPointState {
        val withBank = CoachSkillPointState(unspentBank = STARTING_POINTS)
        val priority = CoachAutoSpendEngine.prioritiesFor(archetype).firstOrNull() ?: return withBank
        return if (CoachSkillTreeEngine.canSpend(withBank, priority)) {
            CoachSkillTreeEngine.spend(withBank, priority)
        } else {
            withBank
        }
    }
}