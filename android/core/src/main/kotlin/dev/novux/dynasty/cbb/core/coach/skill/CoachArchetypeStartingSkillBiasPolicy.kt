package dev.novux.dynasty.cbb.core.coach.skill

import dev.novux.dynasty.cbb.core.coach.CoachArchetype

/** Archetype → starting skill tree bias applied at wizard sign ([MEGA-COACH-026]). */
object CoachArchetypeStartingSkillBiasPolicy {
    const val MEGA_ID = "MEGA-COACH-026"
    const val STARTING_POINTS = 2

    fun seedAtSign(archetype: CoachArchetype, autoSpendFromSettings: Boolean = false): CoachSkillPointState {
        var withBank = CoachSkillPointState(
            unspentBank = STARTING_POINTS,
            autoSpendEnabled = autoSpendFromSettings,
        )
        val priority = CoachAutoSpendEngine.prioritiesFor(archetype).firstOrNull() ?: return withBank
        withBank = if (CoachSkillTreeEngine.canSpend(withBank, priority)) {
            CoachSkillTreeEngine.spend(withBank, priority)
        } else {
            withBank
        }
        return if (autoSpendFromSettings && withBank.unspentBank > 0) {
            CoachAutoSpendEngine.applyAll(withBank, archetype)
        } else {
            withBank
        }
    }
}