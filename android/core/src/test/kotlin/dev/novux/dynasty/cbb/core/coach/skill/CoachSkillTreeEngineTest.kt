package dev.novux.dynasty.cbb.core.coach.skill

import dev.novux.dynasty.cbb.core.coach.CoachArchetype
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CoachSkillTreeEngineTest {
    @Test
    fun spend_reducesBankAndIncrementsLevel() {
        val state = CoachSkillPointState(unspentBank = 2)
        val next = CoachSkillTreeEngine.spend(state, CoachSkillBranch.Recruiting)
        assertEquals(1, next.unspentBank)
        assertEquals(1, CoachSkillTreeEngine.level(next, CoachSkillBranch.Recruiting))
    }

    @Test
    fun autoSpend_drainsBankAlongArchetypePriority() {
        val state = CoachSkillPointState(unspentBank = 3, autoSpendEnabled = true)
        val next = CoachAutoSpendEngine.applyAll(state, CoachArchetype.Recruiter)
        assertEquals(0, next.unspentBank)
        assertTrue(CoachSkillTreeEngine.level(next, CoachSkillBranch.Recruiting) >= 1)
    }
}
