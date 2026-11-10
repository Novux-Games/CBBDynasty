package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingEconomy
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import kotlin.math.roundToInt

data class RecruitingCycleLedger(
    val cycleSeasonIndex: Int,
    val programPrestige: Int,
    val weeklyPoolBase: Int,
    val pointsAvailable: Int,
    val unspentAtLastWeekEnd: Int = 0,
    val cycleClosed: Boolean = false,
) {
    fun withPointsAvailable(next: Int): RecruitingCycleLedger = copy(pointsAvailable = next.coerceAtLeast(0))
}

data class WeekGrantBreakdown(
    val weeklyBase: Int,
    val crunchMultiplier: Double,
    val grantedThisWeek: Int,
    val carryoverFromPrior: Int,
    val newBalance: Int,
)

/**
 * Year-long economy — 50% carryover, offseason W2–5 crunch ([planning/recruiting-engine.md]).
 */
object RecruitingWeekGrant {
    fun openCycle(
        economy: BalanceRecruitingEconomy,
        cycleSeasonIndex: Int,
        programPrestige: Int,
    ): RecruitingCycleLedger {
        val base = RecruitingPrestigePool.weeklyPoolPoints(economy, programPrestige)
        return RecruitingCycleLedger(
            cycleSeasonIndex = cycleSeasonIndex,
            programPrestige = programPrestige,
            weeklyPoolBase = base,
            pointsAvailable = base,
        )
    }

    fun applyWeekAdvanceGrant(
        economy: BalanceRecruitingEconomy,
        ledger: RecruitingCycleLedger,
        phase: CalendarPhase,
        offseasonWeekIndex: Int?,
        pointsSpentThisWeek: Int,
    ): Pair<RecruitingCycleLedger, WeekGrantBreakdown> {
        if (ledger.cycleClosed) {
            val breakdown = WeekGrantBreakdown(
                weeklyBase = ledger.weeklyPoolBase,
                crunchMultiplier = 1.0,
                grantedThisWeek = 0,
                carryoverFromPrior = 0,
                newBalance = ledger.pointsAvailable,
            )
            return ledger to breakdown
        }
        val unspent = (ledger.pointsAvailable - pointsSpentThisWeek).coerceAtLeast(0)
        val carryover = (unspent * economy.weeklyCarryoverFraction).roundToInt()
        val multiplier = signingCrunchMultiplier(economy, phase, offseasonWeekIndex)
        val granted = (ledger.weeklyPoolBase * multiplier).roundToInt()
        val newBalance = granted + carryover
        val next = ledger.copy(
            pointsAvailable = newBalance,
            unspentAtLastWeekEnd = unspent,
        )
        val breakdown = WeekGrantBreakdown(
            weeklyBase = ledger.weeklyPoolBase,
            crunchMultiplier = multiplier,
            grantedThisWeek = granted,
            carryoverFromPrior = carryover,
            newBalance = newBalance,
        )
        return next to breakdown
    }

    fun signingCrunchMultiplier(
        economy: BalanceRecruitingEconomy,
        phase: CalendarPhase,
        offseasonWeekIndex: Int?,
    ): Double {
        if (phase != CalendarPhase.Offseason || offseasonWeekIndex == null) return 1.0
        return if (offseasonWeekIndex in economy.signingCrunchOffseasonWeeks) {
            economy.signingCrunchMultiplier
        } else {
            1.0
        }
    }

    /** Next cycle pool after End of Season prestige recap. */
    fun previewNextCycleWeeklyPool(
        economy: BalanceRecruitingEconomy,
        postRecapPrestige: Int,
    ): Int = RecruitingPrestigePool.weeklyPoolPoints(economy, postRecapPrestige)
}
