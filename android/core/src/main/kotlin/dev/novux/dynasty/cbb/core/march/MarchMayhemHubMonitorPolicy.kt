package dev.novux.dynasty.cbb.core.march

/** March Mayhem hub monitors NCBA / NCI / Heritage rails ([MEGA-GDD-129], [MEGA-POST-054]). */
object MarchMayhemHubMonitorPolicy {
    const val MEGA_ID: String = "MEGA-GDD-129"
    const val POST_SUBTITLE_ID: String = "MEGA-POST-054"

    val monitorTabs: List<String> = listOf("overview", "ncba", "nci", "heritage")

    fun showRailSubtitle(): Boolean = true
}