package dev.novux.dynasty.cbb.ui.shell

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.core.GameConstants
import kotlinx.coroutines.delay

/** Novux Games publisher beat — [planning/app-shell.md#studio-screen-novux-games]. */
@Composable
fun StudioScreen(
    onFinished: () -> Unit,
    autoAdvanceMs: Long = 2_000L,
) {
    LaunchedEffect(Unit) {
        delay(autoAdvanceMs)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = onFinished),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = GameConstants.STUDIO_NAME,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Tap to continue",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 16.dp),
            )
        }
    }
}
