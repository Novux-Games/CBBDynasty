package dev.novux.dynasty.cbb.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.settings.AppSettings
import dev.novux.dynasty.cbb.data.save.ActiveDynastySession
import dev.novux.dynasty.cbb.data.save.SaveSlotRepository
import dev.novux.dynasty.cbb.data.settings.AppSettingsRepository
import dev.novux.dynasty.cbb.requireDynastyApplication
import dev.novux.dynasty.cbb.ui.navigation.AppRoute
import dev.novux.dynasty.cbb.ui.shell.CreditsScreen
import dev.novux.dynasty.cbb.ui.shell.HomeMenuScreen
import dev.novux.dynasty.cbb.ui.shell.LoadGameScreen
import dev.novux.dynasty.cbb.ui.shell.SettingsScreen
import dev.novux.dynasty.cbb.ui.shell.SplashScreen
import dev.novux.dynasty.cbb.ui.shell.StudioScreen
import dev.novux.dynasty.cbb.ui.theme.DynastyTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
fun DynastyApp(
    settingsRepository: AppSettingsRepository =
        LocalContext.current.requireDynastyApplication().appSettingsRepository,
    saveSlotRepository: SaveSlotRepository =
        LocalContext.current.requireDynastyApplication().saveSlotRepository,
) {
    DynastyTheme {
        var launchSettings by remember { mutableStateOf<AppSettings?>(null) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(settingsRepository) {
            launchSettings = settingsRepository.currentSettings()
        }

        if (launchSettings == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            )
        } else {
            val startRoute = if (launchSettings!!.skipStudioIntro) {
                AppRoute.Splash.route
            } else {
                AppRoute.Studio.route
            }
            val navController = rememberNavController()
            val continueSlot by saveSlotRepository.observeContinueSlot()
                .collectAsState(initial = null)
            val context = LocalContext.current
            val snackbarHostState = remember { SnackbarHostState() }

            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
            ) { padding ->
            NavHost(
                navController = navController,
                startDestination = startRoute,
                modifier = Modifier.padding(padding),
            ) {
                composable(AppRoute.Studio.route) {
                    StudioScreen(
                        onFinished = { navController.navigate(AppRoute.Splash.route) },
                    )
                }
                composable(AppRoute.Splash.route) {
                    SplashScreen(
                        onTapToPlay = {
                            navController.navigate(AppRoute.Home.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                        },
                    )
                }
                composable(AppRoute.Home.route) {
                    HomeMenuScreen(
                        continueSlot = continueSlot,
                        onContinue = {
                            val slot = continueSlot ?: return@HomeMenuScreen
                            scope.launch {
                                val loaded = saveSlotRepository.loadSlot(slot.slotIndex)
                                    ?: return@launch
                                ActiveDynastySession.set(loaded)
                            }
                        },
                        onNew = {
                            scope.launch {
                                val loaded = saveSlotRepository.createEmptyDynastyInFirstOpenSlot(
                                    schoolDisplayName = context.getString(R.string.new_bootstrap_school),
                                    coachDisplayName = context.getString(R.string.new_bootstrap_coach),
                                )
                                if (loaded != null) {
                                    ActiveDynastySession.set(loaded)
                                } else {
                                    snackbarHostState.showSnackbar(
                                        context.getString(R.string.new_no_empty_slot),
                                    )
                                }
                            }
                        },
                        onLoad = { navController.navigate(AppRoute.Load.route) },
                        onSettings = { navController.navigate(AppRoute.Settings.route) },
                        onCredits = { navController.navigate(AppRoute.Credits.route) },
                    )
                }
                composable(AppRoute.Load.route) {
                    LoadGameScreen(
                        saveSlotRepository = saveSlotRepository,
                        onBack = { navController.popBackStack() },
                        onDynastyLoaded = { navController.popBackStack() },
                    )
                }
                composable(AppRoute.Credits.route) {
                    CreditsScreen(onBack = { navController.popBackStack() })
                }
                composable(AppRoute.Settings.route) {
                    SettingsScreen(
                        settingsRepository = settingsRepository,
                        onBack = { navController.popBackStack() },
                    )
                }
            }
            }
        }
    }
}
