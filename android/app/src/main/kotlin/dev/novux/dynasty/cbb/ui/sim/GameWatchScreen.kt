package dev.novux.dynasty.cbb.ui.sim

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import dev.novux.dynasty.cbb.core.sim.GameLineupPrep
import dev.novux.dynasty.cbb.core.sim.GameLineupPrepValidator
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import dev.novux.dynasty.cbb.core.sim.GameSimInput
import dev.novux.dynasty.cbb.core.sim.GameSimInjuryRollInput
import dev.novux.dynasty.cbb.core.sim.GameSimResult
import dev.novux.dynasty.cbb.core.sim.GameSimRosterInput
import dev.novux.dynasty.cbb.core.sim.GameSimTeamInput
import dev.novux.dynasty.cbb.core.sim.GameWatchPlayback
import dev.novux.dynasty.cbb.core.sim.GameWatchSpeedPresets
import dev.novux.dynasty.cbb.core.sim.HalftimeCoachAdjustments
import dev.novux.dynasty.cbb.core.sim.HalftimeRosterAdjust
import dev.novux.dynasty.cbb.core.sim.tuning.SimTuning
import dev.novux.dynasty.cbb.core.reports.PostgameReportGenerator
import dev.novux.dynasty.cbb.core.reports.ReportArticle
import dev.novux.dynasty.cbb.core.strategy.DefensiveSchemePreset
import dev.novux.dynasty.cbb.core.strategy.GameplanInput
import dev.novux.dynasty.cbb.core.strategy.OffensiveSchemePreset
import dev.novux.dynasty.cbb.core.balance.BalanceMedicalRedshirtRules
import dev.novux.dynasty.cbb.core.injuries.GameSimInjuryEvent
import dev.novux.dynasty.cbb.ui.postgame.PostgameInjuryFlow
import dev.novux.dynasty.cbb.ui.reports.ReportArticleView
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@Composable
fun GameWatchScreen(
    tuning: SimTuning,
    homeName: String,
    awayName: String,
    homeGameplan: GameplanInput,
    homeOffensiveScheme: OffensiveSchemePreset = OffensiveSchemePreset.Balanced,
    homeDefensiveScheme: DefensiveSchemePreset = DefensiveSchemePreset.ManToMan,
    homeRosterPlayers: List<RosterPlayer> = emptyList(),
    homeStarters: Map<PlayerPosition, String> = emptyMap(),
    onHomeHalftimeAdjustments: (HalftimeCoachAdjustments) -> Unit = {},
    homeSimRoster: GameSimRosterInput? = null,
    injuryRoll: GameSimInjuryRollInput? = null,
    postgameInjuryEvents: List<GameSimInjuryEvent> = emptyList(),
    postgameInjuryRoster: List<RosterPlayer> = emptyList(),
    rsWeekIndex: Int = 0,
    medicalRules: BalanceMedicalRedshirtRules = BalanceMedicalRedshirtRules(),
    onDismissPostgameInjuries: () -> Unit = {},
    onApplyMedicalRedshirt: (String) -> Unit = {},
    onSimComplete: (GameSimResult) -> Unit = {},
    onBack: () -> Unit,
    seed: Long = 17L,
) {
    val context = LocalContext.current
    val lines = remember { mutableStateListOf<String>() }
    var scoreLine by remember { mutableStateOf("") }
    var halftimeOpen by remember { mutableStateOf(false) }
    var halftimeScore by remember { mutableStateOf("") }
    var halftimeAdjustments by remember {
        mutableStateOf(
            HalftimeCoachAdjustments(
                gameplan = homeGameplan,
                offensiveScheme = homeOffensiveScheme,
                defensiveScheme = homeDefensiveScheme,
                starters = homeStarters,
            ),
        )
    }
    var postgameArticle by remember { mutableStateOf<ReportArticle?>(null) }
    var finalResult by remember { mutableStateOf<GameSimResult?>(null) }
    val listState = rememberLazyListState()
    val resumeGate = remember { mutableStateOf<((HalftimeCoachAdjustments) -> Unit)?>(null) }
    var selectedSpeedMultiplier by remember {
        mutableStateOf(GameWatchPlayback.DEFAULT_WATCH_SPEED_MULTIPLIER)
    }
    var simToEnd by remember { mutableStateOf(false) }
    var watchInProgress by remember { mutableStateOf(true) }

    val baseInput = remember(
        tuning,
        seed,
        homeGameplan,
        homeName,
        awayName,
        homeSimRoster,
        homeOffensiveScheme,
        homeDefensiveScheme,
    ) {
        GameSimInput(
            home = GameSimTeamInput(
                homeName,
                78,
                74,
                offensiveScheme = homeOffensiveScheme,
                defensiveScheme = homeDefensiveScheme,
            ),
            away = GameSimTeamInput(awayName, 76, 75),
            tuning = tuning,
            seed = seed,
            homeGameplan = homeGameplan,
            homeRoster = homeSimRoster,
        )
    }

    LaunchedEffect(baseInput) {
        if (homeRosterPlayers.isNotEmpty()) {
            val validation = GameLineupPrepValidator.validate(homeRosterPlayers, homeStarters)
            if (!validation.canStartSim) {
                watchInProgress = false
                scoreLine = context.getString(R.string.strategy_lineup_sim_blocked)
                return@LaunchedEffect
            }
        }
        scoreLine = context.getString(R.string.game_watch_score_start)
        val result = GameWatchPlayback.watch(
            input = baseInput,
            injuryRoll = injuryRoll,
            onLineDelayMs = {
                if (simToEnd) 0L else GameWatchPlayback.lineDelayMs(selectedSpeedMultiplier)
            },
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
                val resolvedStarters = if (homeStarters.isNotEmpty()) {
                    homeStarters
                } else {
                    GameLineupPrep.resolveStarters(homeRosterPlayers, null)
                }
                halftimeAdjustments = HalftimeCoachAdjustments(
                    gameplan = homeGameplan,
                    offensiveScheme = homeOffensiveScheme,
                    defensiveScheme = homeDefensiveScheme,
                    starters = resolvedStarters,
                )
                halftimeOpen = true
                scoreLine = halftimeScore
                suspendCancellableCoroutine { cont ->
                    resumeGate.value = { adj ->
                        val teamId = homeSimRoster?.teamId ?: homeName
                        val newRoster = if (homeRosterPlayers.isNotEmpty()) {
                            val built = GameLineupPrep.build(teamId, homeRosterPlayers, adj.starters)
                            HalftimeRosterAdjust.applyRotationEmphasis(built, adj.rotationEmphasis)
                        } else {
                            homeSimRoster
                        }
                        onHomeHalftimeAdjustments(adj)
                        halftimeOpen = false
                        cont.resume(
                            baseInput.copy(
                                home = baseInput.home.copy(
                                    offensiveScheme = adj.offensiveScheme,
                                    defensiveScheme = adj.defensiveScheme,
                                ),
                                homeHalftimeGameplan = adj.gameplan,
                                homeRoster = newRoster,
                            ),
                        )
                        resumeGate.value = null
                    }
                }
            },
        )
        scoreLine = if (result.overtimePeriods > 0) {
            context.getString(
                R.string.game_watch_score_final_ot,
                result.homeScore,
                result.awayScore,
                result.overtimePeriods,
            )
        } else {
            context.getString(
                R.string.game_watch_score_final,
                result.homeScore,
                result.awayScore,
            )
        }
        finalResult = result
        postgameArticle = PostgameReportGenerator.generate(
            userTeamName = homeName,
            opponentName = awayName,
            userScore = result.homeScore,
            opponentScore = result.awayScore,
        )
        result.homeInjuries.forEach { event ->
            lines.add(
                context.getString(
                    R.string.game_watch_injury_log,
                    event.label,
                    event.weeksRemaining,
                ),
            )
        }
        watchInProgress = false
        onSimComplete(result)
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
        GameWatchSpeedControl(
            selectedSpeedMultiplier = selectedSpeedMultiplier,
            simToEnd = simToEnd,
            enabled = watchInProgress,
            onSelectMultiplier = { mult ->
                simToEnd = false
                selectedSpeedMultiplier = mult
            },
            onSimToEnd = { simToEnd = true },
        )
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
        finalResult?.let { result ->
            PostgameBoxScorePanel(
                result = result,
                homeName = homeName,
                awayName = awayName,
                homeRosterPlayers = homeRosterPlayers,
            )
        }
        postgameArticle?.let { article ->
            Text(
                text = stringResource(R.string.game_report_postgame_heading),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            ReportArticleView(article = article)
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.settings_back))
        }
    }

    PostgameInjuryFlow(
        events = postgameInjuryEvents,
        players = postgameInjuryRoster.ifEmpty { homeRosterPlayers },
        rsWeekIndex = rsWeekIndex,
        medicalRules = medicalRules,
        onDismiss = onDismissPostgameInjuries,
        onApplyMedicalRedshirt = onApplyMedicalRedshirt,
    )

    if (halftimeOpen) {
        HalftimeCoachSheet(
            scoreLine = halftimeScore,
            players = homeRosterPlayers,
            state = halftimeAdjustments,
            onStateChange = { halftimeAdjustments = it },
            onContinue = { resumeGate.value?.invoke(halftimeAdjustments) },
        )
    }
}

