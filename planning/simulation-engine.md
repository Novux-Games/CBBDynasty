# Dynasty: College Basketball — Simulation Engine

**Companion to:** [`game-design.md`](game-design.md)  
**Product shape:** Text-based **2D** dynasty sim (Campus Dynasty–class presentation), **offline**, **local save files**  
**Gameplay lock:** **Sim-only** — no on-court user control; impact comes from **lineup**, **rotation**, and **gameplan** set before tip.  
**Last updated:** 2025-11-08

---

## Contents

1. [Purpose & boundaries](#purpose--boundaries)
2. [Clarifications (design Q&A)](#clarifications-design-qa)
3. [Calendar & when games run](#calendar--when-games-run)
4. [Possession model & clock](#possession-model--clock)
5. [Player & team ratings](#player--team-ratings)
6. [Game prep inputs (user leverage)](#game-prep-inputs-user-leverage)
7. [Box scores, play-by-play & archives](#box-scores-play-by-play--archives)
8. [World sim at 365 teams](#world-sim-at-365-teams)
9. [Statistical coherence](#statistical-coherence)
10. [Résumé metrics (NET / selection inputs)](#résumé-metrics-net--selection-inputs)
11. [Randomness & saves](#randomness--saves)
12. [Difficulty sliders](#difficulty-sliders)
13. [Performance & threading](#performance--threading)
14. [Data-driven tuning](#data-driven-tuning)
15. [Regression fixtures (optional)](#regression-fixtures-optional)
16. [Open implementation notes](#open-implementation-notes)

---

## Purpose & boundaries

The simulation engine is responsible for:

| Owns | Does not own |
|------|----------------|
| Possession-by-possession game resolution | Recruiting UI / NIL negotiation screens |
| Full box scores & team totals | Identity pack display strings |
| Day/week advancement batch sim | Cloud sync, multiplayer |
| Schedule → game instances | Coach skill tree UI |
| Player stat aggregation, season/ career stats | Carousel narrative scripting |
| Résumé metrics derived from results | Committee presentation polish |

**Storage:** All dynasty state and completed game artifacts live in **local files** (format TBD in tech architecture doc). No network required to play.

**Presentation:** **2D text-forward** UI (scores, logs, tables, minimal graphic court optional). Engine outputs structured data; UI renders it.

---

## Clarifications (design Q&A)

Plain-language answers to planning questions — locked unless this doc is revised.

### Randomness — “not every game will be the same” (what we meant by #3)

**You’re right:** every game should feel different. Opponents, rosters, pace, foul trouble, and rolls inside the engine guarantee that **Kentucky–Duke ≠ Kentucky–Samford** and that **Tuesday’s game ≠ Saturday’s game**.

The **only** thing we were asking about is a **save-file exploit**, not “boring identical basketball”:

| Situation | Expected behavior |
|-----------|-------------------|
| Normal play | Outcomes vary game to game; upsets happen; stats differ every night. |
| You sim **Tuesday vs State**, get **81–76**, save, quit the app | When you load that save, Tuesday is still **81–76** — the result is **written to the file**. |
| You sim Tuesday, don’t like the loss, **reload save and sim Tuesday again** | **Not allowed** — no “reroll this game” unless we add a **cheat/sandbox** mode later. |
| You change **lineup or gameplan**, then sim (first time or in a **new** dynasty) | New inputs → **new** outcome (still random within reason). |

**Analogy:** like a season of real basketball on disk. Nights differ; you don’t get to erase a bad loss by reloading.

**Design lock:**

- **Variety:** yes — possession outcomes use RNG weighted by ratings, gameplan, home court, fatigue.
- **Integrity:** final scores **stick** in the save so the bracket and NET stay consistent.

### Résumé / NET (#8) — as real as possible

**Goal:** Bubble talk, quadrant records, and seed lines should **feel like Selection Sunday**, not a made-up number.

| Principle | Detail |
|-----------|--------|
| **No imported real-world ratings** | We don’t paste KenPom/NET CSVs for 2026 teams. |
| **Yes real NCAA-style logic** | Metrics and sort keys **mirror** how the committee actually reasons: efficiency, **where** you played, **who** you played, quality wins, bad losses, late form. |

Engine maintains a **NET-analog ranking** updated **after each day’s games**:

1. **Adjusted offensive & defensive efficiency** (points per 100 possessions; opponent-adjusted iteratively — same convergence *idea* as public NET).
2. **Team value index** — blend of efficiency margin, opponent strength, and game-location difficulty (neutral > road > home for the *value of a win* framing where applicable).
3. **Quadrants 1–4** — each game bucketed by **location** (H/N/A) and **opponent NET tier** at game date; track **W–L per quadrant** like résumé sheets.
4. **SOS & non-conference strength** — opponents’ rolling strength; bad losses vs weak tiers flagged.
5. **Last 10 / late-season form** — weighted for bubble ordering.
6. **Eye-test tie noise** — tiny jitter only for **ties** in sort order, not to override résumé (keeps standings stable).

Selection Sunday in [`game-design.md`](game-design.md) reads these tables; formulas live in **tuning JSON** with documented real-world inspiration (labels in UI: “Resume / NET” unless identity pack renames).

Early season: NET is **volatile** (few games); by conference play NET **stabilizes** — intentional.

### Performance (#9) — what was the actual question?

**Question:** On a typical phone, how **snappy** should sim feel?

This is **not** “how realistic is basketball.” It’s UX/engineering targets:

| Work unit | Target (tune on device) |
|-----------|-------------------------|
| **Watch mode** | ~**2–3 seconds** between possession lines (your spec); skippable |
| **Fast-sim user game** | As fast as CPU allows (no artificial delay) |
| **Advance day** | Sim every other game that date in background; show progress bar; shouldn’t feel “frozen” |
| **Storage** | Full text box scores for all games — **fine** |

We’ll profile on a mid-tier Android when code exists; numbers above are **design targets**, not promises.

### Regression fixtures (#14) — decided

**Ship engineering tests** (player never sees them):

| ID | Scenario | Assert |
|----|----------|--------|
| `slugfest` | Two defensive teams, slow pace | Total points each side 45–75; sum(player PTS) = team PTS |
| `shootout` | Two high-OVR fast-pace teams | Total 150–220 combined; pace within band |
| `foul_trouble` | Star picks up 4 PF in 1H | PF ≥ 4; minutes still reconcile |
| `blowout` | 95 OVR vs 60 OVR | Margin ≥ 20; no incoherent shooting lines |
| `upset_band` | 12-point underdog wins ~15% in Monte Carlo | Win rate 5–25% over 500 trials (tuning guardrail) |
| `ot_path` | Tie at regulation forced via test harness | OT played; winner by 2+ |

**Location:** `testdata/sim/fixtures/` when repo has code; mirror summaries in `planning/fixtures/README.md` for design traceability.

**When:** Run on every engine change in CI once the module exists.

---

## Calendar & when games run

**365 teams** exist in the league; **each calendar day** only teams **scheduled that day** play.

### State machine per game

```
SCHEDULED → (optional) USER_WATCHED_SIM → FINAL
          → BATCH_SIM on advance → FINAL
```

| Moment | Behavior |
|--------|----------|
| User’s game day | User may **watch** possession sim or **fast-sim** to end; result stored when complete |
| Same day, other games | Remain **SCHEDULED** until user **advances day** (or week) |
| On **Advance Day** | Engine sims **all SCHEDULED games that date** (except user’s game if already FINAL), then rolls calendar |
| On **Advance Week** | Sim each intervening day in order (same rules per day) |

**Week advance** is not “one game per team”; it is **iterate days**, sim all games on each day.

### Ordering within a day

1. Optional: prompt user if their game is SCHEDULED and today.
2. User completes or skips to advance.
3. Batch-sim remaining games for that date.
4. Update standings, résumé metrics, injuries, news feed.

---

## Possession model & clock

### Clock

- Regulation: **40 minutes** (college), **2 halves** (20:00 each) unless ruleset data says otherwise for future eras.
- **Overtime:** 5:00 periods until winner.

### Possession loop

Each possession:

1. **Lineup on floor** (from rotation rules + fatigue/fouls).
2. **Gameplan modifiers** (pace, focus, matchups — set pregame).
3. Resolve event: turnover, foul, shot, bonus trip, etc.
4. **Consume game clock:** random **3–30 seconds** per possession (distribution tuned by pace and event type).

| Event class | Typical clock shave |
|-------------|---------------------|
| Immediate turnover / steal | ~3–6 sec |
| Quick shot | ~8–14 sec |
| Foul, halt play | ~12–18 sec |
| Full shot-clock usage | up to **30** sec (cap at possession time remaining) |

5. **Wall-clock delay** when user is watching: **~2–3 seconds** between possessions (skipped in fast-sim).

### Shot clock (logical)

- Engine tracks **35-second** shot clock (ruleset table).
- Violation → turnover possession.
- Clock shave and shot-clock are **consistent** (cannot shave 30s game clock in 5s of shot clock without a reset event).

### Outputs per possession (for live log)

Short text line, e.g.:

`14:32 | HOME | Smith 3PT miss | Long REB | Away possession`

Full log available **during** sim; not all lines persisted after game ends (see [Archives](#box-scores-play-by-play--archives)).

### Stats tracked (full box score)

**Player:** MIN, PTS, FGM/A, 3PM/A, FTM/A, ORB, DRB, REB, AST, STL, BLK, TO, PF (+ optional +/-)  
**Team:** same aggregates, FG%, 3P%, FT%, points in paint, bench points, fouls, etc. as needed for UI and résumé.

**Minutes:** derived from possessions where player is on floor (consistent with 40:00 team total).

---

## Player & team ratings

### Roster player attributes (sim inputs)

| Field | Role in sim |
|-------|-------------|
| **Position** | G/F/C (lineup validity, matchup table) |
| **Class grade** | Fr–Sr (+ RS flag affects narrative, not OVR directly) |
| **Height / weight** | Rebounding, finish, small matchup nudges |
| **Draft potential** | Pro-day / draft story; slight late-game clutch bias optional |
| **Overall** | Master weight in outcome rolls |
| **Offensive rating** | Usage efficiency, shot creation |
| **Defensive rating** | Stop probability, steal/block bias |
| **Potential** | Offseason growth only |
| **Shooting profile** | Generated per player: tendency + 2P/3P/FT skill splits |

**Shooting data (randomized at generation):** e.g. `threePointTendency`, `midRangeTendency`, `driveTendency`, `freeThrowPctSkill` — stable for career unless development changes.

Recruits generate with the same schema at lower bands; development arcs use **potential**.

### Team pregame strength

Blends:

- On-floor lineup ratings (top 8 rotation by default)
- Home court (arena grade)
- Coach tactical grade (prep, not click-by-click)
- Fatigue / injury flags

---

## Game prep inputs (user leverage)

**No mid-game user control.** Before tip:

| Input | Effect |
|-------|--------|
| **Starting lineup** | Who begins each half |
| **Rotation depth / minutes bands** | Who subs in |
| **Gameplan** | Pace tier, inside-outside emphasis, deny star, foul strategy late |
| **Matchups** | Optional: who guards their #1 scorer |

Coach **skill tree** and **scheme fit** modify effectiveness of gameplan (see game-design coaching section). During sim, only AI rotation rules and gameplan execute.

---

## Box scores, play-by-play & archives

| Phase | What user sees |
|-------|----------------|
| **During sim** | Live possession text stream + running score |
| **After final** | **Box score + team stats** (persisted forever in save) |
| **After final** | **No full play-by-play archive** (by design — saves space); optional “last 10 possessions” summary in recap only |

**League-wide:** Every completed game stores at minimum:

- Final score, date, teams, location
- Full player + team box score
- Game ID for résumé recompute

---

## World sim at 365 teams

| Concept | Detail |
|---------|--------|
| Membership | ~365 teams in schedule graph |
| Games per day | Only matchups on that date |
| User team | Same possession engine as AI games |
| AI gameplan | Chosen by AI coach profile + opponent scouts |
| Standings | Update after each day’s batch |

AI programs do not “play” before the calendar says so — avoids 365× simultaneous results and matches dynasty pacing.

---

## Statistical coherence

**Hard rule:** Box score totals must **reconcile**.

| Check | Example |
|-------|---------|
| Team PTS = sum player PTS | No 11 team points with a 24-pt scorer |
| FGA/FGM team = sum players | |
| Team FGM* types ≤ team FGA | |
| Minutes plausible | Lineup math ≈ 200 player-minutes per team |
| Possessions ≈ pace | Estimated possessions within band for both teams |

If validation fails during dev builds: **assert/log** and reject result. In release: **re-sim possession** with capped retries, then fallback minimal correction (last resort — should be rare).

---

## Résumé metrics (NET / selection inputs)

**As real as possible** — computed **only** from sim games, using **NCAA-realistic structure**.

### Per-game inputs (frozen at tip time for résumé)

- Date, location (**H / N / A**)
- Opponent ID
- Opponent **NET rank tier** at game date (1–4 quadrants)
- Result, score, possessions (from box score)

### Rolling team outputs (recomputed nightly)

| Output | Use |
|--------|-----|
| **AdjO / AdjD** | Efficiency per 100 poss., opponent-adjusted |
| **AdjEM (margin)** | AdjO − AdjD |
| **NET rank** | Total ordering 1–365; primary sort for tiers |
| **Quadrant records** | Q1–Q4 W–L |
| **SOS / NC SOS** | Opponent strength aggregates |
| **Quality wins / bad losses** | Flags vs tier thresholds |
| **L10** | Last 10 games weight |
| **Road/neutral résumé** | Committee tie sheets |

### Quadrant bucketing (real pattern)

For each completed game, assign **one** quadrant from opponent NET position at **that date** and game location (e.g. home vs top-50 NET → Q1). Threshold tables in tuning JSON; calibrate so **~40%** of D-I games land Q1/Q2 combined in a typical season (sanity stat).

### Selection committee handoff

Export `ResumeSnapshot` per team at Selection Sunday:

- NET rank, quadrant lines, SOS, adj. efficiency, notable wins/losses list
- No manual overrides

[`game-design.md`](game-design.md) committee sim **sorts** on this snapshot; engine does not pick the bracket UI narrative.

---

## Randomness & saves

**In-game:** each possession uses weighted randomness → **different games, different stories**.

**After final:** result is **persisted**; load save → read box score, don’t sim again.

```
gameSeed = hash(dynastySeed, gameId, gameDate, lineupHash, gameplanHash, difficultyPreset)
```

- Used **once** when game moves SCHEDULED → FINAL.
- Stored on game record for **debug replay** (dev tools), not player rerolls.
- New dynasty → new `dynastySeed` → different league-wide paths.

**Not the goal:** identical scores every season. **Is the goal:** one coherent timeline per save file.

---

## Difficulty sliders

**Defaults are fixed balanced logic.** Sliders scale coefficients, not separate sim forks.

| Slider (example) | Affects |
|----------------|---------|
| Injury rate | Injury event probability |
| Upset frequency | Underdog efficiency bump |
| Fairness / chaos | Turnover variance |
| Sim speed display | Wall-clock delay only |

Defaults documented in tuning JSON; sliders persist per dynasty.

---

## Performance & threading

| Mode | Approach |
|------|----------|
| User watching | Main thread or UI thread dispatches possession events; engine step on worker |
| Day batch | Background job sims N games serially or **parallel per game** (embarrassingly parallel); merge results on completion |
| Save IO | Write box scores append-only per season file or SQLite — tech choice later |

Parallel day-sim is allowed; **user game** should not block other games on same day until advance.

---

## Data-driven tuning

**Not a phased “v1 only” engine** — shipping game uses data tables for:

- Pace distributions, clock shave ranges
- Shot selection weights by gameplan
- Foul rates, fatigue curves
- Home court by arena grade
- Slider default coefficients

Balance passes edit JSON/CSV in repo; code reads tables at load.

---

## Regression fixtures

See [Clarifications — #14 decided](#regression-fixtures-14--decided) for the fixture list and assertions.

---

## Open implementation notes

| Topic | Next decision |
|-------|----------------|
| Save file format | JSON lines, SQLite, or hybrid |
| Kotlin vs other runtime | Android-first; engine should stay UI-agnostic module |
| Optional URL pack import | Offline play unaffected; import is one-time file fetch at user initiative |
| Halftime adjustments | Auto-only from coach grade or fixed gameplan tiers |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Initial simulation engine spec from design Q&A |
| 2025-11-08 | Clarify saves vs variety; real-style NET; perf question; fixture list |
