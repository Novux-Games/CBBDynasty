# MEGA audit: coach card · player card · avatars · new-game

**Scope:** `planning/coach-card.md`, `planning/player-cards.md`, `planning/player-archetypes.md`, `planning/player-loyalty-promises.md`, `planning/game-start.md` (coach appearance) vs Android `ui/coach`, `ui/player`, `ui/newgame`, `:core/coach`, `:core/player`, `:database` coach row.  
**Audit date:** 2026-11-11  
**Method:** Full planning-doc read + static trace of UI/core/save paths (no emulator portrait render).

## Status legend

| Status | Meaning |
|--------|---------|
| **DONE** | Matches spec on shipped path |
| **PARTIAL** | Implemented but incomplete vs planning |
| **STUB** | Placeholder / minimal shell only |
| **WRONG** | Present but contradicts spec |
| **MISSING** | Not found in codebase |

---

## A. Coach avatar presets & persistence (game-start · identity)

| ID | Status | Requirement | Evidence / finding |
|----|--------|-------------|-------------------|
| MEGA-COACH-001 | DONE | Wizard Step 1: first / last name text fields | `CoachInfoStepContent.kt`, `CoachCreateProfile` |
| MEGA-COACH-002 | DONE | Name suffix dropdown None / Jr / II / III / IV | `SuffixDropdown`, `CoachNameSuffix` |
| MEGA-COACH-003 | DONE | Age 28–65 band + validation | `MIN_AGE`/`MAX_AGE`, `CoachCreateValidator` |
| MEGA-COACH-004 | DONE | Alma mater searchable pack schools + None | `AlmaMaterPicker` |
| MEGA-COACH-005 | DONE | Display name `First [Suffix] Last` | `CoachCreateProfile.displayName()`, unit test |
| MEGA-COACH-006 | PARTIAL | Appearance: **pack-driven** preset swatches | Hardcoded `CoachAvatarPresets` (5×5×4×4); comment defers pack |
| MEGA-COACH-007 | DONE | Skin tone preset picker | `PresetChipRow` + `skinTones` |
| MEGA-COACH-008 | DONE | Hair color preset picker | `hairColors` chips |
| MEGA-COACH-009 | DONE | Hair style preset picker incl. bald | `hairStyles` |
| MEGA-COACH-010 | DONE | Beard style incl. None | `beardStyles` |
| MEGA-COACH-011 | MISSING | Visual **swatches** (color chips) vs text labels | `FilterChip` shows string labels only |
| MEGA-COACH-012 | MISSING | Live **portrait preview** while picking appearance | Only name text preview (`new_coach_display_preview`) |
| MEGA-COACH-013 | DONE | `avatarPresetId` composite key | `coach-$skin-$hair-$style-$beard` |
| MEGA-COACH-014 | DONE | Component ids persisted on save | `DynastyCoachEntity` skin/hair/style/beard + `avatarPresetId` |
| MEGA-COACH-015 | DONE | Invalid avatar ids rejected at wizard Next | `CoachCreateValidationError.InvalidAvatar` |
| MEGA-COACH-016 | MISSING | Portrait renderer from preset id | No `Portrait`/`AvatarView` anywhere in `android/` |
| MEGA-COACH-017 | MISSING | Coach card identity strip shows portrait | `HeadCoachCardScreen` has no avatar field |
| MEGA-COACH-018 | MISSING | Coach preview modal quick info + portrait | No `CoachPreview`; shell jumps to full screen |
| MEGA-COACH-019 | PARTIAL | Same preset pipeline as **players** | Coach presets exist; **no** player appearance presets in UI |
| MEGA-COACH-020 | DONE | Unit test coach create validation | `CoachCreateValidatorTest` (name/suffix; not avatar matrix) |

---

## B. New-game wizard (coach-adjacent steps)

