package dev.novux.dynasty.cbb.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import dev.novux.dynasty.cbb.R
import dev.novux.dynasty.cbb.core.settings.AppSettings
import dev.novux.dynasty.cbb.data.save.ActiveDynastySession
import dev.novux.dynasty.cbb.core.save.DynastyLoadResult
import dev.novux.dynasty.cbb.data.save.SaveSlotRepository
import dev.novux.dynasty.cbb.ui.shell.SaveLoadUnavailableDialog
import dev.novux.dynasty.cbb.data.settings.AppSettingsRepository
import dev.novux.dynasty.cbb.requireDynastyApplication
import dev.novux.dynasty.cbb.ui.navigation.AppRoute
import dev.novux.dynasty.cbb.ui.shell.CreditsScreen
import dev.novux.dynasty.cbb.ui.shell.HomeMenuScreen
import dev.novux.dynasty.cbb.ui.shell.LoadGameScreen
import dev.novux.dynasty.cbb.ui.shell.SettingsScreen
import dev.novux.dynasty.cbb.core.save.LoadedDynasty
import dev.novux.dynasty.cbb.ui.navigation.DynastyLaunch
import dev.novux.dynasty.cbb.ui.shell.DynastyShellScreen
import dev.novux.dynasty.cbb.ui.newgame.NewGameWizardScreen
import dev.novux.dynasty.cbb.ui.offseason.CoachingCarouselScreen
import dev.novux.dynasty.cbb.core.recruiting.RecruitingHubDefaults
import dev.novux.dynasty.cbb.core.dashboard.DashboardDeepLink
import dev.novux.dynasty.cbb.ui.recruiting.RecruitingHubScreen
import dev.novux.dynasty.cbb.ui.offseason.OffseasonScreen
import dev.novux.dynasty.cbb.ui.preseason.PreseasonScreen
import dev.novux.dynasty.cbb.ui.signingday.SigningDayInitialTab
import dev.novux.dynasty.cbb.ui.signingday.SigningDayScreen
import dev.novux.dynasty.cbb.ui.seasonreview.SeasonReviewScreen
import dev.novux.dynasty.cbb.ui.shell.SplashScreen
import dev.novux.dynasty.cbb.ui.shell.StudioScreen
import dev.novux.dynasty.cbb.ui.theme.DynastyTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

