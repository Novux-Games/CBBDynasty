# Time system — weeks, byes, postseason calendar

**Kernel:** Dynasty time advances by **day** (optional) or **week**. **Week advance** = save checkpoint ([`game-design.md`](game-design.md#when-games-run-vs-when-the-save-commits)). This doc defines **week labels** and **bye** rules for exhibitions, regular season, conference tournaments, and national postseason.

**Companions:** [`preseason.md`](preseason.md), [`postseason.md`](postseason.md), [`ncba-branding.md`](ncba-branding.md), [`game-frame.md`](game-frame.md) (Calendar tab).

**Last updated:** 2025-11-09

---

## Contents

1. [Layers of the year](#layers-of-the-year)
2. [Exhibition weeks](#exhibition-weeks)
3. [Regular season weeks 1–32](#regular-season-weeks-132)
4. [Early-season event tournaments & byes](#early-season-event-tournaments--byes)
5. [Conference tournaments](#conference-tournaments)
6. [National postseason weeks](#national-postseason-weeks)
7. [Advance rules](#advance-rules)
8. [Cross-links](#cross-links)

---

## Layers of the year

```
OFFSEASON (weeks / carousel) → PRESEASON (setup) →
  EXHIBITION WEEKS 1–3 → REGULAR SEASON WEEKS 1–32 →
  CONFERENCE TOURNAMENT (≤ N weeks) → NATIONAL POSTSEASON (≤ 7 weeks) →
  offseason…
```

**`calendarPhase`** on save: `offseason` | `preseason` | `exhibition` | `regular_season` | `conf_tournament` | `postseason` | …

---

## Exhibition weeks

| Label | Purpose |
|-------|---------|
| **Exhibition Week 1** | First exhibition window |
| **Exhibition Week 2** | Second exhibition window |
| **Exhibition Week 3** | Third exhibition window |

- User may schedule **0–3** exhibitions total ([`preseason.md`](preseason.md)); each game is placed on an **Exhibition Week** slot (not RS weeks).
- Weeks with **no** exhibition scheduled: **advance** still allowed (empty week).
- Exhibitions **do not** count toward **32** RS games or record.

---

## Regular season weeks 1–32

| Label | Purpose |
|-------|---------|
| **Week 1** … **Week 32** | Regular-season calendar — up to **32** RS games per team (exhibitions separate) |

- Each RS game is assigned to exactly one **Week 1–32** bucket for **week advance** batching.
- **Early-season event** games count as RS games and occupy **specific weeks** in this range (see below).
- On **week advance**, all games dated that week finalize; standings, NET, injuries, saves lock.

**Not** calendar-month accurate for UX v1 — **week index** is the source of truth (months in copy optional).

---

## Early-season event tournaments & byes

Invites accepted in preseason ([`preseason.md`](preseason.md#6--tournament-invites)). Templates in pack (`tournamentTemplate` on event).

### Event footprints (default pack)

| Event | Teams | **Calendar weeks** (RS) | **RS game spots** | Notes |
|-------|-------|-------------------------|-------------------|--------|
| **The Island Classic** | **16** | **4** | **4** | Full bracket spread over 4 RS weeks |
| **The Desert Shootout** | **16** | **4** | **4** | Same footprint as Island |
| **The Caribbean Showdown** | 8 | **3** | **3** | Smaller bracket — **not** 4-week / 4-spot |
| **The Blue Blood Tip-Off** | **4** | **2** | **2** | Round 1 week + championship week |

**Design lock:** Only **Island** and **Desert** use the **16-team / 4-week / 4-game-spot** model. Other events use shorter footprints.

### Tournament consumes week slots

When an event spans **K** calendar weeks, those weeks are **reserved** on the participant’s schedule for bracket games (even if that team only plays once that week). Non-event RS games are **not** placed on conflicting dates by the solver.

### Byes after elimination

If a team **loses** and is out of the event before the bracket ends:

| Rule | Behavior |
|------|----------|
| **Bye weeks** | Team has **no game** on remaining **event weeks** until the next **non-event** RS game on their calendar |
| **Example (Blue Blood)** | **4** teams, **2** weeks. Losers of **round 1** are out; **week 2** is the **final** for winners only — losers are on **bye** that week (tournament still occupies a **game spot** in the season cap, but no tip for eliminated teams) |
| **Week advance** | User (and AI) with **no** game that week may still **advance week** — practice / recruiting flavor optional; no forced sim |

Eliminated teams **do not** get replacement games to fill event weeks — the bye is real slack, matching sparse slates after Maui/Atlantis exits.

### Accepting an invite

- Reserves **N** RS games and **K** RS weeks per table above.
- Caps other RS scheduling at **28** only when event uses **4** game spots (Island/Desert); shorter events use `32 − N` for remaining builder ([`preseason.md`](preseason.md)).

---

## Conference tournaments

**When:** After **Regular Season Week 32** (standings final).

| Rule | Detail |
|------|--------|
| **Weeks** | **Number of calendar weeks = maximum possible rounds** for that conference’s `TournamentTemplate` |
| **Rounds** | Single-elim (or pack format); each **round** maps to **one** advance week — e.g. 4-round bracket → **4** conf-tourney weeks max |
| **Byes** | Teams with first-round byes have **no game** in early conf-tourney weeks (same bye semantics as event losses) |
| **Membership** | Who qualifies is template-defined ([`postseason.md`](postseason.md)) |

All conferences sim in parallel each week; user plays when their school has a game.

---

## National postseason weeks

**When:** After conference tournaments; **Selection Sunday** transitions to bracket play.

### NCBA Men's BB Tournament (user path length)

**Design lock:** **7** national postseason **weeks** cover the longest possible user path:

| Week | Round (universe label) |
|------|-------------------------|
| **1** | **Opening Round** (if in the 24-team pool; otherwise **bye** this week) |
| **2** | **Round 1** (round of 64) |
| **3** | **Round 2** (round of 32) |
| **4** | **Super 16** |
| **5** | **Great 8** |
| **6** | **Championship 4** |
| **7** | **National Championship** |

Teams that **skip** Opening Round still use **weeks 2–7** for R1 → title. Eliminated teams **bye** on later weeks until their season ends (no further games).

**Parallel events:** **NCI** and **Heritage Classic** run on the **same calendar** ([`postseason.md`](postseason.md)); a program in only NCI uses **≤5** weeks (32-team bracket). Pack maps NCI/Heritage rounds onto postseason week indices without extending past NCBA’s **7**-week window (sim batch all brackets active that week).

---

## Advance rules

| Phase | Advance |
|-------|---------|
| **Exhibition / RS** | **Advance week** sims all games in that week label for the league; user game(s) first or batch order per settings |
| **Bye week** | Allowed — no games for user does not block advance |
| **Conf tourney / postseason** | One **round layer** per week index; all national brackets progress together on advance |

Coach **Sunday skill points** tie to RS week boundary ([`game-design.md`](game-design.md)).

---

## Cross-links

| Topic | Doc |
|-------|-----|
| Event names | [`ncba-branding.md`](ncba-branding.md) |
| NCBA round labels | [`ncba-branding.md`](ncba-branding.md#ncba-round-names-ui--news) |
| Selection & fields | [`postseason.md`](postseason.md) |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-09 | Exhibition 1–3, RS 1–32, event byes, conf/postseason weeks |
