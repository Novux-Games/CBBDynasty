package dev.novux.dynasty.cbb.ui.dynasty

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.novux.dynasty.cbb.R

data class DynastyShellMenuAction(
    val labelRes: Int,
    val onClick: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DynastyShellChrome(
    schoolDisplayName: String,
    phaseWeekLabel: String,
    selectedTab: DynastyShellTab,
    onTabSelected: (DynastyShellTab) -> Unit,
    menuActions: List<DynastyShellMenuAction>,
    modifier: Modifier = Modifier,
    headerSubtitle: String? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Filled.Menu, contentDescription = stringResource(R.string.shell_menu_open))
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                        ) {
                            menuActions.forEach { action ->
                                DropdownMenuItem(
                                    text = { Text(stringResource(action.labelRes)) },
                                    onClick = {
                                        menuExpanded = false
                                        action.onClick()
                                    },
                                )
                            }
                        }
                    }
                },
                title = {
                    Column {
                        Text(
                            text = stringResource(R.string.shell_dynasty_school, schoolDisplayName),
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                        )
                        Text(
                            text = phaseWeekLabel,
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                        )
                        headerSubtitle?.let { subtitle ->
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                            )
                        }
                    }
                },
            )
        },
        bottomBar = {
            NavigationBar(modifier = Modifier.fillMaxWidth()) {
                NavigationBarItem(
                    selected = selectedTab == DynastyShellTab.Roster,
                    onClick = { onTabSelected(DynastyShellTab.Roster) },
                    icon = { Icon(Icons.Filled.People, null) },
                    label = { Text(stringResource(R.string.shell_tab_roster)) },
                )
                NavigationBarItem(
                    selected = selectedTab == DynastyShellTab.Calendar,
                    onClick = { onTabSelected(DynastyShellTab.Calendar) },
                    icon = { Icon(Icons.Filled.CalendarMonth, null) },
                    label = { Text(stringResource(R.string.shell_tab_calendar)) },
                )
                NavigationBarItem(
                    selected = selectedTab == DynastyShellTab.Dashboard,
                    onClick = { onTabSelected(DynastyShellTab.Dashboard) },
                    icon = { Icon(Icons.Filled.Home, null) },
                    label = { Text(stringResource(R.string.shell_tab_dashboard)) },
                )
                NavigationBarItem(
                    selected = selectedTab == DynastyShellTab.News,
                    onClick = { onTabSelected(DynastyShellTab.News) },
                    icon = { Icon(Icons.Filled.Article, null) },
                    label = { Text(stringResource(R.string.shell_tab_news)) },
                )
            }
        },
        content = content,
    )
}
