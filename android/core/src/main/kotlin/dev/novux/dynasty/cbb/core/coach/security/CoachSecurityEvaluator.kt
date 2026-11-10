package dev.novux.dynasty.cbb.core.coach.security

import dev.novux.dynasty.cbb.core.school.SchoolSeasonRecord

object CoachSecurityEvaluator {
    fun bootstrap(contractYearsLeft: Int): CoachSecuritySnapshot =
        build(
            jobSecurityScore = 72,
            adTrustScore = 68,
            trend = JobSecurityTrend.Flat,
            contractYearsLeft = contractYearsLeft,
            record = SchoolSeasonRecord.Zero,
        )

    fun build(
        jobSecurityScore: Int,
        adTrustScore: Int,
        trend: JobSecurityTrend,
        contractYearsLeft: Int,
        record: SchoolSeasonRecord,
    ): CoachSecuritySnapshot {
        val adTrust = AdTrustSnapshot(
            score = adTrustScore.coerceIn(0, 100),
            band = AdTrustBand.fromScore(adTrustScore),
        )
        val adjusted = (jobSecurityScore + (adTrustScore - 50) / 10).coerceIn(0, 100)
        val factors = mutableListOf<JobSecurityFactor>()
        factors += JobSecurityFactor(
            label = "AD trust",
            contribution = when (adTrust.band) {
                AdTrustBand.Strong -> JobSecurityFactorContribution.Helping
                AdTrustBand.Low -> JobSecurityFactorContribution.Hurting
                AdTrustBand.Steady -> JobSecurityFactorContribution.Neutral
            },
            summary = "Athletics director relationship at ${adTrust.band.label.lowercase()} level",
        )
        factors += JobSecurityFactor(
            label = "Contract runway",
            contribution = when {
                contractYearsLeft >= 3 -> JobSecurityFactorContribution.Helping
                contractYearsLeft <= 1 -> JobSecurityFactorContribution.Hurting
                else -> JobSecurityFactorContribution.Neutral
            },
            summary = "$contractYearsLeft year(s) remaining on current deal",
        )
        factors += JobSecurityFactor(
            label = "Season trajectory",
            contribution = when {
                record.wins > record.losses -> JobSecurityFactorContribution.Helping
                record.losses > record.wins + 2 -> JobSecurityFactorContribution.Hurting
                else -> JobSecurityFactorContribution.Neutral
            },
            summary = "Record ${record.overallLabel}",
        )
        return CoachSecuritySnapshot(
            jobSecurity = JobSecuritySnapshot(
                score = adjusted,
                tier = JobSecurityTier.fromScore(adjusted),
                trend = trend,
                factors = factors,
            ),
            adTrust = adTrust,
        )
    }
}
