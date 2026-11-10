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
import dev.novux.dynasty.cbb.core.march.MarchMayhemHubSnapshot
import dev.novux.dynasty.cbb.data.march.MarchMayhemHubRepository

@Composable
fun MarchMayhemHubRouteScreen(
    dynastyId: String,
    seasonIndex: Int,
    repository: MarchMayhemHubRepository,
    onBack: () -> Unit,
) {
    var snapshot by remember { mutableStateOf<MarchMayhemHubSnapshot?>(null) }
    LaunchedEffect(dynastyId, seasonIndex) {
        snapshot = repository.load(dynastyId, seasonIndex)
    }
    when (val data = snapshot) {
        null -> Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        else -> MarchMayhemHubScreen(snapshot = data, onBack = onBack)
    }
}
