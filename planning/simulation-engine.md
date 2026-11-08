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
9. [Randomness](#randomness)
10. [Watch mode vs fast-sim](#watch-mode-vs-fast-sim)
11. [Tuning data](#tuning-data)

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

### Regulation

- **40:00** college game, **two halves** (20:00 each).
- **35-second** shot clock per possession (reset on change of possession, offensive rebound, etc. per college rules simplified in tuning table).
- **Overtime:** 5:00 periods until winner.

### One possession

1. Determine **five players** on floor per team (rotation logic).
2. Apply **gameplan** weights (pace, shot profile emphasis, foul aggressiveness).
3. Resolve **one terminal event** (or chained events for bonus FTs, and-1, etc.).
4. **Subtract game clock:** **3–30 seconds** per possession depending on event (pace + gameplan shift the distribution).

| Event flavor | Typical game-clock shave |
|--------------|---------------------------|
| Quick turnover | 3–6 sec |
| Quick shot | 8–14 sec |
| Foul / stoppage | 12–18 sec |
| Shot-clock exhaust | up to 30 sec (≤ time left in half) |

5. Update stats, shot clock, foul counts, score; check end of half/game/OT.

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

---

## Prep inputs

Set **before** `simulateGame` runs; frozen for that run.

| Input | Sim use |
|-------|---------|
| **Starting lineup** | Opening minutes each half |
| **Rotation / minutes bands** | Who replaces whom, target minutes |
| **Gameplan** | Pace, inside/outside bias, 3PT rate cap/floor, deny-primary, late-game foul strategy |
| **Optional matchup hint** | Primary defender on opponent’s top usage player |

Coach quality / home environment: if used, caller passes **scalar modifiers** on `GameSimInput` (engine does not load coach career).

---

## Participant data (inputs only)

Final roster/career design lives elsewhere. For **this sim**, each player must provide at least:

| Field | Sim use |
|-------|---------|
| `playerId` | Stat lines |
| `position` | G / F / C — validity, matchup table |
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

## Randomness

- **Between games:** different matchups and rolls → different scores (not “same game every time”).
- **Within a run:** weighted RNG on each possession.
- **Same inputs, new run:** can produce a different result (basketball variance).
- **Whether the player can re-run the same scheduled game** after loading a save is **not** defined here — that is save/dynasty rules in game design.

---

## Watch mode vs fast-sim

**Gameplay (from you):** possession sim with **~2–3 seconds of real time between possession lines** when the user is watching; clock shaves **3–30 seconds of game time** per possession.

**Open (only thing worth answering when you care):**  
Should that **2–3 second delay** be a **user setting** (slow / default / instant text), or one fixed speed? Engine supports both: caller passes `displayMode: WATCH | FAST`; WATCH invokes an optional callback between possessions so the UI can wait.

This doc does **not** set phone performance targets for “advance day” or storage — that is outside **game sim**.

---

## Tuning data

Balance lives in data files (pace tables, foul rates, shot distributions), not hard-coded. Game sim reads:

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
