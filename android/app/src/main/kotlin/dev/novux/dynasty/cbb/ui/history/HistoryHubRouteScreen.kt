package dev.novux.dynasty.cbb.ui.history

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
import dev.novux.dynasty.cbb.core.history.HistoryHubSnapshot
import dev.novux.dynasty.cbb.data.history.HistoryRepository

@Composable
fun HistoryHubRouteScreen(
    dynastyId: String,
    seasonIndex: Int,
    historyRepository: HistoryRepository,
    onBack: () -> Unit,
) {
    var snapshot by remember { mutableStateOf<HistoryHubSnapshot?>(null) }
    LaunchedEffect(dynastyId, seasonIndex) {
        snapshot = historyRepository.loadHubSnapshot(dynastyId, seasonIndex)
    }
    when (val data = snapshot) {
        null -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        else -> HistoryHubScreen(snapshot = data, onBack = onBack)
    }
}
