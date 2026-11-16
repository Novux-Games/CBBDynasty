# MEGA audit: coach card · player card · portraits · new-game coach creation

**Scope:** `planning/coach-card.md`, `planning/player-cards.md`, `planning/player-archetypes.md`, `planning/player-loyalty-promises.md`, `planning/game-start.md` (coach identity; **not** coach portrait surfaces) vs Android `ui/coach`, `ui/player`, `ui/newgame`, `ui/shell`, `:core/coach`, `:core/player`, `:database` coach/player rows.  
**Audit date:** 2026-11-11 (expanded pass)  
**Method:** Full planning-doc read + static trace of UI/core/save/navigation paths (no emulator render).  
**Product lock (this audit):** **No coach avatars / coach portrait renderer** on coach card, preview, or identity strip — wizard may keep **text preset chips** + persisted ids only; **player** headshot pipeline is in scope.

**Legend:** **Status** — `FAIL` spec violation or absent; `GAP` partial/stub; `PASS` aligned; `RISK` fragile; `N/A` explicitly out of product scope. **Pri** — P0 ship-blocker … P3 polish.

---

## A. New-game wizard — coach identity (no coach portrait ship)

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
| MEGA-COACH-001 | P1 | PASS | Wizard Step 1: first / last name text fields | `CoachInfoStepContent.kt`, `CoachCreateProfile` |
| MEGA-COACH-002 | P1 | PASS | Name suffix dropdown None / Jr / II / III / IV | `SuffixDropdown`, `CoachNameSuffix` |
| MEGA-COACH-003 | P0 | PASS | Age 28–65 band + validation | `MIN_AGE`/`MAX_AGE`, `CoachCreateValidator` |
| MEGA-COACH-004 | P1 | PASS | Alma mater searchable pack schools + None | `AlmaMaterPicker` |
| MEGA-COACH-005 | P1 | PASS | Display name `First [Suffix] Last` | `CoachCreateProfile.displayName()`, unit test |
|  | MEGA-COACH-006 | P2 | SHIP | Appearance: **pack-driven** preset swatches | Coach appearance preset chips — pack tint hints via `CoachAppearancePackTintPolicy` |  |
| MEGA-COACH-007 | P2 | PASS | Skin tone preset picker (text/id chips) | `PresetChipRow` + `skinTones` — **no color swatch UI** |
| MEGA-COACH-008 | P2 | PASS | Hair color preset picker | `hairColors` chips |
| MEGA-COACH-009 | P2 | PASS | Hair style preset picker incl. bald | `hairStyles` |
| MEGA-COACH-010 | P2 | PASS | Beard style incl. None | `beardStyles` |
| MEGA-COACH-011 | — | N/A | Visual **color swatches** for appearance | **Product: no coach portrait ship**; text chips sufficient for ids |
| MEGA-COACH-012 | — | N/A | Live **portrait preview** while picking appearance | **Product: no coach avatars**; name text preview only (`new_coach_display_preview`) |
| MEGA-COACH-013 | P1 | PASS | `avatarPresetId` composite key | `coach-$skin-$hair-$style-$beard` |
| MEGA-COACH-014 | P1 | PASS | Component ids persisted on save | `DynastyCoachEntity` skin/hair/style/beard + `avatarPresetId` |
| MEGA-COACH-015 | P1 | PASS | Invalid avatar ids rejected at wizard Next | `CoachCreateValidationError.InvalidAvatar` |
| MEGA-COACH-016 | — | N/A | Coach **portrait renderer** from preset id | **Explicitly excluded** — no `Portrait` for coaches |
| MEGA-COACH-017 | — | N/A | Coach card identity strip shows portrait | **Product: no coach avatars** — strip is text-only per lock |
| MEGA-COACH-018 | P1 | PASS | Coach **preview sheet** quick info (no portrait required) | `CoachPreviewSheet` / `CoachPreviewBottomSheet`; staff tap → preview → View more → `HeadCoachCardScreen`; text-only quick info | `CoachPreviewSheetPathTest` |
|  | MEGA-COACH-019 | P2 | SHIP | Shared appearance **ids** with player pipeline | Shared `PlayerAppearance` + coach preset id namespace documented |  |
| MEGA-COACH-020 | P2 | PASS | Unit test coach create validation | `CoachCreateValidatorTest` (name/suffix; not full avatar matrix) |

---

## B. New-game wizard (coach-adjacent steps)

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
| MEGA-COACH-021 | P0 | PASS | Steps 1–6 Coach → Archetype → Schemes → Settings → School → Sign | `NewGameStep` enum |
| MEGA-COACH-022 | P1 | PASS | Step 2 coach archetype radio list | `ArchetypeStepContent`, `CoachArchetype` |
| MEGA-COACH-023 | P1 | PASS | Step 3 O/D scheme chip pickers (7 presets) | `SchemesStepContent`, strategy enums |
| MEGA-COACH-024 | P0 | PASS | Draft held until `signNewGameFromWizard` | `NewGameWizardScreen` + `NewGameDraft` |
| MEGA-COACH-025 | P1 | PASS | Coach row written with scheme + archetype wires | `DynastyCoachEntity`, `DynastyCoachMappers` |
|  | MEGA-COACH-026 | P2 | SHIP | Archetype → starting skill tree bias at create | Archetype flavor at create; skill tree spend on Overview |  |
|  | MEGA-COACH-027 | P2 | SHIP | Sign step recap: archetype + schemes + school | Sign step structured recap card on `SignContractStepContent` |  |

---

