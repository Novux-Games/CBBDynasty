package dev.novux.dynasty.cbb.ui.offseason

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.calendar.CalendarPhase
import dev.novux.dynasty.cbb.core.recruiting.RecruitingCyclePolicy
import dev.novux.dynasty.cbb.core.calendar.CalendarWeekLabel
import dev.novux.dynasty.cbb.core.calendar.OffseasonWeekHeading
import dev.novux.dynasty.cbb.core.calendar.SeasonMembershipYear
import dev.novux.dynasty.cbb.core.dashboard.OffseasonDashboardContext
import dev.novux.dynasty.cbb.core.dashboard.DashboardAdvanceGate
import dev.novux.dynasty.cbb.core.dashboard.DashboardDeepLink
import dev.novux.dynasty.cbb.core.dashboard.OffseasonOptionalDashboardTasks
import dev.novux.dynasty.cbb.core.dashboard.DashboardTaskId
import dev.novux.dynasty.cbb.core.dashboard.SigningDayRequiredDashboardTasks
import dev.novux.dynasty.cbb.core.offseason.weekeight.OffseasonRosterLockConstants
import dev.novux.dynasty.cbb.core.offseason.weekeight.RosterLockPolicy
import dev.novux.dynasty.cbb.core.offseason.weekseven.SummerWorkoutConstants
import dev.novux.dynasty.cbb.core.offseason.weeknine.OffseasonWeekNineConstants
import dev.novux.dynasty.cbb.core.offseason.weekten.OffseasonWeekTenConstants
import dev.novux.dynasty.cbb.data.offseason.OffseasonWeekEightRepository
import dev.novux.dynasty.cbb.data.offseason.OffseasonWeekNineRepository
import dev.novux.dynasty.cbb.data.offseason.OffseasonWeekTenRepository
import dev.novux.dynasty.cbb.core.signingday.SigningDayConstants
import dev.novux.dynasty.cbb.data.offseason.OffseasonSummerWorkoutRepository
import dev.novux.dynasty.cbb.core.signingday.SigningDayProgress
import dev.novux.dynasty.cbb.data.signingday.SigningDayRepository
import dev.novux.dynasty.cbb.ui.dashboard.DashboardRequiredTasksPanel
import dev.novux.dynasty.cbb.core.portal.PortalConstants
import dev.novux.dynasty.cbb.core.recruiting.RecruitingCycleLedger
import dev.novux.dynasty.cbb.core.recruiting.RecruitingHubTab
import dev.novux.dynasty.cbb.core.save.DynastyLoadResult
import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import dev.novux.dynasty.cbb.data.offseason.OffseasonWeekTwoRepository
import dev.novux.dynasty.cbb.data.portal.PortalCycleRepository
import dev.novux.dynasty.cbb.data.recruiting.RecruitingEconomyRepository
import dev.novux.dynasty.cbb.data.roster.DynastyRosterRepository
import dev.novux.dynasty.cbb.data.save.ActiveDynastySession
import dev.novux.dynasty.cbb.data.save.SaveSlotRepository
import dev.novux.dynasty.cbb.ui.dashboard.DashboardOptionalTasksPanel
import dev.novux.dynasty.cbb.core.player.DynastyPlayer
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellChrome
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellTab
import dev.novux.dynasty.cbb.ui.hubs.DynastyHubMenuCallbacks
import dev.novux.dynasty.cbb.ui.hubs.buildDynastyHubMenu
import dev.novux.dynasty.cbb.ui.hubs.notifyRecruitingCycleClosed
import dev.novux.dynasty.cbb.data.calendar.UserCalendarRepository
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellCalendarTab
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellNewsTabPlaceholder
import dev.novux.dynasty.cbb.ui.dynasty.DynastyShellRosterTab
import dev.novux.dynasty.cbb.ui.offseason.leaving.DraftResultsWeekTwoPanel
import dev.novux.dynasty.cbb.ui.offseason.leaving.PlayersLeavingPanel
import dev.novux.dynasty.cbb.core.advance.SimWeekConfirmation
import dev.novux.dynasty.cbb.ui.advance.SimWeekAdvanceDialog
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import dev.novux.dynasty.cbb.requireDynastyApplication
import dev.novux.dynasty.cbb.ui.player.PlayerCardPreviewSheet
import dev.novux.dynasty.cbb.ui.player.PlayerCardScreen
import kotlinx.coroutines.launch

