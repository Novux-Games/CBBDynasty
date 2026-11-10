package dev.novux.dynasty.cbb.core.save

import dev.novux.dynasty.cbb.core.coach.CoachCreateValidator

object NewGameSignValidator {
    sealed class Error {
        data object IncompleteCoach : Error()
        data object NoSchool : Error()
        data object SchoolNotReviewed : Error()
    }

    fun validate(draft: NewGameDraft): Error? {
        if (CoachCreateValidator.validate(draft.coach) != null) return Error.IncompleteCoach
        if (draft.selectedTeamId.isNullOrBlank()) return Error.NoSchool
        if (!draft.schoolSummaryAcknowledged) return Error.SchoolNotReviewed
        return null
    }
}
