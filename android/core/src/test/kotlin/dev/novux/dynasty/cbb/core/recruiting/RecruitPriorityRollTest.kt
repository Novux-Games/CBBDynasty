package dev.novux.dynasty.cbb.core.recruiting

import org.junit.Assert.assertTrue
import org.junit.Test

class RecruitPriorityRollTest {
    @Test
    fun roll_oneOrTwoPriorities_deterministic() {
        val first = RecruitPriorityRoll.roll("hs-1-10")
        val second = RecruitPriorityRoll.roll("hs-1-10")
        assertEqualsLists(first, second)
        assertTrue(first.size in 1..2)
    }

    private fun assertEqualsLists(a: List<RecruitPriority>, b: List<RecruitPriority>) {
        assertTrue(a == b)
    }
}