| ID | Status | Requirement | Evidence / finding |
|----|--------|-------------|-------------------|
| MEGA-COACH-021 | DONE | Steps 1–6 Coach → Archetype → Schemes → Settings → School → Sign | `NewGameStep` enum |
| MEGA-COACH-022 | DONE | Step 2 coach archetype radio list | `ArchetypeStepContent`, `CoachArchetype` |
| MEGA-COACH-023 | DONE | Step 3 O/D scheme chip pickers (7 presets) | `SchemesStepContent`, strategy enums |
| MEGA-COACH-024 | DONE | Draft held until `signNewGameFromWizard` | `NewGameWizardScreen` + `NewGameDraft` |
| MEGA-COACH-025 | DONE | Coach row written with scheme + archetype wires | `DynastyCoachEntity`, `DynastyCoachMappers` |
| MEGA-COACH-026 | PARTIAL | Archetype → starting skill tree bias | `CoachArchetype` flavor; Sunday spend wiring separate |
| MEGA-COACH-027 | MISSING | Wizard step shows archetype **portrait** or avatar recap on sign step | `SignContractStepContent` — text only (not re-audited line-by-line) |

---

## C. Coach card entry flow & chrome

| ID | Status | Requirement | Evidence / finding |
|----|--------|-------------|-------------------|
| MEGA-COACH-028 | MISSING | Tap staff → **preview sheet** → View more → full card | `DynastyShellScreen` → `HeadCoachCardScreen` direct |
| MEGA-COACH-029 | STUB | Full card: persistent Back / Name / Edit chrome | Single scroll column; no tab bar, no Edit |
| MEGA-COACH-030 | MISSING | Identity strip: portrait, school, role under title | Text-only headline block |
| MEGA-COACH-031 | MISSING | Tab bar: Overview · Contract · History · HOF · Draft | No tabs in coach UI |
| MEGA-COACH-032 | PARTIAL | Overview: school + season record | `seasonRecordLabel`, school name on HC screen |
| MEGA-COACH-033 | MISSING | Overview team card tap → School hub | No clickable team card on coach screen |
| MEGA-COACH-034 | PARTIAL | Overview information: age, alma mater | Age + alma on HC; **no** career/school experience seasons |
| MEGA-COACH-035 | MISSING | Contract overview on Overview (years, years left, prestige) | Only `contractLengthYears` / `yearsLeft`; no prestige row |
| MEGA-COACH-036 | MISSING | Overview achievements block (W–L, tourney, titles, NCI, Heritage) | No achievement model/UI |
| MEGA-COACH-037 | PARTIAL | Overview strategy default O/D schemes | HC screen shows scheme labels |
| MEGA-COACH-038 | DONE | Headline HC ratings O/D/Training/Recruiting 0–99 | `HeadCoachRatings`, `RatingCard` |
| MEGA-COACH-039 | PARTIAL | Edit mode user HC (scheme, skill spend, contract) | Skill tree panel on HC screen; no gated Edit affordance |
| MEGA-COACH-040 | MISSING | Opponent / AI coach cards hide job security | `JobSecurityMeter` only if `userCoachSecurity` passed — caller must gate |

---

## D. Job security · AD trust · contract

| ID | Status | Requirement | Evidence / finding |
|----|--------|-------------|-------------------|
| MEGA-COACH-041 | PARTIAL | Job security meter 0–100 + tier + trend | `JobSecurityMeter`, `CoachSecuritySnapshot` |
| MEGA-COACH-042 | DONE | Factor list helping/hurting chips | `JobSecurityFactorContribution` in UI |
| MEGA-COACH-043 | DONE | Core evaluator + tiers | `CoachSecurityEvaluator`, `JobSecurityTier` tests |
| MEGA-COACH-044 | PARTIAL | Job security on Contract tab + AD trust summary link | Meter on HC overview only; AD on staff tab |
| MEGA-COACH-045 | PARTIAL | AD trust meter on School Staff tab (user school) | `AdTrustMeter` in `SchoolStaffTabPanel` |
| MEGA-COACH-046 | MISSING | Contract tab deep fields (salary, buyout, goals, extension, resign) | No contract tab UI |
| MEGA-COACH-047 | STUB | Job Center carousel offers UI | `JobCenterScreen` list + accept (no coach card contract tie-in) |
| MEGA-COACH-048 | MISSING | Balance bundle `coach.jobSecurityWeights` in runtime | Weights in evaluator — verify JSON load in separate audit |

