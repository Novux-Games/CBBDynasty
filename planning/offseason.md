# Offseason — calendar & phases

**Design lock:** Dynasties **start in offseason**, not preseason. Most coaching movement happens here — matching real hiring cycles. **Preseason** begins after offseason ([`preseason.md`](preseason.md)). **Entry:** after **[End of Season week](season-review.md)** (post postseason).

**Shell:** Same dynasty chrome as in-season — [`game-frame.md`](game-frame.md). **Recruiting depth:** [`recruiting-engine.md`](recruiting-engine.md). **Carousel / contracts:** [`game-design.md`](game-design.md#coaching-carousel--contracts). **New dynasty entry:** [`game-start.md`](game-start.md) → **Offseason Week 1**.

**Last updated:** 2025-11-09

---

## Contents

1. [Philosophy](#philosophy)
2. [Calendar overview](#calendar-overview)
3. [Season 1 vs later seasons](#season-1-vs-later-seasons)
4. [Coaching Carousel](#coaching-carousel)
5. [Week 1 — Players Leaving](#week-1--players-leaving)
6. [Weeks 2–5 — Recruiting & Transfer Portal](#weeks-25--recruiting--transfer-portal)
7. [Week 6 — Summer Workouts](#week-6--summer-workouts)
8. [Week 7 — Cuts & Roster Lock](#week-7--cuts--roster-lock)
9. [Week 8 — Fundraising & Upgrades](#week-8--fundraising--upgrades)
10. [Week 9 — Conference Offers](#week-9--conference-offers)
11. [Advance & save](#advance--save)
12. [Cross-links](#cross-links)

---

## Philosophy

| Idea | Implementation |
|------|----------------|
| **Offseason-first start** | After **Sign contract**, save opens on **Offseason Week 1** — not exhibition prep |
| **Portal realism** | **Week 1** retention on **your** roster; **weeks 2–5** national portal + HS/JUCO recruiting |
| **Carousel realism** | Labeled **Coaching Carousel** (not “Week 0”); runs **before** Week 1 on **season 2+** |
| **User agency** | AI carousel resolves in background; **your** offers, extensions, assistant hires need approval |

User advances **week** only; dashboard tasks gate **Advance week** ([`game-frame.md`](game-frame.md#dashboard-task-gate)).

---

## Calendar overview

Display order each **offseason** (after the last season’s postseason):

```
┌─────────────────────────────────────────────────────────────┐
│  Season 2+ only:  COACHING CAROUSEL  (unlabeled week #)   │
├─────────────────────────────────────────────────────────────┤
│  Week 1   Players Leaving (+ retention, pro days)          │
│  Week 2   Recruiting / Portal  +  Pro draft results        │
│  Week 3   Recruiting / Portal                              │
│  Week 4   Recruiting / Portal                              │
│  Week 5   Recruiting / Portal                              │
│  Week 6   Summer Workouts (auto)                             │
│  Week 7   Cuts & Roster Lock                               │
│  Week 8   Fundraising & Upgrades                           │
│  Week 9   Conference Offers (skip Season 1)                │
└─────────────────────────────────────────────────────────────┘
                          →  PRESEASON  (next doc)
```

**Save field:** `offseasonWeek` **1–9** during numbered weeks; `phaseId: "coaching_carousel"` when in carousel (no “Week 0” in UI).

| UI label | Internal | Season 1 |
|----------|----------|----------|
| **Coaching Carousel** | `coaching_carousel` | **Skipped** |
| **Offseason Week 1** … **9** | `1` … `9` | Weeks **1–8** run; **Week 9 skipped** |

---

## Season 1 vs later seasons

| Phase | Season 1 (new save) | Season 2+ |
|-------|---------------------|-----------|
| **Coaching Carousel** | **No** — user just hired at school | **Yes** — full league carousel |
| **Week 1** | Generated/handoff roster; seniors may graduate; **≥1** transfer intent on user roster | Post-season roster rollover |
| **Week 9 Conference Offers** | **Skipped** | Realignment offer cards |

**Preseason** follows the same way after Week 8 (Season 1) or Week 9 (later).

---

## Coaching Carousel

**UI title:** **Coaching Carousel** only — never “Week 0.”

**When:** First phase of offseason **starting Season 2** (after Year 1 postseason completes).

### Background (auto-resolve)

Runs when user enters carousel or taps **Sim carousel**; no per-school approval.

| Event | Behavior |
|-------|----------|
| **AI head coach firings** | Job security / AD logic ([`coach-card.md`](coach-card.md)) |
| **AI head coach hires** | Vacancies filled from candidate pool |
| **AI contract extensions** | Programs extend or part ways |
| **Assistant poaches** | AI schools steal assistants per rules |

### Manual (user approval required)

| Event | User action |
|-------|-------------|
| **Head coach job offers** | Accept / decline / negotiate (user **unemployed** or **poached**) |
| **Contract extension** | **Renegotiate** or **walk** (reject extension) |
| **Assistant hires** | Approve offer, salary, role |
| **Assistant poaches targeting user staff** | Counter or release (when rules trigger defend flow) |

Notifications queue **offer cards**; carousel **pauses** for mandatory decisions before Week 1 unlocks.

**User employed at same school:** You still resolve **your** extension (if offered) and **assistant** staffing — league churn happens around you.

---

## Week 1 — Players Leaving

**Label:** **Offseason Week 1** · **Players Leaving**

**New saves land here** after [`game-start.md`](game-start.md).

### Rosters processing

**Layout tabs:** **Retention** · **Roster** (departures) · draft declare lists — portal rules: [`transfer-portal.md`](transfer-portal.md).

| Bucket | UI | Interaction |
|--------|-----|-------------|
| **Graduating seniors** | **Roster** tab | **No** stay — they **graduate** (roster exit) |
| **Draft-eligible seniors** | **Roster** + pro days | Pro days (below); leave for draft or graduate |
| **Potential transfers** | **Retention** tab | **Convince to stay** — success → **never** in portal ([`transfer-portal.md`](transfer-portal.md#retention-vs-portal-tab)) |
| **Early draft declarations (underclass)** | Declare list | **Convince to stay** (return) or allow leave |
| **Coach-initiated departures** | **Roster** tab | **Encourage transfer** / cut during **Week 1 window only** |

**Design lock:** **At least one** player on the **user** roster will enter the **transfer portal intent** pool (AI schools also generate movement league-wide).

**Cannot convince:** Graduates / completed eligibility — seniors who are done are **gone**, not retention targets.

### Retention (transfer / early draft)

Authoritative: [`player-loyalty-promises.md`](player-loyalty-promises.md) — **loyalty**, **promise kept/broken**, **no auto-declare** for elites.

| Tool | Notes |
|------|--------|
| **NIL bump** | Collective allocation |
| **Loyalty** | **0–99** — high loyalty eases stay; low + broken promise hardens leave |
| **Promise status** | **Kept** stay pledge / minutes pledge strongly helps; **Broken** pushes portal or declare |
| **Coach ratings** | Recruiting / portal retention skills |
| **Staff grade** | [`game-design.md`](game-design.md) assistant modifiers |

**Early draft:** Underclass **declare intent** is **rolled** (mock rank + loyalty + promises) — not automatic for Top 60. User **convince to stay** before withdrawal deadline.

Failed retention → player enters **portal** for **weeks 2–5** recruiting (or exits draft pool per declare rules).

### Pro days

**Who:** **Seniors** and **undergraduates who declared** for the draft.

| Behavior | Detail |
|----------|--------|
| **Sim** | Training event adjusts **draft stock** (hidden rating + mock rank) |
| **User** | Run pro day per player or batch **Sim all** |
| **Results** | Stock locked for **Week 2** **Pro draft results** |

Week 1 does **not** post final draft order — results publish in **Week 2**.

---

## Weeks 2–5 — Recruiting & Transfer Portal

**Labels:** **Offseason Week 2** … **Week 5** · shared shell **Recruiting & Transfer Portal**

Authoritative recruiting rules: [`recruiting-engine.md`](recruiting-engine.md#year-long-recruiting-cycle-design-lock). **Spend** is **year-long** (preseason through week 5); these four weeks are **signing crunch** (1.5× drip, portal peak).

| Week | Highlights |
|------|------------|
| **2** | **R1** — **NBA draft results**; scholarships open; portal **sub-tab** peak; signing crunch begins |
| **3** | **R2** — Peak offers; NIL pushes |
| **4** | **R3** — Commit wave |
| **5** | **R4** — **Signing close**; hard commits lock |

**Portal:** Incoming class from league-wide entries (including your Week 1 losses). **HS/JUCO:** Pool seeded **last preseason**; user may have been spending all season — crunch weeks close the class.

**Recruiting week index:** R1 = offseason week 2, … R4 = offseason week 5.

---

## Week 6 — Summer Workouts

**Label:** **Offseason Week 6** · **Summer Workouts**

| Behavior | Detail |
|----------|--------|
| **Auto-run** | **Sim workouts** on **returning** roster (no user play-by-play) |
| **Drivers** | Coach **Training** rating, facility grades, age, potential ([`game-design.md`](game-design.md)) |
| **Pro day overlap** | Gains stack philosophy with Week 1 pro days for draft-bound only |
| **Auto-upgrade** | If enabled ([`game-start.md`](game-start.md)), apply XP deltas here when not mid-season |

User: **Advance week** or review **workout report** (optional sheet).

---

## Week 7 — Cuts & Roster Lock

**Label:** **Offseason Week 7** · **Cuts & Roster Lock**

| Rule | Detail |
|------|--------|
| **Scholarship cap** | **15-man** max ([`game-design.md`](game-design.md)) |
| **Cuts** | User cuts dead weight; AI trims |
| **Walk-on tryouts** | Optional **≤2** walk-ons per season ([`recruiting-engine.md`](recruiting-engine.md#walk-ons)) after main signing |
| **Lock** | End of week → **roster locked** for preseason exhibitions |

Must be ≤ **15** before leaving Week 7.

---

## Week 8 — Fundraising & Upgrades

**Label:** **Offseason Week 8** · **Fundraising & Upgrades**

| Activity | Detail |
|----------|--------|
| **Fundraising events** | Gala, alumni game, donor drive → cash + collective + facility seed |
| **Facility upgrades** | Arena, practice, medical, recruiting lounge, etc. |
| **Naming rights** | Optional arena rebrand ([`game-design.md`](game-design.md)) |

Upgrades apply per facility rules (some bonuses **next recruiting cycle**).

---

## Week 9 — Conference Offers

**Label:** **Offseason Week 9** · **Conference Offers**

| Rule | Detail |
|------|--------|
| **Season 1** | **Phase skipped** — calendar jumps from Week 8 → **Preseason** |
| **Season 2+** | Realignment / conference invite **offer cards**; accept/decline ([`game-design.md`](game-design.md#conference-realignment)) |

---

## Advance & save

| Action | Effect |
|--------|--------|
| **Advance week** | Commit phase outcomes; local save checkpoint (after dashboard tasks for that week) |
| **Enter season** | After final offseason week → **preseason** module |

Dynasty metadata: `offseasonWeek`, `phaseId`, `seasonIndex`, `skipConferenceOffers` (Season 1).

---

## Cross-links

| Topic | Doc |
|-------|-----|
| Preseason | [`preseason.md`](preseason.md) |
| Recruiting / portal UI | [`recruiting-engine.md`](recruiting-engine.md) |
| In-season shell | [`game-frame.md`](game-frame.md) |
| NIL / fundraising math | [`game-design.md`](game-design.md#nil--program-economy) |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-09 | Initial calendar: carousel, weeks 1–9, Season 1 exceptions |
