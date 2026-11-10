package dev.novux.dynasty.cbb.ui.newgame

import androidx.annotation.StringRes
import dev.novux.dynasty.cbb.R

/**
 * Wizard steps 1–6 — [planning/game-start.md#new-game-wizard-overview].
 */
enum class NewGameStep(
    val index: Int,
    val routeSegment: String,
    @StringRes val titleRes: Int,
    @StringRes val subtitleRes: Int,
) {
    Coach(
        index = 1,
        routeSegment = "coach",
        titleRes = R.string.new_step_coach_title,
        subtitleRes = R.string.new_step_coach_subtitle,
    ),
    Archetype(
        index = 2,
        routeSegment = "archetype",
        titleRes = R.string.new_step_archetype_title,
        subtitleRes = R.string.new_step_archetype_subtitle,
    ),
    Schemes(
        index = 3,
        routeSegment = "schemes",
        titleRes = R.string.new_step_schemes_title,
        subtitleRes = R.string.new_step_schemes_subtitle,
    ),
    Settings(
        index = 4,
        routeSegment = "settings",
        titleRes = R.string.new_step_settings_title,
        subtitleRes = R.string.new_step_settings_subtitle,
    ),
    School(
        index = 5,
        routeSegment = "school",
        titleRes = R.string.new_step_school_title,
        subtitleRes = R.string.new_step_school_subtitle,
    ),
    SignContract(
        index = 6,
        routeSegment = "sign",
        titleRes = R.string.new_step_sign_title,
        subtitleRes = R.string.new_step_sign_subtitle,
    ),
    ;

    val next: NewGameStep? get() = entries.getOrNull(ordinal + 1)
    val previous: NewGameStep? get() = entries.getOrNull(ordinal - 1)

    companion object {
        const val ROUTE_PREFIX = "new_game_step"

        fun routeFor(step: NewGameStep): String = "$ROUTE_PREFIX/${step.routeSegment}"

        fun fromRoute(fullRoute: String?): NewGameStep? =
            entries.firstOrNull { fullRoute == routeFor(it) }
    }
}
