package dev.novux.dynasty.cbb.core.coach.security

enum class AdTrustBand(val label: String) {
    Low("Low"),
    Steady("Steady"),
    Strong("Strong"),
    ;

    companion object {
        fun fromScore(score: Int): AdTrustBand = when (score.coerceIn(0, 100)) {
            in 70..100 -> Strong
            in 40..69 -> Steady
            else -> Low
        }
    }
}
