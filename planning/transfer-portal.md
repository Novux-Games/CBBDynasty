# Transfer portal

**Context:** **One** national cycle per year — **Offseason Week 1** entries → **Weeks 2–5** recruit/sign in the same **Recruiting** hub as HS/JUCO ([`recruiting-engine.md`](recruiting-engine.md)). **No in-season** portal entries.  
**Companion:** [`offseason.md`](offseason.md#week-1--players-leaving), [`player-loyalty-promises.md`](player-loyalty-promises.md), [`game-design.md`](game-design.md#roster-eligibility--redshirt).  
**Last updated:** 2026-11-09

---

## Contents

1. [Calendar](#calendar)
2. [Who enters the portal](#who-enters-the-portal)
3. [Pool scale](#pool-scale)
4. [Eligibility & roster law](#eligibility--roster-law)
5. [Player presentation](#player-presentation)
6. [Economy (shared with HS/JUCO)](#economy-shared-with-hsjuco)
7. [Retention vs portal tab](#retention-vs-portal-tab)
8. [UI — Week 1 departures](#ui--week-1-departures)
9. [AI](#ai)
10. [Open — UX (discussion)](#open--ux-discussion)

---

## Calendar

| Rule | Detail |
|------|--------|
| **Cycles per year** | **One** |
| **In-season** | **No** portal entries or portal sub-tab |
| **Week 1** | **Entries** — national pool forms after retention / departures resolve ([`offseason.md`](offseason.md)) |
| **Weeks 2–5** | **Recruit & sign** — **Transfer Portal** sub-tab live in Recruiting hub (signing crunch **R1–R4**) |
| **After Week 5** | Portal window closed; signed transfers attach to next season roster path |

---

## Who enters the portal

Players who **enter the national portal** this cycle (not visible in the portal tab until they have **entered**):

| Driver | Detail |
|--------|--------|
| **Failed retention** | Week 1 **convince to stay** failed → **enters** portal |
| **Broken promise** | [`player-loyalty-promises.md`](player-loyalty-promises.md) evaluation → portal intent |
| **User cut / encourage out** | During **Week 1 transfer window** only — coach initiates departure → portal entry ([UI](#ui--week-1-departures)) |
| **AI / league sim** | Minutes, role, coaching change, broken trust, etc. — **not shown to player as “reason”** on portal card |

**Grad “transfers”:** **Not a feature** — players who **graduate** exit the roster; they do not use the portal.

---

## Pool scale

| Rule | Detail |
|------|--------|
| **National entrants** | **Uniform random integer 1,000 – 1,400** per season |
| **Per-team feel** | ~**3–4** entrants per program on average (league-wide sim; user school included) |
| **Stars** | Percentile bands **inside the portal pool only** ([`recruiting-engine.md`](recruiting-engine.md#star-distribution)) — independent of HS/JUCO counts |

---

## Eligibility & roster law

**Ruleset era:** **2026** ([`game-design.md`](game-design.md)).

| Rule | Detail |
|------|--------|
| **Immediate eligibility** | Portal signees are **eligible next season** — no sit-out year |
| **Fifth year** | **Fifth year of eligibility** supported where player record allows (redshirt / prior years used) — track on player card ([`game-design.md`](game-design.md#class--eligibility)) |
| **Scholarships** | **15** max per team ([`game-design.md`](game-design.md#hard-limits)) — departures in Week 1 **open slots** for Weeks 2–5 signings |
| **Roster cap** | **15** players including walk-ons |

---

## Player presentation

| Element | Rule |
|---------|------|
| **Ratings** | **Recruit-style letter grades** until scouted deeper — same pipeline as HS/JUCO ([`recruiting-engine.md`](recruiting-engine.md#hidden-ratings--letter-grades)) |
| **Identity** | Show **prior school** + **class** (Fr/So/Jr/Sr/RS) |
| **Reason** | **Do not show** why they entered (no “minutes” / “promise” chip on portal list) |
| **Portal Top 40** | National order **inside portal sub-tab** — **static** once published for the cycle (same spirit as HS **Top 60** fixed at seed) |
| **Recommended tab** | Default sort for **user** portal recruiting — [`recruiting-engine.md`](recruiting-engine.md#recommended-tab-default) |

---

## Economy (shared with HS/JUCO)

**Design lock:** **One budget** for the user — **weekly recruiting points** + **annual NIL $** fund **both** HS/JUCO and portal spend in Weeks **2–5** (and year-long HS/JUCO cycle elsewhere per [`recruiting-engine.md`](recruiting-engine.md)).

| Event | Effect |
|-------|--------|
| **Week 1 departure finalized** | Scholarship **slot** opens; that player’s **committed NIL** returns to **unallocated** collective pool for remaining offseason spend |
| **Week 1 departure** | **Recruiting points** — weekly **tier pool** unchanged; freed **slots + NIL** are what expand signing capacity (see balance tune for optional departure grants) |
| **Portal sign** | Same actions: scout, call, offer, **one career promise** at this school, NIL allocation |

---

## Retention vs portal tab

| Outcome | Portal tab |
|---------|------------|
| **Convince to stay** succeeds | Player **never entered** portal — **does not** appear on Transfer Portal sub-tab |
| **Convince fails** or **cut / AI entry** | Player **in** national pool → visible on sub-tab Weeks **2–5** |

---

## UI — Week 1 departures

**Shell:** Offseason Week 1 **Players Leaving** flow ([`offseason.md`](offseason.md)).

**Design lock — `Roster` tab** on the graduation/transfers layout:

| Tab (examples) | Role |
|----------------|------|
| **Retention** | Convince **portal-intent** / unhappy players to **stay** |
| **Roster** (new) | Everyone **departing** this week (grads, draft, **cuts**, portal-bound) — actions to **encourage transfer** / confirm cut to portal where allowed |

Graduates and draft exits listed for clarity; only **portal-eligible** rows get **cut / encourage out** during the window.

---

## AI

Same as [`recruiting-engine.md` → AI recruiting`](recruiting-engine.md#ai-recruiting): each school spends off-screen via **Recommended**; **no rival portal boards** visible. User learns via **commits** (see [Open — UX](#open--ux-discussion) for News scope).

---

## Open — UX (discussion)

**Not locked** — product discussion with Austin:

| Topic | Question |
|-------|----------|
| **News** | National **headline tracker** for portal (league-wide commits) vs **hub-only** + shallow dashboard News? |
| **Dashboard** | **Optional tasks** for portal during Weeks 2–5 (same pattern as in-season recruiting) or offseason-only surfacing? |

---

## Cross-links

| Doc | Hook |
|-----|------|
| [`game-frame.md`](game-frame.md) | Recruiting hub, user-only boards |
| [`recruiting-engine.md`](recruiting-engine.md) | Sub-tab, spend actions, Top 40 |
| [`offseason.md`](offseason.md) | Week 1–5 calendar |

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-09 | Initial spec from Austin answers (calendar, scale, eligibility, economy, UI tab) |
| 2026-11-09 | UX News/dashboard left open for discussion |
