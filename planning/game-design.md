# Dynasty: College Basketball — Game Design Document

> **Branding:** [**Dynasty: College Basketball**](branding.md) · Novux Studios · bg `#181a25` · logo [`Public-Assets/app-logo.svg`](../Public-Assets/app-logo.svg)

**Platform:** Android (mobile)  
**Presentation:** Text-based **2D** sim (Campus Dynasty–style); **offline** with **local save files**  
**Mode:** Dynasty / program builder — men's college basketball  
**Game sim:** **[`simulation-engine.md`](simulation-engine.md)** — possession engine only (clock, box score)  
**In-season UI:** **[`game-frame.md`](game-frame.md)** — dashboard, tabs, hubs  
**Player UI:** **[`player-cards.md`](player-cards.md)** — preview, card tabs, ratings; **[`player-archetypes.md`](player-archetypes.md)** — archetypes, draft rank  
**Lineup:** **[`positions-and-lineup.md`](positions-and-lineup.md)** — PG/SG/SF/PF/C, interchange, default starters  
**Strategy:** **[`strategy-schemes.md`](strategy-schemes.md)** — O/D presets, halftime, preview/watch  
**Coach UI:** **[`coach-card.md`](coach-card.md)** — coach preview, card tabs, School staff  
**Recruiting:** **[`recruiting-engine.md`](recruiting-engine.md)** — pools, 4-week window, NIL + points  
**League identity:** **[`league-branding.md`](league-branding.md)** — conference/team real ↔ universe mapping  
**Document type:** Planning GDD (implementation-ready outline)  
**Default era:** **2026–27** at new dynasty start  
**Last updated:** 2025-11-08

---

## Contents

