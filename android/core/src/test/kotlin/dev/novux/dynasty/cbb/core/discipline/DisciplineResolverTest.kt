package dev.novux.dynasty.cbb.core.discipline

import dev.novux.dynasty.cbb.core.balance.BalanceDisciplineTuning
import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DisciplineResolverTest {
    @Test
    fun suspendGamesSetsCounter() {
        val player = roster("p1")
        val incident = PendingDisciplineIncident(
            playerId = "p1",
            playerDisplayName = "Sam",
            kind = DisciplineIncidentKind.Conduct,
            headline = "Team discipline",
            flavorBody = "Incident",
            conductTemplateId = "team_rules",
            recordedAtRsWeek = 5,
        )
        val result = DisciplineResolver.apply(
            listOf(player),
            incident,
            DisciplineResolutionChoice.SuspendGames,
            suspendGames = 4,
            tuning = BalanceDisciplineTuning(),
        )
        assertEquals(4, result.players.single().gamesSuspendedRemaining)
    }

    @Test
    fun academicHoldFlagsPlayer() {
        val player = roster("p1")
        val incident = PendingDisciplineIncident(
            playerId = "p1",
            playerDisplayName = "Sam",
            kind = DisciplineIncidentKind.Academic,
            headline = "Academic",
            flavorBody = "GPA low",
            gpaCurrent = 1.2,
            recordedAtRsWeek = 19,
        )
        val result = DisciplineResolver.apply(
            listOf(player),
            incident,
            DisciplineResolutionChoice.AcademicHoldSupport,
            tuning = BalanceDisciplineTuning(),
        )
        assertTrue(result.players.single().academicHold)
    }

    private fun roster(id: String): RosterPlayer = RosterPlayer(
        playerId = id,
        dynastyId = "d",
        position = PlayerPosition.Center,
        classYear = PlayerClassYear.Junior,
        isStarter = true,
        depthOrder = 5,
        inTransferPortal = false,
        rsGamesPlayedSeason = 10,
    )
}
