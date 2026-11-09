package dev.novux.dynasty.cbb.ui.newgame

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.core.coach.CoachArchetype

@Composable
fun ArchetypeStepContent(
    selected: CoachArchetype,
    onSelected: (CoachArchetype) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        CoachArchetype.entries.forEach { archetype ->
            val rowSelected = archetype == selected
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = rowSelected,
                        onClick = { onSelected(archetype) },
                        role = Role.RadioButton,
                    )
                    .padding(vertical = 8.dp),
            ) {
                androidx.compose.foundation.layout.Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(selected = rowSelected, onClick = null)
                    Column(modifier = Modifier.padding(start = 8.dp)) {
                        Text(archetype.label, style = MaterialTheme.typography.titleMedium)
                        Text(
                            archetype.blurb,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}
