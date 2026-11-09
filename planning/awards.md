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
| **NCBA MVP** | National player of the year (one per season, universe-wide). |
| **Conference POY** | Conference player of the year (one per conference). |
| **All-Conference** / **All-America** | Team selections (chips or lists — no stat breakdown). |

Save/archival fields may use internal ids (e.g. `nationalPoty`); **copy always** uses **NCBA MVP** where the national player award appears ([`ncba-branding.md`](ncba-branding.md)).

---

## Contents

1. [Season win contribution (shared input)](#season-win-contribution-shared-input)
2. [Eligibility gates](#eligibility-gates)
3. [Team honors](#team-honors)
4. [Conference awards](#conference-awards)
5. [National awards](#national-awards)
6. [Weekly honors (regular season)](#weekly-honors-regular-season)
7. [Coach awards](#coach-awards)
8. [UI & feeds](#ui--feeds)
9. [Cross-links](#cross-links)

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
| **Feeds** | **News** headline; conference HOF input ([`history-hub.md`](history-hub.md)) |

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
| **Runner-up** | #2–#5 stored for News / History copy |
| **Bias** | Win contribution in sim only — high volume on a non-bid team rarely beats a star on a deep postseason run |

### All-America teams

| Rule | Detail |
|------|--------|
| **Structure** | **1st / 2nd / 3rd** teams, **5** each (15 + 15 + 15 = 45 names) |
| **Selection** | Sort eligible by `nationalAwardScore`; fill position buckets (2 G, 2 F, 1 C per team, snub next-best) |
| **Top 60** | Draft-ranked elites get **score floor** bump so listed prospects appear when realistic |

Points toward GOAT: [`history-hub.md`](history-hub.md#goat-score--lists).

---

## Weekly honors (regular season)

| Award | When | Logic |
|-------|------|--------|
| **Conference player of the week** | Each RS week | Best **single-week** game score in conf: `PTS + 1.2×AST + REB` in that week’s games, min **1** game; **News** only |
| **National player of the week** | Optional **1** per week | Best among 32 conferences’ weekly winners, or top weekly score nationwide |

Weekly honors **do not** use season win contribution (too early). Feed **News**; not HOF.

---

## Coach awards

| Award | Input |
|-------|--------|
| **Conference coach of the year** | Conf win %, vs preseason expectation, tournament finish — **not** win shares |
| **National coach of the year** | Postseason tier + win delta vs goals + NET rank change |

Coach awards on **coach card History** ([`coach-card.md`](coach-card.md)); not GOAT.

---

## UI & feeds

| Surface | Content |
|---------|---------|
| **Season review** (user) | **Team MVP** chip if yours; **NCBA MVP** / **Conference POY** / All-America lines if applicable — name + label only |
| **School hub → History** | Chips: **Team MVP**, **Conference POY**, All-Conference, All-America, **NCBA MVP** |
| **History hub** year card | **NCBA MVP**, All-America first team (names) |
| **News** | Headlines use same short labels (`NCBA MVP`, `Conference POY`); weekly POTW only in season |
| **Notifications** | User wins **NCBA MVP** or **first-team All-America** → medium feed row (optional) |

---

## Cross-links

| Doc | Hook |
|-----|------|
| [`simulation-engine.md`](simulation-engine.md#season-win-contribution-design-lock) | `winSharesSeason` |
| [`history-hub.md`](history-hub.md#awards-feed) | HOF, GOAT, archives |
| [`season-review.md`](season-review.md) | End of Season presentation |
| [`game-design.md`](game-design.md#awards--media) | Cadence one-liner |
| [`selection-engine.md`](selection-engine.md) | Postseason tier for `teamPostseasonTier` |

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-09 | Initial design lock; win contribution drives player awards |
| 2026-11-09 | UI names: **Team MVP**, **NCBA MVP**; no win-share copy in UI |
