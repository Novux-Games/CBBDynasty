package dev.novux.dynasty.cbb.core.awards

import dev.novux.dynasty.cbb.core.league.LeagueStandingsState
import dev.novux.dynasty.cbb.core.pack.IdentityPackIndex
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import dev.novux.dynasty.cbb.core.seasonreview.SeasonReviewRecordBuilder

data class YearlyAwardsWrapInput(
    val seasonIndex: Int,
    val userTeamId: String,
    val userConferenceId: String,
    val userSchoolName: String,
    val userCoachDisplayName: String,
    val userRoster: List<RosterPlayer>,
    val standings: LeagueStandingsState,
    val packIndex: IdentityPackIndex,
    val winSharesByPlayer: Map<String, Double> = emptyMap(),
)

/** End-of-season honors wrap — POTY + coach awards ([planning/awards.md], step 14.2). */
object YearlyAwardsWrapEngine {
    private val positionLabels = listOf("PG", "SG", "SF", "PF", "C")

    fun compute(input: YearlyAwardsWrapInput): YearlyAwardsSeasonArchive {
        val teamMvp = input.userRoster.maxWithOrNull(
            compareBy<RosterPlayer> { input.winSharesByPlayer[it.playerId] ?: 0.0 }
                .thenBy { it.ovr }
                .thenByDescending { it.depthOrder },
        )
        val userTeamMvp = teamMvp?.let {
            AwardHonorLine(
                awardLabel = "Team MVP",
                winnerName = it.displayName.ifBlank { it.playerId },
                schoolName = input.userSchoolName,
            )
        }
        val netChamp = input.standings.netRankByTeam.minByOrNull { it.value }?.key
        val bracketChamp = input.standings.ncbaMainBracket?.championTeamId
        val mvpTeamId = bracketChamp ?: netChamp ?: input.userTeamId
        val mvpSchool = schoolName(input.packIndex, mvpTeamId)
        val mvpPlayerName = when {
            mvpTeamId == input.userTeamId && teamMvp != null ->
                teamMvp.displayName.ifBlank { teamMvp.playerId }
            else -> AwardPlaceholderNames.leadingScorer(mvpTeamId, mvpSchool)
        }
        val runnerTeam = input.standings.netRankByTeam
            .entries
            .sortedBy { it.value }
            .map { it.key }
            .firstOrNull { it != mvpTeamId }
        val runnerSchool = runnerTeam?.let { schoolName(input.packIndex, it) }
        val runnerPlayer = runnerTeam?.let { AwardPlaceholderNames.leadingScorer(it, runnerSchool!!) }
        val ncbaMvp = AwardHonorLine(
            awardLabel = "NCBA MVP",
            winnerName = mvpPlayerName,
            schoolName = mvpSchool,
            runnerUpName = runnerPlayer,
        )
        val coachTeamId = input.standings.records
            .maxWithOrNull(compareBy<Map.Entry<String, dev.novux.dynasty.cbb.core.league.LeagueTeamRecord>> { it.value.wins }
                .thenBy { -it.value.losses })
            ?.key ?: mvpTeamId
        val coachSchool = schoolName(input.packIndex, coachTeamId)
        val coachWinner = if (coachTeamId == input.userTeamId) {
            input.userCoachDisplayName
        } else {
            AwardPlaceholderNames.coachHonorName(coachSchool)
        }
        val coachRunnerTeam = input.standings.records.entries
            .sortedWith(compareByDescending<Map.Entry<String, dev.novux.dynasty.cbb.core.league.LeagueTeamRecord>> { it.value.wins }
                .thenBy { it.value.losses })
            .map { it.key }
            .firstOrNull { it != coachTeamId }
        val coachRunner = coachRunnerTeam?.let {
            val school = schoolName(input.packIndex, it)
            if (it == input.userTeamId) input.userCoachDisplayName else AwardPlaceholderNames.coachHonorName(school)
        }
        val ncbaCoach = AwardHonorLine(
            awardLabel = "NCBA Coach of the Year",
            winnerName = coachWinner,
            schoolName = coachSchool,
            runnerUpName = coachRunner,
        )
        val ncbaOverview = listOf(ncbaMvp, ncbaCoach)
        val byPosition = positionLabels.associateWith { pos ->
            listOf(
                AwardHonorLine(
                    awardLabel = "NCBA $pos POY",
                    winnerName = AwardPlaceholderNames.leadingScorer("$mvpTeamId-$pos", mvpSchool),
                    schoolName = mvpSchool,
                ),
            )
        }
        val conferenceAwards = buildConferenceAwards(input)
        return YearlyAwardsSeasonArchive(
            seasonIndex = input.seasonIndex,
            ncbaOverview = ncbaOverview,
            ncbaMvp = listOf(ncbaMvp),
            ncbaCoaches = listOf(ncbaCoach),
            ncbaByPosition = byPosition,
            conferenceAwards = conferenceAwards,
            userTeamMvp = userTeamMvp,
        )
    }

    private fun buildConferenceAwards(input: YearlyAwardsWrapInput): Map<String, List<AwardHonorLine>> {
        val byConf = input.packIndex.allTeamDisplays().groupBy { it.conferenceId }
        return byConf.mapValues { (confId, teams) ->
            val ranked = teams.map { team ->
                val record = SeasonReviewRecordBuilder.build(team.teamId, input.standings, input.packIndex)
                team.teamId to record
            }.sortedWith(
                compareByDescending<Pair<String, dev.novux.dynasty.cbb.core.school.SchoolSeasonRecord>> { it.second.confWins }
                    .thenBy { it.second.confLosses },
            )
            val top = ranked.firstOrNull() ?: return@mapValues emptyList()
            val second = ranked.getOrNull(1)
            val topSchool = schoolName(input.packIndex, top.first)
            val poyName = when {
                top.first == input.userTeamId -> {
                    input.userRoster.maxWithOrNull(compareBy<RosterPlayer> { it.ovr })
                        ?.displayName?.ifBlank { null }
                        ?: AwardPlaceholderNames.leadingScorer(top.first, topSchool)
                }
                else -> AwardPlaceholderNames.leadingScorer(top.first, topSchool)
            }
            val confName = input.packIndex.conference(confId)?.displayName ?: confId
            val poy = AwardHonorLine(
                awardLabel = "Conference POY",
                winnerName = poyName,
                schoolName = topSchool,
                runnerUpName = second?.let { s ->
                    AwardPlaceholderNames.leadingScorer(
                        s.first,
                        schoolName(input.packIndex, s.first),
                    )
                },
            )
            val coachWinner = if (top.first == input.userTeamId) {
                input.userCoachDisplayName
            } else {
                AwardPlaceholderNames.coachHonorName(topSchool)
            }
            val coach = AwardHonorLine(
                awardLabel = "Conference Coach of the Year",
                winnerName = coachWinner,
                schoolName = topSchool,
            )
            listOf(poy, coach)
        }
    }

    private fun schoolName(index: IdentityPackIndex, teamId: String): String =
        index.team(teamId)?.displayName ?: teamId
}
