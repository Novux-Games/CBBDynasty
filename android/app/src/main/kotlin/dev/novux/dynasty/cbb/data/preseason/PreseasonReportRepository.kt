package dev.novux.dynasty.cbb.data.preseason

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.novux.dynasty.cbb.core.calendar.SeasonMembershipYear
import dev.novux.dynasty.cbb.core.reports.PreseasonReportGenerator
import dev.novux.dynasty.cbb.core.reports.ReportArticle
import dev.novux.dynasty.cbb.core.reports.ReportArticleType
import dev.novux.dynasty.cbb.data.pack.PackRepository
import dev.novux.dynasty.cbb.database.DynastyDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.preseasonReportStore: DataStore<Preferences> by preferencesDataStore(
    name = "preseason_report",
)

class PreseasonReportRepository(
    private val context: Context,
    private val packRepository: PackRepository,
    private val database: DynastyDatabase,
    private val incomingClassSeedRepository: IncomingClassSeedRepository,
) {
    suspend fun ensureArticle(dynastyId: String, seasonIndex: Int): ReportArticle? {
        val encoded = context.preseasonReportStore.data.first()[articleKey(dynastyId, seasonIndex)]
        if (encoded != null) return decodeArticle(encoded)
        val coach = database.dynastyCoachDao().getByDynastyId(dynastyId) ?: return null
        val team = packRepository.activeSession().index.team(coach.userTeamId) ?: return null
        val conference = packRepository.activeSession().index.conference(team.conferenceId)
        val headline = incomingClassSeedRepository.loadHeadline(dynastyId, seasonIndex)
        val topLine = headline?.headline
            ?: "National polls list several incoming pieces in the Top 60 conversation."
        val article = PreseasonReportGenerator.generate(
            teamDisplayName = team.displayName,
            conferenceDisplayName = conference?.displayName ?: "league",
            programPrestige = team.programPrestige,
            topSixtyOneLiner = topLine,
            seasonYearLabel = SeasonMembershipYear.label(seasonIndex),
        )
        context.preseasonReportStore.edit { prefs ->
            prefs[articleKey(dynastyId, seasonIndex)] = encodeArticle(article)
        }
        return article
    }

    suspend fun markRead(dynastyId: String, seasonIndex: Int) {
        context.preseasonReportStore.edit { prefs ->
            prefs[readKey(dynastyId, seasonIndex)] = true
        }
    }

    suspend fun isRead(dynastyId: String, seasonIndex: Int): Boolean =
        context.preseasonReportStore.data.map { prefs ->
            prefs[readKey(dynastyId, seasonIndex)] == true
        }.first()

    private fun articleKey(dynastyId: String, seasonIndex: Int) =
        stringPreferencesKey("article_${dynastyId}_$seasonIndex")

    private fun readKey(dynastyId: String, seasonIndex: Int) =
        booleanPreferencesKey("read_${dynastyId}_$seasonIndex")

    private fun encodeArticle(article: ReportArticle): String {
        val parts = buildList {
            add(article.type.name)
            add(article.headline.escape())
            add(article.paragraphs.joinToString("%P%") { it.escape() })
            add(article.pullQuote?.escape().orEmpty())
            add(article.scoreLine?.escape().orEmpty())
            add(article.recordLine?.escape().orEmpty())
            add(article.projectedSeasonRecord?.escape().orEmpty())
        }
        return parts.joinToString("|")
    }

    private fun decodeArticle(raw: String): ReportArticle? {
        val p = raw.split("|")
        if (p.size < 7) return null
        val type = runCatching { ReportArticleType.valueOf(p[0]) }.getOrNull() ?: return null
        val paragraphs = p[2].split("%P%").map { it.unescape() }
        if (paragraphs.isEmpty()) return null
        return ReportArticle(
            type = type,
            headline = p[1].unescape(),
            paragraphs = paragraphs,
            pullQuote = p[3].unescape().takeIf { it.isNotEmpty() },
            scoreLine = p[4].unescape().takeIf { it.isNotEmpty() },
            recordLine = p[5].unescape().takeIf { it.isNotEmpty() },
            projectedSeasonRecord = p[6].unescape().takeIf { it.isNotEmpty() },
        )
    }

    private fun String.escape(): String = replace("|", "%7C").replace("%P%", "%25P%25")
    private fun String.unescape(): String = replace("%7C", "|").replace("%25P%25", "%P%")
}
