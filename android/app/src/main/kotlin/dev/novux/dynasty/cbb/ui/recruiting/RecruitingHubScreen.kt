package dev.novux.dynasty.cbb.ui.recruiting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.recruiting.RecruitingHubDefaults
import dev.novux.dynasty.cbb.core.recruiting.RecruitingHubTab

@Composable
fun RecruitingHubScreen(
    onBack: () -> Unit,
) {
    var tabIndex by remember { mutableIntStateOf(RecruitingHubDefaults.tabs.indexOf(RecruitingHubDefaults.defaultTab)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = stringResource(R.string.recruiting_hub_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(R.string.recruiting_hub_cycle_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PrimaryTabRow(selectedTabIndex = tabIndex) {
            Tab(
                selected = tabIndex == 0,
                onClick = { tabIndex = 0 },
                text = { Text(stringResource(R.string.recruiting_tab_recommended)) },
            )
            Tab(
                selected = tabIndex == 1,
                onClick = { tabIndex = 1 },
                text = { Text(stringResource(R.string.recruiting_tab_hs_juco)) },
            )
            Tab(
                selected = tabIndex == 2,
                onClick = { tabIndex = 2 },
                text = { Text(stringResource(R.string.recruiting_tab_portal)) },
            )
        }
        val body = when (RecruitingHubDefaults.tabs[tabIndex]) {
            RecruitingHubTab.Recommended -> stringResource(R.string.recruiting_placeholder_recommended)
            RecruitingHubTab.HsJuco -> stringResource(R.string.recruiting_placeholder_hs_juco)
            RecruitingHubTab.TransferPortal -> stringResource(R.string.recruiting_placeholder_portal)
        }
        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.settings_back))
        }
    }
}
