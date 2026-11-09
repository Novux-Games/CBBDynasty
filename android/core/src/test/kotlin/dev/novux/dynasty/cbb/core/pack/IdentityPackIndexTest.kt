package dev.novux.dynasty.cbb.core.pack

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class IdentityPackIndexTest {
    private val pack: IdentityPack
        get() {
            val json = javaClass.classLoader.getResourceAsStream("pack/minimal-valid.json")!!
                .bufferedReader().readText()
            return (IdentityPackCodec.parseAndValidate(json) as PackLoadResult.Success).pack
        }

    @Test
    fun lookup_teamAndConference_byStableId() {
        val index = IdentityPackIndex.from(pack)
        val team = index.team("team-001")!!
        assertEquals("State University", team.displayName)
        assertEquals("conf-sec", team.conferenceId)
        assertEquals("Southeast Conference", index.conference("conf-sec")!!.displayName)
    }

    @Test
    fun swap_changesDisplay_notIds() {
        val original = PackOverlaySessions.fromValidatedPack(pack)
        val renamed = pack.copy(
            teams = pack.teams.map {
                if (it.id == "team-001") it.copy(displayName = "Renamed U") else it
            },
        )
        val swap = PackOverlaySessions.swap(original, renamed)
        assertEquals("team-001", swap.session.teamDisplay("team-001")!!.teamId)
        assertEquals("Renamed U", swap.session.teamDisplay("team-001")!!.displayName)
        assertNotEquals(
            original.teamDisplay("team-001")!!.displayName,
            swap.session.teamDisplay("team-001")!!.displayName,
        )
    }
}
