package dev.novux.dynasty.cbb.core.coach.skill

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.CalendarPosition
import org.junit.Assert.assertEquals
import org.junit.Test

class CoachSkillPointStateReducerTest {
    @Test
    fun grantsOncePerWeekInRegularSeason() {
        val position = CalendarPosition(1, CalendarPhase.RegularSeason, 3)
        val first = CoachSkillPointStateReducer.ensureSundayGrant(CoachSkillPointState(), position)
        assertEquals(1, first.unspentBank)
        val second = CoachSkillPointStateReducer.ensureSundayGrant(first, position)
        assertEquals(1, second.unspentBank)
    }

    @Test
    fun skipsOffseason() {
        val position = CalendarPosition(1, CalendarPhase.Offseason, 2)
        val next = CoachSkillPointStateReducer.ensureSundayGrant(CoachSkillPointState(), position)
        assertEquals(0, next.unspentBank)
    }
}
