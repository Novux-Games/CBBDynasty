package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.signingday.NationalRecruitingRankings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class IncomingClassSeederTest {
    @Test
    fun poolsIncludeHsAndJucoWithTop60() {
        val pool = IncomingClassSeeder.seed(
            enrollingSeasonIndex = 2,
            hsCount = 200,
            jucoCount = 80,
            seedKey = 42,
        )
        assertEquals(280, pool.size)
        assertTrue(pool.any { it.poolKind == RecruitPoolKind.HighSchool })
        assertTrue(pool.any { it.poolKind == RecruitPoolKind.Juco })
        val top = NationalRecruitingRankings.top60FromPool(pool)
        assertEquals(60, top.size)
        assertEquals(1, top.first().rank)
        pool.forEach { prospect ->
            assertTrue(prospect.topPriorities.isNotEmpty())
            assertTrue(prospect.topPriorities.size <= 2)
        }
    }
}
