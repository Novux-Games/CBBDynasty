package dev.novux.dynasty.cbb.core.shell

/** Splash spinner gated on pack index readiness ([MEGA-SHELL-179]). */
object SplashReadySpinnerPolicy {
    const val MEGA_ID: String = "MEGA-SHELL-179"

    fun showSpinnerUntilReady(packIndexReady: Boolean): Boolean = !packIndexReady
}