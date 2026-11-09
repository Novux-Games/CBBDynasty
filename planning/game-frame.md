# Game frame — dynasty shell (all phases)

**When this applies:** After the dynasty is created — **offseason**, **preseason**, **exhibition / regular season**, **conference & national postseason**, and **End of Season** review. **One chrome** for the whole career (no separate “offseason app”). **Before this shell:** [`game-start.md`](game-start.md). **March bracket labels & hub content:** [`postseason-frame.md`](postseason-frame.md) (same shell).  
**Companion:** [`game-design.md`](game-design.md) (systems), [`simulation-engine.md`](simulation-engine.md) (game sim), [`player-cards.md`](player-cards.md), [`coach-card.md`](coach-card.md). **Task lists by phase:** [`offseason.md`](offseason.md), [`preseason.md`](preseason.md), [`season-review.md`](season-review.md).  
**Last updated:** 2026-11-09

---

## Contents

1. [Entry & shell](#entry--shell)
2. [Time: week advance only](#time-week-advance-only)
3. [Dashboard task gate](#dashboard-task-gate)
4. [Advance week confirmation (sim week)](#advance-week-confirmation-sim-week)
5. [Bottom navigation](#bottom-navigation)
6. [Dashboard (home)](#dashboard-home)
7. [Calendar tab](#calendar-tab)
8. [Roster tab](#roster-tab)
9. [News tab (dashboard)](#news-tab-dashboard)
10. [Top menu (hamburger) & hubs](#top-menu-hamburger--hubs)
11. [School hub](#school-hub)
12. [Coach](#coach)
13. [Strategy hub](#strategy-hub)
14. [Statistics hub](#statistics-hub)
15. [Rankings hub](#rankings-hub)
16. [News hub (full)](#news-hub-full)
17. [History hub](#history-hub)
18. [Cross-cutting behavior](#cross-cutting-behavior)

---

## Entry & shell

```
[Sign contract] → Offseason Week 1 … → preseason → exhibitions / RS → postseason → End of Season → (loop)
                              │
                              ▼
              Same dynasty shell (this document) — phase label in header only
```

- **Persistent chrome:** top bar (hamburger, dynasty context, **phase + week** label) + **bottom nav** (four tabs) — **unchanged** across offseason, preseason, and season play.
- **Default tab:** **Dashboard** (center).
- **School context:** Player’s school is default; other schools use **same hub layout** when drilled from rankings, opponents, or game preview (read-only where appropriate).
- **No** separate `offseason-frame` / `preseason-frame` UI specs — phase docs describe **tasks and rules** only; layout lives here.

---

## Time: week advance only

**Design lock:** The player advances time by **week** only. There is no **Advance day** control in the UI.

| Rule | Detail |
|------|--------|
| **Checkpoint** | **Advance week** commits the week to the save ([`game-design.md`](game-design.md#when-games-run-vs-when-the-save-commits)) |
| **Within a week** | User completes dashboard tasks, sims games, edits strategy; calendar may still list games by **date** for display — that is not a second advance axis |
| **League sim** | On week advance, batch-sim all other schools’ games scheduled in that **week index** ([`time-system.md`](time-system.md#advance-rules)) |

---

## Dashboard task gate

**Design lock:** Everything the user **can or should** do this week surfaces on the **Dashboard** as a **task** (checklist cards, same pattern all phases). Tasks are tagged **Required** or **Optional**.

| Kind | Advance week |
|------|----------------|
| **Required** | **Disabled** until complete |
| **Optional** | Listed for visibility; **does not** disable advance — unresolved optional work is handled on advance via [sim-week confirmation](#advance-week-confirmation-sim-week) (AI sim + user-school recruiting) |

| Phase | Required before advance | Optional dashboard tasks (examples) |
|-------|-------------------------|-------------------------------------|
| **Offseason** | Weekly obligations per [`offseason.md`](offseason.md) | Extra portal scouting, facilities, etc. when surfaced |
| **Preseason** | Full **8-task** checklist ([`preseason.md`](preseason.md#task-checklist)) | — |
| **Exhibition / RS / conf tourney / postseason** | **None** when on a **bye**; otherwise see note below | **Game** — prep + play/sim when scheduled; **Recruiting** — when cycle is open and week has actionable items ([`recruiting-engine.md`](recruiting-engine.md#in-season-dashboard-tasks)); coach points, injuries, strategy edits as rules surface them |
| **End of Season** | Season review acknowledged → advance to offseason ([`season-review.md`](season-review.md)) | — |

**In-season games:** A scheduled user game always appears as an **optional** task (“Play vs …” / “Sim vs …”). The user may **Advance week** without completing it; advance triggers [sim-week confirmation](#advance-week-confirmation-sim-week) instead of hard-blocking.

Completing a task updates dashboard state immediately; **week save** still commits only on **Advance week** ([`game-design.md`](game-design.md#when-games-run-vs-when-the-save-commits)).

---

## Advance week confirmation (sim week)

**Design lock:** Tapping **Advance week** while **required** tasks are done always runs a **confirmation** step when the week would **auto-resolve** player content the user has not finished manually.

| Condition | Confirmation copy (intent) |
|-----------|----------------------------|
| **Unplayed / unsimmed user game** this week | Warn that advancing will **simulate** the game with the current lineup & gameplan, results become **final**, and the user **cannot return** to replay that game this week |
| **Unresolved optional tasks** (e.g. recruiting drip unspent, watchlist nudge) | Summarize what **AI coach logic** will apply (recruiting auto-maintain / default spend — [`recruiting-engine.md`](recruiting-engine.md#ai-recruiting)) |
| **No open optional work** | Short “Advance to {next week label}?” — still commits save |

**On confirm:**

1. **User games** not yet simmed → run **fast sim** (same engine as manual sim; no live play-by-play).
2. **User-school recruiting** for the week → AI spends weekly drip / NIL per automation settings; if none, sane default board maintenance.
3. **League** — batch-sim all other schools’ games in the week index ([`time-system.md`](time-system.md#advance-rules)) and AI recruiting for non-user programs.
4. **Write save** — week locked; no in-week replay ([`game-design.md`](game-design.md#replays)).

**On cancel:** Stay on current week; optional tasks remain editable.

**Manual path:** User completes optional tasks (play game, recruiting hub) first → confirmation is lighter (no game auto-sim line; recruiting line only if drip still unspent and auto will run).

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
| **T25** | **Media** and/or **Coaches** Top 25 badges when applicable (see Rankings hub) |
| Conference rank | League standing |
| **NCBA NET** | **NET rank** `#NN` — résumé / selection order (**365** teams); primary “where am I for March?” |
| **Media / Coaches** (optional row) | `#NN` when user cares about human polls; compact if both differ from NET |
| Conference affiliation | From identity pack |
| (extend) | Streak, home/away split, last 10 — optional rows |

### Rankings on dashboard

**Design lock — three lists** ([`selection-engine.md`](selection-engine.md)):

| List | Driver | Use |
|------|--------|-----|
| **NCBA NET** | `netScore` formula | Selection Sunday, quads, bubble — **authoritative résumé** |
| **Media Poll** | Sim media voters | **T25 (Media)**, headlines, hype |
| **Coaches Poll** | Sim coaches | **T25 (Coaches)**, respect / peers |

All **365** teams have a **NET #** every week. Media & Coaches also rank **365** internally; UI highlights **#1–#25** per poll.

**Not seeds:** NET / poll ranks are **not** tournament seeds. After Selection Sunday: **NCBA #**, **NCI #**, **HC #** ([`postseason-frame.md`](postseason-frame.md#tournament-seed-labels)). **NET #NN** stays on Rankings through March.

Tap **NCBA NET** (or Rankings entry) → Rankings hub.

### Postseason row (dashboard)

When the user’s team has a national invite: show **`{NCBA|NCI|HC} #{seed}`** (+ NCBA region when set). Replaces any “projected” copy — assigned only on Selection Sunday.

### Notifications

- Actionable and informational feed (injuries, portal, coach points Sunday, game day, etc.).
- Tap → relevant screen (roster, game preview, strategy).

### Last game

- Summary line: opponent, score, W/L.
- **Actions:** view **box score**, **team/player stats**, **postgame report** (mini article — [`preseason.md`](preseason.md#preseason-report--game-reports)), recap.

### Next game

- Opponent name, date, location (H/A/N).
- Entry to **Game preview** sub-menu (see below).

### Game preview sub-menu

Opened from **Next game** (and parallel entry from Calendar before tip).

| Section | Content |
|---------|---------|
| **Win prediction %** | **Team ratings + RPI** — works **with zero games played** |
| **Predicted score** | Same model output (e.g. 78–71) |
| **Team preview** | Side-by-side comparison (PPG, RPG, efficiency, pace, key ranks) |
| **Matchup** | **Head-to-head history** vs this opponent (all-time or dynasty-era; W–L, last meetings, scores) |
| **Pregame report** | Mini article + win % / predicted score ([`preseason.md`](preseason.md#preseason-report--game-reports)) |

**Primary CTA from preview:** set **lineup / gameplan** (Strategy hub) → **Sim** when game day (sim rules in game-design / simulation-engine).

---

## Calendar tab

Week labels follow [`time-system.md`](time-system.md): **Exhibition Week 1–3**, **Week 1–32** (RS), then conf-tourney / postseason week indices. **Bye** weeks show empty with optional “Bye” chip.

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

Tap player → [`player-cards.md`](player-cards.md) preview → View more.

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
| **Recruiting** | Year-long class / portal — opens **Recommended** tab by default ([`recruiting-engine.md`](recruiting-engine.md#recommended-tab-default)); visible when cycle open |
| **Strategy** | Scheme, rotation, usage |
| **Statistics** | Leaders (national / conference) |
| **Rankings** | National **`#1`–`#365`** + **T25** hub + conference standings |
| **News** | Full news product (stories, draft, recruits) |
| **History** | Dynasty & national archive cards |
| **March Mayhem** | National postseason — **NCBA · NCI · HC** brackets & games ([`postseason-frame.md`](postseason-frame.md)); **hamburger only after Selection Sunday** |

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
| **Staff (teaser)** | Head coach name + record; **View staff** → Staff tab |
| **Info** | Conference, **prestige (0–99)**, overall record, conference record, top-25 record, **strength of schedule (0–99)** — same `sosRating` / `programPrestige` as **NET** & seeding ([`selection-engine.md`](selection-engine.md)), **power index**, etc. |
| **Rankings** | **NCBA NET #**, Media #, Coaches # (when published) + conference standing |
| **Ratings** | Team offensive/defensive ratings (display layer) |
| **Record** | Current season W–L breakdown |

#### Staff

Full layout: [`coach-card.md`](coach-card.md#staff-school-hub) (HC + assistants = coach cards; support = list only).

| Block | Content |
|-------|---------|
| **AD trust** | User school only — meter on this tab |
| **Roster** | HC, assistants, support staff |
| **Staff grade** | Aggregate program chip |
| **Manage** | Hire, fire, poach, candidate pool (user school) |

Tap **HC or assistant** → coach card preview. **Support** rows are not tappable — **Manage** only (user school).

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

Full spec: [`coach-card.md`](coach-card.md) (includes **Job security** on Overview & Contract for user HC).

**Entry:** School hub **Staff** (head coach row), coach preview from offers/news, or dynasty shortcuts.

**Preview (sheet):** name, school, record, headline ratings; **[View more]** → full coach card.

**Full card tabs:** Overview · Contract · History · Hall of Fame · Draft.

**Edit** (user HC only): default schemes, skill points (Sunday), contract. **Assistants** use coach card for profile/contract; **support** has no card — Staff tab **Manage** only. Trainers cannot become head coaches.

---

## Strategy hub

Pre-sim control surface (no mid-game inputs).

| Section | Behavior |
|---------|----------|
| **Scheme** | Offense & defense presets — [`strategy-schemes.md`](strategy-schemes.md) |
| **Rotation** | Five slots **PG–C**; minute targets; **reset to default** = best OVR at each **listed** position ([`positions-and-lineup.md`](positions-and-lineup.md); PG↔SG / SF↔PF **manual** only) |
| **Usage** | Per-player usage targets feeding sim shot distribution |
| **Halftime** | Adjust scheme / rotation / usage before 2nd half when watching sim |

Changes apply to **next** sim; in-week replay before week advance can use new strategy.

---

## Statistics hub

### National leaders

Leaderboards:

- **PPG**, **APG**, **RPG**, **SPG**, **BPG**

### Conference leaders

- Sub-menu: **choose conference** → same stat categories for that league only.

Tap player → player preview / card; tap team → School hub (view).

---

## Rankings hub

**Picker:** **NCBA NET** · **Media Poll** · **Coaches Poll** · **Conference standings**

| View | Content |
|------|---------|
| **NCBA NET** | Full **`#1`–`#365`** — formula résumé; **default tab** for bubble / Selection Sunday |
| **Media Poll** | Full **`#1`–`#365`**; **T25 (Media)** highlight on **#1–#25** |
| **Coaches Poll** | Full **`#1`–`#365`**; **T25 (Coaches)** highlight on **#1–#25** |
| **Conference** | Standings per conference (W–L, conf record, tiebreaker on drill-down) |

**News** may quote Media or Coaches T25; **Selection Sunday** reads **final NET only** ([`postseason.md`](postseason.md)).

Tap team → School hub.

---

## News hub (full)

Superset of dashboard News tab.

| Tab / section | Content |
|---------------|---------|
| **Stories** | **Headlines only** (with date) — obvious types: upsets, rankings, injuries, commitments, bracket, etc. |
| **Draft** | **Top 60** draft pool — **dynamic** as season/pro days evolve |
| **Recruits** | **Top 60** incoming class — seeded **preseason**; live **all regular season**; see [`recruiting-engine.md`](recruiting-engine.md) |

**Draft** tab = pro/draft pool; **Recruits** tab = high school / JUCO recruit rankings (not portal).

---

## History hub

**Year-by-year cards** (scrollable archive). Each year card expands or drills into detail.

| Card type | Examples |
|-----------|----------|
| **Champions** | National champion by season |
| **POTY** | Player of the year |
| **National records** | New record events |
| **National hall of fame** | League-wide HOF entries |
| **GOAT list** | **Computed** dynasty archive ranking — composite of win shares, national titles as key contributor, POTY, All-America teams, peak OVR seasons; **top 25** overall + **top 5** per position; refreshes each offseason |

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
Hamburger: School | Strategy | Statistics | Rankings | News | History | March Mayhem (post–Selection Sunday)
           └─ School: Overview | Staff | Roster | Schedule | Stats | History | Records | HOF | Drafted
```

### Phase companions (tasks & rules — not separate layouts)

| Doc | Covers |
|-----|--------|
| [`offseason.md`](offseason.md) | Offseason weeks, carousel, portal/recruiting |
| [`preseason.md`](preseason.md) | Preseason task checklist |
| [`postseason-frame.md`](postseason-frame.md) | Tournament seed labels, March Mayhem **hub content** (same shell) |
| [`postseason.md`](postseason.md) | Selection Sunday, brackets |
| [`selection-engine.md`](selection-engine.md) | Poll + Seed Index |
| [`transfer-portal-engine.md`](transfer-portal-engine.md) | *Planned* — portal cycles, or fold into [`recruiting-engine.md`](recruiting-engine.md) |

Scheme catalog: [`strategy-schemes.md`](strategy-schemes.md). Notifications: [`game-design.md`](game-design.md#notifications-android).

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Initial in-season game frame from user spec |
| 2025-11-08 | Link player cards doc |
| 2025-11-08 | School Staff section; coach card doc |
| 2025-11-08 | GOAT list formula; companion doc index (offseason/postseason/portal) |
| 2025-11-08 | Recruit Top 60: preseason seed, in-season News |
| 2025-11-08 | Dashboard: Current ranking; full national list in Rankings |
| 2025-11-09 | Poll = full #1–365 rank; seeds postseason-only |
| 2025-11-09 | Split NCBA NET vs Media & Coaches polls |
| 2025-11-09 | Coach card job security (no separate coach hub) |
| 2025-11-09 | Removed tier color chips from UI spec |
| 2025-11-09 | T25 highlight for #1–25; all 365 ranked every week |
| 2025-11-08 | School Staff tab; AD trust; staff detail sheets |
| 2026-11-09 | Dynasty shell all phases; dashboard task gate; week advance only |
| 2026-11-09 | Required vs optional tasks; sim-week advance confirmation; in-season recruiting tasks |
| 2025-11-08 | Assistants = coach cards; support list-only |
