package dev.novux.dynasty.cbb.core.coach.skill

/** Head coach skill tree branches ([planning/game-design.md#coaching-carousel--contracts]). */
enum class CoachSkillBranch(val wireKey: String, val label: String) {
    Recruiting("recruiting", "Recruiting"),
    PlayerDevelopment("development", "Player development"),
    Tactics("tactics", "In-game tactics"),
    PortalRetention("portal", "Portal retention"),
    NilCharm("nil", "NIL charm"),
    ;

    companion object {
        fun fromWire(key: String): CoachSkillBranch? = entries.firstOrNull { it.wireKey == key }
    }
}
