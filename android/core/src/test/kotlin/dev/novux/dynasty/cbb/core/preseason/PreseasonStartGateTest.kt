package dev.novux.dynasty.cbb.core.preseason

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PreseasonStartGateTest {
    @Test
    fun blocksUntilAllEightDone() {
        val partial = PreseasonTaskProgress(
            completed = setOf(
                PreseasonTaskId.IncomingClassSeed,
                PreseasonTaskId.TournamentInvites,
            ),
        )
        assertFalse(PreseasonStartGate.canStartSeason(partial))
        assertTrue(
            PreseasonStartGate.canStartSeason(
                PreseasonTaskProgress(completed = PreseasonTaskId.entries.toSet()),
            ),
        )
    }

    @Test
    fun scheduleTasksNeedTournamentInvitesFirst() {
        val progress = PreseasonTaskProgress()
        assertFalse(progress.canComplete(PreseasonTaskId.ScheduleExhibitions))
        val afterSix = progress.withComplete(PreseasonTaskId.TournamentInvites)
        assertTrue(afterSix.canComplete(PreseasonTaskId.ScheduleExhibitions))
    }
}
