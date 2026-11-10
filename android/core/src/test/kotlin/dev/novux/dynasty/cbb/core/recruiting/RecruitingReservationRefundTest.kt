package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.balance.BalanceLoadResult
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingActionsParser
import dev.novux.dynasty.cbb.core.balance.BalanceRulesetCodec
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import java.nio.file.Path
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RecruitingReservationRefundTest {
  @Test
  fun weekClose_refundsActiveReservations() {
        val reservations = listOf(
            RecruitingWeekReservation("1", "p1", RecruitingRefundableAction.ScholarshipOffer, 150),
            RecruitingWeekReservation("2", "p2", RecruitingRefundableAction.OfficialVisit, 100),
        )
        assertEquals(250, RecruitingReservationRefund.refundCredits(reservations))
        assertEquals(750, RecruitingReservationRefund.pointsAfterWeekCloseRefunds(500, reservations))
    }

    @Test
    fun scholarshipOffer_createsReservation_andCharges() {
        val actions = loadActions()
        val (spend, reservation) = RecruitingRefundableActionSpend.spendScholarshipOffer(
            prospectId = "hs-1-1",
            pointsAvailable = 400,
            actions = actions,
        )
        assertTrue(spend.success)
        assertTrue(spend.countsAsWeekReservation)
        assertEquals(250, spend.pointsRemaining)
        assertEquals(150, reservation?.pointsReserved)
    }

    @Test
    fun scoutOncePerProspectPerSeason() {
        val actions = loadActions()
        val existing = listOf(
            RecruitingWeekReservation("a", "hs-1-1", RecruitingRefundableAction.ScoutTheirGame, 50),
        )
        val consumption = RecruitingSeasonConsumption(cycleSeasonIndex = 1)
        val (second, _) = RecruitingRefundableActionSpend.spendScoutTheirGame(
            "hs-1-1",
            200,
            actions,
            existing,
            consumption,
            CalendarPhase.RegularSeason,
        )
        assertEquals(false, second.success)
        assertEquals(RecruitingSpendFailure.ReservationAlreadyActive, second.failure)
    }

    @Test
    fun scoutClosedOutsideRegularSeason() {
        val actions = loadActions()
        val (spend, _) = RecruitingRefundableActionSpend.spendScoutTheirGame(
            "hs-1-1",
            200,
            actions,
            emptyList(),
            RecruitingSeasonConsumption(1),
            CalendarPhase.Preseason,
        )
        assertEquals(false, spend.success)
        assertEquals(RecruitingSpendFailure.ActionWindowClosed, spend.failure)
    }

    private fun loadActions(): dev.novux.dynasty.cbb.core.balance.BalanceRecruitingActions {
        var dir = Path.of(System.getProperty("user.dir")).toAbsolutePath()
        while (dir != null && !dir.resolve("assets/balance/balance-2026.json").toFile().exists()) {
            dir = dir.parent
        }
        val raw = dir!!.resolve("assets/balance/balance-2026.json").readText()
        val ruleset = (BalanceRulesetCodec.parseAndValidate(raw) as BalanceLoadResult.Success).ruleset
        return BalanceRecruitingActionsParser.parse(ruleset.recruiting)
    }
}
