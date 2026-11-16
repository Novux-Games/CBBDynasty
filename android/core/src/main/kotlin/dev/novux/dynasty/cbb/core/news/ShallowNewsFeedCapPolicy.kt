package dev.novux.dynasty.cbb.core.news

/** Shallow dashboard feed cap 8–12 headlines ([MEGA-SHELL-145]). */
object ShallowNewsFeedCapPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-145"

    const val SPEC_MIN: Int = 8
    const val SPEC_MAX: Int = 12

    fun effectiveCap(requested: Int = NewsHeadlinePublisher.SHALLOW_FEED_CAP): Int {
        val minOne = requested.coerceAtLeast(1)
        if (requested == NewsHeadlinePublisher.SHALLOW_FEED_CAP) {
            return minOne.coerceIn(SPEC_MIN, SPEC_MAX)
        }
        return if (minOne > SPEC_MAX) SPEC_MAX else minOne
    }
}