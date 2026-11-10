package dev.novux.dynasty.cbb.ui.navigation

import androidx.navigation.NavHostController
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.SeasonReviewGate
import dev.novux.dynasty.cbb.core.carousel.CoachingCarouselGate
import dev.novux.dynasty.cbb.core.carousel.CoachingCarouselState
import dev.novux.dynasty.cbb.core.save.LoadedDynasty

/** Routes after a dynasty load or sign ([planning/game-start.md], [planning/offseason.md]). */
object DynastyLaunch {
    fun navigateForLoaded(
        navController: NavHostController,
        loaded: LoadedDynasty,
        carousel: CoachingCarouselState,
    ) {
        when {
            SeasonReviewGate.isActive(loaded.weekCheckpoint) -> {
                navController.navigate(AppRoute.SeasonReview.route)
            }
            loaded.weekCheckpoint.activePosition.phase == CalendarPhase.Offseason &&
                CoachingCarouselGate.blocksOffseasonShell(loaded.metadata.seasonIndex, carousel) -> {
                navController.navigate(AppRoute.CoachingCarousel.route)
            }
            loaded.weekCheckpoint.activePosition.phase == CalendarPhase.Offseason -> {
                navController.navigate(AppRoute.Offseason.route)
            }
            loaded.weekCheckpoint.activePosition.phase == CalendarPhase.Preseason -> {
                navController.navigate(AppRoute.Preseason.route)
            }
        }
    }
}
