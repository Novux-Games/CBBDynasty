package dev.novux.dynasty.cbb.core.save

import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.PhaseWeekPolicy

/**
 * Validates slot summary vs dynasty header before load. Does not hit the database.
 */
object DynastySaveValidator {
    data class SlotSnapshot(
        val slotIndex: Int,
        val dynastyId: String?,
        val packId: String?,
        val isEmpty: Boolean,
    )

    data class MetadataSnapshot(
        val dynastyId: String,
        val packId: String,
        val seasonIndex: Int,
        val phase: CalendarPhase,
    )

    data class CalendarSnapshot(
        val seasonIndex: Int,
        val phase: CalendarPhase,
        val activeWeekIndex: Int,
        val committedSeasonIndex: Int?,
        val committedPhase: CalendarPhase?,
        val committedWeekIndex: Int?,
    )

    fun validateBeforeLoad(
        slot: SlotSnapshot,
        metadata: MetadataSnapshot?,
    ): DynastyLoadFailure? {
        if (slot.isEmpty || slot.dynastyId.isNullOrBlank()) return null
        val dynastyId = slot.dynastyId
        if (metadata == null) {
            return DynastyLoadFailure.MissingMetadata(dynastyId)
        }
        if (metadata.dynastyId != dynastyId) {
            return DynastyLoadFailure.DynastyIdMismatch(
                slotDynastyId = dynastyId,
                metadataDynastyId = metadata.dynastyId,
            )
        }
        if (slot.packId != null && slot.packId != metadata.packId) {
            return DynastyLoadFailure.PackIdMismatch(
                slotPackId = slot.packId,
                metadataPackId = metadata.packId,
            )
        }
        return null
    }

    fun validateCalendar(calendar: CalendarSnapshot): DynastyLoadFailure? {
        return try {
            validateWeek(
                calendar.seasonIndex,
                calendar.phase,
                calendar.activeWeekIndex,
            )
            if (calendar.committedSeasonIndex != null &&
                calendar.committedPhase != null &&
                calendar.committedWeekIndex != null
            ) {
                validateWeek(
                    calendar.committedSeasonIndex,
                    calendar.committedPhase,
                    calendar.committedWeekIndex,
                )
            }
            null
        } catch (e: IllegalArgumentException) {
            DynastyLoadFailure.InvalidCalendar(e.message ?: "invalid calendar")
        }
    }

    private fun validateWeek(seasonIndex: Int, phase: CalendarPhase, weekIndex: Int) {
        require(seasonIndex >= 1)
        require(weekIndex >= 1)
        require(weekIndex <= PhaseWeekPolicy.weeksInPhase(phase)) {
            "week $weekIndex invalid for $phase"
        }
    }
}
