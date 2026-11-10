package dev.novux.dynasty.cbb.ui.hubs

import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.calendar.CalendarPosition
import dev.novux.dynasty.cbb.core.hubs.DynastyHubMenuPolicy
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellMenuAction

data class DynastyHubMenuCallbacks(
    val onOpenSchool: () -> Unit,
    val onOpenRecruiting: () -> Unit,
    val onOpenStrategy: () -> Unit,
    val onOpenStatistics: () -> Unit,
    val onOpenRankings: () -> Unit,
    val onOpenNewsHub: () -> Unit,
    val onOpenHistory: () -> Unit,
    val onOpenAwards: () -> Unit,
    val onOpenMarchMayhem: () -> Unit,
    val onOpenConferenceTournament: () -> Unit,
    val onExitHome: () -> Unit,
)

fun buildDynastyHubMenu(
    position: CalendarPosition,
    callbacks: DynastyHubMenuCallbacks,
    leadingExtras: List<DynastyShellMenuAction> = emptyList(),
    trailingExtras: List<DynastyShellMenuAction> = emptyList(),
): List<DynastyShellMenuAction> {
    val core = buildList {
        add(DynastyShellMenuAction(R.string.school_hub_open, callbacks.onOpenSchool))
        add(DynastyShellMenuAction(R.string.shell_menu_recruiting_hub, callbacks.onOpenRecruiting))
        add(DynastyShellMenuAction(R.string.dynasty_shell_open_strategy, callbacks.onOpenStrategy))
        add(DynastyShellMenuAction(R.string.shell_menu_statistics_hub, callbacks.onOpenStatistics))
        add(DynastyShellMenuAction(R.string.shell_menu_rankings_hub, callbacks.onOpenRankings))
        add(DynastyShellMenuAction(R.string.shell_menu_news_hub, callbacks.onOpenNewsHub))
        add(DynastyShellMenuAction(R.string.shell_menu_history_hub, callbacks.onOpenHistory))
        add(DynastyShellMenuAction(R.string.shell_menu_awards_hub, callbacks.onOpenAwards))
        if (DynastyHubMenuPolicy.conferenceTournamentMenuVisible(position)) {
            add(DynastyShellMenuAction(R.string.shell_menu_conf_tourney, callbacks.onOpenConferenceTournament))
        }
        if (DynastyHubMenuPolicy.marchMayhemMenuVisible(position)) {
            add(DynastyShellMenuAction(R.string.shell_menu_march_mayhem, callbacks.onOpenMarchMayhem))
        }
        add(DynastyShellMenuAction(R.string.shell_menu_exit_home, callbacks.onExitHome))
    }
    return leadingExtras + core + trailingExtras
}
