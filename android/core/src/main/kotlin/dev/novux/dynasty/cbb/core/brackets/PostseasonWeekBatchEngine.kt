package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.league.LeagueStandingsState

/** Batches national bracket sim for one postseason league week after the user week advances. */
object PostseasonWeekBatchEngine {
    fun runPostseasonWeek(
        standings: LeagueStandingsState,
        postseasonWeek: Int,
        userTeamId: String,
        userResult: LeagueGameResult?,
        conferenceIdByTeam: Map<String, String>,
        seed: Long,
    ): LeagueStandingsState {
        val selection = standings.selectionSunday ?: return standings
        var opening = standings.ncbaOpeningRound ?: NcbaOpeningRoundEngine.planFromSelection(selection)
        var nci = standings.nciBracket ?: NciHeritageBracketEngine.planNci(selection)
        var heritage = standings.heritageBracket ?: NciHeritageBracketEngine.planHeritage(selection)
        var state = standings.copy(
            ncbaOpeningRound = opening,
            nciBracket = nci,
            heritageBracket = heritage,
        )

        opening = NcbaOpeningRoundEngine.simulatePostseasonWeek(
            opening,
            postseasonWeek,
            userTeamId,
            userResult,
            seed,
        )
        state = state.copy(ncbaOpeningRound = opening)

        if (postseasonWeek >= 2 && opening.completed) {
            var main = NcbaMainBracketWeekEngine.ensureBuilt(
                selection,
                opening,
                conferenceIdByTeam,
                state.ncbaMainBracket,
            )
            main = NcbaMainBracketWeekEngine.simulatePostseasonWeek(
                main,
                postseasonWeek,
                userTeamId,
                userResult,
                seed + 1_000L,
            )
            state = state.copy(ncbaMainBracket = main)
        }

        nci = SingleEliminationWeekEngine.simulatePostseasonWeek(
            nci,
            postseasonWeek,
            PostseasonParallelCalendar::nciRoundWeek,
            PostseasonParallelCalendar.NCI_ROUNDS,
            userTeamId,
            userResult,
            seed + 2_000L,
        )
        heritage = SingleEliminationWeekEngine.simulatePostseasonWeek(
            heritage,
            postseasonWeek,
            PostseasonParallelCalendar::heritageRoundWeek,
            PostseasonParallelCalendar.HERITAGE_ROUNDS,
            userTeamId,
            userResult,
            seed + 3_000L,
        )
        return state.copy(
            nciBracket = nci,
            heritageBracket = heritage,
            lastCommittedPostseasonWeek = postseasonWeek,
        )
    }
}
