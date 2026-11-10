package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingActions
import dev.novux.dynasty.cbb.core.balance.BalanceVisitAllowedThrough
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.balance.BalanceVisitDistanceCosts
import java.util.UUID

enum class RecruitingRefundableAction {
    ScholarshipOffer,
    ScoutTheirGame,
    OfficialVisit,
}

enum class OfficialVisitDistanceBand {
    Local,
    Regional,
    Remote,
}

data class RecruitingWeekReservation(
    val reservationId: String,
    val prospectId: String,
    val action: RecruitingRefundableAction,
    val pointsReserved: Int,
)

/**
 * Week-bound reservations — credited back on week advance while still active
 * ([planning/recruiting-engine.md#refunded-actions-week-reservation]).
 */
object RecruitingReservationRefund {
    fun refundCredits(active: List<RecruitingWeekReservation>): Int =
        active.sumOf { it.pointsReserved }

    fun pointsAfterWeekCloseRefunds(
        pointsAvailableEndOfWeek: Int,
        active: List<RecruitingWeekReservation>,
    ): Int = pointsAvailableEndOfWeek + refundCredits(active)
}

object RecruitingRefundableActionSpend {
    fun spendScholarshipOffer(
        prospectId: String,
        pointsAvailable: Int,
        actions: BalanceRecruitingActions,
    ): Pair<RecruitingSpendResult, RecruitingWeekReservation?> =
        spendRefundable(
            RecruitingRefundableAction.ScholarshipOffer,
            prospectId,
            pointsAvailable,
            actions.scholarshipOffer.cost,
        )

    fun spendScoutTheirGame(
        prospectId: String,
        pointsAvailable: Int,
        actions: BalanceRecruitingActions,
        existingReservations: List<RecruitingWeekReservation>,
        consumption: RecruitingSeasonConsumption,
        phase: CalendarPhase,
    ): Pair<RecruitingSpendResult, RecruitingWeekReservation?> {
        if (!RecruitingActionWindows.scoutTheirGameOpen(phase, actions.scoutTheirGame)) {
            return blocked(RecruitingPointAction.ScoutTheirGame, pointsAvailable, RecruitingSpendFailure.ActionWindowClosed)
        }
        if (actions.scoutTheirGame.oncePerRecruitPerSeason && consumption.consumedScout(prospectId)) {
            return blocked(
                RecruitingPointAction.ScoutTheirGame,
                pointsAvailable,
                RecruitingSpendFailure.AlreadyUsedThisSeason,
            )
        }
        if (existingReservations.any {
                it.prospectId == prospectId && it.action == RecruitingRefundableAction.ScoutTheirGame
            }
        ) {
            return blocked(
                RecruitingPointAction.ScoutTheirGame,
                pointsAvailable,
                RecruitingSpendFailure.ReservationAlreadyActive,
            )
        }
        return spendRefundable(
            RecruitingRefundableAction.ScoutTheirGame,
            prospectId,
            pointsAvailable,
            actions.scoutTheirGame.cost,
        )
    }

    fun spendOfficialVisit(
        prospectId: String,
        pointsAvailable: Int,
        actions: BalanceRecruitingActions,
        distance: OfficialVisitDistanceBand,
        existingReservations: List<RecruitingWeekReservation>,
        consumption: RecruitingSeasonConsumption,
        phase: CalendarPhase,
        offseasonWeekIndex: Int?,
    ): Pair<RecruitingSpendResult, RecruitingWeekReservation?> {
        if (!RecruitingActionWindows.officialVisitOpen(
                phase,
                offseasonWeekIndex,
                actions.officialVisit.allowedThrough ?: BalanceVisitAllowedThrough(),
            )
        ) {
            return blocked(RecruitingPointAction.OfficialVisit, pointsAvailable, RecruitingSpendFailure.ActionWindowClosed)
        }
        if (actions.officialVisit.oncePerRecruitPerSeason && consumption.consumedOfficialVisit(prospectId)) {
            return blocked(
                RecruitingPointAction.OfficialVisit,
                pointsAvailable,
                RecruitingSpendFailure.AlreadyUsedThisSeason,
            )
        }
        if (existingReservations.any {
                it.prospectId == prospectId && it.action == RecruitingRefundableAction.OfficialVisit
            }
        ) {
            return blocked(
                RecruitingPointAction.OfficialVisit,
                pointsAvailable,
                RecruitingSpendFailure.ReservationAlreadyActive,
            )
        }
        val cost = visitCost(actions.officialVisit.costByDistanceBand, distance)
        return spendRefundable(
            RecruitingRefundableAction.OfficialVisit,
            prospectId,
            pointsAvailable,
            cost,
        )
    }

