package dev.novux.dynasty.cbb.core.march

/** March Mayhem hub surfaces NCBA/NCI/Heritage bracket rails ([MEGA-GDD-129]). */
object PostseasonBracketMonitorPolicy {
    const val MEGA_ID: String = "MEGA-GDD-129"

    val monitoredEvents: List<String> = listOf("NCBA", "NCI", "Heritage")
}