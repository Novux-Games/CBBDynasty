package dev.novux.dynasty.cbb.core.coach.security

enum class JobSecurityTrend(val wire: String, val symbol: String) {
    Up("up", "↑"),
    Down("down", "↓"),
    Flat("flat", "—"),
    ;

    companion object {
        fun fromWire(value: String): JobSecurityTrend =
            entries.firstOrNull { it.wire == value } ?: Flat
    }
}
