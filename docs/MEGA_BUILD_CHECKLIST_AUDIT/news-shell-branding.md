# MEGA audit — app shell, branding, news hub, notifications, discipline, chemistry

**Scope:** [`planning/app-shell.md`](../../planning/app-shell.md), [`planning/branding.md`](../../planning/branding.md), [`planning/news-hub.md`](../../planning/news-hub.md), [`planning/notifications.md`](../../planning/notifications.md), [`planning/in-season-systems.md`](../../planning/in-season-systems.md) vs Android pre-menu + dynasty UI (`android/app/.../ui/shell`, `ui/news`, `ui/discipline`, core `news` / `notification` / `discipline` / `chemistry` / `fatigue`).

**Audited:** 2026-11-11

**Legend**

| Tag | Meaning |
|-----|---------|
| **SHIP** | Matches spec (core + UI wired) |
| **PARTIAL** | Present but incomplete vs design lock |
| **GAP** | Missing or wrong behavior |
| **N/A** | Out of scope for current routing |

---

## Pre-menu — studio, splash, branding

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-SHELL-001 | SHIP | Cold start: Studio → Splash → Home | Nav order `AppRoute.Studio` → `Splash` → `Home` | `DynastyApp.kt` |
| MEGA-SHELL-002 | SHIP | Studio auto-advance 3.5s | `GameConstants.STUDIO_AUTO_ADVANCE_MS = 3_500` | `StudioScreen.kt` |
| MEGA-SHELL-003 | SHIP | Studio tap skip after 0.5s min | `STUDIO_MIN_SKIP_MS`; clickable gated on `skipEnabled` | `StudioScreen.kt` |
| MEGA-SHELL-004 | SHIP | Studio Novux lockup, not game logo | `R.drawable.novux_studio_lockup` hero | `StudioScreen.kt`, `app-shell.md` |
| MEGA-SHELL-005 | SHIP | Studio novux.dev line | `R.string.studio_website` | `StudioScreen.kt` |
| MEGA-SHELL-006 | PARTIAL | Studio background `#181a25` full bleed | Theme uses brand hex; studio screen relies on Material `background` — verify edge-to-edge on device | `Theme.kt`, `StudioScreen.kt` |
| MEGA-SHELL-007 | GAP | Studio tap hint in `strings.xml` | Hard-coded `"Tap to continue"` in Compose — violates branding single-source copy rule | `StudioScreen.kt` vs `branding.md#ui-copy` |
| MEGA-SHELL-008 | SHIP | Settings: skip studio intro | `AppSettings.skipStudioIntro` → start at Splash | `SettingsScreen`, `DynastyApp` `startRoute` |
| MEGA-SHELL-009 | SHIP | Splash: game logo + official title | `app_logo`, `GameConstants.GAME_TITLE` | `SplashScreen.kt` |
| MEGA-SHELL-010 | SHIP | Splash pulsing Tap to Play CTA | Scale 0.94↔1.06, opacity 0.78↔1.0, 900ms | `SplashScreen.kt`, `app-shell.md#tap-to-play` |
| MEGA-SHELL-011 | PARTIAL | Splash loading gate before CTA | Fixed `delay(600)` — not tied to pack/save index readiness | `SplashScreen.kt` |
| MEGA-SHELL-012 | GAP | Splash pack / asset fail + Retry | Spec: stay on splash with error + Retry | `app-shell.md#loading--errors` — not implemented |
| MEGA-SHELL-013 | SHIP | Splash footer © Novux + director line | `splash_copyright`, `game_director_line` | `SplashScreen.kt` |
| MEGA-SHELL-014 | SHIP | Brand background `#181A25` in theme + launcher | `GameConstants.BRAND_BACKGROUND_HEX`, `colors.xml`, adaptive icon | `Theme.kt`, `mipmap-anydpi-v26` |
| MEGA-SHELL-015 | SHIP | Contrast tokens on brand field | `#E8EAED` on-surface, `#A8AEB8` muted | `Theme.kt` vs `branding.md#color` |
| MEGA-SHELL-016 | PARTIAL | Home menu title branding | Text title only — no app logo on home | `HomeMenuScreen.kt` |
| MEGA-SHELL-017 | SHIP | Credits © Novux, website, disclaimer | `CreditsScreen.kt` | `game-start.md` |
| MEGA-SHELL-018 | GAP | Credits → Visit Novux / replay studio | Optional replay studio screen — not in Credits UI | `app-shell.md` |

---

