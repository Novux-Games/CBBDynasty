package dev.novux.dynasty.cbb.core.roster

object RosterConstants {
    const val SCHOLARSHIP_CAP = 15
}

/**
 * Bootstrap Season 1 roster at sign — [docs/BUILD_CHECKLIST.md] step 4.7.
 */
object SeasonOneRosterGenerator {
    private val classRotation = listOf(
        PlayerClassYear.Senior,
        PlayerClassYear.Junior,
        PlayerClassYear.Sophomore,
        PlayerClassYear.Freshman,
    )

    fun generate(
        dynastyId: String,
        rosterCap: Int = RosterConstants.SCHOLARSHIP_CAP,
    ): List<RosterPlayerSeed> {
        require(rosterCap == RosterConstants.SCHOLARSHIP_CAP) {
            "Season 1 bootstrap targets scholarship cap ${RosterConstants.SCHOLARSHIP_CAP}"
        }
        val positions = buildList {
            repeat(3) { addAll(PlayerPosition.STARTER_ORDER) }
        }.take(rosterCap)
        return positions.mapIndexed { index, position ->
            val classYear = classRotation[index % classRotation.size]
            val starter = index < PlayerPosition.STARTER_ORDER.size
            RosterPlayerSeed(
                playerId = "$dynastyId-player-${index + 1}",
                dynastyId = dynastyId,
                position = position,
                classYear = classYear,
                isStarter = starter,
                depthOrder = index + 1,
            )
        }
    }
}
