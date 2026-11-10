package dev.novux.dynasty.cbb.core.chemistry

import dev.novux.dynasty.cbb.core.balance.BalanceChemistry
import kotlinx.serialization.Serializable
import dev.novux.dynasty.cbb.core.lineup.DefaultStarterAlgorithm
import dev.novux.dynasty.cbb.core.roster.RosterPlayer

@Serializable
data class ChemistrySeasonState(
    val teamChemistry: Int = 70,
    val winStreak: Int = 0,
    val lossStreak: Int = 0,
    val stableStarterWeeks: Int = 0,
    val rosterChurnWeeksRemaining: Int = 0,
    val promiseBrokenApplied: Boolean = false,
)

data class ChemistryWeeklyTickInput(
    val userWonGame: Boolean? = null,
    val promiseBrokenThisSeason: Boolean = false,
    val rosterMassChangeThisWeek: Boolean = false,
    val charismaBonus: Int = 0,
)

object ChemistryWeekEngine {
    fun bootstrap(startValue: Int): ChemistrySeasonState =
        ChemistrySeasonState(teamChemistry = startValue.coerceIn(0, 100))

    fun tickAfterWeekAdvance(
        state: ChemistrySeasonState,
        players: List<RosterPlayer>,
        input: ChemistryWeeklyTickInput,
        tuning: BalanceChemistry,
    ): ChemistrySeasonState {
        var win = state.winStreak
        var loss = state.lossStreak
        when (input.userWonGame) {
            true -> {
                win++
                loss = 0
            }
            false -> {
                loss++
                win = 0
            }
            null -> { /* no game or unknown */ }
        }
        var churn = state.rosterChurnWeeksRemaining
        if (input.rosterMassChangeThisWeek) {
            churn = 4
        } else if (churn > 0) {
            churn--
        }
        var stable = state.stableStarterWeeks
        val starters = DefaultStarterAlgorithm.pickStarters(players)
        val sameFive = starters.size == 5 && players.count { it.isStarter } >= 5
        stable = if (sameFive) (stable + 1).coerceAtMost(6) else 0

        var delta = 0
        if (win >= 3) delta += tuning.winStreakBonusPerWeek
        if (loss >= 3) delta -= tuning.lossStreakPenaltyPerWeek
        if (churn > 0) delta -= 4
        if (stable >= 6) delta += 1
        delta += input.charismaBonus

        var promiseApplied = state.promiseBrokenApplied
        if (input.promiseBrokenThisSeason && !promiseApplied) {
            delta -= 8
            promiseApplied = true
        }

        val chemistry = (state.teamChemistry + delta).coerceIn(0, 100)
        return ChemistrySeasonState(
            teamChemistry = chemistry,
            winStreak = win,
            lossStreak = loss,
            stableStarterWeeks = stable,
            rosterChurnWeeksRemaining = churn,
            promiseBrokenApplied = promiseApplied,
        )
    }
}
