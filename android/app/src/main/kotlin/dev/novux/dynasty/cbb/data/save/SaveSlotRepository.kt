package dev.novux.dynasty.cbb.data.save

import androidx.room.withTransaction
import dev.novux.dynasty.cbb.core.calendar.SeasonMembershipYear
import dev.novux.dynasty.cbb.core.calendar.SeasonReviewGate
import dev.novux.dynasty.cbb.core.save.DynastyBootstrap
import dev.novux.dynasty.cbb.core.save.DynastyLoadFailure
import dev.novux.dynasty.cbb.core.save.DynastyLoadResult
import dev.novux.dynasty.cbb.core.save.DynastySaveValidator
import dev.novux.dynasty.cbb.core.roster.SeasonOneRosterGenerator
import dev.novux.dynasty.cbb.core.save.NewGameDraft
import dev.novux.dynasty.cbb.core.save.NewGameSignValidator
import dev.novux.dynasty.cbb.core.save.DynastySchema
import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import dev.novux.dynasty.cbb.core.save.SaveSlotSummary
import dev.novux.dynasty.cbb.core.save.SaveSlots
import dev.novux.dynasty.cbb.database.calendar.DynastyCalendarEntity
import dev.novux.dynasty.cbb.data.calendar.WeekCheckpointRepository
import dev.novux.dynasty.cbb.data.balance.BalanceParseException
import dev.novux.dynasty.cbb.data.balance.BalanceRulesetRepository
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.data.sim.SimTuningParseException
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.data.portal.PortalCycleRepository
import dev.novux.dynasty.cbb.data.recruiting.RecruitingEconomyRepository
import dev.novux.dynasty.cbb.data.sim.SimTuningRepository
import dev.novux.dynasty.cbb.database.DynastyDatabase
import dev.novux.dynasty.cbb.database.dynasty.toCoachEntity
import dev.novux.dynasty.cbb.database.roster.toEntities
import dev.novux.dynasty.cbb.database.dynasty.toEntity
import dev.novux.dynasty.cbb.database.dynasty.toDomain
import dev.novux.dynasty.cbb.database.save.SaveSlotEntity
import dev.novux.dynasty.cbb.database.save.ensureSlotRows
import dev.novux.dynasty.cbb.database.save.toSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class SaveSlotRepository(
    private val database: DynastyDatabase,
    private val weekCheckpointRepository: WeekCheckpointRepository,
    private val packRepository: PackRepository,
    private val balanceRulesetRepository: BalanceRulesetRepository,
    private val simTuningRepository: SimTuningRepository,
    private val recruitingEconomyRepository: RecruitingEconomyRepository,
    private val portalCycleRepository: PortalCycleRepository,
) {
    private val slotDao = database.saveSlotDao()
    private val metadataDao = database.dynastyMetadataDao()
    private val coachDao = database.dynastyCoachDao()
    private val rosterDao = database.dynastyRosterDao()
    private val calendarDao = database.dynastyCalendarDao()
    private val mutex = Any()

    suspend fun ensureInitialized() {
        synchronized(mutex) {
            val existing = slotDao.listAll()
            if (existing.size < SaveSlots.COUNT) {
                val merged = ensureSlotRows(existing)
                slotDao.upsertAll(merged)
            }
        }
    }

    fun observeSlots(): Flow<List<SaveSlotSummary>> =
        slotDao.observeAll().map { rows ->
            ensureSlotRows(rows).map { it.toSummary() }
        }

    fun observeContinueSlot(): Flow<SaveSlotSummary?> =
        observeSlots().map { slots ->
            slots
                .filter { !it.isEmpty }
                .maxByOrNull { it.lastPlayedAtEpochMillis ?: 0L }
        }

    suspend fun listSlots(): List<SaveSlotSummary> {
        ensureInitialized()
        return ensureSlotRows(slotDao.listAll()).map { it.toSummary() }
    }

    suspend fun continueSlot(): SaveSlotSummary? {
        ensureInitialized()
        return slotDao.getMostRecentlyPlayed()?.toSummary()
    }

    suspend fun firstEmptySlotIndex(): Int? {
        ensureInitialized()
        return ensureSlotRows(slotDao.listAll())
            .firstOrNull { it.dynastyId.isNullOrBlank() }
            ?.slotIndex
    }

    suspend fun loadSlot(slotIndex: Int): DynastyLoadResult {
        require(slotIndex in SaveSlots.INDICES) { "slotIndex out of range" }
        return try {
            ensureInitialized()
            val entity = slotDao.getByIndex(slotIndex)
                ?: return DynastyLoadResult.Unreadable(
                    slotIndex,
                    DynastyLoadFailure.DatabaseRead("slot row missing"),
                )
            val summary = entity.toSummary()
            if (summary.isEmpty) return DynastyLoadResult.SlotEmpty

            val slotSnap = DynastySaveValidator.SlotSnapshot(
                slotIndex = slotIndex,
                dynastyId = summary.dynastyId,
                packId = summary.packId,
                isEmpty = false,
            )
            val metadataEntity = summary.dynastyId?.let { metadataDao.getById(it) }
            val metadata = metadataEntity?.toDomain()
            val metaSnap = metadata?.let {
                DynastySaveValidator.MetadataSnapshot(
                    dynastyId = it.dynastyId,
                    packId = it.packId,
                    seasonIndex = it.seasonIndex,
                    phase = it.phase,
                )
            }
            DynastySaveValidator.validateBeforeLoad(slotSnap, metaSnap)?.let { failure ->
                return DynastyLoadResult.Unreadable(slotIndex, failure)
            }
            val meta = metadata
                ?: return DynastyLoadResult.Unreadable(
                    slotIndex,
                    DynastyLoadFailure.MissingMetadata(summary.dynastyId!!),
                )

            packRepository.restorePack(meta.packId)

            val balanceRuleset = try {
                balanceRulesetRepository.restorePinned(
                    meta.balanceRulesetId,
                    meta.balanceSchemaVersion,
                )
            } catch (e: BalanceParseException) {
                return DynastyLoadResult.Unreadable(
                    slotIndex,
                    DynastyLoadFailure.InvalidBalanceRuleset(e.message ?: "balance load failed"),
                )
            }

            val simTuning = try {
                simTuningRepository.restorePinned(meta.simTuningId, meta.simSchemaVersion)
            } catch (e: SimTuningParseException) {
                return DynastyLoadResult.Unreadable(
                    slotIndex,
                    DynastyLoadFailure.InvalidSimTuning(e.message ?: "sim tuning load failed"),
                )
            }

            val calendarRow = calendarDao.getByDynastyId(meta.dynastyId)
            calendarRow?.toCalendarSnapshot()?.let { snap ->
                DynastySaveValidator.validateCalendar(snap)?.let { failure ->
                    return DynastyLoadResult.Unreadable(slotIndex, failure)
                }
            }

            val weekCheckpoint = if (calendarRow == null) {
                database.withTransaction {
                    weekCheckpointRepository.repairCalendarFromMetadata(meta)
                }
            } else {
                weekCheckpointRepository.loadCheckpoint(meta.dynastyId)
                    ?: return DynastyLoadResult.Unreadable(
                        slotIndex,
                        DynastyLoadFailure.InvalidCalendar("calendar row unreadable"),
                    )
            }

            val now = System.currentTimeMillis()
            val updatedSummary = summary.copy(lastPlayedAtEpochMillis = now)
            slotDao.update(entity.copy(lastPlayedAtEpochMillis = now))
            DynastyLoadResult.Success(
                LoadedDynasty(
                    slotIndex = slotIndex,
                    dynastyId = meta.dynastyId,
                    summary = updatedSummary,
                    metadata = meta,
                    weekCheckpoint = weekCheckpoint,
                    balanceRuleset = balanceRuleset,
                    simTuning = simTuning,
                ),
            )
        } catch (e: Exception) {
            DynastyLoadResult.Unreadable(
                slotIndex,
                DynastyLoadFailure.DatabaseRead(e.message ?: "load failed"),
            )
        }
    }

    /** True when occupied slot header rows are present (fast list hint; load still validates). */
    suspend fun isSlotLikelyLoadable(slotIndex: Int): Boolean {
        ensureInitialized()
        val entity = slotDao.getByIndex(slotIndex) ?: return false
        val summary = entity.toSummary()
        if (summary.isEmpty || summary.dynastyId.isNullOrBlank()) return true
        return metadataDao.getById(summary.dynastyId) != null
    }

    suspend fun deleteSlot(slotIndex: Int) {
        require(slotIndex in SaveSlots.INDICES) { "slotIndex out of range" }
        ensureInitialized()
        val dynastyId = slotDao.getByIndex(slotIndex)?.dynastyId
        database.withTransaction {
            if (!dynastyId.isNullOrBlank()) {
                metadataDao.deleteById(dynastyId)
                coachDao.deleteByDynastyId(dynastyId)
                rosterDao.deleteForDynasty(dynastyId)
                weekCheckpointRepository.deleteForDynasty(dynastyId)
            }
            slotDao.clearSlot(slotIndex)
        }
    }

    /**
     * Creates a new dynasty with default metadata (offseason, season 1, default pack).
     * Full new-game wizard will replace placeholder display names.
     */
    suspend fun createEmptyDynasty(
        slotIndex: Int,
        schoolDisplayName: String,
        coachDisplayName: String,
        packId: String = DynastySchema.DEFAULT_PACK_ID,
        dynastyId: String = UUID.randomUUID().toString(),
    ): LoadedDynasty? {
        require(slotIndex in SaveSlots.INDICES) { "slotIndex out of range" }
        ensureInitialized()
        val existing = slotDao.getByIndex(slotIndex)?.toSummary()
        if (existing != null && !existing.isEmpty) return null

        val shippedBalance = balanceRulesetRepository.ensureShippedDefaultLoaded()
        val shippedSim = simTuningRepository.ensureShippedDefaultLoaded()
        val metadata = DynastyBootstrap.newDynasty(
            dynastyId = dynastyId,
            packId = packId,
            balanceRulesetId = shippedBalance.balanceRulesetId,
            balanceSchemaVersion = shippedBalance.schemaVersion,
            simTuningId = shippedSim.simTuningId,
            simSchemaVersion = shippedSim.schemaVersion,
        )
        val seasonLabel = SeasonMembershipYear.label(metadata.seasonIndex)
        val now = System.currentTimeMillis()

        database.withTransaction {
            metadataDao.upsert(metadata.toEntity())
            weekCheckpointRepository.insertBootstrapCalendar(metadata)
            slotDao.upsert(
                SaveSlotEntity(
                    slotIndex = slotIndex,
                    dynastyId = dynastyId,
                    lastPlayedAtEpochMillis = now,
                    schoolDisplayName = schoolDisplayName,
                    coachDisplayName = coachDisplayName,
                    seasonLabel = seasonLabel,
                    wins = 0,
                    losses = 0,
                    packId = packId,
                ),
            )
        }

        val weekCheckpoint = weekCheckpointRepository.loadOrBootstrap(metadata)
        val summary = SaveSlotSummary(
            slotIndex = slotIndex,
            isEmpty = false,
            dynastyId = dynastyId,
            schoolDisplayName = schoolDisplayName,
            coachDisplayName = coachDisplayName,
            seasonLabel = seasonLabel,
            wins = 0,
            losses = 0,
            lastPlayedAtEpochMillis = now,
            packId = packId,
        )
        return LoadedDynasty(
            slotIndex = slotIndex,
            dynastyId = dynastyId,
            summary = summary,
            metadata = metadata,
            weekCheckpoint = weekCheckpoint,
            balanceRuleset = shippedBalance,
            simTuning = shippedSim,
        )
    }

    private fun DynastyCalendarEntity.toCalendarSnapshot(): DynastySaveValidator.CalendarSnapshot =
        DynastySaveValidator.CalendarSnapshot(
            seasonIndex = seasonIndex,
            phase = phase,
            activeWeekIndex = activeWeekIndex,
            committedSeasonIndex = committedSeasonIndex,
            committedPhase = committedPhase,
            committedWeekIndex = committedWeekIndex,
        )

    /**
     * Signs the wizard contract: creates metadata, coach row, calendar at Offseason week 1.
     */
    suspend fun signNewGameFromWizard(
        draft: NewGameDraft,
        schoolDisplayName: String,
        packId: String = DynastySchema.DEFAULT_PACK_ID,
    ): LoadedDynasty? {
        if (NewGameSignValidator.validate(draft) != null) return null
        val slotIndex = firstEmptySlotIndex() ?: return null
        val dynastyId = UUID.randomUUID().toString()
        val coachName = draft.coach.displayName()
        val shippedBalance = balanceRulesetRepository.ensureShippedDefaultLoaded()
        val shippedSim = simTuningRepository.ensureShippedDefaultLoaded()
        val metadata = DynastyBootstrap.newDynasty(
            dynastyId = dynastyId,
            packId = packId,
            balanceRulesetId = shippedBalance.balanceRulesetId,
            balanceSchemaVersion = shippedBalance.schemaVersion,
            simTuningId = shippedSim.simTuningId,
            simSchemaVersion = shippedSim.schemaVersion,
        )
        val seasonLabel = SeasonMembershipYear.label(metadata.seasonIndex)
        val now = System.currentTimeMillis()

        database.withTransaction {
            metadataDao.upsert(metadata.toEntity())
            coachDao.upsert(draft.toCoachEntity(dynastyId))
            val roster = SeasonOneRosterGenerator.generate(dynastyId)
            rosterDao.upsertAll(roster.toEntities())
            weekCheckpointRepository.insertBootstrapCalendar(metadata)
            slotDao.upsert(
                SaveSlotEntity(
                    slotIndex = slotIndex,
                    dynastyId = dynastyId,
                    lastPlayedAtEpochMillis = now,
                    schoolDisplayName = schoolDisplayName,
                    coachDisplayName = coachName,
                    seasonLabel = seasonLabel,
                    wins = 0,
                    losses = 0,
                    packId = packId,
                ),
            )
        }

        val weekCheckpoint = weekCheckpointRepository.loadOrBootstrap(metadata)
        val summary = SaveSlotSummary(
            slotIndex = slotIndex,
            isEmpty = false,
            dynastyId = dynastyId,
            schoolDisplayName = schoolDisplayName,
            coachDisplayName = coachName,
            seasonLabel = seasonLabel,
            wins = 0,
            losses = 0,
            lastPlayedAtEpochMillis = now,
            packId = packId,
        )
        return LoadedDynasty(
            slotIndex = slotIndex,
            dynastyId = dynastyId,
            summary = summary,
            metadata = metadata,
            weekCheckpoint = weekCheckpoint,
            balanceRuleset = shippedBalance,
            simTuning = shippedSim,
        )
    }

    suspend fun createEmptyDynastyInFirstOpenSlot(
        schoolDisplayName: String,
        coachDisplayName: String,
        packId: String = DynastySchema.DEFAULT_PACK_ID,
    ): LoadedDynasty? {
        val slotIndex = firstEmptySlotIndex() ?: return null
        return createEmptyDynasty(
            slotIndex = slotIndex,
            schoolDisplayName = schoolDisplayName,
            coachDisplayName = coachDisplayName,
            packId = packId,
        )
    }

    /**
     * End of Season CTA — advances into Offseason Week 1 of the next membership year ([planning/season-review.md]).
     */
    /** Advances the active dynasty one calendar week and persists checkpoint + metadata. */
    suspend fun advanceDynastyWeek(slotIndex: Int): DynastyLoadResult {
        require(slotIndex in SaveSlots.INDICES) { "slotIndex out of range" }
        ensureInitialized()
        val loaded = when (val initial = loadSlot(slotIndex)) {
            is DynastyLoadResult.Success -> initial.loaded
            else -> return initial
        }
        weekCheckpointRepository.advanceWeek(loaded.dynastyId, loaded.weekCheckpoint)
        val meta = metadataDao.getById(loaded.dynastyId)?.toDomain()
        if (meta != null) {
            val slotEntity = slotDao.getByIndex(slotIndex)
            if (slotEntity != null) {
                slotDao.upsert(
                    slotEntity.copy(seasonLabel = SeasonMembershipYear.label(meta.seasonIndex)),
                )
            }
        }
        val afterAdvance = when (val again = loadSlot(slotIndex)) {
            is DynastyLoadResult.Success -> again.loaded
            else -> return again
        }
        recruitingEconomyRepository.onCalendarWeekAdvanced(afterAdvance)
        val pos = afterAdvance.weekCheckpoint.activePosition
        if (pos.phase == CalendarPhase.Offseason) {
            portalCycleRepository.onOffseasonWeekStarted(
                afterAdvance.dynastyId,
                afterAdvance.metadata.seasonIndex,
                pos.weekIndex,
            )
        }
        return loadSlot(slotIndex)
    }

    suspend fun completeSeasonReviewEnterOffseason(slotIndex: Int): DynastyLoadResult {
        require(slotIndex in SaveSlots.INDICES) { "slotIndex out of range" }
        ensureInitialized()
        val loaded = when (val initial = loadSlot(slotIndex)) {
            is DynastyLoadResult.Success -> initial.loaded
            else -> return initial
        }
        if (!SeasonReviewGate.canEnterOffseason(loaded.weekCheckpoint)) {
            return DynastyLoadResult.Unreadable(
                slotIndex,
                DynastyLoadFailure.InvalidCalendar("dynasty is not in End of Season week"),
            )
        }
        weekCheckpointRepository.advanceWeek(loaded.dynastyId, loaded.weekCheckpoint)
        val meta = metadataDao.getById(loaded.dynastyId)?.toDomain()
            ?: return DynastyLoadResult.Unreadable(
                slotIndex,
                DynastyLoadFailure.MissingMetadata(loaded.dynastyId),
            )
        val slotEntity = slotDao.getByIndex(slotIndex)
            ?: return DynastyLoadResult.Unreadable(
                slotIndex,
                DynastyLoadFailure.DatabaseRead("slot row missing"),
            )
        slotDao.upsert(
            slotEntity.copy(seasonLabel = SeasonMembershipYear.label(meta.seasonIndex)),
        )
        recruitingEconomyRepository.openNextCycleAfterSeasonReview(
            dynastyId = loaded.dynastyId,
            balance = loaded.balanceRuleset,
            nextSeasonIndex = meta.seasonIndex,
            postRecapPrestige = RecruitingEconomyRepository.DEFAULT_PROGRAM_PRESTIGE,
        )
        return loadSlot(slotIndex)
    }
}
