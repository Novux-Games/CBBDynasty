package dev.novux.dynasty.cbb.core.brackets

import dev.novux.dynasty.cbb.core.selection.NcbaBidType
import dev.novux.dynasty.cbb.core.selection.NcbaSelectionEntry
import dev.novux.dynasty.cbb.core.selection.SelectionSundaySnapshot

object NcbaFieldBuilder {
    fun fieldOf64(
        selection: SelectionSundaySnapshot,
        openingRound: NcbaOpeningRoundSnapshot,
    ): List<NcbaSelectionEntry> {
        require(openingRound.completed) { "Opening Round must be complete before main bracket" }
        val winners = openingRound.winnerTeamIds.toSet()
        val field = selection.ncbaField
            .filter { entry ->
                entry.bidType != NcbaBidType.OpeningRound || entry.teamId in winners
            }
            .sortedBy { it.ncbaSeed }
        require(field.size == 64) { "expected 64 NCBA main bracket teams, found ${field.size}" }
        return field
    }
}
