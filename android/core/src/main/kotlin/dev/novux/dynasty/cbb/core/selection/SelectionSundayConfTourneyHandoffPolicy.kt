package dev.novux.dynasty.cbb.core.selection

/** After league conference tournaments sim, freeze NET + publish Selection Sunday ([MEGA-POST-005]). */
object SelectionSundayConfTourneyHandoffPolicy {
    const val MEGA_ID: String = "MEGA-POST-005"

    fun shouldPublishAfterConfTourneySim(): Boolean = true
}