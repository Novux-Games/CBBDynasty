package dev.novux.dynasty.cbb.core.school

/** Conference standing line prefers live W-L when standings exist ([MEGA-WORLD-113]). */
object SchoolHubConferenceStandingPolicy {
    const val MEGA_ID = "MEGA-WORLD-113"

    fun label(record: SchoolSeasonRecord, prestigeOrderIndex: Int, conferenceSize: Int): String? {
        if (record.confWins + record.confLosses > 0) {
            return "${record.conferenceLabel} conference"
        }
        if (prestigeOrderIndex < 0) return null
        return SchoolMetricsEstimator.conferenceStandingPlaceholder(prestigeOrderIndex + 1, conferenceSize)
    }
}