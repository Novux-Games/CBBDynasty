package dev.novux.dynasty.cbb.ui.shell

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.GameConstants
import kotlinx.coroutines.delay

/** Novux Games publisher beat — [planning/app-shell.md#studio-screen-novux-games]. */
@Composable
fun StudioScreen(
    onFinished: () -> Unit,
    autoAdvanceMs: Long = 2_000L,
    minDisplayBeforeSkipMs: Long = 500L,
) {
    var skipEnabled by remember { mutableStateOf(false) }
    var fadeIn by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        fadeIn = true
        delay(minDisplayBeforeSkipMs)
        skipEnabled = true
    }

    val logoAlpha by animateFloatAsState(
        targetValue = if (fadeIn) 1f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "studioLogoFade",
    )

    var finished by remember { mutableStateOf(false) }
    val advance: () -> Unit = {
        if (!finished) {
            finished = true
            onFinished()
        }
    }

    LaunchedEffect(skipEnabled) {
        if (!skipEnabled) return@LaunchedEffect
        delay(autoAdvanceMs - minDisplayBeforeSkipMs)
        advance()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = skipEnabled, onClick = advance),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 32.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.novux_studio_lockup),
                contentDescription = GameConstants.STUDIO_NAME,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(0.88f)
                    .sizeIn(maxHeight = 200.dp)
                    .graphicsLayer { alpha = logoAlpha },
            )

            Text(
                text = "Tap to continue",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .alpha(logoAlpha),
            )
        }
    }
}