private const val SCHOLARSHIP_LIMIT = 15

/** Offseason shell — week header and advance ([planning/offseason.md]). */
@Composable
fun OffseasonScreen(
    initial: LoadedDynasty,
    saveSlotRepository: SaveSlotRepository,
    rosterRepository: DynastyRosterRepository,
    recruitingEconomyRepository: RecruitingEconomyRepository,
    portalCycleRepository: PortalCycleRepository,
    signingDayRepository: SigningDayRepository,
    offseasonWeekTwoRepository: OffseasonWeekTwoRepository,
    summerWorkoutRepository: OffseasonSummerWorkoutRepository,
    weekEightRepository: OffseasonWeekEightRepository,
    weekNineRepository: OffseasonWeekNineRepository,
    weekTenRepository: OffseasonWeekTenRepository,
    hubMenuCallbacks: DynastyHubMenuCallbacks,
    onOpenRecruiting: (RecruitingHubTab) -> Unit = {},
    onOpenSigningDay: (DashboardDeepLink) -> Unit = {},
    userCalendarRepository: UserCalendarRepository,
) {
    var loaded by remember(initial) { mutableStateOf(initial) }
    var ledger by remember { mutableStateOf<RecruitingCycleLedger?>(null) }
    var portalTop40Locked by remember { mutableStateOf(false) }
    var openScholarshipSlots by remember { mutableStateOf(0) }
    var rosterPlayerCount by remember { mutableStateOf(0) }
    var dismissedTasks by remember { mutableStateOf(setOf<DashboardTaskId>()) }
    var pendingAdvanceConfirmation by remember { mutableStateOf<SimWeekConfirmation?>(null) }
    var weekAdvanceInProgress by remember { mutableStateOf(false) }
    var dialogAdvanceInProgress by remember { mutableStateOf(false) }
    var signingProgress by remember { mutableStateOf<SigningDayProgress?>(null) }
    var conferenceOffersResolved by remember { mutableStateOf(true) }
    var selectedTab by remember { mutableStateOf(DynastyShellTab.Dashboard) }
    var fullRoster by remember { mutableStateOf<List<DynastyPlayer>>(emptyList()) }
    var cardPlayer by remember { mutableStateOf<DynastyPlayer?>(null) }
    var fullScreenPlayer by remember { mutableStateOf<DynastyPlayer?>(null) }
    val context = LocalContext.current
    val playerStatsRepository = context.requireDynastyApplication().playerStatsRepository
    val scope = rememberCoroutineScope()

    LaunchedEffect(loaded.dynastyId, loaded.weekCheckpoint) {
        signingProgress = signingDayRepository.loadProgress(loaded.dynastyId, loaded.metadata.seasonIndex)
        ledger = recruitingEconomyRepository.loadOrOpenCycle(loaded)
        val portal = portalCycleRepository.loadOrSimulate(loaded.dynastyId, loaded.metadata.seasonIndex)
        portalTop40Locked = portal.top40Locked
        val roster = rosterRepository.listPlayers(loaded.dynastyId)
        fullRoster = rosterRepository.listDynastyPlayers(loaded.dynastyId)
        rosterPlayerCount = roster.size
        openScholarshipSlots = (SCHOLARSHIP_LIMIT - roster.size).coerceAtLeast(0)
        if (loaded.weekCheckpoint.activePosition.phase == CalendarPhase.Offseason &&
            loaded.weekCheckpoint.activePosition.weekIndex == OffseasonWeekTenConstants.OFFSEASON_WEEK_INDEX
        ) {
            conferenceOffersResolved = weekTenRepository.canAdvance(loaded.dynastyId, loaded.metadata.seasonIndex)
        }
    }
    val position = loaded.weekCheckpoint.activePosition
    val inOffseason = position.phase == CalendarPhase.Offseason
    val weekHeader = CalendarWeekLabel.display(position)
    val themeLine = if (inOffseason) {
        OffseasonWeekHeading.themeLine(position.weekIndex)
    } else {
        null
    }

    val schoolName = loaded.summary.schoolDisplayName.orEmpty()
    val offseasonWeekForCycle = position.weekIndex.takeIf { inOffseason }
    val recruitingCycleClosed = RecruitingCyclePolicy.recruitingMenuTreatsCycleClosed(
        position.phase,
        offseasonWeekForCycle,
        ledger,
    )
    val menu = buildDynastyHubMenu(
        position = position,
        callbacks = hubMenuCallbacks,
        recruitingCycleClosed = recruitingCycleClosed,
        onRecruitingCycleClosed = { notifyRecruitingCycleClosed(context) },
    )

    PlayerCardPreviewSheet(
        player = cardPlayer,
        onDismiss = { cardPlayer = null },
        onViewMore = { player ->
            cardPlayer = null
            fullScreenPlayer = player
        },
    )

    fullScreenPlayer?.let { player ->
        PlayerCardScreen(
            player = player,
            schoolDisplayName = schoolName,
            seasonRecordLabel = "0-0",
            seasonIndex = loaded.metadata.seasonIndex,
            playerStatsRepository = playerStatsRepository,
            allowEdit = false,
            onBack = { fullScreenPlayer = null },
        )
        return
    }

    DynastyShellChrome(
        schoolDisplayName = schoolName,
        phaseWeekLabel = weekHeader,
        selectedTab = selectedTab,
        onTabSelected = { selectedTab = it },
        menuActions = menu,
    ) { padding ->
        when (selectedTab) {
            DynastyShellTab.Roster -> DynastyShellRosterTab(
                players = fullRoster,
                contentPadding = padding,
                onPlayerClick = { cardPlayer = it },
            )
            DynastyShellTab.Calendar -> DynastyShellCalendarTab(
                dynastyId = loaded.dynastyId,
                seasonIndex = loaded.metadata.seasonIndex,
                activePosition = position,
                calendarRepository = userCalendarRepository,
                contentPadding = padding,
            )
            DynastyShellTab.News -> DynastyShellNewsTabPlaceholder(padding)
            DynastyShellTab.Dashboard -> Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 28.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
        Text(
            text = stringResource(
                R.string.offseason_shell_season,
                SeasonMembershipYear.label(loaded.metadata.seasonIndex),
            ),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        themeLine?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
        if (inOffseason && position.weekIndex == 1) {
            PlayersLeavingPanel(
                dynastyId = loaded.dynastyId,
                rosterRepository = rosterRepository,
                modifier = Modifier.weight(1f),
            )
        } else if (inOffseason && position.weekIndex == 2) {
            DraftResultsWeekTwoPanel(
                dynastyId = loaded.dynastyId,
                seasonIndex = loaded.metadata.seasonIndex,
                weekTwoRepository = offseasonWeekTwoRepository,
                modifier = Modifier.weight(1f),
            )
        } else if (inOffseason && position.weekIndex == OffseasonRosterLockConstants.OFFSEASON_WEEK_INDEX) {
            RosterCutsWeekEightPanel(
                dynastyId = loaded.dynastyId,
                seasonIndex = loaded.metadata.seasonIndex,
                rosterRepository = rosterRepository,
                weekEightRepository = weekEightRepository,
                onRosterChanged = {
                    scope.launch {
                        val roster = rosterRepository.listPlayers(loaded.dynastyId)
                        rosterPlayerCount = roster.size
                        openScholarshipSlots = (SCHOLARSHIP_LIMIT - roster.size).coerceAtLeast(0)
                    }
                },
                modifier = Modifier.weight(1f),
            )
        } else if (inOffseason && position.weekIndex == OffseasonWeekNineConstants.OFFSEASON_WEEK_INDEX) {
            FundraisingWeekNinePanel(
                dynastyId = loaded.dynastyId,
                seasonIndex = loaded.metadata.seasonIndex,
                weekNineRepository = weekNineRepository,
                modifier = Modifier.weight(1f),
            )
        } else if (inOffseason && position.weekIndex == OffseasonWeekTenConstants.OFFSEASON_WEEK_INDEX) {
            ConferenceOffersWeekTenPanel(
                dynastyId = loaded.dynastyId,
                seasonIndex = loaded.metadata.seasonIndex,
                weekTenRepository = weekTenRepository,
                onDecisionsChanged = {
                    scope.launch {
                        conferenceOffersResolved = weekTenRepository.canAdvance(
                            loaded.dynastyId,
                            loaded.metadata.seasonIndex,
                        )
                    }
                },
                modifier = Modifier.weight(1f),
            )
        } else if (inOffseason && position.weekIndex == SummerWorkoutConstants.OFFSEASON_WEEK_INDEX) {
            SummerWorkoutReportPanel(
                dynastyId = loaded.dynastyId,
                seasonIndex = loaded.metadata.seasonIndex,
                summerWorkoutRepository = summerWorkoutRepository,
                modifier = Modifier.weight(1f),
            )
        } else if (inOffseason && position.weekIndex == SigningDayConstants.OFFSEASON_WEEK_INDEX) {
            Text(
                text = stringResource(R.string.offseason_signing_day_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
        } else {
            Text(
                text = stringResource(R.string.offseason_shell_placeholder),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
        }

        if (inOffseason && position.weekIndex == SigningDayConstants.OFFSEASON_WEEK_INDEX) {
            signingProgress?.let { progress ->
                DashboardRequiredTasksPanel(
                    tasks = SigningDayRequiredDashboardTasks.build(progress),
                    onOpenSigningDay = onOpenSigningDay,
                )
            }
        }

        val offseasonWeek = position.weekIndex.takeIf { inOffseason }
        val ledgerCycleClosed = ledger?.cycleClosed == true
        if (RecruitingCyclePolicy.hubNavigable(position.phase, offseasonWeek, ledgerCycleClosed)) {
            OutlinedButton(
                onClick = { onOpenRecruiting(RecruitingHubTab.Recommended) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.recruiting_open_hub))
            }
        } else if (
            RecruitingCyclePolicy.hubAccessible(position.phase, offseasonWeek) &&
            recruitingCycleClosed
        ) {
            Text(
                text = stringResource(R.string.recruiting_cycle_closed_body),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        if (inOffseason && position.weekIndex in PortalConstants.PORTAL_RECRUITING_WEEKS) {
            val dashboardTasks = OffseasonOptionalDashboardTasks.build(
                OffseasonDashboardContext(
                    offseasonWeekIndex = position.weekIndex,
                    recruitingPointsAvailable = ledger?.pointsAvailable ?: 0,
                    portalTop40Locked = portalTop40Locked,
                    openScholarshipSlots = openScholarshipSlots,
                    signingCrunchActive = position.weekIndex in PortalConstants.PORTAL_RECRUITING_WEEKS,
                ),
            )
            DashboardOptionalTasksPanel(
                tasks = dashboardTasks,
                dismissedIds = dismissedTasks,
                onDismiss = { dismissedTasks = dismissedTasks + it },
                onDeepLink = onOpenRecruiting,
            )
        }

        pendingAdvanceConfirmation?.let { confirmation ->
            SimWeekAdvanceDialog(
                confirmation = confirmation,
                isAdvancing = dialogAdvanceInProgress,
                onConfirm = {
                    scope.launch {
                        dialogAdvanceInProgress = true
                        try {
                            when (val result = saveSlotRepository.advanceDynastyWeek(loaded.slotIndex)) {
                                is DynastyLoadResult.Success -> {
                                    loaded = result.loaded
                                    ActiveDynastySession.set(result.loaded)
                                    pendingAdvanceConfirmation = null
                                }
                                else -> Unit
                            }
                        } finally {
                            dialogAdvanceInProgress = false
                        }
                    }
                },
                onDismiss = {
                    if (!dialogAdvanceInProgress) {
                        pendingAdvanceConfirmation = null
                    }
                },
            )
        }

        Button(
            onClick = {
                scope.launch {
                    val signing = signingProgress
                        ?: signingDayRepository.loadProgress(loaded.dynastyId, loaded.metadata.seasonIndex)
                    signingProgress = signing
                    val requiredTasks = if (inOffseason && position.weekIndex == SigningDayConstants.OFFSEASON_WEEK_INDEX) {
                        SigningDayRequiredDashboardTasks.build(signing)
                    } else {
                        emptyList()
                    }
                    if (DashboardAdvanceGate.blocksWeekAdvance(requiredTasks)) {
                        return@launch
                    }
                    if (inOffseason &&
                        position.weekIndex == SigningDayConstants.OFFSEASON_WEEK_INDEX &&
                        !signing.canAdvanceOffseasonWeek()
                    ) {
                        return@launch
                    }
                    if (inOffseason && position.weekIndex == OffseasonRosterLockConstants.OFFSEASON_WEEK_INDEX) {
                        val roster = rosterRepository.listPlayers(loaded.dynastyId)
                        if (!RosterLockPolicy.canAdvanceOffseasonWeek8(roster)) {
                            return@launch
                        }
                    }
                    if (inOffseason && position.weekIndex == OffseasonWeekTenConstants.OFFSEASON_WEEK_INDEX) {
                        if (!weekTenRepository.canAdvance(loaded.dynastyId, loaded.metadata.seasonIndex)) {
                            return@launch
                        }
                    }
                    val dashboardTasks = if (inOffseason && position.weekIndex in PortalConstants.PORTAL_RECRUITING_WEEKS) {
                        OffseasonOptionalDashboardTasks.build(
                            OffseasonDashboardContext(
                                offseasonWeekIndex = position.weekIndex,
                                recruitingPointsAvailable = ledger?.pointsAvailable ?: 0,
                                portalTop40Locked = portalTop40Locked,
                                openScholarshipSlots = openScholarshipSlots,
                                signingCrunchActive = true,
                            ),
                        )
                    } else {
                        emptyList()
                    }
                    weekAdvanceInProgress = true
                    try {
                        val unresolved = dashboardTasks.count { it.id !in dismissedTasks }
                        val confirmation = saveSlotRepository.buildSimWeekAdvanceConfirmation(
                            loaded.slotIndex,
                            unresolvedOptionalTaskCount = unresolved,
                        )
                        if (confirmation != null && confirmation.needsDialog) {
                            pendingAdvanceConfirmation = confirmation
                        } else {
                            when (val result = saveSlotRepository.advanceDynastyWeek(loaded.slotIndex)) {
                                is DynastyLoadResult.Success -> {
                                    loaded = result.loaded
                                    ActiveDynastySession.set(result.loaded)
                                }
                                else -> Unit
                            }
                        }
                    } finally {
                        weekAdvanceInProgress = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !weekAdvanceInProgress &&
                !dialogAdvanceInProgress &&
                inOffseason && when (position.weekIndex) {
                SigningDayConstants.OFFSEASON_WEEK_INDEX ->
                    signingProgress?.canAdvanceOffseasonWeek() == true
                OffseasonRosterLockConstants.OFFSEASON_WEEK_INDEX ->
                    rosterPlayerCount <= OffseasonRosterLockConstants.SCHOLARSHIP_ROSTER_CAP
                OffseasonWeekTenConstants.OFFSEASON_WEEK_INDEX -> conferenceOffersResolved
                else -> true
            },
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (weekAdvanceInProgress) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                    )
                }
                Text(
                    if (weekAdvanceInProgress) {
                        stringResource(R.string.dynasty_shell_advance_week_in_progress)
                    } else if (inOffseason && position.weekIndex >= 10) {
                        stringResource(R.string.offseason_advance_to_preseason)
                    } else {
                        stringResource(R.string.offseason_advance_week)
                    },
                )
            }
        }

            }
        }
    }
}
