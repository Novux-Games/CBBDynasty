package dev.novux.dynasty.cbb.core.player

/**
 * In-season injury — [planning/injuries.md#data-model], player-card strip fields.
 */
data class PlayerInjuryState(
    val injuryActive: Boolean,
    val injuryId: String?,
    val weeksRemaining: Int,
    val label: String?,
    val severityWire: String? = null,
    val occurredAtRsWeek: Int = 0,
    val occurredAtRsGameIndex: Int = 0,
    val rsGamesPlayedAtInjury: Int = 0,
    val wasStarterSlotWire: String? = null,
    val priorRotationBand: Int? = null,
) {
    init {
        require(weeksRemaining >= 0)
        if (injuryActive) {
            require(weeksRemaining > 0)
            require(!label.isNullOrBlank())
        }
    }

    fun injuryStripOrNull(): PlayerInjuryStrip? {
        if (!injuryActive || weeksRemaining <= 0) return null
        val text = label ?: return null
        return PlayerInjuryStrip(chip = "OUT", label = text, weeksRemaining = weeksRemaining)
    }

    companion object {
        val Healthy = PlayerInjuryState(
            injuryActive = false,
            injuryId = null,
            weeksRemaining = 0,
            label = null,
        )
    }
}

data class PlayerInjuryStrip(
    val chip: String,
    val label: String,
    val weeksRemaining: Int,
) {
    fun rosterRowSuffix(): String = "$label · ${weeksRemaining}w"
}
