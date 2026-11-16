package dev.novux.dynasty.cbb.core.pack

import kotlinx.serialization.Serializable

/** NCBA / NCI / Heritage round and body labels swappable per pack ([planning/ncba-branding.md]). */
@Serializable
data class PackBrandingPack(
    val nationalBodyShort: String = PackBrandingDefaults.NATIONAL_BODY_SHORT,
    val marchPostseasonTitle: String = PackBrandingDefaults.MARCH_POSTSEASON_TITLE,
    val ncbaTournamentName: String = PackBrandingDefaults.NCBA_TOURNAMENT_NAME,
    val nciName: String = PackBrandingDefaults.NCI_NAME,
    val heritageClassicName: String = PackBrandingDefaults.HERITAGE_CLASSIC_NAME,
    val roundOpening: String = PackBrandingDefaults.ROUND_OPENING,
    val round1: String = PackBrandingDefaults.ROUND_1,
    val round2: String = PackBrandingDefaults.ROUND_2,
    val roundSuper16: String = PackBrandingDefaults.ROUND_SUPER_16,
    val roundGreat8: String = PackBrandingDefaults.ROUND_GREAT_8,
    val roundChampionship4: String = PackBrandingDefaults.ROUND_CHAMPIONSHIP_4,
    val roundNationalChampionship: String = PackBrandingDefaults.ROUND_NATIONAL_CHAMPIONSHIP,
)

object PackBrandingDefaults {
    const val NATIONAL_BODY_SHORT = "NCBA"
    const val MARCH_POSTSEASON_TITLE = "March Mayhem"
    const val NCBA_TOURNAMENT_NAME = "NCBA Men's BB Tournament"
    const val NCI_NAME = "National College Invitational"
    const val HERITAGE_CLASSIC_NAME = "National Heritage Classic"
    const val ROUND_OPENING = "Opening Round"
    const val ROUND_1 = "Round 1"
    const val ROUND_2 = "Round 2"
    const val ROUND_SUPER_16 = "Super 16"
    const val ROUND_GREAT_8 = "Great 8"
    const val ROUND_CHAMPIONSHIP_4 = "Championship 4"
    const val ROUND_NATIONAL_CHAMPIONSHIP = "National Championship"

    fun embeddedDefault(): PackBrandingPack = PackBrandingPack()
}