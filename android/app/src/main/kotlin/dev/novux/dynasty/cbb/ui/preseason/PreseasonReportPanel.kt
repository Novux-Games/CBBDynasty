package dev.novux.dynasty.cbb.ui.preseason

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.preseason.PreseasonTaskId
import dev.novux.dynasty.cbb.core.reports.ReportArticle
import dev.novux.dynasty.cbb.data.preseason.PreseasonProgressRepository
import dev.novux.dynasty.cbb.data.preseason.PreseasonReportRepository
import dev.novux.dynasty.cbb.ui.reports.ReportArticleView
import kotlinx.coroutines.launch

@Composable
fun PreseasonReportPanel(
    dynastyId: String,
    seasonIndex: Int,
    reportRepository: PreseasonReportRepository,
    progressRepository: PreseasonProgressRepository,
    onMarkedRead: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var article by remember { mutableStateOf<ReportArticle?>(null) }
    var read by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(dynastyId, seasonIndex) {
        article = reportRepository.ensureArticle(dynastyId, seasonIndex)
        read = reportRepository.isRead(dynastyId, seasonIndex)
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = stringResource(R.string.preseason_report_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        article?.let { ReportArticleView(article = it) }
        OutlinedButton(
            onClick = {
                scope.launch {
                    reportRepository.markRead(dynastyId, seasonIndex)
                    read = true
                    progressRepository.completeTask(dynastyId, seasonIndex, PreseasonTaskId.PreseasonReport)
                    onMarkedRead()
                }
            },
            enabled = article != null && !read,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                if (read) {
                    stringResource(R.string.preseason_report_done)
                } else {
                    stringResource(R.string.preseason_report_mark_read)
                },
            )
        }
    }
}
