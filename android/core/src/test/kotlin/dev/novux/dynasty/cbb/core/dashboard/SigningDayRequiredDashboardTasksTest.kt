package dev.novux.dynasty.cbb.core.dashboard

import dev.novux.dynasty.cbb.core.signingday.SigningDayProgress
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SigningDayRequiredDashboardTasksTest {
    @Test
    fun week6_startsWithRankingsAndPositions() {
        val tasks = SigningDayRequiredDashboardTasks.build(SigningDayProgress(1))
        assertEquals(2, tasks.size)
        assertTrue(tasks.all { it.kind == DashboardTaskKind.Required })
    }

    @Test
    fun addsHardSignWhenPrereqsMet() {
        val tasks = SigningDayRequiredDashboardTasks.build(
            SigningDayProgress(1, rankingsViewed = true, positionsConfirmed = true),
        )
        assertTrue(tasks.any { it.id == DashboardTaskId.SigningDayHardSign })
    }
}
