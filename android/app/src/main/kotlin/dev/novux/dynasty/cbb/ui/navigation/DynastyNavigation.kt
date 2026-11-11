package dev.novux.dynasty.cbb.ui.navigation

import androidx.navigation.NavHostController
import dev.novux.dynasty.cbb.core.navigation.DynastyDashboardBackTargets

/** Pops to the active in-career dashboard — never the main menu ([MEGA-QA-046]). */
fun NavHostController.popBackToInShellDashboard(): Boolean {
    for (route in DynastyDashboardBackTargets.ORDERED) {
        if (popBackStack(route, inclusive = false)) {
            return true
        }
    }
    return popBackStack()
}

fun NavHostController.navigateToRecruitingHub() {
    navigate(AppRoute.Recruiting.route) {
        launchSingleTop = true
    }
}