---

## E. Assistant coach card & staff

| ID | Status | Requirement | Evidence / finding |
|----|--------|-------------|-------------------|
| MEGA-COACH-049 | PARTIAL | Assistant card Overview ratings Rec/Dev/Scout | `AssistantCoachCardScreen` |
| MEGA-COACH-050 | MISSING | Assistant tabs History · Contract (HC conditional Achievements/HOF/Draft) | Single scroll, no tabs |
| MEGA-COACH-051 | DONE | Support staff rows not tappable / no card | `SchoolStaffTabPanel` `clickable = false` |
| MEGA-COACH-052 | DONE | Policy: coach-track eligible for card | `CoachCardPolicy`, test |
| MEGA-COACH-053 | MISSING | Staff grade aggregate chip | No staff grade on staff panel |
| MEGA-COACH-054 | MISSING | Staff Manage hire/fire/poach (user school) | Staff panel read-only list |
| MEGA-COACH-055 | PARTIAL | Shell navigation Staff → HC / Assistant | `ShellCoachView` in `DynastyShellScreen` |
| MEGA-COACH-056 | STUB | `HeadCoachCard` model missing avatar + portrait ids | Data class has no appearance fields for card UI |

---

## F. Player card UI (`player-cards.md`)

| ID | Status | Requirement | Evidence / finding |
|----|--------|-------------|-------------------|
| MEGA-COACH-057 | PARTIAL | Entry: tap player → preview sheet | `PlayerCardSheet` ModalBottomSheet |
| MEGA-COACH-058 | MISSING | View more → full-screen card with tabs | Sheet is overview-only; no full route |
| MEGA-COACH-059 | MISSING | Chrome: Back, Name, Edit; identity strip grade/headshot/position/injury | Title + text lines only |
| MEGA-COACH-060 | MISSING | Tabs Overview · Ratings · Stats · Games · History | All content in one column |
| MEGA-COACH-061 | PARTIAL | Overview: archetype, hometown, measurements | `PlayerCardOverviewContent` |
| MEGA-COACH-062 | PARTIAL | Overview headline OVR/OFF/DEF | Shown; scout OVR letter also |
| MEGA-COACH-063 | MISSING | Overview team card → School hub | No team card block |
| MEGA-COACH-064 | MISSING | NIL valuation line on Overview | Not on `PlayerCardSheet` |
| MEGA-COACH-065 | MISSING | Projected draft #1–60 + tier label on Overview | No draft projection UI on card |
| MEGA-COACH-066 | MISSING | Ratings tab full attribute breakdown + potential | Only headline triplet + scout |
| MEGA-COACH-067 | MISSING | Stats four cards (season avg/tot, career avg/tot) | No stats section |
| MEGA-COACH-068 | MISSING | Games tab game log + box score drill-in | Not implemented |
| MEGA-COACH-069 | MISSING | History tab prior seasons | Not implemented |
| MEGA-COACH-070 | PARTIAL | Injury chip on card identity | `PlayerInjuryOverviewBlock` |
| MEGA-COACH-071 | MISSING | Edit mode user roster only | No Edit on sheet |

---

## G. Player archetypes & draft projection (`player-archetypes.md`)

| ID | Status | Requirement | Evidence / finding |
|----|--------|-------------|-------------------|
| MEGA-COACH-072 | DONE | Primary archetype on player record | `DynastyPlayer.archetype`, assigner |
| MEGA-COACH-073 | DONE | Archetype catalog guards/forwards/centers | `PlayerArchetype` enum |
| MEGA-COACH-074 | DONE | Assignment from position + rating skew | `PlayerArchetypeAssigner`, tests |
| MEGA-COACH-075 | DONE | Archetype on player card Overview information row | `player_card_identity` string |
| MEGA-COACH-076 | MISSING | Ratings tab archetype flavor text | Not in `PlayerCardSheet` |
| MEGA-COACH-077 | MISSING | Draft rank from national Top 60 when eligible | No `DraftProjection` on `DynastyPlayer` card path |
| MEGA-COACH-078 | MISSING | Rank labels Lottery / First round / Early second / Late second | No UI |
| MEGA-COACH-079 | PARTIAL | Top 60 pool exists for recruits/news | `Top60NewsPublisher`, seeder — not wired to roster player card |

