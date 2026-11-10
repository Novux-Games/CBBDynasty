package dev.novux.dynasty.cbb.core.portal

/** National D-I program count — matches default identity pack. */
object PortalConstants {
    const val DI_PROGRAM_COUNT = 365
    const val CONSIDERING_POOL_MIN = 1_000
    const val CONSIDERING_POOL_MAX = 1_400
    const val TOP_ENTRANTS_RANKED = 40
    const val ENTRANTS_PER_TEAM_MIN = 3.0
    const val ENTRANTS_PER_TEAM_MAX = 4.0
    val PORTAL_RECRUITING_WEEKS: IntRange = 2..5
}
