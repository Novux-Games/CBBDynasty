package dev.novux.dynasty.cbb.core.signingday

/** Signing Day rankings tab mirrors News hub Recruits / Top 60 context ([MEGA-RECR-108]). */
object SigningDayRankingsTop60ContextPolicy {
    const val MEGA_ID: String = "MEGA-RECR-108"

    fun contextLine(top60HeadlineTitle: String?, classSize: Int): String? {
        val headline = top60HeadlineTitle?.takeIf { it.isNotBlank() }
        return when {
            headline != null ->
                "National board: $headline — ${classSize} recruits on your Signing Day list match News hub Recruits."
            classSize > 0 ->
                "Review the full Top 60 in News hub Recruits for national context beyond this list."
            else -> null
        }
    }
}