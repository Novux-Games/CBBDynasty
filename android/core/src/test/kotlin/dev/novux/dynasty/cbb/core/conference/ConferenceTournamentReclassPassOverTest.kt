package dev.novux.dynasty.cbb.core.conference

import dev.novux.dynasty.cbb.core.league.LeagueTeamRecord
import dev.novux.dynasty.cbb.core.pack.TournamentRoundPack
import dev.novux.dynasty.cbb.core.pack.TournamentTemplatePack
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ConferenceTournamentReclassPassOverTest {
    @Test
    fun simAppliesRunnerUpPassOver() {
        val template = TournamentTemplatePack(
            id = "tpl",
            bracketSize = 2,
            seeds = 2,
            sitePolicy = "neutral",
            qualificationPolicy = "all",
            rounds = listOf(TournamentRoundPack("F", 1)),
        )
        val result = ConferenceTournamentEngine.simulate(
            ConferenceTournamentInput(
                conferenceId = "conf",
                template = template,
                teamIds = listOf("reclass", "eligible"),
                records = mapOf(
                    "reclass" to LeagueTeamRecord("reclass", 20, 2),
                    "eligible" to LeagueTeamRecord("eligible", 18, 4),
                ),
                postseasonEligible = mapOf("reclass" to false, "eligible" to true),
                seasonIndex = 1,
                seed = 99L,
            ),
        )
        if (result.championTeamId == "reclass") {
            assertEquals("eligible", result.ncbaAutoBidTeamId)
            assertNotEquals(result.championTeamId, result.ncbaAutoBidTeamId)
        } else {
            assertEquals(result.championTeamId, result.ncbaAutoBidTeamId)
        }
    }
}
