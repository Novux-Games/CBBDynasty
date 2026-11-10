package dev.novux.dynasty.cbb.ui.preview

import dev.novux.dynasty.cbb.core.preview.GamePreviewResult
import dev.novux.dynasty.cbb.core.preview.GamePreviewVenue

object GamePreviewLaunch {
    var matchupLabel: String = ""
    var userTeamName: String = ""
    var opponentName: String = ""
    var venue: GamePreviewVenue = GamePreviewVenue.Home
    var venuePhrase: String = ""
    var result: GamePreviewResult? = null
}
