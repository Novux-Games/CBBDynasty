package dev.novux.dynasty.cbb.core.carousel

import java.util.UUID

/**
 * Carousel flow — background sim then user approvals ([planning/offseason.md], [planning/game-design.md]).
 */
object CoachingCarouselEngine {
    fun initialState(membershipSeasonIndex: Int): CoachingCarouselState =
        if (!CoachingCarouselPolicy.requiredForMembershipYear(membershipSeasonIndex)) {
            CoachingCarouselState(CarouselStatus.NotRequired)
        } else {
            CoachingCarouselState(CarouselStatus.AwaitingBackgroundSim)
        }

    fun runBackgroundSim(state: CoachingCarouselState): CoachingCarouselState {
        require(state.status == CarouselStatus.AwaitingBackgroundSim) {
            "background sim already run"
        }
        val cards = stubUserApprovalCards()
        val nextStatus = if (cards.isEmpty()) {
            CarouselStatus.Complete
        } else {
            CarouselStatus.AwaitingUserApprovals
        }
        return state.copy(
            status = nextStatus,
            pendingApprovals = cards,
            backgroundSimGeneration = state.backgroundSimGeneration + 1,
        )
    }

    fun resolveApproval(
        state: CoachingCarouselState,
        cardId: String,
        accepted: Boolean,
    ): CoachingCarouselState {
        require(state.status == CarouselStatus.AwaitingUserApprovals) {
            "no approvals pending"
        }
        val remaining = state.pendingApprovals.filterNot { it.id == cardId }
        val nextStatus = if (remaining.isEmpty()) CarouselStatus.Complete else CarouselStatus.AwaitingUserApprovals
        return state.copy(status = nextStatus, pendingApprovals = remaining)
    }

    fun skipToComplete(state: CoachingCarouselState): CoachingCarouselState =
        state.copy(status = CarouselStatus.Complete, pendingApprovals = emptyList())

    private fun stubUserApprovalCards(): List<CarouselApprovalCard> {
        val id = UUID.randomUUID().toString()
        return listOf(CarouselApprovalCatalog.card(id, CarouselApprovalKind.ContractExtension))
    }
}

object CoachingCarouselGate {
    fun blocksOffseasonShell(
        membershipSeasonIndex: Int,
        carousel: CoachingCarouselState,
    ): Boolean =
        CoachingCarouselPolicy.requiredForMembershipYear(membershipSeasonIndex) &&
            !carousel.canEnterOffseasonWeekOne()
}