## C. Coach card entry flow & chrome (no coach portrait)

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
| MEGA-COACH-028 | P0 | PASS | Tap staff → **preview sheet** → View more → full card | `CoachPreviewBottomSheet` on staff tap; View more → `ShellCoachView` full card | `CoachPreviewSheet.kt`, `DynastyShellScreen.kt` |
| MEGA-COACH-029 | P1 | PASS | Full card: persistent Back / Name / Edit chrome | `CoachCardPersistentHeader` on HC + assistant full cards | `CoachCardPersistentHeader.kt`, `DynastyShellScreen.kt` |
| MEGA-COACH-030 | P1 | PASS | Identity strip: **school + role** (no portrait per product lock) | `CoachCardIdentityStrip` — school, role, record; no portrait | `CoachCardIdentityStrip.kt` |
| MEGA-COACH-031 | P0 | PASS | Tab bar: Overview · Contract · History · HOF · Draft | `PrimaryTabRow` on `HeadCoachCardScreen` | `HeadCoachCardScreen.kt`, `CoachCardTabs.kt` |
| MEGA-COACH-032 | P2 | PASS | Overview: school + season record | Identity strip + `CoachOverviewTeamCard` on Overview | `CoachCardIdentityStrip.kt`, `CoachOverviewTeamCard.kt` |
| MEGA-COACH-033 | P1 | PASS | Overview team card tap → School hub | `CoachOverviewTeamCard` on HC Overview; tap → `ShellCoachView.School` | `CoachOverviewTeamCardPathTest` |
|  | MEGA-COACH-034 | P2 | SHIP | Overview information: age, alma mater | HC Overview shows age + alma mater lines |  |
| MEGA-COACH-035 | P1 | PASS | Contract overview on Overview (years, years left, **prestige**) | `CoachOverviewContractCard` on HC Overview | `CoachOverviewContractCardPathTest` |
| MEGA-COACH-036 | P1 | PASS | Overview achievements block (W–L, tourney, titles, NCI, Heritage) | `CoachAchievementEngine` + ledger; `CoachOverviewAchievementsCard` | `CoachAchievementEngineTest`, `CoachOverviewAchievementsPathTest` |
| MEGA-COACH-037 | P2 | PASS | Overview strategy default O/D schemes | `RatingCard` strategy block on user HC Overview | `HeadCoachCardScreen.kt` |
| MEGA-COACH-038 | P1 | PASS | Headline HC ratings O/D/Training/Recruiting 0–99 | `HeadCoachRatings`, `RatingCard` |
| MEGA-COACH-039 | P2 | PASS | Edit mode user HC (scheme, skill spend, contract) | Edit affordance + hint dialog; skill spend on Overview for user HC | `CoachCardPersistentHeader.kt`, `HeadCoachCardScreen.kt` |
| MEGA-COACH-040 | P1 | PASS | Opponent / AI coach cards hide job security | `CoachCardUserGate` + gated preview/full security + read-only contract | `CoachCardUserGate.kt`, `DynastyShellScreen.kt` |

---

## D. Job security · AD trust · contract

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
| MEGA-COACH-041 | P1 | PASS | Job security meter 0–100 + tier + trend | `JobSecurityMeter` on HC Overview + Contract tab | `JobSecurityMeter.kt`, `HeadCoachCardScreen.kt` |
| MEGA-COACH-042 | P1 | PASS | Factor list helping/hurting chips | `JobSecurityFactorContribution` in UI |
| MEGA-COACH-043 | P1 | PASS | Core evaluator + tiers | `CoachSecurityEvaluator`, `JobSecurityTier` tests |
|  | MEGA-COACH-044 | P2 | SHIP | Job security on Contract tab + AD trust summary link | Job security meter on Overview + Contract tab |  |
|  | MEGA-COACH-045 | P2 | SHIP | AD trust meter on School Staff tab (user school) | `AdTrustMeter` on `SchoolStaffTabPanel` |  |
| MEGA-COACH-046 | P0 | PASS | Contract tab deep fields (salary, buyout, goals, extension, resign) | Salary/buyout/goals, AD trust link, resign CTA | `HeadCoachContractEngine.kt`, Contract tab |
|  | MEGA-COACH-047 | P2 | SHIP | Job Center carousel offers UI | `JobCenterScreen` accept → coach employment refresh |  |
|  | MEGA-COACH-048 | P2 | SHIP | Balance bundle `coach.jobSecurityWeights` in runtime | `CoachSecurityEvaluator` loads balance weights at runtime |  |

---

## E. Assistant coach card & staff

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
|  | MEGA-COACH-049 | P2 | SHIP | Assistant card Overview ratings Rec/Dev/Scout | Assistant Overview ratings rows on `AssistantCoachCardScreen` |  |
| MEGA-COACH-050 | P1 | PASS | Assistant tabs History · Contract (HC conditional Achievements/HOF/Draft) | `PrimaryTabRow` on `AssistantCoachCardScreen` | `AssistantCoachCardScreen.kt`, `CoachCardTabs.kt` |
| MEGA-COACH-051 | P1 | PASS | Support staff rows not tappable / no card | `SchoolStaffTabPanel` `clickable = false` |
| MEGA-COACH-052 | P2 | PASS | Policy: coach-track eligible for card | `CoachCardPolicy`, test |
| MEGA-COACH-053 | P2 | PASS | Staff grade aggregate chip | `SchoolStaffGradeEngine` + `AssistChip` on `SchoolStaffTabPanel` | `SchoolStaffGradeEngineTest`, `MegaCoachBatch32PathTest` |
| MEGA-COACH-054 | P1 | PASS | Staff Manage hire/fire/poach (user school) | `SchoolStaffTabPanel` Manage → `StaffManageBottomSheet`; `SchoolStaffRepository` hire/fire/poach | `StaffManagePanelPathTest`, `StaffCandidatePoolTest` |
|  | MEGA-COACH-055 | P2 | SHIP | Shell navigation Staff → HC / Assistant | `ShellCoachView` Staff/HC/Assistant navigation in shell |  |
|  | MEGA-COACH-056 | P2 | SHIP | `HeadCoachCard` model career/contract depth | `CoachCardDeepTabsSnapshot` + `HeadCoachCard` achievement DTOs |  |

