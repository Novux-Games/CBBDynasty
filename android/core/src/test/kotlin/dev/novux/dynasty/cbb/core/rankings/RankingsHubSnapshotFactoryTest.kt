package dev.novux.dynasty.cbb.core.rankings

import dev.novux.dynasty.cbb.core.league.LeagueStandingsState
import dev.novux.dynasty.cbb.core.league.LeagueTeamRecord
import dev.novux.dynasty.cbb.core.pack.IdentityPack
import dev.novux.dynasty.cbb.core.pack.IdentityPackCodec
import dev.novux.dynasty.cbb.core.pack.IdentityPackIndex
import dev.novux.dynasty.cbb.core.pack.PackLoadResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RankingsHubSnapshotFactoryTest {
  private val pack: IdentityPack
    get() {
      val json = javaClass.classLoader.getResourceAsStream("pack/minimal-valid.json")!!
        .bufferedReader().readText()
      return (IdentityPackCodec.parseAndValidate(json) as PackLoadResult.Success).pack
    }

  @Test
  fun buildsNationalAndConferenceTabsFromPack() {
    val index = IdentityPackIndex.from(pack)
    val snapshot = RankingsHubSnapshotFactory.build(index, standings = null, rankingsPublished = true)
    assertTrue(snapshot.net.isNotEmpty())
    assertEquals(snapshot.net.size, snapshot.media.size)
    assertTrue(snapshot.conferences.isNotEmpty())
    assertFalse(snapshot.net.any { it.top25Highlight })
  }

  @Test
  fun honorsPersistedPollRanks() {
    val index = IdentityPackIndex.from(pack)
    val teamId = index.allTeamDisplays().first().teamId
    val standings = LeagueStandingsState(
      seasonIndex = 1,
      records = mapOf(teamId to LeagueTeamRecord(teamId, 2, 0)),
      netRankByTeam = mapOf(teamId to 1),
      mediaPollRankByTeam = mapOf(teamId to 1),
      coachesPollRankByTeam = mapOf(teamId to 1),
    )
    val snapshot = RankingsHubSnapshotFactory.build(index, standings, rankingsPublished = true)
    assertEquals(1, snapshot.net.first { it.teamId == teamId }.rank)
    assertTrue(snapshot.media.first { it.teamId == teamId }.top25Highlight)
  }
}
