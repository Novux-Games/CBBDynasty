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
import dev.novux.dynasty.cbb.core.coach.CoachCreateValidator
import dev.novux.dynasty.cbb.core.pack.TeamDisplayOverlay
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.data.save.SaveSlotRepository

/** Multi-step **New** flow — [planning/game-start.md#new-game-wizard-overview]. */
@Composable
fun NewGameWizardScreen(
    saveSlotRepository: SaveSlotRepository,
    packRepository: PackRepository,
    onExitWizard: () -> Unit,
    onNoEmptySlot: () -> Unit,
) {
    val navController = rememberNavController()
    var slotGatePassed by remember { mutableStateOf(false) }
    var draft by remember { mutableStateOf(NewGameDraft()) }
    var coachShowErrors by remember { mutableStateOf(false) }
    var schools by remember { mutableStateOf<List<TeamDisplayOverlay>>(emptyList()) }

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

    LaunchedEffect(slotGatePassed) {
        if (!slotGatePassed) return@LaunchedEffect
        val session = packRepository.ensureEmbeddedLoaded()
        schools = session.index.allTeamDisplays()
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
                val isCoach = step == NewGameStep.Coach
                NewGameStepScaffold(
                    step = step,
                    onBack = {
                        when (step.previous) {
                            null -> onExitWizard()
                            else -> navController.popBackStack()
                        }
                    },
                    onNext = if (isLast) {
                        null
                    } else {
                        {
                            if (isCoach && CoachCreateValidator.validate(draft.coach) != null) {
                                coachShowErrors = true
                            } else {
                                step.next?.let { next ->
                                    navController.navigate(NewGameStep.routeFor(next))
                                }
                            }
                        }
                    },
                    nextLabel = stringResource(
                        if (isLast) R.string.new_step_sign_title else R.string.new_wizard_next,
                    ),
                ) {
                    when (step) {
                        NewGameStep.Coach -> CoachInfoStepContent(
                            profile = draft.coach,
                            schools = schools,
                            onProfileChange = { draft = draft.copy(coach = it) },
                            showValidationErrors = coachShowErrors,
                        )
                        NewGameStep.Archetype -> ArchetypeStepContent(
                            selected = draft.archetype,
                            onSelected = { draft = draft.copy(archetype = it) },
                        )
                        NewGameStep.Schemes -> SchemesStepContent(
                            offensive = draft.offensiveScheme,
                            defensive = draft.defensiveScheme,
                            onOffensiveChange = { draft = draft.copy(offensiveScheme = it) },
                            onDefensiveChange = { draft = draft.copy(defensiveScheme = it) },
                        )
                        NewGameStep.SignContract -> {
                            Text(
                                text = stringResource(R.string.new_step_sign_stub),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        else -> NewGameStepPlaceholderBody(step = step)
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
