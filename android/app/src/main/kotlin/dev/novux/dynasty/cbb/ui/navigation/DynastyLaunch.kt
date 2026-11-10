package dev.novux.dynasty.cbb.ui.navigation

import androidx.navigation.NavHostController
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.calendar.SeasonReviewGate
import dev.novux.dynasty.cbb.core.carousel.CoachingCarouselGate
import dev.novux.dynasty.cbb.core.carousel.CoachingCarouselState
import dev.novux.dynasty.cbb.core.coach.employment.CoachEmploymentStatus
import dev.novux.dynasty.cbb.core.coach.employment.CoachEmploymentState
import dev.novux.dynasty.cbb.core.save.LoadedDynasty

/** Routes after a dynasty load or sign ([planning/game-start.md], [planning/offseason.md]). */
object DynastyLaunch {
    fun navigateForLoaded(
        navController: NavHostController,
        loaded: LoadedDynasty,
        carousel: CoachingCarouselState,
        employment: CoachEmploymentState = CoachEmploymentState(),
    ) {
        when {
            employment.status == CoachEmploymentStatus.Unemployed -> {
                navController.navigate(AppRoute.JobCenter.route)
            }
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
            loaded.weekCheckpoint.activePosition.phase in IN_SEASON_PHASES -> {
                navController.navigate(AppRoute.DynastyShell.route)
            }
        }
    }

    private val IN_SEASON_PHASES = setOf(
        CalendarPhase.Exhibition,
        CalendarPhase.RegularSeason,
        CalendarPhase.ConferenceTournament,
        CalendarPhase.Postseason,
    )
}
