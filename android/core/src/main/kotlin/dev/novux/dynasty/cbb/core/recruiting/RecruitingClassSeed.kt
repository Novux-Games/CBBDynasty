package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import kotlin.random.Random

/** Deterministic demo class until full HS/JUCO seed lands (7.2). */
object RecruitingClassSeed {
    private val firstNames = listOf("Jordan", "Eli", "Marcus", "Tyler", "Cole", "Darius", "Liam", "Noah")
    private val lastNames = listOf("Walker", "Brooks", "Hayes", "Carter", "Mills", "Foster", "Reed", "Shaw")
    private val states = listOf("TX", "OH", "FL", "CA", "NC", "IN", "KY", "VA", "IL", "GA")

    fun demoPool(membershipSeasonIndex: Int, size: Int = 120): List<RecruitProspect> {
        val random = Random(membershipSeasonIndex.toLong() * 7919)
        return List(size) { index ->
            val stars = starBand(index, size)
            val position = PlayerPosition.entries[index % PlayerPosition.entries.size]
            val state = states[random.nextInt(states.size)]
            val nationalRank = if (stars >= 4 && index < 60) index + 1 else null
            val nilAsk = nilAskForStars(stars)
            RecruitProspect(
                prospectId = "hs-$membershipSeasonIndex-$index",
                displayName = "${firstNames[random.nextInt(firstNames.size)]} " +
                    "${lastNames[random.nextInt(lastNames.size)]}",
                stars = stars,
                position = position,
                homeState = state,
                nationalHsRank = nationalRank,
                nilAskUsd = nilAsk,
                scoutLetterOverall = letterForStars(stars, random),
            )
        }
    }

    private fun starBand(index: Int, size: Int): Int {
        val pct = index.toDouble() / size
        return when {
            pct < 0.02 -> 5
            pct < 0.20 -> 4
            pct < 0.50 -> 3
            else -> 2
        }
    }

    private fun nilAskForStars(stars: Int): Int = when (stars) {
        5 -> 450_000
        4 -> 180_000
        3 -> 60_000
        else -> 15_000
    }

    private fun letterForStars(stars: Int, random: Random): String {
        val base = when (stars) {
            5 -> listOf("A", "A-")
            4 -> listOf("B+", "B")
            3 -> listOf("B-", "C+")
            else -> listOf("C", "C-")
        }
        return base[random.nextInt(base.size)]
    }
}
