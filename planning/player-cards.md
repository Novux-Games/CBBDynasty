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
| **Position** | **PG / SG / SF / PF / C** (listed position; see [`positions-and-lineup.md`](positions-and-lineup.md)) |
| **Injury** (when active) | `OUT` chip + label + games remaining — [`injuries.md`](injuries.md#player-ui) |

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
| **Archetype** | Primary label from [`player-archetypes.md`](player-archetypes.md) |
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

### NIL valuation

Single **Overview** line — no extra tab.

| Rule | Detail |
|------|--------|
| **What it shows** | **Program NIL valuation** for this player: what you **committed at signing** (recruiting or portal), in abstract **dollars** or **deal points** (one format for the whole game). |
| **Source** | Written when recruit hard-commits / portal player joins; stored on player record. |
| **Display** | e.g. `NIL: $142,000/yr` or `NIL deal: 78 pts` — pick one style in UI theme. |
| **Retention bumps** | If offseason renegotiation raises the deal, update this number; optional small `↑` chip the season it changed. |
| **Visitors** | Scouting opponent: show **valuation** if known (committed deal); uncommitted HS in recruiting stay off roster cards until signed. |
| **Ratings tab** | Unchanged — NIL does not add attribute rows. |

Keeps NIL **legible** without a finance minigame on the card; full collective budget stays in game-design / offseason UI later.

### Projected draft position

From national **Top 60** pool when player is **draft-eligible** and ranked — see [`player-archetypes.md`](player-archetypes.md#draft-projection-overview).

| Display |
|---------|
| `Draft: #N` (1–60) |
| Optional sublabel: Lottery / First round / Early second / Late second |
| Otherwise **`N/A`** |

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

### Loyalty & promise (roster players)

Authoritative rules: [`player-loyalty-promises.md`](player-loyalty-promises.md).

| Field | Placement |
|-------|-----------|
| **Loyalty** | **0–99** on **Ratings** tab; optional compact on **Overview** |
| **Active promise** | **Overview** — type, band, status **Kept / At risk / Broken** |
| **Promise history** | **History** tab — **one** locked promise at commit + per-season **Kept/Broken** evaluations |

---

## Stats

Four cards (stacked vertically). **Three layers:**

| Layer | Tab / card | What it is |
|-------|------------|------------|
| **Each game** | **Games** | That night’s line (PTS, AST, …) + result |
| **This season summary** | **Stats** cards 1–2 | Averages & totals across current season GP |
| **Whole college career** | **Stats** cards 3–4 | Same idea, all seasons at this program / career |

### How the math works

**Games played (GP)** — only games that count toward official stats (week-finalized games per game-design).

**Season averages (card 1)** — for counting stats, one number for the season:

`average = season total ÷ GP`

Examples: PPG = points ÷ GP; **FGM** on this card = season FGM ÷ GP (e.g. “7.2 made per game”); **FG%** = season FGM ÷ season FGA (not an average of weekly percentages).

**Season totals (card 2)** — sums for this season only (whole numbers): points, assists, rebounds, steals, blocks, turnovers. No per-game breakdown here.

**Career averages (card 3)** — **career** rate stats, not “average of each season’s averages”:

| Stat | Formula |
|------|---------|
| PPG, APG, RPG, SPG, BPG, TOPG | **Career total** of that stat ÷ **career GP** (all seasons combined) |
| FG%, 3P%, FT% | **Career** makes ÷ **career** attempts (one blended percentage) |

So career PPG is “how many points per game he’s averaged across every college game in the save,” and career FG% is “lifetime makes ÷ lifetime attempts.” There is **no** career FGM/FGA line on this card — only the percentages, per your spec.

**Career totals (card 4)** — career sums (points, assists, rebounds, steals, blocks, turnovers).

### 1. Season averages

Spell out labels in app (not only abbreviations in UI copy where specified).

| Stat | Meaning on this card |
|------|----------------------|
| Points per game (PPG) | Season points ÷ GP |
| Assists per game (APG) | Season assists ÷ GP |
| Rebounds per game (RPG) | Season rebounds ÷ GP |
| Steals per game (SPG) | Season steals ÷ GP |
| Blocks per game (BPG) | Season blocks ÷ GP |
| Turnovers per game (TOPG) | Season turnovers ÷ GP |
| Field goals made (FGM) | Season FGM ÷ GP |
| Field goals attempted (FGA) | Season FGA ÷ GP |
| Field goal percentage (FG%) | Season FGM ÷ season FGA |
| Three-pointers made (3PM) | Season 3PM ÷ GP |
| Three-pointers attempted (3PA) | Season 3PA ÷ GP |
| Three-point percentage (3P%) | Season 3PM ÷ season 3PA |
| Free throws made (FTM) | Season FTM ÷ GP |
| Free throws attempted (FTA) | Season FTA ÷ GP |
| Free throw percentage (FT%) | Season FTM ÷ season FTA |

Individual **games** stay on the **Games** tab; **season counting totals** (e.g. 412 points) stay on **Season totals**.

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

Career rate stats only (see table above). No career FGM/FGA on this card — shooting volume is implied by career totals + GP, or prior seasons on **History**.

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

Tap row → **box score sheet** for that game (stats line + team score).

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

**Edit** (header) visible when player is on **user roster** and game phase allows ([`strategy-schemes.md`](strategy-schemes.md#strategy-edits--week-save-design-lock--confirmed) — same week-save rules as lineup).

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
| 2025-11-08 | Clarify stats layers: Games vs averages vs totals vs career math |
| 2025-11-08 | NIL valuation on Overview (recruiting commit) |