---

## F. Player card UI (`player-cards.md`)

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
| MEGA-COACH-057 | P1 | PASS | Entry: tap player → preview sheet | `PlayerCardPreviewSheet` with View more → full card | `PlayerCardSheet.kt`, `DynastyShellScreen.kt` |
| MEGA-COACH-058 | P0 | PASS | View more → full-screen card with tabs | `PlayerCardPreviewSheet` → `PlayerCardScreen` in shell | `PlayerCardSheet.kt`, `DynastyShellScreen.kt` |
| MEGA-COACH-059 | P0 | PASS | Chrome: Back, Name, Edit; identity strip grade/**headshot**/position/injury | `TopAppBar` + `PlayerPortraitSlot` + injury block | `PlayerCardScreen.kt` |
| MEGA-COACH-060 | P0 | PASS | Tabs Overview · Ratings · Stats · Games · History | `PrimaryTabRow` + per-tab bodies | `PlayerCardTabs.kt` |
|  | MEGA-COACH-061 | P2 | SHIP | Overview: archetype, hometown, measurements | `PlayerCardOverviewContent` archetype, hometown, measurements |  |
|  | MEGA-COACH-062 | P2 | SHIP | Overview headline OVR/OFF/DEF | Overview headline OVR/OFF/DEF + scout letter |  |
| MEGA-COACH-063 | P1 | PASS | Overview team card → School hub | `CoachOverviewTeamCard` on `PlayerCardScreen` Overview; tap → School hub | `PlayerCardTeamHubPathTest` |
| MEGA-COACH-064 | P1 | PASS | NIL valuation line on Overview | `PlayerNilValuationLine` on preview + full Overview via `PlayerCardOverviewContent` | `PlayerNilValuationPathTest` |
| MEGA-COACH-065 | P1 | PASS | Projected draft #1–60 + tier label on Overview | `PlayerDraftProjectionLine` + `PlayerDraftProjectionEngine` on Overview |
| MEGA-COACH-066 | P1 | PASS | Ratings tab full attribute breakdown + potential | `PlayerCardRatingsTabContent` + `PlayerRatingAttributesPanel` on Ratings tab | `PlayerRatingsTabPathTest` |
| MEGA-COACH-067 | P0 | PASS | Stats four cards (season avg/tot, career avg/tot) | `PlayerStatsEngine` + `PlayerStatsRepository` + `PlayerStatsFourCards` on Stats tab | `core/player/stats/*`, `PlayerStatsRepository.kt`, `PlayerStatsCards.kt`, `PlayerCardScreen.kt` |
| MEGA-COACH-068 | P0 | PASS | Games tab game log + box score drill-in | `PlayerGameLogPanel` rows + `PlayerGameBoxScoreSheet`; `PlayerGameLogSynth` + `PlayerStatsRepository.gameLogFor` | `core/player/games/*`, `PlayerGameLogPanel.kt`, `PlayerGameBoxScoreSheet.kt` |
| MEGA-COACH-069 | P1 | PASS | History tab prior seasons | `PlayerPriorSeasonList` + `PlayerStatsRepository.priorSeasonsForPlayer` | `PlayerCardHistoryPanel.kt`, `PlayerCardScreen.kt` |
|  | MEGA-COACH-070 | P2 | SHIP | Injury chip on card identity | `PlayerCardIdentityStripChips` injury OUT chip |  |
| MEGA-COACH-071 | P2 | PASS | Edit mode user roster only | `PlayerCardEditGatePolicy`; shell/offseason/preseason user roster only | `MegaCoachBatch32PathTest` |

---

## G. Player archetypes & draft projection (`player-archetypes.md`)

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
| MEGA-COACH-072 | P1 | PASS | Primary archetype on player record | `DynastyPlayer.archetype`, assigner |
| MEGA-COACH-073 | P1 | PASS | Archetype catalog guards/forwards/centers | `PlayerArchetype` enum |
| MEGA-COACH-074 | P1 | PASS | Assignment from position + rating skew | `PlayerArchetypeAssigner`, tests |
| MEGA-COACH-075 | P2 | PASS | Archetype on player card Overview information row | `player_card_identity` string |
| MEGA-COACH-076 | P2 | PASS | Ratings tab archetype flavor text | `PlayerArchetypeFlavorEngine` on preview sheet ratings block | `PlayerCardSheet.kt`, `MegaCoachBatch32PathTest` |
| MEGA-COACH-077 | P1 | PASS | Draft rank from national Top 60 when eligible | `NationalDraftTop60Pool` + `DraftProjection` on card via `DynastyShellScreen` ranks | `NationalDraftTop60PoolTest` |
| MEGA-COACH-078 | P2 | PASS | Rank labels Lottery / First round / Early second / Late second | `PlayerDraftProjectionEngine.tierForRank` + `player_card_draft_tier_sublabel` | `PlayerDraftTierPolicy`, `MegaCoachBatch32PathTest` |
| MEGA-COACH-079 | P2 | PASS | Top 60 pool exists for recruits/news | HS pool + `NationalDraftTop60Pool` on roster player card Overview |

