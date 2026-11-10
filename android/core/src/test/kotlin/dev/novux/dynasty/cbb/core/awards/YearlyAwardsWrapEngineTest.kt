package dev.novux.dynasty.cbb.core.awards

import dev.novux.dynasty.cbb.core.league.LeagueStandingsState
import dev.novux.dynasty.cbb.core.league.LeagueTeamRecord
import dev.novux.dynasty.cbb.core.pack.ConferencePack
import dev.novux.dynasty.cbb.core.pack.IdentityPack
import dev.novux.dynasty.cbb.core.pack.IdentityPackIndex
import dev.novux.dynasty.cbb.core.pack.TeamColorsPack
import dev.novux.dynasty.cbb.core.pack.TeamPack
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class YearlyAwardsWrapEngineTest {
    @Test
    fun producesNcbaMvpAndCoachLines() {
        val index = stubPack()
        val standings = LeagueStandingsState(
            seasonIndex = 1,
            records = mapOf(
                "a" to LeagueTeamRecord("a", wins = 28, losses = 4),
                "b" to LeagueTeamRecord("b", wins = 22, losses = 10),
            ),
            netRankByTeam = mapOf("a" to 1, "b" to 2),
        )
        val archive = YearlyAwardsWrapEngine.compute(
            YearlyAwardsWrapInput(
                seasonIndex = 1,
                userTeamId = "a",
                userConferenceId = "c1",
                userSchoolName = "Alpha",
                userCoachDisplayName = "Coach Alpha",
                userRoster = listOf(
                    rosterPlayer("p1", "a", ovr = 88),
                    rosterPlayer("p2", "a", ovr = 70),
                ),
                standings = standings,
                packIndex = index,
            ),
        )
        assertEquals("NCBA MVP", archive.ncbaMvp.single().awardLabel)
        assertEquals("NCBA Coach of the Year", archive.ncbaCoaches.single().awardLabel)
        assertEquals("Team MVP", archive.userTeamMvp?.awardLabel)
        assertTrue(archive.conferenceAwards.containsKey("c1"))
    }

    private fun rosterPlayer(id: String, dynastyId: String, ovr: Int) = RosterPlayer(
        playerId = id,
        dynastyId = dynastyId,
        position = PlayerPosition.PointGuard,
        classYear = PlayerClassYear.Sophomore,
        isStarter = true,
        depthOrder = 1,
        inTransferPortal = false,
        displayName = "Star $id",
        ovr = ovr,
    )

    private fun stubPack(): IdentityPackIndex =
        IdentityPackIndex.from(
            IdentityPack(
                packVersion = 1,
                membershipYear = "2026-27",
                disclaimerAcceptedRequired = true,
                rulesetYear = 2026,
                conferences = listOf(
                    ConferencePack(
                        id = "c1",
                        displayName = "Test Conf",
                        leagueGameCount = 18,
                        tournamentTemplateId = null,
                    ),
                ),
                teams = listOf(
                    TeamPack(
                        id = "a",
                        conferenceId = "c1",
                        displayName = "Alpha",
                        shortName = "A",
                        homeCity = "X",
                        homeState = "CA",
                        colors = TeamColorsPack(primary = "#000", secondary = "#fff"),
                        arenaBaseName = "Arena",
                    ),
                    TeamPack(
                        id = "b",
                        conferenceId = "c1",
                        displayName = "Beta",
                        shortName = "B",
                        homeCity = "X",
                        homeState = "CA",
                        colors = TeamColorsPack(primary = "#000", secondary = "#fff"),
                        arenaBaseName = "Arena",
                    ),
                ),
                tournamentTemplates = emptyList(),
            ),
        )
}
