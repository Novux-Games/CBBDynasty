package dev.novux.dynasty.cbb.ui.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.preview.GamePreviewResult
import dev.novux.dynasty.cbb.core.preview.GamePreviewVenue
import dev.novux.dynasty.cbb.core.reports.PregameReportGenerator
import dev.novux.dynasty.cbb.ui.reports.ReportArticleView

@Composable
fun GamePreviewScreen(
    matchupLabel: String,
    userTeamName: String,
    opponentName: String,
    venuePhrase: String,
    venue: GamePreviewVenue,
    preview: GamePreviewResult,
    onOpenStrategy: () -> Unit,
    onPlayGame: () -> Unit = {},
    onBack: () -> Unit,
) {
    val pregameArticle = remember(userTeamName, opponentName, venuePhrase, preview) {
        PregameReportGenerator.generate(userTeamName, opponentName, venuePhrase, preview)
    }
    val venueLabel = when (venue) {
        GamePreviewVenue.Home -> stringResource(R.string.dynasty_shell_home)
        GamePreviewVenue.Away -> stringResource(R.string.dynasty_shell_away)
        GamePreviewVenue.Neutral -> stringResource(R.string.game_preview_neutral)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.game_preview_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(text = matchupLabel, style = MaterialTheme.typography.titleMedium)
        Text(
            text = stringResource(R.string.game_preview_venue, venueLabel),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(R.string.game_report_pregame_heading),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        ReportArticleView(article = pregameArticle)
        Text(
            text = stringResource(R.string.game_preview_win_pct, preview.userWinPercent),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = stringResource(
                R.string.game_preview_predicted_score,
                preview.predictedUserScore,
                preview.predictedOpponentScore,
            ),
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = stringResource(R.string.game_preview_h2h_heading),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        if (preview.headToHead.meetings.isEmpty()) {
            Text(
                text = stringResource(R.string.game_preview_h2h_empty),
                style = MaterialTheme.typography.bodyMedium,
            )
        } else {
            Text(
                text = stringResource(
                    R.string.game_preview_h2h_record,
                    preview.headToHead.userWins,
                    preview.headToHead.userLosses,
                ),
                style = MaterialTheme.typography.bodyLarge,
            )
            preview.headToHead.meetings.take(5).forEach { meeting ->
                Text(
                    text = stringResource(
                        R.string.game_preview_h2h_meeting,
                        meeting.seasonLabel,
                        meeting.userScore,
                        meeting.opponentScore,
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        OutlinedButton(onClick = onPlayGame, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.game_preview_play_game))
        }
        OutlinedButton(onClick = onOpenStrategy, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.game_preview_open_strategy))
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.settings_back))
        }
    }
}
