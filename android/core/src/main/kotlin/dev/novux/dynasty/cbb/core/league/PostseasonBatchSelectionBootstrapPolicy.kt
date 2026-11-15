package dev.novux.dynasty.cbb.core.league

/** Postseason batch tries selection bootstrap before aborting ([MEGA-POST-006]). */
object PostseasonBatchSelectionBootstrapPolicy {
    const val MEGA_ID: String = "MEGA-POST-006"

    fun mayAdvanceWithoutSelection(): Boolean = false
}