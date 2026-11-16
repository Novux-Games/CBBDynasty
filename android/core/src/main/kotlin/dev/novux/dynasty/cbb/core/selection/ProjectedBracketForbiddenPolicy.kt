package dev.novux.dynasty.cbb.core.selection

/** In-season projected bracket UI is design-locked absent ([MEGA-POST-036], [MEGA-POST-156]). */
object ProjectedBracketForbiddenPolicy {
    const val MEGA_ID: String = "MEGA-POST-036"
    const val GUARD_ID: String = "MEGA-POST-156"

    fun allowsInSeasonProjectedBracket(): Boolean = false
}