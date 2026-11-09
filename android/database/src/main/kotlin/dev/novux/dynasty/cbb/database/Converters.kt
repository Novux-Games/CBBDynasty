package dev.novux.dynasty.cbb.database

import androidx.room.TypeConverter
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

class Converters {
    @TypeConverter
    fun fromPhase(value: String): CalendarPhase = CalendarPhase.valueOf(value)

    @TypeConverter
    fun phaseToString(phase: CalendarPhase): String = phase.name
}
