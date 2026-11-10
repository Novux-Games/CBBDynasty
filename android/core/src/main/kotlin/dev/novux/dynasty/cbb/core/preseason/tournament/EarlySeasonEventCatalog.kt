package dev.novux.dynasty.cbb.core.preseason.tournament

data class EarlySeasonEvent(
    val templateId: String,
    val displayName: String,
    val rsGameSpots: Int,
)

object EarlySeasonEventCatalog {
    val islandClassic = EarlySeasonEvent("evt-island-classic", "The Island Classic", 4)
    val caribbeanShowdown = EarlySeasonEvent("evt-caribbean-showdown", "The Caribbean Showdown", 3)
    val blueBloodTipOff = EarlySeasonEvent("evt-blue-blood-tipoff", "The Blue Blood Tip-Off", 2)
    val desertShootout = EarlySeasonEvent("evt-desert-shootout", "The Desert Shootout", 4)

    val all: List<EarlySeasonEvent> = listOf(
        islandClassic,
        caribbeanShowdown,
        blueBloodTipOff,
        desertShootout,
    )

    fun byId(id: String): EarlySeasonEvent? = all.firstOrNull { it.templateId == id }
}
