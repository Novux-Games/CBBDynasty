package dev.novux.dynasty.cbb.core.strategy

/** Seven offensive and seven defensive sim profiles — checklist 10.3. */
object SchemeModifierCatalog {
     fun offensive(preset: OffensiveSchemePreset): OffensiveSchemeSimProfile = when (preset) {
        OffensiveSchemePreset.Balanced -> OffensiveSchemeSimProfile()
        OffensiveSchemePreset.Motion -> OffensiveSchemeSimProfile(
            tripPaceScale = 1.02,
            threePointAttemptRate = 0.36,
            turnoverChance = 0.21,
        )
        OffensiveSchemePreset.Drive -> OffensiveSchemeSimProfile(
            tripPaceScale = 0.98,
            threePointAttemptRate = 0.30,
            turnoverChance = 0.23,
            makeRateBonus = 0.02,
        )
        OffensiveSchemePreset.Set -> OffensiveSchemeSimProfile(
            tripPaceScale = 1.08,
            threePointAttemptRate = 0.35,
            turnoverChance = 0.20,
        )
        OffensiveSchemePreset.Early -> OffensiveSchemeSimProfile(
            tripPaceScale = 0.90,
            threePointAttemptRate = 0.37,
            turnoverChance = 0.25,
        )
        OffensiveSchemePreset.Perimeter -> OffensiveSchemeSimProfile(
            tripPaceScale = 0.96,
            threePointAttemptRate = 0.48,
            turnoverChance = 0.21,
        )
        OffensiveSchemePreset.Post -> OffensiveSchemeSimProfile(
            tripPaceScale = 1.05,
            threePointAttemptRate = 0.28,
            turnoverChance = 0.22,
            makeRateBonus = 0.015,
        )
    }

    fun defensive(preset: DefensiveSchemePreset): DefensiveSchemeSimProfile = when (preset) {
        DefensiveSchemePreset.ManToMan -> DefensiveSchemeSimProfile()
        DefensiveSchemePreset.Zone23 -> DefensiveSchemeSimProfile(
            ratingDelta = 2,
            threePointAttemptAdjust = -0.02,
            makeRatePenalty = 0.03,
        )
        DefensiveSchemePreset.Zone32 -> DefensiveSchemeSimProfile(
            ratingDelta = 1,
            turnoverPressure = 0.01,
            threePointAttemptAdjust = 0.03,
        )
        DefensiveSchemePreset.Zone131 -> DefensiveSchemeSimProfile(
            turnoverPressure = 0.04,
        )
        DefensiveSchemePreset.BoxAndOne -> DefensiveSchemeSimProfile(ratingDelta = 1)
        DefensiveSchemePreset.FullCourtPress -> DefensiveSchemeSimProfile(
            ratingDelta = -1,
            turnoverPressure = 0.06,
        )
        DefensiveSchemePreset.SwitchingMan -> DefensiveSchemeSimProfile(
            ratingDelta = 1,
            threePointAttemptAdjust = -0.015,
            makeRatePenalty = 0.01,
        )
    }
}
