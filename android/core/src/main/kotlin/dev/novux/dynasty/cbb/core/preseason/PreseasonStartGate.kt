package dev.novux.dynasty.cbb.core.preseason

object PreseasonStartGate {
    fun canStartSeason(progress: PreseasonTaskProgress): Boolean = progress.allRequiredComplete()
}
