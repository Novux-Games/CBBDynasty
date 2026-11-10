package dev.novux.dynasty.cbb.core.signingday

data class SigningDayProgress(
    val cycleSeasonIndex: Int,
    val rankingsViewed: Boolean = false,
    val positionsConfirmed: Boolean = false,
    val hardSignComplete: Boolean = false,
) {
    fun requiredTasksComplete(): Boolean = rankingsViewed && positionsConfirmed

    fun canAdvanceOffseasonWeek(): Boolean = hardSignComplete
}

data class SigningDayHardSignResult(
    val signed: List<IncomingSignee>,
    val progress: SigningDayProgress,
)

object SigningDayHardSignEngine {
    fun finalizeClass(
        progress: SigningDayProgress,
        signees: List<IncomingSignee>,
    ): SigningDayHardSignResult {
        require(progress.requiredTasksComplete()) {
            "Signing Day required tasks incomplete"
        }
        return SigningDayHardSignResult(
            signed = signees,
            progress = progress.copy(hardSignComplete = true),
        )
    }
}
