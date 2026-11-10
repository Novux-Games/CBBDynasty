package dev.novux.dynasty.cbb.core.news

import kotlinx.serialization.Serializable

enum class NewsHeadlineKind {
    UserInjury,
    LeagueInjury,
    LeagueResult,
    RecruitingSpotlight,
    General,
}

@Serializable
data class NewsHeadline(
    val id: String,
    val seasonIndex: Int,
    val rsWeekIndex: Int? = null,
    val kind: NewsHeadlineKind,
    val title: String,
    val priority: Int = 0,
    val createdAtMs: Long = 0L,
)
