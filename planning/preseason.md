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

Complete **every** task (any order; default list order below):

| # | Task | Required | Completes when |
|---|------|----------|----------------|
| 1 | **Incoming Class Seed** | Yes | HS/JUCO pools generated + **Top 60** published |
| 2 | **Preseason Workouts** | Yes | User runs **Sim workouts** or reviews auto report |
| 3 | **Coach Upgrades** | Yes | Skill points **spent or explicitly banked** (“Save points”) |
| 4 | **NET + Media & Coaches Poll Review (Preseason)** | Yes | User opens rankings sheet + dismisses / acknowledges |
| 5 | **View Preseason Report** | Yes | User opens report (scroll or **Mark read**) |
| 6 | **Schedule Exhibition Games** | Yes* | *Acknowledge* — **0–3** games scheduled **or** confirm **Skip exhibitions** |
| 7 | **Schedule Regular Season Games** | Yes | Solver valid: **≤32** RS games, conf + non-conf + optional early event ([`ncba-branding.md`](ncba-branding.md)) |

**“Skip exhibitions”** counts as completing task 6.

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

### 6 — Schedule Exhibition Games

| Behavior | Detail |
|----------|--------|
| **Cap** | **0–3** exhibitions (typically **October** window) |
| **UX** | Opponent search, H/A, date picker; exhibitions **do not** count toward record or redshirt games |
| **Skip** | **No exhibitions** → still completes task |
| **Effects** | Morale, light poll momentum, injury risk ([`game-design.md`](game-design.md)) |

### 7 — Schedule Regular Season Games

| Behavior | Detail |
|----------|--------|
| **Conference** | Auto matrix from pack |
| **User** | Fill non-conference slots; **one** early-season event optional (Island / Caribbean / Blue Blood / Desert) |
| **Validation** | **32-game** cap, no double-books, event rules ([`game-design.md`](game-design.md#scheduling--regular-season)) |
| **Complete** | Solver green → lock schedule |

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

Early-season **event** bracket games are part of the **regular-season** calendar (November–December), not exhibitions.

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
