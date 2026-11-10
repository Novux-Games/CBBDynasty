package dev.novux.dynasty.cbb.data.nil

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.balance.BalanceNilRules
import dev.novux.dynasty.cbb.core.nil.NilAssignOfferResult
import dev.novux.dynasty.cbb.core.nil.NilBudgetLedger
import dev.novux.dynasty.cbb.core.nil.NilBudgetLedgerCalculator
import dev.novux.dynasty.cbb.core.nil.NilOfferEngine
import dev.novux.dynasty.cbb.core.nil.NilSigningSnapshot
import dev.novux.dynasty.cbb.core.nil.NilSigningSnapshotEngine
import dev.novux.dynasty.cbb.core.recruiting.RecruitingRefundableAction
import dev.novux.dynasty.cbb.data.recruiting.RecruitingReservationRepository
import kotlinx.coroutines.flow.first

private val Context.nilBudgetStore: DataStore<Preferences> by preferencesDataStore(name = "nil_budget")

class NilBudgetRepository(
    private val context: Context,
    private val reservationRepository: RecruitingReservationRepository,
) {
    suspend fun loadOffers(dynastyId: String, cycleSeasonIndex: Int): Map<String, Long> {
        val raw = context.nilBudgetStore.data.first()[offersKey(dynastyId, cycleSeasonIndex)] ?: return emptyMap()
        return decodeOffers(raw)
    }

    suspend fun loadLedger(
        dynastyId: String,
        cycleSeasonIndex: Int,
        poolAnnualUsd: Long,
        rosterCommittedUsd: Long = 0L,
    ): NilBudgetLedger {
        val offers = loadOffers(dynastyId, cycleSeasonIndex)
        val persistedCommitted = context.nilBudgetStore.data.first()[committedKey(dynastyId, cycleSeasonIndex)] ?: 0L
        val scholarshipIds = scholarshipOfferProspectIds(dynastyId)
        return NilBudgetLedgerCalculator.compute(
            poolAnnualUsd = poolAnnualUsd,
            committedUsd = rosterCommittedUsd + persistedCommitted,
            pendingOffersUsd = offers,
            scholarshipOfferProspectIds = scholarshipIds,
        )
    }

    suspend fun assignOffer(
        dynastyId: String,
        cycleSeasonIndex: Int,
        prospectId: String,
        nilAskUsd: Int,
        requestedOfferUsd: Long,
        rules: BalanceNilRules,
        poolAnnualUsd: Long,
        rosterCommittedUsd: Long = 0L,
    ): NilAssignOfferResult {
        val offers = loadOffers(dynastyId, cycleSeasonIndex).toMutableMap()
        val prior = offers[prospectId] ?: 0L
        val ledger = loadLedger(dynastyId, cycleSeasonIndex, poolAnnualUsd, rosterCommittedUsd)
        val result = NilOfferEngine.validateAssign(nilAskUsd, requestedOfferUsd, rules, ledger, prior)
        if (result.clampedOfferUsd <= 0L) {
            offers.remove(prospectId)
        } else {
            offers[prospectId] = result.clampedOfferUsd
        }
        persistOffers(dynastyId, cycleSeasonIndex, offers)
        return result
    }

    suspend fun offerUsd(dynastyId: String, cycleSeasonIndex: Int, prospectId: String): Long =
        loadOffers(dynastyId, cycleSeasonIndex)[prospectId] ?: 0L

    suspend fun signingSnapshots(
        dynastyId: String,
        cycleSeasonIndex: Int,
        signeeIds: List<Pair<String, Int>>,
    ): List<NilSigningSnapshot> {
        val offers = loadOffers(dynastyId, cycleSeasonIndex)
        return signeeIds.map { (prospectId, nilAskUsd) ->
            val offer = offers[prospectId]?.toInt() ?: 0
            NilSigningSnapshotEngine.forProspect(prospectId, nilAskUsd, offer)
        }
    }

    suspend fun commitSignedDeals(
        dynastyId: String,
        cycleSeasonIndex: Int,
        snapshots: List<NilSigningSnapshot>,
    ) {
        if (snapshots.isEmpty()) return
        val committed = context.nilBudgetStore.data.first()[committedKey(dynastyId, cycleSeasonIndex)] ?: 0L
        val add = snapshots.sumOf { it.nilSignedUsd.toLong() }
        val offers = loadOffers(dynastyId, cycleSeasonIndex).toMutableMap()
        snapshots.forEach { offers.remove(it.prospectId) }
        context.nilBudgetStore.edit { prefs ->
            prefs[committedKey(dynastyId, cycleSeasonIndex)] = committed + add
            prefs[offersKey(dynastyId, cycleSeasonIndex)] = encodeOffers(offers)
        }
    }

    private suspend fun scholarshipOfferProspectIds(dynastyId: String): Set<String> =
        reservationRepository.loadActive(dynastyId)
            .filter { it.action == RecruitingRefundableAction.ScholarshipOffer }
            .map { it.prospectId }
            .toSet()

    private suspend fun persistOffers(dynastyId: String, cycleSeasonIndex: Int, offers: Map<String, Long>) {
        context.nilBudgetStore.edit { prefs ->
            prefs[offersKey(dynastyId, cycleSeasonIndex)] = encodeOffers(offers)
        }
    }

    private fun offersKey(dynastyId: String, cycleSeasonIndex: Int) =
        stringPreferencesKey("nil_offers_${dynastyId}_$cycleSeasonIndex")

    private fun committedKey(dynastyId: String, cycleSeasonIndex: Int) =
        longPreferencesKey("nil_committed_${dynastyId}_$cycleSeasonIndex")

    private fun encodeOffers(offers: Map<String, Long>): String =
        offers.entries.joinToString(";") { "${it.key}:${it.value}" }

    private fun decodeOffers(raw: String): Map<String, Long> =
        raw.split(";").filter { it.isNotBlank() }.mapNotNull { token ->
            val idx = token.indexOf(':')
            if (idx <= 0) return@mapNotNull null
            val amount = token.substring(idx + 1).toLongOrNull() ?: return@mapNotNull null
            token.substring(0, idx) to amount
        }.toMap()
}
