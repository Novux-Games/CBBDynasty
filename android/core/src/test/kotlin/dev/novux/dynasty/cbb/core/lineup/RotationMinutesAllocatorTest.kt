package dev.novux.dynasty.cbb.core.lineup

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class RotationMinutesAllocatorTest {
    @Test
    fun minutesSumToTwoHundred() {
        val players = fullRoster()
        val starters = DefaultStarterAlgorithm.pickStarters(players)
        val minutes = RotationMinutesAllocator.allocate(players, starters)
        assertEquals(200, minutes.values.sum())
    }

    @Test
    fun injuredExcluded() {
        val players = fullRoster().map {
            if (it.position == PlayerPosition.Center) {
                it.copy(injuryActive = true, injuryWeeksRemaining = 3)
            } else {
                it
            }
        }
        val starters = DefaultStarterAlgorithm.pickStarters(players)
        assertFalse(starters.containsValue(players.first { it.position == PlayerPosition.Center }.playerId))
    }

    private fun fullRoster(): List<RosterPlayer> {
        val starters = PlayerPosition.entries.map { pos ->
            RosterPlayer(
                playerId = pos.wire,
                dynastyId = "d",
                position = pos,
                classYear = PlayerClassYear.Sophomore,
                isStarter = false,
                depthOrder = 1,
                inTransferPortal = false,
                ovr = 70 + pos.ordinal,
                off = 70 + pos.ordinal,
                def = 68,
            )
        }
        val bench = (1..5).map { i ->
            RosterPlayer(
                playerId = "bench$i",
                dynastyId = "d",
                position = PlayerPosition.ShootingGuard,
                classYear = PlayerClassYear.Freshman,
                isStarter = false,
                depthOrder = 5 + i,
                inTransferPortal = false,
                ovr = 60,
                off = 60,
                def = 58,
            )
        }
        return starters + bench
    }
}
