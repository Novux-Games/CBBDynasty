package dev.novux.dynasty.cbb.core.preseason.tournament

sealed class TournamentInviteResolution {
    data object Unresolved : TournamentInviteResolution()

    /** No cards offered — user acknowledged. */
    data object NoInvitesAcknowledged : TournamentInviteResolution()

    /** Invites offered but user passed on all. */
    data object DeclinedAll : TournamentInviteResolution()

    data class Accepted(val event: EarlySeasonEvent) : TournamentInviteResolution()
}
