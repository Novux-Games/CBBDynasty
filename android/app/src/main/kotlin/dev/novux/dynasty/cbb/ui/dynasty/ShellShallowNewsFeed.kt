package dev.novux.dynasty.cbb.ui.dynasty

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.novux.dynasty.cbb.core.news.NewsHeadline
import dev.novux.dynasty.cbb.data.news.NewsFeedRepository
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository

/** Shallow headlines for shell News tab across career phases ([MEGA-FRAME-049]). */
@Composable
fun rememberShellNewsHeadlines(
    dynastyId: String,
    seasonIndex: Int,
    weekIndex: Int,
    newsFeedRepository: NewsFeedRepository,
    rosterRepository: DynastyRosterRepository,
): List<NewsHeadline> {
    var headlines by remember(dynastyId, seasonIndex) { mutableStateOf<List<NewsHeadline>>(emptyList()) }
    LaunchedEffect(dynastyId, seasonIndex, weekIndex) {
        val roster = rosterRepository.listPlayers(dynastyId)
        headlines = newsFeedRepository.shallowFeed(dynastyId, seasonIndex, weekIndex, roster)
    }
    return headlines
}
