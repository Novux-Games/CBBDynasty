package dev.novux.dynasty.cbb.core.coach.employment

import dev.novux.dynasty.cbb.core.pack.TeamDisplayOverlay
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class JobCenterEngineTest {
    @Test
    fun generatesStableOfferCount() {
        val teams = (1..20).map { i ->
            TeamDisplayOverlay(
                teamId = "t$i",
                conferenceId = "conf-test",
                displayName = "School $i",
                shortName = "S$i",
                homeCity = "City",
                homeState = "OH",
                primaryColorHex = "#000000",
                secondaryColorHex = "#ffffff",
                logoUri = null,
                postseasonEligible = true,
                programPrestige = 30 + i,
            )
        }
        val offers = JobCenterEngine.generateOffers(teams, seasonIndex = 2, dynastyId = "dyn-a")
        assertEquals(JobCenterEngine.OFFERS_PER_WINDOW, offers.size)
        assertTrue(offers.all { it.programPrestige in 1..99 })
    }
}
