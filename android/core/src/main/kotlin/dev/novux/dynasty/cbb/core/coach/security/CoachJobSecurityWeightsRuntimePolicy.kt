package dev.novux.dynasty.cbb.core.coach.security

/** MEGA queue batch 36 — [MEGA-COACH-048]. */
object CoachJobSecurityWeightsRuntimePolicy {
    const val MEGA_ID = "MEGA-COACH-048"
    fun weightsLoadedFromBalance(jobSecurityWeights: Map<String, Double>?): Boolean = jobSecurityWeights != null
}
