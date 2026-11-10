package dev.novux.dynasty.cbb.core.preseason.tournament

/** Bracket shape per catalog event ([planning/time-system.md#early-season-event-tournaments--byes]). */
object EarlySeasonEventFootprints {
    fun roundGameCounts(event: EarlySeasonEvent): List<Int> = when (event.templateId) {
        EarlySeasonEventCatalog.islandClassic.templateId,
        EarlySeasonEventCatalog.desertShootout.templateId,
        -> listOf(8, 4, 2, 1)
        EarlySeasonEventCatalog.caribbeanShowdown.templateId -> listOf(4, 2, 1)
        EarlySeasonEventCatalog.blueBloodTipOff.templateId -> listOf(2, 1)
        else -> generateRoundCounts(event.teamCount)
    }

    private fun generateRoundCounts(teamCount: Int): List<Int> {
        require(teamCount >= 2 && teamCount and (teamCount - 1) == 0) {
            "teamCount must be power of two"
        }
        val rounds = mutableListOf<Int>()
        var remaining = teamCount
        while (remaining > 1) {
            rounds += remaining / 2
            remaining /= 2
        }
        return rounds
    }
}
