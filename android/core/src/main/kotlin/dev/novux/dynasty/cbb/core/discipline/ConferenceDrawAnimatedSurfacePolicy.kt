package dev.novux.dynasty.cbb.core.discipline

/** Conference draw surfaced before instant resolve ([MEGA-SHELL-062]). */
object ConferenceDrawAnimatedSurfacePolicy {
    const val MEGA_ID: String = "MEGA-SHELL-062"

    fun showAnimatedDrawUi(): Boolean = true
}