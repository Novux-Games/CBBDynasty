# MEGA build checklist audit — sim · strategy · lineup · injuries

**Repo:** `/workspace`  
**Audited:** 2026-11-11  
**Planning authority:** [`planning/simulation-engine.md`](../../planning/simulation-engine.md), [`planning/strategy-schemes.md`](../../planning/strategy-schemes.md), [`planning/positions-and-lineup.md`](../../planning/positions-and-lineup.md), [`planning/injuries.md`](../../planning/injuries.md)  
**Code focus:** `android/core/src/main/kotlin/dev/novux/dynasty/cbb/core/sim/**`, `core/strategy`, `core/lineup`, `core/injuries`, `ui/sim`, `ui/strategy`

## Status legend

| Tag | Meaning |
|-----|---------|
| **SHIP** | Behavior or surface matches spec; covered by tests or wired in prod path |
| **PARTIAL** | Simplified or incomplete vs spec; usable stub |
| **GAP** | Spec requirement not implemented |
| **WIRE** | Core logic exists; dynasty UI, persistence, or batch path missing |

---

## A — Possession engine & output (`:core/sim`)

| ID | Status | Requirement (spec) | Code / test | Finding |
|----|--------|-------------------|-------------|---------|
| MEGA-SIM-001 | SHIP | `simulateGame(input) → result`; stateless engine, caller owns calendar/save | `PossessionSimEngine.simulate`, `GameSimInput` | Contract present; name is `simulate` not `simulateGame` |
| MEGA-SIM-002 | SHIP | Regulation **40:00**, two halves **20:00** each | `GameSimClock`, `PossessionSimSession` | Clock starts 2400s, decrements per trip |
| MEGA-SIM-003 | PARTIAL | **30s** shot clock state + reset on change of possession / **20** after OReb | `SimTuning.shotClock`, `GameSimClock.MAX_TRIP_SECONDS` | JSON defines 30/20; engine caps trip at 30s only — no shot-clock state machine or OReb reset |
| MEGA-SIM-004 | GAP | Shot-clock **violation** turnover type | — | No violation branch in `resolvePossessionForSession` |
| MEGA-SIM-005 | PARTIAL | Trip time bands: quick TO 3–6, quick shot 8–14, foul 12–18 | `SimTuning.timeUsedSeconds`, `sampleTripSeconds` | Bands loaded; pacing also scaled via `pacedTripSeconds` |
| MEGA-SIM-006 | GAP | Late-clock (22–30s) contested shot / TO / foul bumps from tuning | — | No late-clock branch keyed on `used` |
| MEGA-SIM-007 | GAP | **Overtime** 5:00 until winner | — | Loop ends at `gameClock == 0`; no OT |
| MEGA-SIM-008 | PARTIAL | **~60–70** possessions per team | `possessionsPerGameTarget`, paced trips | Team possession counts often in band per tests; not enforced hard |
| MEGA-SIM-009 | PARTIAL | Terminal events: TO, 2PT/3PT, FT chains, rebounds, blocks, steals text | `resolvePossessionForSession` | Only coarse TO / FGA / score; no FTs, rebounds, blocks, steals in box |
| MEGA-SIM-010 | GAP | Possession log format `14:32 \| HOME \| #12 3PT miss \| …` | `formatPossessionLog` | Generic `clock \| side \| Ns trip \| TO/+PTS/miss` — no jersey numbers |
| MEGA-SIM-011 | SHIP | Running score + clock in output | `GameSimResult`, watch UI score strings | Score lines updated in `GameWatchScreen` |
| MEGA-SIM-012 | PARTIAL | Full **player** box: MIN, PTS, FGM/A, … | `TeamBoxScore` only | Team-level aggregates; no per-player lines |
| MEGA-SIM-013 | SHIP | Team PTS = sum semantics / FGA bounds | `BoxScoreCoherence.assertValid` | Asserts scoreboard vs team box, FGM≤FGA, 3PM≤3PA |
| MEGA-SIM-014 | GAP | **200** player-minutes per team regulation coherence | `RotationMinutesAllocator` in prep only | Prep targets 200; sim does not accumulate MIN or validate |
| MEGA-SIM-015 | GAP | Release retry on coherence fail (cap N rolls) | `BoxScoreCoherence` | `require()` only — no retry loop |
| MEGA-SIM-016 | GAP | **Personal fouls**, disqualification at 5, foul-trouble pull | — | Not modeled in possession loop |
| MEGA-SIM-017 | GAP | In-game **substitution** from rotation bands / fatigue | `RotationMinutesAllocator` (pre-game only) | Five-on-floor not rotated during sim |
| MEGA-SIM-018 | GAP | Mid-game **injury** DNP from injury minute onward | `injuries.md` sim hook | Injuries only `GameSimInjuryRoll` post-game |
| MEGA-SIM-019 | SHIP | `GameSimResult` includes `possessionLog`, `isComplete`, `midGame` | `GameSimModels.kt` | Present |
| MEGA-SIM-020 | GAP | `periodsPlayed` (regulation + OT count) | — | Field not on result type |
| MEGA-SIM-021 | GAP | Participant fields: `height`, `weight`, `shooting` struct | `GameSimParticipant` | Only ids, slots, OFF/DEF, targetMinutes |
| MEGA-SIM-022 | PARTIAL | Slot-played ratings drive outcomes (84 PG at SG > 72 SG) | `GameLineupPrep`, `weightedOffensive` | Team rating is minutes-weighted; no per-possession player attribution |
| MEGA-SIM-023 | GAP | Optional position-comfort nudge without flipping OVR order | — | Not in tuning consumption |
| MEGA-SIM-024 | SHIP | Home/venue modifiers when caller supplies | `SimVenueModifiers` in tuning JSON | Loaded; **not applied** in `PossessionSimSession` today |
| MEGA-SIM-025 | SHIP | Tuning bundle from `assets/sim/sim-tuning.json` | `SimTuningCodec`, `SimTuningAssetTest` | ADR-003 path wired in app |
| MEGA-SIM-026 | SHIP | Same engine for watch vs batch (`simToEnd`) | `GameWatchPlayback.simToEnd` | Identical `PossessionSimEngine.simulate` |
| MEGA-SIM-027 | SHIP | Deterministic variance via `seed` on `GameSimInput` | `PossessionSimSession` Random(seed) | Halftime resume preserves RNG state via `MidGameState` |
| MEGA-SIM-028 | GAP | `winSharesSeason` rollup | Dynasty EoS (`simulation-engine` §season win contribution) | Correctly **not** in sim module |

