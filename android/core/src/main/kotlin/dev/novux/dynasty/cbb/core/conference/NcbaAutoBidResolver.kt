package dev.novux.dynasty.cbb.core.conference

/**
 * NCBA conference auto-bid with reclass pass-over — [planning/postseason.md#step-1--auto-bids-32].
 */
object NcbaAutoBidResolver {
    fun resolve(
        result: ConferenceTournamentResult,
        postseasonEligible: (String) -> Boolean,
    ): String {
        val ladder = finishLadder(result)
        return ladder.first { postseasonEligible(it) }
    }

    fun finishLadder(result: ConferenceTournamentResult): List<String> {
        if (result.finishOrder.isNotEmpty()) return result.finishOrder.distinct()
        return buildList {
            add(result.championTeamId)
            result.runnerUpTeamId?.let { add(it) }
            addAll(
                result.qualifiedTeamIds.filter { it != result.championTeamId && it != result.runnerUpTeamId },
            )
        }
    }
}
