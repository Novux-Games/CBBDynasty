package dev.novux.dynasty.cbb.core.coach.security

data class AdTrustSnapshot(
    val score: Int,
    val band: AdTrustBand,
)

data class JobSecuritySnapshot(
    val score: Int,
    val tier: JobSecurityTier,
    val trend: JobSecurityTrend,
    val factors: List<JobSecurityFactor>,
)

data class CoachSecuritySnapshot(
    val jobSecurity: JobSecuritySnapshot,
    val adTrust: AdTrustSnapshot,
)
