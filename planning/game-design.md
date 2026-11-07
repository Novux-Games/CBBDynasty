# Dynasty: College Basketball — Game Design (Planning)

**Platform:** Android (mobile)  
**Mode:** Dynasty / program builder — men's college basketball  
**Status:** Planning — systems outline from design prep (Nov 2025)

---

## Vision (working)

Build a deep **program dynasty** sim where the player runs a Division I men's basketball program across years: coaching, roster, recruiting (including NIL), facilities, conference politics, and a full seasonal calendar through March Madness — with **all ~365 D-I teams** and **real conference structure** as a design target.

---

## Scope pillars

| Pillar | Direction |
|--------|-----------|
| **Teams** | ~365 NCAA Division I men's programs |
| **Conferences** | Real conference alignment and sizes (e.g. SEC/Big Ten → 20 league games) |
| **Roster** | Current NCAA-style limits (15-player cap, scholarship rules below) |
| **Time model** | Calendar-driven flow used across all game eras |
| **Depth** | Offseason is as important as in-season play |

---

## Licensing & real names (planning notes)

**Goal:** Real schools, conferences, and league structure without blocking shipping or creating unacceptable legal risk.

This is **not legal advice** — confirm strategy with counsel before release. Common **design and product paths** to research:

| Approach | Pros | Cons / notes |
|----------|------|----------------|
| **Fictional universe** | Full control; no third-party marks | Less fantasy for hardcore CBB fans |
| **Real structure, generic labels** | "365 teams, 32 conferences" feel without marks | Fans map mentally; still need care with likeness |
| **User / community data packs** | Base game ships generic; optional imports | Platform policy, support burden, moderation |
| **Historical or expired marks** | Sometimes usable for specific eras | Narrow; doesn't solve full D-I set today |
| **Official / partner license** | Real brands | Cost, exclusivity, platform constraints — often impractical for indie |
| **Editor-style disclosure** | "Unofficial fan sim; no affiliation" | Does not replace clearance for logos/names |

**Practical planning defaults until legal sign-off:**

- Treat **team names, logos, fight songs, and broadcaster-style presentation** as a **swappable data layer** (real ↔ fictional ↔ mixed).
- **Conference names and membership** can ship as **structure-first** (member lists, game counts, tournament formats) with **display names** configurable.
- Avoid **real player/coach likenesses** in marketing; use **procedural or fictional personas** unless licensed.
- **NIL in-game** should be **abstract economics** (budget, deals, reputation), not real athlete IP.

**Decision log (TBD):**

- [ ] Ship generic first, real-names DLC/pack later?
- [ ] Single "college basketball" disclaimer + fictional default?
- [ ] Pursue any institutional or media partnership?

---

## Roster & scholarships

Aligned with **current rules** (tune annually if NCAA/NIL rules change in product roadmap):

| Rule | Design |
|------|--------|
| **Roster cap** | **15 players maximum** |
| **Scholarships** | Up to **15**; programs **need not** award all 15 |
| **Walk-ons** | **Preferred walk-ons** allowed; still bound by 15-man cap |

**Implications for sim:**

- Scholarship management is a **resource puzzle** (partial vs full rides, walk-on development arcs).
- Roster construction interacts with **transfer portal**, **graduation**, **draft losses**, and **cuts** in offseason windows.

---

## Macro calendar (all eras)

One **master calendar** drives exhibition, non-conference, conference play, postseason, and offseason phases.

### Season (in-year)

| Phase | Timing | Notes |
|-------|--------|--------|
| **Exhibition** | Throughout **October** | Up to **3** exhibition games |
| **Non-conference** | **November – December** | User scheduling / sim |
| **Conference / mix** | **January** | Full league schedule for large conferences; smaller leagues may mix non-conference |
| **Conference** | **February** | League play focus |
| **Conference tournaments** | **Early March** | Formats researched per real conference |
| **Selection Sunday** | **Mid-March** | Bracket reveal |
| **March Madness** | **Mid-March → early April** | See postseason |
| **Coach skill upgrades** | **Each Sunday in season** (optional **auto**) | Same upgrade system as preseason |

### Offseason (between seasons)

See **Offseason systems** below — recruiting, portal, carousel, facilities, etc.

### Preseason (before exhibitions)

See **Preseason** — scheduling, coach upgrades, automated workouts.

---

## Offseason systems

### 1. Coaching carousel

- **Deep carousel** — hires, fires, poaching, contract years, performance pressure.
- **Coaching contract management** — salary, length, buyouts, extension decisions (player and AI programs).
- Ties into **program prestige**, **boosters**, and **postseason success**.

### 2. Roster churn

| Channel | Description |
|---------|-------------|
| **Graduation** | Class cycle; senior leadership loss |
| **NBA draft** | Early entrants; stay/go decisions where applicable |
| **Transfer portal** | Entries and arrivals |
| **Cuts** | Roster trimming in designated **offseason window** |

### 3. Recruiting & transfer portal (**5 weeks**)

- **Recruiting window:** fixed **5-week** offseason phase (not in-season recruiting — design lock unless changed later).
- **NIL:** integrated as a **recruiting / retention** factor (abstract deals and budget, not real individuals).
- **Recruiting budget:**
  - Earned from **academic grades** (program APR/grades metaphor — exact formula TBD).
  - **Upgrades** made this offseason can improve **next year's** recruiting cycle (facilities, staff, NIL collectives, etc.).

### 4. Summer workouts (automated)

