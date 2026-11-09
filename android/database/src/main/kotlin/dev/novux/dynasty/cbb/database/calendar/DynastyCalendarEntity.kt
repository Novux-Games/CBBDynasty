package dev.novux.dynasty.cbb.database.calendar

import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

@Entity(tableName = "dynasty_calendar")
data class DynastyCalendarEntity(
    @PrimaryKey val dynastyId: String,
    val seasonIndex: Int,
    val phase: CalendarPhase,
    val activeWeekIndex: Int,
    val committedSeasonIndex: Int?,
    val committedPhase: CalendarPhase?,
    val committedWeekIndex: Int?,
)
