# Coach card

**Context:** In-season and offseason for **head coaches** and **assistant coaches** (coach card). **Support staff** (trainers, etc.) have **no** card UI — see [Staff (School hub)](#staff-school-hub).  
**Applies when:** School hub staff, carousel/poach, league leaderboards — [`game-frame.md`](game-frame.md).  
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
9. [Assistant coach card](#assistant-coach-card)
10. [Staff (School hub)](#staff-school-hub)
11. [Edit mode](#edit-mode)
12. [Cross-links](#cross-links)

---

## Entry flow

```
Tap coach personnel (HC or assistant — School staff, poach, carousel, etc.)
        │
        ▼
   Coach preview (modal / sheet)
   · Quick info (name, role, school, headline ratings)
   · [View more] ──────────────────┐
        │                           │
   dismiss                          ▼
                     Full coach card (HC or assistant variant)
```

**Preview:** fast read; no tab chrome required.  
**Coach card:** full-screen (or primary route) with tabs below.

**Who gets this flow**

| Personnel | Coach card? |
|-----------|-------------|
| **Head coach** | Yes — full tabs ([§ Tabs](#tabs)) |
| **Assistant coaches** | Yes — [assistant variant](#assistant-coach-card) |
| **Support staff** (trainer, video, ops, …) | **No** — list row on School **Staff** tab only; manage via hire/fire/contract sheet, not a card |

**Your head coach:** School hub → Staff, dynasty shortcuts, notifications.  
**Assistants:** same entry when row is tappable; **support rows are not tappable** for profile/card.

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
| **Role** | **Head coach** or assistant title (Associate HC, Assistant, …) |

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
| **Tournament record** | **NCBA Men's BB Tournament** games W–L |
| **Tournament win %** | NCBA tourney wins ÷ tourney games |
| **National titles** | **NCBA** National Championships |
| **Final Fours** | **Championship 4** appearances |
| **Tournament appearances** | NCBA (76-team field) bids |
| **Conference regular-season championships** | outright or co-champion per conference tiebreaker rules |
| **Conference tournament championships** | conference bracket winner (auto-bid path separate from regular-season title) |
| **Secondary tournament record** | **NCI** (National College Invitational) W–L — [`game-design.md`](game-design.md#nci--national-college-invitational-32-teams) |
| **Secondary tournament win %** | Wins ÷ NCI games |
| **Secondary tournament wins** | **NCI championships** |

Display via [`ncba-branding.md`](ncba-branding.md); stats keys stay `secondaryPostseason*`.

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
| **Performance goals** | Super 16, .500 conference, win total, etc. |
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
| Conf. RS champ / conf. tourney champ | Separate chips when either earned that season |

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

Full **Staff** tab on School hub ([`game-frame.md`](game-frame.md#school-hub)). **Overview** may show a one-line HC + “View staff” shortcut; depth lives on the **Staff** tab.

### School hub — Staff tab

| Element | Behavior |
|---------|----------|
| **AD trust** | **User school only** — relationship meter with athletics director; drives extensions, firing leash, goal difficulty (see below) |
| **Head coach row** | Name, record snippet; tap → [coach preview](#entry-flow) |
| **Assistants** | Names, roles, headline rating; tap → [assistant coach card](#assistant-coach-card) |
| **Support staff** | Trainers, video, ops, etc. — **list only** (name, role, rating chip); **no card**; user school **Manage** → inline contract / fire / replace |
| **Manage** | **User school only:** hire, fire, promote, offer contract, poach |
| **Staff grade** | Aggregate chip from filled roles (recruiting pitch efficiency, development — [`game-design.md`](game-design.md)) |

**Other schools:** view staff roster; **poach** flows start from a staff row when carousel / offseason rules allow.

### AD trust (user school)

Placed on **School hub → Staff** tab (not on coach card preview).

| Display | Detail |
|---------|--------|
| **Meter** | 0–100 (or tiered Low / Steady / Strong) with short AD name from pack |
| **Trend** | Optional `↑` / `↓` chip after bad loss streak, tourney run, missed goals |
| **What it affects** | Extension willingness, buyout negotiation, time-to-fire warnings, optional facility / budget narrative hooks |
| **Inputs (systems)** | Win trajectory vs contract goals, postseason results, scandal/eligibility events, conference expectations |

**Coach card Contract tab** references AD trust (e.g. “AD trust: Strong — extension likely”) but the **canonical meter** sits on Staff tab so it reads as a **program ↔ coach** relationship, not a personal profile stat.

### Hiring sources

| Source | Notes |
|--------|--------|
| **Candidate pool** | Generated **coaches** (HC/assistant track) and **support** hires separately; trainers never offered as HC carousel candidates |
| **Other schools** | Poach with buyout / reputation cost; AI may counter |
| **In-house promotion** | **Graduate assistant** on staff list → after **2** seasons, promote to **assistant** with generated Recruiting/Dev/Scouting ratings |

### Relationship to coach card

| Surface | What it shows |
|---------|----------------|
| **Coach card** | HC + **assistants** (not support staff) |
| **School Staff tab** | AD trust, full roster, hire/fire/poach; support = rows without card drill-in |

### Support staff (no card)

| Rule | Detail |
|------|--------|
| **UI** | Row on Staff tab: name, role, contribution rating, salary snippet (optional) |
| **Interaction** | **No** preview / View more. User school: **Manage** opens compact sheet (extend, fire, replace from pool) |
| **Carousel** | **Trainers** and other support **`eligibleForHeadCoach: false`** — never promoted to HC; not in HC job candidate lists |
| **Career tracking** | Support tenure on **School hub → History** staff log (name, role, seasons); not on individual coach cards |

---

## Assistant coach card

Same **preview → View more** shell as head coach; tab set differs. Assistants may later become HC via carousel/promotion; they stay on the **coach** entity track (unlike trainers).

### Tabs (assistant)

| Tab | Purpose |
|-----|---------|
| **Overview** | Team card, information, **assistant ratings**, contract summary on Overview |
| **History** | Season **résumé snapshots** (see below) — not the deep HC History tab |
| **Contract** | Salary, years left, buyout, poach offer |

**Conditional** (`hasHeadCoachCareer`): **Achievements**, **Hall of Fame**, **Draft** — same content rules as HC coach card for prior HC tenure only.

**Omitted for assistants (current role):** Overview **Achievements** (W–L as HC), **Strategy** (default O/D), deep **History** tab format used for active HCs.

### History (assistant card)

One row per coached season on the **coach track** (assistant or HC positions only — **not** trainer/support seasons).

| Column | Content |
|--------|---------|
| **Season** | e.g. `2026–27` |
| **School** | Program |
| **Position** | Head coach, Associate HC, Assistant, Recruiting coordinator, etc. |
| **Result** | Team **W–L** (+ optional postseason line) |

Tap row → light expand; HC seasons may show **Open head coach card (deep)** for that stop.

**Current season** at top; updates after **week advance**.

### Overview (assistant)

| Block | Notes |
|-------|--------|
| **Team card** | School logo, record → School hub |
| **Information** | Age, years in role, years at school, contract overview (years / years left) — **no prestige** row |
| **Ratings** | **Recruiting**, **Development**, **Scouting** (0–99); feeds staff grade |

### Contract (assistant)

Same patterns as HC Contract but scoped to assistant deals; poach CTA when viewing another school’s assistant.

---

## Edit mode

**Edit** visible for **user head coach** when game phase allows.

| Editable (examples) | Route |
|---------------------|--------|
| **Default offensive / defensive scheme** | Overview → Strategy fields |
| **Skill point spend** | Sunday allocation; skill tree per game-design |
| **Contract actions** | Contract tab |
| **Default auto-spend priorities** | Coach skills automation |

**Other coaches (HC or assistant):** no Edit.  
**User assistants:** contract extend/fire on assistant **Contract** tab or Staff **Manage**.  
**Support staff:** edit only via Staff tab **Manage** sheet (no coach card).

---

## Cross-links

| From | To |
|------|-----|
| Overview team card | School hub |
| School hub Staff → HC row | Coach preview → full card |
| School hub Staff → assistant row | Coach preview → assistant card |
| School hub Staff → support row | Manage sheet only (no card) |
| School hub Staff tab | AD trust (user school) |
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
| Staff grade (School hub) | Assistant aggregate bumping pitch efficiency & development |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Initial coach card spec from user (mirror player card UX) |
| 2025-11-08 | Staff tab + AD trust; staff detail sheets; split conf titles |
| 2025-11-08 | Staff History tab: season/school/position/result for all staff |
| 2025-11-08 | Assistants use coach card; support staff list-only; trainers ineligible for HC |
| 2025-11-08 | Collegiate Invitational achievements; GA promotion; staff log on School history |
| 2025-11-08 | NCBA / NCI achievement labels (`ncba-branding.md`) |
