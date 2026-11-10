package dev.novux.dynasty.cbb.core.reports

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class PreseasonReportGeneratorTest {
    @Test
    fun generatesPreseasonFields() {
        val article = PreseasonReportGenerator.generate(
            teamDisplayName = "Test U",
            conferenceDisplayName = "Big Example",
            programPrestige = 75,
            topSixtyOneLiner = "Top 60 watch: three commitments headline November.",
            seasonYearLabel = "2026-27",
        )
        assertEquals(ReportArticleType.Preseason, article.type)
        assertEquals(4, article.paragraphs.size)
        assertNotNull(article.pullQuote)
        assertEquals("Projected record: 24-8", article.projectedSeasonRecord)
    }
}
