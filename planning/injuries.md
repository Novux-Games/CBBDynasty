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

**Authoritative catalog:** [`balance-2026.json`](../assets/balance/balance-2026.json) → `injuries.bank[]` (**40** entries, v2).

| Field | Meaning |
|-------|---------|
| `id` | Stable key |
| `label` | UI copy |
| `bodyPart` | `ankle` / `knee` / `leg` / `foot` / `hand` / `arm` / `head` / `torso` / `illness` — UI + history |
| `severity` | `minor` / `moderate` / `major` — news tier + medical RS eligibility |
| `durationGamesMin` / `durationGamesMax` | **Games missed** (inclusive roll) |
| `weight` | Relative pick weight |

**Mix (tunable):** ~**50%** minor, ~**35%** moderate, ~**15%** major by weight sum.

**Re-injury:** New injury while **OUT** → **replace** instance (`stackPolicy`: `replace`).

### Categories (reference)

| Severity | Examples in bank |
|----------|------------------|
| **Minor** | Ankle/foot tweaks, finger, calf/quad/shin, back/neck, hip pointer, bruised rib, illness/flu/congestion |
| **Moderate** | Sprains (knee/MCL, hamstring, groin, elbow, wrist, high ankle), concussion, shoulder, ribs/oblique, plantar fasciitis, hand fracture, meniscus irritation |
| **Major** | ACL/MCL tear, Achilles, Jones fracture, labrum/dislocation, stress fracture, torn meniscus, broken hand, patellar tendon, season-ending leg |

---

## Duration & availability

| Rule | Detail |
|------|--------|
| **OUT** | Player **cannot** be in starting five or rotation for games while `gamesRemaining > 0` |
| **Tick** | Each **missed** scheduled game for that team decrements `gamesRemaining` on week advance (or immediately after that game’s sim completes) |
| **Byes** | Injury clock **does not** tick on bye weeks with no game |
| **Return** | At `gamesRemaining == 0`, status → **healthy**; **auto-restore lineup** (see below) before next prep unless medical redshirt ended season |
| **Staff** | High **medical** staff grade: **−1** game on **minor** injuries only (min 1 game if rolled 1) |

### Games played (medical RS counters)

| Counter | Counts |
|---------|--------|
| **`rsGamesPlayed`** | Exhibition **excluded**; **RS + conf tourney + postseason** games where player **appeared** (≥1 minute) |
| **`rsScheduleGameIndex`** | Team’s **Nth** RS game **1–32** on calendar (exhibitions excluded); set when injury is recorded |

---

## Lineup & rotation

### Auto-adjust on injury (design lock)

When an injury is **saved** to dynasty state (postgame or batch sim), the **lineup engine** runs immediately for that program — **no** user step required.

```
for each injured player in this event:
  snapshot injuryWasStarterSlot + injuryPriorRotationBand (minutes band / reserve rank)
  if player in startingFive[slot]:
    remove from slot → move to **Reserves** list (roster depth)
    clear rotation minutes target → **0**

for each empty starter slot (PG…C):
  promote best healthy player using [default starter algorithm](positions-and-lineup.md#default-starters-algorithm)
    (eligible + healthy only; same listed-position rules)

rebalance rotation bands: injured pinned to Reserves; promoted starters inherit vacated minute bands when possible
```

| Rule | Detail |
|------|--------|
| **Reserves** | Injured players **always** appear under **Reserves**, not starters — even if they were in the five when hurt mid-game |
| **Promotion** | Same rules as **Reset to default** but **skip** injured and **never** auto cross-list (PG→SG) unless user already did |
| **User override** | Next Strategy visit shows updated five; user may change before following game |
| **Multi-injury** | One pass per event; if multiple starters out, fill slots top-down PG→C in same pass |

### Auto-restore on return (design lock)

When `gamesRemaining` becomes **0** (injury tick after a missed game or same-moment clear for 1-game injuries), run **`applyInjuryReturnLineupAdjust`** for that player — **before** next game prep.

```
if injuryWasStarterSlot is set:
  displaced = current starter in that slot
  startingFive[slot] = returning player
  demote displaced → Reserves (rotation band = typical bench for slot)
  restore returning player's rotation band from injuryPriorRotationBand

else (was reserve / rotation only):
  restore injuryPriorRotationBand
  bump to **top** reserve rotation rank (highest minutes band among non-starters)
```

| Rule | Detail |
|------|--------|
| **Starters** | Returning starter **reclaims** saved slot; interim starter drops to reserves — user is **not** required to swap back manually |
| **Reserves** | Returns to **top of rotation** (prior band, highest among bench) |
| **Conflict** | Multiple returns same week: process **PG → C**; second reclaim may demote first returnee only if still in that slot as interim |
| **Medical RS** | No return adjust — player remains OUT for season |
| **User override** | User may edit Strategy after restore; restore runs again only on **next** clearance |

### Other lineup rules

| Surface | Behavior |
|---------|----------|
| **Strategy / game prep** | Injured **greyed**; cannot drag into lineup or rotation |
| **Auto-sim** | Engine substitutes healthy reserves per rotation; injured not eligible |
| **Foul trouble** | Normal foul rules; injured players not eligible to re-enter |
| **Dashboard** | Optional task: **“{Name} out {N} games”** → Strategy (lineup already adjusted) |

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
| `injuryWasStarterSlot` | enum? | `PG`…`C` if starter when hurt; else null |
| `injuryPriorRotationBand` | int? | Minutes-band rank at injury; drives return-to-top |

**GameSimResult** append:

```text
injuries: [{ playerId, injuryId, bodyPart?, gamesRemaining, severity, occurredInGameMinute? }]
```

**Caller after persist:** `applyInjuryLineupAdjust(teamId, injuredPlayerIds)` — [Auto-adjust on injury](#auto-adjust-on-injury-design-lock).

**Caller on injury clear:** `applyInjuryReturnLineupAdjust(teamId, playerId)` — [Auto-restore on return](#auto-restore-on-return-design-lock).

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
| 2026-11-09 | Auto lineup on injury; bank v1 (25 rows + bodyPart) |
| 2026-11-09 | Bank v2 (40 rows); auto-restore starters + top reserve on return |
