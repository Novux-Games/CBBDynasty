# Player loyalty & coach promises

**Why:** Avoid **automatic** early draft exits for elite players (Campus Dynasty–style “Top 60 = instant declare”) and give **meaning** to recruiting pitches beyond NIL.  
**Companion:** [`recruiting-engine.md`](recruiting-engine.md), [`player-cards.md`](player-cards.md), [`offseason.md`](offseason.md#week-1--players-leaving), [`game-design.md`](game-design.md#procedural-roster-events).  
**Last updated:** 2026-11-09

---

## Contents

1. [Loyalty rating](#loyalty-rating)
2. [Coach promises](#coach-promises)
3. [Keeping vs breaking](#keeping-vs-breaking)
4. [Transfer risk](#transfer-risk)
5. [Early draft declaration](#early-draft-declaration)
6. [UI & data](#ui--data)
7. [Cross-links](#cross-links)

---

## Loyalty rating

| Rule | Detail |
|------|--------|
| **Scale** | **0–99** integer on the **player record** (same scale family as skill ratings) |
| **Display** | **Roster player card** — [Ratings tab](player-cards.md#ratings) and compact chip on **Overview** |
| **Recruits** | Hidden until **scout depth** threshold; rough **letter band** optional on recruit sheet (“Loyalty tendency: High/Med/Low”) |
| **Sim** | **Not** a possession-engine attribute — drives **portal intent**, **draft declare** rolls, and narrative only |

### Seed & drift

| Event | Loyalty |
|-------|---------|
| **Class generation** | Base from hidden **personality** tag (e.g. hometown kid, one-and-done lean, program guy) — typically **25–75**; tune in balance JSON |
| **Promise kept** (season check) | **+** bump (cap 99) |
| **Promise broken** | **−** large hit |
| **Coach keeps minutes/role in band** | Small **+** per season |
| **Coach fired / user leaves** | **−** for affected promises; portal risk spike |
| **Team success** (tourney run) | Mild **+** league-wide on roster |

---

## Coach promises

**Design lock:**

| Rule | Detail |
|------|--------|
| **One per player** | Exactly **one** promise per player per **program tenure** — pick **one** type at first binding offer; **no** stacking minutes + stay pledge + role |
| **Career-long** | Same promise **follows the player** every season at that school until **transfer out**, **draft exit**, **cut**, or **graduation** — re-checked each evaluation window, not renegotiated each year |
| **When set** | Chosen at **scholarship offer** (or portal equivalent); **locks** on **hard commit** / sign |
| **After transfer** | New school may set **one new** promise at portal sign — prior school’s promise is **archived** on History |

Promises are **binding commitments** the user (or AI coach) makes at offer/sign. They are stored on the player and shown on the card.

### Promise types (design lock — pick exactly one)

| Type | User sets (examples) | Tracked against |
|------|----------------------|-----------------|
| **Minutes** | Band: **Starter** (e.g. ≥26 mpg target), **Rotation** (14–25), **Develop** (&lt;14) | Season **minutes per game** (week-finalized games) |
| **Role** | Starter / sixth man / redshirt year | Lineup usage + games played (redshirt = **0** RS games per ruleset) |
| **Stay pledge** | “Return unless **lottery**” / “unless **top-N** mock” (N tunable, e.g. 14) | Offseason **declare intent** vs mock rank at Week 1 **each eligible year** |

**NIL** is **not** a promise type — it is the signed **deal** on the Overview. Underfunding NIL can still break **trust** (loyalty hit) via separate rule in game-design.

### At recruiting

| Step | UX |
|------|-----|
| **Scholarship offer** | **Promise picker** — user selects **exactly one** type (minutes / role / stay pledge); required for **high**-loyalty-tendency targets, optional for others — tune AI |
| **Hard commit** | That **single** promise **locks** for the career at this school; immutable type — only **status** (Kept / Broken) updates |
| **Amend** | **No** mid-career promise swap or Week 1 “new promise” — retention uses NIL / pitches only ([`offseason.md`](offseason.md)) |
| **Cost** | No extra recruiting **points** for the promise itself — the **binding** is the cost ([`recruiting-engine.md`](recruiting-engine.md#what-recruiting-points-buy-design-lock)) |

---

## Keeping vs breaking

**Evaluation cadence:** End of **regular season** (before postseason) and again at **Offseason Week 1** before retention UI — always against the **same** locked promise text, every year on roster.

| Status | Condition |
|--------|-----------|
| **Kept** | Actual minutes/role within **tolerance** of promise band for the evaluation window (e.g. starter promise: season mpg ≥ target − 3) |
| **Broken** | Sustained miss below tolerance **or** explicit user **cut** / **benched** flag if game scripts a broken role promise |
| **Deferred** | **Redshirt** promise — kept if redshirt rules satisfied |

On **kept:** loyalty **up**, headline optional News “Coach delivers on promise to {name}.”  
On **broken:** loyalty **down**, **transfer risk** ([below](#transfer-risk)), optional News / chemistry chip.

---

## Transfer risk

| Trigger | Effect |
|---------|--------|
| **Broken promise** | Player eligible for **portal intent** at next **Offseason Week 1** only — **no** in-season transfer path ([`transfer-portal.md`](transfer-portal.md)) |
| **Low loyalty** + benching | Adds weight to **≥1 user transfer** design lock ([`offseason.md`](offseason.md)) |
| **Retention Week 1** | **Convince to stay** uses **loyalty**, **promise status**, NIL bump, coach ratings — kept promise is a strong positive |

Failed retention → portal per existing offseason flow.

---

## Early draft declaration

**Design lock:** **No automatic declare** for high mock / Top 60 players. **Declaration is a probabilistic choice** each **Offseason Week 1** for eligible underclassmen (and seniors going early when ruleset allows).

### Declare intent roll (conceptual)

```
baseDeclare = f(mockDraftRank, proPotential)   // higher rank → higher base
modifier -= loyalty * kLoyalty
modifier -= promiseKeptBonus
modifier -= stayPledgeActive ? stayPledgeStrength : 0
modifier += brokenPromisePenalty
P(declare) = clamp(baseDeclare + modifier)
```

| Outcome | Behavior |
|---------|----------|
| **Declares** | Enters **early draft** bucket on Week 1 UI; user may **convince to stay** (retention tools) before withdrawal deadline |
| **Stays** | Remains on roster; loyalty **+** if stay pledge honored by coach |
| **Senior / graduate** | Unchanged — pro day → draft results |

**Elite players can still leave** — lottery-tier mocks keep **material** declare odds — but **high loyalty + kept stay pledge** makes “program guy” **Jr/Sr returns** common, fixing the instant-exit feel.

**Pro days** and **draft stock** unchanged ([`offseason.md`](offseason.md#pro-days)); loyalty affects **whether they enter the pool**, not the sim of their stock once declared.

---

## UI & data

| Surface | Content |
|---------|---------|
| **Player card Overview** | **Loyalty: 72**; **Promise:** Starter minutes — status **Kept** / **At risk** / **Broken** |
| **Ratings tab** | **Loyalty** row (0–99) |
| **Recruit sheet** | Promise selector at offer; tendency band if unscouted |
| **Offseason Week 1** | Declare list shows **loyalty** + promise status in convince UI |
| **Save schema** | `loyalty`, `promise` `{ type, params, status, lockedAtSeason }` — **singular**; `promiseHistory[]` optional audit log per season — implementation phase |

Tuning: `balance-2026.json` → `player.loyalty` (declare weights, break thresholds) when loaders ship.

---

## Cross-links

| Doc | Hook |
|-----|------|
| [`recruiting-engine.md`](recruiting-engine.md) | Offer + promise at commit |
| [`player-cards.md`](player-cards.md) | Loyalty display |
| [`offseason.md`](offseason.md) | Retention & declare buckets |
| [`player-archetypes.md`](player-archetypes.md) | Personality → loyalty seed |
| [`simulation-engine.md`](simulation-engine.md) | Minutes stats feed promise checks |

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-09 | Initial loyalty attribute + promises; anti auto-declare for elites |
| 2026-11-09 | One promise per tenure; career-long lock; no retention re-pitch |
