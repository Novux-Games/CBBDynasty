package dev.novux.dynasty.cbb.core.coach.staff

import dev.novux.dynasty.cbb.core.coach.card.AssistantCoachCard
import dev.novux.dynasty.cbb.core.coach.card.HeadCoachCard

/** School hub Staff tab model — [planning/coach-card.md#staff-school-hub]. */
data class SchoolStaffRoster(
    val headCoach: HeadCoachCard,
    val assistants: List<AssistantCoachCard>,
    val supportStaff: List<SupportStaffMember>,
)
