package dev.novux.dynasty.cbb.core.coach.card.deep

import dev.novux.dynasty.cbb.core.history.DynastyHistoryArchive
import dev.novux.dynasty.cbb.core.history.HistoryYearCard
import dev.novux.dynasty.cbb.core.league.LeagueStandingsState
import dev.novux.dynasty.cbb.core.offseason.weektwo.NbaDraftOutcome
import dev.novux.dynasty.cbb.core.offseason.weektwo.NbaDraftResultLine

/** Resolves coach-card History / HOF / Draft tabs from dynasty save data ([MEGA-COACH-093]). */
object CoachCardDeepTabsEngine {
    const val MEGA_ID = "MEGA-COACH-093"

    fun coachedPlayerIds(archive: DynastyHistoryArchive, userTeamId: String): Set<String> =
        archive.careers.values
            .filter { career -> career.seasons.any { it.teamId == userTeamId } }
            .map { it.playerId }
            .toSet()

    fun build(
        dynastyId: String,
        userTeamId: String,
        schoolDisplayName: String,
        currentSeasonIndex: Int,
        currentRecordLabel: String,
        rolledSeasons: Set<Int>,
        archive: DynastyHistoryArchive,
        standingsBySeason: Map<Int, LeagueStandingsState?>,
        draftBySeason: Map<Int, List<NbaDraftResultLine>>,
    ): CoachCardDeepTabsSnapshot {
        val coached = coachedPlayerIds(archive, userTeamId)
        return CoachCardDeepTabsSnapshot(
            dynastyId = dynastyId,
            seasonHistory = seasonHistory(
                rolledSeasons = rolledSeasons,
                currentSeasonIndex = currentSeasonIndex,
                schoolDisplayName = schoolDisplayName,
                currentRecordLabel = currentRecordLabel,
                userTeamId = userTeamId,
                yearCards = archive.yearCards,
                standingsBySeason = standingsBySeason,
            ),
            hallOfFame = hallOfFame(
                coachedPlayerIds = coached,
                archive = archive,
                userTeamId = userTeamId,
            ),
            draftDepartures = draftResume(
                coachedPlayerIds = coached,
                schoolLabel = schoolDisplayName,
                draftBySeason = draftBySeason,
            ),
        )
    }

    fun seasonHistory(
        rolledSeasons: Set<Int>,
        currentSeasonIndex: Int,
        schoolDisplayName: String,
        currentRecordLabel: String,
        userTeamId: String,
        yearCards: List<HistoryYearCard>,
        standingsBySeason: Map<Int, LeagueStandingsState?>,
    ): List<CoachSeasonHistoryRow> {
        val seasons = (rolledSeasons + currentSeasonIndex).distinct().sortedDescending()
        return seasons.map { seasonIndex ->
            val yearCard = yearCards.find { it.seasonIndex == seasonIndex }
            val teamRecord = standingsBySeason[seasonIndex]?.records?.get(userTeamId)
            val recordLabel = when {
                teamRecord != null -> "${teamRecord.wins}-${teamRecord.losses}"
                seasonIndex == currentSeasonIndex -> currentRecordLabel
                else -> "—"
            }
            val highlights = buildList {
                yearCard?.recordsNoted?.let { addAll(it) }
                if (yearCard?.ncbaChampionSchool == schoolDisplayName) {
                    add("NCBA champion")
                }
            }
            val postseason = yearCard?.ncbaMvpLine
                ?: yearCard?.recordsNoted?.firstOrNull()
                ?: "—"
            CoachSeasonHistoryRow(
                seasonIndex = seasonIndex,
                schoolDisplayName = schoolDisplayName,
                recordLabel = recordLabel,
                postseasonSummary = postseason,
                highlightLabels = highlights,
            )
        }
    }

    fun hallOfFame(
        coachedPlayerIds: Set<String>,
        archive: DynastyHistoryArchive,
        userTeamId: String,
    ): List<CoachHofResumeEntry> {
        val national = archive.goatSnapshot
            .filter { it.playerId in coachedPlayerIds && it.nationalHof }
            .map { goat ->
                CoachHofResumeEntry(
                    playerId = goat.playerId,
                    displayName = goat.displayName,
                    schoolLabel = goat.schoolLabel,
                    honorSeasonIndex = nationalInductionSeason(goat.displayName, archive.yearCards)
                        ?: goat.seasonFromChips(),
                    isNational = true,
                )
            }
        val conference = archive.careers.values.flatMap { career ->
            if (career.playerId !in coachedPlayerIds) return@flatMap emptyList()
            career.seasons
                .filter { it.teamId == userTeamId && it.conferencePoty }
                .map { line ->
                    CoachHofResumeEntry(
                        playerId = career.playerId,
                        displayName = career.displayName,
                        schoolLabel = career.seasons.lastOrNull { it.teamId == userTeamId }?.displayName
                            ?: career.displayName,
                        honorSeasonIndex = line.seasonIndex,
                        isNational = false,
                    )
                }
        }
        return (national + conference)
            .distinctBy { "${it.playerId}:${it.isNational}:${it.honorSeasonIndex}" }
            .sortedWith(
                compareByDescending<CoachHofResumeEntry> { it.isNational }
                    .thenBy { it.honorSeasonIndex },
            )
    }

    fun draftResume(
        coachedPlayerIds: Set<String>,
        schoolLabel: String,
        draftBySeason: Map<Int, List<NbaDraftResultLine>>,
    ): List<CoachDraftResumeEntry> =
        draftBySeason.flatMap { (seasonIndex, lines) ->
            lines.filter { it.playerId in coachedPlayerIds }.map { line ->
                CoachDraftResumeEntry(
                    playerId = line.playerId,
                    displayName = line.displayLabel,
                    draftSeasonIndex = seasonIndex,
                    pickSummary = line.pickSummary,
                    schoolLabel = schoolLabel,
                    drafted = line.outcome == NbaDraftOutcome.Drafted,
                )
            }
        }.sortedWith(
            compareByDescending<CoachDraftResumeEntry> { it.drafted }
                .thenByDescending { it.draftSeasonIndex }
                .thenBy { it.displayName },
        )

    private fun nationalInductionSeason(displayName: String, yearCards: List<HistoryYearCard>): Int? =
        yearCards.firstOrNull { displayName in it.nationalHofInductees }?.seasonIndex

    private fun dev.novux.dynasty.cbb.core.history.GoatListEntry.seasonFromChips(): Int =
        chips.firstNotNullOfOrNull { chip ->
            Regex("""(\d{4})""").find(chip)?.groupValues?.getOrNull(1)?.toIntOrNull()
        } ?: 0
}
