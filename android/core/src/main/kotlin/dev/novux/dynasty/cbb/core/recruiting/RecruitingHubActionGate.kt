package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingActions
import dev.novux.dynasty.cbb.core.balance.BalanceVisitAllowedThrough
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase

enum class RecruitingActionBlockReason {
    WindowClosed,
    AlreadyUsedThisSeason,
    ReservationActiveThisWeek,
    InsufficientPoints,
}

data class RecruitingHubActionGate(
    val allowed: Boolean,
    val pointCost: Int,
    val blockReason: RecruitingActionBlockReason? = null,
)

data class RecruitingProspectActionGates(
    val scoutTheirGame: RecruitingHubActionGate,
    val officialVisit: RecruitingHubActionGate,
    val visitDistance: OfficialVisitDistanceBand,
)

object RecruitingHubActionGateEvaluator {
    fun forProspect(
        prospectId: String,
        prospectHomeState: String,
        phase: CalendarPhase,
        offseasonWeekIndex: Int?,
        pointsAvailable: Int,
        actions: BalanceRecruitingActions,
        school: SchoolRecruitingContext,
        consumption: RecruitingSeasonConsumption,
        activeReservations: List<RecruitingWeekReservation>,
    ): RecruitingProspectActionGates {
        val visitDistance = OfficialVisitDistanceResolver.resolve(prospectHomeState, school)
        val scoutCost = actions.scoutTheirGame.cost
        val visitCost = visitCostFor(actions, visitDistance)
        return RecruitingProspectActionGates(
            scoutTheirGame = scoutGate(
                prospectId,
                phase,
                pointsAvailable,
                scoutCost,
                actions,
                consumption,
                activeReservations,
            ),
            officialVisit = visitGate(
                prospectId,
                phase,
                offseasonWeekIndex,
                pointsAvailable,
                visitCost,
                actions,
                consumption,
                activeReservations,
            ),
            visitDistance = visitDistance,
        )
    }

    private fun scoutGate(
        prospectId: String,
        phase: CalendarPhase,
        pointsAvailable: Int,
        cost: Int,
        actions: BalanceRecruitingActions,
        consumption: RecruitingSeasonConsumption,
        active: List<RecruitingWeekReservation>,
    ): RecruitingHubActionGate {
        if (!RecruitingActionWindows.scoutTheirGameOpen(phase, actions.scoutTheirGame)) {
            return RecruitingHubActionGate(false, cost, RecruitingActionBlockReason.WindowClosed)
        }
        if (actions.scoutTheirGame.oncePerRecruitPerSeason && consumption.consumedScout(prospectId)) {
            return RecruitingHubActionGate(false, cost, RecruitingActionBlockReason.AlreadyUsedThisSeason)
        }
        if (active.any {
                it.prospectId == prospectId && it.action == RecruitingRefundableAction.ScoutTheirGame
            }
        ) {
            return RecruitingHubActionGate(false, cost, RecruitingActionBlockReason.ReservationActiveThisWeek)
        }
        if (cost > pointsAvailable) {
            return RecruitingHubActionGate(false, cost, RecruitingActionBlockReason.InsufficientPoints)
        }
        return RecruitingHubActionGate(true, cost)
    }

    private fun visitGate(
        prospectId: String,
        phase: CalendarPhase,
        offseasonWeekIndex: Int?,
        pointsAvailable: Int,
        cost: Int,
        actions: BalanceRecruitingActions,
        consumption: RecruitingSeasonConsumption,
        active: List<RecruitingWeekReservation>,
    ): RecruitingHubActionGate {
        if (!RecruitingActionWindows.officialVisitOpen(
                phase,
                offseasonWeekIndex,
                actions.officialVisit.allowedThrough ?: BalanceVisitAllowedThrough(),
            )
        ) {
            return RecruitingHubActionGate(false, cost, RecruitingActionBlockReason.WindowClosed)
        }
        if (actions.officialVisit.oncePerRecruitPerSeason && consumption.consumedOfficialVisit(prospectId)) {
            return RecruitingHubActionGate(false, cost, RecruitingActionBlockReason.AlreadyUsedThisSeason)
        }
        if (active.any {
                it.prospectId == prospectId && it.action == RecruitingRefundableAction.OfficialVisit
            }
        ) {
            return RecruitingHubActionGate(false, cost, RecruitingActionBlockReason.ReservationActiveThisWeek)
        }
        if (cost > pointsAvailable) {
            return RecruitingHubActionGate(false, cost, RecruitingActionBlockReason.InsufficientPoints)
        }
        return RecruitingHubActionGate(true, cost)
    }

    private fun visitCostFor(actions: BalanceRecruitingActions, distance: OfficialVisitDistanceBand): Int {
        val bands = actions.officialVisit.costByDistanceBand
        return when (distance) {
            OfficialVisitDistanceBand.Local -> bands.local
            OfficialVisitDistanceBand.Regional -> bands.regional
            OfficialVisitDistanceBand.Remote -> bands.remote
        }
    }
}
