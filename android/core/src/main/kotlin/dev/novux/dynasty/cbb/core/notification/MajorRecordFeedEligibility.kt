package dev.novux.dynasty.cbb.core.notification

/**
 * Major national record feed eligibility ([planning/history-hub.md#major-record-feed-eligible]).
 */
object MajorRecordFeedEligibility {
    enum class StatKind {
        Points,
        Rebounds,
        Assists,
        Steals,
        Blocks,
        ThreePointers,
        Ppg,
    }

    fun isFeedEligible(
        stat: StatKind,
        value: Double,
        isNewNationalRecord: Boolean,
    ): Boolean {
        if (stat == StatKind.Points && value >= 50) return true
        if (!isNewNationalRecord) return false
        return when (stat) {
            StatKind.Points,
            StatKind.Rebounds,
            StatKind.Assists,
            StatKind.Steals,
            StatKind.Blocks,
            StatKind.ThreePointers,
            StatKind.Ppg,
            -> value > 0
        }
    }
}
