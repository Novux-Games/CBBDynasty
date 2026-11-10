package dev.novux.dynasty.cbb.core.offseason.weekten

data class ConferenceOfferCard(
    val offerId: String,
    val invitingConferenceId: String,
    val invitingConferenceName: String,
    val currentConferenceName: String,
    val inviteScore: Int,
    val prosSummary: String,
    val consSummary: String,
)
