package dev.novux.dynasty.cbb.core.offseason.weeknine

data class ProgramEconomyState(
    val cashUsd: Int = OffseasonWeekNineConstants.STARTING_CASH_USD,
    val nilBoostUsd: Int = 0,
    val facilityGrades: FacilityGrades = FacilityGrades(),
    val completedEvents: Set<FundraisingEventType> = emptySet(),
    val namingRightsSold: Boolean = false,
)
