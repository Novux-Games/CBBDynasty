package dev.novux.dynasty.cbb.core.news

import dev.novux.dynasty.cbb.core.offseason.weektwo.NbaDraftOutcome
import dev.novux.dynasty.cbb.core.offseason.weektwo.NbaDraftResultLine
import dev.novux.dynasty.cbb.core.signingday.NationalRankRow

/** In-season Draft tab shows watch-list board until week-2 results publish ([MEGA-SHELL-155]). */
object NewsHubDraftInSeasonBoardPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-155"

    const val IN_SEASON_MOCK_TIER: String = "Watch list"

    fun fallbackLines(
        publishedResults: List<NbaDraftResultLine>,
        topRecruits: List<NationalRankRow>,
        inRegularSeason: Boolean,
    ): List<NbaDraftResultLine> {
        if (publishedResults.isNotEmpty()) return publishedResults
        if (!inRegularSeason || topRecruits.isEmpty()) return emptyList()
        return topRecruits.take(10).map { row ->
            NbaDraftResultLine(
                playerId = row.prospect.prospectId,
                displayLabel = row.prospect.displayName,
                stockPoints = (row.prospect.stars * 14).coerceAtLeast(10),
                mockTier = IN_SEASON_MOCK_TIER,
                outcome = NbaDraftOutcome.ReturnedToRoster,
                pickSummary = null,
            )
        }
    }
}