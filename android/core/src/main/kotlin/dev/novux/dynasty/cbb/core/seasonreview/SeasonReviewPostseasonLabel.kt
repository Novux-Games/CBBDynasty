package dev.novux.dynasty.cbb.core.seasonreview

import dev.novux.dynasty.cbb.core.brackets.NationalBracketSnapshot
import dev.novux.dynasty.cbb.core.brackets.NcbaMainBracketSnapshot
import dev.novux.dynasty.cbb.core.brackets.NcbaOpeningRoundSnapshot
import dev.novux.dynasty.cbb.core.calendar.PostseasonCalendar
import dev.novux.dynasty.cbb.core.league.LeagueGameResult
import dev.novux.dynasty.cbb.core.selection.SelectionSundaySnapshot

object SeasonReviewPostseasonLabel {
    fun forUserTeam(
        userTeamId: String,
        selection: SelectionSundaySnapshot?,
        opening: NcbaOpeningRoundSnapshot?,
        main: NcbaMainBracketSnapshot?,
        nci: NationalBracketSnapshot?,
        heritage: NationalBracketSnapshot?,
    ): String {
        if (selection == null) return "Season complete"
        val inNcba = selection.ncbaField.any { it.teamId == userTeamId }
        val inNci = selection.nciField.any { it.teamId == userTeamId }
        val inHc = selection.heritageField.any { it.teamId == userTeamId }
        if (!inNcba && !inNci && !inHc) return "Season complete"

        main?.championTeamId?.takeIf { it == userTeamId }?.let { return "NCBA National Champion" }
        nci?.championTeamId?.takeIf { it == userTeamId }?.let { return "NCI Champion" }
        heritage?.championTeamId?.takeIf { it == userTeamId }?.let { return "Heritage Champion" }

        if (inNcba && main != null) {
            val lastMainWeek = main.gameResults
                .filter { userTeamId == it.homeTeamId || userTeamId == it.awayTeamId }
                .maxOfOrNull { it.rsWeekIndex }
            if (lastMainWeek != null) {
                val lost = main.gameResults.any { game ->
                    game.rsWeekIndex == lastMainWeek &&
                        (userTeamId == game.homeTeamId || userTeamId == game.awayTeamId) &&
                        loserOf(game) == userTeamId
                }
                if (lost) {
                    return PostseasonCalendar.ncbaRoundLabel(lastMainWeek).let { "NCBA — eliminated $it" }
                }
            }
            if (main.completed && main.championTeamId != userTeamId) {
                return "NCBA — Final Four or later"
            }
        }

        if (inNcba && opening != null) {
            val playedOpening = opening.gameResults.any { userTeamId == it.homeTeamId || userTeamId == it.awayTeamId }
            if (playedOpening && opening.advancingTeamIds.contains(userTeamId).not()) {
                return "NCBA — eliminated Opening Round"
            }
        }

        if (inNci && nci != null) {
            return invitationalExit(userTeamId, nci, "NCI")
        }
        if (inHc && heritage != null) {
            return invitationalExit(userTeamId, heritage, "Heritage")
        }
        return "Postseason participant"
    }

    private fun invitationalExit(userTeamId: String, bracket: NationalBracketSnapshot, label: String): String {
        val lastWeek = bracket.gameResults
            .filter { userTeamId == it.homeTeamId || userTeamId == it.awayTeamId }
            .maxOfOrNull { it.rsWeekIndex }
        if (lastWeek == null) return "$label participant"
        val lost = bracket.gameResults.any { game ->
            game.rsWeekIndex == lastWeek &&
                (userTeamId == game.homeTeamId || userTeamId == game.awayTeamId) &&
                loserOf(game) == userTeamId
        }
        return if (lost) "$label — eliminated week $lastWeek" else "$label — active"
    }

    private fun loserOf(game: LeagueGameResult): String =
        if (game.homeScore > game.awayScore) game.awayTeamId else game.homeTeamId
}