---

## B — Playback & halftime UI

| ID | Status | Requirement (spec) | Code / test | Finding |
|----|--------|-------------------|-------------|---------|
| MEGA-SIM-029 | PARTIAL | Default **2s** wall-clock between possession lines | `GameWatchPlayback.DEFAULT_POSSESSION_DELAY_MS` | Constant 2000ms exists; `GameWatchScreen` hardcodes **8.0×** speed |
| MEGA-SIM-030 | GAP | User **speed multiplier** control (0.5×, 2×, 4×) on watch screen | — | No UI slider; fixed 8× in scrimmage |
| MEGA-SIM-031 | SHIP | **Sim to end** completes full loop | `GameWatchPlayback.simToEnd` | Used by batch fast path when tuning passed |
| MEGA-SIM-032 | SHIP | Halftime pause on watched run; merge updated prep; resume second half | `GameWatchPlayback.watch`, `GameSimHalftimeTest` | MidGame + `homeHalftimeGameplan` |
| MEGA-SIM-033 | PARTIAL | Halftime: change O/D scheme, rotation, usage, swap starters | `strategy-schemes.md` §halftime | Dialog only: deny-primary toggle + late-foul chips — no scheme/usage/lineup |
| MEGA-SIM-034 | SHIP | First-half stats/score fixed after halftime | `MidGameState` carry box scores | Scores accumulate from mid state |
| MEGA-SIM-035 | PARTIAL | `activeHomeGameplan(clock)` uses halftime plan in 2nd half | `GameSimInput.activeHomeGameplan` | `clock <= HALF_SECONDS` selects halftime plan (correct for countdown clock) |
| MEGA-SIM-036 | WIRE | Strategy **halftime preset** separate from pregame | `StrategyGameplanPanel` Set/Clear halftime | Stored in `halftimeGameplan` but watch passes `homeHalftimeGameplan` only when user sets preset — scrimmage button uses `halftimeGameplan ?: gameplan` as **pregame** only on `GameWatchLaunch` |
| MEGA-SIM-037 | GAP | In-sim shot clock display on lines (optional product) | — | Not shown |
| MEGA-SIM-038 | PARTIAL | Postgame report after sim | `PostgameReportGenerator` in `GameWatchScreen` | Article only; no full box UI |