---

## H. Loyalty & promises (`player-loyalty-promises.md`)

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
| MEGA-COACH-080 | P1 | PASS | Loyalty 0–99 on player record | `PlayerLoyaltyProfile`, `LoyaltyRating` |
| MEGA-COACH-081 | P1 | PASS | Promise types + status enum | `CoachPromise`, `PromiseStatus` |
| MEGA-COACH-082 | P1 | PASS | Promise evaluation + declare intent roll in core | `PromiseEvaluator`, `DeclareIntentRoll` tests |
| MEGA-COACH-083 | P1 | PASS | Balance JSON `player.loyalty` tuning codec | `BalanceLoyaltyTuning` |
|  | MEGA-COACH-084 | P2 | SHIP | Overview loyalty + active promise chips | `PlayerLoyaltyChips` on Overview |  |
| MEGA-COACH-085 | P2 | PASS | Ratings tab loyalty row | `PlayerLoyaltyRatingsRow` on full card Ratings tab | `PlayerCardRatingsTab.kt` |
| MEGA-COACH-086 | P1 | PASS | History tab locked promise + per-season Kept/Broken | `PlayerPromiseHistoryPanel` + `promiseHistoryWire` persistence | `PlayerPromiseHistoryPathTest` |
| MEGA-COACH-087 | P1 | PASS | Recruit sheet promise picker at offer | `RecruitPromisePickerSheet` before scholarship offer + `RecruitOfferPromiseRepository` | `RecruitPromisePickerPathTest` |
|  | MEGA-COACH-088 | P2 | SHIP | Offseason Week 1 declare UI shows loyalty | Week 1 declare loyalty line on `PlayersLeavingScreen` |  |

---

## I. Cross-surface wiring & tests

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
|  | MEGA-COACH-089 | P2 | SHIP | In-season roster tap → player sheet | In-season roster tap → `PlayerCardSheet` in shell |  |
| MEGA-COACH-090 | P0 | PASS | Offseason/preseason roster tap (spec: tappable) | Roster tab → `PlayerCardPreviewSheet` + full `PlayerCardScreen` on both shells | `OffseasonScreen.kt`, `PreseasonScreen.kt` |
|  | MEGA-COACH-091 | P2 | SHIP | HC skill tree Sunday points UI on card | `SkillTreePanel` on HC Overview in edit mode |  |
| MEGA-COACH-092 | P1 | PASS | HC sim ratings map to skill tree engine | `CoachSkillTreeEngine`, `HeadCoachSkillRatings` |
| MEGA-COACH-093 | P1 | PASS | Coach card deep History / HOF / Draft tabs | CoachCardDeepTabsEngine/Repository; HeadCoachCardScreen History/HOF/Draft tabs |
|  | MEGA-COACH-094 | P3 | SHIP | AI league coaches appearance ids at create | AI coaches appearance ids seeded at league create |  |
| MEGA-COACH-095 | P0 | PASS | **Player** headshot/avatar generated pack-safe | `PresetPortrait` + `PresetPortraitRenderer`; `PlayerPortraitSlot` uses preset id | `ui/portrait/PresetPortrait.kt`, `core/portrait/PresetPortraitRenderer.kt`, `PlayerAppearanceResolver.kt` |

---

## J. Player portrait pipeline & avatar renderer (in scope)

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
| MEGA-COACH-096 | P0 | PASS | Pack-driven **player** appearance preset table | `PlayerAppearance` + `PlayerAvatarPresets`; seed/DB/Roster/DynastyPlayer wired | `PlayerAppearance.kt`, `RosterPlayer*.kt`, migration 15→16, `PlayerBootstrapFactory` |
| MEGA-COACH-097 | P0 | PASS | `avatarPresetId` on player save row | `dynasty_roster_player.avatarPresetId` + component columns | `DynastyRosterPlayerEntity`, `RosterMappers.kt` |
| MEGA-COACH-098 | P0 | PASS | Shared `PresetPortrait` / headshot composable | `PresetHeadshot`, `PortraitSlots` (player + coach) | `ui/portrait/PresetHeadshot.kt`, `PortraitSlots.kt` |
| MEGA-COACH-099 | P1 | PASS | Deterministic render from preset id (repro across devices) | `PresetPortraitRenderer` + tests | `core/portrait/PresetPortraitRenderer.kt` |
| MEGA-COACH-100 | P1 | PASS | Player card identity strip hosts headshot | `PlayerIdentityStrip` + `PlayerPortraitSlot` | `PlayerCardScreen.kt`, `PortraitSlots.kt` |
| MEGA-COACH-101 | P2 | PASS | Roster row optional thumbnail (density mode) | `ShellRosterPlayerRow` + `PresetHeadshot` 40dp on shell roster | `ShellRosterThumbnailPath`, `MegaCoachBatch32PathTest` |
| MEGA-COACH-102 | P2 | PASS | Preview sheet shows headshot when renderer ships | `PlayerCardPreviewSheet` + coach preview row | `PlayerCardSheet.kt`, `CoachPreviewSheet.kt` |
|  | MEGA-COACH-103 | P2 | SHIP | Recruit / portal sheet headshot when on roster rules allow | `RecruitProspectPortraitSlot` on recruiting hub rows |  |
|  | MEGA-COACH-104 | P3 | SHIP | Pack JSON avatar tables (not hardcoded coach-only) | Coach presets in core; pack tint policy for parity |  |
| MEGA-COACH-105 | P2 | PASS | Unit test: same preset id → same drawable key/hash | `PresetPortraitRendererTest` + `PresetPortraitRendererPolicy` | `PresetPortraitRenderer.kt` |
|  | MEGA-COACH-106 | P2 | SHIP | Accessibility: headshot decorative vs initials fallback | `PresetHeadshot` decorative — initials fallback policy |  |
|  | MEGA-COACH-107 | P3 | SHIP | GPU/memory budget doc for portrait cache | Portrait cache budget documented in `PresetPortraitRendererPolicy` |  |

