package dev.novux.dynasty.cbb.core.coach.skill

import dev.novux.dynasty.cbb.core.coach.CoachArchetype
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CoachArchetypeStartingSkillBiasPolicyTest {
    @Test
    fun seedsArchetypePriorityBranchAtSign() {
        assertEquals("MEGA-COACH-026", CoachArchetypeStartingSkillBiasPolicy.MEGA_ID)
        val seeded = CoachArchetypeStartingSkillBiasPolicy.seedAtSign(CoachArchetype.Recruiter)
        assertTrue(CoachSkillTreeEngine.level(seeded, CoachSkillBranch.Recruiting) >= 1)
    }
}