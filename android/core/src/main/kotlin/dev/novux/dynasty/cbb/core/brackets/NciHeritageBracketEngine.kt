package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.selection.SelectionSundaySnapshot

/** Parallel NCI (32) and Heritage (16) brackets — [planning/postseason.md]. */
object NciHeritageBracketEngine {
    fun planNci(selection: SelectionSundaySnapshot): NationalBracketSnapshot =
        SingleEliminationBracketEngine.plan(
            tournamentId = PostseasonParallelCalendar.NCI_TOURNAMENT_ID,
            seasonIndex = selection.seasonIndex,
            invites = selection.nciField,
        )

    fun planHeritage(selection: SelectionSundaySnapshot): NationalBracketSnapshot =
        SingleEliminationBracketEngine.plan(
            tournamentId = PostseasonParallelCalendar.HERITAGE_TOURNAMENT_ID,
            seasonIndex = selection.seasonIndex,
            invites = selection.heritageField,
        )

    fun simulateNci(snapshot: NationalBracketSnapshot, seed: Long): NationalBracketSnapshot =
        SingleEliminationBracketEngine.simulate(
            snapshot,
            weekForRound = PostseasonParallelCalendar::nciRoundWeek,
            seed = seed,
        )

    fun simulateHeritage(snapshot: NationalBracketSnapshot, seed: Long): NationalBracketSnapshot =
        SingleEliminationBracketEngine.simulate(
            snapshot,
            weekForRound = PostseasonParallelCalendar::heritageRoundWeek,
            seed = seed + 50_000L,
        )
}