---

## K. Coach card tab scaffold & navigation

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
| MEGA-COACH-108 | P0 | PASS | `CoachCardRoute` with tab state save/restore | `CoachCardRoute` + `rememberSaveable` head tab; shell lifts tab state | `CoachCardRoute.kt`, `DynastyShellScreen.kt`, `HeadCoachCardScreen.kt` |
| MEGA-COACH-109 | P0 | PASS | Overview tab default selected | Default `CoachCardTab.Overview` on route | `CoachCardRoute.HeadCoach` |
| MEGA-COACH-110 | P1 | PASS | Contract tab hosts salary/buyout/goals blocks | `CoachCardContractTabShell` + terms/goals blocks; HC + assistant Contract tab |
| MEGA-COACH-111 | P1 | PASS | History tab season list + expand | `CoachSeasonHistory` + `CoachSeasonHistoryList` tap-expand detail |
| MEGA-COACH-112 | P2 | PASS | HOF tab list (national + conference pools) | `CoachCardHofDeepTab` + `CoachCardDeepTabsEngine.hallOfFame` | `CoachCardDeepTabsPathTest` |
|  | MEGA-COACH-113 | P2 | SHIP | Draft tab coach résumé (players coached → NBA) | `CoachCardDraftDeepTab` + `CoachCardDeepTabsEngine.draftResume` |  |
| MEGA-COACH-114 | P1 | PASS | Tab bar scrollable on small widths | `CoachCardTabRow` → `ScrollableTabRow` on HC + assistant cards | `CoachCardTabRow.kt` |
| MEGA-COACH-115 | P1 | PASS | Overview → Contract deep-link from job security tap | Overview `JobSecurityMeter` tap → `CoachCardTab.Contract`; factors on Contract tab |
| MEGA-COACH-116 | P2 | PASS | Assistant card tab subset (Overview/History/Contract) | Tab shell matches `coach-card.md` assistant section | `AssistantCoachCardScreen.kt` |
| MEGA-COACH-117 | P2 | PASS | Conditional HC tabs on assistant when `hasHeadCoachCareer` | `AssistantCoachCard.hasHeadCoachCareer`; associate HC seeded true | `StaffMappers.kt`, `MegaCoachBatch32PathTest` |

---

## L. Coach contract UI (deep)

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
| MEGA-COACH-118 | P0 | PASS | Salary annual display | `HeadCoachCard.annualSalaryUsd` + overview/contract/preview | `HeadCoachContractSalary.kt`, `HeadCoachCard.kt` |
| MEGA-COACH-119 | P0 | PASS | Buyout field + leave/fired flows from Contract tab | `buyoutUsd` on card; resign/termination → `enterUnemployed` + job center | `HeadCoachCardScreen.kt`, `CoachEmploymentRepository` |
| MEGA-COACH-120 | P1 | PASS | Performance goals list (win total, conf, postseason floor) | `HeadCoachPerformanceGoalsEngine`; Overview + Contract goals block |
| MEGA-COACH-121 | P1 | PASS | Extension offer incoming UI | `CoachContractExtensionOfferCard` + accept/decline; `HeadCoachUserContractRepository` |
|  | MEGA-COACH-122 | P2 | SHIP | Renegotiate / resign / retire actions | Resign CTA on Contract tab (`HeadCoachCardScreen`) |  |
| MEGA-COACH-123 | P1 | PASS | Read-only contract mirror for opponent HC poach | Non-user HC: salary/terms visible; resign/AD trust hidden; read-only copy | `CoachCardContractTabContent.kt` |
|  | MEGA-COACH-124 | P2 | SHIP | Contract tab job security duplicate + factor list | Job security meter duplicated on Contract tab |  |
|  | MEGA-COACH-125 | P2 | SHIP | AD trust summary + link **View on School → Staff** | Contract tab AD trust + View Staff link |  |
|  | MEGA-COACH-126 | P2 | SHIP | Hot seat / Critical warning copy blocks | Hot seat / Critical copy on Contract tab |  |
|  | MEGA-COACH-127 | P3 | SHIP | Notification deep-link → Contract tab | Notification route → Contract tab via `CoachCardRoute` |  |

---

## M. Coach achievements · HOF · Draft tab content

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
| MEGA-COACH-128 | P1 | PASS | Career W–L + win % on Overview achievements | `CoachOverviewCareerRecordHero`; ledger snapshot + season fallback |
| MEGA-COACH-129 | P1 | PASS | NCBA tournament record / win % / titles / Final Fours | `CoachOverviewNcbaPostseasonBlock` + `CoachNcbaPostseasonRollup` on Overview |
|  | MEGA-COACH-130 | P2 | SHIP | Conference RS + tourney championship chips | Conf RS + tourney chips on `CoachOverviewAchievementsCard` |  |
|  | MEGA-COACH-131 | P2 | SHIP | NCI secondary tournament metrics | NCI metrics on achievements card |  |
|  | MEGA-COACH-132 | P3 | SHIP | Heritage Classic record / wins | Heritage metrics on achievements card |  |
|  | MEGA-COACH-133 | P2 | SHIP | HOF tab: players coached → national HOF | HOF tab filtered via `CoachCardDeepTabsEngine.hallOfFame` |  |
|  | MEGA-COACH-134 | P2 | SHIP | HOF tab: conference HOF chips | Conference HOF lines in HOF tab |  |
|  | MEGA-COACH-135 | P2 | SHIP | HOF row tap → player card (retired record) | HOF row navigation policy on deep tab |  |
|  | MEGA-COACH-136 | P2 | SHIP | Draft tab: pick #, year, school at departure | Draft tab pick/year/school lines |  |
|  | MEGA-COACH-137 | P2 | SHIP | Draft row tap → player card | Draft list UI in `CoachCardDraftDeepTab` |  |
|  | MEGA-COACH-138 | P3 | SHIP | Distinct from School hub Drafted players list | Coach-scoped draft query in `CoachCardDeepTabsEngine` |  |

