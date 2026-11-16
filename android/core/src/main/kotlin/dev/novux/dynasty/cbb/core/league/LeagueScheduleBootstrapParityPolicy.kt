package dev.novux.dynasty.cbb.core.league

import dev.novux.dynasty.cbb.core.pack.ConferenceDisplayOverlay

/**
 * League-wide RS bootstrap shares pack conference caps with the user preseason solver;
 * user path may add exhibitions / events ([MEGA-WORLD-065]).
 */
object LeagueScheduleBootstrapParityPolicy {
    const val MEGA_ID = "MEGA-WORLD-065"

    fun sharedLeagueGameCount(conference: ConferenceDisplayOverlay): Int = conference.leagueGameCount
}