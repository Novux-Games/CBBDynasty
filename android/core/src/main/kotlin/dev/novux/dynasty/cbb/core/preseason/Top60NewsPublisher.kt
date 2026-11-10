package dev.novux.dynasty.cbb.core.preseason

import dev.novux.dynasty.cbb.core.recruiting.RecruitProspect
import dev.novux.dynasty.cbb.core.signingday.NationalRecruitingRankings

object Top60NewsPublisher {
    fun publish(seasonIndex: Int, pool: List<RecruitProspect>): Top60NewsHeadline {
        val top60 = NationalRecruitingRankings.top60FromPool(pool)
        val lead = top60.firstOrNull()?.prospect?.displayName ?: "the top prospects"
        val headline = "Preseason Top 60: $lead leads the national class"
        return Top60NewsHeadline(
            seasonIndex = seasonIndex,
            headline = headline,
            top60 = top60,
        )
    }
}
