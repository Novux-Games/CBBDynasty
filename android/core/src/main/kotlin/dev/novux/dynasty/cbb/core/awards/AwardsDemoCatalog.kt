package dev.novux.dynasty.cbb.core.awards

/** Sample prior-season rows until phase 14.3 archive compute lands. */
object AwardsDemoCatalog {
    fun ncbaOverview(seasonLabel: String): List<AwardHonorLine> = listOf(
        AwardHonorLine("NCBA MVP", "Jordan Ellis", "Blue Valley", runnerUpName = "Mason Reed"),
        AwardHonorLine("NCBA Coach of the Year", "Pat Morrison", "Cedar Ridge", runnerUpName = "Alicia Boone"),
    )

    fun ncbaPositionPoy(position: String): List<AwardHonorLine> = listOf(
        AwardHonorLine(
            awardLabel = "NCBA $position POY",
            winnerName = "Demo Winner",
            schoolName = "State U",
            runnerUpName = "Demo Runner-up",
        ),
    )

    fun conferenceOverview(conferenceName: String): List<AwardHonorLine> = listOf(
        AwardHonorLine(
            "Conference POY",
            "Chris Alvarez",
            conferenceName,
            runnerUpName = "Tyler Dunn",
        ),
        AwardHonorLine(
            "Conference Coach of the Year",
            "Sam Ortega",
            "North Plaza",
            runnerUpName = "Riley Kent",
        ),
    )

    fun weeklyPotw(rsWeekIndex: Int, conferenceName: String): AwardHonorLine =
        AwardHonorLine(
            awardLabel = "Conference Player of the Week",
            winnerName = "Week $rsWeekIndex Standout",
            schoolName = conferenceName,
        )
}
