package dev.novux.dynasty.cbb.core.recruiting

import dev.novux.dynasty.cbb.core.signingday.NationalRecruitingRankings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class IncomingClassSeederTest {
    @Test
    fun hsPoolSizeDrawsUniformBandPerSeason() {
        val count = IncomingClassSeeder.resolveHsPoolSize(enrollingSeasonIndex = 3, seedKey = 99)
        assertTrue(count in IncomingClassSeeder.HS_POOL_MIN..IncomingClassSeeder.HS_POOL_MAX)
        assertEquals(count, IncomingClassSeeder.resolveHsPoolSize(3, 99))
        val otherSeason = IncomingClassSeeder.resolveHsPoolSize(enrollingSeasonIndex = 4, seedKey = 99)
        assertTrue(otherSeason in IncomingClassSeeder.HS_POOL_MIN..IncomingClassSeeder.HS_POOL_MAX)
    }

    @Test
    fun defaultSeedUsesHsBandPlusJuco() {
        val pool = IncomingClassSeeder.seed(enrollingSeasonIndex = 5, seedKey = 7_777)
        val hsCount = pool.count { it.poolKind == RecruitPoolKind.HighSchool }
        assertTrue(hsCount in IncomingClassSeeder.HS_POOL_MIN..IncomingClassSeeder.HS_POOL_MAX)
        assertEquals(IncomingClassSeeder.JUCO_POOL_SIZE, pool.count { it.poolKind == RecruitPoolKind.Juco })
    }

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
