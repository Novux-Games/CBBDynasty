package dev.novux.dynasty.cbb.core.lineup

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertEquals
import org.junit.Test

class RotationMinutesBalancerTest {
    @Test
    fun adjustKeepsTeamCap() {
        val starters = mapOf(
            PlayerPosition.PointGuard to "p1",
            PlayerPosition.ShootingGuard to "p2",
            PlayerPosition.SmallForward to "p3",
            PlayerPosition.PowerForward to "p4",
            PlayerPosition.Center to "p5",
        )
        val players = (1..6).map { i ->
            RosterPlayer(
                playerId = "p$i",
                dynastyId = "d",
                position = PlayerPosition.ShootingGuard,
                classYear = PlayerClassYear.Sophomore,
                isStarter = i <= 5,
                depthOrder = i,
                inTransferPortal = false,
            )
        }
        val base = RotationMinutesBalancer.baseline(players, starters)
        assertEquals(RotationMinutesAllocator.TEAM_MINUTES_REGULATION, base.values.sum())
        val bumped = RotationMinutesBalancer.adjust(base, "p6", +5)
        assertEquals(RotationMinutesAllocator.TEAM_MINUTES_REGULATION, bumped.values.sum())
        assertEquals(true, (bumped["p6"] ?: 0) > (base["p6"] ?: 0))
    }
}
