package dev.novux.dynasty.cbb.core.dashboard

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertTrue
import org.junit.Test

class InjuryOptionalDashboardTasksTest {
    @Test
    fun buildsReviewAndMedicalTasksForEligibleInjury() {
        val player = RosterPlayer(
            playerId = "p1",
            dynastyId = "d",
            position = PlayerPosition.ShootingGuard,
            classYear = PlayerClassYear.Sophomore,
            isStarter = true,
            depthOrder = 2,
            inTransferPortal = false,
            displayName = "Sam Brooks",
            injuryActive = true,
            injuryId = "knee",
            injuryWeeksRemaining = 4,
            injuryLabel = "Knee sprain",
            injurySeverityWire = "moderate",
            injuryOccurredAtRsGameIndex = 8,
            rsGamesPlayedSeason = 8,
        )
        val tasks = InjuryOptionalDashboardTasks.build(listOf(player), rsWeekIndex = 10)
        assertTrue(tasks.any { it.id == DashboardTaskId.ReviewInjury })
        assertTrue(tasks.any { it.id == DashboardTaskId.MedicalRedshirtCandidate })
        assertTrue(tasks.all { it.deepLink == DashboardDeepLink.Strategy })
    }
}