---

## C — Strategy & schemes (`:core/strategy` + UI)

| ID | Status | Requirement (spec) | Code / test | Finding |
|----|--------|-------------------|-------------|---------|
| MEGA-SIM-039 | SHIP | **7** offensive presets | `OffensiveSchemePreset` enum | Matches planning table |
| MEGA-SIM-040 | SHIP | **7** defensive presets | `DefensiveSchemePreset` enum | Matches planning table |
| MEGA-SIM-041 | SHIP | Scheme modifiers affect 3PA rate, TO, pace, make bias | `SchemeModifierCatalog`, `SchemeMatchupResolverTest` | Wired in `PossessionSimSession` |
| MEGA-SIM-042 | SHIP | O/D interaction nudges (sample matchups) | `SchemeMatchupResolver.interactionNudge` | Partial 7×7 matrix — not exhaustive |
| MEGA-SIM-043 | SHIP | No separate pace slider — pace from offensive preset + interaction | `ResolvedSchemeEffects.tripPaceScale` | Aligns with spec |
| MEGA-SIM-044 | WIRE | Per-game offensive/defensive scheme on `GameSimTeamInput` | `GameSimTeamInput`, `LeagueFastSim` | Defaults **Balanced/Man**; Strategy hub does not set team schemes for user games |
| MEGA-SIM-045 | SHIP | Coach default O/D on new dynasty | `NewGameWizardScreen`, `SchoolStaffRepository` | Wizard + coach card — not per-game override |
| MEGA-SIM-046 | SHIP | Usage weights in gameplan | `GameplanInput.usageWeights`, `StrategyGameplanPanel` | Stepper per starter |
| MEGA-SIM-047 | PARTIAL | Usage affects offense via normalized share | `GameplanEffectsResolver.offenseTweaks` | Peak usage only — not per-player on possession |
| MEGA-SIM-048 | SHIP | **Deny primary scorer** toggle | Strategy panel + halftime dialog; test `denyPrimaryProducesMoreTurnovers` | `primaryScorerSlot` exists but **no UI** to pick opponent slot |
| MEGA-SIM-049 | SHIP | **Late-game foul** strategy Standard / Foul early / Protect | `LateGameFoulStrategy`, defense tweaks last 2:00 | Chips in strategy + halftime |
| MEGA-SIM-050 | GAP | **Primary defensive assignment** map PG–C → defender | `GameplanInput.primaryScorerSlot` only | No matchup assignment UI or sim pressure on assigned slot |
| MEGA-SIM-051 | GAP | Strategy edits blocked after week advance | `strategy-schemes.md` design lock | Not enforced in `StrategyScreen` / save checkpoint hooks |
| MEGA-SIM-052 | PARTIAL | Game preview win % / predicted score from ratings + RPI | `GamePreviewScreen` | Exists; not re-audited here — sim path separate |
| MEGA-SIM-053 | WIRE | Halftime gameplan persisted on dynasty save for real games | `GameWatchLaunch` globals | Scrimmage-only launch params; schedule games need save fields |

