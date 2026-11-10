package dev.novux.dynasty.cbb.ui.hubs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R

private enum class MarchMayhemTab { Ncba, Nci, Heritage }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarchMayhemHubScreen(onBack: () -> Unit) {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabs = MarchMayhemTab.entries

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text(stringResource(R.string.march_mayhem_hub_title)) })
        OutlinedButton(onClick = onBack, modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(stringResource(R.string.dynasty_shell_back))
        }
        PrimaryTabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, tab ->
                Tab(
                    selected = tabIndex == index,
                    onClick = { tabIndex = index },
                    text = {
                        Text(
                            when (tab) {
                                MarchMayhemTab.Ncba -> stringResource(R.string.march_mayhem_tab_ncba)
                                MarchMayhemTab.Nci -> stringResource(R.string.march_mayhem_tab_nci)
                                MarchMayhemTab.Heritage -> stringResource(R.string.march_mayhem_tab_heritage)
                            },
                        )
                    },
                )
            }
        }
        Text(
            text = stringResource(R.string.march_mayhem_hub_placeholder),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
        )
    }
}
