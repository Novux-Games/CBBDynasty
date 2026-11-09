# Injuries & medical redshirt

**Context:** **Random** in-season events during played games. **[`simulation-engine.md`](simulation-engine.md)** rolls occurrences at game end (or per possession sample — see [Sim hook](#sim-hook)); this doc defines **types**, **duration**, **lineup impact**, **news**, and **medical redshirt**.

**Shell:** Optional dashboard tasks — [`dashboard-tasks.md`](dashboard-tasks.md). **News** headlines — [`news-hub.md`](news-hub.md).

**Last updated:** 2026-11-09

---

## Contents

1. [Occurrence](#occurrence)
2. [Sim hook](#sim-hook)
3. [Injury bank](#injury-bank)
4. [Duration & availability](#duration--availability)
5. [Lineup & rotation](#lineup--rotation)
6. [Player UI](#player-ui)
7. [News & league](#news--league)
8. [Medical redshirt](#medical-redshirt)
9. [Data model](#data-model)
10. [Cross-links](#cross-links)

---

## Occurrence

| Rule | Detail |
|------|--------|
| **Nature** | **Random** — not scripted story injuries |
| **When** | Each **completed** team game (user sim/live or batch league sim) |
| **Who** | Players who **logged minutes** in that game; weighted by minutes, fatigue, medical staff grade, prior injuries (balance) |
| **Count** | **0–2** injuries per team per game (cap in balance); league-wide independent rolls |

### Phases

| Phase | Injuries? | Notes |
|-------|-----------|--------|
| **Exhibitions** | Yes (lower rate) | `exhibitionMultiplier` in balance; **do not** count toward RS games played / medical RS |
| **RS Weeks 1–32** | Yes | Full rate; drives medical RS counters |
| **Conference tournament** | Yes | Counts as **RS games played** for medical RS |
| **National postseason** | Yes | Counts as **RS games played** |
| **Offseason / preseason** | No | No injury bank rolls |

---

## Sim hook

**Caller** passes healthy roster into `simulateGame`; engine returns **injury events** on `GameSimResult.injuries[]`.

```
Per completed game (each team):
  for each player with minutes > 0:
    pInjury = baseRate × phaseMultiplier × minutesWeight × fatigueWeight × medicalStaffFactor × historyFactor
    if roll < pInjury and under team cap:
      pick bank row (weighted)
      roll durationGames in [min, max]
      append InjuryEvent { playerId, injuryId, gamesRemaining, severity }
```

| Tunable | Location |
|---------|----------|
| Base rate, caps, weights | `balance-2026.json` → `injuries.occurrence` |
| Catalog | `injuries.bank[]` |

**Mid-game exit (watch mode):** If injury fires before the player is pulled, the **possession log** gets an injury line; the player stops accruing minutes for the rest of that game (DNP‑injury from injury minute onward).

**Batch sim:** Same logic — no separate “injury cheat” for user or AI.

---

## Injury bank

**Authoritative catalog:** [`balance-2026.json`](../assets/balance/balance-2026.json) → `injuries.bank[]`.

| Field | Meaning |
|-------|---------|
| `id` | Stable key |
| `label` | UI copy (e.g. “Ankle sprain”) |
| `severity` | `minor` / `moderate` / `major` — news tier + medical RS eligibility |
| `durationGamesMin` / `durationGamesMax` | **Games missed** (inclusive roll) |
| `weight` | Relative pick weight among rows |

**Re-injury:** New injury while still **OUT** → replace active instance (new `injuryId`, re-roll duration) unless balance `stackPolicy` = `extend` (default **replace**).

---

## Duration & availability

| Rule | Detail |
|------|--------|
| **OUT** | Player **cannot** be in starting five or rotation for games while `gamesRemaining > 0` |
| **Tick** | Each **missed** scheduled game for that team decrements `gamesRemaining` on week advance (or immediately after that game’s sim completes) |
| **Byes** | Injury clock **does not** tick on bye weeks with no game |
| **Return** | At `gamesRemaining == 0`, status → **healthy** before next game prep unless user holds out manually (optional “limited minutes” not v1) |
| **Staff** | High **medical** staff grade: **−1** game on **minor** injuries only (min 1 game if rolled 1) |

### Games played (medical RS counters)

| Counter | Counts |
|---------|--------|
| **`rsGamesPlayed`** | Exhibition **excluded**; **RS + conf tourney + postseason** games where player **appeared** (≥1 minute) |
| **`rsScheduleGameIndex`** | Team’s **Nth** RS game **1–32** on calendar (exhibitions excluded); set when injury is recorded |

---

## Lineup & rotation

| Surface | Behavior |
|---------|----------|
| **Strategy / game prep** | Injured players **greyed**; cannot assign to lineup or rotation slots |
| **Auto-sim** | Engine substitutes next man in rotation; may violate minutes targets |
| **Foul trouble** | Normal foul rules; injured players not eligible to re-enter |
| **Dashboard** | Optional task: **“{Name} out {N} games”** → adjust rotation or open Strategy |

---

## Player UI

| Surface | Content |
|---------|---------|
| **Roster row** | `OUT` chip + `"{label} · {N}g"` |
| **Player card → Overview** | Injury block: label, severity, games remaining, expected return (next game date or “After {opponent}”) |
| **Game log** | `DNP‑INJ` or partial line if hurt mid-game |
| **Edit mode** | No position/NIL edits blocked; cannot toggle “play” while OUT |

See [`player-cards.md`](player-cards.md) — injury strip under identity when active.

---

## News & league

| Tier | Trigger |
|------|---------|
| **User school** | Any user player injury → notification + optional dashboard task |
| **League headline** | **Major** bank injury on player above **overall** threshold, or **Top N** scorer on team; **moderate** for high prestige programs only (balance) |
| **Stories feed** | One-line with team, player, injury label, games out — [`news-hub.md`](news-hub.md) |

---

## Medical redshirt

Separate from **preseason 0-game redshirt** ([`game-design.md`](game-design.md#redshirt-development)).

### Eligibility (all required)

| # | Rule |
|---|------|
| 1 | **Active** injury with `severity` in `medicalRedshirt.qualifyingSeverities` (balance: **moderate**, **major**) |
| 2 | **`rsGamesPlayed` ≤ 15** |
| 3 | If **`rsGamesPlayed` &lt; 15**: **`rsScheduleGameIndex` ≤ 20** at time of injury |
| 4 | Injury recorded while **RS Week ≤ 20** on calendar (equivalent to rule 3 for normal 32-game RS layouts) |
| 5 | **No** applications from **RS Week 21** onward — UI hidden |

### Effect

| Outcome | Detail |
|---------|--------|
| **Season** | Player **OUT** for remainder; **no** further stats; card shows **medical redshirt** for year |
| **Eligibility** | Per 2026 ruleset — clock does **not** pause; copy is hardship/development ([`game-design.md`](game-design.md)) |
| **Scholarship** | Still counts toward **15** until cut window unless user cuts |

### UX flow

1. Injury postgame sheet → **Apply for medical redshirt** (if eligible) or **Dismiss**.
2. Confirmation cites games played + schedule index.
3. On confirm: clear `gamesRemaining` as irrelevant; set `medicalRedshirtSeason: true`; optional news line.
4. **Staff grade:** edge approval only if ambiguous cases (future balance); your rules above are **hard** gates.

### Dashboard

| Task | When |
|------|------|
| **Review injury — {Name}** | User player newly OUT |
| **Medical redshirt — {Name}?** | Optional while eligible (same week as injury or until week 20) |

---

## Data model

**Dynasty save** (per player, current season):

| Field | Type | Notes |
|-------|------|-------|
| `injuryActive` | bool | |
| `injuryId` | string? | Bank id |
| `gamesRemaining` | int | |
| `injuryOccurredAtRsWeek` | int | Calendar week when recorded |
| `injuryOccurredAtRsGameIndex` | int | 1–32 |
| `rsGamesPlayedAtInjury` | int | Snapshot for medical RS audit |
| `medicalRedshirtSeason` | bool | |

**GameSimResult** append:

```text
injuries: [{ playerId, injuryId, gamesRemaining, severity, occurredInGameMinute? }]
```

---

## Cross-links

| Doc | Hook |
|-----|------|
| [`game-design.md`](game-design.md) | Roster, eligibility, redshirt table |
| [`player-cards.md`](player-cards.md) | Injury strip |
| [`simulation-engine.md`](simulation-engine.md) | `GameSimResult.injuries` |
| [`preseason.md`](preseason.md) | Standard redshirt elect |
| [`coach-card.md`](coach-card.md) | Medical staff grade |

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-09 | Initial spec — random bank, duration ranges, medical RS gates |
| 2026-11-09 | Sim hook, lineup, UI, news, data model, phase rules |
