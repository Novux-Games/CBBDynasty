package dev.novux.dynasty.cbb.core.preseason.tournament

data class EarlySeasonEvent(
    val templateId: String,
    val displayName: String,
    val rsGameSpots: Int,
    val teamCount: Int,
    val rsCalendarWeeks: Int,
) {
    val rsWeekIndices: List<Int>
        get() = (1..rsCalendarWeeks).toList()

    val roundGameCounts: List<Int>
        get() = EarlySeasonEventFootprints.roundGameCounts(this)
}

object EarlySeasonEventCatalog {
    val islandClassic = EarlySeasonEvent("evt-island-classic", "The Island Classic", 4, 16, 4)
    val caribbeanShowdown = EarlySeasonEvent("evt-caribbean-showdown", "The Caribbean Showdown", 3, 8, 3)
    val blueBloodTipOff = EarlySeasonEvent("evt-blue-blood-tipoff", "The Blue Blood Tip-Off", 2, 4, 2)
    val desertShootout = EarlySeasonEvent("evt-desert-shootout", "The Desert Shootout", 4, 16, 4)

    val all: List<EarlySeasonEvent> = listOf(
        islandClassic,
        caribbeanShowdown,
        blueBloodTipOff,
        desertShootout,
    )

    fun byId(id: String): EarlySeasonEvent? = all.firstOrNull { it.templateId == id }
}
