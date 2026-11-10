package dev.novux.dynasty.cbb.data.preseason

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.preseason.PreseasonSeasonStartPolicy
import dev.novux.dynasty.cbb.core.preseason.PreseasonStartGate
import dev.novux.dynasty.cbb.core.preseason.SeasonStartFocus
import dev.novux.dynasty.cbb.core.save.DynastyLoadFailure
import dev.novux.dynasty.cbb.core.save.DynastyLoadResult
import dev.novux.dynasty.cbb.data.league.LeagueScheduleRepository
import dev.novux.dynasty.cbb.data.save.SaveSlotRepository

class PreseasonSeasonStartRepository(
    private val saveSlotRepository: SaveSlotRepository,
    private val progressRepository: PreseasonProgressRepository,
    private val scheduleRepository: PreseasonScheduleRepository,
    private val leagueScheduleRepository: LeagueScheduleRepository,
    private val earlySeasonBracketRepository: EarlySeasonBracketRepository,
) {
    suspend fun startSeason(slotIndex: Int): DynastyLoadResult {
        val loaded = when (val initial = saveSlotRepository.loadSlot(slotIndex)) {
            is DynastyLoadResult.Success -> initial.loaded
            else -> return initial
        }
        if (loaded.weekCheckpoint.activePosition.phase != CalendarPhase.Preseason) {
            return DynastyLoadResult.Unreadable(slotIndex, DynastyLoadFailure.InvalidCalendar("not preseason"))
        }
        val progress = progressRepository.load(loaded.dynastyId, loaded.metadata.seasonIndex)
        if (!PreseasonStartGate.canStartSeason(progress)) {
            return DynastyLoadResult.Unreadable(slotIndex, DynastyLoadFailure.InvalidCalendar("preseason incomplete"))
        }
        val exhibitionCount = scheduleRepository.loadExhibitionCount(
            loaded.dynastyId,
            loaded.metadata.seasonIndex,
        )
        val plan = scheduleRepository.loadPlan(loaded.dynastyId, loaded.metadata.seasonIndex)
        val focus = PreseasonSeasonStartPolicy.resolve(
            loaded.metadata.seasonIndex,
            exhibitionCount,
            plan,
        )
        leagueScheduleRepository.generateAndPersist(loaded.dynastyId, loaded.metadata.seasonIndex)
        earlySeasonBracketRepository.bootstrapForSeason(loaded.dynastyId, loaded.metadata.seasonIndex)
        return saveSlotRepository.enterInSeasonFromPreseason(slotIndex, focus)
    }
}
