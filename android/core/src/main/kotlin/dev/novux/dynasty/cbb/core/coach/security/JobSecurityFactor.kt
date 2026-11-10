package dev.novux.dynasty.cbb.core.coach.security

enum class JobSecurityFactorContribution {
    Helping,
    Hurting,
    Neutral,
}

data class JobSecurityFactor(
    val label: String,
    val contribution: JobSecurityFactorContribution,
    val summary: String,
)
