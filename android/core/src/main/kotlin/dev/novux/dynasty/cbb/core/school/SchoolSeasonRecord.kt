package dev.novux.dynasty.cbb.core.school

data class SchoolSeasonRecord(
    val wins: Int,
    val losses: Int,
    val confWins: Int,
    val confLosses: Int,
) {
    val overallLabel: String get() = "$wins-$losses"
    val conferenceLabel: String get() = "$confWins-$confLosses"

    companion object {
        val Zero = SchoolSeasonRecord(0, 0, 0, 0)
    }
}