## News — one pool, two surfaces

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-SHELL-019 | SHIP | Shallow dashboard cap (top N) | `ShallowNewsFeed` + `SHALLOW_FEED_CAP = 10` | `ShallowNewsFeed.kt`, `NewsHeadlinePublisher.kt` |
| MEGA-SHELL-020 | SHIP | Same archive powers hub Stories | `NewsFeedRepository.storiesFeed` = sorted archive | `NewsFeedRepository.kt` |
| MEGA-SHELL-021 | PARTIAL | Dashboard vs hub same priority queue | Shallow merge uses priority + `createdAtMs`; hub Stories sort emphasizes `createdAtMs` first | compare `storiesFeed` vs `ShallowNewsFeed` |
| MEGA-SHELL-022 | SHIP | In-season shallow News tab | `DynastyShellNewsTab` + `shallowFeed` on shell | `DynastyShellScreen.kt` |
| MEGA-SHELL-023 | GAP | Offseason / preseason News tab live slice | Placeholder empty feed on non–in-season shells | `DynastyShellNewsTabPlaceholder`, `OffseasonScreen`, `PreseasonScreen` |
| MEGA-SHELL-024 | GAP | News tab headline tap → detail / hub | Cards are non-clickable | `DynastyShellNewsTab.kt` |
| MEGA-SHELL-025 | GAP | Story date chip on feed lines | Spec: headline + date chip | `news-hub.md#stories-feed` — titles only |
| MEGA-SHELL-026 | SHIP | Hamburger → full News hub route | `AppRoute.NewsHub`, menu item | `DynastyHubMenu.kt`, `NewsHubScreen.kt` |
| MEGA-SHELL-027 | SHIP | Hub tabs: Stories / Portal / Recruits / Draft | Four tabs implemented | `NewsHubScreen.kt` |
| MEGA-SHELL-028 | PARTIAL | Portal tab hidden outside OS weeks 1–5 | Tab **row always shows Portal**; body shows “hidden” copy | `NewsHubPolicy`, `PortalTab` |
| MEGA-SHELL-029 | SHIP | Portal tab visible OS weeks 1–5 | Policy matches `PortalConstants.PORTAL_RECRUITING_WEEKS` | `NewsHubPolicyTest.kt` |
| MEGA-SHELL-030 | SHIP | Week 1 Top 40 placeholder until lock | `news_hub_portal_pre_lock` when `!top40Locked` | `NewsHubScreen.kt` |
| MEGA-SHELL-031 | GAP | Portal Top 40 full #1–#40 human labels | Rows use `entrantId` / `priorProgramId` not display names | `news_hub_portal_row` |
| MEGA-SHELL-032 | GAP | Portal recent commits / entrants sections | Only Top 40 block — no rolling commit/entrant lists | `news-hub.md#portal-tab` |
| MEGA-SHELL-033 | GAP | Portal filters row | Conference, stars, position, pipeline — absent | `news-hub.md#portal-tab` |
| MEGA-SHELL-034 | GAP | Portal row tap → profile / school hub | Plain `Text` rows — no navigation | `NewsHubScreen.kt` |
| MEGA-SHELL-035 | GAP | Add to board deep link | Spec CTA to Recruiting Portal Recommended | `news-hub.md#row-interactions` |
| MEGA-SHELL-036 | SHIP | Recruits tab national Top 60 #1–#60 | Seed or `NationalRecruitingRankings.top60` | `NewsHubScreen.kt` |
| MEGA-SHELL-037 | PARTIAL | Draft tab dynamic pro pool | Offseason week 2 published lines only — not in-season dynamic board | `offseasonWeekTwoRepository` |
| MEGA-SHELL-038 | GAP | Story detail mini-article | `ReportArticleView` used elsewhere — **no** story detail screen for headlines | `news-hub.md#story-detail--articles` |
| MEGA-SHELL-039 | GAP | Portal / recruiting headline kinds in feed | `NewsHeadlineKind` lacks PortalEntry/Commit/Flip; generators not wired | `NewsHeadline.kt`, portal sim |
| MEGA-SHELL-040 | PARTIAL | Headline types (upsert, poll, carousel) | League results, injuries, awards, recruiting spotlight partially via `NewsHeadlinePublisher` | `NewsFeedRepository.appendLeagueWeek` |

---

