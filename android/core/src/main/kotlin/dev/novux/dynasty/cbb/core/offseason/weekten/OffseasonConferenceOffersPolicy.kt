package dev.novux.dynasty.cbb.core.offseason.weekten

object OffseasonConferenceOffersPolicy {
    /** Season 1 skips week 10 entirely ([planning/offseason.md]). */
    fun skipConferenceOffersWeek(seasonIndex: Int): Boolean = seasonIndex <= 1

    fun canAdvanceWeek(
        offers: List<ConferenceOfferCard>,
        decisions: Map<String, ConferenceOfferDecision>,
    ): Boolean {
        if (offers.isEmpty()) return true
        return offers.all { decisions[it.offerId] == ConferenceOfferDecision.Accepted ||
            decisions[it.offerId] == ConferenceOfferDecision.Declined }
    }
}
