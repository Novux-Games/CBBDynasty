package dev.novux.dynasty.cbb.ui.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.CalendarPosition
import dev.novux.dynasty.cbb.core.news.NewsHeadline
import dev.novux.dynasty.cbb.core.news.NewsHubPolicy
import dev.novux.dynasty.cbb.core.offseason.weektwo.NbaDraftOutcome
import dev.novux.dynasty.cbb.core.offseason.weektwo.NbaDraftResultLine
import dev.novux.dynasty.cbb.core.portal.PortalCycleState
import dev.novux.dynasty.cbb.core.portal.PortalTop40
import dev.novux.dynasty.cbb.core.signingday.NationalRankRow
import dev.novux.dynasty.cbb.core.signingday.NationalRecruitingRankings
import dev.novux.dynasty.cbb.data.news.NewsFeedRepository
import dev.novux.dynasty.cbb.data.offseason.OffseasonWeekTwoRepository
import dev.novux.dynasty.cbb.data.portal.PortalCycleRepository
import dev.novux.dynasty.cbb.data.preseason.IncomingClassSeedRepository

enum class NewsHubTab { Stories, Portal, Recruits, Draft }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsHubScreen(
    dynastyId: String,
    seasonIndex: Int,
    position: CalendarPosition,
    newsFeedRepository: NewsFeedRepository,
    incomingClassSeedRepository: IncomingClassSeedRepository,
    portalCycleRepository: PortalCycleRepository,
    offseasonWeekTwoRepository: OffseasonWeekTwoRepository,
    onBack: () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(NewsHubTab.Stories.ordinal) }
    var stories by remember { mutableStateOf<List<NewsHeadline>>(emptyList()) }
    var recruits by remember { mutableStateOf<List<NationalRankRow>>(emptyList()) }
    var portalState by remember { mutableStateOf<PortalCycleState?>(null) }
    var draftLines by remember { mutableStateOf<List<NbaDraftResultLine>>(emptyList()) }
    val portalVisible = NewsHubPolicy.portalTabVisible(
        position.phase,
        position.weekIndex.takeIf { position.phase == CalendarPhase.Offseason },
    )

    LaunchedEffect(dynastyId, seasonIndex, position) {
        stories = newsFeedRepository.storiesFeed(dynastyId, seasonIndex)
        recruits = incomingClassSeedRepository.loadHeadline(dynastyId, seasonIndex)?.top60.orEmpty()
        if (portalVisible) {
            portalState = portalCycleRepository.loadOrSimulate(dynastyId, seasonIndex)
        }
        draftLines = offseasonWeekTwoRepository.loadPublishedResults(dynastyId, seasonIndex)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text(stringResource(R.string.news_hub_title)) })
        OutlinedButton(onClick = onBack, modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(stringResource(R.string.dynasty_shell_back))
        }
        ScrollableTabRow(selectedTabIndex = selectedTab) {
            NewsHubTab.entries.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(tabLabel(tab)) },
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            when (NewsHubTab.entries[selectedTab]) {
                NewsHubTab.Stories -> StoriesTab(stories)
                NewsHubTab.Portal -> PortalTab(portalVisible, portalState)
                NewsHubTab.Recruits -> RecruitsTab(recruits)
                NewsHubTab.Draft -> DraftTab(draftLines)
            }
        }
    }
}

@Composable
private fun tabLabel(tab: NewsHubTab): String = when (tab) {
    NewsHubTab.Stories -> stringResource(R.string.news_hub_tab_stories)
    NewsHubTab.Portal -> stringResource(R.string.news_hub_tab_portal)
    NewsHubTab.Recruits -> stringResource(R.string.news_hub_tab_recruits)
    NewsHubTab.Draft -> stringResource(R.string.news_hub_tab_draft)
}

@Composable
private fun StoriesTab(stories: List<NewsHeadline>) {
    if (stories.isEmpty()) {
        Text(stringResource(R.string.news_hub_stories_empty), color = MaterialTheme.colorScheme.onSurfaceVariant)
        return
    }
    stories.forEach { story ->
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(story.title, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun PortalTab(visible: Boolean, state: PortalCycleState?) {
    if (!visible) {
        Text(stringResource(R.string.news_hub_portal_hidden), color = MaterialTheme.colorScheme.onSurfaceVariant)
        return
    }
    val portal = state ?: return
    if (!portal.top40Locked) {
        Text(stringResource(R.string.news_hub_portal_pre_lock), color = MaterialTheme.colorScheme.onSurfaceVariant)
        return
    }
    Text(
        stringResource(R.string.news_hub_portal_top40_heading),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )
    PortalTop40.rankedList(portal.entrants).forEach { entrant ->
        Text(
            stringResource(
                R.string.news_hub_portal_row,
                entrant.nationalTop40Rank ?: 0,
                entrant.entrantId,
                entrant.priorProgramId,
                entrant.stars ?: 0,
            ),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun RecruitsTab(rows: List<NationalRankRow>) {
    Text(
        stringResource(R.string.news_hub_recruits_heading),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )
    Text(
        stringResource(R.string.news_hub_recruits_scope),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
    rows.forEach { row ->
        val p = row.prospect
        Text(
            stringResource(
                R.string.incoming_seed_top60_line,
                row.rank,
                p.displayName,
                p.stars,
                p.position.name,
                p.homeState,
            ),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun DraftTab(lines: List<NbaDraftResultLine>) {
    if (lines.isEmpty()) {
        Text(stringResource(R.string.news_hub_draft_empty), color = MaterialTheme.colorScheme.onSurfaceVariant)
        return
    }
    lines.forEach { line ->
        val outcome = when (line.outcome) {
            NbaDraftOutcome.Drafted -> stringResource(
                R.string.draft_results_outcome_drafted,
                line.pickSummary.orEmpty(),
            )
            NbaDraftOutcome.UndraftedGraduate -> stringResource(R.string.draft_results_outcome_undrafted)
            NbaDraftOutcome.ReturnedToRoster -> stringResource(R.string.draft_results_outcome_returned)
        }
        Text(
            stringResource(R.string.news_hub_draft_row, line.displayLabel, line.mockTier, outcome),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
