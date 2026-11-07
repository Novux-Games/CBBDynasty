# Dynasty: College Basketball — Game Design (Planning)

**Platform:** Android (mobile)  
**Mode:** Dynasty / program builder — men's college basketball  
**Status:** Planning — systems outline from design prep (Nov 2025)  
**Default era:** **2026–27** season year at new dynasty start

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
| **Time model** | Calendar-driven flow used across all game eras; **default start 2026–27** |
| **Identity data** | **Generic default** or **imported** team/conference pack (JSON / URL), same schema as real-world layout |
| **Depth** | Offseason is as important as in-season play |

---

## Licensing, identity packs & save safety

**Goal:** Real schools, conferences, and league **structure** (~365 D-I, real conference formats) without baking sensitive marks into core save data.

This is **not legal advice** — confirm import UX and user-facing disclaimers with counsel before release.

### Chosen approach (design lock)

| Layer | Behavior |
|-------|----------|
| **Ship default** | **Generic** names and presentation; **same data schema** as real-world (IDs, conference sizes, schedule rules, tournament templates). |
| **Optional import** | User supplies **JSON file** or **URL** to an identity pack (teams, conferences, colors, display strings, logos if allowed). |
| **Runtime swap** | Player can **revert to default** or **re-import** anytime from settings; operation is a **display/identity overlay**, not a destructive world rebuild. |
| **Save integrity** | Dynasty progress (rosters, history, sim state) stays keyed on **stable internal IDs**; swapping packs **must not corrupt** saves. |

**Implementation principles:**

- **One schema** for default and imported packs — validators reject malformed imports; partial apply is not allowed (all-or-nothing per import).
- **Version stamp** on packs (e.g. `membershipYear: "2026-27"`) for UI warnings when pack era ≠ dynasty era.
- Avoid **real player/coach likenesses** in marketing; in-game personas remain procedural/fictional unless licensed.
- **NIL in-game** stays **abstract economics** (budget, deals, reputation), not real athlete IP.
- **Disclaimer** on import: user attests rights to supplied assets; base game remains unofficial.

### Other paths (reference only)

| Approach | Notes |
|----------|--------|
| Official / partner license | Optional future; not required for v1 identity model |
| Historical marks | Special packs later; same import pipeline |

**Decision log:**

- [x] Generic default + JSON/URL import, swappable without save corruption
- [ ] Final disclaimer copy and store policy for URL imports
- [ ] Pursue any institutional or media partnership

---

## Roster & scholarships

Aligned with **current rules** (tune annually if NCAA/NIL rules change in product roadmap):

| Rule | Design |
|------|--------|
| **Roster cap** | **15 players maximum** |
| **Scholarships** | Up to **15**; programs **need not** award all 15 |
| **Walk-ons** | **Preferred walk-ons** allowed; still bound by 15-man cap |
| **Redshirt** | Standard **redshirt** seasons; **medical redshirt** when eligibility rules are met (injury threshold / games played — tune to era) |

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

**Offseason week 1 — transfer window + pro days**

- **First week of offseason** opens the **transfer portal window** (aligned with portal gameplay).
- **Pro days** (offseason, not preseason):
  - **Graduates** and **declaring undergraduates** participate this week.
  - Influences **stay/go** and **draft stock** before final roster decisions.
- **NBA draft results** publish at **week 1 of recruiting** (start of the 5-week recruiting phase), after pro-day declarations resolve.

### 3. Recruiting & transfer portal (**5 weeks**)

- **Recruiting window:** fixed **5-week** offseason phase (not in-season recruiting — design lock unless changed later).
- **Custom recruiting engine** — not a clone of legacy CBB sims; supports **visits** as a mechanic within our ruleset (timing, budgets, and outcomes defined in recruiting spec).
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

### Preseason features (scoped)

| Feature | Plan |
|---------|------|
| **Media & preview rankings** | Polls / preview pieces entering exhibitions; updates form and scheduling leverage |
| **Exhibition scheduling UX** | Pick up to **3** October exhibitions (dates, opponents, home/away) with calendar feedback |
| **Redshirt / medical redshirt** | Preseason confirmation of redshirt intent; in-season injury paths toward medical redshirt (see Roster) |
| **Recruiting carryover** | Offseason visit results and commitments **display** in roster/recruiting UI; no in-season recruiting window |

---

## Season gameplay

- **Calendar** is source of truth for sim speed and player tasks.
- **Games:** sim or play (play scope TBD — full game vs possession sim vs coach mode).
- **Weekly rhythm:** Sunday coach upgrades; midweek games; recruiting **off** during season (locked).

### In-season simulation layers (scoped)

All layers below are **in scope** for the sim; league and selection logic should use **tiebreakers and ordering rules grounded in real NCAA/conference data** (seeded from research tables per conference / NCAA).

| Layer | Behavior |
|-------|----------|
| **Injuries** | Availability, recovery, medical staff / facility modifiers |
| **Suspensions** | Discipline and eligibility holds |
| **Academic eligibility** | Grades, incompletes, APR pressure tied to recruiting budget meta |
| **In-season transfer portal** | Modeled per **calendar year** of the dynasty when real rules change |
| **Conference standings** | **Real-style tiebreakers** (head-to-head, record vs shared opponents, etc.) per conference pack data |
| **Selection metrics** | NET / quadrant-style résumé inputs for bubble and seeding (committee spec; not claiming official accuracy) |

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

**Content pipeline:**

- **Default membership year:** **2026–27** at dynasty start (realignment data and schedule generators keyed to this era).
- **Import packs** may target other years; game warns on mismatch but does not block play.
- Update path for annual realignment: new default pack versions + optional URL imports; **internal IDs stable** across pack swaps.

---

## Systems map (high level)

```
Year loop (default start: 2026–27)
├── Offseason
│   ├── Week 1: transfer window + pro days (grads / early entry)
│   ├── Coaching carousel & contracts
│   ├── Graduation / portal / cuts / draft declarations
│   ├── Recruiting (5 wks) + NIL + budget from grades — draft results posted week 1
│   ├── Summer workouts (auto)
│   └── Fundraising, upgrades, naming rights, conference offers
├── Preseason
│   ├── Schedule build (32 cap, tournaments)
│   ├── Media & preview rankings
│   ├── Exhibition scheduling UX (≤3, October)
│   ├── Coach upgrades (+ auto)
│   └── Preseason workouts (auto); redshirt / medical redshirt state
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

### Remaining backlog

- Awards, all-conference, and national player honors cadence
- Scrimmages vs exhibitions (copy / rules clarity)
- NBA early-entry withdrawal deadline vs pro-day week ordering (edge cases)
- Recruiting engine spec (visits, NIL, custom board UX)

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-07 | Initial organization from game design prep notes |
| 2025-11-07 | Identity import (JSON/URL), preseason scope, sim layers, 2026–27 default era |
