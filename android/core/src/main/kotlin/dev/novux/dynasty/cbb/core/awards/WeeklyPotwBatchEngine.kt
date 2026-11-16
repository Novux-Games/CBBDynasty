package dev.novux.dynasty.cbb.core.awards

import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.pack.IdentityPackIndex

/**
 * Conference + national POTW from weekly box scores ([MEGA-POST-077], [MEGA-POST-160]–[163], [MEGA-POST-169]).
 */
object WeeklyPotwBatchEngine {
    const val MEGA_ID: String = "MEGA-POST-077"
    const val CONF_POTW_ID: String = "MEGA-POST-160"
    const val NATIONAL_POTW_ID: String = "MEGA-POST-161"
    const val PERSIST_ID: String = "MEGA-POST-162"
    const val BATCH_HOOK_ID: String = "MEGA-POST-163"
    const val FORMULA_ONLY_ID: String = "MEGA-POST-169"

    fun rowsForWeek(
        seasonIndex: Int,
        rsWeekIndex: Int,
        weekResults: List<LeagueGameResult>,
        packIndex: IdentityPackIndex,
        userConferenceId: String,
    ): List<AwardWeekRow> {
        if (weekResults.isEmpty()) return emptyList()
        val byConf = packIndex.allTeamDisplays().groupBy { it.conferenceId }
        return byConf.map { (confId, teams) ->
            val teamIds = teams.map { it.teamId }.toSet()
            val games = weekResults.filter { it.homeTeamId in teamIds || it.awayTeamId in teamIds }
            val bestTeam = games.maxByOrNull { it.homeScore + it.awayScore }?.let { g ->
                if (g.homeTeamId in teamIds) g.homeTeamId else g.awayTeamId
            } ?: teams.first().teamId
            val school = packIndex.team(bestTeam)?.displayName ?: bestTeam
            AwardWeekRow(
                seasonIndex = seasonIndex,
                rsWeekIndex = rsWeekIndex,
                conferenceId = confId,
                playerName = AwardPlaceholderNames.leadingScorer(bestTeam, school),
                schoolName = school,
                nationalPotw = confId == userConferenceId,
            )
        }
    }
}