1. [Vision & pillars](#vision--pillars)
2. [Core loop](#core-loop)
3. [Simulation engine (brief)](#simulation-engine-brief)
4. [In-season game frame (brief)](#in-season-game-frame-brief)
5. [Identity packs & save model](#identity-packs--save-model)
6. [World scale & league simulation](#world-scale--league-simulation)
7. [Roster, eligibility & redshirt](#roster-eligibility--redshirt)
8. [Offseason (authoritative calendar)](#offseason-authoritative-calendar)
9. [Recruiting engine](#recruiting-engine)
10. [NIL & program economy](#nil--program-economy)
11. [Coaching carousel & contracts](#coaching-carousel--contracts)
12. [Facilities, fundraising & upgrades](#facilities-fundraising--upgrades)
13. [Conference realignment](#conference-realignment)
14. [Preseason](#preseason)
15. [Scheduling & regular season](#scheduling--regular-season)
16. [Season gameplay & prep](#season-gameplay--prep)
17. [Postseason & selection](#postseason--selection)
18. [Awards & media](#awards--media)
19. [Mobile UX, automation & notifications](#mobile-ux-automation--notifications)
20. [Engineering milestones](#engineering-milestones)
21. [Glossary](#glossary)
22. [Document history](#document-history)

---

## Vision & pillars

### Vision

A **program dynasty** where running a D-I men's hoops school feels like a year-round job: **offseason** portal wars and recruiting, **preseason** schedule craft and hype, **season** survival on the calendar, **March** as the payoff. The world runs **~365 programs** with **real conference mechanics** (schedule size, tiebreakers, tournament shapes) while **display identity** stays swappable and save-safe.

### Pillars

| Pillar | Commitment |
|--------|------------|
| **Scale** | Full D-I men's membership (~365) |
| **Structure** | Real conference sizes & rules (data-driven templates) |
| **Roster law** | 15-man cap, scholarship flexibility, walk-ons |
| **Calendar** | One master timeline for every game year |
| **Offseason depth** | Recruiting + portal + carousel + facilities = co-equal with games |
| **Identity** | Generic default; optional JSON/URL packs; no save corruption on swap |
| **Era** | 2026–27 bootstrap content; dynasty can advance many years |

### Non-goals

- Real-time player-controlled on-court action (inputs are **prep only** — see simulation doc).
- Licensed NCAA / school trademarks in the base APK.
- Real player likenesses or real NIL deals tied to individuals.
- Online multiplayer, cloud saves, or always-on internet.
- Women's basketball (future SKU / expansion).

---

## Core loop

```
OFFSEASON → PRESEASON → REGULAR SEASON → POSTSEASON → (advance year)
     ↑__________________________________________________|
```

**Player fantasy beats**

1. **Build the roster** — portal, recruiting, NIL, cuts, pro-day/draft fallout.
2. **Build the program** — facilities, grades, fundraising, conference trajectory.
3. **Build the résumé** — schedule smart, win the right games, own March.
4. **Build the legend** — coach progression, carousel drama, multi-decade archives.

**Session types (mobile)**

| Session | Typical length | Activity |
|---------|----------------|----------|
| **Micro** | 1–3 min | Sim one day, one recruiting action, one upgrade |
| **Standard** | 10–15 min | Clear a week, recruiting board, schedule tweak |
| **Event** | 20+ min | Selection Sunday, conference tournament run, bracket play |

---

## Simulation engine (brief)

**Possession game sim only:** [`simulation-engine.md`](simulation-engine.md). Dynasty layer owns calendar, saves, NET, recruiting.

| Topic | Summary |
|-------|---------|
| **Style** | Text **2D**; **40:00** clock; **3–30 sec** game time per possession |
| **User leverage** | Lineup, rotation, **gameplan** before tip |
| **Output** | Full box score; live text during sim |
| **League** | Same `simulateGame` for user watch and batch; **when** to call it = calendar (below) |
| **Saves** | **Week advance** checkpoints local file; **replay** simmed games if app closed before then |

---

## In-season game frame (brief)

Full navigation and screens: **[`game-frame.md`](game-frame.md)**.

| Area | Summary |
|------|---------|
| **Bottom tabs** | Roster · Calendar · **Dashboard** · News (headlines only) |
| **Dashboard** | Team overview, notifications, last game, next game + **game preview** (win %, predicted score, team compare, H2H) |
| **Hamburger hubs** | School, Strategy, Statistics, Rankings, News (full), History |
| **School hub** | Overview, roster, schedule, stats, history, records, HOF, drafted players (same layout for other schools view-only) |

---

## Identity packs & save model

### Design lock

| Rule | Detail |
|------|--------|
| **Default ship** | Generic names, generic logos, full **structural** fidelity (IDs, conferences, formats). |
| **Import** | Local **JSON** or remote **URL** (HTTPS); same schema as default. |
| **Swap** | Revert to default or re-import anytime; **never** rewrite dynasty history. |
| **Keys** | All simulation entities use **opaque stable IDs** (`teamId`, `confId`); display strings live in pack overlay. |

### Pack schema (conceptual)

Packs are validated **atomically**. Invalid pack → reject; no partial apply.

```json
{
  "packVersion": 1,
  "membershipYear": "2026-27",
  "disclaimerAcceptedRequired": true,
  "conferences": [
    {
      "id": "conf-sec",
      "displayName": "Southeast Conference",
      "leagueGameCount": 20,
      "standingsTiebreakers": ["h2h", "h2h-point-diff", "record-vs-top-teams"],
      "tournamentTemplateId": "tpl-sec-16",
      "ncaaAutoBidSlot": 1
    }
  ],
  "teams": [
    {
      "id": "team-001",
      "conferenceId": "conf-sec",
      "displayName": "State University",
      "shortName": "ST",
      "colors": { "primary": "#003366", "secondary": "#CCCCCC" },
      "arenaBaseName": "Arena",
      "logoUri": null
    }
  ],
  "tournamentTemplates": [],
  "rulesetYear": 2026
}
```

**Required templates in default pack**

- Conference **round-robin or matrix** schedule generators per `leagueGameCount`.
- **TournamentTemplate** per conference (bracket size, byes, reseeding, venue neutral/home).
- **NCAA** opening-round slot map (76 → 64).

### Save partitioning

| Save layer | Pack-dependent? | Notes |
|------------|-------------------|--------|
| Dynasty metadata | No | Start year, difficulty, coach career |
| Program state | IDs only | Prestige, facility grades, budgets |
| People | No likeness | Generated players/coaches |
| History | IDs only | Results, stats archive |
| Display cache | Yes | Rebuilt on pack swap |

**Post-swap behavior:** Recompute display names/logos; log `packId` + `membershipYear` in dynasty metadata; show non-blocking warning if pack year ≠ current dynasty year.

### Legal UX (ship checklist)

- [ ] Import screen: user attestation + unofficial disclaimer.
- [ ] URL fetch: domain allowlist option, size cap, signature optional (future).
- [ ] Store listing: no implied NCAA endorsement.

---

## World scale & league simulation

### Programs

- **~365** D-I teams in the league graph; each **calendar day** only scheduled teams play (not 365 games per day).
- **User role:** Head coach (single program dynasty); AD-style actions folded into coach UI.

### When games run vs when the save commits

| Action | Effect |
|--------|--------|
| **Sim user game** | Possession sim runs; result held in **current week** session state |
| **Advance day** | Roll calendar; batch-sim other **scheduled** games that date (same week state) |
| **Advance week** | **Write save**; week’s results, standings, stats, and league outcomes become **final** |

Until **week advance**, nothing from that week is treated as permanent on disk.

### Replays

- **Fully close and reopen** the app **before** week advance → user may **replay** games already simmed this week (new lineup/gameplan allowed).
- **After** week advance → those games are **locked** in the local save; no replay without cheat/sandbox.

In-week variety is normal (different sim runs, different scores). **Locked** only after the weekly checkpoint.

### Ruleset evolution

- `rulesetYear` in pack ties to **portal windows**, **calendar dates**, **scholarship limits**.
- When dynasty year advances, engine loads **rulesetYear + (dynastyYear - startYear)** patches from data (e.g. portal deadline shifts).

### Offseason sim

- Batch recruit AI for non-user schools between user sessions (offline, local saves).

---

## Roster, eligibility & redshirt

### Hard limits

| Rule | Value |
|------|--------|
| Roster max | **15** |
| Scholarships max | **15** (may use fewer) |
| Walk-ons | Allowed; count toward 15 |

### Class & eligibility

- **Eligibility years** tracked per player (games played, redshirt flags).
- **Academic standing** per player: eligible / at-risk / ineligible for next game.
- **Incompletes** at term boundary can bench player for postseason (configurable strictness by difficulty).

### Redshirt

| Type | Trigger |
|------|---------|
| **Standard redshirt** | User (preseason) elects; player plays **0** games (exhibitions exempt or count per ruleset — **exhibitions do not count** toward redshirt games played). |
| **Medical redshirt** | In-season injury; games played ≤ ruleset threshold; medical staff grade affects approval odds in edge cases. |

### Procedural roster events

- **Graduation** — fixed class year.
- **Early draft entry** — pro days + declare; withdrawal deadline before draft results week (see offseason).
- **Portal in/out** — separate from high school recruiting board.
- **Cuts** — offseason window; must land ≤15 by roster lock before exhibitions.

---

## Offseason (authoritative calendar)

Offseason length: **~14 weeks** (tunable); user advances **week** or **day** in portal/recruiting sub-windows.

| Week | Phase | Player highlights |
|------|--------|-------------------|
| **1** | **Transfer portal (cycle 1)** + **pro days** | Portal entries/responses; grad/early-entry pro day participation; stay-go decisions for declarations |
| **2–3** | **Coaching carousel** (peak) | AI hires/fires; user contract extension; poaching risk |
| **4** | **Cuts & roster lock prep** | Scholarship allocation; walk-on offers |
| **5** | **Draft withdrawal deadline** | Last chance to return for withdrawing early entrants (before results) |
| **6–9** | **Recruiting (4 weeks)** | HS + JUCO + portal — see [`recruiting-engine.md`](recruiting-engine.md) |
| **6** | **NBA draft results** (recruiting **week 1**) | Published mock/order reveal; early entrants leave; scholarship opens; portal board opens |
| **10** | Portal **cycle 2** (optional overlap) | Late portal fills for AI if ruleset enables |
| **11** | **Summer workouts** (auto) | Development deltas from coach + facilities |
| **12–13** | **Fundraising & upgrades** | Arena/facility spend; naming rights deal |
| **14** | **Conference offers** + **schedule prep unlock** | Accept/decline realignment; preseason scheduling opens |

**Notes**

- **Coaching carousel** can spawn events in weeks 2–14 at lower intensity (assistant offers, extension pressure).
- **Graduation** processed at offseason start (week 0 rollover).
- **Summer workouts** also apply passive gains to returning players who skipped pro-day declare drama.

### Pro days & draft timeline (edge-case spec)

| Event | Timing |
|-------|--------|
| Pro day participation | Offseason **week 1** |
| Declare / stay | Week 1 decisions; can revisit until withdrawal deadline |
| **Withdrawal deadline** | Offseason **week 5** |
| **Draft results posted** | **Recruiting week 1** (offseason week 6) |
| Who attends pro day | **Graduates** + **undergraduates who declared** |

If player withdraws by week 5 → retains eligibility; if not withdrawn before draft results → either drafted (roster exit) or undrafted free agent exit per user/NBA odds model.

---

## Recruiting engine

**Authoritative spec:** [`recruiting-engine.md`](recruiting-engine.md) — 4-week period, 2–4K HS + 1.5K JUCO, portal sub-tab, recruiting points + NIL, letter grades, prestige 0–99.

**Brief:** Custom engine; **no in-season HS/JUCO window**; class **seeded in early offseason**; portal recruits available **recruiting week 1**; star bands **per pool** (top 2% = 5★); combined HS/JUCO board; hidden ratings with **A–F** scout view.

---

## NIL & program economy

### Abstract NIL (no real persons)

| Concept | Behavior |
|---------|----------|
| **Collective strength** | Program-level 0–100; upgraded by fundraising and success |
| **Player deal points** | Per-player allocation from collective budget |
| **Recruit demand** | Each recruit has `nilAsk`; failing ask lowers interest |
| **Retention** | Upperclassmen request raises after breakout seasons |

### Boosters & fundraising

- Offseason **fundraising mini-phase**: choose events (gala, alumni game, donor drive).
- Success → **cash** + **collective boost** + **facility seed money**.
- **Arena naming rights:** one-time cash infusion; slight prestige hit if name is silly; reversible in fiction only via rebrand cost.

### Academic grades → recruiting budget

**Program academics grade (0–100)** computed each spring from:

| Input | Weight (tunable) |
|-------|----------------|
| Team GPA | 30% |
| APR-style retention | 40% |
| Eligibility incidents | −20% penalty stack |
| Academic facility grade | +10% cap bonus |

**Recruiting budget formula (v1)**

```
baseBudget = prestigeTierTable[prestige]
gradeMultiplier = 0.6 + (academicsGrade / 100) * 0.8   // 0.6x – 1.4x
facilityBonus = 1 + 0.05 * (recruitingFacilityGrade - 3)  // grade 1–10
recruitingBudget = floor(baseBudget * gradeMultiplier * facilityBonus)
```

**Upgrades done in offseason 12–13** apply to **next year's** recruiting cycle (+1 facility tier → next year `facilityBonus`).

### Cash sinks

- Coach buyouts (carousel)
- Facility construction
- NIL pool (in-season retention top-ups limited)

---

## Coaching carousel & contracts

### Coach entity

- **Overall** + **skill tree** (recruiting, player dev, in-game tactics, portal retention, NIL charm).
- **Skill points:** earned on level-up; spend preseason + **each Sunday** in season; **auto-spend** optional with priority list.

### Contract fields

| Field | Notes |
|-------|-------|
| Years remaining | 1–6 typical |
| Salary | Affects AD satisfaction |
| Buyout | User or AI pays to leave |
| Performance goals | Sweet 16, .500 conf, etc. |

### Carousel AI (behavior spec)

| Factor | Effect on AI |
|--------|----------------|
| Last season wins | Job security |
| Postseason run | Extension + poach immunity short window |
| Prestige mismatch | Mid-major success → power offers |
| Age / tenure | Regression to mean on firing |
| Portal exodus | Firing risk |

**User interaction**

- Accept extension, renegotiate, resign.
- **Interview offers** from other schools (prestige check).
- Firing → choose new job from offer list or unemployed (spectate sim optional mode TBD).

### Assistant coaches (light v1)

- Aggregate into **staff grade** bumping visit efficiency and development; full assistant sim optional v2.
- **UI:** assistants use **coach cards**; support staff (e.g. trainers) are list-only — trainers **`eligibleForHeadCoach: false`** ([`coach-card.md`](coach-card.md)).

---

## Facilities, fundraising & upgrades

### Facility categories (grades 1–10)

| Category | Affects |
|----------|---------|
| **Arena** | Home revenue, attendance, recruit wow factor |
| **Practice gym** | Development, scheme prep |
| **Strength & sports science** | Injury recovery, summer gains |
| **Medical** | Medical redshirt odds, injury duration |
| **Academic center** | Grades, eligibility recovery |
| **Recruiting lounge** | Visit success, budget efficiency |

### Upgrade flow

- Spend **cash** + **weeks** (construction overlaps recruiting late weeks).
- **Naming rights** on arena: +cash, display name override in pack layer (`arenaDisplayName`).

---

## Conference realignment

**Design lock:** **No relegation.** Conferences can **invite** programs; user/AI **accept or decline**.

### Invite score (AI conference commissioner)

```
inviteScore = 0.35 * onCourtSuccess
            + 0.25 * programPrestige
            + 0.20 * marketSize
            + 0.10 * academicsGrade
            + 0.10 * facilityGrade
```

- Top conferences poach from mids when **inviteScore > threshold** and **conference has slot** (real membership caps from pack).
- User receives **offer card** offseason week 14 with **pros/cons** (travel, competition, recruiting tier).

### Schedule impact

- Conference change effective **next season**; conference games regenerate from template.

---

## Preseason

### Flow (after offseason week 14)

1. **Roster finalization** — confirm redshirts.
2. **Regular season schedule** — non-conference + conference auto + optional tournament.
3. **Coach upgrades** — manual or auto.
4. **Preseason workouts** — automated deltas.
5. **Media & preview rankings** — national + conference poll sim; sets expectation meter.
6. **Exhibition scheduling** — up to **3** in **October** (UX: calendar picker, opponent search, travel).

### Exhibitions vs scrimmages

| Type | Counts toward record | Counts toward redshirt games |
|------|----------------------|------------------------------|
| **Exhibition** | No | No |
| **Closed scrimmage** | No | No (narrative event only) |

Exhibitions affect **morale** and **poll momentum** slightly; injuries can occur.

### Media & preview rankings

- **Preseason Top 25** (national) + conference poll.
- **User storyline:** "overrated/underrated" tag if disparity vs roster OVR.
- Updates partially after exhibitions (small bump).

---

## Scheduling & regular season

### Caps

| Constraint | Value |
|------------|-------|
| Total regular season games | **32** max |
| Conference games | Up to **20** (from conference template) |
| exempt tournament | **4** games → **28** other games |

### Scheduling UX

- **Conference:** auto-generated round-robin/matrix from template.
- **User picks:** non-conference opponents (date windows Nov–Dec, limited Jan slots for small conferences).
- **Tournaments:** join preseason; bracket injected; 4-game cap.
- **Validation:** solver prevents over-cap; highlights conflict days.

### In-season calendar (typical 2026–27 shape)

| Month | Focus |
|-------|-------|
| Oct | ≤3 exhibitions |
| Nov–Dec | Non-conference |
| Jan | Conference (large) or mix (small) |
| Feb | Conference |
| Early Mar | Conference tournaments |
| Mid Mar | Selection Sunday |
| Mid Mar–early Apr | NCAA tournament |

### Conference standings & tiebreakers

**Data-driven stack** per conference in pack (`standingsTiebreakers` array).

**Supported tiebreaker modules (implement in order)**

1. **Head-to-head** (mini-league if 3+ tied)
2. **Head-to-head point differential**
3. **Conference record** (already tied at this step)
4. **Record vs top N** in conference
5. **Record vs common opponents**
6. **Point differential** (all conf games)
7. **Coin flip** (logged for absurdities)

NCAA seeding uses **conference tournament champion** as auto bid; remaining at-large from résumé.

---

## Season gameplay & prep

Gameplay is **sim-only**; see **[`simulation-engine.md`](simulation-engine.md)** for possession loop, clock, and archives.

| Mode | Behavior |
|------|----------|
| **Watch sim** | **2s** per possession; **halftime** adjustments ([`strategy-schemes.md`](strategy-schemes.md)) |
| **Fast sim** | Same engine, no wall-clock delay |
| **Prep** | Lineup, rotation, gameplan before tip |

### In-season systems (all in scope)

| System | Spec summary |
|--------|----------------|
| **Injuries** | Severity tiers; games missed; medical staff reduces duration |
| **Suspensions** | Administrative; X games |
| **Academic eligibility** | Rolling grades; bench ineligible |
| **Portal (in-season)** | Ruleset-driven window; user can lose players midseason |
| **Chemistry** | Roster stability + coaching; affects clutch sim |
| **Fatigue** | Back-to-back penalties optional |

### NET-style résumé (selection)

**Maintained by the dynasty layer** from **week-finalized** box scores ([`simulation-engine.md`](simulation-engine.md) supplies each game’s box score). Game design consumes:

Per team, maintain:

| Metric | Use |
|--------|-----|
| **Net efficiency** | Quality indicator |
| **Quad schedule** | Wins/losses buckets 1–4 (home/neutral/road vs NET) |
| **KenPom-like rating** | Hidden true strength |
| **SOS / non-conf strength** | Bubble breaker |
| **Recent form** | Last 10 weight for bubble |

All **inspired by** real metrics; labels in UI are generic ("Resume Index", "Tier wins") unless pack enables fan names.

### Weekly rhythm

- **Sunday:** coach skill points (notification).
- **Tue–Sat:** games per schedule.
- **Monday:** optional news digest (injuries, polls).

---

## Postseason & selection

### Conference tournaments

Each conference uses a **TournamentTemplate**:

```json
{
  "id": "tpl-sec-16",
  "bracketSize": 16,
  "seeds": 16,
  "byes": [],
  "reseedingAfterRound": false,
  "sitePolicy": "neutral-primary",
  "rounds": [
    { "name": "R1", "games": 8 },
    { "name": "QF", "games": 4 },
    { "name": "SF", "games": 2 },
    { "name": "F", "games": 1 }
  ]
}
```

**Design lock:** **real** bracket format per conference (size, byes, reseeding, sites) for **2026–27** — one `TournamentTemplate` per conference in the default pack.

### NCAA field: 76 teams

| Slot type | Count |
|-----------|-------|
| Auto bids | 32 (conference champions) |
| At-large | 44 |
| **Total** | **76** |

### Opening round (12 games → 64)

**Structural plan (First Four analog)**

- **8 teams** play **4 games** for **4 seeds** into 64-bracket (last four in at-large/high seed play-in).
- **4 teams** play **2 games** for **2 remaining 16-seeds** (autobid play-in concept).
- Total **6 opening-round sessions × 2 teams = 12 games** (matches 76 − 64 = 12 teams eliminated).

Winners **inherit** the seed line assigned on Selection Sunday (not reseeded into different region after win).

### Bracket construction

- **4 regions**, 16 teams each after opening round.
- **S-curve** seeding with **conference conflict rules** (no same-conference until Sweet 16 if possible).
- **User bracket view** + sim; national semifinals at **Final Four** site.

### Selection Sunday (committee sim)

**Auto bid lock** after conference finals.

**At-large ordering:**

1. Lock autobids.
2. Rank remaining by **composite resume score** (NET + quad wins + bad losses penalty).
3. Take top 44 at-large + autobids = 76.
4. Assign **seeds 1–16** per region via S-curve.
5. Assign **opening-round matchups** to lowest at-large seeds and designated play-in autobids.

**Bubble drama UI:** last four in / last four out screen for user if involved.

---

## Awards & media

### Cadence

| Award | When |
|-------|------|
| **Weekly honors** | Optional mid-season |
| **All-conference** | After conference tournament |
| **National awards** | After tournament (POY, coach, all-america teams) |
| **Hall of fame (dynasty)** | Career archive milestones |

### Media systems

- **Polls:** weekly AP-style top 25 (sim voters).
- **Bracketology:** updates weekly in February in news feed.
- **User stories:** win streaks, upset alerts, portal steals.

---

## Mobile UX, automation & notifications

### Automation toggles (global)

| System | Auto options |
|--------|----------------|
| Coach skills | Auto-spend with priority tree |
| Summer/preseason workouts | Always auto |
| Recruiting | Auto-maintain board (caps) |
| Non-user games | Always batch sim |

### Manual must-feel-good

- Recruiting visits & NIL on borderline targets
- Opening-round / tournament rounds for user team
- Conference accept/decline
- Schedule non-conference rivals

### Notifications (Android)

| Event | Priority |
|-------|----------|
| Recruiting commit / decommit | High |
| Portal offer accepted | High |
| Job offer / fired | High |
| Selection Sunday | High |
| Sunday coach points | Medium |
| Game day tip-off | Medium (user opt-in) |
| Fundraising complete | Low |

### Accessibility

- Large tap targets on calendar.
- Color-blind safe pack colors; pattern overlays for jersey sim.

---

## Engineering milestones

Ordered build tracks toward **shippable game** (not a separate “demo SKU”).

| Track | Delivers |
|-------|----------|
| **Calendar & saves** | Day/week kernel, local persistence, stable IDs, pack overlay |
| **Possession sim** | 40-min engine, coherence checks, box scores — [`simulation-engine.md`](simulation-engine.md) |
| **Dynasty systems** | Offseason, recruiting, portal, facilities |
| **Full league** | 365-team scheduling, daily batch sim, derived résumé |
| **March** | Conference templates, 76-field bracket, opening round |
| **Ship** | 2D UI, sliders, notifications, optional identity import |

### Remaining product decisions (short list)

- Local save format (SQLite vs files).
- URL pack **signing** for optional one-time import while offline play remains default.

---

## Glossary

| Term | Meaning |
|------|---------|
| **Pack** | Identity overlay JSON (teams, names, templates) |
| **Ruleset year** | NCAA rule configuration for that real-world year |
| **Resume score** | Composite for bubble/seeding |
| **Opening round** | 12-game First Four phase (76→64) |
| **Grade multiplier** | Academics → recruiting budget |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-07 | Initial organization from game design prep notes |
| 2025-11-07 | Identity import (JSON/URL), preseason scope, sim layers, 2026–27 default era |
| 2025-11-07 | Deep finish: offseason calendar, recruiting/NIL, selection math, tiebreakers, UX, milestones |
| 2025-11-08 | Linked `simulation-engine.md`; text-based offline sim, day-advance league batching |
| 2025-11-08 | NET realism note; save integrity vs per-game variety (simulation doc) |
| 2025-11-08 | `simulation-engine.md` scoped to possession game sim only |
| 2025-11-08 | Save on week advance; replay games if reopened before checkpoint |
| 2025-11-08 | Linked `game-frame.md` (in-season UI shell) |
| 2025-11-08 | Linked `player-cards.md` |
