package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

/**
 * After hard sign, recruiting hub is browse-only on Committed / Lost tabs ([MEGA-RECR-113]).
 */
object RecruitingHubReadOnlyCyclePolicy {
    const val MEGA_ID: String = "MEGA-RECR-113"

    val tabs: List<RecruitingHubTab> = listOf(
        RecruitingHubTab.Committed,
        RecruitingHubTab.Lost,
    )

    fun isReadOnly(
        phase: CalendarPhase,
        offseasonWeekIndex: Int?,
        ledgerCycleClosed: Boolean,
    ): Boolean =
        RecruitingCyclePolicy.hubAccessible(phase, offseasonWeekIndex) &&
            RecruitingCyclePolicy.effectiveCycleClosed(phase, offseasonWeekIndex, ledgerCycleClosed)

    fun allowsSpend(isReadOnly: Boolean): Boolean = !isReadOnly
}