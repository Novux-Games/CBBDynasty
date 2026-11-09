# In-season systems — discipline, chemistry, fatigue

**Context:** RS through postseason — complements **injuries** ([`injuries.md`](injuries.md)). **Discipline** uses **games** missed; injuries use **calendar weeks** — do not mix counters.

**Shell:** Required **dashboard decision** sheets block **Advance week** until resolved ([`dashboard-tasks.md`](dashboard-tasks.md)). **In-app feed** on new events ([`notifications.md`](notifications.md)).

**Last updated:** 2026-11-09 · **Status:** Design lock.

---

## Contents

1. [Discipline events (shared engine)](#discipline-events-shared-engine)
2. [Conduct / suspension incidents](#conduct--suspension-incidents)
3. [Academic eligibility incidents](#academic-eligibility-incidents)
4. [Chemistry](#chemistry)
5. [Fatigue](#fatigue)
6. [Lineup & sim](#lineup--sim)
7. [AD trust, job security, news](#ad-trust-job-security-news)
8. [Data model](#data-model)
9. [Balance keys](#balance-keys)
10. [Cross-links](#cross-links)

---

## Discipline events (shared engine)

### When

| Rule | Detail |
|------|--------|
| **Roll** | On **week advance** (after games finalize), per **user program** + league AI batch |
| **Frequency** | **Random** — not every week — `baseChancePerTeamWeek` in balance (default **~0.06**) |
| **Cap** | Max **3** discipline resolutions per team per RS season (conduct + academic combined) |
| **Phases** | **RS Weeks 1–32**, conf tourney, postseason — **no** exhibitions, offseason, preseason |

### UX (user school)

1. **Dashboard required card** — *“Team discipline — {headline}”* (blocks advance).
2. Full-screen **decision sheet** with flavor text + player card header.
3. Resolve → feed row + optional **News** (user school boost if scandal).

AI programs auto-resolve with personality weights (no UI).

### Games suspended counter

| Field | Behavior |
|-------|----------|
| `gamesSuspendedRemaining` | Decrement **1** per **team game missed** (player ineligible for that game) |
| **Byes** | **Do not** decrement — same as missed game (player would not play anyway) |
| **Return** | At **0**, eligible next prep; **no** auto lineup special case (user sets rotation) |

Suspended players: **greyed** in Strategy; cannot start or rotate.

---

## Conduct / suspension incidents

### Event pool (examples)

Weighted pick from balance `discipline.conductTemplates[]`:

| `id` (example) | Flavor |
|----------------|--------|
| `arrest` | *“PG {Name} was arrested last night.”* |
| `campus_conduct` | *“{Name} involved in an on-campus incident.”* |
| `team_rules` | *“{Name} violated team rules — undisclosed.”* |

**Who:** Random roster player with **≥ 5** RS games played this season (weighted toward higher minutes).

### Options (design lock)

| Option | Effect |
|--------|--------|
| **Dismiss from team** | Cut **now**; scholarship counts toward cap until offseason cut window; loyalty N/A; News optional |
| **Suspend** | User picks **games** **1–10**; set `gamesSuspendedRemaining`; player stays on roster |
| **Delegate to conference** | **Conference office** resolves — animated **coin-flip / draw** UI |

### Conference draw outcomes

| Outcome | Weight (default) | Result |
|---------|------------------|--------|
| **Cleared** | 25% | No games; `eligibilityIncident` flag cleared |
| **Short suspension** | 45% | **1–3** games (uniform roll) |
| **Season suspension** | 20% | **8–16** games |
| **Dismissed by conference** | 10% | Same as cut; conference copy |

Weights in `discipline.conferenceDraw`.

---

## Academic eligibility incidents

### Timing

| Rule | Detail |
|------|--------|
| **Primary window** | **RS Weeks 18–22** — models second-semester academic check ( **not** labeled “semester” in UI) |
| **Outside window** | **5%** of academic rolls still fire other weeks (plausible mid-year report) |
| **Minimum** | Player **≥ 12** RS credits story / **Sophomore+** grade label preferred |

### Trigger copy (example)

*"C {Name}'s GPA is **1.15**, under the **2.0** eligibility requirement."*

Per-player **`gpaCurrent`** (0.00–4.00) hidden until incident or **Academic Support**; **`gpaRequired`** = **2.0** (balance).

### Options (design lock)

| Option | Effect |
|--------|--------|
| **Sweep under the rug** | Player **eligible immediately**; sets `academicCoverUp: true` on season |
| **Suspend — games** | User picks **1–10** games; `gamesSuspendedRemaining` |
| **Suspend until eligible + Academic Support** | `academicHold: true` — **OUT** until `gpaCurrent ≥ gpaRequired`; enroll in support ([below](#academic-support)) |

### Sweep under the rug — risk

| Rule | Detail |
|------|--------|
| **Audit roll** | **End of Season week**: `P(caught) = baseAudit × (1 - academicFacilityBonus)` |
| **If caught** | **AD trust −15**; **job security −10**; **News** headline; counts toward program **eligibility incidents** ([`game-design.md`](game-design.md#academic-grades-program)) |
| **If not caught** | No stat change |

### Academic Support

While `academicHold`:

| Rule | Detail |
|------|--------|
| **Weekly tick** | On week advance: `gpaCurrent += weeklyGpaGain` |
| **Gain** | Base **0.08** + **`academicFacilityGrade`** bonus + staff **Academic** coordinator rating |
| **Return** | When `gpaCurrent ≥ 2.0`, clear hold; feed row *“{Name} cleared academically”* |
| **Games** | Still miss games while on hold (counter not used — hold is the gate) |

**Program academics grade** (spring) still uses team GPA aggregate — individual fixes help long-term story.

---

## Chemistry

**Team chemistry** `teamChemistry` **0–100**, shown on **School hub → Overview** (user school) and Strategy prep strip.

### Inputs (recomputed weekly after advance)

| Input | Effect |
|-------|--------|
| **Win streak** (3+) | +2 per week capped +10 |
| **Losing streak** (3+) | −3 per week capped −12 |
| **Promise broken** (season) | −8 once |
| **Portal returner / new rotation** | −4 first 4 weeks after mass roster change |
| **Stable starting five** (same 5, 6+ weeks) | +1/week capped +6 |
| **Coach charisma** skill | Small flat bonus |

Clamp **0–100**. Start season **70** for new save.

### Sim use

| Band | Clutch / close-game modifier |
|------|------------------------------|
| **80+** | +1.5% win prob in clutch possessions |
| **50–79** | Neutral |
| **&lt;50** | −2.0% clutch; slightly higher turnover rate |

Does **not** block lineup changes.

---

## Fatigue

Per-player **`stamina`** **0–100** (fresh → exhausted).

### Drain (per game played)

```
staminaAfterGame = staminaBefore
  - baseDrainPerMinute × minutesPlayed
  - pressSchemeDrain if full-court press heavy
  - backToBackPenalty if team plays 2+ games same week
```

### Recovery

| When | Amount |
|------|--------|
| **Week advance** (no game) | +25 (bye) |
| **Week advance** (after game) | +15 between games same week |
| **Off week** | Full **100** at offseason |

### Sim use

| Stamina | Effect |
|---------|--------|
| **&lt;35** | Auto-rotation pulls player earlier; injury `fatigueWeight` ↑ |
| **&lt;20** | Cannot assign as starter next game unless user override (warning) |

**Medical / strength staff** reduces drain; see balance `fatigue.*`.

---

## Lineup & sim

| State | Lineup |
|-------|--------|
| **Injured** (`weeksRemaining`) | Per [`injuries.md`](injuries.md) |
| **Suspended** (`gamesSuspendedRemaining` or `academicHold`) | Ineligible; greyed; no minutes |
| **Fatigue** | Eligible but sim may limit minutes |

Discipline does **not** trigger injury auto-lineup.

---

## AD trust, job security, news

| Event | AD trust | Job security | News |
|-------|----------|--------------|------|
| Dismiss star | −5 to −12 (OVR-based) | −3 | Optional user story |
| Long suspension | −2 | −1 | Rare |
| Cover-up **caught** | −15 | −10 | Yes |
| Conference dismiss | −8 | −5 | Yes |

Feed row on every **user** discipline sheet start ([`notifications.md`](notifications.md)).

---

## Data model

**Per player (season):**

| Field | Notes |
|-------|-------|
| `gamesSuspendedRemaining` | int |
| `academicHold` | bool |
| `gpaCurrent` | float |
| `academicCoverUp` | bool |
| `stamina` | 0–100 |
| `conductIncidentId` | string? |

**Per team:**

| Field | Notes |
|-------|-------|
| `teamChemistry` | 0–100 |
| `disciplineEventsResolved` | int (season cap) |

---

## Balance keys

`balance-2026.json` → `discipline`, `chemistry`, `fatigue` (authoritative numbers).

| Block | Keys |
|-------|------|
| `discipline.occurrence` | `baseChancePerTeamWeek`, `maxPerSeason`, `academicWindowWeeks` |
| `discipline.conductTemplates` | `id`, `label`, `weight` |
| `discipline.conferenceDraw` | outcome weights, game ranges |
| `discipline.academic` | `gpaRequired`, `weeklyGpaGain`, `auditBaseChance` |
| `chemistry` | streak deltas, clutch modifiers |
| `fatigue` | drain, recovery, press bonus |

---

## Cross-links

| Doc | Hook |
|-----|------|
| [`injuries.md`](injuries.md) | Weeks vs games |
| [`dashboard-tasks.md`](dashboard-tasks.md) | Required discipline card |
| [`coach-card.md`](coach-card.md) | AD trust, job security |
| [`simulation-engine.md`](simulation-engine.md) | Fatigue, chemistry clutch |
| [`game-design.md`](game-design.md) | Program academics grade |

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-09 | Discipline events, chemistry, fatigue — product locks |
