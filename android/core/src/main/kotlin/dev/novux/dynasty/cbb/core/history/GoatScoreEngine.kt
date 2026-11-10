package dev.novux.dynasty.cbb.core.history

import kotlin.math.max
import kotlin.math.round

object GoatScoreEngine {
    fun score(career: PlayerCareerHistory, weights: BalanceGoatWeights): Double {
        val peakBonus = max(0, career.peakOvrCareer - weights.peakOvrThreshold)
        val raw = weights.winShares * career.careerWinShares +
            weights.nationalTitles * career.nationalTitles +
            weights.nationalPoty * career.nationalPotySeasons +
            weights.allAmericaPoints * career.allAmericaPoints +
            weights.confPoty * career.confPotySeasons +
            weights.peakOvrBonus * peakBonus
        return round(raw * 10.0) / 10.0
    }

    fun eligible(career: PlayerCareerHistory): Boolean {
        val games = career.seasons.sumOf { it.minutesSeason } / 28
        return games >= 40 || career.nationalPotySeasons > 0
    }

    fun buildGoatEntry(
        rank: Int,
        career: PlayerCareerHistory,
        schoolLabel: String,
        goatScore: Double,
        nationalHof: Boolean,
    ): GoatListEntry {
        val chips = buildList {
            if (career.nationalTitles > 0) add("${career.nationalTitles}× ring")
            if (career.nationalPotySeasons > 0) add("NCBA MVP")
            if (career.allAmericaPoints >= 3) add("All-America")
            if (nationalHof) add("Nat. HOF")
        }
        return GoatListEntry(
            rank = rank,
            playerId = career.playerId,
            displayName = career.displayName,
            schoolLabel = schoolLabel,
            goatScore = goatScore,
            careerWinShares = career.careerWinShares,
            nationalHof = nationalHof,
            chips = chips,
        )
    }
}
