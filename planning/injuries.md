# Injuries & medical redshirt

**Context:** Random in-season events during games (and optionally exhibitions — tune in balance). **[`simulation-engine.md`](simulation-engine.md)** rolls occurrences; this doc defines **types**, **duration**, and **medical redshirt** eligibility.

**Shell:** Optional dashboard task when a user player is hurt — [`dashboard-tasks.md`](dashboard-tasks.md). **News** may headline notable injuries ([`news-hub.md`](news-hub.md)).

**Last updated:** 2026-11-09

---

## Contents

1. [Occurrence](#occurrence)
2. [Injury bank](#injury-bank)
3. [Duration & availability](#duration--availability)
4. [Medical redshirt](#medical-redshirt)
5. [Cross-links](#cross-links)

---

## Occurrence

| Rule | Detail |
|------|--------|
| **Nature** | **Random** per game (and tunable exhibition risk) — not scripted story injuries |
| **Who** | Any player in rotation; weight by minutes, fatigue, medical staff grade, injury history (balance) |
| **Output** | Active **injury instance**: `injuryId` from bank, **games/weeks remaining** out, return ETA on card |

---

## Injury bank

**Authoritative catalog:** [`balance-2026.json`](../assets/balance/balance-2026.json) → `injuries.bank[]`.

Each entry:

| Field | Meaning |
|-------|---------|
| `id` | Stable key |
| `label` | UI copy (e.g. “Ankle sprain”) |
| `severity` | `minor` / `moderate` / `major` (headline + sim weight) |
| `durationGamesMin` / `durationGamesMax` | Typical **games missed** (rolled inclusive) |
| `durationWeeksMin` / `durationWeeksMax` | Optional cap when team has **byes** — engine uses **games missed** first |

On roll, pick a bank row (weighted), sample duration in range, set player **OUT** until counter expires or return cleared by staff events.

---

## Duration & availability

| Rule | Detail |
|------|--------|
| **Missed games** | Player **cannot** appear in lineup for injured interval |
| **Advance** | Injury clock ticks on **week advance** when a missed game falls in that week |
| **Staff** | Medical grade may shave **1** game off minor injuries (balance toggle) |
| **Sim** | Live game may end early for player (DNP‑injury in box) |

---

## Medical redshirt

**Design lock** — separate from **preseason 0-game redshirt** ([`game-design.md`](game-design.md#redshirt-development)).

Apply **during the injury flow** when staff/user requests **medical redshirt** for the season.

### Eligibility (all required)

| # | Rule |
|---|------|
| 1 | Player has an **active** qualifying injury from the bank (typically **moderate** or **major** — minors excluded in balance) |
| 2 | **RS games completed** this season: **≤ 15** (has **not** already played more than 15 RS games) |
| 3 | **If** RS games played **&lt; 15**: injury must occur on or before the team’s **20th RS game** on the season schedule (still within the early-season window) |
| 4 | **Calendar:** **No** medical redshirt applications from **RS Week 21** onward ([`time-system.md`](time-system.md) — regular season weeks **1–32**) |

**Week 21+** hard-stops the UI even if games-played tests would pass.

### Effect

| Outcome | Detail |
|---------|--------|
| **Season** | Current season treated as **medical redshirt** — **0** further RS games count toward stats; development year on card |
| **Eligibility clock** | Per [`game-design.md`](game-design.md#roster-eligibility--redshirt) — **2026 ruleset**: clock does **not** pause; copy is **medical hardship / development**, not a free fifth year unless balance adds waiver fiction |
| **Staff** | **Medical** staff grade improves approval odds on edge cases (balance) |

### Dashboard

When a user player becomes eligible, surface **optional** task: review injury → **Apply for medical redshirt** or **Return when healthy** ([`dashboard-tasks.md`](dashboard-tasks.md)).

---

## Cross-links

| Doc | Hook |
|-----|------|
| [`game-design.md`](game-design.md) | Roster, eligibility, redshirt table |
| [`player-cards.md`](player-cards.md) | Injury chip, games played |
| [`simulation-engine.md`](simulation-engine.md) | In-game injury roll |
| [`preseason.md`](preseason.md) | Standard redshirt elect |

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-09 | Initial spec — random bank, duration ranges, medical RS gates |
