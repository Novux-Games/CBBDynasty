package dev.novux.dynasty.cbb.core.history

/** GOAT by position (PG–C) in History hub ([MEGA-POST-092], [MEGA-POST-187], [MEGA-POST-186]). */
object HistoryGoatByPositionSurfacePolicy {
    const val MEGA_ID: String = "MEGA-POST-092"
    const val TAB_DEPTH_ID: String = "MEGA-POST-187"
    const val HUB_TABS_ID: String = "MEGA-POST-186"

    val positionOrder: List<String> = listOf("PG", "SG", "SF", "PF", "C")

    fun topFiveByPosition(snapshot: HistoryHubSnapshot): Map<String, List<GoatListEntry>> =
        snapshot.goatByPosition.ifEmpty {
            emptyMap()
        }
}