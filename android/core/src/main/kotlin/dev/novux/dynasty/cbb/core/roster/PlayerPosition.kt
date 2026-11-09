package dev.novux.dynasty.cbb.core.roster

/** Lineup slots — [planning/positions-and-lineup.md]. */
enum class PlayerPosition(val wire: String, val label: String) {
    PointGuard("pg", "PG"),
    ShootingGuard("sg", "SG"),
    SmallForward("sf", "SF"),
    PowerForward("pf", "PF"),
    Center("c", "C"),
    ;

    companion object {
        val STARTER_ORDER: List<PlayerPosition> = entries

        fun fromWire(value: String): PlayerPosition? = entries.firstOrNull { it.wire == value }
    }
}
