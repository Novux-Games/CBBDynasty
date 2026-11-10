package dev.novux.dynasty.cbb.core.strategy

/**
 * Default season O/D schemes — [planning/strategy-schemes.md].
 */
enum class OffensiveSchemePreset(val wireId: String, val label: String) {
    Balanced("balanced", "Balanced"),
    Motion("motion", "Motion"),
    Drive("drive", "Drive"),
    Set("set", "Set"),
    Early("early", "Early"),
    Perimeter("perimeter", "Perimeter"),
    Post("post", "Post"),
    ;

    companion object {
        fun fromWire(id: String): OffensiveSchemePreset? = entries.firstOrNull { it.wireId == id }
    }
}

enum class DefensiveSchemePreset(val wireId: String, val label: String) {
    ManToMan("man", "Man-to-Man"),
    Zone23("zone_2_3", "2-3 Zone"),
    Zone32("zone_3_2", "3-2 Zone"),
    Zone131("zone_1_3_1", "1-3-1 Zone"),
    BoxAndOne("box_and_one", "Box-and-One"),
    FullCourtPress("press", "Full-court press"),
    SwitchingMan("switching", "Switching man"),
    ;

    companion object {
        fun fromWire(id: String): DefensiveSchemePreset? = entries.firstOrNull { it.wireId == id }
    }
}
