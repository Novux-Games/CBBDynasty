package dev.novux.dynasty.cbb.ui.sim

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.sim.GameSimInput
import dev.novux.dynasty.cbb.core.sim.GameSimTeamInput
import dev.novux.dynasty.cbb.core.sim.GameWatchPlayback
import dev.novux.dynasty.cbb.core.sim.tuning.SimTuning
import dev.novux.dynasty.cbb.core.strategy.GameplanInput
import dev.novux.dynasty.cbb.core.strategy.LateGameFoulStrategy
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@Composable
fun GameWatchScreen(
    tuning: SimTuning,
    homeName: String,
    awayName: String,
    homeGameplan: GameplanInput,
    onHomeHalftimeGameplan: (GameplanInput) -> Unit,
    onBack: () -> Unit,
    seed: Long = 17L,
) {
    val context = LocalContext.current
    val lines = remember { mutableStateListOf<String>() }
    var scoreLine by remember { mutableStateOf("") }
    var halftimeOpen by remember { mutableStateOf(false) }
    var halftimeScore by remember { mutableStateOf("") }
    var halftimePlan by remember { mutableStateOf(homeGameplan) }
    val listState = rememberLazyListState()
    val resumeGate = remember { mutableStateOf<((GameplanInput) -> Unit)?>(null) }

    val baseInput = remember(tuning, seed, homeGameplan, homeName, awayName) {
        GameSimInput(
            home = GameSimTeamInput(homeName, 78, 74),
            away = GameSimTeamInput(awayName, 76, 75),
            tuning = tuning,
            seed = seed,
            homeGameplan = homeGameplan,
        )
    }

    LaunchedEffect(baseInput) {
        scoreLine = context.getString(R.string.game_watch_score_start)
        val result = GameWatchPlayback.watch(
            input = baseInput,
            speedMultiplier = 8.0,
            onLine = { line ->
                lines.add(line)
                listState.animateScrollToItem(lines.lastIndex)
            },
            onHalftime = { partial, _ ->
                halftimeScore = context.getString(
                    R.string.game_watch_score_half,
                    partial.homeScore,
                    partial.awayScore,
                )
                halftimePlan = homeGameplan
                halftimeOpen = true
                scoreLine = halftimeScore
                suspendCancellableCoroutine { cont ->
                    resumeGate.value = { plan ->
                        onHomeHalftimeGameplan(plan)
                        halftimeOpen = false
                        cont.resume(baseInput.copy(homeHalftimeGameplan = plan))
                        resumeGate.value = null
                    }
                }
            },
        )
        scoreLine = context.getString(
            R.string.game_watch_score_final,
            result.homeScore,
            result.awayScore,
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(R.string.game_watch_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(R.string.game_watch_subtitle, homeName, awayName),
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(text = scoreLine, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(lines) { line ->
                Text(
                    text = line,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                )
            }
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.settings_back))
        }
    }

    if (halftimeOpen) {
        HalftimeAdjustDialog(
            scoreLine = halftimeScore,
            gameplan = halftimePlan,
            onGameplanChange = { halftimePlan = it },
            onContinue = { resumeGate.value?.invoke(halftimePlan) },
        )
    }
}

@Composable
private fun HalftimeAdjustDialog(
    scoreLine: String,
    gameplan: GameplanInput,
    onGameplanChange: (GameplanInput) -> Unit,
    onContinue: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(stringResource(R.string.game_watch_halftime_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(scoreLine)
                Text(stringResource(R.string.strategy_deny_primary))
                TextButton(
                    onClick = {
                        onGameplanChange(gameplan.copy(denyPrimaryScorer = !gameplan.denyPrimaryScorer))
                    },
                ) {
                    Text(
                        if (gameplan.denyPrimaryScorer) {
                            stringResource(R.string.game_watch_halftime_deny_on)
                        } else {
                            stringResource(R.string.game_watch_halftime_deny_off)
                        },
                    )
                }
                Text(stringResource(R.string.strategy_late_fouls))
                LateGameFoulStrategy.entries.forEach { strategy ->
                    TextButton(
                        onClick = { onGameplanChange(gameplan.copy(lateGameFoulStrategy = strategy)) },
                    ) {
                        Text(
                            when (strategy) {
                                LateGameFoulStrategy.Standard ->
                                    stringResource(R.string.strategy_late_foul_standard)
                                LateGameFoulStrategy.FoulEarly ->
                                    stringResource(R.string.strategy_late_foul_foul_early)
                                LateGameFoulStrategy.ProtectLead ->
                                    stringResource(R.string.strategy_late_foul_protect)
                            },
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onContinue) {
                Text(stringResource(R.string.game_watch_halftime_resume))
            }
        },
    )
}
