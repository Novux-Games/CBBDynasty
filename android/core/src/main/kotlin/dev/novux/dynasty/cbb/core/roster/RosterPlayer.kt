package dev.novux.dynasty.cbb.core.roster

data class RosterPlayer(
    val playerId: String,
    val dynastyId: String,
    val position: PlayerPosition,
    val classYear: PlayerClassYear,
    val isStarter: Boolean,
    val depthOrder: Int,
    val inTransferPortal: Boolean,
    val declaredForDraft: Boolean = false,
    val draftStockPoints: Int = 0,
    val proDayCompleted: Boolean = false,
) {
    fun displayLabel(): String = "${position.label} · ${classYear.label}"
}
