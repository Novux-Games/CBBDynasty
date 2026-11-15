package dev.novux.dynasty.cbb.core.qa

import dev.novux.dynasty.cbb.core.coach.staff.SchoolStaffGradeEngine
import dev.novux.dynasty.cbb.core.player.archetype.PlayerArchetypeFlavorEngine
import dev.novux.dynasty.cbb.core.player.card.PlayerCardEditGatePolicy
import dev.novux.dynasty.cbb.core.player.draft.PlayerDraftTierPolicy
import dev.novux.dynasty.cbb.core.portrait.PresetPortraitRendererPolicy
import java.nio.file.Paths
import kotlin.io.path.readText
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MegaCoachBatch32PathTest {
    private val root = generateSequence(Paths.get("").toAbsolutePath()) { it.parent }
        .first { it.resolve("android").resolve("settings.gradle.kts").toFile().exists() }

    @Test
    fun staffGradeChipOnStaffPanel() {
        assertTrue(root.resolve(SchoolStaffGradeChipPath.ENGINE).readText().contains(SchoolStaffGradeEngine.MEGA_ID))
        assertTrue(root.resolve(SchoolStaffGradeChipPath.UI).readText().contains(SchoolStaffGradeEngine.MEGA_ID))
        assertTrue(root.resolve(SchoolStaffGradeChipPath.UI).readText().contains("staff_aggregate_grade_chip"))
    }

    @Test
    fun playerEditGateWiredInShell() {
        val shell = root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/shell/DynastyShellScreen.kt").readText()
        assertTrue(root.resolve("android/core/src/main/kotlin/dev/novux/dynasty/cbb/core/player/card/PlayerCardEditGatePolicy.kt").readText().contains(PlayerCardEditGatePolicy.MEGA_ID))
        assertTrue(shell.contains("allowEditOnPreview"))
        assertTrue(shell.contains("allowEditOnFullCard"))
    }

    @Test
    fun archetypeFlavorAndBadges() {
        val ratings = root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/player/PlayerCardRatingsTab.kt").readText()
        assertTrue(ratings.contains(PlayerArchetypeFlavorEngine.MEGA_ID_RATINGS))
        val sheet = root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/player/PlayerCardSheet.kt").readText()
        assertTrue(sheet.contains(PlayerArchetypeFlavorEngine.MEGA_ID_SHEET))
        assertTrue(root.resolve(PlayerArchetypeBadgePath.CHIP).readText().contains(PlayerArchetypeBadgePath.MEGA_ROSTER))
        assertTrue(root.resolve(ShellRosterThumbnailPath.ROW).readText().contains(ShellRosterThumbnailPath.MEGA_ID))
        assertTrue(root.resolve(ShellRosterThumbnailPath.ROSTER_TAB).readText().contains("ShellRosterPlayerRow"))
    }

    @Test
    fun draftTierLabelsAndPortraitPolicy() {
        val line = root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/player/PlayerDraftProjectionLine.kt").readText()
        assertTrue(line.contains(PlayerDraftTierPolicy.MEGA_ID_LABELS))
        assertTrue(line.contains("player_card_draft_tier_sublabel"))
        val renderer = root.resolve("android/core/src/main/kotlin/dev/novux/dynasty/cbb/core/portrait/PresetPortraitRenderer.kt").readText()
        assertTrue(renderer.contains(PresetPortraitRendererPolicy.MEGA_ID))
        assertTrue(root.resolve("android/core/src/test/kotlin/dev/novux/dynasty/cbb/core/portrait/PresetPortraitRendererTest.kt").readText().contains("signatureHash"))
    }

    @Test
    fun assistantHeadCoachCareerFlag() {
        val mapper = root.resolve("android/database/src/main/kotlin/dev/novux/dynasty/cbb/database/staff/StaffMappers.kt").readText()
        assertTrue(mapper.contains("hasHeadCoachCareer"))
        val assistant = root.resolve("android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/coach/AssistantCoachCardScreen.kt").readText()
        assertTrue(assistant.contains("hasHeadCoachCareer"))
    }

    @Test
    fun editGateBlocksNonUserRoster() {
        assertFalse(PlayerCardEditGatePolicy.allowEditOnFullCard(isOnUserRoster = false))
    }
}