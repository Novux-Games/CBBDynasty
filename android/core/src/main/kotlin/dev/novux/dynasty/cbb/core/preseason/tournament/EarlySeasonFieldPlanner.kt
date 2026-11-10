package dev.novux.dynasty.cbb.core.preseason.tournament

import dev.novux.dynasty.cbb.core.pack.TeamDisplayOverlay
import kotlin.random.Random

/** Assign at most one early event per team per season ([planning/ncba-branding.md]). */
object EarlySeasonFieldPlanner {
    fun planFields(
        teams: List<TeamDisplayOverlay>,
        userTeamId: String,
        userAcceptedEventId: String?,
        seed: Long,
    ): Map<String, List<String>> {
        val random = Random(seed)
        val remaining = teams.sortedByDescending { it.programPrestige }
            .map { it.teamId }
            .toMutableList()
        if (userAcceptedEventId != null && userTeamId in remaining) {
            remaining.remove(userTeamId)
        }
        val fields = mutableMapOf<String, MutableList<String>>()
        EarlySeasonEventCatalog.all.sortedByDescending { it.teamCount }.forEach { event ->
            val cap = event.teamCount
            val picks = mutableListOf<String>()
            if (event.templateId == userAcceptedEventId) {
                picks += userTeamId
            }
            val iterator = remaining.iterator()
            while (picks.size < cap && iterator.hasNext()) {
                val next = iterator.next()
                if (next == userTeamId && userAcceptedEventId != event.templateId) continue
                if (next in picks) continue
                picks += next
                iterator.remove()
            }
            if (userAcceptedEventId == event.templateId && userTeamId !in picks) {
                picks += userTeamId
            }
            if (picks.size >= 2) {
                fields[event.templateId] = picks.shuffled(random).take(cap).toMutableList()
            }
        }
        return fields.mapValues { (_, v) -> v.toList() }
    }
}