- Progression driven by:
  - **Head coach** quality / development traits
  - **Facility grade** (arena, practice, strength, medical, etc.)
- Player development outcomes feed **preseason ratings** and **scheme fit**.

### 5. Offseason window: cuts, conference movement, fundraising

| System | Behavior |
|--------|----------|
| **Cuts** | Trim to 15; scholarship redistribution |
| **Conference changes** | Programs **accept or decline** invites based on **performance** and conference logic — **no relegation** |
| **Fundraising / upgrades** | Raise funds; improve **arena** and **facilities** |
| **Naming rights** | Sell **arena naming** (revenue vs fan pride tradeoffs — TBD) |

---

## Preseason

### Scheduling

| Constraint | Value |
|------------|--------|
| **Regular season cap** | **32** games total |
| **Conference games** | Up to **20** (depends on conference size; e.g. SEC/Big Ten) |
| **Non-conference + conference** | Must sum within 32-game cap |
| **Tournaments** | **4 games**; joining a tournament uses **28** other games + tournament (32 hard cap) |

**Design tasks:**

- Solver or UI for **valid schedules** (conference matrix + user non-conference choices + optional exempt tournaments).
- Conference offices may auto-fill **league games**; user picks **non-conference** and **early-season events**.

### Coach upgrades

- Spend **skill points** when available (preseason + weekly in-season per above).
- **Auto-upgrade** toggle for casual play.

### Preseason workouts

- **Automated** (like summer); sets readiness, chemistry baselines, injury risk modifiers if used.

### Preseason gaps to define (from brainstorm)

- Media / preview rankings?
- Pro days / scouting combine for draft prospects?
- Exhibition scheduling UX (3 games in October)?
- Redshirt / medical redshirt rules?
- Official visits / recruiting carryover from offseason (display only)?

---

## Season gameplay

- **Calendar** is source of truth for sim speed and player tasks.
- **Games:** sim or play (play scope TBD — full game vs possession sim vs coach mode).
- **Weekly rhythm:** Sunday coach upgrades; midweek games; recruiting **off** during season (locked).

**Open sim layers (TBD):**

- Injuries, suspensions, academic eligibility
- In-season portal (if real-world rules modeled year-by-year)
- Conference tiebreakers, NET/quadrant metaphors for selection

---

## Postseason

### Conference championships

- **Research task:** document each conference's **bracket format**, seeding, byes, and venue rules.
- Store as **data-driven templates** per conference.

### NCAA tournament (March Madness)

| Element | Plan |
|---------|------|
| **Field** | **76 teams** |
| **Opening round** | **12 games** → winners join **field of 64** |
| **Bracket** | Standard 64-team path to champion |
| **Culmination** | **Final Four** + **national championship** |

**Selection Sunday:**

- Auto committee logic (TBD): résumé, auto bids, bubble, metrics inspired by real selection without claiming official accuracy.

---

## Teams & conferences (content)

| Item | Target |
|------|--------|
| D-I membership | **~365** programs |
| Conferences | Real-world alignment and sizes |
| Game counts | Per-conference schedule generators (up to 20 league games) |
| Realignment | Offseason offers; performance-driven; **no relegation** |

**Content pipeline (TBD):**

- Source of truth for membership (year stamped: e.g. 2025–26).
- Update path for annual realignment without breaking saves.

---

## Systems map (high level)

```
Year loop
├── Offseason
│   ├── Coaching carousel & contracts
│   ├── Graduation / Draft / Portal / Cuts
│   ├── Recruiting (5 wks) + NIL + budget from grades
│   ├── Summer workouts (auto)
│   └── Fundraising, upgrades, naming rights, conference offers
├── Preseason
│   ├── Schedule build (32 cap, tournaments)
│   ├── Coach upgrades (+ auto)
│   └── Preseason workouts (auto)
├── Season (calendar)
│   ├── Oct: exhibitions (≤3)
│   ├── Nov–Dec: non-conference
│   ├── Jan–Feb: conference (± mix)
│   ├── Weekly: coach upgrades (Sun)
│   └── Early Mar: conference tournaments
└── Postseason
    ├── Selection Sunday
    ├── Opening round (12) → 64
    └── Final Four & championship
```

---

## Open questions & next design passes

**Enough to start?** Yes — this is sufficient for **vertical slice planning**, **data model sketch**, and **calendar prototype**. Below are the highest-value follow-ups.

### Must research

- [ ] Real **conference tournament** formats (all leagues in scope or tiered by priority).
- [ ] **Opening round** / First Four style bracket math for field of 76 → 64.
- [ ] NCAA rule changes timeline if game spans multiple real years.

### Must specify (mechanics)

- [ ] NIL economy: ceilings, boosters, collective vs individual, retention vs recruiting.
- [ ] Grade → recruiting budget formula and facility upgrade payoffs.
- [ ] AI coaching carousel aggression and contract math.
- [ ] Conference invite logic (no relegation but promotion-style moves).

### Product / UX

- [ ] Sim depth on mobile (what's manual vs auto each phase).
- [ ] Notifications for carousel, portal, recruiting, selection Sunday.

### Missing preseason / in-season tasks (candidate backlog)

- Academic eligibility and APR consequences
- Injuries and medical staff impact
- Practice squad / scout team (if any — likely N/A for hoops)
- Scrimmages vs exhibitions
- Polls, awards, all-conference
- NBA draft combine and early entry withdrawal deadlines

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-07 | Initial organization from game design prep notes |
