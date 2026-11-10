package dev.novux.dynasty.cbb.core.coach.card

import dev.novux.dynasty.cbb.core.coach.staff.StaffKind

object CoachCardPolicy {
    fun eligibleForCoachCard(kind: StaffKind): Boolean = kind == StaffKind.CoachTrack
}
