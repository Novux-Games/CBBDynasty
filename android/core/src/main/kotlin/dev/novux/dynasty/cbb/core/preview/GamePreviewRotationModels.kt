package dev.novux.dynasty.cbb.core.preview

/** Read-only opponent rotation rows for pregame preview ([MEGA-GDD-009]). */
data class GamePreviewRotationLine(
    val slotLabel: String,
    val playerLabel: String,
    val targetMinutes: Int,
    val starter: Boolean,
)

data class GamePreviewRotationSnapshot(
    val teamDisplayName: String,
    val lines: List<GamePreviewRotationLine>,
)