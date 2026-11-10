package dev.novux.dynasty.cbb.data.recruiting

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingActionsParser
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingEconomy
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingParser
import dev.novux.dynasty.cbb.core.balance.BalanceRuleset
import dev.novux.dynasty.cbb.core.recruiting.RecruitingActionSpend
import dev.novux.dynasty.cbb.core.recruiting.RecruitingPointAction
import dev.novux.dynasty.cbb.core.recruiting.RecruitingSpendResult
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.recruiting.RecruitingCycleLedger
import dev.novux.dynasty.cbb.core.recruiting.RecruitingWeekGrant
import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import kotlinx.coroutines.flow.first

private val Context.recruitingEconomyStore: DataStore<Preferences> by preferencesDataStore(
    name = "recruiting_economy",
)

class RecruitingEconomyRepository(
    private val context: Context,
) {
    suspend fun loadOrOpenCycle(
        dynasty: LoadedDynasty,
        defaultPrestige: Int = DEFAULT_PROGRAM_PRESTIGE,
    ): RecruitingCycleLedger {
        val economy = economyFrom(dynasty.balanceRuleset)
        val prefs = context.recruitingEconomyStore.data.first()
        val key = ledgerKey(dynasty.dynastyId)
        val raw = prefs[key]
        if (raw != null) {
            return decode(raw)
        }
        val ledger = RecruitingWeekGrant.openCycle(
            economy,
            cycleSeasonIndex = dynasty.metadata.seasonIndex,
            programPrestige = defaultPrestige,
        )
        persist(dynasty.dynastyId, ledger)
        return ledger
    }

    suspend fun spendPointAction(
        dynasty: LoadedDynasty,
        action: RecruitingPointAction,
        bonusAmount: Int? = null,
    ): RecruitingSpendResult {
        val actions = BalanceRecruitingActionsParser.parse(dynasty.balanceRuleset.recruiting)
        val ledger = loadOrOpenCycle(dynasty)
        val result = RecruitingActionSpend.spend(
            action = action,
            pointsAvailable = ledger.pointsAvailable,
            actions = actions,
            bonusAmount = bonusAmount,
        )
        if (result.success) {
            persist(dynasty.dynastyId, ledger.withPointsAvailable(result.pointsRemaining))
        }
        return result
    }

    suspend fun onCalendarWeekAdvanced(dynasty: LoadedDynasty, pointsSpentThisWeek: Int = 0) {
        val economy = economyFrom(dynasty.balanceRuleset)
        val ledger = loadOrOpenCycle(dynasty)
        val pos = dynasty.weekCheckpoint.activePosition
        val (next, _) = RecruitingWeekGrant.applyWeekAdvanceGrant(
            economy,
            ledger,
            phase = pos.phase,
            offseasonWeekIndex = pos.weekIndex.takeIf { pos.phase == CalendarPhase.Offseason },
            pointsSpentThisWeek = pointsSpentThisWeek,
        )
        persist(dynasty.dynastyId, next)
    }

    suspend fun openNextCycleAfterSeasonReview(
        dynastyId: String,
        balance: BalanceRuleset,
        nextSeasonIndex: Int,
        postRecapPrestige: Int,
    ): RecruitingCycleLedger {
        val economy = economyFrom(balance)
        val base = RecruitingWeekGrant.previewNextCycleWeeklyPool(economy, postRecapPrestige)
        val ledger = RecruitingCycleLedger(
            cycleSeasonIndex = nextSeasonIndex,
            programPrestige = postRecapPrestige,
            weeklyPoolBase = base,
            pointsAvailable = base,
        )
        persist(dynastyId, ledger)
        return ledger
    }

    private fun economyFrom(balance: BalanceRuleset): BalanceRecruitingEconomy =
        BalanceRecruitingParser.parse(balance.recruiting)

    private suspend fun persist(dynastyId: String, ledger: RecruitingCycleLedger) {
        context.recruitingEconomyStore.edit { prefs ->
            prefs[ledgerKey(dynastyId)] = encode(ledger)
        }
    }

    private fun ledgerKey(dynastyId: String) = stringPreferencesKey("ledger_$dynastyId")

    private fun encode(ledger: RecruitingCycleLedger): String =
        "${ledger.cycleSeasonIndex}|${ledger.programPrestige}|${ledger.weeklyPoolBase}|" +
            "${ledger.pointsAvailable}|${ledger.unspentAtLastWeekEnd}"

    private fun decode(raw: String): RecruitingCycleLedger {
        val p = raw.split("|")
        require(p.size == 5)
        return RecruitingCycleLedger(
            cycleSeasonIndex = p[0].toInt(),
            programPrestige = p[1].toInt(),
            weeklyPoolBase = p[2].toInt(),
            pointsAvailable = p[3].toInt(),
            unspentAtLastWeekEnd = p[4].toInt(),
        )
    }

    companion object {
        const val DEFAULT_PROGRAM_PRESTIGE = 40
    }
}
