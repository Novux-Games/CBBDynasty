package dev.novux.dynasty.cbb.core.reports

import dev.novux.dynasty.cbb.core.preview.GamePreviewResult
import dev.novux.dynasty.cbb.core.preview.HeadToHeadRecord
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GameReportGeneratorTest {
    private val preview = GamePreviewResult(
        userWinPercent = 61,
        predictedUserScore = 78,
        predictedOpponentScore = 71,
        headToHead = HeadToHeadRecord(0, 0, emptyList()),
    )

    @Test
    fun pregameArticleUsesPreviewNumbers() {
        val article = PregameReportGenerator.generate(
            userTeamName = "State",
            opponentName = "Tech",
            venuePhrase = "home court",
            preview = preview,
        )
        assertEquals(ReportArticleType.Pregame, article.type)
        assertEquals(3, article.paragraphs.size)
        assertTrue(article.scoreLine!!.contains("78"))
        assertTrue(article.recordLine!!.contains("61"))
    }

    @Test
    fun postgameArticleReflectsFinalScore() {
        val win = PostgameReportGenerator.generate("State", "Tech", 80, 74)
        assertEquals(ReportArticleType.Postgame, win.type)
        assertTrue(win.headline.contains("tops"))
        val loss = PostgameReportGenerator.generate("State", "Tech", 68, 75)
        assertTrue(loss.headline.contains("edges"))
    }
}
