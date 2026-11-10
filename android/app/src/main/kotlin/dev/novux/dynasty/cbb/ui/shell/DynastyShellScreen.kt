package dev.novux.dynasty.cbb.ui.shell

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import dev.novux.dynasty.cbb.core.calendar.CalendarWeekLabel
import dev.novux.dynasty.cbb.core.preseason.FirstPlayableGame
import dev.novux.dynasty.cbb.core.preseason.PreseasonSeasonStartPolicy
import dev.novux.dynasty.cbb.core.preseason.SeasonStartFocus
import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import dev.novux.dynasty.cbb.data.preseason.PreseasonScheduleRepository

@Composable
fun DynastyShellScreen(
    loaded: LoadedDynasty,
    scheduleRepository: PreseasonScheduleRepository,
    onExitHome: () -> Unit,
) {
    var focus by remember { mutableStateOf<SeasonStartFocus?>(null) }

    LaunchedEffect(loaded.dynastyId, loaded.metadata.seasonIndex) {
        val exhibitionCount = scheduleRepository.loadExhibitionCount(
            loaded.dynastyId,
            loaded.metadata.seasonIndex,
        )
        val plan = scheduleRepository.loadPlan(loaded.dynastyId, loaded.metadata.seasonIndex)
        focus = PreseasonSeasonStartPolicy.resolve(
            loaded.metadata.seasonIndex,
            exhibitionCount,
            plan,
        )
    }

    val position = loaded.weekCheckpoint.activePosition

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 36.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.dynasty_shell_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = loaded.summary.schoolDisplayName.orEmpty(),
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = CalendarWeekLabel.display(position),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = stringResource(R.string.dynasty_shell_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        focus?.firstGame?.let { game ->
            NextGameCard(game)
        }
        OutlinedButton(
            onClick = onExitHome,
            modifier = Modifier.align(androidx.compose.ui.Alignment.CenterHorizontally),
        ) {
            Text(stringResource(R.string.settings_back))
        }
    }
}

@Composable
private fun NextGameCard(game: FirstPlayableGame) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = stringResource(R.string.dynasty_shell_next_game),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = stringResource(
                    R.string.dynasty_shell_matchup,
                    game.opponentDisplayName,
                    if (game.homeGame) stringResource(R.string.dynasty_shell_home) else stringResource(R.string.dynasty_shell_away),
                ),
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = stringResource(R.string.dynasty_shell_game_type, game.kind.name),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