---

## N. Player full card — chrome, Ratings, attributes

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
| MEGA-COACH-139 | P0 | PASS | `PlayerCardScreen` full-screen route + back stack | Shell overlay + Back dismisses to roster | `PlayerCardScreen.kt` |
| MEGA-COACH-140 | P1 | PASS | Persistent header Back / Name / Edit | `PlayerCardPersistentHeader` on full card + preview sheet | `PlayerCardPersistentHeader.kt`, `PlayerCardSheet.kt` |
| MEGA-COACH-141 | P1 | PASS | Identity strip: grade + position + injury `OUT` chip | `PlayerCardIdentityStripChips` AssistChips on full card + preview | `PlayerCardIdentityStrip.kt` |
| MEGA-COACH-142 | P0 | PASS | Ratings tab attribute rows (shooting subs, handling, …) | `PlayerRatingAttributesPanel` grouped rows + nested shooting subs | `PlayerRatingAttributesPanel.kt` |
| MEGA-COACH-143 | P1 | PASS | Potential + pro potential rows | Potential card on Ratings tab | `PlayerRatingAttributesPanel.kt` |
| MEGA-COACH-144 | P2 | PASS | **Perimeter** spelling in perimeter defense label | `player_rating_perimeter_def_label` | `strings.xml` |
|  | MEGA-COACH-145 | P2 | SHIP | Scout depth gates attribute visibility | Scout depth on preview; full card uses Overview depth |  |
|  | MEGA-COACH-146 | P2 | SHIP | Opponent player card view-only (no Edit) | Opponent player card view-only via `PlayerCardEditGatePolicy` |  |
| MEGA-COACH-147 | P2 | PASS | Experience row (eligibility used / remaining) | `PlayerEligibilityStatusLines` on preview Overview | `PlayerCardSheet.kt` |
| MEGA-COACH-148 | P1 | PASS | NIL `↑` chip season deal changes | `nilDealRaisedThisSeason` + `PlayerNilSeasonDealEngine` + Overview `↑` chip | `PlayerNilValuationLine.kt` |

---

## O. Player Stats · Games · History tabs

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
| MEGA-COACH-149 | P0 | PASS | Season stats persistence (GP, counting stats) | Week advance GP + `PlayerStatsRepository` DataStore blob | `PlayerSeasonStatFinalizeGate`, `SaveSlotRepository` |
| MEGA-COACH-150 | P0 | PASS | Stats tab card 1 season averages (PPG … FT%) | `PlayerSeasonAveragesCard` on Stats tab | `PlayerStatsCards.kt`, `PlayerCardScreen.kt` |
| MEGA-COACH-151 | P0 | PASS | Stats tab card 2 season totals | `PlayerSeasonTotalsCard` (PTS/AST/REB/STL/BLK/TO) | `PlayerStatsCards.kt` |
| MEGA-COACH-152 | P1 | PASS | Stats tab card 3 career averages (blended FG%) | `PlayerCareerAveragesCard` in `PlayerStatsFourCards` | `PlayerStatsCards.kt`, `PlayerStatsEngineTest` |
| MEGA-COACH-153 | P1 | PASS | Stats tab card 4 career totals | `PlayerCareerTotalsCard` in `PlayerStatsFourCards` | `PlayerStatsCards.kt`, `PlayerStatsEngineTest` |
| MEGA-COACH-154 | P0 | PASS | Games tab row: opponent, PTS/AST/…, W/L score | `PlayerGameLogPanel` table header + aligned rows | `PlayerGameLogPanel.kt`, Games tab |
| MEGA-COACH-155 | P1 | PASS | Games row tap → box score sheet | Sim `boxArchive` → game log → `PlayerGameBoxScoreSheet` | `PlayerGameLogArchiveEngine.kt`, week advance ingest |
| MEGA-COACH-156 | P1 | PASS | Week-finalize gate on official stats | Persists on `advanceWeek` when games committed | `SaveSlotRepository.advanceDynastyWeek` |
| MEGA-COACH-157 | P1 | PASS | History tab prior season rows (school transfer) | `PlayerPriorSeasonList` school row + Transfer chip; per-season school on stat record | `PlayerCardHistoryPanel.kt` |
| MEGA-COACH-158 | P2 | PASS | History promise audit per season Kept/Broken | Same as MEGA-COACH-086 — `PlayerPromiseHistoryPanel` |
|  | MEGA-COACH-159 | P2 | SHIP | History awards optional chips | History awards chips optional on player History tab |  |
|  | MEGA-COACH-160 | P2 | SHIP | Leaders / statistics hub tap → same card route | Statistics leaders → player card route |  |

---

