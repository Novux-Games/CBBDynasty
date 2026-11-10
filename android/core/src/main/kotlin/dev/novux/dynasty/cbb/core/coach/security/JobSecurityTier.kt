package dev.novux.dynasty.cbb.core.coach.security

enum class JobSecurityTier(val label: String) {
    Secure("Secure"),
    Stable("Stable"),
    Watch("Watch"),
    HotSeat("Hot seat"),
    Critical("Critical"),
    ;

    companion object {
        fun fromScore(score: Int): JobSecurityTier = when (score.coerceIn(0, 100)) {
            in 80..100 -> Secure
            in 60..79 -> Stable
            in 40..59 -> Watch
            in 20..39 -> HotSeat
            else -> Critical
        }
    }
}
