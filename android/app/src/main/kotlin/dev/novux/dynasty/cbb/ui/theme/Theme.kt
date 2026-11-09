package dev.novux.dynasty.cbb.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.novux.dynasty.cbb.core.GameConstants

private val BrandBackground = Color(GameConstants.BRAND_BACKGROUND_HEX)
private val BrandOnBackground = Color(0xFFE8EAED)
private val BrandOnBackgroundMuted = Color(0xFFA8AEB8)
private val BrandAccent = Color(0xFF6B9FFF)

private val DynastyColorScheme = darkColorScheme(
    primary = BrandAccent,
    onPrimary = BrandBackground,
    background = BrandBackground,
    onBackground = BrandOnBackground,
    surface = BrandBackground,
    onSurface = BrandOnBackground,
    onSurfaceVariant = BrandOnBackgroundMuted,
    outline = BrandOnBackgroundMuted,
)

@Composable
fun DynastyTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DynastyColorScheme,
        content = content,
    )
}
