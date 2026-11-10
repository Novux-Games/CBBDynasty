package dev.novux.dynasty.cbb.core.coach.staff

enum class SupportStaffRole(val wire: String, val label: String) {
    HeadTrainer("head_trainer", "Head athletic trainer"),
    StrengthCoach("strength", "Director of strength & conditioning"),
    VideoCoordinator("video", "Video coordinator"),
    DirectorOfOps("ops", "Director of operations"),
    ;

    companion object {
        fun fromWire(value: String): SupportStaffRole? = entries.firstOrNull { it.wire == value }
    }
}
