package dev.novux.dynasty.cbb.core.portal

import dev.novux.dynasty.cbb.core.roster.PlayerClassYear
import java.util.UUID
import kotlin.math.roundToInt
import kotlin.random.Random

/**
 * National portal sim — considering pool → final entrants
 * ([planning/transfer-portal.md#pool-scale], [planning/transfer-portal.md#ai-entry-drivers]).
 */
object PortalSimulationEngine {
    fun rollConsideringPoolSize(random: Random): Int =
        random.nextInt(
            PortalConstants.CONSIDERING_POOL_MIN,
            PortalConstants.CONSIDERING_POOL_MAX + 1,
        )

    fun simulateOffseasonCycle(
        membershipSeasonIndex: Int,
        random: Random = Random.Default,
    ): PortalCycleState {
        val consideringSize = rollConsideringPoolSize(random)
        val targetEntrants = targetEntrantCount(random)
        val considering = generateConsideringPlayers(consideringSize, membershipSeasonIndex, random)
        val entrants = selectFinalEntrants(considering, targetEntrants.coerceAtMost(consideringSize), random)
        return PortalCycleState(
            membershipSeasonIndex = membershipSeasonIndex,
            consideringPoolSize = consideringSize,
            entrants = entrants,
            top40Locked = false,
        )
    }

    private fun targetEntrantCount(random: Random): Int {
        val perTeam = PortalConstants.ENTRANTS_PER_TEAM_MIN +
            random.nextDouble() * (PortalConstants.ENTRANTS_PER_TEAM_MAX - PortalConstants.ENTRANTS_PER_TEAM_MIN)
        return (PortalConstants.DI_PROGRAM_COUNT * perTeam).roundToInt()
    }

    private data class ConsideringPlayer(
        val entrantId: String,
        val priorProgramId: String,
        val classYear: PlayerClassYear,
        val hiddenRating: Int,
        val entryScore: Double,
        val eligibilityClock: PortalEligibilityClock,
    )

    private fun generateConsideringPlayers(
        count: Int,
        membershipSeasonIndex: Int,
        random: Random,
    ): List<ConsideringPlayer> {
        val classes = PlayerClassYear.entries
        return List(count) { index ->
            val programSlot = index % PortalConstants.DI_PROGRAM_COUNT
            val priorProgramId = "team-conf-sim-$programSlot"
            val classYear = classes[random.nextInt(classes.size)]
            val hiddenRating = random.nextInt(45, 98)
            val volatility = random.nextDouble() * 0.15
            val driver = random.nextDouble() * 0.25
            val entryScore = hiddenRating + volatility * 100 + driver * 50
            ConsideringPlayer(
                entrantId = UUID.randomUUID().toString(),
                priorProgramId = priorProgramId,
                classYear = classYear,
                hiddenRating = hiddenRating,
                entryScore = entryScore,
                eligibilityClock = PortalEligibilityClock(
                    clockStartSeasonIndex = membershipSeasonIndex - random.nextInt(0, 4),
                    seasonsOfCompetitionUsed = random.nextInt(0, 3),
                ),
            )
        }
    }

    private fun selectFinalEntrants(
        considering: List<ConsideringPlayer>,
        entrantCount: Int,
        random: Random,
    ): List<PortalEntrant> {
        val picked = considering
            .shuffled(random)
            .sortedByDescending { it.entryScore }
            .take(entrantCount)
        return picked.map { row ->
            PortalEntrant(
                entrantId = row.entrantId,
                priorProgramId = row.priorProgramId,
                classYear = row.classYear,
                hiddenRating = row.hiddenRating,
                stars = null,
                nationalTop40Rank = null,
                eligibilityClock = row.eligibilityClock,
            )
        }
    }
}
