package dev.novux.dynasty.cbb.core.preseason.tournament

data class TournamentInvitesState(
    val offers: List<EarlySeasonEvent> = emptyList(),
    val resolution: TournamentInviteResolution = TournamentInviteResolution.Unresolved,
) {
    val isResolved: Boolean = resolution !is TournamentInviteResolution.Unresolved

    val slotBudget: RegularSeasonSlotBudget
        get() = RegularSeasonSlotBudget.fromResolution(resolution)
}
