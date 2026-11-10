package dev.novux.dynasty.cbb.core.calendar

import dev.novux.dynasty.cbb.core.pack.ConferencePack
import dev.novux.dynasty.cbb.core.pack.IdentityPack
import dev.novux.dynasty.cbb.core.pack.IdentityPackCodec
import dev.novux.dynasty.cbb.core.pack.PackLoadResult
import dev.novux.dynasty.cbb.core.pack.TeamColorsPack
import dev.novux.dynasty.cbb.core.pack.TeamPack
import dev.novux.dynasty.cbb.core.pack.TournamentRoundPack
import dev.novux.dynasty.cbb.core.pack.TournamentTemplatePack
import org.junit.Assert.assertEquals
import org.junit.Test

class ConferenceTournamentCalendarTest {
    @Test
    fun leagueMaxConfWeeks_equalsDeepestTemplateRoundCount() {
        val pack = loadMinimalValid()
        assertEquals(4, ConferenceTournamentCalendar.leagueMaxConfTourneyWeeks(pack))
    }

    @Test
    fun shortConference_byesFinalWeekWhileOthersPlay() {
        val pack = packWithMixedBracketDepths()
        assertEquals(5, ConferenceTournamentCalendar.leagueMaxConfTourneyWeeks(pack))
        assertEquals(listOf(5), ConferenceTournamentCalendar.byeWeeksForConference(pack, "conf-short"))
        assertEquals(emptyList<Int>(), ConferenceTournamentCalendar.byeWeeksForConference(pack, "conf-deep"))
    }

    private fun loadMinimalValid(): IdentityPack {
        val json = this::class.java.getResource("/pack/minimal-valid.json")!!.readText()
        return (IdentityPackCodec.parseAndValidate(json) as PackLoadResult.Success).pack
    }

    private fun packWithMixedBracketDepths(): IdentityPack {
        val fourRounds = listOf(
            TournamentRoundPack("R1", 4),
            TournamentRoundPack("QF", 2),
            TournamentRoundPack("SF", 2),
            TournamentRoundPack("F", 1),
        )
        val fiveRounds = fourRounds + TournamentRoundPack("Consolation", 1)
        return IdentityPack(
            packVersion = 1,
            membershipYear = "2026-27",
            disclaimerAcceptedRequired = true,
            rulesetYear = 2026,
            conferences = listOf(
                ConferencePack(
                    id = "conf-short",
                    displayName = "Short",
                    leagueGameCount = 18,
                    tournamentTemplateId = "tpl-4",
                ),
                ConferencePack(
                    id = "conf-deep",
                    displayName = "Deep",
                    leagueGameCount = 18,
                    tournamentTemplateId = "tpl-5",
                ),
            ),
            teams = listOf(
                TeamPack(
                    id = "team-a",
                    conferenceId = "conf-short",
                    displayName = "A",
                    shortName = "A",
                    homeCity = "X",
                    homeState = "Y",
                    postseasonEligible = true,
                    colors = TeamColorsPack("#000", "#fff"),
                    arenaBaseName = "Arena",
                ),
            ),
            tournamentTemplates = listOf(
                TournamentTemplatePack(
                    id = "tpl-4",
                    bracketSize = 8,
                    seeds = 8,
                    sitePolicy = "neutral",
                    qualificationPolicy = "all",
                    rounds = fourRounds,
                ),
                TournamentTemplatePack(
                    id = "tpl-5",
                    bracketSize = 16,
                    seeds = 16,
                    sitePolicy = "neutral",
                    qualificationPolicy = "all",
                    rounds = fiveRounds,
                ),
            ),
        )
    }
}
