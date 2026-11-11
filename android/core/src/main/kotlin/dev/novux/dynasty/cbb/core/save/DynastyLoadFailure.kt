package dev.novux.dynasty.cbb.core.save

/**
 * Why an occupied slot could not be loaded. Normal play should not produce these;
 * UI offers delete as a fallback ([planning/game-start.md#continue--load]).
 */
sealed class DynastyLoadFailure {
    data class MissingMetadata(val dynastyId: String) : DynastyLoadFailure()

    data class DynastyIdMismatch(
        val slotDynastyId: String,
        val metadataDynastyId: String?,
    ) : DynastyLoadFailure()

    data class PackIdMismatch(
        val slotPackId: String?,
        val metadataPackId: String,
    ) : DynastyLoadFailure()

    data class InvalidCalendar(val detail: String) : DynastyLoadFailure()

    data class InvalidBalanceRuleset(val detail: String) : DynastyLoadFailure()

    data class InvalidSimTuning(val detail: String) : DynastyLoadFailure()

    data class DatabaseRead(val detail: String) : DynastyLoadFailure()

    /** Wizard sign left coach/roster in a non-atomic state ([MEGA-QA-029]). */
    data class IncompleteNewGameSign(val detail: String) : DynastyLoadFailure()
}

sealed class DynastyLoadResult {
    data object SlotEmpty : DynastyLoadResult()

    data class Success(val loaded: LoadedDynasty) : DynastyLoadResult()

    data class WeekAdvanceBlocked(
        val slotIndex: Int,
        val reason: DynastyWeekAdvanceBlockReason,
        val loaded: LoadedDynasty,
    ) : DynastyLoadResult()

    data class Unreadable(
        val slotIndex: Int,
        val failure: DynastyLoadFailure,
    ) : DynastyLoadResult()
}