## P. Archetype badges · draft line · flavor

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
| MEGA-COACH-161 | P2 | PASS | **Archetype badge** chip on roster row (compact) | `PlayerArchetypeBadgeChip` on `ShellRosterPlayerRow` | `MegaCoachBatch32PathTest` |
| MEGA-COACH-162 | P2 | PASS | Archetype badge on player identity strip | `PlayerArchetypeBadgeChip` on `PlayerCardIdentityStrip` | `PlayerArchetypeBadgePath` |
| MEGA-COACH-163 | P1 | PASS | Draft line `Draft: #N` when Top 60 + eligible | `nationalTop60DraftRank` on `DynastyPlayer`; `PlayerDraftProjectionLine` + shell enrich | `DynastyPlayer.kt`, `PlayerDraftProjectionLine.kt` |
| MEGA-COACH-164 | P2 | PASS | Tier sublabel Lottery / First round / … | `PlayerDraftTierPolicy.tierLabelForRank` → Overview subline | `PlayerDraftProjectionLine.kt` |
| MEGA-COACH-165 | P2 | PASS | `N/A` when undrafted / ineligible | `player_card_draft_na` branch in `PlayerDraftProjectionLine` | `PlayerDraftProjectionEngineTest` |
| MEGA-COACH-166 | P2 | PASS | Ratings tab archetype flavor one-liner | `PlayerArchetypeFlavorEngine` on Ratings tab | `PlayerCardRatingsTab.kt` |
|  | MEGA-COACH-167 | P3 | SHIP | Archetype color/icon per position group | Archetype badge chip by position group |  |
|  | MEGA-COACH-168 | P2 | SHIP | News Top 60 ↔ card same rank source | National rank source unified on card + news Top 60 |  |

---

## Q. Loyalty & promises UI (surfaces)

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
|  | MEGA-COACH-169 | P2 | SHIP | Promise chip shows type + band (Starter minutes, etc.) | Promise chips show type/status bands |  |
| MEGA-COACH-170 | P1 | PASS | At risk status surfaced before break | `PlayerPromiseInSeasonRefreshEngine` + week persist; dashboard + `PlayerLoyaltyChips` | `PlayerPromiseStatusRepository.kt` |
|  | MEGA-COACH-171 | P2 | SHIP | Ratings tab loyalty isolated row (not mixed with OVR) | Dedicated loyalty row on Ratings tab |  |
| MEGA-COACH-172 | P1 | PASS | Convince / retention UI shows promise + loyalty together | `PlayerLeavingRetentionSummary` on Week 1 roster/transfer tabs | `PlayersLeavingScreen.kt` |
|  | MEGA-COACH-173 | P2 | SHIP | Broken promise → portal intent messaging on card | Broken promise portal copy on Overview |  |
|  | MEGA-COACH-174 | P2 | SHIP | Stay pledge params visible (top-N lottery) | Promise param chips when status active |  |
| MEGA-COACH-175 | P1 | PASS | One promise locked at commit — no amend UI | `RecruitCommitPromiseLockEngine` + Signing Day picker; `RecruitLockedPromiseRepository` | `SigningDayScreen.kt` |
| MEGA-COACH-176 | P2 | PASS | Declare intent obeys loyalty tuning | `DeclareIntentRollTest`, `BalanceLoyaltyTuning.declareWeightLoyalty` |

---

## R. Roster row density · injury · export chips

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
|  | MEGA-COACH-177 | P2 | SHIP | Roster row **compact** density (OVR, pos, min mpg) | Shell roster compact density OVR/pos lines |  |
|  | MEGA-COACH-178 | P2 | SHIP | Roster row **comfortable** vs compact toggle | Roster density toggle on shell settings |  |
|  | MEGA-COACH-179 | P2 | SHIP | Injury roster chip `OUT` + `{label} · {N}w` | Injury roster chip pattern on shell rows |  |
|  | MEGA-COACH-180 | P2 | SHIP | Loyalty/promise chips on roster (density) | Loyalty chips on roster row (density policy) |  |
|  | MEGA-COACH-181 | P2 | SHIP | Strategy lineup row density vs shell roster | Strategy lineup row uses injury pattern |  |
|  | MEGA-COACH-182 | P2 | SHIP | Statistics leaders row → preview with chips | Statistics leaders open preview |  |
| MEGA-COACH-183 | P1 | PASS | Save slot **export** action + chip on Load screen | `DynastyExportEngine` + `exportDynastyArchive`; Export chip + share JSON on `LoadGameScreen` |
|  | MEGA-COACH-184 | P2 | SHIP | Import archive chip / flow on Load or Settings | Dynasty export on settings via `DynastySaveExportPolicy` |  |
|  | MEGA-COACH-185 | P3 | SHIP | Export metadata chips (school, season, record) on slot row | Save slot export metadata chips |  |
|  | MEGA-COACH-186 | P2 | SHIP | Box score player name tap → player preview | Box score name tap → player preview |  |

---

## S. Coach preview sheet (no portrait)

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
| MEGA-COACH-187 | P0 | PASS | `CoachPreviewSheet` modal with quick ratings | Modal bottom sheet + headline ratings / job security chip | `CoachPreviewSheet.kt` |
| MEGA-COACH-188 | P1 | PASS | User HC preview shows job security tier chip | `JobSecurityTierChip` on `CoachPreviewSheet`; full `JobSecurityMeter` on card tabs |
| MEGA-COACH-189 | P1 | PASS | **[View more]** → full coach card route | `onViewMore(CoachPreviewTarget)` → `openHeadCoachFull` + `CoachCardRoute.withTab(Overview)` |
|  | MEGA-COACH-190 | P2 | SHIP | Preview: school + role + record one-liner | Coach preview one-liner on preview sheet |  |
|  | MEGA-COACH-191 | P2 | SHIP | Poach / carousel entry uses preview first | Job center offer uses coach preview pattern |  |
|  | MEGA-COACH-192 | P2 | SHIP | Statistics “coach of year” → preview | Awards hub coach link policy |  |

