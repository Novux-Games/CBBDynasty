package dev.novux.dynasty.cbb.ui.dynasty

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.news.ShallowNewsFeedSnapshot
import dev.novux.dynasty.cbb.core.shell.PhaseShellNewsFeedPolicy
import dev.novux.dynasty.cbb.data.news.NewsFeedRepository
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository

/** Shallow headlines for shell News tab across career phases ([MEGA-FRAME-049]). */
@Composable
fun rememberShellNewsFeed(
    dynastyId: String,
    seasonIndex: Int,
    weekIndex: Int,
    newsFeedRepository: NewsFeedRepository,
    rosterRepository: DynastyRosterRepository,
    calendarPhase: CalendarPhase = CalendarPhase.RegularSeason,
    refreshKey: Any? = null,
): ShallowNewsFeedSnapshot {
    @Suppress("unused")
    val phasePolicy = PhaseShellNewsFeedPolicy.MEGA_ID
    var feed by remember(dynastyId, seasonIndex) {
        mutableStateOf(ShallowNewsFeedSnapshot.Empty)
    }
    val offseasonWeek = PhaseShellNewsFeedPolicy.offseasonWeekForFeed(calendarPhase, weekIndex)
    LaunchedEffect(dynastyId, seasonIndex, weekIndex, calendarPhase, offseasonWeek, refreshKey) {
        val roster = rosterRepository.listPlayers(dynastyId)
        feed = newsFeedRepository.shallowFeedSnapshot(
            dynastyId,
            seasonIndex,
            weekIndex,
            roster,
            calendarPhase = calendarPhase,
            offseasonWeekIndex = offseasonWeek,
        )
    }
    return feed
}
