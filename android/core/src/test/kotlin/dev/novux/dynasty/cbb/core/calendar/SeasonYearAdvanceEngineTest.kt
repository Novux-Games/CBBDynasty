package dev.novux.dynasty.cbb.core.calendar

import dev.novux.dynasty.cbb.core.calendar.checkpoint.WeekCheckpointState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SeasonYearAdvanceEngineTest {
    @Test
    fun planEnterOffseason_seasonOneComplete_requiresCoachingCarouselForSeasonTwo() {
        val checkpoint = WeekCheckpointState.bootstrap(1, CalendarPhase.SeasonReview)
        val plan = SeasonYearAdvanceEngine.planEnterOffseason(checkpoint)
        assertEquals(2, plan.newSeasonIndex)
        assertTrue(plan.coachingCarouselRequired)
        assertEquals(
            OffseasonEntryKind.CoachingCarouselFirst,
            plan.transition.offseasonEntry,
        )
    }
}
