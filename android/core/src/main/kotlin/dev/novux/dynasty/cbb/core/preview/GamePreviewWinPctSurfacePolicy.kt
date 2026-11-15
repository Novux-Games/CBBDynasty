package dev.novux.dynasty.cbb.core.preview

/** Game preview surfaces win pct + predicted score ([MEGA-SIM-052]). */
object GamePreviewWinPctSurfacePolicy {
    const val MEGA_ID: String = "MEGA-SIM-052"

    fun showPrimaryMetrics(): Boolean = true
}