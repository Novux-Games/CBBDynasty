package dev.novux.dynasty.cbb.core.coach.card

import dev.novux.dynasty.cbb.core.coach.staff.StaffKind
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CoachCardPolicyTest {
    @Test
    fun supportStaffHasNoCard() {
        assertFalse(CoachCardPolicy.eligibleForCoachCard(StaffKind.Support))
    }

    @Test
    fun coachTrackHasCard() {
        assertTrue(CoachCardPolicy.eligibleForCoachCard(StaffKind.CoachTrack))
    }
}
