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

    data object Preseason : AppRoute("preseason")

    /** In-season dynasty shell — [planning/game-frame.md]. */
    data object DynastyShell : AppRoute("dynasty_shell")

    data object Strategy : AppRoute("strategy")

    data object GameWatch : AppRoute("game_watch")

    data object GamePreview : AppRoute("game_preview")

    data object Discipline : AppRoute("discipline")

    data object CoachingCarousel : AppRoute("coaching_carousel")

    data object Recruiting : AppRoute("recruiting")

    data object SigningDay : AppRoute("signing_day")

    data object NewsHub : AppRoute("news_hub")

    data object SchoolHub : AppRoute("school_hub")

    data object StatisticsHub : AppRoute("statistics_hub")

    data object RankingsHub : AppRoute("rankings_hub")

    data object HistoryHub : AppRoute("history_hub")

    data object MarchMayhem : AppRoute("march_mayhem")

    data object AwardsHub : AppRoute("awards_hub")
}
