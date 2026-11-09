package dev.novux.dynasty.cbb.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.novux.dynasty.cbb.core.settings.AppSettings
import dev.novux.dynasty.cbb.data.settings.AppSettingsRepository
import dev.novux.dynasty.cbb.requireDynastyApplication
import dev.novux.dynasty.cbb.ui.navigation.AppRoute
import dev.novux.dynasty.cbb.ui.shell.HomeMenuScreen
import dev.novux.dynasty.cbb.ui.shell.SettingsScreen
import dev.novux.dynasty.cbb.ui.shell.SplashScreen
import dev.novux.dynasty.cbb.ui.shell.StudioScreen
import dev.novux.dynasty.cbb.ui.theme.DynastyTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun DynastyApp(
    settingsRepository: AppSettingsRepository =
        LocalContext.current.requireDynastyApplication().appSettingsRepository,
) {
    DynastyTheme {
        var launchSettings by remember { mutableStateOf<AppSettings?>(null) }

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

            NavHost(
                navController = navController,
                startDestination = startRoute,
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
                        hasSave = false,
                        onContinue = { },
                        onNew = { },
                        onLoad = { },
                        onSettings = { navController.navigate(AppRoute.Settings.route) },
                        onCredits = { },
                    )
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
