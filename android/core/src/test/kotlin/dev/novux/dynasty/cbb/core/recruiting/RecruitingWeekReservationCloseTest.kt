package dev.novux.dynasty.cbb.core.recruiting

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RecruitingWeekReservationCloseTest {
    @Test
    fun weekClose_keepsOffers_recordsScoutAndVisit() {
        val active = listOf(
            RecruitingWeekReservation("1", "a", RecruitingRefundableAction.ScholarshipOffer, 150),
            RecruitingWeekReservation("2", "b", RecruitingRefundableAction.ScoutTheirGame, 50),
            RecruitingWeekReservation("3", "c", RecruitingRefundableAction.OfficialVisit, 100),
        )
        val (still, consumption) = RecruitingWeekReservationClose.afterWeekAdvance(
            active,
            RecruitingSeasonConsumption(1),
        )
        assertEquals(1, still.size)
        assertEquals(RecruitingRefundableAction.ScholarshipOffer, still.single().action)
        assertTrue(consumption.consumedScout("b"))
        assertTrue(consumption.consumedOfficialVisit("c"))
    }
}
