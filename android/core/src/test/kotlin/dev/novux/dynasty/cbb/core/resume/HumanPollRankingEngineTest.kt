package dev.novux.dynasty.cbb.core.resume

import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.league.LeagueTeamRecord
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HumanPollRankingEngineTest {
    @Test
    fun mediaAndCoachesPollsCanDiverge() {
        val prestige = mapOf(
            "a" to 92,
            "b" to 70,
            "c" to 55,
            "d" to 48,
            "e" to 45,
        )
        val results = listOf(LeagueGameResult("a", "e", 68, 70, rsWeekIndex = 1))
        val records = mapOf(
            "a" to LeagueTeamRecord("a", 0, 1),
            "e" to LeagueTeamRecord("e", 1, 0),
        )
        val net = NcbaNetRankingEngine.compute(prestige.keys, prestige, records, results)
        val polls = HumanPollRankingEngine.compute(
            netTable = net,
            programPrestige = prestige,
            results = results,
            priorMediaRanks = mapOf("a" to 5),
            priorCoachesRanks = mapOf("a" to 8),
            lastWeekResults = results,
        )
        assertTrue(
            polls.mediaRankByTeam.any { (id, rank) ->
                polls.coachesRankByTeam[id] != rank
            },
        )
        assertEquals(5, polls.mediaRankByTeam.size)
    }

    @Test
    fun listsTwentyFiveSpotlightsPerPoll() {
        val prestige = (1..30).associate { "t$it" to (40 + it) }
        val net = NcbaNetRankingEngine.compute(prestige.keys, prestige, emptyMap(), emptyList())
        val polls = HumanPollRankingEngine.compute(
            netTable = net,
            programPrestige = prestige,
            results = emptyList(),
            priorMediaRanks = emptyMap(),
            priorCoachesRanks = emptyMap(),
            lastWeekResults = emptyList(),
        )
        assertEquals(25, polls.mediaTop25TeamIds.size)
        assertEquals(25, polls.coachesTop25TeamIds.size)
    }
}
