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

### “Determinism” (same save, same outcome?)

**Meaning:** If you simulate the *same* scheduled game twice **without changing** lineup, gameplan, injuries, or global seed inputs, do you get the **same** result?

| Rule | Choice |
|------|--------|
| **Once a game is simmed, it is final** | Result + box score **persist** in the save; reloading the save does **not** re-roll. |
| **Before sim** | Changing lineup/gameplan **should** change outcomes (new input → new sim). |
| **Optional setting (later)** | “Locked seed preview” for QA only — not default player experience. |

So: not “press sim until you win,” but also not “identical inputs forever” across roster changes.

### “NET / résumé — derived vs hybrid?”

| Term | Meaning |
|------|---------|
| **Derived (chosen)** | Efficiency, tier wins, SOS, and bubble sort keys are **computed only from sim outputs** (box scores, schedule locations, opponent strength). |
| **Hybrid (not used)** | Injecting external real-world ratings or manual overrides per team. |

Opponent strength updates iteratively as the season sim progresses (weak early-season NET snaps, stabilizes by February). See [Résumé metrics](#résumé-metrics-net--selection-inputs).

### “Performance budget”

**Meaning:** How fast must the phone finish work?

| Work unit | Target (engineering goal — tune on device) |
|-----------|---------------------------------------------|
| **User game** (40 min clock, all possessions) | Readable speed: ~**2–3 seconds wall-clock per possession** while user watches; **fast-sim** skips display delay |
| **Rest of D-I that day** | When user hits **Advance Day/Week**, sim **all other games scheduled that day** in one batch |
| **Typical day** | ~30–80 games league-wide (not 365 — only teams with a game that date) |
| **Storage** | Text box scores for every completed game are **acceptable**; compress optional |

Text output keeps CPU heavier than GPU; batch day sim should run on a **background worker** with cancel-safe progress UI.

### “Golden tests / regression fixtures”

**Meaning:** Saved **tiny scenarios** (fixed rosters + gameplan + schedule) with **expected box score ranges** so code changes do not break “star scoring 24 when team scores 81.”

Optional but recommended for CI; not player-facing.

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

All **derived** after games finalize:

| Metric | Source |
|--------|--------|
| Offensive / defensive efficiency | Points per 100 possessions from box scores |
| Adjusted efficiency | Opponent strength iteration (schedule-weighted) |
| Quad/Tier record | Home/road + opponent strength buckets |
| SOS | Opponent adj. efficiency aggregate |
| Recent form | Rolling last N games |

**Schedule location** comes from schedule table; **opponent strength** from their games already played — so metrics evolve realistically through the season.

Selection Sunday logic in game-design consumes these tables; engine **only maintains the numbers**.

---

## Randomness & saves

```
gameSeed = hash(dynastySeed, gameId, gameDate, difficultyPreset)
```

- **dynastySeed** created at new dynasty.
- Each **final** game stores `gameSeed` used + result (reproducible for debugging).
- **Reloading save** reads stored results — does not re-run RNG for FINAL games.

Variance between playthroughs comes from different schedules, injuries, and user choices — not from re-rolling saved history.

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

## Regression fixtures (optional)

Maintain `planning/fixtures/` (or `testdata/sim/`) with:

- 5-on-5 low-total defensive slugfest
- High-pace shootout
- Foul trouble (star with 4 PF early)
- Overtime game

Each fixture: inputs + expected team points band + stat sum checks. Used in automated tests when build pipeline exists.

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