---

## H. Loyalty & promises (`player-loyalty-promises.md`)

| ID | Status | Requirement | Evidence / finding |
|----|--------|-------------|-------------------|
| MEGA-COACH-080 | DONE | Loyalty 0–99 on player record | `PlayerLoyaltyProfile`, `LoyaltyRating` |
| MEGA-COACH-081 | DONE | Promise types + status enum | `CoachPromise`, `PromiseStatus` |
| MEGA-COACH-082 | DONE | Promise evaluation + declare intent roll in core | `PromiseEvaluator`, `DeclareIntentRoll` tests |
| MEGA-COACH-083 | DONE | Balance JSON `player.loyalty` tuning codec | `BalanceLoyaltyTuning` |
| MEGA-COACH-084 | PARTIAL | Overview loyalty + active promise chips | `PlayerLoyaltyChips` |
| MEGA-COACH-085 | PARTIAL | Ratings tab loyalty row | `PlayerLoyaltyRatingsRow` |
| MEGA-COACH-086 | MISSING | History tab locked promise + per-season Kept/Broken | No history UI |
| MEGA-COACH-087 | MISSING | Recruit sheet promise picker at offer | Recruiting UI gap (cross-ref RECR audit) |
| MEGA-COACH-088 | PARTIAL | Offseason Week 1 declare UI shows loyalty | `PlayersLeavingScreen` loyalty line |

---

## I. Cross-surface wiring & tests

| ID | Status | Requirement | Evidence / finding |
|----|--------|-------------|-------------------|
| MEGA-COACH-089 | PARTIAL | In-season roster tap → player sheet | `DynastyShellScreen` + `PlayerCardSheet` |
| MEGA-COACH-090 | WRONG | Offseason/preseason roster tap (spec: tappable) | `onPlayerClick = { }` on some screens per frame audit |
| MEGA-COACH-091 | PARTIAL | HC skill tree Sunday points UI on card | `SkillTreePanel` when state passed |
| MEGA-COACH-092 | DONE | HC sim ratings map to skill tree engine | `CoachSkillTreeEngine`, `HeadCoachSkillRatings` |
| MEGA-COACH-093 | MISSING | Coach card deep History / HOF / Draft tabs | No entities or UI |
| MEGA-COACH-094 | PARTIAL | AI league coaches appearance at create | Avatar ids on user coach only in DB; AI staff names generated without avatar store |
| MEGA-COACH-095 | STUB | Player headshot/avatar generated pack-safe | Spec requires; zero render pipeline for players or coaches |

---

## Summary counts

| Status | Count |
|--------|------:|
| DONE | 28 |
| PARTIAL | 24 |
| STUB | 3 |
| WRONG | 1 |
| MISSING | 39 |
| **Total MEGA-COACH items** | **95** |

**Avatar / preset focus:** Persistence and wizard pickers are **DONE** (MEGA-COACH-007–015); visual portrait pipeline is **MISSING** end-to-end (MEGA-COACH-011–012, 016–018, 095). Coach card UX is largely **STUB** vs `coach-card.md` tab/chrome spec (MEGA-COACH-028–031). Player card is **PARTIAL** overview-only vs five-tab spec (MEGA-COACH-057–071).

---

## Suggested build order (P0→P2)

1. **P0:** Shared `PresetPortrait` composable + coach card / preview identity strip (unblocks MEGA-COACH-012, 016–018, 095).  
2. **P0:** Coach preview sheet + tab scaffold (even empty Contract/History) to match entry flow.  
3. **P1:** Player full card route + Ratings/Stats/Games/History tabs; draft + NIL lines on Overview.  
4. **P1:** Job security only on user HC; contract tab fields from save.  
5. **P2:** Pack-driven avatar tables; recruit promise picker; achievements/HOF/Draft coach tabs.