    fun revokeReservation(
        active: List<RecruitingWeekReservation>,
        reservationId: String,
    ): List<RecruitingWeekReservation> = active.filterNot { it.reservationId == reservationId }

    private fun spendRefundable(
        action: RecruitingRefundableAction,
        prospectId: String,
        pointsAvailable: Int,
        cost: Int,
    ): Pair<RecruitingSpendResult, RecruitingWeekReservation?> {
        if (cost > pointsAvailable) {
            return insufficient(mapToPointAction(action), pointsAvailable)
        }
        val reservation = RecruitingWeekReservation(
            reservationId = UUID.randomUUID().toString(),
            prospectId = prospectId,
            action = action,
            pointsReserved = cost,
        )
        val spend = RecruitingSpendResult(
            action = mapToPointAction(action),
            pointsCharged = cost,
            pointsRemaining = pointsAvailable - cost,
            countsAsWeekReservation = true,
            success = true,
        )
        return spend to reservation
    }

    private fun mapToPointAction(action: RecruitingRefundableAction): RecruitingPointAction =
        when (action) {
            RecruitingRefundableAction.ScholarshipOffer -> RecruitingPointAction.ScholarshipOffer
            RecruitingRefundableAction.ScoutTheirGame -> RecruitingPointAction.ScoutTheirGame
            RecruitingRefundableAction.OfficialVisit -> RecruitingPointAction.OfficialVisit
        }

    private fun visitCost(bands: BalanceVisitDistanceCosts, distance: OfficialVisitDistanceBand): Int =
        when (distance) {
            OfficialVisitDistanceBand.Local -> bands.local
            OfficialVisitDistanceBand.Regional -> bands.regional
            OfficialVisitDistanceBand.Remote -> bands.remote
        }

    private fun insufficient(
        action: RecruitingPointAction,
        pointsAvailable: Int,
    ): Pair<RecruitingSpendResult, RecruitingWeekReservation?> =
        blocked(action, pointsAvailable, RecruitingSpendFailure.InsufficientPoints)

    private fun blocked(
        action: RecruitingPointAction,
        pointsAvailable: Int,
        failure: RecruitingSpendFailure,
    ): Pair<RecruitingSpendResult, RecruitingWeekReservation?> =
        RecruitingSpendResult(
            action = action,
            pointsCharged = 0,
            pointsRemaining = pointsAvailable,
            countsAsWeekReservation = false,
            success = false,
            failure = failure,
        ) to null
}

/**
 * After week close, scout/visit reservations complete for the season; offers persist.
 */
object RecruitingWeekReservationClose {
    fun afterWeekAdvance(
        active: List<RecruitingWeekReservation>,
        consumption: RecruitingSeasonConsumption,
    ): Pair<List<RecruitingWeekReservation>, RecruitingSeasonConsumption> {
        var nextConsumption = consumption
        val stillActive = buildList {
            for (reservation in active) {
                when (reservation.action) {
                    RecruitingRefundableAction.ScholarshipOffer -> add(reservation)
                    RecruitingRefundableAction.ScoutTheirGame -> {
                        nextConsumption = nextConsumption.recordScout(reservation.prospectId)
                    }
                    RecruitingRefundableAction.OfficialVisit -> {
                        nextConsumption = nextConsumption.recordOfficialVisit(reservation.prospectId)
                    }
                }
            }
        }
        return stillActive to nextConsumption
    }
}