## Notifications — dashboard feed (not News, not push)

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-SHELL-041 | SHIP | In-app feed panel on in-season dashboard | `DashboardNotificationFeedPanel` when `showNotificationFeed` | `DynastyShellScreen.kt` |
| MEGA-SHELL-042 | SHIP | Default feed visible; settings toggle | `showDashboardNotificationFeed` default true | `AppSettings`, `SettingsScreen` |
| MEGA-SHELL-043 | SHIP | No OS push / tray | No FCM or system notification implementation in app module | grep; `notifications.md#design-locks` |
| MEGA-SHELL-044 | PARTIAL | Notification copy in `strings.xml` | **`NotificationFeedComposer` hard-coded English** title/body strings | `NotificationFeedComposer.kt` vs `branding.md` |
| MEGA-SHELL-045 | SHIP | User injury → high feed row | Composed with `DashboardDeepLink.Strategy` | `NotificationFeedComposer` |
| MEGA-SHELL-046 | SHIP | User commits / decommits → high | Repository helpers + composer kinds | `NotificationFeedRepository`, composer |
| MEGA-SHELL-047 | SHIP | Fired / postseason eliminated blocking modal | `requiresBlockingModal` + `CareerBlockNotificationDialog` | `DynastyShellScreen` |
| MEGA-SHELL-048 | PARTIAL | Week advance snapshot breadth | Shell snapshot wires injury, game week, coach points, fired — **not** decommit, signing recap, rankings, postseason invite on advance | `DynastyShellScreen` LaunchedEffect |
| MEGA-SHELL-049 | GAP | Discipline sheet opened → feed row | Spec high-priority row; composer has no discipline kind | `notifications.md#event-catalog` |
| MEGA-SHELL-050 | GAP | Carousel offer / must-sign extension feed rows | Not in composer catalog | `notifications.md` high tier |
| MEGA-SHELL-051 | GAP | Selection Sunday feed row | Not composed | `notifications.md` |
| MEGA-SHELL-052 | GAP | Medical redshirt eligible feed row | Not composed | `notifications.md` medium |
| MEGA-SHELL-053 | GAP | Fundraising / facility complete (low) | Not composed | `notifications.md` |
| MEGA-SHELL-054 | SHIP | Stories not duplicated in notification feed | League churn goes to News archive, not notification kinds for league-only events | architecture split |
| MEGA-SHELL-055 | PARTIAL | Feed deep links | Panel routes Strategy, Coach, Signing Day, Recruiting tabs — `PlayWeekGame` opens Strategy not preview | `DashboardNotificationFeedPanel.kt` |
| MEGA-SHELL-056 | GAP | Notification feed on offseason dashboard | Feed load gated to in-season shell path only | compare `OffseasonScreen` |

---

## Discipline — conduct, academic, lineup

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-SHELL-057 | SHIP | Rolls on week advance RS + tourney + postseason | `DisciplineCalendarPolicy` | `DisciplineOccurrenceEngine.kt` |
| MEGA-SHELL-058 | SHIP | Season cap on discipline resolutions | `maxPerSeason` in tuning | `DisciplineOccurrenceEngine` |
| MEGA-SHELL-059 | SHIP | Required dashboard card blocks advance | `DisciplineRequiredDashboardTasks` + `DashboardAdvanceGate` | `DashboardTaskGate`, shell |
| MEGA-SHELL-060 | SHIP | Full-screen decision sheet | `DisciplineDecisionScreen` route from shell | `DynastyApp.kt` |
| MEGA-SHELL-061 | SHIP | Conduct: dismiss, suspend 1–10, conference delegate | UI options match design lock | `DisciplineDecisionScreen.kt` |
| MEGA-SHELL-062 | GAP | Conference draw animated coin-flip UI | Instant resolve in core — no animated draw surface | `in-season-systems.md#conference-draw` |
| MEGA-SHELL-063 | SHIP | Academic: sweep, suspend games, hold + support | Three branches + slider | `DisciplineDecisionScreen` |
| MEGA-SHELL-064 | SHIP | `gamesSuspendedRemaining` decrement per team game | `decrementGameSuspensions` when `teamPlayedGame` | `DisciplineResolver.kt` |
| MEGA-SHELL-065 | PARTIAL | Bye weeks do not decrement suspensions | Decrement only when `teamPlayedGame` — depends on caller passing bye correctly | `DisciplineRepository.afterWeekAdvance` |
| MEGA-SHELL-066 | SHIP | Suspended / hold ineligible in lineup | `DisciplineResolver.isIneligibleForLineup` in Strategy + auto-starters | `StrategyScreen`, `DefaultStarterAlgorithm` |
| MEGA-SHELL-067 | PARTIAL | Greyed suspended players in Strategy UI | Ineligible treated like injury grey path via `isIneligibleForLineup` — verify visual distinction | `StrategyScreen.kt` |
| MEGA-SHELL-068 | SHIP | Weekly academic support GPA tick | `tickAcademicSupport` on week advance | `DisciplineResolver`, repository |
| MEGA-SHELL-069 | GAP | Cover-up audit end of season | `academicCoverUp` flag set; **no** `auditBaseChance` roll in save flow | `balance-2026.json`, `SaveSlotRepository` |
| MEGA-SHELL-070 | GAP | AD trust / job security / News on discipline outcomes | Fields in spec table — not wired from resolver to coach card + News | `in-season-systems.md#ad-trust` |
| MEGA-SHELL-071 | PARTIAL | Conduct flavor from balance templates | Engine uses templates; headlines in core English | `DisciplineOccurrenceEngine.rollConduct` |
| MEGA-SHELL-072 | PARTIAL | Academic window RS weeks 18–22 | `academicWindowWeeks` in balance JSON | confirm tuning matches spec default |

