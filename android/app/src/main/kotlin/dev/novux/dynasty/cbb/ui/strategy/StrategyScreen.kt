package dev.novux.dynasty.cbb.ui.strategy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.discipline.DisciplineResolver
import dev.novux.dynasty.cbb.core.lineup.DefaultStarterAlgorithm
import dev.novux.dynasty.cbb.core.roster.PlayerPosition
import dev.novux.dynasty.cbb.core.roster.RosterPlayer
import dev.novux.dynasty.cbb.core.strategy.GameplanInput
import dev.novux.dynasty.cbb.core.strategy.GameplanUsageNormalizer
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository
import dev.novux.dynasty.cbb.ui.player.PlayerInjuryRosterRow

@Composable
fun StrategyScreen(
    dynastyId: String,
    rosterRepository: DynastyRosterRepository,
    teamChemistry: Int? = null,
    fatigueTuning: dev.novux.dynasty.cbb.core.balance.BalanceFatigue =
        dev.novux.dynasty.cbb.core.balance.BalanceFatigue(),
    onBack: () -> Unit,
) {
    var players by remember { mutableStateOf<List<RosterPlayer>>(emptyList()) }
    LaunchedEffect(dynastyId) {
        players = rosterRepository.listPlayers(dynastyId)
    }
    val starters = DefaultStarterAlgorithm.pickStarters(
        players,
        starterBlockStamina = fatigueTuning.starterBlockThreshold,
    )
    val bench = players
        .filter { it.playerId !in starters.values }
        .sortedBy { it.depthOrder }
    var gameplan by remember { mutableStateOf(GameplanInput()) }
    var halftimeGameplan by remember { mutableStateOf<GameplanInput?>(null) }
    LaunchedEffect(starters.values) {
        if (gameplan.usageWeights.isEmpty() && starters.isNotEmpty()) {
            gameplan = gameplan.copy(
                usageWeights = GameplanUsageNormalizer.defaultForStarters(starters.values),
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.strategy_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(R.string.strategy_injured_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        teamChemistry?.let { chem ->
            Text(
                text = stringResource(R.string.strategy_chemistry, chem),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Text(
            text = stringResource(R.string.strategy_starters),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        PlayerPosition.STARTER_ORDER.forEach { slot ->
            val player = starters[slot]?.let { id -> players.firstOrNull { it.playerId == id } }
            StrategyLineupCard(
                title = slot.label,
                player = player,
            )
        }
        Text(
            text = stringResource(R.string.strategy_bench),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        bench.forEach { player ->
            StrategyLineupCard(
                title = player.position.label,
                player = player,
            )
        }
        StrategyGameplanPanel(
            starters = starters,
            players = players,
            gameplan = gameplan,
            onGameplanChange = { gameplan = it },
            halftimeGameplan = halftimeGameplan,
            onHalftimeChange = { halftimeGameplan = it },
        )
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.settings_back))
        }
    }
}

@Composable
private fun StrategyLineupCard(
    title: String,
    player: RosterPlayer?,
) {
    val injured = player != null && DisciplineResolver.isIneligibleForLineup(player)
    val exhausted = player != null && player.stamina < 20
    val dimmed = injured || exhausted
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (dimmed) 0.45f else 1f),
        colors = CardDefaults.cardColors(
            containerColor = if (dimmed) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surface
            },
        ),
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = title, style = MaterialTheme.typography.labelLarge)
            if (player == null) {
                Text(
                    text = stringResource(R.string.strategy_slot_empty),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else if (injured) {
                PlayerInjuryRosterRow(player)
            } else {
                Text(text = player.displayLabel(), style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = stringResource(R.string.strategy_stamina, player.stamina),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
