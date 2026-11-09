# History, records, Hall of Fame & GOAT

**Context:** Dynasty **archive** — what happened across seasons in this save. Surfaces in the hamburger **History** hub ([`game-frame.md`](game-frame.md#history-hub)), **School hub** (program tabs), **player** / **coach** cards, **End of Season** review, and optional **News** when records break.

**Scope:** **Dynasty-era** data only (from save create forward). Identity pack renames do not rewrite stored history IDs.

**Last updated:** 2026-11-09 · **Status:** Design lock.

---

## Contents

1. [Surfaces](#surfaces)
2. [Cadence](#cadence)
3. [Season archive](#season-archive)
4. [Record books](#record-books)
5. [Hall of Fame](#hall-of-fame)
6. [GOAT score & lists](#goat-score--lists)
7. [Awards feed](#awards-feed)
8. [News & notifications](#news--notifications)
9. [Data model](#data-model)
10. [Cross-links](#cross-links)

---

## Surfaces

| Surface | Audience | Content |
|---------|----------|---------|
| **History hub** (hamburger) | League-wide archive | Year cards: champion, POTY, records, HOF class, **GOAT** snapshot |
| **School hub → History** | One program | Season-by-season W–L, postseason, coach, awards |
| **School hub → Records** | One program | Program single-game / season / career record book |
| **School hub → Hall of Fame** | One program | Program HOF inductees |
| **School hub → Drafted** | One program | Alumni NBA draft picks ([`game-design.md`](game-design.md)) |
| **Player card → History** | One player | Prior seasons at each school |
| **Coach card → History / HOF / Draft** | One coach | Seasons coached, players inducted, draft résumé ([`coach-card.md`](coach-card.md)) |
| **Season review** | User wrap-up | Links into History + new HOF / record chips ([`season-review.md`](season-review.md)) |

**View-only** on opponent schools — same tabs, no edit.

---

## Cadence

| When | Engine work |
|------|-------------|
| **After each finalized game** | Update running season & career stats; check **single-game** record candidates |
| **End of Season week** | Freeze season archives; run **national awards**; **HOF** induction pass; recompute **GOAT** ranks; append History hub year card |
| **On player departure** (grad, draft, portal exit) | Finalize **career** totals at that program; **program HOF** eligibility check |

Weekly honors during RS do **not** create History hub cards — they appear in **News** only.

---

## Season archive

### School hub → History (per program)

One row per **`seasonIndex`**:

| Field | Detail |
|-------|--------|
| Record | RS W–L, conf W–L |
| Head coach | Name at season end |
| Postseason | `NCBA #seed …`, `NCI`, `HC`, conf champ, or `—` |
| Awards | Chips: conf POY, All-America, etc. |
| Tap | Season detail sheet: top 5 scorers, schedule summary, news tag |

### History hub — year card (league)

Collapsed card per completed **`seasonIndex`**:

| Strip | Content |
|-------|---------|
| **Champion** | **NCBA** winner (runner-up, score) |
| **POTY** | National player of the year (name, school) |
| **Records** | National records **set that year** (if any) |
| **HOF** | New **national** inductees that year |
| **GOAT** | Link to **GOAT list** as of that offseason (see below) |

Cards **stack newest first**. National content includes all **365** programs.

---

## Record books

### Stat categories (design lock)

Tracked for **record book** comparisons:

| Scope | Single game | Season (min **20** GP) | Career (program) |
|-------|-------------|------------------------|------------------|
| Points | ✓ | ✓ (total & PPG) | ✓ |
| Rebounds | ✓ | ✓ | ✓ |
| Assists | ✓ | ✓ | ✓ |
| Steals | ✓ | ✓ | ✓ |
| Blocks | ✓ | ✓ | ✓ |
| Made 3PT | ✓ | ✓ | ✓ |

**Ties:** Co-holders allowed; UI shows shared #1.

**National vs program:**

| Book | Holder scope |
|------|----------------|
| **National** | Any player, any school, dynasty-wide — History hub + News when broken |
| **Program** | Players while at **that school only** — School hub → Records |

Exhibitions **excluded** from record consideration.

---

## Hall of Fame

Three **distinct** lists:

### Program HOF (school)

**When evaluated:** Player **leaves program** (graduation, draft, portal transfer out, cut after eligibility).

**Induct if any:**

| Criterion |
|-----------|
| **1,800+** career points at program |
| **2×** first-team **All-Conference** |
| Roster member of **NCBA national champion** (≥1 minute in title season) |
| Holds **program career record** in PTS, REB, or AST at departure |
| **National POTY** while at program |

Max **one** induction event per player per program.

### Conference HOF

**When evaluated:** Same departure event; player’s **conference at departure**.

**Induct if any:**

| Criterion |
|-----------|
| **3×** first-team All-Conference in that conference |
| **Conference POTY** |
| **National POTY** while member of conference |

Attributed on **player archive**, **coach card → HOF**, and **History hub** year strip when inducted.

### National HOF (dynasty)

**When evaluated:** **End of Season week**, after awards.

**Induct if any:**

| Criterion |
|-----------|
| **GOAT score** ≥ **national threshold** (see below) |
| **National POTY** |
| **2×** first-team **All-America** |
| **NCBA champion** + **≥ 28 MPG** that title season |

**One** national induction per player lifetime. Immortal players remain queryable on retired player records.

---

## GOAT score & lists

### Purpose

**Computed** ranking — no user voting. Compares **entire college careers** in the save (all schools a player attended sum for one career arc).

### Per-season inputs (stored)

After each season completes:

| Input | Meaning |
|-------|---------|
| `winSharesSeason` | Engine stat: contribution to team wins from minutes + efficiency ([`simulation-engine.md`](simulation-engine.md) exports per player) |
| `peakOvrSeason` | Max **OVR** during that season |
| `minutesSeason` | Total minutes |

### Career aggregates

| Field | Formula |
|-------|---------|
| `careerWinShares` | Σ `winSharesSeason` |
| `nationalTitles` | Count seasons on **NCBA champion** roster (≥1 RS minute) |
| `peakOvrCareer` | Max `peakOvrSeason` |
| `allAmericaPoints` | 1st team = **3**, 2nd = **2**, 3rd = **1** per season (national teams only) |
| `nationalPoty` | **1** if ever national POTY else **0** |
| `confPoty` | Count conference POTY seasons |

### GOAT score (design lock)

```
goatScore =
    10.0 * careerWinShares
  + 25.0 * nationalTitles
  + 40.0 * nationalPoty
  +  8.0 * allAmericaPoints
  +  3.0 * confPoty
  +  1.5 * max(0, peakOvrCareer - 75)
```

Tune coefficients in `balance-2026.json` → `history.goatWeights` when loader ships; **ratios above** are the default balance intent.

**National HOF threshold:** `goatScore >= 85` (balance key `history.nationalHofMinGoatScore`).

### GOAT lists (UI)

Recomputed **End of Season week**; snapshot stored per year for History card drill-down.

| List | Size | Sort |
|------|------|------|
| **Overall** | **Top 25** | `goatScore` desc, tie-break `careerWinShares`, then `playerId` |
| **By position** | **Top 5** each PG–C | Same; **listed position** at career peak OVR season |

**Eligibility:** **≥ 40** career RS games played (exhibitions excluded) OR **national POTY** once.

**Display:** Rank, name, final school (or “Multiple” if 2+), `goatScore` one decimal, chips (rings, POTY, All-America count). Tap → **retired player card** (archived record).

**Current vs historical:** History hub defaults to **latest** list; year card opens **that season’s** frozen snapshot.

---

## Awards feed

National & conference awards **drive** HOF and GOAT inputs ([`game-design.md`](game-design.md#awards--media)).

| Award | History use |
|-------|-------------|
| National POTY | GOAT + national HOF |
| All-America 1st/2nd/3rd | `allAmericaPoints` |
| Conference POTY / All-Conference 1st | Conference HOF + GOAT `confPoty` |
| Weekly POTW | News only |

Coach national coach of the year appears on **coach card History**, not GOAT.

---

## News & notifications

| Event | Surface |
|-------|---------|
| **National record** broken | **News → Stories** headline; optional History year tag at EoS |
| **National HOF** class announced | **News** + History hub year card |
| **GOAT #1 change** | **News** soft headline at EoS only (not weekly) |

**No** duplicate **notification feed** row for routine records — [`notifications.md`](notifications.md). Major national record (e.g. single-game 50+ PTS) may use feed at product discretion in balance.

---

## Data model

**Dynasty save** (append-only archives):

| Entity | Key fields |
|--------|------------|
| `SeasonArchive` | `seasonIndex`, `teamId`, W–L, postseason code, coachId, awardIds[] |
| `RecordBookEntry` | scope `national|program`, `teamId?`, stat, value, playerId, gameId?, seasonIndex |
| `HofEntry` | `hofType`, `playerId`, `teamId?`, `confId?`, `seasonIndex` inducted |
| `GoatSnapshot` | `seasonIndex`, ranked rows `{ playerId, rank, goatScore, positionBucket? }` |
| `PlayerCareerArchive` | per player: GOAT inputs, `hofFlags`, season stat lines |

**Player records** while active live in roster tables; on departure, merge into **archive** (player card still opens).

---

## Cross-links

| Doc | Hook |
|-----|------|
| [`game-frame.md`](game-frame.md#history-hub) | Hub placement & navigation |
| [`season-review.md`](season-review.md) | EoS triggers recompute |
| [`coach-card.md`](coach-card.md#hall-of-fame) | Coach HOF attribution |
| [`player-cards.md`](player-cards.md) | Stats, History tab |
| [`news-hub.md`](news-hub.md) | Record / award headlines |
| [`simulation-engine.md`](simulation-engine.md) | `winSharesSeason` output |
| [`notifications.md`](notifications.md) | No routine record spam |

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-09 | Initial spec — records, triple HOF, GOAT formula, cadence |
