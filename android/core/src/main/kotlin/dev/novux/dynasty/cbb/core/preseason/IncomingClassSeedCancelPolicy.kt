package dev.novux.dynasty.cbb.core.preseason

/** Cooperative cancel while long-running seed UI is active ([MEGA-RECR-115]). */
object IncomingClassSeedCancelPolicy {
    const val MEGA_ID: String = "MEGA-RECR-115"

    fun allowCancel(seedingInFlight: Boolean): Boolean = seedingInFlight
}