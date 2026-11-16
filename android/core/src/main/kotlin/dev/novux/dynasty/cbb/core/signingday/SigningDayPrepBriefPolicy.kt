package dev.novux.dynasty.cbb.core.signingday

/** Offseason prep copy before Signing Day week ([MEGA-RECR-112]). */
object SigningDayPrepBriefPolicy {
    const val MEGA_ID: String = "MEGA-RECR-112"

    val prepWeeks: IntRange = 5..SigningDayConstants.OFFSEASON_WEEK_INDEX

    fun showPrepBrief(offseasonWeekIndex: Int, signingCrunchActive: Boolean): Boolean =
        offseasonWeekIndex in prepWeeks && signingCrunchActive

    fun briefFormatArgs(offseasonWeekIndex: Int): List<Int> =
        listOf(offseasonWeekIndex, SigningDayConstants.OFFSEASON_WEEK_INDEX)
}