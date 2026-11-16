package dev.novux.dynasty.cbb.core.calendar

/** NCBA round labels on postseason calendar rows ([MEGA-POST-003], [MEGA-POST-114], [MEGA-NOTES-018]). */
object PostseasonCalendarGameRoundPolicy {
    const val MEGA_ID: String = "MEGA-POST-003"
    const val SUB_TAB_ROUND_ID: String = "MEGA-POST-114"
    const val NOTES_CALENDAR_ID: String = "MEGA-NOTES-018"

    fun roundLabelForPostseasonWeek(postseasonWeek: Int): String? =
        if (postseasonWeek > 0) PostseasonCalendar.ncbaRoundLabel(postseasonWeek) else null
}