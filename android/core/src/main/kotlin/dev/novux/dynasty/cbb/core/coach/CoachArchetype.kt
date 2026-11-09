package dev.novux.dynasty.cbb.core.coach

/** Wizard archetype — [planning/game-start.md#step-2--archetype]. */
enum class CoachArchetype(val wireId: String, val label: String, val blurb: String) {
    Recruiter("recruiter", "Recruiter", "Pipeline and portal charm"),
    SchemeManager("scheme_manager", "Scheme Manager", "Tactics and scouting"),
    Developer("developer", "Developer", "Player growth and summer gains"),
    ProgramBuilder("program_builder", "Program Builder", "Culture and retention"),
    Balanced("balanced", "Balanced", "Even baseline — no spike"),
    ;

    companion object {
        fun fromWire(id: String): CoachArchetype? = entries.firstOrNull { it.wireId == id }
    }
}
