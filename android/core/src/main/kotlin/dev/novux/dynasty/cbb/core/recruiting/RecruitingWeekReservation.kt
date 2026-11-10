package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.balance.BalanceRecruitingActions
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
    ): Pair<RecruitingSpendResult, RecruitingWeekReservation?> {
        if (existingReservations.any {
                it.prospectId == prospectId && it.action == RecruitingRefundableAction.ScoutTheirGame
            }
        ) {
            return insufficient(RecruitingPointAction.ScholarshipOffer, pointsAvailable) to null
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
    ): Pair<RecruitingSpendResult, RecruitingWeekReservation?> {
        if (existingReservations.any {
                it.prospectId == prospectId && it.action == RecruitingRefundableAction.OfficialVisit
            }
        ) {
            return insufficient(RecruitingPointAction.ScholarshipOffer, pointsAvailable) to null
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
            return insufficient(mapToPointAction(action), pointsAvailable) to null
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
            RecruitingRefundableAction.ScoutTheirGame,
            RecruitingRefundableAction.OfficialVisit,
            -> RecruitingPointAction.ScholarshipOffer
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
    ): RecruitingSpendResult =
        RecruitingSpendResult(
            action = action,
            pointsCharged = 0,
            pointsRemaining = pointsAvailable,
            countsAsWeekReservation = false,
            success = false,
            failure = RecruitingSpendFailure.InsufficientPoints,
        )
}
