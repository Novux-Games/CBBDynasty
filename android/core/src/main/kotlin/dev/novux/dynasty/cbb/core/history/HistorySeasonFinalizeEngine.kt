package dev.novux.dynasty.cbb.core.history

import dev.novux.dynasty.cbb.core.awards.YearlyAwardsSeasonArchive
import dev.novux.dynasty.cbb.core.league.LeagueStandingsState
import dev.novux.dynasty.cbb.core.pack.IdentityPackIndex
import dev.novux.dynasty.cbb.core.roster.RosterPlayer

object HistorySeasonFinalizeEngine {
    fun finalizeSeason(
        dynastyId: String,
        existing: DynastyHistoryArchive,
        seasonIndex: Int,
        userTeamId: String,
        roster: List<RosterPlayer>,
        standings: LeagueStandingsState,
        awards: YearlyAwardsSeasonArchive,
        packIndex: IdentityPackIndex,
        tuning: BalanceHistoryTuning,
    ): DynastyHistoryArchive {
        val teamWins = standings.recordFor(userTeamId).wins
        val winShares = WinContributionEngine.allocateTeamWins(roster, teamWins, tuning.winContribution)
        val ncbaMvpTeam = awards.ncbaMvp.firstOrNull()
        val userConfPoy = awards.conferenceAwards[packIndex.team(userTeamId)?.conferenceId.orEmpty()]
            ?.firstOrNull { it.awardLabel.contains("POY", ignoreCase = true) }
        val championId = standings.ncbaMainBracket?.championTeamId
        val userWonTitle = championId == userTeamId
        val seasonLines = roster.map { player ->
            val nationalPoty = ncbaMvpTeam?.winnerName == player.displayName.ifBlank { player.playerId }
            val confPoty = userConfPoy?.winnerName == player.displayName.ifBlank { player.playerId }
            PlayerSeasonHistoryLine(
                playerId = player.playerId,
                seasonIndex = seasonIndex,
                teamId = userTeamId,
                displayName = player.displayName.ifBlank { player.playerId },
                winSharesSeason = winShares[player.playerId] ?: 0.0,
                peakOvrSeason = player.ovr,
                minutesSeason = player.rsGamesPlayedSeason.coerceAtLeast(1) * if (player.isStarter) 28 else 12,
                nationalPoty = nationalPoty,
                conferencePoty = confPoty,
                nationalTitle = userWonTitle,
            )
        }
        val careers = existing.careers.toMutableMap()
        seasonLines.forEach { line ->
            val prior = careers[line.playerId]
            val mergedSeasons = (prior?.seasons.orEmpty() + line)
            careers[line.playerId] = PlayerCareerHistory(
                playerId = line.playerId,
                displayName = line.displayName,
                finalTeamId = userTeamId,
                seasons = mergedSeasons,
            )
        }
        val goatCandidates = careers.values
            .filter { GoatScoreEngine.eligible(it) }
            .map { career ->
                val score = GoatScoreEngine.score(career, tuning.goatWeights)
                Triple(career, score, packIndex.team(career.finalTeamId)?.displayName ?: career.finalTeamId)
            }
            .sortedWith(
                compareByDescending<Triple<PlayerCareerHistory, Double, String>> { it.second }
                    .thenByDescending { it.first.careerWinShares }
                    .thenBy { it.first.playerId },
            )
        val goatSnapshot = goatCandidates.take(25).mapIndexed { index, (career, score, school) ->
            val hof = score >= tuning.nationalHofMinGoatScore || career.nationalPotySeasons > 0
            GoatScoreEngine.buildGoatEntry(index + 1, career, school, score, hof)
        }
        val byPosition = roster
            .groupBy { it.position.name }
            .mapValues { (_, players) ->
                val ids = players.map { it.playerId }.toSet()
                goatSnapshot.filter { it.playerId in ids }.take(5)
            }
        val newHof = goatSnapshot.filter { it.nationalHof }.map { it.displayName }
        val yearCard = HistoryYearCard(
            seasonIndex = seasonIndex,
            ncbaChampionSchool = championId?.let { packIndex.team(it)?.displayName },
            ncbaMvpLine = ncbaMvpTeam?.let { "${it.winnerName} (${it.schoolName})" },
            nationalHofInductees = newHof,
            goatNumberOne = goatSnapshot.firstOrNull()?.displayName,
        )
        val yearCards = (existing.yearCards.filter { it.seasonIndex != seasonIndex } + yearCard)
            .sortedBy { it.seasonIndex }
        return DynastyHistoryArchive(
            dynastyId = dynastyId,
            careers = careers,
            yearCards = yearCards,
            goatSnapshot = goatSnapshot,
            goatByPosition = byPosition,
        )
    }

    fun toHubSnapshot(archive: DynastyHistoryArchive, seasonIndex: Int): HistoryHubSnapshot =
        HistoryHubSnapshot(
            dynastyId = archive.dynastyId,
            seasonIndex = seasonIndex,
            yearCards = archive.yearCards,
            goatOverall = archive.goatSnapshot,
            goatByPosition = archive.goatByPosition,
        )
}
