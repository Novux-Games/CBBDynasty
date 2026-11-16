package dev.novux.dynasty.cbb.core.qa

import java.nio.file.Paths
import kotlin.io.path.readText
import org.junit.Assert.assertTrue
import org.junit.Test

class LeagueWorldPackLeftoversPathTest {
    private fun root() =
        generateSequence(Paths.get("").toAbsolutePath()) { it.parent }
            .first { it.resolve("android").resolve("settings.gradle.kts").toFile().exists() }

    @Test
    fun jobCenterRepositoryUsesPackTeamListPolicy() {
        val text = root().resolve(
            "android/app/src/main/kotlin/dev/novux/dynasty/cbb/data/coach/CoachEmploymentRepository.kt",
        ).readText()
        assertTrue(text.contains("JobCenterPackTeamListPolicy"))
        assertTrue(text.contains("allTeamDisplays()"))
    }

    @Test
    fun leagueScheduleRepositoryUsesPackConferenceOverlays() {
        val text = root().resolve(
            "android/app/src/main/kotlin/dev/novux/dynasty/cbb/data/league/LeagueScheduleRepository.kt",
        ).readText()
        assertTrue(text.contains("allConferenceDisplays()"))
        assertTrue(text.contains("LeagueScheduleGenerator.generate"))
    }
}