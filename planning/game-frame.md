# Game frame — in-season (coach loaded)

**When this applies:** Coach and dynasty exist; player is in **in-season** play (regular season, conference tournament, postseason — not full offseason UI).  
**Companion:** [`game-design.md`](game-design.md) (systems), [`simulation-engine.md`](simulation-engine.md) (game sim).  
**Last updated:** 2025-11-08

---

## Contents

1. [Entry & shell](#entry--shell)
2. [Bottom navigation](#bottom-navigation)
3. [Dashboard (home)](#dashboard-home)
4. [Calendar tab](#calendar-tab)
5. [Roster tab](#roster-tab)
6. [News tab (dashboard)](#news-tab-dashboard)
7. [Top menu (hamburger) & hubs](#top-menu-hamburger--hubs)
8. [School hub](#school-hub)
9. [Coach](#coach)
10. [Strategy hub](#strategy-hub)
11. [Statistics hub](#statistics-hub)
12. [Rankings hub](#rankings-hub)
13. [News hub (full)](#news-hub-full)
14. [History hub](#history-hub)
15. [Cross-cutting behavior](#cross-cutting-behavior)

---

## Entry & shell

```
[New dynasty complete] → Coach created → In-season shell loads
                              │
                              ▼
                    Game frame (this document)
```

- **Persistent chrome:** top bar (hamburger, dynasty context) + **bottom nav** (four tabs).
- **Default tab:** **Dashboard** (center).
- **School context:** Player’s school is default; other schools use **same hub layout** when drilled from rankings, opponents, or game preview (read-only where appropriate).

---

## Bottom navigation

Left → right (center tab = home):

| Tab | Role |
|-----|------|
| **Roster** | Your full roster (starters / reserves / bench) |
| **Calendar** | Your schedule: non-conference & conference games |
| **Dashboard** | Home — overview, notifications, last/next game |
| **News** | **Top headlines only** (league-wide; shallow) |

```
┌─────────────────────────────────────┐
│  ☰  Dynasty: Your School      ···   │  ← hamburger → hubs
├─────────────────────────────────────┤
│                                     │
│           (active tab content)      │
│                                     │
├─────────────────────────────────────┤
│ Roster │ Calendar │ Dashboard │ News │
└─────────────────────────────────────┘
```

---

## Dashboard (home)

### Team overview

At-a-glance card(s) for **your program**:

| Field (examples) | Notes |
|------------------|--------|
| Overall record | W–L |
| National rank | Poll / power index placement |
| Conference rank | League standing |
| Projected seed | Bracketology-style seed line |
| Conference affiliation | From identity pack |
| (extend) | Streak, home/away split, last 10 — optional rows |

### Notifications

- Actionable and informational feed (injuries, portal, coach points Sunday, game day, etc.).
- Tap → relevant screen (roster, game preview, strategy).

### Last game

- Summary line: opponent, score, W/L.
- **Actions:** view **box score**, **team/player stats**, recap.

### Next game

- Opponent name, date, location (H/A/N).
- Entry to **Game preview** sub-menu (see below).

### Game preview sub-menu

Opened from **Next game** (and parallel entry from Calendar before tip).

| Section | Content |
|---------|---------|
| **Win prediction %** | Model from ratings + home + résumé |
| **Predicted score** | Point estimate (e.g. 78–71) |
| **Team preview** | Side-by-side comparison (PPG, RPG, efficiency, pace, key ranks) |
| **Matchup** | **Head-to-head history** vs this opponent (all-time or dynasty-era; W–L, last meetings, scores) |

**Primary CTA from preview:** set **lineup / gameplan** (Strategy hub) → **Sim** when game day (sim rules in game-design / simulation-engine).

---

## Calendar tab

- **Your** games only (not full D-I slate).
- Lists **non-conference** and **conference** games in chronological order.
- Row: date, opponent, location, result if played (in-week tentative) or “upcoming.”
- Tap game → preview (past: box score; future: game preview sub-menu).
- Visual distinction: conference vs non-conference (badge or section headers).

---

## Roster tab

Same roster model as School hub roster, at bottom-nav depth.

| Group | Meaning |
|-------|---------|
| **Starters** | Current default starting five |
| **Reserves** | Rotation (regular minutes) |
| **Bench warmers** | Deep bench / low-minute |

Tap player → player card (stats, role, bio fields — detail doc later).

---

## News tab (dashboard)

**Shallow league feed** — biggest **headlines only** (top N stories).

- Not the full News hub (no draft class board, full recruit list here).
- Tap headline → story detail or deep link into **News hub** when needed.

---

## Top menu (hamburger) & hubs

Hamburger opens **hub picker** (overlay or slide drawer):

| Hub | Purpose |
|-----|---------|
| **School** | Your program (see [School hub](#school-hub)); others when scouting |
| **Strategy** | Scheme, rotation, usage |
| **Statistics** | Leaders (national / conference) |
| **Rankings** | Top 25 + conference standings |
| **News** | Full news product (stories, draft, recruits) |
| **History** | Dynasty & national archive cards |

Bottom tabs remain available; hubs stack or replace main content per navigation pattern (implementation choice).

---

## School hub

**Your school by default.** Same structure when viewing **another school** (from rankings, opponent, game preview “view school”) with read-only/edit rules:

- **Yours:** edit where systems allow (coach link, strategy from hub shortcuts).
- **Others:** view-only stats, roster, schedule, history.

### Tabs / sections

#### Overview

| Block | Content |
|-------|---------|
| **Head coach** | Name, record; tap → **Coach** (preview); edit → Coach tab / contract flows |
| **Info** | Conference, prestige, overall record, conference record, top-25 record, strength of schedule, **power index**, etc. |
| **Rankings** | National + conference placement snapshot |
| **Ratings** | Team offensive/defensive ratings (display layer) |
| **Record** | Current season W–L breakdown |

#### Roster

- Same **starters / reserves / bench warmers** as Dashboard Roster tab.

#### Schedule

- Same as **Calendar tab** content, nested under school context.

#### Statistics

- **Quick stats** — team leaders this season.
- **Program stats** — all-time W–L, tournament runs, **national titles**, etc.

#### History

- **Season history** — quick result rows by year.
- Tap season → deeper season summary (record, postseason, awards).

#### Records

- Program record book (single-game, season, career where tracked).

#### Hall of fame

- Program HOF inductees (dynasty-earned).

#### Drafted players

- Alumni drafted under this coach / program arc (NBA draft results).

---

## Coach

Reached from School overview (head coach tap) or hamburger flow.

| Section | Content |
|---------|---------|
| **Overview** | Portrait, age/tenure, career record at school, skills summary |
| **Contract overview** | Years, salary, buyout, goals |
| **AD trust** | Relationship meter; affects extensions / leash |
| **More** | Skill tree entry, career accolades, (TBD achievements) |

**Edit** path from preview → contract negotiation, skill points (Sunday), extension — tied to game-design carousel/contract systems.

---

## Strategy hub

Pre-sim control surface (no mid-game inputs).

| Section | Behavior |
|---------|----------|
| **Scheme** | Pick **offensive** and **defensive** scheme from **5–6 presets** each (e.g. motion, pick-roll, transition, pack-line, press — names TBD in data) |
| **Rotation** | Lineup management: minute targets, slot starters/reserves; **reset to default** via algorithm (best five + minute distribution by OVR/position) |
| **Usage** | Per-player usage targets (starters / go-to options) feeding sim shot distribution |

Changes apply to **next** sim; in-week replay before week advance can use new strategy.

---

## Statistics hub

### National leaders

Leaderboards:

- **PPG**, **APG**, **RPG**, **SPG**, **BPG**

### Conference leaders

- Sub-menu: **choose conference** → same stat categories for that league only.

Tap player → player card; tap team → School hub (view).

---

## Rankings hub

| View | Content |
|------|---------|
| **Top 25** | National poll-style list |
| **Conference rankings** | Standings per conference (W–L, conf record, tiebreaker note on drill-down) |

Tap team → School hub.

---

## News hub (full)

Superset of dashboard News tab.

| Tab / section | Content |
|---------------|---------|
| **Stories** | Top headlines **with date**; same story copy as dashboard, full list |
| **Draft** | **Top 60** players in national draft pool |
| **Recruits** | **Top 50** recruits nationally |

Offseason recruiting depth may expand here later; in-season focus is board visibility.

---

## History hub

**Year-by-year cards** (scrollable archive). Each year card expands or drills into detail.

| Card type | Examples |
|-----------|----------|
| **Champions** | National champion by season |
| **POTY** | Player of the year |
| **National records** | New record events |
| **National hall of fame** | League-wide HOF entries |
| **GOAT list** | Curated / computed greatest players list (criteria TBD) |

Cards **accumulate** as dynasty seasons complete; national content includes teams beyond user school.

---

## Cross-cutting behavior

| Topic | Rule |
|-------|------|
| **Game sim** | Launch from game preview / calendar on game day; output feeds **last game** on Dashboard |
| **Week save** | Results shown everywhere update for week; **final** after **week advance** ([`game-design.md`](game-design.md)) |
| **Identity pack** | School names, logos, conference labels from pack overlay |
| **Other schools** | School hub pattern reused; edit restricted to user program |
| **Deep news product** | Referenced later; dashboard vs hub split documented above |

### Navigation map (summary)

```
Bottom:  Roster | Calendar | Dashboard | News
Hamburger: School | Strategy | Statistics | Rankings | News | History
           └─ School: Overview | Roster | Schedule | Stats | History | Records | HOF | Drafted
```

### Open (later docs)

- Player card layout
- Offseason frame (recruiting / portal shell)
- Notifications taxonomy
- Scheme preset catalog
- Postseason frame (bracket UI)

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Initial in-season game frame from user spec |
