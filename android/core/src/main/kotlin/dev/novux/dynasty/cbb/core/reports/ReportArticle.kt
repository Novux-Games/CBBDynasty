package dev.novux.dynasty.cbb.core.reports

/**
 * Shared mini-article template ([planning/preseason.md#preseason-report--game-reports]).
 */
data class ReportArticle(
    val type: ReportArticleType,
    val headline: String,
    val paragraphs: List<String>,
    val pullQuote: String? = null,
    val scoreLine: String? = null,
    val recordLine: String? = null,
    val projectedSeasonRecord: String? = null,
) {
    init {
        require(paragraphs.size in 2..4) { "Mini-article expects 2–4 paragraphs" }
    }
}
