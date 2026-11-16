package dev.novux.dynasty.cbb.core.signingday

import dev.novux.dynasty.cbb.core.recruiting.RecruitProspect
import kotlin.random.Random

/** Elite unsigned recruits land at AI programs when the user hard-signs their class ([MEGA-RECR-106]). */
object SigningDayUnsignedElsewherePolicy {
    const val MEGA_ID: String = "MEGA-RECR-106"

    const val MIN_STARS: Int = 4
    const val MAX_HEADLINES: Int = 6
}

data class SigningDayElsewhereCommit(
    val prospectId: String,
    val displayName: String,
    val stars: Int,
    val schoolName: String,
)

object SigningDayUnsignedElsewhereEngine {
    fun resolve(
        pool: List<RecruitProspect>,
        signedProspectIds: Set<String>,
        aiSchoolNames: List<String>,
        seed: Long,
    ): List<SigningDayElsewhereCommit> {
        if (aiSchoolNames.isEmpty()) return emptyList()
        val targets = pool
            .filter { it.prospectId !in signedProspectIds && it.stars >= SigningDayUnsignedElsewherePolicy.MIN_STARS }
            .sortedWith(compareByDescending<RecruitProspect> { it.stars }.thenBy { it.nationalHsRank ?: Int.MAX_VALUE })
            .take(SigningDayUnsignedElsewherePolicy.MAX_HEADLINES)
        val rng = Random(seed)
        return targets.map { prospect ->
            val school = aiSchoolNames[rng.nextInt(aiSchoolNames.size)]
            SigningDayElsewhereCommit(
                prospectId = prospect.prospectId,
                displayName = prospect.displayName,
                stars = prospect.stars,
                schoolName = school,
            )
        }
    }
}