package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.preseason.tournament.EarlySeasonBracketEngine
import dev.novux.dynasty.cbb.core.selection.NcbaBidType
import dev.novux.dynasty.cbb.core.selection.NcbaSelectionEntry
import dev.novux.dynasty.cbb.core.selection.SelectionSundaySnapshot

/** Opening Round pairings 1v24 … 12v13 by NET within the bubble ([planning/postseason.md#opening-round]). */
object NcbaOpeningRoundPairing {
    const val OPENING_TEAM_COUNT = 24
    const val OPENING_GAME_COUNT = 12

    fun openingInvitees(selection: SelectionSundaySnapshot): List<NcbaSelectionEntry> =
        selection.ncbaField
            .filter { it.bidType == NcbaBidType.OpeningRound }
            .sortedWith(compareBy({ it.netRank }, { it.teamId }))

    fun pairings(selection: SelectionSundaySnapshot): List<Pair<NcbaSelectionEntry, NcbaSelectionEntry>> {
        val invitees = openingInvitees(selection)
        require(invitees.size == OPENING_TEAM_COUNT) {
            "Opening Round requires $OPENING_TEAM_COUNT teams, found ${invitees.size}"
        }
        val teamIds = invitees.map { it.teamId }
        val pairs = EarlySeasonBracketEngine.openingRoundPairings(teamIds)
        val byId = invitees.associateBy { it.teamId }
        return pairs.map { (home, away) ->
            byId.getValue(home) to byId.getValue(away)
        }
    }

    fun bubbleRank(invitees: List<NcbaSelectionEntry>, teamId: String): Int =
        invitees.indexOfFirst { it.teamId == teamId } + 1

    fun buildMatchups(selection: SelectionSundaySnapshot): List<NcbaOpeningRoundMatchup> {
        val invitees = openingInvitees(selection)
        return pairings(selection).map { (home, away) ->
            NcbaOpeningRoundMatchup(
                homeTeamId = home.teamId,
                awayTeamId = away.teamId,
                bubbleRankHome = bubbleRank(invitees, home.teamId),
                bubbleRankAway = bubbleRank(invitees, away.teamId),
                ncbaSeedHome = home.ncbaSeed,
                ncbaSeedAway = away.ncbaSeed,
            )
        }
    }
}
