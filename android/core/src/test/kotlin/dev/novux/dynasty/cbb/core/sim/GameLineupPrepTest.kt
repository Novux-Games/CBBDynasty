package dev.novux.dynasty.cbb.core.sim

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameLineupPrepTest {
    @Test
    fun prepUsesFiveStartersAndRotationMinutes() {
        val roster = (1..10).map { i ->
            val pos = PlayerPosition.entries[i % 5]
            RosterPlayer(
                playerId = "p$i",
                dynastyId = "d",
                position = pos,
                classYear = PlayerClassYear.Junior,
                isStarter = i <= 5,
                depthOrder = i,
                inTransferPortal = false,
                ovr = 60 + i,
                off = 60 + i,
                def = 58,
            )
        }
        val prep = GameLineupPrep.build("team", roster)
        assertEquals(200, prep.participants.sumOf { it.targetMinutes })
        assertEquals(5, prep.participants.count { it.targetMinutes >= 30 })
    }

    @Test
    fun userInterchangeHonorsSlotPolicy() {
        val pg = RosterPlayer(
            playerId = "pg1",
            dynastyId = "d",
            position = PlayerPosition.PointGuard,
            classYear = PlayerClassYear.Senior,
            isStarter = true,
            depthOrder = 1,
            inTransferPortal = false,
            ovr = 90,
            off = 90,
            def = 70,
        )
        val sg = RosterPlayer(
            playerId = "sg1",
            dynastyId = "d",
            position = PlayerPosition.ShootingGuard,
            classYear = PlayerClassYear.Senior,
            isStarter = true,
            depthOrder = 2,
            inTransferPortal = false,
            ovr = 72,
            off = 72,
            def = 70,
        )
        val prep = GameLineupPrep.build(
            "team",
            listOf(pg, sg),
            userStarters = mapOf(
                PlayerPosition.PointGuard to "pg1",
                PlayerPosition.ShootingGuard to "pg1",
            ),
        )
        assertTrue(prep.participants.any { it.playerId == "pg1" && it.slotPlayed == PlayerPosition.ShootingGuard })
    }
}
