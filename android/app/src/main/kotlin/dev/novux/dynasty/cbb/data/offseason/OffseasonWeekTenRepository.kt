package dev.novux.dynasty.cbb.data.offseason

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.offseason.weekten.ConferenceOfferCard
import dev.novux.dynasty.cbb.core.offseason.weekten.ConferenceOfferDecision
import dev.novux.dynasty.cbb.core.offseason.weekten.ConferenceOfferGenerator
import dev.novux.dynasty.cbb.core.offseason.weekten.OffseasonConferenceOffersPolicy
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.database.DynastyDatabase
import kotlinx.coroutines.flow.first

private val Context.offseasonWeekTenStore: DataStore<Preferences> by preferencesDataStore(
    name = "offseason_week_ten",
)

data class ConferenceOffersWeekState(
    val offers: List<ConferenceOfferCard>,
    val decisions: Map<String, ConferenceOfferDecision>,
    val acceptedConferenceId: String?,
)

class OffseasonWeekTenRepository(
    private val context: Context,
    private val packRepository: PackRepository,
    private val database: DynastyDatabase,
) {
    suspend fun loadState(dynastyId: String, seasonIndex: Int): ConferenceOffersWeekState {
        if (OffseasonConferenceOffersPolicy.skipConferenceOffersWeek(seasonIndex)) {
            return ConferenceOffersWeekState(emptyList(), emptyMap(), null)
        }
        val prefs = context.offseasonWeekTenStore.data.first()
        val offers = decodeOffers(prefs[offersKey(dynastyId, seasonIndex)])
        val decisions = decodeDecisions(prefs[decisionsKey(dynastyId, seasonIndex)])
        val accepted = prefs[acceptedKey(dynastyId, seasonIndex)]
        val resolvedOffers = if (offers.isEmpty()) {
            ensureOffersGenerated(dynastyId, seasonIndex)
        } else {
            offers
        }
        return ConferenceOffersWeekState(resolvedOffers, decisions, accepted)
    }

    suspend fun respond(
        dynastyId: String,
        seasonIndex: Int,
        offerId: String,
        decision: ConferenceOfferDecision,
    ) {
        require(decision != ConferenceOfferDecision.Pending)
        val state = loadState(dynastyId, seasonIndex)
        val offer = state.offers.firstOrNull { it.offerId == offerId } ?: return
        val nextDecisions = state.decisions.toMutableMap()
        nextDecisions[offerId] = decision
        var acceptedId = state.acceptedConferenceId
        if (decision == ConferenceOfferDecision.Accepted) {
            acceptedId = offer.invitingConferenceId
            state.offers.forEach { other ->
                if (other.offerId != offerId && nextDecisions[other.offerId] == null) {
                    nextDecisions[other.offerId] = ConferenceOfferDecision.Declined
                }
            }
        }
        persist(dynastyId, seasonIndex, state.offers, nextDecisions, acceptedId)
    }

    suspend fun canAdvance(dynastyId: String, seasonIndex: Int): Boolean {
        val state = loadState(dynastyId, seasonIndex)
        return OffseasonConferenceOffersPolicy.canAdvanceWeek(state.offers, state.decisions)
    }

    private suspend fun ensureOffersGenerated(dynastyId: String, seasonIndex: Int): List<ConferenceOfferCard> {
        val coach = database.dynastyCoachDao().getByDynastyId(dynastyId) ?: return emptyList()
        val index = packRepository.activeSession().index
        val offers = ConferenceOfferGenerator.generate(
            packIndex = index,
            userTeamId = coach.userTeamId,
            dynastyId = dynastyId,
            seasonIndex = seasonIndex,
        )
        if (offers.isNotEmpty()) {
            persist(dynastyId, seasonIndex, offers, emptyMap(), null)
        }
        return offers
    }

    private suspend fun persist(
        dynastyId: String,
        seasonIndex: Int,
        offers: List<ConferenceOfferCard>,
        decisions: Map<String, ConferenceOfferDecision>,
        acceptedConferenceId: String?,
    ) {
        context.offseasonWeekTenStore.edit { prefs ->
            prefs[offersKey(dynastyId, seasonIndex)] = encodeOffers(offers)
            prefs[decisionsKey(dynastyId, seasonIndex)] = encodeDecisions(decisions)
            if (acceptedConferenceId != null) {
                prefs[acceptedKey(dynastyId, seasonIndex)] = acceptedConferenceId
            }
        }
    }

    private fun offersKey(dynastyId: String, seasonIndex: Int) =
        stringPreferencesKey("offers_${dynastyId}_$seasonIndex")

    private fun decisionsKey(dynastyId: String, seasonIndex: Int) =
        stringPreferencesKey("decisions_${dynastyId}_$seasonIndex")

    private fun acceptedKey(dynastyId: String, seasonIndex: Int) =
        stringPreferencesKey("accepted_conf_${dynastyId}_$seasonIndex")

    private fun encodeOffers(offers: List<ConferenceOfferCard>): String =
        offers.joinToString("\n") {
            listOf(
                it.offerId,
                it.invitingConferenceId,
                it.invitingConferenceName.escapePipe(),
                it.currentConferenceName.escapePipe(),
                it.inviteScore,
                it.prosSummary.escapePipe(),
                it.consSummary.escapePipe(),
            ).joinToString("|")
        }

    private fun decodeOffers(raw: String?): List<ConferenceOfferCard> {
        if (raw.isNullOrBlank()) return emptyList()
        return raw.lineSequence().mapNotNull { line ->
            val p = line.split("|")
            if (p.size < 7) return@mapNotNull null
            ConferenceOfferCard(
                offerId = p[0],
                invitingConferenceId = p[1],
                invitingConferenceName = p[2].unescapePipe(),
                currentConferenceName = p[3].unescapePipe(),
                inviteScore = p[4].toIntOrNull() ?: 0,
                prosSummary = p[5].unescapePipe(),
                consSummary = p[6].unescapePipe(),
            )
        }.toList()
    }

    private fun encodeDecisions(decisions: Map<String, ConferenceOfferDecision>): String =
        decisions.entries.joinToString(",") { "${it.key}=${it.value.name}" }

    private fun decodeDecisions(raw: String?): Map<String, ConferenceOfferDecision> {
        if (raw.isNullOrBlank()) return emptyMap()
        return raw.split(",").mapNotNull { part ->
            val split = part.split("=")
            if (split.size != 2) return@mapNotNull null
            val decision = runCatching { ConferenceOfferDecision.valueOf(split[1]) }.getOrNull() ?: return@mapNotNull null
            split[0] to decision
        }.toMap()
    }

    private fun String.escapePipe(): String = replace("|", "%7C")
    private fun String.unescapePipe(): String = replace("%7C", "|")
}
