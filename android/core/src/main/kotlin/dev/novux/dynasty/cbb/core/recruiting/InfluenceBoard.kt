package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingRecommended
import kotlin.math.roundToInt
import kotlin.random.Random

data class InfluenceLeaderRow(
    val programId: String,
    val displayName: String,
    val influenceScore: Int,
    val rank: Int,
)

data class ProspectInfluenceBoard(
    val closeness: Int,
    val userProgramId: String,
    val userRank: Int?,
    val leaders: List<InfluenceLeaderRow>,
)

/**
 * Parallel influence ranks + closeness ([planning/recruiting-engine.md#influence-board-design-lock]).
 */
object InfluenceBoardEngine {
    fun build(
        prospect: RecruitProspect,
        userSchool: SchoolRecruitingContext,
        userProgramId: String,
        recommended: BalanceRecruitingRecommended,
        userHasScholarshipOffer: Boolean = false,
    ): ProspectInfluenceBoard {
        val competitors = demoCompetitors(prospect.prospectId)
        val scores = competitors.map { competitor ->
            val school = SchoolRecruitingContext(
                programPrestige = competitor.prestige,
                schoolHomeState = competitor.homeState,
                pipelineStates = competitor.pipeline,
                nilPoolAnnualUsd = competitor.nilPool,
            )
            val snapshot = RecruitInterestEngine.baselineSnapshot(prospect, school, recommended)
            competitor.programId to snapshot.baselineInterest
        } + run {
            val userSnapshot = RecruitInterestEngine.baselineSnapshot(prospect, userSchool, recommended)
            val offerBoost = if (userHasScholarshipOffer) 8 else 0
            userProgramId to (userSnapshot.baselineInterest + offerBoost)
        }
        val ranked = scores.sortedByDescending { it.second }
        val leaders = ranked.mapIndexed { index, (programId, score) ->
            val name = competitors.firstOrNull { it.programId == programId }?.displayName ?: "Your program"
            InfluenceLeaderRow(
                programId = programId,
                displayName = name,
                influenceScore = score,
                rank = index + 1,
            )
        }
        val userRank = leaders.firstOrNull { it.programId == userProgramId }?.rank
        val top = leaders.firstOrNull()?.influenceScore ?: 0
        val closeness = (top * 0.85 + if (userHasScholarshipOffer) 12 else 0).roundToInt().coerceIn(0, 100)
        return ProspectInfluenceBoard(
            closeness = closeness,
            userProgramId = userProgramId,
            userRank = userRank,
            leaders = leaders.take(5),
        )
    }

    private data class CompetitorProgram(
        val programId: String,
        val displayName: String,
        val prestige: Int,
        val homeState: String,
        val pipeline: Set<String>,
        val nilPool: Int,
    )

    private fun demoCompetitors(prospectId: String): List<CompetitorProgram> {
        val random = Random(prospectId.hashCode().toLong() * 31)
        val names = listOf("Blue Ridge U", "Cedar Valley", "Iron City", "Gulf Coast A&M")
        return names.mapIndexed { index, name ->
            CompetitorProgram(
                programId = "ai-$index-$name",
                displayName = name,
                prestige = 35 + random.nextInt(45),
                homeState = listOf("TX", "FL", "NC", "KY")[index],
                pipeline = setOf("GA", "AL"),
                nilPool = 400_000 + random.nextInt(1_200_000),
            )
        }
    }
}

/** Decommit preserves interest; board/rank moves when another school leads. */
object InfluenceDecommitPolicy {
    fun afterUserDecommit(
        board: ProspectInfluenceBoard,
        preservedLiveInterest: Int,
        newLeaderProgramId: String?,
    ): Pair<Int, ProspectInfluenceBoard> {
        require(preservedLiveInterest in 0..100)
        if (newLeaderProgramId == null || newLeaderProgramId == board.userProgramId) {
            return preservedLiveInterest to board
        }
        val reordered = board.leaders.sortedByDescending { row ->
            when (row.programId) {
                newLeaderProgramId -> row.influenceScore + 15
                board.userProgramId -> row.influenceScore - 10
                else -> row.influenceScore
            }
        }.mapIndexed { index, row -> row.copy(rank = index + 1) }
        val userRank = reordered.firstOrNull { it.programId == board.userProgramId }?.rank
        val updated = board.copy(
            leaders = reordered,
            userRank = userRank,
            closeness = (board.closeness - 5).coerceIn(0, 100),
        )
        return preservedLiveInterest to updated
    }
}
