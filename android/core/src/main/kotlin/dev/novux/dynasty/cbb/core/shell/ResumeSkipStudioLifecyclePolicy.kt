package dev.novux.dynasty.cbb.core.shell

/** Resume-from-background may skip studio when policy allows ([MEGA-SHELL-176]). */
object ResumeSkipStudioLifecyclePolicy {
    const val MEGA_ID: String = "MEGA-SHELL-176"

    fun skipStudioWhenResuming(warmResume: Boolean, userPrefersSkip: Boolean): Boolean =
        warmResume && userPrefersSkip
}