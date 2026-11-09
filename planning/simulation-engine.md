# Game simulation — possession engine

**Scope:** This document is **only** how **one basketball game** is simulated: clock, possessions, outcomes, box score.  
**Not here:** recruiting, roster creation, calendar, saves, NET, brackets, UI layout — see [`game-design.md`](game-design.md).  
**Product:** Text-based **2D**, **sim-only**; user affects **lineup**, **rotation**, and **gameplan** before tip, not during play.

---

## Contents

1. [Contract](#contract)
2. [Possession loop & clock](#possession-loop--clock)
3. [What can happen on a possession](#what-can-happen-on-a-possession)
4. [Rotation & fouls (in sim)](#rotation--fouls-in-sim)
5. [Prep inputs](#prep-inputs)
6. [Participant data (inputs only)](#participant-data-inputs-only)
7. [Output](#output)
8. [Statistical coherence](#statistical-coherence)
9. [Season win contribution](#season-win-contribution-design-lock) (GOAT / history — not shown in box score)
10. [Randomness](#randomness)
11. [Playback: watch, speed, sim to end](#playback-watch-speed-sim-to-end)
12. [Why track a shot clock?](#why-track-a-shot-clock)
13. [Tuning data](#tuning-data)

---

## Contract

**In:** two sides, each with a roster slice for this game, prep (lineup / rotation / gameplan), environment (home/away/neutral, optional modifiers the caller passes in).

**Out:** final score, full box score, team totals, optional possession log for this run.

The engine does **not** decide *when* the game is played, whether it is saved, or what happens to the league — the **caller** does. Same engine for “user watches” and “silent batch sim.”

```
simulateGame(input: GameSimInput) → GameSimResult
```

---

## Possession loop & clock

### Regulation (men’s CBB rules)

| Rule | Value |
|------|--------|
| **Game clock** | **40:00**, **two halves** (20:00 each) |
| **Shot clock** | **30 seconds** (NCAA men’s) |
| **Shot-clock reset** | Change of possession → **30**; offensive rebound → **20** (per current NCAA table in tuning JSON) |
| **Overtime** | **5:00** periods until winner |

The engine is **possession-based** (one terminal event chain per trip), not continuous physics. **Game clock** and **shot clock** are **game-state timers** that stay correct for stats, log text, and rule outcomes — independent of how fast the **UI** plays possession lines.

### One possession

1. Determine **five players** on floor per team (rotation logic).
2. Initialize / carry **shot clock** for offense (30 on new trip, 20 after offensive rebound, etc.).
3. Apply **gameplan** weights (pace, shot profile, foul aggressiveness).
4. Sample or resolve **time used** on this trip (0–shot clock cap) → drives late-clock branches and **shot-clock violations**.
5. Resolve **terminal event** (or chain: and-1, bonus FTs, etc.).
6. **Subtract game clock** by time used (and stoppages); clamp to half/game end.

| Event flavor | Typical time used (game + shot clock) |
|--------------|----------------------------------------|
| Quick turnover | 3–6 sec |
| Quick shot | 8–14 sec |
| Foul / stoppage | 12–18 sec |
| Shot-clock violation | **30** (or remaining on clock) |

7. Update stats, fouls, score; reset shot clock on change of possession; check end of half/game/OT.

Half ends when game clock hits 0:00; track **made basket** ending a half if clock at 0 on release (tuning).

### Possessions per game

Target band **~60–70** possessions per team (college pace); tuning adjusts from gameplan.

---

## What can happen on a possession

Non-exhaustive; implementation uses weighted rolls from participant ratings + gameplan + situation (score differential late, fouls to give).

| Category | Examples |
|----------|----------|
| **Turnover** | Steal, bad pass, offensive foul, shot-clock violation, travel |
| **Shot** | 2PT/3PT attempt → make/miss; block; foul on shot → FTs |
| **Free throws** | Bonus / double bonus sequences |
| **Rebound** | Offensive → same team new possession; defensive → change |
| **Jump ball / alternate possession** | Tie-up rare event |

Each event emits **one text line** for live log (see [Output](#output)).

---

## Rotation & fouls (in sim)

- **Starting lineup** from prep; substitutions from **rotation rules** (minutes bands, foul trouble pull, fatigue).
- **Disqualification** at 5 personal fouls (or ruleset table).
- **Minutes:** accumulated while on floor; team total should align with 200 player-minutes per team per regulation (coherence check).

No user substitution UI **during** sim — only auto per prep.

### Injuries (output only)

After the game, roll **random injuries** per [`injuries.md`](injuries.md#sim-hook) for players with minutes. Append to `GameSimResult.injuries[]` — **caller** persists to dynasty state, runs **auto lineup adjust** (demote to reserves, promote healthy starters), and blocks injured players in the **next** game’s prep. On **week advance**, **caller** decrements `weeksRemaining` (bye weeks included); at **0**, runs **return lineup adjust** (reclaim starter slot / top reserve). Injured players in **this** run stop playing once an mid-game injury event fires.

---

## Prep inputs

Set **before** `simulateGame` runs; frozen for that run.

| Input | Sim use |
|-------|---------|
| **Starting lineup** | Opening minutes each half |
| **Rotation / minutes bands** | Who replaces whom, target minutes |
| **Gameplan** | Pace, inside/outside bias, 3PT rate cap/floor, deny-primary, late-game foul strategy |
| **Defensive assignment** | Per-slot matchup from gameplan; **deny primary** boosts pressure on assignment |

Coach quality / home environment: if used, caller passes **scalar modifiers** on `GameSimInput` (engine does not load coach career).

---

## Participant data (inputs only)

Final roster/career design lives elsewhere. For **this sim**, each player must provide at least:

| Field | Sim use |
|-------|---------|
| `playerId` | Stat lines |
| `position` | **PG / SG / SF / PF / C** — listed; **slot played** for this game in prep; ratings follow the player in slot (84 at SG > 72 at SG). See [`positions-and-lineup.md`](positions-and-lineup.md) |
| `height`, `weight` | Rebound & finish nudges |
| `overall`, `offensive`, `defensive` | Outcome weights |
| `shooting` | Tendencies + make skills (2PT, 3PT, FT); caller-defined struct |

Class year, draft potential, recruiting story, etc. **not read** by the possession engine unless later mapped into these numbers.

---

## Output

### Live (during `simulateGame`)

- Stream of possession lines, e.g.  
  `14:32 | HOME | #12 3PT miss | #3 REB | AWAY ball`
- Running score, clock, half.

### Returned struct

| Field | Notes |
|-------|-------|
| `finalScore` | Home / away |
| `boxScore` | Full player + team stats |
| `periodsPlayed` | Regulation + OT count |
| `possessionLog` | Full text log for this run (caller may discard) |

**After the game (product):** UI may show only box score + team stats; that is a **product** choice, not engine logic.

### Stats

**Player:** MIN, PTS, FGM/A, 3PM/A, FTM/A, ORB, DRB, REB, AST, STL, BLK, TO, PF  
**Team:** aggregates + shooting %, fouls, points off TO, etc. as needed for display.

---

## Statistical coherence

Before returning `GameSimResult`:

| Rule |
|------|
| Team PTS = sum of player PTS |
| Team FGA/FGM/… = sums of players |
| FGM ≤ FGA, 3PM ≤ 3PA, FTM ≤ FTA |
| Team points match scoreboard |
| Reasonable minutes vs possessions |

Fail in dev → error. Release → retry possession rolls capped N times; last resort abort game sim with logged error (should be rare).

---

## Season win contribution (design lock)

### What players see

**Nothing on the box score.** This is a **hidden season summary**: *“How much of this team’s wins should we credit to this player?”*  
Used for **GOAT lists**, record books, and Hall of Fame style rankings ([`history-hub.md`](history-hub.md)). Stored on the player’s season archive as `winSharesSeason` (internal field name only).

### When it runs

| Trigger | Detail |
|---------|--------|
| **Who computes** | **Dynasty layer** after the season is finished — **End of Season week**, not inside `simulateGame` |
| **Games counted** | Same set as season record / eligibility: **regular season + conference championships + national postseason**; **exhibitions excluded** |
| **Team wins** | That program’s **W** in those games for that season |

Possession sim only supplies **per-game box scores**; the season job **rolls them up** into season totals.

### Plain rule

1. **Playing time** is the base — you can’t earn much if you rarely play.  
2. **Production** from the season stat line nudges credit up or down vs teammates (scoring, assists, rebounds, turnovers).  
3. On each team, credits **add up to that team’s win total** for the year — e.g. a **20-win** team’s roster shares sum to **20.0**, so a 6.0 season is a “big chunk” of a good year.

**Transfers:** If a player appears at **two schools in one season** (unusual in this game’s portal rules), compute **separately per school**; career GOAT **adds** both seasons ([`history-hub.md`](history-hub.md)).  
**Zero minutes:** `winSharesSeason = 0` (redshirt, DNPs all year).

### Inputs (per player, per school, per season)

From aggregated box score:

| Input | Meaning |
|-------|---------|
| `minutesSeason` | Total MIN |
| `pts`, `ast`, `reb`, `to` | Season sums |
| `teamWins` | Program W (table above) |
| `teamMinutes` | Σ `minutesSeason` over roster players with MIN &gt; 0 |

### Formula (design lock)

**Production score** (one number for “how much did they do with the ball”):

```
production = PTS + 1.5×AST + 1.0×REB − 1.0×TO
```

**Per player on that team:**

```
minuteShare   = minutesSeason / teamMinutes
productionIdx = production / max(1, teamAvgProduction)    // teamAvg = mean production among players with MIN > 0
rawCredit     = minuteShare × (0.60 + 0.40 × productionIdx)
```

Clamp `productionIdx` to **0.25–1.75** before the blend so one outlier game plan cannot dominate.

**Allocate wins** (sums to `teamWins`):

```
winSharesSeason = teamWins × (rawCredit / Σ rawCredit over roster with MIN > 0)
```

Round to **2** decimals for storage. Optional balance exponents live in `balance-2026.json` → `history.winContribution` ([ADR-002](../docs/ADR-002-balance-ruleset-bundle.md)); **behavior above is the default intent**.

### Worked example (illustrative)

20-win team; star plays 35% of team minutes with high production → ~**5.5–7.0** shares. Bench warm-body at 5% minutes, low production → **~0.2–0.5**.

### Cross-links

| Consumer | Use |
|----------|-----|
| [`history-hub.md`](history-hub.md) | `careerWinShares`, GOAT score, tie-breaks |
| Season review / archive | Optional debug export in dev builds only |

---

## Randomness

- **Between games:** different matchups and rolls → different scores (not “same game every time”).
- **Within a run:** weighted RNG on each possession.
- **Same inputs, new run:** can produce a different result (basketball variance).

**Re-sim same matchup:** allowed until the dynasty layer **commits the week** (see game-design: save on **week advance**; reload before that → replay). The possession engine is stateless; the caller decides whether to run again.

---

## Playback: watch, speed, sim to end

Same `simulateGame` run for all modes — only **UI pacing** changes. Final score and box score are **identical** for the same input + RNG seed (if seed exposed) regardless of speed.

| Mode | Behavior |
|------|----------|
| **Watch** | Default **2 s** wall-clock between possession log lines ([`strategy-schemes.md`](strategy-schemes.md)); halftime pause for prep |
| **Scaling speed** | User multiplier on wall-clock delay (e.g. 0.5×, 2×, 4×) — does **not** change game clock or shot clock inside the sim |
| **Sim to end** | UI streams no lines (or a summary); engine completes the full possession loop immediately — same result as watching every line |

**Halftime:** only affects **watched** runs — caller pauses after half, merges updated prep, resumes second half.

League **batch sim** on **week advance** always uses **sim to end** internally for non-user games.

---

## Why track a shot clock?

Possessions are simulated **discretely** (one trip → one outcome chain), so it is fair to ask why a **30-second** shot clock exists if we are not simulating every dribble.

| Reason | Detail |
|--------|--------|
| **Rule outcomes** | **Shot-clock violation** is a real turnover type in CBB; text log and AST/TO stats need it. |
| **Time used** | Each trip consumes **0–30 s** of **game clock**; without a shot-clock cap, pace tuning cannot match real possession length or **~60–70** trips per team. |
| **Late clock** | When sampled time is high (e.g. 22–30 s), tuning bumps **contested shots**, turnovers, and foul rates — “late clock” behavior without animating the floor. |
| **After OReb** | **20-second** reset creates a different distribution than a fresh **30** — affects extend-the-possession sequences. |
| **Not for UI speed** | Shot clock is **in-sim**; **scaling speed / sim to end** only change how fast the **player reads** the log, not whether the rule exists. |

**What we do *not* do:** sub-second simulation or 24-second real-time countdown in the UI. The shot clock is **state + sampled time used** (or a small number of sub-phases if tuning requires it), not a visible ticking timer unless the UI chooses to display the sampled value on each line.

---

## Tuning data

Balance lives in data files (pace tables, foul rates, shot distributions), not hard-coded. **Canonical file:** [`assets/sim/sim-tuning.json`](../assets/sim/sim-tuning.json) — [ADR-003](../docs/ADR-003-sim-tuning-bundle.md). Game sim reads:

- Game-clock shave distributions
- Shot-clock rules
- Event probability weights vs rating gaps
- Home/neutral modifiers **when caller supplies them**

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Initial draft (over-scoped) |
| 2025-11-08 | Narrowed to possession game sim only |
| 2025-11-08 | Re-sim policy → week-advance save (game design) |
| 2025-11-08 | 30 s shot clock; playback modes; why shot clock in possession sim |
| 2026-11-09 | Season **win contribution** (`winSharesSeason`) for GOAT — computed at End of Season |
