package dev.novux.dynasty.cbb.core.reports

import kotlin.math.roundToInt

object PreseasonReportGenerator {
    fun generate(
        teamDisplayName: String,
        conferenceDisplayName: String,
        programPrestige: Int,
        topSixtyOneLiner: String,
        seasonYearLabel: String,
    ): ReportArticle {
        val projected = projectedRecord(programPrestige)
        val headline = "$teamDisplayName enters $seasonYearLabel with momentum"
        val paragraphs = listOf(
            "Practice courts across the $conferenceDisplayName are buzzing as $teamDisplayName " +
                "wraps camp and turns toward the schedule.",
            "Coaching staff likes the roster's versatility: shooters on the perimeter, size in the paint, " +
                "and enough depth to survive the grind of a 32-game regular season.",
            "The transfer portal reshuffled minutes league-wide, but the home program believes continuity " +
                "in the system gives them an edge on early November tripstops.",
            topSixtyOneLiner,
        )
        val pullQuote = "\"We know where we stack up — now we have to prove it every week.\""
        return ReportArticle(
            type = ReportArticleType.Preseason,
            headline = headline,
            paragraphs = paragraphs,
            pullQuote = pullQuote,
            scoreLine = null,
            recordLine = "Record: 0-0 (preseason)",
            projectedSeasonRecord = projected,
        )
    }

    private fun projectedRecord(prestige: Int): String {
        val wins = (prestige / 100.0 * 32.0).roundToInt().coerceIn(6, 28)
        val losses = 32 - wins
        return "Projected record: $wins-$losses"
    }
}
