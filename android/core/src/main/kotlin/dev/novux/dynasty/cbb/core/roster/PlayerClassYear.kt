package dev.novux.dynasty.cbb.core.roster

enum class PlayerClassYear(val wire: String, val label: String) {
    Freshman("fr", "Fr"),
    Sophomore("so", "So"),
    Junior("jr", "Jr"),
    Senior("sr", "Sr"),
    WalkOn("wo", "Walk-on"),
    ;

    companion object {
        fun fromWire(value: String): PlayerClassYear? = entries.firstOrNull { it.wire == value }
    }
}
