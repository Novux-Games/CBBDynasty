package dev.novux.dynasty.cbb.ui.newgame

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.data.save.SaveSlotRepository

/** Multi-step **New** flow — BUILD 4.1; step bodies ship in 4.2–4.7. */
@Composable
fun NewGameWizardScreen(
    saveSlotRepository: SaveSlotRepository,
    onExitWizard: () -> Unit,
    onNoEmptySlot: () -> Unit,
) {
    val navController = rememberNavController()
    var slotGatePassed by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        saveSlotRepository.ensureInitialized()
        val hasSlot = saveSlotRepository.firstEmptySlotIndex() != null
        if (!hasSlot) {
            onNoEmptySlot()
            onExitWizard()
        } else {
            slotGatePassed = true
        }
    }

    if (!slotGatePassed) {
        return
    }

    NavHost(
        navController = navController,
        startDestination = NewGameStep.routeFor(NewGameStep.Coach),
        modifier = Modifier.fillMaxSize(),
    ) {
        NewGameStep.entries.forEach { step ->
            composable(NewGameStep.routeFor(step)) {
                val isLast = step == NewGameStep.SignContract
                NewGameStepScaffold(
                    step = step,
                    onBack = {
                        when (val previous = step.previous) {
                            null -> onExitWizard()
                            else -> navController.popBackStack()
                        }
                    },
                    onNext = if (isLast) {
                        null
                    } else {
                        {
                            step.next?.let { next ->
                                navController.navigate(NewGameStep.routeFor(next))
                            }
                        }
                    },
                    nextLabel = stringResource(
                        if (isLast) R.string.new_step_sign_title else R.string.new_wizard_next,
                    ),
                ) {
                    NewGameStepPlaceholderBody(step = step)
                    if (isLast) {
                        Text(
                            text = stringResource(R.string.new_step_sign_stub),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NewGameStepPlaceholderBody(step: NewGameStep) {
    Text(
        text = stringResource(R.string.new_step_placeholder),
        style = MaterialTheme.typography.bodyLarge,
    )
}
