package dev.novux.dynasty.cbb.core.news

/** Postseason awards headlines use `NewsHeadlineKind.Awards` ([MEGA-SHELL-140]). */
object AwardsNewsPublishPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-140"

    fun publishesViaAwardsKind(): Boolean = true
}