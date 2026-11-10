package dev.novux.dynasty.cbb.ui.hubs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.novux.dynasty.cbb.core.conference.ConferenceTournamentHubSnapshot
import dev.novux.dynasty.cbb.data.conference.ConferenceTournamentHubRepository

@Composable
fun ConferenceTournamentHubRouteScreen(
    dynastyId: String,
    seasonIndex: Int,
    repository: ConferenceTournamentHubRepository,
    onBack: () -> Unit,
) {
    var snapshot by remember { mutableStateOf<ConferenceTournamentHubSnapshot?>(null) }
    LaunchedEffect(dynastyId, seasonIndex) {
        snapshot = repository.load(dynastyId, seasonIndex)
    }
    when (val data = snapshot) {
        null -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        else -> ConferenceTournamentHubScreen(snapshot = data, onBack = onBack)
    }
}
