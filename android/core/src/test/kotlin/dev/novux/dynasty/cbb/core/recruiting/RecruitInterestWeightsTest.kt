package dev.novux.dynasty.cbb.core.recruiting

import org.junit.Assert.assertTrue
import org.junit.Test

class RecruitInterestWeightsTest {
    @Test
    fun priorities_boostMappedFactors() {
        val base = RecruitInterestWeights.effectiveWeights(emptyList(), stars = 3)
        val boosted = RecruitInterestWeights.effectiveWeights(
            listOf(RecruitPriority.StayHome, RecruitPriority.FollowMoney),
            stars = 3,
        )
        assertTrue(boosted.getValue(RecruitingInterestFactor.Proximity) > base.getValue(RecruitingInterestFactor.Proximity))
        assertTrue(boosted.getValue(RecruitingInterestFactor.Nil) > base.getValue(RecruitingInterestFactor.Nil))
        assertTrue(boosted.values.sum() - 1.0 < 0.001)
    }
}
