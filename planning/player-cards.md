# Player cards

**Context:** In-season (and anywhere roster/leaders are tappable) — see [`game-frame.md`](game-frame.md).  
**Stats source:** Box scores from [`simulation-engine.md`](simulation-engine.md).  
**Last updated:** 2025-11-08

---

## Contents

1. [Entry flow](#entry-flow)
2. [Chrome (persistent header)](#chrome-persistent-header)
3. [Tabs](#tabs)
4. [Overview](#overview)
5. [Ratings](#ratings)
6. [Stats](#stats)
7. [Games](#games)
8. [History](#history)
9. [Edit mode](#edit-mode)
10. [Cross-links](#cross-links)

---

## Entry flow

```
Tap player (roster, leaders, box score, etc.)
        │
        ▼
   Player preview (modal / sheet)
   · Quick info (name, position, grade, key ratings)
   · [View more] ──────────────────┐
        │                           │
   dismiss                          ▼
                            Full player card (this doc)
```

**Preview:** fast read; no tab chrome required.  
**Player card:** full-screen (or primary route) with tabs below.

---

## Chrome (persistent header)

Fixed for all tabs (does not scroll away with tab content):

| Element | Placement |
|---------|-----------|
| **Back** | Top-left → closes card / returns to preview caller |
| **Name** | Title center or left-of-center |
| **Edit** | Top-right → [Edit mode](#edit-mode) when allowed (user roster only) |

**Below title row (identity strip):**

| Field | Notes |
|-------|--------|
| **Grade** | Fr / So / Jr / Sr / RS variant as applicable |
| **Headshot or avatar** | Generated portrait; pack-safe |
| **Position** | G / F / C (fixed label for this player) |

---

## Tabs

| Tab | Purpose |
|-----|---------|
| **Overview** | Bio, measurements, headline ratings, draft projection |
| **Ratings** | Full attribute breakdown + potential |
| **Stats** | Season & career aggregates |
| **Games** | Game log this season |
| **History** | Prior seasons summary |

Tab bar sits under identity strip; content scrolls per tab.

---

## Overview

### Team card

- School logo/name, **current season record** (W–L).
- **Tap** → **School hub** for that program ([`game-frame.md`](game-frame.md#school-hub)).
- If player on user team: your school hub; if scouting opponent player: their hub (view-only).

### Information

| Field |
|-------|
| Hometown |
| Grade |
| Position |
| **Archetype** | e.g. Stretch four, Combo guard — display label |
| Experience | Years of college eligibility used / remaining |

### Measurements

| Field |
|-------|
| Height |
| Weight |

### Ratings (summary)

Same numbers as Ratings tab headline (read-only here):

| Field |
|-------|
| **OVR** |
| **OFF** |
| **DEF** |

### Projected draft position

- Shown when applicable (declaree, senior, pro prospect).
- Copy pattern: “Lottery”, “Mid first”, “Early second”, “UDFA”, or **None** / hidden for non-prospects.

---

## Ratings

**Scale:** all displayed ratings **0–99** (integer UI).

### Top card

| Rating |
|--------|
| OVR |
| OFF |
| DEF |

### Attributes

| Group | Detail |
|-------|--------|
| **Shooting (overall)** | Composite of sub-ratings below |
| ↳ Sub | **Inside**, **Mid-range**, **Three-point** (average → Shooting overall) |
| **Handling** | |
| **Passing** | |
| **Rebounding** | |
| **Perimeter defense** | UI label: spell **Perimeter** correctly in app |
| **Interior defense** | |
| **Stealing** | |
| **Blocking** | |

Display: row per attribute; shooting expands or nests three subs.

### Potential

| Field |
|-------|
| **Potential (overall)** | Growth ceiling |
| **Pro potential** | NBA outcome weight / draft stock analog |

---

## Stats

Four cards (stacked vertically; each card is a section).

### 1. Season averages

Spell out labels in app (not only abbreviations in UI copy where specified).

| Stat |
|------|
| Points per game (PPG) |
| Assists per game (APG) |
| Rebounds per game (RPG) |
| Steals per game (SPG) |
| Blocks per game (BPG) |
| Turnovers per game (TOPG) |
| Field goals made (FGM) |
| Field goals attempted (FGA) |
| Field goal percentage (FG%) |
| Three-pointers made (3PM) |
| Three-pointers attempted (3PA) |
| Three-point percentage (3P%) |
| Free throws made (FTM) |
| Free throws attempted (FTA) |
| Free throw percentage (FT%) |

*Note: FGM/FGA/FTM/FTA on averages card are **per-game** made/attempted unless you later switch to efficiency-only; document implementation as per-game averages.*

### 2. Season totals

| Stat |
|------|
| Points |
| Assists |
| Rebounds |
| Steals |
| Blocks |
| Turnovers |

### 3. Career averages

| Stat |
|------|
| PPG |
| APG |
| RPG |
| SPG |
| BPG |
| TOPG |
| FG% |
| 3P% |
| FT% |

### 4. Career totals

| Stat |
|------|
| Points |
| Assists |
| Rebounds |
| Steals |
| Blocks |
| Turnovers |

---

## Games

**This season** game log — one row per game played.

| Column | Content |
|--------|---------|
| Opponent / date | Optional compact date |
| **PTS, AST, REB, STL, BLK, TO** | Line for that game |
| **Result** | W/L + score (e.g. `W 81–76`) |

Tap row → optional: box score for that game or stay on line (TBD).

Only games **after week finalize** appear in official totals; in-week sims follow same week-replay rules as game-design.

---

## History

**Prior seasons** only (current season lives in Stats / Games).

Per season row or card:

| Field (examples) |
|----------------|
| Season year |
| School (if transfer) |
| Games played |
| PPG / key stats line |
| Awards (optional chip) |

Tap → expand season detail or read-only mini stat block.

---

## Edit mode

**Edit** (header) visible when player is on **user roster** and editing is allowed by game phase (strategy/usage/redshirt — rules TBD in strategy doc).

- Overview/Ratings may show read-only with edit routing to Strategy hub or inline editors (implementation).
- Opponent / recruit view-only: **no Edit** button.

---

## Cross-links

| From | To |
|------|-----|
| Overview team card | School hub |
| Player preview | Full card |
| Roster tab, Statistics leaders, box score | Player preview |
| Ratings attribute math | Feeds [`simulation-engine.md`](simulation-engine.md) participant `shooting` + OVR/OFF/DEF |

### Mapping sim ↔ display

| Card | Sim input |
|------|-----------|
| OVR, OFF, DEF | Master weights |
| Shooting subs | `shooting` tendency/skills |
| Handling, passing, etc. | Attribute table driving possession rolls |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Initial player card spec from user |
