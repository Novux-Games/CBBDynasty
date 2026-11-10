package dev.novux.dynasty.cbb.core.player.scout

import dev.novux.dynasty.cbb.core.player.DynastyPlayer
import dev.novux.dynasty.cbb.core.player.archetype.PlayerArchetype

/** Scout view — category letters, not integers. */
data class PlayerScoutReport(
    val overall: String,
    val offense: String,
    val defense: String,
    val shooting: String,
    val handling: String,
    val athleticism: String,
    val potential: String,
    val archetype: PlayerArchetype,
    val archetypeFit: String?,
) {
    companion object {
        fun fromPlayer(player: DynastyPlayer, depth: ScoutDepth = ScoutDepth.Light): PlayerScoutReport {
            val fuzz = depth.fuzzSteps
            val r = player.ratings
            val shootingAvg = (r.shootingInside + r.shootingMid + r.shootingThree) / 3
            val handlePass = (r.handling + r.passing) / 2
            val athletic = (r.rebounding + r.blocking) / 2
            val fit = ScoutLetterScale.fromRating(
                (r.ovr + r.off + shootingAvg) / 3,
                fuzz,
            )
            return PlayerScoutReport(
                overall = ScoutLetterScale.fromRating(r.ovr, fuzz),
                offense = ScoutLetterScale.fromRating(r.off, fuzz),
                defense = ScoutLetterScale.fromRating(r.def, fuzz),
                shooting = ScoutLetterScale.fromRating(shootingAvg, fuzz),
                handling = ScoutLetterScale.fromRating(handlePass, fuzz),
                athleticism = ScoutLetterScale.fromRating(athletic, fuzz),
                potential = ScoutLetterScale.fromRating(r.potentialOverall, fuzz + 1),
                archetype = player.archetype,
                archetypeFit = if (depth == ScoutDepth.Unscouted) null else fit,
            )
        }
    }
}
