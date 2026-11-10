package dev.novux.dynasty.cbb.core.rankings

data class RankingsHubTeamRow(
    val teamId: String,
    val displayName: String,
    val rank: Int,
    val top25Highlight: Boolean,
)

data class ConferenceTeamRecordRow(
    val teamId: String,
    val displayName: String,
    val wins: Int,
    val losses: Int,
)

data class ConferenceStandingsGroup(
    val conferenceId: String,
    val conferenceName: String,
    val teams: List<ConferenceTeamRecordRow>,
)

data class RankingsHubSnapshot(
    val published: Boolean,
    val net: List<RankingsHubTeamRow>,
    val media: List<RankingsHubTeamRow>,
    val coaches: List<RankingsHubTeamRow>,
    val conferences: List<ConferenceStandingsGroup>,
)
