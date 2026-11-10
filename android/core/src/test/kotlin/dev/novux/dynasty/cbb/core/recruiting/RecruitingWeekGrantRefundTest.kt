package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import org.junit.Assert.assertTrue
import org.junit.Test

class RecruitingWeekGrantRefundTest {
    @Test
    fun weekAdvance_grantAppliesAfterReservationRefund() {
        val economy = dev.novux.dynasty.cbb.core.balance.BalanceRecruitingEconomy(
            weeklyCarryoverFraction = 0.5,
            signingCrunchMultiplier = 1.5,
            signingCrunchOffseasonWeeks = listOf(2, 3, 4, 5),
            prestigeTiers = emptyList(),
        )
        val afterOfferSpend = RecruitingCycleLedger(
            cycleSeasonIndex = 1,
            programPrestige = 40,
            weeklyPoolBase = 1000,
            pointsAvailable = 850,
        )
        val reservations = listOf(
            RecruitingWeekReservation("1", "p1", RecruitingRefundableAction.ScholarshipOffer, 150),
        )
        val withRefund = afterOfferSpend.copy(
            pointsAvailable = RecruitingReservationRefund.pointsAfterWeekCloseRefunds(
                afterOfferSpend.pointsAvailable,
                reservations,
            ),
        )
        val (next, _) = RecruitingWeekGrant.applyWeekAdvanceGrant(
            economy,
            withRefund,
            phase = CalendarPhase.RegularSeason,
            offseasonWeekIndex = null,
            pointsSpentThisWeek = 0,
        )
        assertTrue(next.pointsAvailable >= withRefund.weeklyPoolBase)
    }
}
