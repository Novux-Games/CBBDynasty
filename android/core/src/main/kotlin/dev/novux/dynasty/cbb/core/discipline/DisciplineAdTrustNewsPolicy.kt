package dev.novux.dynasty.cbb.core.discipline

/** Discipline outcomes publish AD trust / job security headlines ([MEGA-SHELL-070]). */
object DisciplineAdTrustNewsPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-070"

    fun headlineForOutcome(playerName: String, outcome: ConferenceDrawOutcome): String =
        when (outcome) {
            ConferenceDrawOutcome.Cleared -> "$playerName cleared by conference review."
            ConferenceDrawOutcome.Dismissed -> "$playerName dismissed from program."
            ConferenceDrawOutcome.ShortSuspension,
            ConferenceDrawOutcome.LongSuspension,
            -> "$playerName suspended after conference draw."
        }
}