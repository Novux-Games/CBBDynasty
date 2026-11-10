package dev.novux.dynasty.cbb.core.player

data class PlayerRosterRole(
    val isStarter: Boolean,
    val depthOrder: Int,
    val inTransferPortal: Boolean,
    val declaredForDraft: Boolean,
    val draftStockPoints: Int,
    val proDayCompleted: Boolean,
    val starterSlotWire: String? = null,
    val rsGamesPlayedSeason: Int = 0,
)