---

## Chemistry & fatigue

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-SHELL-073 | SHIP | `teamChemistry` 0–100 persisted per season | `ChemistrySeasonState`, DataStore | `ChemistryRepository.kt` |
| MEGA-SHELL-074 | SHIP | Bootstrap ~70 new save | `ChemistryWeekEngine.bootstrap` | core tests |
| MEGA-SHELL-075 | SHIP | Weekly tick after advance (in-season phases) | `afterWeekAdvance` in repository + `SaveSlotRepository` | win/loss streak inputs |
| MEGA-SHELL-076 | SHIP | Display on School hub Overview + Strategy strip | `school_hub_chemistry`, `strategy_chemistry` | `SchoolHubScreen`, `StrategyScreen` |
| MEGA-SHELL-077 | GAP | Chemistry clutch modifier in live sim | `ChemistryRepository.clutchModifier` defined — **not referenced** in sim package | grep `sim/` |
| MEGA-SHELL-078 | PARTIAL | Promise broken / roster churn / charisma inputs | Engine supports; shell tick passes `userWonGame` only — other inputs default false | `ChemistryWeeklyTickInput` wiring |
| MEGA-SHELL-079 | SHIP | Per-player `stamina` on roster model | DB columns + `FatigueEngine` | `RosterPlayer`, migrations |
| MEGA-SHELL-080 | SHIP | Fatigue tick on week advance | `DynastyRosterRepository.tickFatigueForWeekAdvance` | `SaveSlotRepository` |
| MEGA-SHELL-081 | SHIP | Strategy stamina readout + starter block &lt;20 | `strategy_stamina`, `starterBlockThreshold` | `StrategyScreen.kt` |
| MEGA-SHELL-082 | PARTIAL | Fatigue rotation pull in sim | `shouldPullInRotation` in core — sim integration not audited here | `FatigueEngine.kt` |
| MEGA-SHELL-083 | PARTIAL | Press scheme / back-to-back drain extras | Base drain in `FatigueEngine` — press/B2B penalties thin vs spec formula | `in-season-systems.md#fatigue` |
| MEGA-SHELL-084 | SHIP | Chemistry does not block lineup edits | Display-only chemistry on Strategy | `StrategyScreen` |

---

## Cross-cutting shell UX

| ID | Status | Spec / topic | Finding | Code / notes |
|----|--------|--------------|---------|--------------|
| MEGA-SHELL-085 | PARTIAL | Resume from background skip studio | No lifecycle handler documented in app — cold start only path audited | `app-shell.md#launch-flow` |
| MEGA-SHELL-086 | SHIP | Official product title constant | `Dynasty: College Basketball` single constant | `GameConstants.GAME_TITLE` |
| MEGA-SHELL-087 | GAP | `lint_player_strings.py` CI on notification composer | Hard-coded user-facing strings in core composer may fail lint if extended to core | `branding.md#ui-copy` |

---

## Summary counts

| Status | Count |
|--------|------:|
| SHIP | 38 |
| PARTIAL | 22 |
| GAP | 27 |
| **Total items** | **87** |

**Priority clusters:** MEGA-SHELL-031–035 (News portal product depth), MEGA-SHELL-038–039 (stories + portal headlines), MEGA-SHELL-044–056 (notification catalog + strings), MEGA-SHELL-062/069–070 (discipline UX + consequences), MEGA-SHELL-077–078 (chemistry sim), MEGA-SHELL-011–012 (splash load hardening).

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-11 | Initial audit vs app-shell, branding, news-hub, notifications, in-season-systems |