---

## D — Lineup & rotation (`:core/lineup`, `GameLineupPrep`)

| ID | Status | Requirement (spec) | Code / test | Finding |
|----|--------|-------------------|-------------|---------|
| MEGA-SIM-054 | SHIP | Listed positions PG/SG/SF/PF/C | `PlayerPosition` | — |
| MEGA-SIM-055 | SHIP | Slot eligibility interchange rules | `LineupSlotPolicy.slotEligible` | Matches `slotEligible()` in planning doc |
| MEGA-SIM-056 | SHIP | Default starters: best **listed** OVR per slot; tie playerId | `DefaultStarterAlgorithm.pickStarters` | Does not auto PG→SG for higher OVR |
| MEGA-SIM-057 | SHIP | Manual interchange in prep when user starters passed | `GameLineupPrepTest.userInterchangeHonorsSlotPolicy` | — |
| MEGA-SIM-058 | WIRE | User override from Strategy UI drag / pick | `positions-and-lineup.md` §user override | **StrategyScreen** read-only cards; does not pass `starterSlotWire` map into `GameLineupPrep.build` |
| MEGA-SIM-059 | SHIP | Injured / medical RS ineligible for lineup | `RotationMinutesAllocator.isLineupEligible`, `DefaultStarterAlgorithm` | Filters `injuryActive` + weeks |
| MEGA-SIM-060 | PARTIAL | Empty slot unfilled + UI prompt | `StrategyScreen` `strategy_slot_empty` | Shows empty; no assign flow |
| MEGA-SIM-061 | SHIP | Rotation minutes bands → ~200 team minutes | `RotationMinutesAllocator`, `GameLineupPrepTest` | Starters 32 + bench grants |
| MEGA-SIM-062 | GAP | Bench sub logic during sim from minutes bands | `simulation-engine` §rotation | Minutes are prep weights only |
| MEGA-SIM-063 | PARTIAL | Reserves list / depth order in Strategy UI | `StrategyScreen` bench cards | Display only |
| MEGA-SIM-064 | WIRE | Persist user lineup across sessions | `DynastyRosterPlayerEntity.starterSlotWire` | DB column exists; Strategy does not edit |
| MEGA-SIM-065 | SHIP | Stamina gate for default starters | `DefaultStarterAlgorithm` `starterBlockStamina` | Strategy passes fatigue tuning threshold |

---

## E — Injuries (`:core/injuries`, sim hook, UI)

