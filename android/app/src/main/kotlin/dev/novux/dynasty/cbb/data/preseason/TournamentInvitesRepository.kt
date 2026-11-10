package dev.novux.dynasty.cbb.data.preseason

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.preseason.tournament.EarlySeasonEvent
import dev.novux.dynasty.cbb.core.preseason.tournament.EarlySeasonEventCatalog
import dev.novux.dynasty.cbb.core.preseason.tournament.TournamentInviteGenerator
import dev.novux.dynasty.cbb.core.preseason.tournament.TournamentInviteResolution
import dev.novux.dynasty.cbb.core.preseason.tournament.TournamentInvitesState
import dev.novux.dynasty.cbb.data.recruiting.RecruitingEconomyRepository
import kotlinx.coroutines.flow.first

private val Context.tournamentInvitesStore: DataStore<Preferences> by preferencesDataStore(
    name = "preseason_tournament_invites",
)

class TournamentInvitesRepository(private val context: Context) {
    suspend fun loadState(
        dynastyId: String,
        seasonIndex: Int,
        programPrestige: Int = RecruitingEconomyRepository.DEFAULT_PROGRAM_PRESTIGE,
    ): TournamentInvitesState {
        val prefs = context.tournamentInvitesStore.data.first()
        val offers = decodeOffers(prefs[offersKey(dynastyId, seasonIndex)])
            ?: ensureOffers(dynastyId, seasonIndex, programPrestige)
        val resolution = decodeResolution(prefs[resolutionKey(dynastyId, seasonIndex)])
        return TournamentInvitesState(offers, resolution)
    }

    suspend fun acknowledgeNoInvites(dynastyId: String, seasonIndex: Int): TournamentInvitesState {
        persistResolution(dynastyId, seasonIndex, TournamentInviteResolution.NoInvitesAcknowledged)
        return loadState(dynastyId, seasonIndex)
    }

    suspend fun declineAll(dynastyId: String, seasonIndex: Int): TournamentInvitesState {
        persistResolution(dynastyId, seasonIndex, TournamentInviteResolution.DeclinedAll)
        return loadState(dynastyId, seasonIndex)
    }

    suspend fun acceptInvite(
        dynastyId: String,
        seasonIndex: Int,
        templateId: String,
    ): TournamentInvitesState {
        val event = EarlySeasonEventCatalog.byId(templateId)
            ?: return loadState(dynastyId, seasonIndex)
        persistResolution(dynastyId, seasonIndex, TournamentInviteResolution.Accepted(event))
        return loadState(dynastyId, seasonIndex)
    }

    private suspend fun ensureOffers(
        dynastyId: String,
        seasonIndex: Int,
        programPrestige: Int,
    ): List<EarlySeasonEvent> {
        val seed = dynastyId.hashCode().toLong() xor seasonIndex.toLong()
        val offers = TournamentInviteGenerator.invites(programPrestige, seed)
        context.tournamentInvitesStore.edit { prefs ->
            prefs[offersKey(dynastyId, seasonIndex)] = encodeOffers(offers)
        }
        return offers
    }

    private suspend fun persistResolution(
        dynastyId: String,
        seasonIndex: Int,
        resolution: TournamentInviteResolution,
    ) {
        context.tournamentInvitesStore.edit { prefs ->
            prefs[resolutionKey(dynastyId, seasonIndex)] = encodeResolution(resolution)
        }
    }

    private fun offersKey(dynastyId: String, seasonIndex: Int) =
        stringPreferencesKey("offers_${dynastyId}_$seasonIndex")

    private fun resolutionKey(dynastyId: String, seasonIndex: Int) =
        stringPreferencesKey("resolution_${dynastyId}_$seasonIndex")

    private fun encodeOffers(offers: List<EarlySeasonEvent>): String =
        offers.joinToString(",") { it.templateId }

    private fun decodeOffers(raw: String?): List<EarlySeasonEvent>? {
        if (raw == null) return null
        if (raw.isBlank()) return emptyList()
        return raw.split(",").mapNotNull { EarlySeasonEventCatalog.byId(it) }
    }

    private fun encodeResolution(resolution: TournamentInviteResolution): String = when (resolution) {
        TournamentInviteResolution.Unresolved -> "unresolved"
        TournamentInviteResolution.NoInvitesAcknowledged -> "none_ack"
        TournamentInviteResolution.DeclinedAll -> "declined"
        is TournamentInviteResolution.Accepted -> "accepted:${resolution.event.templateId}"
    }

    private fun decodeResolution(raw: String?): TournamentInviteResolution {
        if (raw.isNullOrBlank() || raw == "unresolved") return TournamentInviteResolution.Unresolved
        return when {
            raw == "none_ack" -> TournamentInviteResolution.NoInvitesAcknowledged
            raw == "declined" -> TournamentInviteResolution.DeclinedAll
            raw.startsWith("accepted:") -> {
                val id = raw.removePrefix("accepted:")
                val event = EarlySeasonEventCatalog.byId(id)
                if (event != null) TournamentInviteResolution.Accepted(event)
                else TournamentInviteResolution.Unresolved
            }
            else -> TournamentInviteResolution.Unresolved
        }
    }
}
