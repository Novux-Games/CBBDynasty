package dev.novux.dynasty.cbb.ui.navigation

/**
 * Pre-dynasty shell — [planning/app-shell.md], [planning/game-start.md].
 */
sealed class AppRoute(val route: String) {
    data object Studio : AppRoute("studio")

    data object Splash : AppRoute("splash")

    data object Home : AppRoute("home")

    data object Settings : AppRoute("settings")

    data object Credits : AppRoute("credits")

    data object Load : AppRoute("load")

    data object NewGame : AppRoute("new_game")

    data object SeasonReview : AppRoute("season_review")

    data object Offseason : AppRoute("offseason")

    data object CoachingCarousel : AppRoute("coaching_carousel")

    data object Recruiting : AppRoute("recruiting")
}
