package dev.novux.dynasty.cbb.core.selection

/** Final NET refresh after conference tournaments, before Selection Sunday ([MEGA-POST-021], [MEGA-POST-143]). */
object SelectionSundayNetFreezePolicy {
    const val MEGA_ID: String = "MEGA-POST-021"
    const val CONF_HANDOFF_ID: String = "MEGA-POST-007"
    const val WIRE_COMPUTE_ID: String = "MEGA-POST-142"
    const val FINAL_FREEZE_ID: String = "MEGA-POST-143"

    fun refreshNetBeforeSelectionSunday(): Boolean = true
}