@Composable
private fun GameWatchSpeedControl(
    selectedSpeedMultiplier: Double,
    simToEnd: Boolean,
    enabled: Boolean,
    onSelectMultiplier: (Double) -> Unit,
    onSimToEnd: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = stringResource(R.string.game_watch_speed_label),
            style = MaterialTheme.typography.labelLarge,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            GameWatchSpeedPresets.SPEED_MULTIPLIERS.forEach { mult ->
                val selected = !simToEnd && kotlin.math.abs(selectedSpeedMultiplier - mult) < 0.001
                FilterChip(
                    selected = selected,
                    enabled = enabled,
                    onClick = { onSelectMultiplier(mult) },
                    label = {
                        Text(
                            when (mult) {
                                0.5 -> stringResource(R.string.game_watch_speed_half)
                                1.0 -> stringResource(R.string.game_watch_speed_one)
                                2.0 -> stringResource(R.string.game_watch_speed_two)
                                4.0 -> stringResource(R.string.game_watch_speed_four)
                                else -> "${mult}×"
                            },
                        )
                    },
                )
            }
            FilterChip(
                selected = simToEnd,
                enabled = enabled,
                onClick = onSimToEnd,
                label = { Text(stringResource(R.string.game_watch_speed_sim_to_end)) },
            )
        }
    }
}

