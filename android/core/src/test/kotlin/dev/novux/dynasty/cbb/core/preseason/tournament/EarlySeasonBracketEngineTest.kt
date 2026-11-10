package dev.novux.dynasty.cbb.core.preseason.tournament

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EarlySeasonBracketEngineTest {
    @Test
    fun islandOpeningRoundHasEightGames() {
        val seeds = (1..16).map { "team-$it" }
        val snapshot = EarlySeasonBracketEngine.bootstrapSnapshot(
            EarlySeasonEventCatalog.islandClassic,
            seasonIndex = 1,
            participantIds = seeds,
            seed = 99,
        )
        val pairings = EarlySeasonBracketEngine.pairingsForWeek(snapshot, rsWeekIndex = 1)
        assertEquals(8, pairings.size)
    }

    @Test
    fun loserGetsEliminationByesOnLaterEventWeeks() {
        val event = EarlySeasonEventCatalog.blueBloodTipOff
        val seeds = listOf("a", "b", "c", "d")
        var snapshot = EarlySeasonBracketEngine.bootstrapSnapshot(event, 1, seeds, seed = 1)
        val weekOne = EarlySeasonBracketEngine.advanceWeek(
            snapshot,
            rsWeekIndex = 1,
            results = emptyList(),
            seed = 5,
        )
        val loser = weekOne.eliminatedAtRsWeek.keys.first()
        val byes = EarlySeasonBracketEngine.eliminationByeWeeks(weekOne, loser)
        assertEquals(listOf(2), byes)
        assertEquals(EarlySeasonWeekSlot.EliminationBye, EarlySeasonBracketEngine.teamWeekSlot(weekOne, loser, 2))
    }

    @Test
    fun desertFourWeekFootprint() {
        assertEquals(listOf(8, 4, 2, 1), EarlySeasonEventFootprints.roundGameCounts(EarlySeasonEventCatalog.desertShootout))
        assertEquals(4, EarlySeasonEventCatalog.desertShootout.rsCalendarWeeks)
    }

    @Test
    fun fieldPlannerRespectsUserEvent() {
        val teams = (1..20).map {
            dev.novux.dynasty.cbb.core.pack.TeamDisplayOverlay(
                teamId = "t$it",
                conferenceId = "c",
                displayName = "T$it",
                shortName = "T$it",
                homeCity = "X",
                homeState = "ST",
                primaryColorHex = "#000",
                secondaryColorHex = "#fff",
                logoUri = null,
                postseasonEligible = true,
                programPrestige = it,
            )
        }
        val fields = EarlySeasonFieldPlanner.planFields(
            teams = teams,
            userTeamId = "t3",
            userAcceptedEventId = EarlySeasonEventCatalog.islandClassic.templateId,
            seed = 42,
        )
        val island = fields[EarlySeasonEventCatalog.islandClassic.templateId]
        assertTrue(island != null)
        assertTrue(island!!.contains("t3"))
    }
}
