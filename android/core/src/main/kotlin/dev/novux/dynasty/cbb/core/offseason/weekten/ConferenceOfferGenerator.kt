package dev.novux.dynasty.cbb.core.offseason.weekten

import dev.novux.dynasty.cbb.core.pack.IdentityPackIndex
import kotlin.math.roundToInt
import kotlin.random.Random

object ConferenceOfferGenerator {
    fun generate(
        packIndex: IdentityPackIndex,
        userTeamId: String,
        dynastyId: String,
        seasonIndex: Int,
        maxOffers: Int = 2,
    ): List<ConferenceOfferCard> {
        val userTeam = packIndex.team(userTeamId) ?: return emptyList()
        val currentConf = userTeam.conferenceId
        val confPrestige = averagePrestigeByConference(packIndex)
        val currentTier = confPrestige[currentConf] ?: userTeam.programPrestige.toDouble()
        val seed = (dynastyId.hashCode().toLong() shl 32) xor seasonIndex.toLong()
        val random = Random(seed)
        val candidates = confPrestige.entries
            .filter { it.key != currentConf && it.value >= currentTier + 3.0 }
            .sortedByDescending { it.value }
            .shuffled(random)
            .take(maxOffers)
        val currentName = packIndex.conference(currentConf)?.displayName ?: currentConf
        return candidates.mapIndexed { index, (confId, prestige) ->
            val confName = packIndex.conference(confId)?.displayName ?: confId
            val inviteScore = (prestige * 10 + random.nextInt(15)).roundToInt().coerceIn(0, 100)
            ConferenceOfferCard(
                offerId = "$dynastyId-$seasonIndex-$confId-$index",
                invitingConferenceId = confId,
                invitingConferenceName = confName,
                currentConferenceName = currentName,
                inviteScore = inviteScore,
                prosSummary = "Stronger league profile · better national exposure",
                consSummary = "Tougher nightly matchups · longer travel",
            )
        }
    }

    private fun averagePrestigeByConference(packIndex: IdentityPackIndex): Map<String, Double> {
        val teams = packIndex.allTeamDisplays()
        return teams.groupBy { it.conferenceId }
            .mapValues { (_, list) ->
                list.map { it.programPrestige }.average()
            }
    }
}
