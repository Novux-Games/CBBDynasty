package dev.novux.dynasty.cbb.core.awards

import kotlinx.serialization.Serializable

/** Weekly POTW archive row ([awards.md#archive--runner-ups], [MEGA-POST-078], [MEGA-POST-162]). */
@Serializable
data class AwardWeekRow(
    val seasonIndex: Int,
    val rsWeekIndex: Int,
    val conferenceId: String,
    val playerName: String,
    val schoolName: String,
    val nationalPotw: Boolean = false,
)