package dev.novux.dynasty.cbb.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.novux.dynasty.cbb.ui.navigation.AppRoute
import dev.novux.dynasty.cbb.ui.shell.HomeMenuScreen
import dev.novux.dynasty.cbb.ui.shell.SplashScreen
import dev.novux.dynasty.cbb.ui.shell.StudioScreen
import dev.novux.dynasty.cbb.ui.theme.DynastyTheme

@Composable
fun DynastyApp() {
    DynastyTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = AppRoute.Studio.route,
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
                            popUpTo(AppRoute.Studio.route) { inclusive = true }
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
                    onSettings = { },
                    onCredits = { },
                )
            }
        }
    }
}
