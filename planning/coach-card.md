# Coach card

**Context:** In-season and offseason whenever a **head coach** is tappable (your career, School hub staff, opponent scout, carousel offers) — see [`game-frame.md`](game-frame.md).  
**Systems source:** Coaching carousel, contracts, skills — [`game-design.md`](game-design.md#coaching-carousel--contracts).  
**Default schemes:** Preset tables in [`strategy-schemes.md`](strategy-schemes.md) (coach card = **default** selection; Strategy hub = **per-game** prep).  
**Last updated:** 2025-11-08

---

## Contents

1. [Entry flow](#entry-flow)
2. [Chrome (persistent header)](#chrome-persistent-header)
3. [Tabs](#tabs)
4. [Overview](#overview)
5. [Contract](#contract)
6. [History](#history)
7. [Hall of Fame](#hall-of-fame)
8. [Draft](#draft)
9. [Staff (School hub)](#staff-school-hub)
10. [Edit mode](#edit-mode)
11. [Cross-links](#cross-links)

---

## Entry flow

```
Tap head coach (School staff, carousel offer, league leaderboards, etc.)
        │
        ▼
   Coach preview (modal / sheet)
   · Quick info (name, school, record, headline ratings)
   · [View more] ──────────────────┐
        │                           │
   dismiss                          ▼
                            Full coach card (this doc)
```

**Preview:** fast read; no tab chrome required.  
**Coach card:** full-screen (or primary route) with tabs below.

**Your coach:** reachable from **School hub → Staff** (head coach row), dynasty shortcuts, notifications (extension, carousel).  
**Other coaches:** view-only card when scouting opponents or reviewing job offers.

---

## Chrome (persistent header)

Fixed for all tabs (does not scroll away with tab content):

| Element | Placement |
|---------|-----------|
| **Back** | Top-left → closes card / returns to caller |
| **Name** | Title center or left-of-center |
| **Edit** | Top-right → [Edit mode](#edit-mode) when allowed (**user head coach only**) |

**Below title row (identity strip):**

| Field | Notes |
|-------|--------|
| **Portrait or avatar** | Generated; pack-safe |
| **Current school** | Display name from identity pack |
| **Role** | **Head coach** (assistants use **staff** UI, not this card) |

---

## Tabs

| Tab | Purpose |
|-----|---------|
| **Overview** | Bio, contract summary, achievements, default strategy, headline ratings |
| **Contract** | Deep contract management |
| **History** | Deep season-by-season coaching history |
| **Hall of Fame** | HOF players coached (national & conference) |
| **Draft** | Players coached who were drafted |

Tab bar sits under identity strip; content scrolls per tab.

---

## Overview

### Team name + record card

- School **logo**, **name**, **current season record** (W–L).
- **Tap** → **School hub** for that program ([`game-frame.md`](game-frame.md#school-hub)).
- User school: full hub; opponent coach: their school hub (view-only).

### Information

| Field | Notes |
|-------|--------|
| **Age** | Coach age (birth year derived) |
| **Career experience** | Seasons as head coach (all schools, dynasty) |
| **School experience** | Seasons at **current** program |
| **Contract overview** | See [subfields](#contract-overview-on-overview) below |

#### Contract overview (on Overview)

Single grouped block — detail editing on **Contract** tab.

| Field | Notes |
|-------|--------|
| **Years** | Contract length (e.g. `4-year deal`) |
| **Years left** | Remaining seasons on current deal |
| **Prestige** | **Program prestige tier** at signing / current school prestige snapshot (display label from program state; not coach OVR) |

### Achievements

Career totals as **head coach** unless a row says “at current school.” Scope: **this dynasty save** (multi-school careers accumulate across stops).

| Metric | Notes |
|--------|--------|
| **Record** | W–L |
| **Win %** | Wins ÷ (W + L) |
| **Tournament record** | NCAA tournament games W–L |
| **Tournament win %** | NCAA tourney wins ÷ tourney games |
| **National titles** | NCAA championships |
| **Final Fours** | |
| **Tournament appearances** | NCAA bids |
| **Conference titles** | Regular-season and/or tournament auto-bids per game-design conference rules (display both if tracked separately; v1 may show **conference tournament titles** as primary chip) |
| **Secondary tournament record** | Postseason **secondary** bracket W–L (NIT-style; league structure **TBD** — likely mirror real NIT eligibility/format in data) |
| **Secondary tournament win %** | Wins ÷ games in secondary bracket |
| **Secondary tournament wins** | **Championships** in secondary bracket (e.g. NIT titles when that template ships) |

**TBD (product):** exact secondary-tournament slot map and label in UI (`NIT`, generic “Secondary Tourney”, or pack-named). Achievement rows ship **with** the bracket template; show `—` until league sim exposes the bracket.

### Strategy

**Coach default** offensive and defensive schemes — set at dynasty setup / coaching profile, **not** the live gameplan.

| Field | Notes |
|-------|--------|
| **Offensive scheme** | One of seven presets ([`strategy-schemes.md`](strategy-schemes.md#offensive-scheme-presets-7)) |
| **Defensive scheme** | One of seven presets ([`strategy-schemes.md`](strategy-schemes.md#defensive-scheme-presets-7)) |

These defaults **pre-fill** Strategy hub before each game; user may change per game and at halftime without changing coach card defaults (unless [Edit mode](#edit-mode) updates defaults).

### Rating

Headline coach ratings (**0–99** integers in UI). Feeds sim modifiers, recruiting, development — see [`game-design.md`](game-design.md#coaching-carousel--contracts).

| Rating | Role |
|--------|------|
| **Offensive** | In-game tactics / scheme efficiency |
| **Defensive** | In-game tactics / scheme efficiency |
| **Training** | Player development / summer gains |
| **Recruiting** | Visits, interest, charisma analog |

**Not shown here:** aggregate **staff grade** from assistants (School **Staff** section); staff modifiers stack in systems layer.

---

## Contract

Deep contract management for **user head coach**; **read-only** mirror for scouting other HCs you might poach or replace.

### Fields

| Field | Behavior |
|-------|----------|
| **Years remaining** | Countdown; rollover in offseason |
| **Total contract length** | Original term |
| **Salary** | Annual; affects AD trust / budget story |
| **Buyout** | Cost to leave or for school to fire you |
| **Performance goals** | Sweet 16, .500 conference, win total, etc. |
| **Extension offers** | Incoming from AD when trust high |
| **Renegotiate** | Adjust years/salary when allowed |
| **Resign / retire** | Exit to carousel |

### Flows (tie-in)

| Event | UI surface |
|-------|------------|
| **Sunday skill points** | Notification → Coach card or quick sheet ([`game-design.md`](game-design.md)) |
| **Carousel job offer** | Offer card → opponent School hub + this coach’s Contract (proposed deal) |
| **Fired** | Contract tab shows termination; routes to job list |
| **Buyout to leave** | Confirm buyout from Contract tab before accepting external offer |

Assistant contracts and staff salaries live under [Staff (School hub)](#staff-school-hub), not this tab.

---

## History

**Deep season history** for this head coach (career arc in save).

### Per season row / card

| Field (examples) |
|------------------|
| Season year |
| School (if multi-stop career) |
| Record (W–L) |
| Conference record |
| Postseason line (conf tourney, NCAA, secondary) |
| Awards (coach of year chips) |
| **Skill points earned** (optional subline) |

Tap row → expand **season detail**: full schedule summary, tourney path, roster link, news headlines tag (implementation).

**Current season:** appears at top; in-season rows may update after each **week advance**.

---

## Hall of Fame

Players **coached by this head coach** (any roster stint under them) who earned:

| Pool | Definition |
|------|------------|
| **National HOF** | Dynasty national hall entries attributed to player |
| **Conference HOF** | Conference-specific HOF chips |

List: name, school season(s), position, induction year, tap → [`player-cards.md`](player-cards.md) (retired / archival player record).

Opponent coaches: view-only.

---

## Draft

Players **coached by this head coach** who appeared in **NBA draft results** ([`game-design.md`](game-design.md) offseason week / draft week).

| Column (examples) |
|-------------------|
| Player name |
| Draft year |
| Pick (#1–60 or undrafted marker if tracked) |
| School at departure |
| Tap → player card |

Distinct from **School hub → Drafted players** (program alumni list); this tab is **coach résumé** (includes players drafted after coaching them at prior schools in the same save).

---

## Staff (School hub)

Replaces the old **Head coach–only** block on **School hub → Overview** ([`game-frame.md`](game-frame.md#school-hub)).

### School hub — Staff section

| Element | Behavior |
|---------|----------|
| **Head coach row** | Name, record snippet; tap → [coach preview](#entry-flow) |
| **Assistants** | Associate / assistant / position coaches (names, roles, headline specialty rating) |
| **Support staff** | Trainers, recruiting ops, video, etc. (v1 list depth TBD) |
| **Manage** | **User school only:** hire, fire, promote, offer contract |
| **Staff grade** | Aggregate chip (replaces “light v1” only-in-systems — now visible here) |

**Other schools:** view staff roster; **poach** flows start from a staff row when carousel / offseason rules allow.

### Hiring sources

| Source | Notes |
|--------|--------|
| **Candidate pool** | Generated coaches/staff each offseason; filtered by role |
| **Other schools** | Poach with buyout / reputation cost; AI may counter |
| **In-house promotion** | Optional: bump GA → assistant (TBD) |

### Relationship to coach card

| Surface | What it shows |
|---------|----------------|
| **Coach card** | **Head coach** identity, HC ratings, contract, HC history/HOF/draft |
| **School Staff** | Full bench + support; assistant contracts; poaching |

Assistants do **not** open full coach card tabs; they use a **staff detail sheet** (ratings by role, contract, poach offer). Link from staff row optional later if a staff “card” doc is added.

---

## Edit mode

**Edit** visible for **user head coach** when game phase allows.

| Editable (examples) | Route |
|---------------------|--------|
| **Default offensive / defensive scheme** | Overview → Strategy fields |
| **Skill point spend** | Sunday allocation; skill tree per game-design |
| **Contract actions** | Contract tab |
| **Default auto-spend priorities** | Coach skills automation |

**Other coaches:** no Edit.  
**Staff:** edit on School hub Staff section, not via coach card Edit.

---

## Cross-links

| From | To |
|------|-----|
| Overview team card | School hub |
| School hub Staff → HC row | Coach preview → full card |
| Strategy hub | Per-game scheme overrides defaults shown on Overview |
| Carousel / AD events | Contract tab |
| Statistics / news “coach of year” | Coach preview |
| Sim modifiers | HC offensive/defensive ratings → `GameSimInput` ([`simulation-engine.md`](simulation-engine.md)) |
| Player HOF / draft rows | Player card |

### Mapping ratings ↔ systems

| Card rating | Game-design analog |
|-------------|-------------------|
| Offensive / Defensive | Tactics branch of skill tree |
| Training | Player development |
| Recruiting | Recruiting / NIL charm |
| Staff grade (School hub) | Assistant aggregate bumping visits & development |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Initial coach card spec from user (mirror player card UX) |
