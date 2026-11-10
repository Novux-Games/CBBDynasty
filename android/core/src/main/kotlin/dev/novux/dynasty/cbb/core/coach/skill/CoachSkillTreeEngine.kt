package dev.novux.dynasty.cbb.core.coach.skill

import dev.novux.dynasty.cbb.core.coach.CoachArchetype

object CoachSkillTreeEngine {
    const val MAX_LEVEL: Int = 5
    const val POINTS_PER_LEVEL: Int = 1

    fun level(state: CoachSkillPointState, branch: CoachSkillBranch): Int =
        state.skillLevels[branch.wireKey]?.coerceIn(0, MAX_LEVEL) ?: 0

    fun canSpend(state: CoachSkillPointState, branch: CoachSkillBranch): Boolean =
        state.unspentBank >= POINTS_PER_LEVEL && level(state, branch) < MAX_LEVEL

    fun spend(state: CoachSkillPointState, branch: CoachSkillBranch): CoachSkillPointState {
        require(canSpend(state, branch)) { "cannot spend on ${branch.wireKey}" }
        val nextLevel = level(state, branch) + 1
        return state.copy(
            unspentBank = state.unspentBank - POINTS_PER_LEVEL,
            skillLevels = state.skillLevels + (branch.wireKey to nextLevel),
        )
    }

    fun ratingBonus(level: Int): Int = level * 2
}

object CoachAutoSpendEngine {
    fun prioritiesFor(archetype: CoachArchetype): List<CoachSkillBranch> = when (archetype) {
        CoachArchetype.Recruiter -> listOf(
            CoachSkillBranch.Recruiting,
            CoachSkillBranch.NilCharm,
            CoachSkillBranch.PortalRetention,
            CoachSkillBranch.PlayerDevelopment,
            CoachSkillBranch.Tactics,
        )
        CoachArchetype.SchemeManager -> listOf(
            CoachSkillBranch.Tactics,
            CoachSkillBranch.PlayerDevelopment,
            CoachSkillBranch.Recruiting,
            CoachSkillBranch.PortalRetention,
            CoachSkillBranch.NilCharm,
        )
        CoachArchetype.Developer -> listOf(
            CoachSkillBranch.PlayerDevelopment,
            CoachSkillBranch.Tactics,
            CoachSkillBranch.Recruiting,
            CoachSkillBranch.PortalRetention,
            CoachSkillBranch.NilCharm,
        )
        CoachArchetype.ProgramBuilder -> listOf(
            CoachSkillBranch.PortalRetention,
            CoachSkillBranch.Recruiting,
            CoachSkillBranch.NilCharm,
            CoachSkillBranch.PlayerDevelopment,
            CoachSkillBranch.Tactics,
        )
        CoachArchetype.Balanced -> CoachSkillBranch.entries.toList()
    }

    fun applyAll(state: CoachSkillPointState, archetype: CoachArchetype): CoachSkillPointState {
        var current = state
        val order = prioritiesFor(archetype)
        var progressed = true
        while (progressed) {
            progressed = false
            for (branch in order) {
                if (CoachSkillTreeEngine.canSpend(current, branch)) {
                    current = CoachSkillTreeEngine.spend(current, branch)
                    progressed = true
                    break
                }
            }
        }
        return current
    }
}
