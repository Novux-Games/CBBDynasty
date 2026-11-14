package dev.novux.dynasty.cbb.core.qa

import java.nio.file.Paths
import kotlin.io.path.readText
import org.junit.Assert.assertTrue
import org.junit.Test

/** [MEGA-FRAME-051] */
class ShellNewsHeadlineTapPathTest {
    @Test
    fun shellNewsTab_headlinesOpenDetailSheet() {
        val root = repoRoot()
        val tab = root.resolve(
            "android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/dynasty/DynastyShellTabContent.kt",
        ).readText()
        assertTrue(tab.contains("NewsHeadlineArticleBuilder"))
        assertTrue(tab.contains("clickable { selectedHeadline = headline }"))
        assertTrue(tab.contains("ModalBottomSheet"))
        assertTrue(tab.contains("ReportArticleView"))
        assertTrue(
            root.resolve(
                "android/core/src/main/kotlin/dev/novux/dynasty/cbb/core/news/NewsHeadlineArticleBuilder.kt",
            ).toFile().exists(),
        )
    }

    private fun repoRoot() =
        generateSequence(Paths.get("").toAbsolutePath()) { it.parent }
            .first { it.resolve("android").resolve("settings.gradle.kts").toFile().exists() }
}
