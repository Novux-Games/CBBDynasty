package dev.novux.dynasty.cbb.core.reports

/** Preseason report cites enrolling-class / Top 60 outlook ([MEGA-RECR-114]). */
object PreseasonReportRecruitingOutlookPolicy {
    const val MEGA_ID: String = "MEGA-RECR-114"

    fun outlookParagraph(topSixtyHeadline: String, top60Count: Int): String =
        "Recruiting outlook: $topSixtyHeadline " +
            "The national board lists $top60Count prospects in the Top 60 — see News hub Recruits for the full class."
}