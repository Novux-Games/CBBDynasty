package dev.novux.dynasty.cbb.core.recruiting.ai

import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingEconomy
import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingRecommended
import dev.novux.dynasty.cbb.core.recruiting.RecruitProspect
import dev.novux.dynasty.cbb.core.recruiting.RecommendedRecruitList
import dev.novux.dynasty.cbb.core.recruiting.RecruitingPrestigePool
import dev.novux.dynasty.cbb.core.recruiting.SchoolRecruitingContext

data class AiRecruitingWeekResult(
    val leagueBoards: List<AiRecruitingBoard>,
    val userBoard: AiRecruitingBoard?,
    val userAutoMaintained: Boolean,
    val userPointsSpent: Int,
    val leagueProgramsProcessed: Int,
)

object AiRecruitingWeekRunner {
    private const val OFFER_COST = 150
    private const val DM_COST = 50

    fun runWeek(
        prospectPool: List<RecruitProspect>,
        economy: BalanceRecruitingEconomy,
        recommended: BalanceRecruitingRecommended,
        userProgramId: String,
        userSchool: SchoolRecruitingContext,
        userPointsAvailable: Int,
        userPointsSpentByPlayer: Int,
        autoMaintainUserSchool: Boolean,
        leaguePrograms: List<AiProgramSnapshot>,
    ): AiRecruitingWeekResult {
        val leagueBoards = leaguePrograms.map { program ->
            val weeklyPoints = RecruitingPrestigePool.weeklyPoolPoints(economy, program.programPrestige)
            buildBoard(
                programId = program.programId,
                school = program.toSchoolContext(),
                weeklyPoints = weeklyPoints,
                pool = prospectPool,
                economy = economy,
                recommended = recommended,
            )
        }
        val shouldAutoMaintain = autoMaintainUserSchool && userPointsSpentByPlayer == 0 && userPointsAvailable > 0
        val userBoard = if (shouldAutoMaintain) {
            buildBoard(
                programId = userProgramId,
                school = userSchool,
                weeklyPoints = userPointsAvailable,
                pool = prospectPool,
                economy = economy,
                recommended = recommended,
            )
        } else {
            null
        }
        return AiRecruitingWeekResult(
            leagueBoards = leagueBoards,
            userBoard = userBoard,
            userAutoMaintained = userBoard != null,
            userPointsSpent = userBoard?.pointsSpentThisWeek ?: 0,
            leagueProgramsProcessed = leagueBoards.size,
        )
    }

    private fun buildBoard(
        programId: String,
        school: SchoolRecruitingContext,
        weeklyPoints: Int,
        pool: List<RecruitProspect>,
        economy: BalanceRecruitingEconomy,
        recommended: BalanceRecruitingRecommended,
    ): AiRecruitingBoard {
        val ordered = RecommendedRecruitList.build(pool, school, recommended)
        val cap = AiRecruitingCapacity.coreTargetCap(economy, school.programPrestige)
        val targets = ordered.take(cap).map { it.prospect.prospectId }
        var remaining = weeklyPoints
        for (targetId in targets) {
            if (targetId.isEmpty()) continue
            if (remaining < DM_COST) break
            val offer = if (remaining >= OFFER_COST) OFFER_COST else 0
            val dm = if (remaining - offer >= DM_COST) DM_COST else 0
            remaining -= (offer + dm)
        }
        val spent = weeklyPoints - remaining
        return AiRecruitingBoard(
            programId = programId,
            targetProspectIds = targets,
            pointsSpentThisWeek = spent,
        )
    }
}
