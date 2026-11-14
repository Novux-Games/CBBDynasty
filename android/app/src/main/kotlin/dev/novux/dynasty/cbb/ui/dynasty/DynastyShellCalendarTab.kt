package dev.novux.dynasty.cbb.ui.dynasty

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import dev.novux.dynasty.cbb.core.calendar.UserCalendarGameEntry
import dev.novux.dynasty.cbb.core.calendar.UserCalendarRowKind
import dev.novux.dynasty.cbb.core.calendar.UserCalendarWeekRow
import dev.novux.dynasty.cbb.core.schedule.ScheduleGameKind
import dev.novux.dynasty.cbb.data.calendar.UserCalendarRepository

@Composable
fun DynastyShellCalendarTab(
    dynastyId: String,
    seasonIndex: Int,
    activePosition: CalendarPosition,
    calendarRepository: UserCalendarRepository,
    contentPadding: PaddingValues,
) {
    var rows by remember { mutableStateOf<List<UserCalendarWeekRow>>(emptyList()) }
    LaunchedEffect(dynastyId, seasonIndex, activePosition) {
        rows = calendarRepository.loadCalendarRows(dynastyId, seasonIndex, activePosition)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(rows, key = { "${it.phase}:${it.weekIndex}" }) { row ->
            CalendarWeekCard(row = row)
        }
    }
}

@Composable
private fun CalendarWeekCard(row: UserCalendarWeekRow) {
    val colors = if (row.isCurrentWeek) {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    } else {
        CardDefaults.cardColors()
    }
    Card(modifier = Modifier.fillMaxWidth(), colors = colors) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = row.weekLabel,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                if (row.isCurrentWeek) {
                    AssistChip(onClick = {}, label = { Text(stringResource(R.string.shell_calendar_current)) })
                }
            }
            when (row.rowKind) {
                UserCalendarRowKind.Bye -> {
                    AssistChip(onClick = {}, label = { Text(stringResource(R.string.shell_calendar_bye)) })
                }
                UserCalendarRowKind.OffWeek -> {
                    val copy = if (row.phase == CalendarPhase.Exhibition) {
                        stringResource(R.string.shell_calendar_exhibition_empty)
                    } else {
                        stringResource(R.string.shell_calendar_open_week)
                    }
                    Text(
                        text = copy,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                UserCalendarRowKind.Game -> {
                    row.games.forEach { game ->
                        CalendarGameLine(game)
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarGameLine(game: UserCalendarGameEntry) {
    val status = if (game.isPastWeek) {
        stringResource(R.string.shell_calendar_played)
    } else {
        stringResource(R.string.shell_calendar_upcoming)
    }
    val location = if (game.homeGame) {
        stringResource(R.string.dynasty_shell_home)
    } else {
        stringResource(R.string.dynasty_shell_away)
    }
    val date = game.gameDateLabel.ifBlank { "—" }
    Text(
        text = stringResource(
            R.string.shell_calendar_game_line,
            date,
            game.opponentDisplayName,
            location,
            gameKindLabel(game.gameKind),
            status,
        ),
        style = MaterialTheme.typography.bodyMedium,
    )
}

@Composable
private fun gameKindLabel(kind: ScheduleGameKind): String = when (kind) {
    ScheduleGameKind.Conference -> stringResource(R.string.shell_calendar_kind_conf)
    ScheduleGameKind.NonConference -> stringResource(R.string.shell_calendar_kind_nonconf)
    ScheduleGameKind.EarlyEvent -> stringResource(R.string.shell_calendar_kind_event)
    ScheduleGameKind.Exhibition -> stringResource(R.string.shell_calendar_kind_exhibition)
}
