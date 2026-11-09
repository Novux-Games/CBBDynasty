# Preseason — single-week setup

**When:** After [`offseason.md`](offseason.md) completes (Week 8 on Season 1, Week 9 on later seasons). **One week** of setup tasks — then **regular season** (exhibitions first if scheduled).

**UI companion (planned):** [`preseason-frame.md`](preseason-frame.md). **Recruiting seed:** [`recruiting-engine.md`](recruiting-engine.md). **Rankings:** [`selection-engine.md`](selection-engine.md). **In-season shell:** [`game-frame.md`](game-frame.md).

**Last updated:** 2025-11-09

---

## Contents

1. [Overview](#overview)
2. [Task checklist](#task-checklist)
3. [Task detail](#task-detail)
4. [Preseason report & game reports](#preseason-report--game-reports)
5. [Advance to regular season](#advance-to-regular-season)
6. [Exhibitions vs regular season start](#exhibitions-vs-regular-season-start)
7. [Cross-links](#cross-links)

---

## Overview

| Rule | Detail |
|------|--------|
| **Length** | **Single week** — no multi-week preseason calendar |
| **Gate** | **All tasks complete** → **Start season** enabled |
| **Save** | `preseasonComplete: false` until gate; checkpoint after each task |
| **Season 1** | Same checklist after first offseason (Weeks 1–8) |
| **Redshirt** | Elect **0-game redshirts** during **Preseason Workouts** or roster review (exhibitions do not count — [`game-design.md`](game-design.md)) |

**Shell:** Offseason chrome → **Preseason** header with task list + week-less progress ring.

---

## Task checklist

Complete **every** task. Tasks **1–5** in any order; **Tournament Invites (6)** must finish **before** scheduling tasks **7–8**.

| # | Task | Required | Completes when |
|---|------|----------|----------------|
| 1 | **Incoming Class Seed** | Yes | HS/JUCO pools generated + **Top 60** published |
| 2 | **Preseason Workouts** | Yes | User runs **Sim workouts** or reviews auto report |
| 3 | **Coach Upgrades** | Yes | Skill points **spent or explicitly banked** (“Save points”) |
| 4 | **NET + Media & Coaches Poll Review (Preseason)** | Yes | User opens rankings sheet + dismisses / acknowledges |
| 5 | **View Preseason Report** | Yes | User opens report (scroll or **Mark read**) |
| 6 | **Tournament Invites** | Yes | Accept **one** invite, **decline all**, or acknowledge **No invites available** |
| 7 | **Schedule Exhibition Games** | Yes* | *After task 6* — **0–3** games **or** **Skip exhibitions** |
| 8 | **Schedule Regular Season Games** | Yes | *After task 6* — solver valid ([§ Schedule regular season](#8--schedule-regular-season-games)) |

**“Skip exhibitions”** counts as completing task 7.

---

## Task detail

### 1 — Incoming Class Seed

| Behavior | Detail |
|----------|--------|
| **System** | Roll **2K–4K HS** + **1.5K JUCO**; stars, hidden ratings, geography ([`recruiting-engine.md`](recruiting-engine.md)) |
| **Output** | **Top 60** board in **News** for the **upcoming regular season** (and visible through next offseason spend) |
| **User** | **Run seed** / watch progress → **Done** when Top 60 headline publishes |
| **Season 1** | Same task — first class seed before year-one games |

Committed recruits from offseason signing appear on roster preview; seed is the **national** pool for scouting, not re-rolling commits.

### 2 — Preseason Workouts

| Behavior | Detail |
|----------|--------|
| **Scope** | Returning roster (+ new arrivals on scholarship) |
| **Sim** | Attribute / readiness deltas (coach **Training**, facilities) |
| **User** | **Sim workouts** or accept auto result |
| **Optional** | Redshirt designations on player rows before confirm |

Distinct from **offseason Week 6** summer workouts — preseason is tune-up before games.

### 3 — Coach Upgrades

| Behavior | Detail |
|----------|--------|
| **Points** | Unspent skill points + any preseason grant |
| **UI** | Skill tree ([`game-design.md`](game-design.md#coaching-carousel--contracts)); **auto-spend** if dynasty setting enabled |
| **Complete** | All points allocated **or** user taps **Bank for season** (explicit opt-in to save) |

### 4 — NET + Media & Coaches Poll Review (Preseason)

| Behavior | Detail |
|----------|--------|
| **Publish** | **Preseason** NET order (`#1`–`#365`) + **Media** & **Coaches** polls from roster/prior résumé ([`selection-engine.md`](selection-engine.md)) |
| **UI** | Read-only hub slice: your **NET #**, poll ranks, **T25** chips |
| **Note** | **In-season NET** recomputes after games; this is **pre-tip** expectation |
| **Complete** | User opens sheet and taps **Continue** |

### 5 — View Preseason Report

| Behavior | Detail |
|----------|--------|
| **Format** | **Mini article** — newsletter / beat-writer tone |
| **Includes** | Roster outlook, key returners, schedule difficulty teaser, **projected W–L** for upcoming season (model: roster OVR + SOS prior + poll prior) |
| **Complete** | Open report → **Mark read** |

See [§ Preseason report & game reports](#preseason-report--game-reports).

### 6 — Tournament Invites

Early-season **holiday events** ([`ncba-branding.md`](ncba-branding.md#early-season-holiday-events-regular-season)) — **not** part of the regular-season picker until this task resolves.

| Prestige / résumé | UX |
|--------------------|-----|
| **Low** | **“No invites available.”** — task completes on **Acknowledge** (no bracket) |
| **Mid** | **0–1** invite cards |
| **High** | **Multiple** invites — user picks **at most one** (Island Classic, Caribbean Showdown, Blue Blood Tip-Off, Desert Shootout) |

| Choice | Effect |
|--------|--------|
| **Accept** | Bracket reserved (**up to 4** games); counts toward **32** RS cap; **only 28** other RS games allowed (conf + non-conf in that pool) |
| **Decline all** / none offered | Full **32** RS slots remain for conference + non-conference |

**Gate:** Tasks **7** and **8** stay locked until task **6** is complete.

### 7 — Schedule Exhibition Games

| Behavior | Detail |
|----------|--------|
| **Cap** | **0–3** exhibitions (typically **October** window) |
| **UX** | Opponent search, H/A, date picker; exhibitions **do not** count toward record or redshirt games |
| **Skip** | **No exhibitions** → still completes task |
| **Effects** | Morale, light poll momentum, injury risk ([`game-design.md`](game-design.md)) |

### 8 — Schedule Regular Season Games

**Prerequisite:** Task **6** (tournament invites) complete.

| Behavior | Detail |
|----------|--------|
| **Conference games** | **Shown** on calendar builder from pack template (`leagueGameCount`, opponents, **home/away / repeat**). **Double-scheduling allowed** — e.g. play the same conference opponent **twice** when template specifies (mirrors real 2026–27 slates) |
| **Non-conference games** | **Slot count varies** by **conference + program** — not a flat number for all teams ([`game-design.md`](game-design.md#scheduling--regular-season)) |
| **User** | Fill **remaining** non-conf opponents within allowed slots (Nov–Dec windows; limited January for some leagues) |
| **Tournament** | If accepted in task 6, bracket games **pre-injected**; user builds only within **28** non-event RS games + those 4 |
| **Cap math** | **≤32** total RS games = conference + non-conference + tournament (max 4). Exhibitions **separate** |
| **Validation** | Solver green — no over-cap, no date conflicts, respects per-team non-conf budget |

```
if (tournamentAccepted)
  maxOtherRS = 28   // conf + non-conf share this pool (+ 4 event = 32)
else
  maxRS = 32
nonConfSlots = maxOtherRS_or_maxRS − conferenceGamesLocked
```

**Complete** → lock schedule.

---

## Preseason report & game reports

**Design lock:** Same **mini-article** component used for:

| Report | When |
|--------|------|
| **Preseason report** | Preseason task 5 |
| **Pregame report** | Game preview ([`game-frame.md`](game-frame.md#game-preview-sub-menu)) — opponent, keys, **projected score** / win % |
| **Postgame report** | After sim — recap, standout lines, poll/NET movement note |

Shared fields: headline, 2–4 paragraphs, optional pull quote, **projected or final score**, record line.

**Preseason report** uniquely includes **season-long projected W–L** and **Top 60 / portal** one-liner.

Implementation: one `ReportArticle` template; `reportType: preseason | pregame | postgame`.

---

## Advance to regular season

**CTA:** **Start season** (enabled when checklist 100%).

| Step | Effect |
|------|--------|
| Set `preseasonComplete: true` | Exit preseason shell |
| Load **`game-frame.md`** in-season UI | Bottom nav live |
| Calendar focus | **First scheduled game** — exhibition if any, else **first regular-season** tip |

No automatic sim — user drives first game from **Calendar** / **Dashboard → Next game**.

---

## Exhibitions vs regular season start

```
IF exhibitions.length > 0
  → first playable = earliest exhibition
ELSE
  → first playable = earliest regular-season game
```

Early-season **event** bracket games (from **Tournament Invites**, task 6) are **regular-season** games (November–December), not exhibitions.

---

## Cross-links

| Topic | Doc |
|-------|-----|
| Offseason exit | [`offseason.md`](offseason.md) |
| Recruit seed math | [`recruiting-engine.md`](recruiting-engine.md) |
| Early events | [`ncba-branding.md`](ncba-branding.md) |
| Game preview stats | [`game-frame.md`](game-frame.md) |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-09 | Single-week preseason + 7 tasks + report pattern |
| 2025-11-09 | Tournament Invites task; 28-game cap when in event; conf repeats |
