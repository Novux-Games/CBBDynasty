package dev.novux.dynasty.cbb.core.reports

import dev.novux.dynasty.cbb.core.preview.GamePreviewResult

object PregameReportGenerator {
    fun generate(
        userTeamName: String,
        opponentName: String,
        venuePhrase: String,
        preview: GamePreviewResult,
    ): ReportArticle {
        val headline = "$userTeamName preps for $opponentName on $venuePhrase"
        val paragraphs = listOf(
            "Scouting reports pin $opponentName as a measuring-stick trip for $userTeamName, " +
                "with pace and turnover margin likely deciding the night.",
            "The staff leaned on film from similar resume profiles: spacing on one side, " +
                "rim protection on the other, and late-clock execution when the crowd tightens.",
            "Our model gives $userTeamName a ${preview.userWinPercent}% win chance with a " +
                "projected final near ${preview.predictedUserScore}-${preview.predictedOpponentScore}, " +
                "though November basketball rarely reads the script.",
        )
        return ReportArticle(
            type = ReportArticleType.Pregame,
            headline = headline,
            paragraphs = paragraphs,
            pullQuote = "\"We respect them, but this is about our standard on both ends.\"",
            scoreLine = "Projected final: ${preview.predictedUserScore}-${preview.predictedOpponentScore}",
            recordLine = "Win chance: ${preview.userWinPercent}%",
            projectedSeasonRecord = null,
        )
    }
}
