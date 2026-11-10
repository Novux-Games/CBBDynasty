package dev.novux.dynasty.cbb.core.preseason.tournament

import kotlin.random.Random

object TournamentInviteGenerator {
    fun invites(programPrestige: Int, seed: Long): List<EarlySeasonEvent> {
        val random = Random(seed)
        return when {
            programPrestige < 30 -> emptyList()
            programPrestige < 55 -> {
                if (random.nextBoolean()) {
                    listOf(EarlySeasonEventCatalog.all[random.nextInt(EarlySeasonEventCatalog.all.size)])
                } else {
                    emptyList()
                }
            }
            else -> EarlySeasonEventCatalog.all.shuffled(random).take(2 + random.nextInt(2))
        }
    }
}
