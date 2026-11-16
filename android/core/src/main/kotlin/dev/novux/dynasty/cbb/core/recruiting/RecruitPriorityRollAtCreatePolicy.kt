package dev.novux.dynasty.cbb.core.recruiting

/**
 * HS/JUCO prospects roll 1–2 top priorities at creation via [RecruitPriorityRoll] ([MEGA-RECR-011]).
 */
object RecruitPriorityRollAtCreatePolicy {
    const val MEGA_ID: String = "MEGA-RECR-011"

    fun rollAtCreate(prospectId: String): List<RecruitPriority> = RecruitPriorityRoll.roll(prospectId)
}