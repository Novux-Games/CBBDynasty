package dev.novux.dynasty.cbb.core.history

import kotlinx.serialization.Serializable

@Serializable
data class PlayerSeasonHistoryLine(
    val playerId: String,
    val seasonIndex: Int,
    val teamId: String,
    val displayName: String,
    val winSharesSeason: Double,
    val peakOvrSeason: Int,
    val minutesSeason: Int,
    val nationalPoty: Boolean = false,
    val conferencePoty: Boolean = false,
    val allAmericaTier: Int = 0,
    val nationalTitle: Boolean = false,
)

@Serializable
data class PlayerCareerHistory(
    val playerId: String,
    val displayName: String,
    val finalTeamId: String,
    val seasons: List<PlayerSeasonHistoryLine>,
) {
    val careerWinShares: Double get() = seasons.sumOf { it.winSharesSeason }
    val nationalTitles: Int get() = seasons.count { it.nationalTitle }
    val peakOvrCareer: Int get() = seasons.maxOfOrNull { it.peakOvrSeason } ?: 0
    val nationalPotySeasons: Int get() = seasons.count { it.nationalPoty }
    val confPotySeasons: Int get() = seasons.count { it.conferencePoty }
    val allAmericaPoints: Int get() = seasons.sumOf { seasonPoints(it.allAmericaTier) }

    private fun seasonPoints(tier: Int): Int = when (tier) {
        1 -> 3
        2 -> 2
        3 -> 1
        else -> 0
    }
}

@Serializable
data class GoatListEntry(
    val rank: Int,
    val playerId: String,
    val displayName: String,
    val schoolLabel: String,
    val goatScore: Double,
    val careerWinShares: Double,
    val nationalHof: Boolean,
    val chips: List<String>,
)

@Serializable
data class HistoryYearCard(
    val seasonIndex: Int,
    val ncbaChampionSchool: String?,
    val ncbaMvpLine: String?,
    val nationalHofInductees: List<String>,
    val goatNumberOne: String?,
    val recordsNoted: List<String> = emptyList(),
)

@Serializable
data class DynastyHistoryArchive(
    val dynastyId: String,
    val careers: Map<String, PlayerCareerHistory> = emptyMap(),
    val yearCards: List<HistoryYearCard> = emptyList(),
    val goatSnapshot: List<GoatListEntry> = emptyList(),
    val goatByPosition: Map<String, List<GoatListEntry>> = emptyMap(),
)

@Serializable
data class HistoryHubSnapshot(
    val dynastyId: String,
    val seasonIndex: Int,
    val yearCards: List<HistoryYearCard>,
    val goatOverall: List<GoatListEntry>,
    val goatByPosition: Map<String, List<GoatListEntry>>,
)
