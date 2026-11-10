package dev.novux.dynasty.cbb.core.player.loyalty

enum class LoyaltyPersonalityTag(val wire: String) {
    ProgramGuy("program_guy"),
    HometownKid("hometown_kid"),
    OneAndDoneLean("one_and_done"),
    Mercurial("mercurial"),
    ;

    companion object {
        fun fromWire(wire: String): LoyaltyPersonalityTag? =
            entries.firstOrNull { it.wire == wire }

        fun pickForSeed(playerId: String, archetypeWire: String): LoyaltyPersonalityTag {
            val hash = (playerId.hashCode() xor archetypeWire.hashCode()).absoluteValue
            return when (hash % 4) {
                0 -> ProgramGuy
                1 -> HometownKid
                2 -> OneAndDoneLean
                else -> Mercurial
            }
        }

        private val Int.absoluteValue: Int get() = if (this < 0) -this else this
    }
}
