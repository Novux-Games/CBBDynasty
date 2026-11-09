# Transfer portal

**Context:** **One** national cycle per year — **Offseason Week 1** entries → **Weeks 2–5** recruit/sign in the **Recruiting** hub ([`recruiting-engine.md`](recruiting-engine.md)). **No in-season** entries.  
**Companion:** [`offseason.md`](offseason.md#week-1--players-leaving), [`player-loyalty-promises.md`](player-loyalty-promises.md), [`game-design.md`](game-design.md#class--eligibility).  
**Last updated:** 2026-11-09

---

## Contents

1. [Calendar](#calendar)
2. [Week 1 — Roster vs Transfer tabs](#week-1--roster-vs-transfer-tabs)
3. [Who enters the portal](#who-enters-the-portal)
4. [Pool scale](#pool-scale)
5. [Eligibility & roster law (2026)](#eligibility--roster-law-2026)
6. [Player presentation](#player-presentation)
7. [Economy (shared with HS/JUCO)](#economy-shared-with-hsjuco)
8. [National recruiting (Weeks 2–5)](#national-recruiting-weeks-25)
9. [AI entry drivers](#ai-entry-drivers)
10. [AI spend](#ai-spend)
11. [UX — Hub-first (design lock)](#ux--hub-first-design-lock)
12. [UX — Dashboard optional tasks (portal only)](#ux--dashboard-optional-tasks-portal-only)

---

## Calendar

| Rule | Detail |
|------|--------|
| **Cycles per year** | **One** |
| **In-season** | **No** portal entries or portal recruiting |
| **Week 1** | Entries, **Roster** / **Transfer** tabs ([below](#week-1--roster-vs-transfer-tabs)) |
| **Weeks 2–5** | National **Transfer Portal** sub-tab in Recruiting hub |
| **After Week 5** | Window closed; signed transfers attach for next season |

---

## Week 1 — Roster vs Transfer tabs

**Shell:** Offseason Week 1 **Players Leaving** ([`offseason.md`](offseason.md)).

| Tab | Who appears | Player actions |
|-----|-------------|----------------|
| **Roster** | Your roster **minus anyone in the portal** — only players **not** currently portal entrants (scholarship + walk-ons within cap) | Review; **encourage transfer** — target must **not** already be on Transfer tab |
| **Transfer** | Only players who have **entered the portal** (your entrants in Week 1; national pool Weeks 2–5 in Recruiting hub) | **Convince to stay** on **your** entrants — success → off Transfer tab, **back on Roster** |

**Mutual exclusion:** A player appears on **exactly one** of these tabs in Week 1 — **never both**. Portal entry → **removed from Roster tab**; convince-to-stay → **removed from Transfer tab**, **restored to Roster tab**.

**Encourage transfer (Roster tab):** Confirm → **enters portal** → **only** on Transfer tab.

**Convince to stay (Transfer tab):** Success → **only** on Roster tab. Failure → stays on Transfer tab through Weeks 2–5 if unsigned.

Graduates, draft exits, and completed eligibility are handled on **Roster** / declare flows — they **do not** use the portal ([grad rule](#who-enters-the-portal)).

---

## Who enters the portal

| Path | Detail |
|------|--------|
| **Coach encourage (Roster tab)** | Week 1 only — not on roster once entered |
| **Failed convince to stay** | Was on Transfer tab; remains in portal |
| **Broken promise / retention fail** | Enters portal (off roster when entered) |
| **League sim** | See [AI entry drivers](#ai-entry-drivers) |

**Grad transfers:** **Not modeled** — graduates exit; no separate grad-portal path.

---

## Pool scale

| Stage | Rule |
|-------|------|
| **Considering pool** | **Uniform random integer 1,000 – 1,400** per season nationwide — players **evaluating** portal entry (intent), not all will enter |
| **Final entrants** | **Simulation outcome** — typical **~3–4 entrants per team** average; no fixed national total |
| **Stars** | Assigned on **final entrant pool** only ([`recruiting-engine.md`](recruiting-engine.md#star-distribution)) |

---

## Eligibility & roster law (2026)

**Ruleset label:** **2026** — align with NCAA Division I direction in [`game-design.md`](game-design.md) (`calendar` in balance bundle).

| Rule | Real-world basis (simplified for sim) |
|------|--------------------------------------|
| **Transfer eligibility** | **Immediate eligibility** at the new school after portal entry and sign (no mandatory sit-out year in this era) |
| **Five-year period** | Eligibility tied to a **continuous five-year window** from initial full-time enrollment / age-based clock start — not legacy “seasons of competition” counting ([NCAA eligibility-center age-based rules](https://www.ncaa.org/eligibility-center/division-i-and-division-ii-age-based-eligibility-rules/)) |
| **Player record** | Store **`eligibilityClockStart`** and **games/seasons used**; portal sign blocked if **no time left** in window |
| **Redshirt (game)** | **Zero-game season** still a **development** choice in dynasty UI; under 2026 rules the **eligibility clock does not pause** for a traditional “athletic redshirt” — tune copy in balance if we soften for fun |
| **Court carve-outs** | Real-world **2022-class litigation** / extra portal windows — **out of scope** for default sim |

| Roster law | Value |
|------------|--------|
| **Scholarships** | **15** max ([`game-design.md`](game-design.md#hard-limits)) |
| **Roster cap** | **15** including walk-ons |

---

## Player presentation

| Element | Rule |
|---------|------|
| **Ratings** | **Letter grades** until scouted ([`recruiting-engine.md`](recruiting-engine.md#hidden-ratings--letter-grades)) |
| **Identity** | **Prior school** + **class** |
| **Reason** | **Hidden** — no UI reason chip |
| **Portal Top 40** | National rank list in portal sub-tab — **locked at start of Offseason Week 2** among **final entrants** |
| **Recommended** | Default sort in hub ([`recruiting-engine.md`](recruiting-engine.md#recommended-tab-default)) |

---

## Economy (shared with HS/JUCO)

**One budget:** same **weekly recruiting points** tier + **annual NIL $** for HS/JUCO and portal (Weeks 2–5 portal + year-long HS/JUCO per recruiting doc).

| Week 1 departure (portal entry or grad/draft clear) | Effect |
|-----------------------------------------------------|--------|
| **Scholarship** | **Slot opens** toward **15** cap |
| **NIL** | Player’s committed deal **returns to unallocated** collective pool |
| **Recruiting points** | **No** extra weekly drip or per-departure point grant |

---

## National recruiting (Weeks 2–5)

**Transfer Portal** sub-tab: only players who **entered** the portal (not the full 1k–1.4k considering pool).

Same spend/promise rules as HS/JUCO; **Portal Top 40** visible **Week 2+**.

Portal targets use the same [interest model](recruiting-engine.md#interest--offers) as HS/JUCO (`baselineInterest` at zero spend vs the live meter). Whether an entrant’s `topPriorities` re-roll or carry is [open](recruiting-engine.md#open-questions).

---

## AI entry drivers

League sim promotes players from **considering** → **entrant** using weights (tunable in balance):

| Driver | Notes |
|--------|--------|
| **Broken promise** | From loyalty doc |
| **Low minutes vs role** | Roster analytics |
| **Coaching carousel** | New HC at school |
| **Losing season** | NET / record band |
| **Prestige mismatch** | High-rated player, low-tier minutes |
| **Random volatility** | Small noise so pool isn’t deterministic |

User sees **outcomes** only (who entered), not internal reason.

---

## AI spend

[`recruiting-engine.md` → AI recruiting`](recruiting-engine.md#ai-recruiting) — Recommended ordering, hidden boards. League outcomes primarily via **Recruiting hub**; [`news-hub.md`](news-hub.md) for optional league read-only context.

---

## UX — Hub-first (design lock)

| Rule | Detail |
|------|--------|
| **Act (gameplay)** | **Recruiting → Transfer Portal** — spend, Recommended, board, commits |
| **Read — dashboard** | **News** bottom tab — **headlines only** (top **N**); portal stories included when recent ([`news-hub.md`](news-hub.md#surfaces)) |
| **Read — depth** | **News hub** — full Stories + **Portal** tab (Top 40, trackers) Weeks 1–5; same headline pool, more UI |

## UX — Dashboard optional tasks (portal only)

**Index:** [`dashboard-tasks.md`](dashboard-tasks.md#offseason) (all phases). **Scope here:** **Offseason Weeks 2–5** only. **Optional** — never block **Advance week** ([`game-frame.md`](game-frame.md#dashboard-task-gate)). **HS/JUCO** nudges: [`recruiting-engine.md`](recruiting-engine.md#in-season-dashboard-tasks).

| Trigger | Task (examples) | Deep link |
|---------|------------------|-----------|
| **Portal window** | “Transfer Portal — Offseason Week {2–5}” (dismissable) | Recruiting → **Portal** → **Recommended** |
| **Week 2 lock** | “Portal Top 40 published” (ack / dismiss) | News hub **Portal** tab or Portal **Recommended** |
| **Pipeline entrant** | “{Name} in portal ({prior school})” | Portal profile → **Add to board** |
| **Open scholarships** | After Week 1 losses: “{K} slots open” when &lt; 15 and spend still available | Portal **Recommended** |
| **Signing crunch** | “Signing week R{#} — 1.5× points” (flavor) | Portal hub |
| **Watchlist nudge** | Rival commit / decommit on **your** portal board target | Portal sheet for that player |

**Week 1:** **Roster / Transfer** tabs on Players Leaving — **not** duplicated as dashboard optional cards.

**Sim-week / advance:** Unfinished optional portal nudges → confirm line + **AI user-school** portal spend on **Recommended** (same family as [`game-frame.md`](game-frame.md#advance-week-confirmation-sim-week) — portal slice only).

---

## Cross-links

| Doc | Hook |
|-----|------|
| [`offseason.md`](offseason.md) | Week 1 layout |
| [`recruiting-engine.md`](recruiting-engine.md#interest--offers) | Sub-tab, actions, same interest weights |

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-09 | Initial spec |
| 2026-11-09 | Roster/Transfer tabs; considering vs entrant pool; 2026 eligibility; Top 40 @ Week 2 |
| 2026-11-09 | Roster tab excludes portal entrants; mutual exclusion between tabs |
| 2026-11-09 | Hub-first portal UX; news-hub supplementary |
| 2026-11-09 | Portal-only optional dashboard tasks Weeks 2–5 |
| 2026-11-09 | Pointer to recruit interest model; portal priority re-roll stays open |
