package dev.novux.dynasty.cbb.core.coach.staff

enum class CoachTrackRole(val wire: String, val label: String) {
    AssociateHeadCoach("associate_hc", "Associate head coach"),
    Assistant("assistant", "Assistant coach"),
    RecruitingCoordinator("recruiting_coord", "Recruiting coordinator"),
    GraduateAssistant("grad_assistant", "Graduate assistant"),
    ;

    companion object {
        fun fromWire(value: String): CoachTrackRole? = entries.firstOrNull { it.wire == value }
    }
}
