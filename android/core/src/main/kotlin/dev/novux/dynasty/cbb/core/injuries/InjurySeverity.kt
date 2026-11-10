package dev.novux.dynasty.cbb.core.injuries

enum class InjurySeverity(val wire: String) {
    Minor("minor"),
    Moderate("moderate"),
    Major("major"),
    ;

    companion object {
        fun fromWire(value: String): InjurySeverity? = entries.firstOrNull { it.wire == value }
    }
}
