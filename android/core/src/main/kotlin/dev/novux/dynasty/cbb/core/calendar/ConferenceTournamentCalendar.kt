package dev.novux.dynasty.cbb.core.calendar

import dev.novux.dynasty.cbb.core.calendar.bye.ConferenceByeWeeks
import dev.novux.dynasty.cbb.core.pack.IdentityPack

/**
 * League-wide conference tournament week count = deepest bracket in the identity pack ([planning/time-system.md]).
 */
object ConferenceTournamentCalendar {
    fun roundsInTemplate(pack: IdentityPack, templateId: String): Int =
        pack.tournamentTemplates.first { it.id == templateId }.rounds.size

    fun leagueMaxConfTourneyWeeks(pack: IdentityPack): Int =
        pack.conferences
            .mapNotNull { it.tournamentTemplateId }
            .map { roundsInTemplate(pack, it) }
            .maxOrNull()
            ?: 1

    fun roundsForConference(pack: IdentityPack, conferenceId: String): Int {
        val conf = pack.conferences.first { it.id == conferenceId }
        val templateId = requireNotNull(conf.tournamentTemplateId) {
            "conference $conferenceId has no tournament template"
        }
        return roundsInTemplate(pack, templateId)
    }

    /** Weeks this conference sits on bye while other leagues finish deeper brackets. */
    fun byeWeeksForConference(pack: IdentityPack, conferenceId: String): List<Int> {
        val maxWeeks = leagueMaxConfTourneyWeeks(pack)
        val rounds = roundsForConference(pack, conferenceId)
        return ConferenceByeWeeks.byeWeekIndices(maxWeeks, rounds)
    }
}
