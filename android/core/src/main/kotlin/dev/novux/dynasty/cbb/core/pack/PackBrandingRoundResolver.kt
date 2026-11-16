package dev.novux.dynasty.cbb.core.pack

/** Resolves postseason week labels from pack branding overlay ([MEGA-WORLD-019], [MEGA-WORLD-035]). */
object PackBrandingRoundResolver {
    const val MEGA_ID = "MEGA-WORLD-035"

    fun ncbaRoundLabel(weekIndex: Int, branding: PackBrandingPack = PackBrandingDefaults.embeddedDefault()): String =
        when (weekIndex) {
            1 -> branding.roundOpening
            2 -> branding.round1
            3 -> branding.round2
            4 -> branding.roundSuper16
            5 -> branding.roundGreat8
            6 -> branding.roundChampionship4
            7 -> branding.roundNationalChampionship
            else -> error("postseason week $weekIndex out of range")
        }
}