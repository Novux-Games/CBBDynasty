package dev.novux.dynasty.cbb.core.preseason

/**
 * Year 2+ preseason must not re-run [IncomingClassSeedRepository.runSeed] on an already-seeded key
 * ([MEGA-RECR-129]).
 */
object IncomingClassSeedReseedGuardPolicy {
    const val MEGA_ID: String = "MEGA-RECR-129"

    fun mayRunInitialSeed(alreadySeeded: Boolean): Boolean = !alreadySeeded

    fun mayConfirmExistingHeadline(headlinePresent: Boolean, seedingInFlight: Boolean): Boolean =
        headlinePresent && !seedingInFlight
}