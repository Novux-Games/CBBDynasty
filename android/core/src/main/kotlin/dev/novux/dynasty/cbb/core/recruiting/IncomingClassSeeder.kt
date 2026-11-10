package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import kotlin.random.Random

/**
 * Preseason HS + JUCO pool generation ([planning/recruiting-engine.md#timeline--seeding]).
 */
object IncomingClassSeeder {
    const val HS_POOL_SIZE: Int = 2_400
    const val JUCO_POOL_SIZE: Int = 1_500
    const val TOP_NATIONAL_RANKS: Int = 60

    private val firstNames = listOf(
        "Jordan", "Eli", "Marcus", "Tyler", "Cole", "Darius", "Liam", "Noah", "Jayden", "Kai",
    )
    private val lastNames = listOf(
        "Walker", "Brooks", "Hayes", "Carter", "Mills", "Foster", "Reed", "Shaw", "Parks", "Lang",
    )
    private val states = listOf("TX", "OH", "FL", "CA", "NC", "IN", "KY", "VA", "IL", "GA", "MI", "PA")

    fun seed(
        enrollingSeasonIndex: Int,
        hsCount: Int = HS_POOL_SIZE,
        jucoCount: Int = JUCO_POOL_SIZE,
        seedKey: Long = enrollingSeasonIndex.toLong(),
    ): List<RecruitProspect> {
        val random = Random(seedKey xor (enrollingSeasonIndex.toLong() * 524_287))
        val hs = generatePool(
            enrollingSeasonIndex,
            hsCount,
            RecruitPoolKind.HighSchool,
            "hs",
            random,
            jucoBias = false,
        )
        val juco = generatePool(
            enrollingSeasonIndex,
            jucoCount,
            RecruitPoolKind.Juco,
            "juco",
            random,
            jucoBias = true,
        )
        val combined = hs + juco
        val ranked = combined.sortedByDescending { it.hiddenRating }
        val rankById = ranked.take(TOP_NATIONAL_RANKS)
            .mapIndexed { index, prospect -> prospect.prospectId to (index + 1) }
            .toMap()
        return combined.map { prospect ->
            prospect.copy(nationalHsRank = rankById[prospect.prospectId])
        }
    }

    private fun generatePool(
        seasonIndex: Int,
        count: Int,
        kind: RecruitPoolKind,
        idPrefix: String,
        random: Random,
        jucoBias: Boolean,
    ): List<RecruitProspect> =
        List(count) { index ->
            val stars = starBand(index, count, jucoBias)
            val hidden = hiddenRatingForStars(stars, random)
            val position = PlayerPosition.entries[random.nextInt(PlayerPosition.entries.size)]
            val state = states[random.nextInt(states.size)]
            val prospectId = "$idPrefix-$seasonIndex-$index"
            RecruitProspect(
                prospectId = prospectId,
                displayName = "${firstNames[random.nextInt(firstNames.size)]} " +
                    "${lastNames[random.nextInt(lastNames.size)]}",
                stars = stars,
                position = position,
                homeState = state,
                nilAskUsd = nilAskForStars(stars, jucoBias),
                scoutLetterOverall = letterForStars(stars, random),
                topPriorities = RecruitPriorityRoll.roll(prospectId),
                poolKind = kind,
                hiddenRating = hidden,
            )
        }

    private fun starBand(index: Int, size: Int, jucoBias: Boolean): Int {
        val pct = index.toDouble() / size.coerceAtLeast(1)
        return when {
            jucoBias && pct < 0.01 -> 5
            !jucoBias && pct < 0.02 -> 5
            pct < 0.18 -> 4
            pct < 0.45 -> 3
            else -> 2
        }
    }

    private fun hiddenRatingForStars(stars: Int, random: Random): Int {
        val base = when (stars) {
            5 -> 88
            4 -> 76
            3 -> 62
            else -> 48
        }
        return (base + random.nextInt(12) - 3).coerceIn(1, 99)
    }

    private fun nilAskForStars(stars: Int, jucoBias: Boolean): Int {
        val factor = if (jucoBias) 0.85 else 1.0
        val base = when (stars) {
            5 -> 450_000
            4 -> 180_000
            3 -> 60_000
            else -> 15_000
        }
        return (base * factor).toInt()
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
