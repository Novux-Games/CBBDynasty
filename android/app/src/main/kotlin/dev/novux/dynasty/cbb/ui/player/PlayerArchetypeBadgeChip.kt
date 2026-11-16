package dev.novux.dynasty.cbb.ui.player

import androidx.compose.material3.AssistChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.novux.dynasty.cbb.core.player.archetype.PlayerArchetype
import dev.novux.dynasty.cbb.core.qa.PlayerArchetypeBadgePath

/** Compact archetype badge ([MEGA-COACH-161], [MEGA-COACH-162]). */
@Composable
fun PlayerArchetypeBadgeChip(
    archetype: PlayerArchetype,
    modifier: Modifier = Modifier,
) {
    @Suppress("unused")
    val qaRoster = PlayerArchetypeBadgePath.MEGA_ROSTER
    @Suppress("unused")
    val qaIdentity = PlayerArchetypeBadgePath.MEGA_IDENTITY
    AssistChip(
        onClick = {},
        modifier = modifier,
        label = {
            Text(
                archetype.displayName,
                style = MaterialTheme.typography.labelMedium,
            )
        },
    )
}