@Composable
fun DynastyApp(
    settingsRepository: AppSettingsRepository =
        LocalContext.current.requireDynastyApplication().appSettingsRepository,
    saveSlotRepository: SaveSlotRepository =
        LocalContext.current.requireDynastyApplication().saveSlotRepository,
    coachingCarouselRepository: dev.novux.dynasty.cbb.data.carousel.CoachingCarouselRepository =
        LocalContext.current.requireDynastyApplication().coachingCarouselRepository,
    recruitingEconomyRepository: dev.novux.dynasty.cbb.data.recruiting.RecruitingEconomyRepository =
        LocalContext.current.requireDynastyApplication().recruitingEconomyRepository,
) {
    DynastyTheme {
        var launchSettings by remember { mutableStateOf<AppSettings?>(null) }
        val scope = rememberCoroutineScope()

        LaunchedEffect(settingsRepository) {
            launchSettings = settingsRepository.currentSettings()
        }

        if (launchSettings == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            )
        } else {
            val startRoute = if (launchSettings!!.skipStudioIntro) {
                AppRoute.Splash.route
            } else {
                AppRoute.Studio.route
            }
            val navController = rememberNavController()
            val continueSlot by saveSlotRepository.observeContinueSlot()
                .collectAsState(initial = null)
            val context = LocalContext.current
            val snackbarHostState = remember { SnackbarHostState() }
            var continueLoadFailedSlot by remember { mutableStateOf<Int?>(null) }
            var recruitingLaunchTab by remember {
                mutableStateOf(RecruitingHubDefaults.defaultTab)
            }
            var signingDayInitialTab by remember { mutableStateOf(SigningDayInitialTab.Rankings) }
            val portalCycleRepository = LocalContext.current.requireDynastyApplication().portalCycleRepository
            val signingDayRepository = LocalContext.current.requireDynastyApplication().signingDayRepository
            val offseasonWeekTwoRepository =
                LocalContext.current.requireDynastyApplication().offseasonWeekTwoRepository
            val summerWorkoutRepository =
                LocalContext.current.requireDynastyApplication().summerWorkoutRepository
            val offseasonWeekEightRepository =
                LocalContext.current.requireDynastyApplication().offseasonWeekEightRepository
            val offseasonWeekNineRepository =
                LocalContext.current.requireDynastyApplication().offseasonWeekNineRepository
            val offseasonWeekTenRepository =
                LocalContext.current.requireDynastyApplication().offseasonWeekTenRepository
            val preseasonProgressRepository =
                LocalContext.current.requireDynastyApplication().preseasonProgressRepository
            val incomingClassSeedRepository =
                LocalContext.current.requireDynastyApplication().incomingClassSeedRepository
            val tournamentInvitesRepository =
                LocalContext.current.requireDynastyApplication().tournamentInvitesRepository
            val preseasonScheduleRepository =
                LocalContext.current.requireDynastyApplication().preseasonScheduleRepository
            val preseasonReportRepository =
                LocalContext.current.requireDynastyApplication().preseasonReportRepository
            val preseasonSeasonStartRepository =
                LocalContext.current.requireDynastyApplication().preseasonSeasonStartRepository
            val schoolStaffRepository =
                LocalContext.current.requireDynastyApplication().schoolStaffRepository

            suspend fun onDynastyReady(loaded: LoadedDynasty) {
                ActiveDynastySession.set(loaded)
                val carousel = coachingCarouselRepository.loadOrCreate(
                    loaded.dynastyId,
                    loaded.metadata.seasonIndex,
                )
                DynastyLaunch.navigateForLoaded(navController, loaded, carousel)
            }

            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
            ) { padding ->
            NavHost(
                navController = navController,
                startDestination = startRoute,
                modifier = Modifier.padding(padding),
            ) {
                composable(AppRoute.Studio.route) {
                    StudioScreen(
                        onFinished = { navController.navigate(AppRoute.Splash.route) },
                    )
                }
                composable(AppRoute.Splash.route) {
                    SplashScreen(
                        onTapToPlay = {
                            navController.navigate(AppRoute.Home.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = true
                                }
                            }
                        },
                    )
                }
                composable(AppRoute.Home.route) {
                    HomeMenuScreen(
                        continueSlot = continueSlot,
                        onContinue = {
                            val slot = continueSlot ?: return@HomeMenuScreen
                            scope.launch {
                                when (val result = saveSlotRepository.loadSlot(slot.slotIndex)) {
                                    is DynastyLoadResult.Success -> onDynastyReady(result.loaded)
                                    is DynastyLoadResult.Unreadable ->
                                        continueLoadFailedSlot = slot.slotIndex
                                    DynastyLoadResult.SlotEmpty -> Unit
                                }
                            }
                        },
                        onNew = { navController.navigate(AppRoute.NewGame.route) },
                        onLoad = { navController.navigate(AppRoute.Load.route) },
                        onSettings = { navController.navigate(AppRoute.Settings.route) },
                        onCredits = { navController.navigate(AppRoute.Credits.route) },
                    )
                }
                composable(AppRoute.Load.route) {
                    LoadGameScreen(
                        saveSlotRepository = saveSlotRepository,
                        onBack = { navController.popBackStack() },
                        onDynastyLoaded = { loaded ->
                            onDynastyReady(loaded)
                            navController.popBackStack()
                        },
                    )
                }
                composable(AppRoute.SeasonReview.route) {
                    val session = ActiveDynastySession.current
                    if (session != null) {
                        SeasonReviewScreen(
                            loaded = session,
                            saveSlotRepository = saveSlotRepository,
                            onReturnedHome = {
                                navController.popBackStack(AppRoute.Home.route, inclusive = false)
                            },
                            onEnteredOffseason = { updated ->
                                scope.launch { onDynastyReady(updated) }
                            },
                        )
                    } else {
                        LaunchedEffect(Unit) {
                            navController.popBackStack(AppRoute.Home.route, inclusive = false)
                        }
                    }
                }
                composable(AppRoute.CoachingCarousel.route) {
                    val session = ActiveDynastySession.current
                    if (session != null) {
                        CoachingCarouselScreen(
                            loaded = session,
                            repository = coachingCarouselRepository,
                            onComplete = {
                                navController.navigate(AppRoute.Offseason.route) {
                                    popUpTo(AppRoute.Home.route) { inclusive = false }
                                }
                            },
                        )
                    } else {
                        LaunchedEffect(Unit) {
                            navController.popBackStack(AppRoute.Home.route, inclusive = false)
                        }
                    }
                }
                composable(AppRoute.Preseason.route) {
                    val session = ActiveDynastySession.current
                    if (session != null) {
                        PreseasonScreen(
                            initial = session,
                            saveSlotRepository = saveSlotRepository,
                            progressRepository = preseasonProgressRepository,
                            incomingClassSeedRepository = incomingClassSeedRepository,
                            tournamentInvitesRepository = tournamentInvitesRepository,
                            scheduleRepository = preseasonScheduleRepository,
                            preseasonReportRepository = preseasonReportRepository,
                            seasonStartRepository = preseasonSeasonStartRepository,
                            onSeasonStarted = {
                                navController.navigate(AppRoute.DynastyShell.route) {
                                    popUpTo(AppRoute.Home.route) { inclusive = false }
                                }
                            },
                            onExitHome = {
                                navController.popBackStack(AppRoute.Home.route, inclusive = false)
                            },
                        )
                    } else {
                        LaunchedEffect(Unit) {
                            navController.popBackStack(AppRoute.Home.route, inclusive = false)
                        }
                    }
                }
                composable(AppRoute.DynastyShell.route) {
                    val session = ActiveDynastySession.current
                    if (session != null) {
                        DynastyShellScreen(
                            loaded = session,
                            scheduleRepository = preseasonScheduleRepository,
                            staffRepository = schoolStaffRepository,
                            onExitHome = {
                                navController.popBackStack(AppRoute.Home.route, inclusive = false)
                            },
                        )
                    } else {
                        LaunchedEffect(Unit) {
                            navController.popBackStack(AppRoute.Home.route, inclusive = false)
                        }
                    }
                }
                composable(AppRoute.Offseason.route) {
                    val session = ActiveDynastySession.current
                    if (session != null) {
                        OffseasonScreen(
                            initial = session,
                            saveSlotRepository = saveSlotRepository,
                            rosterRepository = LocalContext.current.requireDynastyApplication().dynastyRosterRepository,
                            recruitingEconomyRepository = recruitingEconomyRepository,
                            portalCycleRepository = portalCycleRepository,
                            signingDayRepository = signingDayRepository,
                            offseasonWeekTwoRepository = offseasonWeekTwoRepository,
                            summerWorkoutRepository = summerWorkoutRepository,
                            weekEightRepository = offseasonWeekEightRepository,
                            weekNineRepository = offseasonWeekNineRepository,
                            weekTenRepository = offseasonWeekTenRepository,
                            onExitHome = {
                                navController.popBackStack(AppRoute.Home.route, inclusive = false)
                            },
                            onOpenRecruiting = { tab ->
                                recruitingLaunchTab = tab
                                navController.navigate(AppRoute.Recruiting.route)
                            },
                            onOpenSigningDay = { link ->
                                signingDayInitialTab = when (link) {
                                    DashboardDeepLink.SigningDayRankings -> SigningDayInitialTab.Rankings
                                    DashboardDeepLink.SigningDayPositions -> SigningDayInitialTab.Positions
                                    else -> SigningDayInitialTab.Rankings
                                }
                                navController.navigate(AppRoute.SigningDay.route)
                            },
                        )
                    } else {
                        LaunchedEffect(Unit) {
                            navController.popBackStack(AppRoute.Home.route, inclusive = false)
                        }
                    }
                }
                composable(AppRoute.NewGame.route) {
                    NewGameWizardScreen(
                        saveSlotRepository = saveSlotRepository,
                        packRepository = LocalContext.current.requireDynastyApplication().packRepository,
                        onExitWizard = { navController.popBackStack() },
                        onDynastyCreated = {
                            scope.launch {
                                ActiveDynastySession.current?.let { onDynastyReady(it) }
                                navController.popBackStack()
                            }
                        },
                        onNoEmptySlot = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    context.getString(R.string.new_no_empty_slot),
                                )
                            }
                        },
                    )
                }
                composable(AppRoute.Credits.route) {
                    CreditsScreen(onBack = { navController.popBackStack() })
                }
                composable(AppRoute.Recruiting.route) {
                    val active = ActiveDynastySession.current
                    if (active != null) {
                        RecruitingHubScreen(
                            loaded = active,
                            recruitingEconomyRepository = recruitingEconomyRepository,
                            weekNineRepository = offseasonWeekNineRepository,
                            initialTab = recruitingLaunchTab,
                            onBack = { navController.popBackStack() },
                        )
                    }
                }
                composable(AppRoute.SigningDay.route) {
                    val active = ActiveDynastySession.current
                    if (active != null) {
                        SigningDayScreen(
                            loaded = active,
                            signingDayRepository = signingDayRepository,
                            recruitingEconomyRepository = recruitingEconomyRepository,
                            incomingClassSeedRepository = incomingClassSeedRepository,
                            initialTab = signingDayInitialTab,
                            onBack = { navController.popBackStack() },
                        )
                    }
                }
                composable(AppRoute.Settings.route) {
                    SettingsScreen(
                        settingsRepository = settingsRepository,
                        onBack = { navController.popBackStack() },
                    )
                }
            }

            continueLoadFailedSlot?.let { slotIndex ->
                SaveLoadUnavailableDialog(
                    slotDisplayNumber = slotIndex + 1,
                    onDelete = {
                        scope.launch {
                            saveSlotRepository.deleteSlot(slotIndex)
                            if (ActiveDynastySession.current?.slotIndex == slotIndex) {
                                ActiveDynastySession.clear()
                            }
                            continueLoadFailedSlot = null
                        }
                    },
                    onDismiss = { continueLoadFailedSlot = null },
                )
            }
            }
        }
    }
}
