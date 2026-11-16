package dev.novux.dynasty.cbb.core.pack

/** Default embedded pack ships NCBA branding keys ([MEGA-WORLD-019]). */
object PackBrandingEmbeddedPolicy {
    const val MEGA_ID = "MEGA-WORLD-019"

    fun hasRoundKeys(branding: PackBrandingPack?): Boolean =
        branding?.roundOpening?.isNotBlank() == true && branding.roundNationalChampionship.isNotBlank()
}