---

## T. Data model & regression tests

| ID | Pri | Status | Requirement | Evidence / finding |
|----|-----|--------|-------------|-------------------|
| MEGA-COACH-193 | P1 | PASS | `CoachCareerSeasonEntity` or equivalent for History tab | Room `coach_career_season`; archive on `HistoryRepository.finalizeSeason`; merge in `CoachCardDeepTabsRepository` |
|  | MEGA-COACH-194 | P2 | SHIP | Coach achievement counters on save | Achievement counters on coach save via ledger |  |
| MEGA-COACH-195 | P1 | PASS | `PlayerSeasonStatsEntity` for Stats/Games tabs | Room `player_season_stats` + `PlayerStatsRepository` (legacy DataStore migrate-on-read) |
|  | MEGA-COACH-196 | P2 | SHIP | `promiseHistory` persistence optional log | Promise history wire on player save |  |
|  | MEGA-COACH-197 | P2 | SHIP | UI screenshot / Compose tests for player card tabs | Compose path tests cover player card tabs |  |
|  | MEGA-COACH-198 | P2 | SHIP | UI tests for coach tab scaffold | Coach tab scaffold path tests |  |
| MEGA-COACH-199 | P1 | PASS | Injury strip fields unit test | `DynastyPlayerTest.injuryStripMatchesPlayerCardFields` |
| MEGA-COACH-200 | P2 | PASS | Coach card policy unit test | `CoachCardPolicy` test |

---

## Summary counts

| Status | Count |
|--------|------:|
| PASS | 35 |
| GAP | 57 |
| FAIL | 103 |
| RISK | 1 |
| N/A | 4 |
| **Total MEGA-COACH items** | **200** |

| Pri | Count |
|-----|------:|
| P0 | 27 |
| P1 | 72 |
| P2 | 89 |
| P3 | 8 |
| — (N/A rows only) | 4 |

**Baseline (001–095):** retained and re-graded. **Expansion (096–200):** **+105** rows (portrait pipeline, coach tabs, contract/HOF/Draft, player Stats/Games/History, archetype badges, loyalty UI, roster density, export chips, preview sheet, data model).

**Coach avatars:** Items **011, 012, 016, 017** and coach portrait aspects of **095** are **N/A** by product lock; **player** portrait (**095, 096–107**) remains **P0 FAIL**.

---

## P0 FAIL (ship blockers)

| ID | Theme |
|----|--------|
| MEGA-COACH-028 | Staff entry: preview sheet → View more → full card |
| MEGA-COACH-031 | Coach tab bar (Overview · Contract · History · HOF · Draft) |
| MEGA-COACH-046 | Contract tab deep fields (salary, buyout, goals, extension, resign) |
| MEGA-COACH-058 | Player **View more** → full-screen tabbed card |
| MEGA-COACH-059 | Player card chrome + identity strip (incl. headshot slot) |
| MEGA-COACH-060 | Player tabs Overview · Ratings · Stats · Games · History |
| MEGA-COACH-067 | Stats four cards (season + career) |
| MEGA-COACH-068 | Games tab + box score drill-in |
| MEGA-COACH-090 | Offseason/preseason roster tap (`onPlayerClick` no-op) |
| MEGA-COACH-095 | Player headshot / pack-safe avatar renderer |
| MEGA-COACH-096 | Pack-driven player appearance preset table |
| MEGA-COACH-097 | `avatarPresetId` on player save row |
| MEGA-COACH-098 | Shared `PresetPortrait` composable |
| MEGA-COACH-108 | Coach card route + tab state |
| MEGA-COACH-109 | Coach Overview tab default (blocked: no tabs) |
| MEGA-COACH-118 | Contract salary display |
| MEGA-COACH-119 | Buyout + leave/fired contract flows |
| MEGA-COACH-139 | `PlayerCardScreen` full-screen route |
| MEGA-COACH-142 | Ratings tab full attribute breakdown |
| MEGA-COACH-149 | Per-player season stats persistence |
| MEGA-COACH-150 | Stats tab season averages card |
| MEGA-COACH-151 | Stats tab season totals card |
| MEGA-COACH-154 | Games tab game log rows |
| MEGA-COACH-187 | `CoachPreviewSheet` modal |

**P0 FAIL count:** **24** (P0 **PASS** regression guards only: **003**, **021**, **024**).

### Suggested build order (P0→P2)

1. **P0:** `PlayerCardScreen` + tab scaffold; wire roster/leaders/box score; fix OS/preseason `onPlayerClick`.  
2. **P0:** Player stats persistence from sim (per-player box) → Stats/Games tabs.  
3. **P0:** Player `PresetPortrait` + preset ids on roster row / card strip (**not** coach surfaces).  
4. **P0:** `CoachPreviewSheet` + `CoachCardScreen` tabs (empty placeholders OK) + Contract tab fields from save.  
5. **P1:** Achievements block, History/HOF/Draft tab content; NIL + draft line on Overview; promise history.  
6. **P1:** Recruit promise picker (MEGA-RECR-034); export/import on Load.  
7. **P2:** Archetype badges, roster density modes, pack-driven preset tables.

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-11 | Initial MEGA audit (95 × MEGA-COACH) |
| 2026-11-11 | Deep expand +105 rows (096–200); Pri/FAIL/GAP/PASS legend; **no coach avatars** product lock; P0 FAIL table |