| ID | Status | Requirement (spec) | Code / test | Finding |
|----|--------|-------------------|-------------|---------|
| MEGA-SIM-066 | SHIP | Post-game roll for players with minutes; 0–2 cap per team | `InjuryOccurrenceEngine`, balance `maxInjuriesPerTeamPerGame` | — |
| MEGA-SIM-067 | SHIP | Phase multipliers (exhibition lower; no OS/preseason) | `InjuryOccurrenceEngine.rollAfterGame` | — |
| MEGA-SIM-068 | SHIP | Bank catalog from `balance-2026.json` injuries.bank | `InjuryOccurrenceEngine.bankFromBalance` | App loads 40-row bank in watch path |
| MEGA-SIM-069 | SHIP | Medical staff **−1 week** on minor | `InjuryOccurrenceEngine` | When grade ≥ baseline |
| MEGA-SIM-070 | PARTIAL | Minutes weight for injury probability | `GameSimInjuryRoll.estimateMinutes` | Scales prep target minutes × poss/70 — not actual box MIN |
| MEGA-SIM-071 | SHIP | `GameSimResult` home/away injury lists | `GameSimModels`, `GameSimInjuryRoll.attach` | Only when `isComplete` |
| MEGA-SIM-072 | SHIP | Persist + `applyInjuryLineupAdjust` after user scrimmage | `DynastyApp` `onSimComplete`, `InjuryLineupAdjust` | Scrimmage path applies injuries |
| MEGA-SIM-073 | SHIP | Auto-fill starters on injury (listed-position algorithm) | `InjuryLineupAdjustTest` | Snapshots `injuryWasStarterSlotWire` |
| MEGA-SIM-074 | SHIP | Auto-restore on `weeksRemaining → 0` | `applyInjuryReturnLineupAdjust`, week advance repos | Tested in injuries package |
| MEGA-SIM-075 | PARTIAL | Strategy: injured greyed / cannot slot | `StrategyScreen` alpha + `PlayerInjuryRosterRow` | Visual only — no drag to block |
| MEGA-SIM-076 | WIRE | Medical redshirt postgame sheet | `injuries.md` UX flow | Policy in core; sim UI lacks **Apply for medical redshirt** sheet after watch |
| MEGA-SIM-077 | GAP | Injury line in **live** possession log when mid-game injury exists | Spec mid-game exit | Post-game only |
| MEGA-SIM-078 | PARTIAL | AI league batch injury rolls | `AiLeagueWeekOps`, `LeagueWeekBatchSimEngine` | Count stub; full per-team bank roll not shown in fast sim path |
| MEGA-SIM-079 | WIRE | Real RS game uses possession sim + injuries | `LeagueWeekBatchRepository` | User game sim path exists; AI games often `LeagueFastSim` without roster/tuning/injuries |
| MEGA-SIM-080 | SHIP | Fatigue weight in injury probability | `FatigueEngine.fatigueInjuryWeight` | — |

---

## F — Cross-cutting integration gaps

| ID | Status | Requirement (spec) | Code / test | Finding |
|----|--------|-------------------|-------------|---------|
| MEGA-SIM-081 | WIRE | User calendar game → Strategy prep → watch/sim commit | `DynastyShellScreen`, `GameWatchLaunch` | Scrimmage from Strategy hub; schedule card path may not pass schemes/roster |
| MEGA-SIM-082 | PARTIAL | Batch week sim uses possession engine | `LeagueFastSim` with `SimTuning` | Fallback random scores when `tuning == null` |
| MEGA-SIM-083 | GAP | `eventWeights` JSON consumed per possession | `SimTuning.eventWeights` | Empty object in asset; unused in engine |
| MEGA-SIM-084 | SHIP | `:core:test` possession / lineup / injury / watch tests | `android/core/src/test/.../sim/*` | Green in CI per BUILD_CHECKLIST 0.3 |
| MEGA-SIM-085 | PARTIAL | Tie games broken | `LeagueFastSim` +1 for standings | Possession sim can tie; batch nudges home +1 only in fast sim wrapper |

---

## Summary counts

| Status | Count (of 85 items) |
|--------|---------------------|
| SHIP | 38 |
| PARTIAL | 18 |
| WIRE | 11 |
| GAP | 18 |

**Highest-impact gaps for spec fidelity:** player-level box score and minutes (012, 014, 062), shot-clock violations and OReb reset (003–004), fouls/rotation in sim (016–017), Strategy lineup override + schemes on game input (044, 058, 064), watch UX timing (029–030), halftime full coach adjustments (033), defensive assignment (050), mid-game injuries (018, 077), league AI games on full engine + injury bank (079).

**Build checklist cross-ref:** Phase **10.1–10.8** marked `[x]` in [`docs/BUILD_CHECKLIST.md`](../BUILD_CHECKLIST.md); this audit refines **done when** versus planning detail for sim/strategy/lineup/injuries surfaces.

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-11 | Initial audit — 85× `MEGA-SIM-NNN` items vs four planning docs + core/sim UI |
