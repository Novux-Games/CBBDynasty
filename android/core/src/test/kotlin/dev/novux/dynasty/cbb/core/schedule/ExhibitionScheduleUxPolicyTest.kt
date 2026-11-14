package dev.novux.dynasty.cbb.core.schedule

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExhibitionScheduleUxPolicyTest {
    @Test
    fun validate_allowsSkip() {
        assertTrue(ExhibitionScheduleUxPolicy.validateDraft(0, emptyList()))
    }

    @Test
    fun validate_requiresOpponentsAndDistinctWeeks() {
        val drafts = listOf(
            ExhibitionGameDraft("a", "A", true, 1),
            ExhibitionGameDraft("b", "B", false, 2),
        )
        assertTrue(ExhibitionScheduleUxPolicy.validateDraft(2, drafts))
        assertFalse(ExhibitionScheduleUxPolicy.validateDraft(2, drafts + drafts))
        assertFalse(
            ExhibitionScheduleUxPolicy.validateDraft(
                2,
                listOf(ExhibitionGameDraft("", "?", true, 1), drafts[1]),
            ),
        )
    }

    @Test
    fun draftsToStubs_sortsByWeek() {
        val stubs = ExhibitionScheduleUxPolicy.draftsToStubs(
            listOf(
                ExhibitionGameDraft("b", "B", false, 2),
                ExhibitionGameDraft("a", "A", true, 1),
            ),
        )
        assertEquals(ScheduleGameKind.Exhibition, stubs.first().kind)
        assertEquals("a", stubs.first().opponentTeamId)
        assertEquals(1, stubs.first().rsWeekIndex)
    }

    @Test
    fun megaFrameId() {
        assertEquals("MEGA-FRAME-075", ExhibitionScheduleUxPolicy.MEGA_FRAME_ID)
    }
}
