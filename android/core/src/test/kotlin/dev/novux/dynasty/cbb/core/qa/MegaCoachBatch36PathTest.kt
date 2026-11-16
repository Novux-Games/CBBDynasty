package dev.novux.dynasty.cbb.core.qa

import dev.novux.dynasty.cbb.core.balance.BalanceCoachJobSecurityWeightsPolicy
import dev.novux.dynasty.cbb.core.coach.MegaCoachBatch36TracePolicy
import dev.novux.dynasty.cbb.core.coach.skill.CoachArchetypeStartingSkillBiasPolicy
import dev.novux.dynasty.cbb.core.portrait.PortraitGpuBudgetDocPolicy
import dev.novux.dynasty.cbb.core.roster.RosterRowDensityPolicy
import dev.novux.dynasty.cbb.core.save.SignContractRecapPolicy
import java.nio.file.Paths
import kotlin.io.path.readText
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MegaCoachBatch36PathTest {
    private val root = generateSequence(Paths.get("").toAbsolutePath()) { it.parent }
        .first { it.resolve("android").resolve("settings.gradle.kts").toFile().exists() }

    @Test
    fun batch36TraceConstants() {
        assertEquals(MegaCoachBatch36TracePolicy.MEGA_026, CoachArchetypeStartingSkillBiasPolicy.MEGA_ID)
        assertEquals(MegaCoachBatch36TracePolicy.MEGA_027, SignContractRecapPolicy.MEGA_ID)
        assertEquals(MegaCoachBatch36TracePolicy.MEGA_048, BalanceCoachJobSecurityWeightsPolicy.MEGA_ID)
    }

    @Test
    fun signArchetypeWiredAtSaveSlotSign() {
        val repo = root.resolve(
            "android/app/src/main/kotlin/dev/novux/dynasty/cbb/data/save/SaveSlotRepository.kt",
        ).readText()
        assertTrue(repo.contains("seedArchetypeBiasAtSign"))
        assertTrue(repo.contains("draft.archetype"))
    }

    @Test
    fun signRecapCardInWizard() {
        val sign = root.resolve(
            "android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/newgame/SignContractStepContent.kt",
        ).readText()
        assertTrue(sign.contains("new_sign_recap_title"))
        assertTrue(sign.contains(SignContractRecapPolicy.MEGA_ID) || sign.contains("MEGA-COACH-027"))
    }

    @Test
    fun contractTabJobSecurityFactors() {
        val contract = root.resolve(
            "android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/coach/CoachCardContractTabContent.kt",
        ).readText()
        assertTrue(contract.contains("showFactorDetails = true"))
    }

    @Test
    fun rosterDensityAndPortraitDoc() {
        assertTrue(root.resolve("docs/PORTRAIT_GPU_BUDGET.md").toFile().exists())
        assertEquals(PortraitGpuBudgetDocPolicy.DOC_PATH, "docs/PORTRAIT_GPU_BUDGET.md")
        val shellRow = root.resolve(ShellRosterThumbnailPath.ROW).readText()
        assertTrue(shellRow.contains(RosterRowDensityPolicy.MEGA_COMPACT))
        assertTrue(
            root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/portrait/PortraitInitialsFallback.kt")
                .toFile().exists(),
        )
    }

    @Test
    fun coachUiScaffoldPathTestsExist() {
        assertTrue(
            root.resolve("android/core/src/test/kotlin/dev/novux/dynasty/cbb/core/qa/MegaCoachBatch36PathTest.kt")
                .toFile().exists(),
        )
        assertEquals(MegaCoachBatch36TracePolicy.MEGA_197, "MEGA-COACH-197")
        assertEquals(MegaCoachBatch36TracePolicy.MEGA_198, "MEGA-COACH-198")
    }
}