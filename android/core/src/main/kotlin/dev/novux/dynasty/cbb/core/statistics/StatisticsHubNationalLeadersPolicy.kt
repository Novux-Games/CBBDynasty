package dev.novux.dynasty.cbb.core.statistics

/**
 * Statistics hub surfaces national leaderboards in-season ([MEGA-SHELL-151], [MEGA-SHELL-096]).
 */
object StatisticsHubNationalLeadersPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-151"

    fun nationalLeadersLive(snapshot: StatisticsHubSnapshot): Boolean =
        StatisticsHubLiveContentPolicy.showLeaderboardUi(snapshot)
}