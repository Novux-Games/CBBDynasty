package dev.novux.dynasty.cbb.ui.seasonreview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.calendar.CalendarWeekLabel
import dev.novux.dynasty.cbb.core.calendar.OffseasonEntryKind
import dev.novux.dynasty.cbb.core.calendar.OffseasonEntryRouter
import dev.novux.dynasty.cbb.core.calendar.SeasonReviewGate
import dev.novux.dynasty.cbb.core.save.DynastyLoadResult
import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import dev.novux.dynasty.cbb.data.save.ActiveDynastySession
import dev.novux.dynasty.cbb.data.save.SaveSlotRepository
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellChrome
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellMenuAction
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellTab
import dev.novux.dynasty.cbb.data.calendar.UserCalendarRepository
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellCalendarTab
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellNewsTabPlaceholder
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch

/** End of Season gate — [planning/season-review.md]. */
@Composable
fun SeasonReviewScreen(
    loaded: LoadedDynasty,
    saveSlotRepository: SaveSlotRepository,
    onReturnedHome: () -> Unit,
    onEnteredOffseason: (LoadedDynasty) -> Unit = { onReturnedHome() },
    userCalendarRepository: UserCalendarRepository,
) {
    val scope = rememberCoroutineScope()
    val weekHeader = CalendarWeekLabel.display(loaded.weekCheckpoint.activePosition)
    var selectedTab by remember { mutableStateOf(DynastyShellTab.Dashboard) }
    val schoolName = loaded.summary.schoolDisplayName.orEmpty()
    val menu = listOf(DynastyShellMenuAction(R.string.shell_menu_exit_home, onReturnedHome))

    DynastyShellChrome(
        schoolDisplayName = schoolName,
        phaseWeekLabel = weekHeader,
        selectedTab = selectedTab,
        onTabSelected = { selectedTab = it },
        menuActions = menu,
    ) { padding ->
        when (selectedTab) {
            DynastyShellTab.Calendar -> DynastyShellCalendarTab(
                dynastyId = loaded.dynastyId,
                seasonIndex = loaded.metadata.seasonIndex,
                activePosition = loaded.weekCheckpoint.activePosition,
                calendarRepository = userCalendarRepository,
                contentPadding = padding,
            )
            DynastyShellTab.News -> DynastyShellNewsTabPlaceholder(padding)
            DynastyShellTab.Roster,
            DynastyShellTab.Dashboard,
            -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 28.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
        Text(
            text = stringResource(R.string.season_review_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(R.string.season_review_subtitle),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = loaded.summary.schoolDisplayName.orEmpty(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = stringResource(R.string.season_review_placeholder),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f),
        )

        val previewEntry = OffseasonEntryRouter.entryAfterSeasonReview(loaded.metadata.seasonIndex + 1)
        Text(
            text = when (previewEntry) {
                OffseasonEntryKind.WeekOnePlayersLeaving ->
                    stringResource(R.string.season_review_offseason_week1)
                OffseasonEntryKind.CoachingCarouselFirst ->
                    stringResource(R.string.season_review_carousel_next)
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Button(
            onClick = {
                scope.launch {
                    if (!SeasonReviewGate.canEnterOffseason(loaded.weekCheckpoint)) return@launch
                    when (
                        val result = saveSlotRepository.completeSeasonReviewEnterOffseason(loaded.slotIndex)
                    ) {
                        is DynastyLoadResult.Success -> {
                            ActiveDynastySession.set(result.loaded)
                            onEnteredOffseason(result.loaded)
                        }
                        else -> onReturnedHome()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = SeasonReviewGate.canEnterOffseason(loaded.weekCheckpoint),
        ) {
            Text(stringResource(R.string.season_review_enter_offseason))
        }

            }
        }
    }
}
