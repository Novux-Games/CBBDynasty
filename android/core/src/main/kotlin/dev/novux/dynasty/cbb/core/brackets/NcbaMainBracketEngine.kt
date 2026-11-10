package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.calendar.PostseasonCalendar
import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.selection.SelectionSundaySnapshot

/** NCBA main bracket R1→NC — [planning/postseason.md#main-bracket-64--champion]. */
object NcbaMainBracketEngine {
    private val FINAL_FOUR_MATCHUPS = listOf(
        NcbaRegionId.East to NcbaRegionId.West,
        NcbaRegionId.South to NcbaRegionId.Midwest,
    )

    fun build(
        selection: SelectionSundaySnapshot,
        openingRound: NcbaOpeningRoundSnapshot,
        conferenceIdByTeam: Map<String, String>,
    ): NcbaMainBracketSnapshot {
        val field = NcbaFieldBuilder.fieldOf64(selection, openingRound)
        var entrants = NcbaSCurveEngine.assignEntrants(field)
        entrants = NcbaConferenceConflictAdjuster.adjust(entrants, conferenceIdByTeam)
        return NcbaMainBracketSnapshot(
            seasonIndex = selection.seasonIndex,
            entrants = entrants,
        )
    }

    fun simulate(
        snapshot: NcbaMainBracketSnapshot,
        seed: Long,
    ): NcbaMainBracketSnapshot {
        if (snapshot.completed) return snapshot
        val results = mutableListOf<LeagueGameResult>()
        val regionalChampions = mutableMapOf<NcbaRegionId, String>()
        var week = PostseasonCalendar.OPENING_ROUND_WEEK + 1
        NcbaRegionId.serpentineOrder.forEach { region ->
            val regionalEntrants = snapshot.entrants.filter { it.regionId == region }
            val (regionalResults, champion) = NcbaRegionalBracket.simulateRegion(
                regionalEntrants,
                startPostseasonWeek = week,
                seed = seed + region.ordinal * 1_000_000L,
            )
            results += regionalResults
            regionalChampions[region] = champion
        }
        week += 4
        val semifinalTeams = FINAL_FOUR_MATCHUPS.map { (a, b) ->
            regionalChampions.getValue(a) to regionalChampions.getValue(b)
        }
        val (semiWinners, semiResults) = NcbaRegionalBracket.simulateRound(
            semifinalTeams,
            postseasonWeek = week,
            seed = seed + 9_000L,
        )
        results += semiResults
        week++
        val (finalWinners, finalResults) = NcbaRegionalBracket.simulateRound(
            listOf(semiWinners[0] to semiWinners[1]),
            postseasonWeek = week,
            seed = seed + 10_000L,
        )
        results += finalResults
        return snapshot.copy(
            gameResults = results,
            regionalChampions = regionalChampions,
            championTeamId = finalWinners.single(),
            completed = true,
        )
    }
}
