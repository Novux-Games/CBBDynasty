package dev.novux.dynasty.cbb.core.conference

import dev.novux.dynasty.cbb.core.league.LeagueTeamRecord
import dev.novux.dynasty.cbb.core.pack.ConferencePack
import dev.novux.dynasty.cbb.core.pack.IdentityPack
import dev.novux.dynasty.cbb.core.pack.TeamColorsPack
import dev.novux.dynasty.cbb.core.pack.TeamPack
import dev.novux.dynasty.cbb.core.pack.TournamentRoundPack
import dev.novux.dynasty.cbb.core.pack.TournamentTemplatePack
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ConferenceTournamentEngineTest {
    @Test
    fun simulatesTemplateRoundsAndCrownChampion() {
        val template = TournamentTemplatePack(
            id = "tpl-test",
            bracketSize = 8,
            seeds = 8,
            sitePolicy = "neutral",
            qualificationPolicy = "top-8",
            rounds = listOf(
                TournamentRoundPack("R1", 4),
                TournamentRoundPack("SF", 2),
                TournamentRoundPack("F", 1),
            ),
        )
        val teams = (1..8).map { "t$it" }
        val records = teams.associateWith { LeagueTeamRecord(it, wins = it.removePrefix("t").toInt(), losses = 0) }
        val result = ConferenceTournamentEngine.simulate(
            ConferenceTournamentInput(
                conferenceId = "conf",
                template = template,
                teamIds = teams,
                records = records,
                seasonIndex = 1,
                seed = 42L,
            ),
        )
        assertEquals(8, result.qualifiedTeamIds.size)
        assertNotNull(result.championTeamId)
        assertTrue(result.gameResults.isNotEmpty())
        assertEquals(4 + 2 + 1, result.gameResults.size)
    }

    @Test
    fun top14PolicyLimitsField() {
        val template = TournamentTemplatePack(
            id = "tpl-sec",
            bracketSize = 16,
            seeds = 16,
            sitePolicy = "neutral",
            qualificationPolicy = "top-14",
            rounds = listOf(
                TournamentRoundPack("R1", 8),
                TournamentRoundPack("QF", 4),
                TournamentRoundPack("SF", 2),
                TournamentRoundPack("F", 1),
            ),
        )
        val teams = (1..16).map { "t$it" }
        val records = teams.associateWith {
            val rank = it.removePrefix("t").toInt()
            LeagueTeamRecord(it, wins = 20 - rank, losses = rank)
        }
        val qualified = ConferenceTournamentQualifier.qualify(template, teams, records)
        assertEquals(14, qualified.size)
    }

    @Test
    fun simulateLeagueRunsAllConferences() {
        val pack = IdentityPack(
            packVersion = 1,
            membershipYear = "2026-27",
            disclaimerAcceptedRequired = true,
            rulesetYear = 2026,
            conferences = listOf(
                ConferencePack(id = "c1", displayName = "One", leagueGameCount = 18, tournamentTemplateId = "tpl"),
                ConferencePack(id = "c2", displayName = "Two", leagueGameCount = 18, tournamentTemplateId = "tpl"),
            ),
            teams = (1..4).map {
                TeamPack(
                    id = "t$it",
                    conferenceId = if (it <= 2) "c1" else "c2",
                    displayName = "T$it",
                    shortName = "T$it",
                    homeCity = "X",
                    homeState = "Y",
                    postseasonEligible = true,
                    colors = TeamColorsPack("#000", "#fff"),
                    arenaBaseName = "Arena",
                )
            },
            tournamentTemplates = listOf(
                TournamentTemplatePack(
                    id = "tpl",
                    bracketSize = 2,
                    seeds = 2,
                    sitePolicy = "neutral",
                    qualificationPolicy = "all",
                    rounds = listOf(TournamentRoundPack("F", 1)),
                ),
            ),
        )
        val templates = pack.conferences.mapNotNull { conf ->
            val tpl = pack.tournamentTemplates.first { it.id == conf.tournamentTemplateId }
            conf.id to tpl
        }
        val teamsByConf = pack.teams.groupBy({ it.conferenceId }, { it.id })
        val season = ConferenceTournamentEngine.simulateLeague(
            conferences = templates,
            records = pack.teams.associate { it.id to LeagueTeamRecord(it.id, 10, 5) },
            teamsByConference = teamsByConf,
            seasonIndex = 1,
            seed = 7L,
        )
        assertEquals(2, season.resultsByConference.size)
    }
}
