package dev.novux.dynasty.cbb.core.injuries

/**
 * Active injury on a program — [planning/injuries.md#duration--availability].
 */
data class InjuryInstance(
    val playerId: String,
    val injuryId: String,
    val weeksRemaining: Int,
) {
    init {
        require(weeksRemaining > 0) { "weeksRemaining must be > 0 while injured" }
    }
}
