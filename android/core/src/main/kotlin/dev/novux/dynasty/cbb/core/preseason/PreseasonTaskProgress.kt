package dev.novux.dynasty.cbb.core.preseason

data class PreseasonTaskProgress(
    val completed: Set<PreseasonTaskId> = emptySet(),
) {
    fun isComplete(id: PreseasonTaskId): Boolean = id in completed

    fun withComplete(id: PreseasonTaskId): PreseasonTaskProgress =
        copy(completed = completed + id)

    fun allRequiredComplete(): Boolean =
        PreseasonTaskId.entries.all { it in completed }

    fun schedulingUnlocked(): Boolean = isComplete(PreseasonTaskId.TournamentInvites)

    fun canComplete(id: PreseasonTaskId): Boolean = when (id) {
        PreseasonTaskId.ScheduleExhibitions,
        PreseasonTaskId.ScheduleRegularSeason,
        -> schedulingUnlocked()
        else -> true
    }
}
