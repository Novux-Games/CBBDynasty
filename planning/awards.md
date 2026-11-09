# Awards — players & coaches

**Context:** Voted-style honors computed by the sim (no user ballot). **Season win contribution** ([`simulation-engine.md`](simulation-engine.md#season-win-contribution-design-lock)) is the primary “helped the team win” input for **player** awards.  
**When:** Mostly **End of Season week**, after postseason and **win contribution** rollup; **weekly** honors during regular season only.  
**Surfaces:** News, History hub, School hub season lines, player/coach cards, season review.  
**Companion:** [`history-hub.md`](history-hub.md) (HOF, GOAT), [`game-design.md`](game-design.md#awards--media) (cadence summary), [`news-hub.md`](news-hub.md).

**Last updated:** 2026-11-09 · **Status:** Design lock.

### Player-facing names (design lock)

Keep award UI **short**. **Never** show win contribution, formulas, or “shares” to the player.

| UI label | Meaning |
|----------|---------|
| **Team MVP** | Best player on **that program** this season (one per team). |
| **NCBA MVP** | National player of the year (one per season). |
| **NCBA {PG\|SG\|SF\|PF\|C} POY** | Best at that **listed position** nationally (NCBA only). |
| **Conference POY** | Conference player of the year (one per conference). |
| **All-Conference** / **All-America** | Team selections (chips or lists — no stat breakdown). |
| **Conference Coach of the Year** / **NCBA Coach of the Year** | Coach honors. |

Save/archival fields may use internal ids (e.g. `nationalPoty`); **copy always** uses **NCBA MVP** where the national player award appears ([`ncba-branding.md`](ncba-branding.md)).

---

## Contents

1. [Season win contribution (shared input)](#season-win-contribution-shared-input)
2. [Eligibility gates](#eligibility-gates)
3. [Team honors](#team-honors)
4. [Conference awards](#conference-awards)
5. [National awards](#national-awards)
6. [NCBA position player of the year](#ncba-position-player-of-the-year)
7. [Weekly honors (regular season)](#weekly-honors-regular-season)
8. [Coach awards](#coach-awards)
9. [Awards hub UI](#awards-hub-ui-design-lock)
10. [Archive & runner-ups](#archive--runner-ups)
11. [Other surfaces](#other-surfaces)
12. [Cross-links](#cross-links)

---

## Season win contribution (shared input)

| Rule | Detail |
|------|--------|
| **What it is** | Hidden per-player, per-season **credit for team wins** (minutes + production); roster sums to team W — see [spec](simulation-engine.md#season-win-contribution-design-lock). |
| **Awards use** | Primary **success contribution** signal for POTY, All-Conference, All-America, and **team MVP**. |
| **Not the only input** | Stats (PPG, efficiency), team finish, postseason run, and overall rating break ties and gate eligibility. |

---

## Eligibility gates

| Award tier | Minimum (default) |
|------------|-------------------|
| **Team MVP** | **≥ 1** minute; top share on roster |
| **All-Conference** | **≥ 50%** of team games played (RS + conf + national postseason), **≥ 8 MPG** season average |
| **Conference POTY** | Same + team **top 4** conference finish **or** conference tournament champion |
| **All-America** | **≥ 60%** GP, **≥ 12 MPG**; team **Selection Sunday** bid **or** **Top 60** draft rank band |
| **NCBA MVP** | All-America eligible + **top 15** national in composite award score (below) |

Tune floors in `balance-2026.json` → `awards.eligibility`.

---

## Team honors

Per program, computed at **End of Season** (same pass as win contribution).

| Honor | Rule |
|-------|------|
| **Team MVP** | Player with **highest** win contribution on that roster (computed — not shown). Tie → MPG, then PTS. |
| **Display** | Chip: **Team MVP** only — School **History**, **Season review** (user school), player **History** tab |

No league-wide News for every school’s team MVP — **user school** optional feed row: *“{Name} — Team MVP.”*

---

## Conference awards

After **conference championships** finalize; before national awards.

**Design lock:** Conference awards are **only** — **Conference Coach of the Year**, **Conference POY**, and **All-Conference** teams. **No** per-position conference POY.

### All-Conference teams

| Rule | Detail |
|------|--------|
| **Teams** | **1st** / **2nd** (optional **3rd** for 16-team leagues — balance `awards.allConferenceTeams`) |
| **Size** | **5** players + **1** honorary position flexibility (2 G, 2 F, 1 C or best remaining) |
| **Score** | `confAwardScore = winSharesSeason×10 + confRankBonus + statLineRank` |
| **confRankBonus** | Champ **+6**, finalist **+4**, top 4 **+2**, else **0** |
| **statLineRank** | Normalized conference rank in **PPG + AST + REB − TO** per 40 (balance weights) |

Pick top 5 + 5 (+ 5) by position buckets within conference pool.

### Conference player of the year

| Rule | Detail |
|------|--------|
| **Pool** | Eligible players in conference |
| **Winner** | Highest `confAwardScore`; prefer **winSharesSeason** rank **#1** in conf unless stat outlier within **0.5** shares |
| **Runner-up** | **#2** stored (display in Awards hub — [Archive](#archive--runner-ups)) |
| **Feeds** | **News** headline; conference HOF input ([`history-hub.md`](history-hub.md)) |

### Conference Coach of the Year

| Rule | Detail |
|------|--------|
| **Winner** | [`Coach awards`](#coach-awards) conference logic |
| **Runner-up** | **#2** coach stored |

---

## National awards

After **national postseason** completes (same End of Season pass).

### Composite award score

```
nationalAwardScore =
    12.0 × winSharesSeason
  +  4.0 × teamPostseasonTier      // 0 missed, 1 bid, 2 Opening win, 3 R32, 4 S16, 5 F4, 6 champ
  +  2.0 × peakOvrSeason / 10
  +  statRankNational               // blended PPG/efficiency percentile 0–3
```

Coefficients in `balance-2026.json` → `awards.nationalWeights`.

### NCBA MVP

| Rule | Detail |
|-------|--------|
| **UI** | **NCBA MVP** (never “National player of the year” or win-contribution copy in UI) |
| **Winner** | Highest `nationalAwardScore` among eligible players |
| **Runner-ups** | **#2–#5** stored ([Archive](#archive--runner-ups)) |
| **Bias** | Win contribution in sim only — high volume on a non-bid team rarely beats a star on a deep postseason run |

### All-America teams

| Rule | Detail |
|------|--------|
| **Structure** | **1st / 2nd / 3rd** teams, **5** each (15 + 15 + 15 = 45 names) |
| **Selection** | Sort eligible by `nationalAwardScore`; fill position buckets (2 G, 2 F, 1 C per team, snub next-best) |
| **Top 60** | Draft-ranked elites get **score floor** bump so listed prospects appear when realistic |

Points toward GOAT: [`history-hub.md`](history-hub.md#goat-score--lists). All-America rows are **full teams** (15×3) — no separate “runner-up” per slot beyond 2nd/3rd team designations.

---

## NCBA position player of the year

**NCBA only** — one winner (+ runner-up) at each listed position **PG, SG, SF, PF, C** per season.

| Rule | Detail |
|------|--------|
| **Pool** | Players whose **listed recruit/roster position** matches; same **All-America** eligibility floor |
| **Score** | `nationalAwardScore` (or position-normalized tie-break) among that position pool nationwide |
| **Winner** | Top score per position |
| **Runner-up** | **#2** per position |
| **UI** | Sub-tab or filter under **NCBA** → e.g. **PG POY** … **C POY** ([Awards hub](#awards-hub-ui-design-lock)) |
| **Conference** | **Does not** award position POYs |

---

## Weekly honors (regular season)

| Award | When | Logic |
|-------|------|--------|
| **Conference player of the week** | Each RS week | Best **single-week** game score in conf: `PTS + 1.2×AST + REB` in that week’s games, min **1** game; **News** only |
| **National player of the week** | Optional **1** per week | Best among 32 conferences’ weekly winners, or top weekly score nationwide |

Weekly honors **do not** use season win contribution (too early). **Winners only** — no runner-ups. Separate **[Weekly Awards](#weekly-awards-screen)** UI ([`game-frame.md`](game-frame.md#awards-hub)).

---

## Coach awards

| Award | Input |
|-------|--------|
| **Conference Coach of the Year** | Conf win %, vs preseason expectation, conference championship finish |
| **NCBA Coach of the Year** | Postseason tier + win delta vs goals + NET rank change |

**Runner-ups:** **#2** coach per tier stored for yearly archive. Coach rows on **coach card History** ([`coach-card.md`](coach-card.md)); not GOAT.

---

## Awards hub UI (design lock)

**Entry:** **Hamburger → Awards** ([`game-frame.md`](game-frame.md#awards-hub)).

**Landing** — two actions (no mixed yearly/weekly on one scroll):

| Button | Opens |
|--------|--------|
| **Yearly awards** | [Yearly Awards screen](#yearly-awards-screen) |
| **Weekly awards** | [Weekly Awards screen](#weekly-awards-screen) |

### Yearly awards screen

| Rule | Detail |
|------|--------|
| **In-season lock** | While `calendarPhase` is exhibition through national postseason (**before End of Season**), **current season** yearly rows show **Locked** / **Announced at end of season** — no winners. **Prior seasons** fully browsable. |
| **After End of Season** | Current season populates; frozen forever in archive |
| **Season picker** | Optional chip row: **2026–27**, **2025–26**, … default **latest completed or locked current** |

**Primary tabs (top):**

| Tab | Content |
|-----|---------|
| **NCBA** | National yearly honors for selected season |
| **Conference** | Conference yearly honors for **one** selected conference |

**NCBA tab — award-type sub-tabs (horizontal filter):**

| Sub-tab | Shows |
|---------|--------|
| **Overview** | **NCBA MVP**, **NCBA Coach of the Year** — winner + runner-up(s) summary |
| **MVP** | **NCBA MVP** — winner, runners **#2–#5** |
| **By position** | **PG / SG / SF / PF / C POY** — each: winner + **runner-up** |
| **All-America** | 1st / 2nd / 3rd teams (five names each) |
| **Coaches** | **NCBA Coach of the Year** — winner + runner-up |

Row: player or coach name, school, tap → card preview.

**Conference tab:**

| Control | Behavior |
|---------|----------|
| **Conference filter** | Dropdown or searchable picker; list **A→Z** below filter |
| **Default** | **User school’s conference** when opening tab (not “first alphabetically” — user’s league first; picker list still **A→Z**) |

**Conference sub-tabs:**

| Sub-tab | Shows |
|---------|--------|
| **Overview** | **Conference POY** + **Conference Coach of the Year** |
| **Player** | **Conference POY** — winner + runner-up |
| **All-Conference** | 1st team (required), 2nd (if league has), 3rd (if configured) |
| **Coach** | **Conference Coach of the Year** — winner + runner-up |

**No** position POY sub-tabs on Conference tab.

### Weekly awards screen

| Rule | Detail |
|------|--------|
| **When** | **Regular season weeks only** — updates after each week advance |
| **Layout** | Week selector (RS Week **1–32**) or scroll newest-first |
| **Rows** | **Conference Player of the Week** (per conference or filter to yours); optional **National Player of the Week** |
| **Winner only** | No runner-up rows |
| **Yearly lock does not apply** | Weekly UI **live** in-season |

---

## Archive & runner-ups

| Award class | Winner | Runner-up |
|-------------|--------|-----------|
| **NCBA MVP** | 1 | **#2–#5** |
| **NCBA position POY** (×5) | 1 each | **#2** each |
| **NCBA Coach of the Year** | 1 | **#2** |
| **Conference POY** | 1 per conf | **#2** |
| **Conference Coach of the Year** | 1 per conf | **#2** |
| **All-Conference / All-America** | Team lists | 2nd/3rd **teams** only (not per-player runner-up) |
| **Team MVP** | 1 per team | **None** in Awards hub (school history only) |
| **Weekly POTW** | 1 | **None** |

Persist `AwardSeasonArchive` at End of Season (yearly) + `AwardWeekRow` each RS week (weekly).

---

## Other surfaces

| Surface | Content |
|---------|---------|
| **Awards hub** | Authoritative browse — [above](#awards-hub-ui-design-lock) |
| **Season review** (user) | **Team MVP** chip; your national/conference lines if applicable — name + label only |
| **School hub → History** | Chips: **Team MVP**, **Conference POY**, All-Conference, All-America, **NCBA MVP**, position POY if any |
| **History hub** year card | **NCBA MVP**, All-America 1st snippet |
| **News** | Headlines on announcement; detail can deep-link **Yearly Awards** |
| **Notifications** | User **NCBA MVP** or **first-team All-America** → optional feed row |

---

## Cross-links

| Doc | Hook |
|-----|------|
| [`simulation-engine.md`](simulation-engine.md#season-win-contribution-design-lock) | `winSharesSeason` |
| [`history-hub.md`](history-hub.md#awards-feed) | HOF, GOAT, archives |
| [`season-review.md`](season-review.md) | End of Season presentation |
| [`game-design.md`](game-design.md#awards--media) | Cadence one-liner |
| [`selection-engine.md`](selection-engine.md) | Postseason tier for `teamPostseasonTier` |
| [`game-frame.md`](game-frame.md#awards-hub) | Navigation entry |

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-09 | Initial design lock; win contribution drives player awards |
| 2026-11-09 | UI names: **Team MVP**, **NCBA MVP**; no win-share copy in UI |
| 2026-11-09 | NCBA position POY; Awards hub (Yearly/Weekly); conf award set; runner-ups |
