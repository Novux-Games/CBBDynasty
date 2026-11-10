package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.balance.BalanceInjuryOccurrence
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.injuries.GameSimInjuryEvent
import dev.novux.dynasty.cbb.core.injuries.InjuryBankRow
import dev.novux.dynasty.cbb.core.injuries.InjuryOccurrenceEngine
import dev.novux.dynasty.cbb.core.injuries.InjuryRosterOps
import dev.novux.dynasty.cbb.core.injuries.InjuryApplyContext
import dev.novux.dynasty.cbb.core.roster.RosterPlayer

/**
 * Post-game injury rolls — [planning/injuries.md#sim-hook].
 */
data class GameSimInjuryRollInput(
    val phase: CalendarPhase,
    val bank: List<InjuryBankRow>,
    val occurrence: BalanceInjuryOccurrence,
    val medicalStaffGrade: Int,
    val homeRosterPlayers: List<RosterPlayer>?,
    val awayRosterPlayers: List<RosterPlayer>?,
    val injuryApplyContext: InjuryApplyContext? = null,
)

object GameSimInjuryRoll {
    fun attach(
        input: GameSimInput,
        result: GameSimResult,
        rollInput: GameSimInjuryRollInput,
    ): GameSimResult {
        if (!result.isComplete) return result
        val homeMinutes = estimateMinutes(input.homeRoster, result.homeBox.possessions)
        val awayMinutes = estimateMinutes(input.awayRoster, result.awayBox.possessions)
        val homeInjuries = rollInput.homeRosterPlayers?.let { roster ->
            InjuryOccurrenceEngine.rollAfterGame(
                phase = rollInput.phase,
                minutesByPlayerId = homeMinutes,
                roster = roster,
                bank = rollInput.bank,
                tuning = rollInput.occurrence,
                medicalStaffGrade = rollInput.medicalStaffGrade,
                gameSeed = input.seed xor input.home.teamId.hashCode().toLong(),
            ).injuries
        }.orEmpty()
        val awayInjuries = rollInput.awayRosterPlayers?.let { roster ->
            InjuryOccurrenceEngine.rollAfterGame(
                phase = rollInput.phase,
                minutesByPlayerId = awayMinutes,
                roster = roster,
                bank = rollInput.bank,
                tuning = rollInput.occurrence,
                medicalStaffGrade = rollInput.medicalStaffGrade,
                gameSeed = input.seed xor input.away.teamId.hashCode().toLong(),
            ).injuries
        }.orEmpty()
        return result.copy(
            homeInjuries = homeInjuries,
            awayInjuries = awayInjuries,
        )
    }

    fun applyToRoster(
        players: List<RosterPlayer>,
        events: List<GameSimInjuryEvent>,
        context: InjuryApplyContext,
    ): List<RosterPlayer> = InjuryRosterOps.applyGameInjuries(players, events, context)

    private fun estimateMinutes(roster: GameSimRosterInput?, possessions: Int): Map<String, Double> {
        if (roster == null) return emptyMap()
        val scale = possessions.toDouble() / 70.0.coerceAtLeast(1.0)
        return roster.participants
            .groupBy { it.playerId }
            .mapValues { (_, parts) -> parts.sumOf { it.targetMinutes } * scale }
    }

}
