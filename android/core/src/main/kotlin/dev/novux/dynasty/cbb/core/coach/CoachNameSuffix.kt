package dev.novux.dynasty.cbb.core.coach

/** Wizard suffix dropdown — [planning/game-start.md#step-1--coach-info--appearance]. */
enum class CoachNameSuffix(val wireValue: String?, val displayToken: String?) {
    None(null, null),
    Jr("Jr", "Jr."),
    Second("II", "II"),
    Third("III", "III"),
    Fourth("IV", "IV"),
    ;

    companion object {
        fun fromWire(value: String?): CoachNameSuffix =
            entries.firstOrNull { it.wireValue == value } ?: None
    }
}
