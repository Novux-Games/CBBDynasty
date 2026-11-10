package dev.novux.dynasty.cbb.ui.sim

import dev.novux.dynasty.cbb.core.strategy.GameplanInput

/** In-memory launch payload for watch sim (same dynasty session). */
object GameWatchLaunch {
    var homeGameplan: GameplanInput = GameplanInput()
    var homeName: String = "Home"
    var awayName: String = "Away"
}
