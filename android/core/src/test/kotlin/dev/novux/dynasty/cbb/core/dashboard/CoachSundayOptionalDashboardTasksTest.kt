package dev.novux.dynasty.cbb.core.dashboard

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CoachSundayOptionalDashboardTasksTest {
    @Test
    fun nudgeWhenBankPositive() {
        val tasks = CoachSundayOptionalDashboardTasks.build(2)
        assertEquals(1, tasks.size)
        assertEquals(DashboardTaskId.CoachSundaySkillPoints, tasks.first().id)
        assertEquals(DashboardDeepLink.CoachCard, tasks.first().deepLink)
        assertEquals(2, tasks.first().formatArgs.first())
    }

    @Test
    fun emptyWhenZeroBank() {
        assertTrue(CoachSundayOptionalDashboardTasks.build(0).isEmpty())
    }
}
