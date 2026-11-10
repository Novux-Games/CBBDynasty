package dev.novux.dynasty.cbb.core.strategy

/**
 * Merges offensive preset, defensive preset, and light 7×7 interaction nudges for one possession.
 */
object SchemeMatchupResolver {
    fun resolve(
        offense: OffensiveSchemePreset,
        defense: DefensiveSchemePreset,
    ): ResolvedSchemeEffects {
        val off = SchemeModifierCatalog.offensive(offense)
        val def = SchemeModifierCatalog.defensive(defense)
        val interaction = interactionNudge(offense, defense)
        val threeRate = (off.threePointAttemptRate + def.threePointAttemptAdjust + interaction.threeAdjust)
            .coerceIn(0.12, 0.62)
        val turnover = (off.turnoverChance + def.turnoverPressure + interaction.turnoverAdjust)
            .coerceIn(0.10, 0.40)
        val makeAdjust = off.makeRateBonus - def.makeRatePenalty + interaction.makeAdjust
        val pace = (off.tripPaceScale * interaction.tripPaceScale).coerceIn(0.82, 1.18)
        return ResolvedSchemeEffects(
            tripPaceScale = pace,
            threePointAttemptRate = threeRate,
            turnoverChance = turnover,
            makeRateAdjust = makeAdjust,
            offensiveRatingDelta = off.ratingDelta + interaction.offRatingDelta,
            defensiveRatingDelta = def.ratingDelta + interaction.defRatingDelta,
        )
    }

    private data class InteractionNudge(
        val threeAdjust: Double = 0.0,
        val turnoverAdjust: Double = 0.0,
        val makeAdjust: Double = 0.0,
        val tripPaceScale: Double = 1.0,
        val offRatingDelta: Int = 0,
        val defRatingDelta: Int = 0,
    )

    private fun interactionNudge(
        offense: OffensiveSchemePreset,
        defense: DefensiveSchemePreset,
    ): InteractionNudge = when (offense to defense) {
        OffensiveSchemePreset.Early to DefensiveSchemePreset.FullCourtPress ->
            InteractionNudge(turnoverAdjust = 0.03, tripPaceScale = 0.95)
        OffensiveSchemePreset.Perimeter to DefensiveSchemePreset.Zone23 ->
            InteractionNudge(threeAdjust = -0.02, makeAdjust = -0.02)
        OffensiveSchemePreset.Perimeter to DefensiveSchemePreset.Zone32 ->
            InteractionNudge(threeAdjust = 0.04, makeAdjust = -0.01)
        OffensiveSchemePreset.Post to DefensiveSchemePreset.Zone23 ->
            InteractionNudge(makeAdjust = -0.03)
        OffensiveSchemePreset.Drive to DefensiveSchemePreset.Zone23 ->
            InteractionNudge(makeAdjust = -0.02)
        OffensiveSchemePreset.Drive to DefensiveSchemePreset.SwitchingMan ->
            InteractionNudge(makeAdjust = -0.01, turnoverAdjust = 0.01)
        OffensiveSchemePreset.Motion to DefensiveSchemePreset.Zone131 ->
            InteractionNudge(turnoverAdjust = 0.02)
        else -> InteractionNudge()
    }
}
