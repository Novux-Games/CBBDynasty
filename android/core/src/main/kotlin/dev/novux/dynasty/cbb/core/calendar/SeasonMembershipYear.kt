package dev.novux.dynasty.cbb.core.calendar

/** Membership year labels (e.g. **2026–27** for season index 1) — [planning/game-start.md#save-bootstrap]. */
object SeasonMembershipYear {
    const val FIRST_SEASON_START_YEAR = 2026

    fun label(seasonIndex: Int): String {
        require(seasonIndex >= 1) { "seasonIndex must be >= 1" }
        val startYear = FIRST_SEASON_START_YEAR + seasonIndex - 1
        val endTwoDigit = (startYear + 1) % 100
        return "$startYear-${endTwoDigit.toString().padStart(2, '0')}"
    }
}
