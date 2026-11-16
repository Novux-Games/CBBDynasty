package dev.novux.dynasty.cbb.core.qa

import dev.novux.dynasty.cbb.core.coach.MegaCoachBatch36TracePolicy
import dev.novux.dynasty.cbb.core.coach.skill.CoachArchetypeStartingSkillBiasPolicy
import dev.novux.dynasty.cbb.core.gdd.MegaGddBatch36TracePolicy
import dev.novux.dynasty.cbb.core.recruiting.MegaRecruitBatch36TracePolicy
import dev.novux.dynasty.cbb.core.save.NewGameSignRecapPolicy
import java.nio.file.Paths
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MegaBatch36PathTest {
  private val root = generateSequence(Paths.get("").toAbsolutePath()) { it.parent }
        .first { it.resolve("android").resolve("settings.gradle.kts").toFile().exists() }

    @Test
    fun batch36TracePoliciesCoverFinalQueue() {
        assertEquals("MEGA-COACH-026", MegaCoachBatch36TracePolicy.MEGA_026)
        assertEquals("MEGA-GDD-069", MegaGddBatch36TracePolicy.MEGA_069)
        assertEquals("MEGA-RECR-041", MegaRecruitBatch36TracePolicy.MEGA_041)
    }

    @Test
    fun signArchetypeBiasAndRecapWired() {
        assertEquals("MEGA-COACH-026", CoachArchetypeStartingSkillBiasPolicy.MEGA_ID)
        assertEquals("MEGA-COACH-027", NewGameSignRecapPolicy.MEGA_ID)
        val signUi = root.resolve(
            "android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/newgame/SignContractStepContent.kt",
        ).readText()
        assertTrue(signUi.contains("NewGameSignRecapPolicy"))
        val save = root.resolve(
            "android/app/src/main/kotlin/dev/novux/dynasty/cbb/data/save/SaveSlotRepository.kt",
        ).readText()
        assertTrue(save.contains("seedArchetypeBiasAtSign"))
    }

    @Test
    fun shellAndContractSurfacesTraceBatch36() {
        val shell = root.resolve(
            "android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/shell/DynastyShellScreen.kt",
        ).readText()
        assertTrue(shell.contains("InSeasonRosterPlayerCardPolicy.MEGA_ID"))
        val contract = root.resolve(
            "android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/coach/CoachCardContractTabContent.kt",
        ).readText()
        assertTrue(contract.contains("ContractTabJobSecurityPolicy.MEGA_ID"))
    }
}