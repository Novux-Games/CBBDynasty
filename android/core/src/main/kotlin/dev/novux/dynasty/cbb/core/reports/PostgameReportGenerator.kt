package dev.novux.dynasty.cbb.core.reports

object PostgameReportGenerator {
    fun generate(
        userTeamName: String,
        opponentName: String,
        userScore: Int,
        opponentScore: Int,
    ): ReportArticle {
        val won = userScore > opponentScore
        val lost = userScore < opponentScore
        val outcome = when {
            won -> "a wire-to-wire statement"
            lost -> "a frustrating road bump"
            else -> "a grinding draw that leaves both staffs wanting more"
        }
        val headline = when {
            won -> "$userTeamName tops $opponentName ${userScore}-${opponentScore}"
            lost -> "$opponentName edges $userTeamName ${opponentScore}-${userScore}"
            else -> "$userTeamName and $opponentName settle at ${userScore}-${opponentScore}"
        }
        val paragraphs = listOf(
            "The final ledger shows $outcome for $userTeamName, with rebounding swings " +
                "and second-chance points coloring the possession log.",
            if (won) {
                "A balanced shot chart and timely stops in the second half allowed the home " +
                    "rotation to close without panic."
            } else if (lost) {
                "Cold stretches from the perimeter and a few late turnovers flipped momentum " +
                    "when the game tightened."
            } else {
                "Neither side found a late separator; free throws and offensive boards " +
                    "kept hope alive deep into the second half."
            },
            "Early NET and media poll updates may nudge resume numbers slightly, but the staff " +
                "will file this one under teaching tape before the next tip.",
        )
        val pullQuote = when {
            won -> "\"Guys stayed poised — we can build on this.\""
            lost -> "\"No panic. We correct and move to the next game.\""
            else -> "\"We'll take the fight, but we need a cleaner finish.\""
        }
        return ReportArticle(
            type = ReportArticleType.Postgame,
            headline = headline,
            paragraphs = paragraphs,
            pullQuote = pullQuote,
            scoreLine = "Final: $userTeamName $userScore, $opponentName $opponentScore",
            recordLine = when {
                won -> "Result: Win"
                lost -> "Result: Loss"
                else -> "Result: Tie (exhibition)"
            },
            projectedSeasonRecord = null,
        )
    }
}
