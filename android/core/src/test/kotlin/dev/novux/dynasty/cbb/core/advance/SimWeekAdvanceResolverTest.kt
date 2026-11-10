package dev.novux.dynasty.cbb.core.advance

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.PhaseWeekPolicy
import dev.novux.dynasty.cbb.core.calendar.checkpoint.WeekCheckpointState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SimWeekAdvanceResolverTest {
    @Test
    fun confirmation_includesRecruitingLineWhenAutoMaintain() {
        val confirmation = SimWeekAdvanceResolver.buildConfirmation(
            SimWeekAdvanceInput(
                nextWeekLabel = "Regular Season · Week 2",
                unplayedUserGameCount = 0,
                recruitingPointsAvailable = 1500,
                userManualRecruitingSpent = 0,
                recruitingHubOpen = true,
                estimatedAutoRecruitingSpend = 200,
                portalRecruitingWeekOpen = false,
            ),
        )
        assertTrue(confirmation.needsDialog)
        assertTrue(
            confirmation.lines.any {
                it.kind == SimWeekConfirmationLineKind.UserRecruitingAutoMaintain
            },
        )
    }

    @Test
    fun resolve_fastSimsUnplayedGameBeforeAdvance() {
        val state = WeekCheckpointState.bootstrap(1, CalendarPhase.RegularSeason)
        val resolution = SimWeekAdvanceResolver.resolvePreAdvance(
            state,
            SimWeekAdvanceInput(
                nextWeekLabel = "Week 2",
                unplayedUserGameCount = 1,
                recruitingPointsAvailable = 0,
                userManualRecruitingSpent = 0,
                recruitingHubOpen = false,
                estimatedAutoRecruitingSpend = 0,
                portalRecruitingWeekOpen = false,
            ),
            dynastySeed = 42L,
        )
        assertEquals(1, resolution.fastSimmedGames)
        assertEquals(1, resolution.checkpointAfterPreAdvance.provisionalGames.size)
    }

    @Test
    fun unplayedGames_detectedWhenNoProvisionalMark() {
        val state = WeekCheckpointState.bootstrap(1, CalendarPhase.RegularSeason)
        val count = UserWeekGameStatus.unplayedUserGames(state, teamHasScheduledGame = true)
        assertEquals(1, count)
    }
}
