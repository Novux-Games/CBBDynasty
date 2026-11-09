# Dashboard tasks — master index

**Purpose:** Single catalog of **Required** vs **Optional** dashboard tasks across the dynasty. **Gate rules** (advance blocked, sim-week confirmation) live in [`game-frame.md`](game-frame.md#dashboard-task-gate) and [`game-frame.md`](game-frame.md#advance-week-confirmation-sim-week). Domain docs keep **mechanics**; this doc is the **checklist index**.

**Shell:** [`game-frame.md`](game-frame.md). **Last updated:** 2026-11-09

---

## Contents

1. [Rules (all phases)](#rules-all-phases)
2. [Coaching Carousel](#coaching-carousel)
3. [Offseason](#offseason)
4. [End of Season](#end-of-season)
5. [Preseason](#preseason)
6. [In-season (exhibitions → postseason)](#in-season-exhibitions--postseason)
7. [Detail by domain](#detail-by-domain)
8. [Document history](#document-history)

---

## Rules (all phases)

| Kind | Advance week |
|------|----------------|
| **Required** | **Disabled** until complete |
| **Optional** | Visible; advance allowed — unresolved items roll into [`sim-week confirmation`](game-frame.md#advance-week-confirmation-sim-week) |

| Pattern | Detail |
|---------|--------|
| **Task card** | Checklist row on **Dashboard**; deep link to hub/screen |
| **Not dashboard** | Full-screen phase flows (e.g. Offseason **Week 1** **Roster / Transfer** tabs) — [`transfer-portal.md`](transfer-portal.md#week-1--roster-vs-transfer-tabs) |
| **Notifications** | Headlines / alerts; **tasks** carry the **action** entry |
| **Complete** | User finishes the wired action or explicit dismiss where allowed |
| **AI on advance** | Unplayed games fast-sim; user-school recruiting/portal per automation — [`recruiting-engine.md`](recruiting-engine.md#ai-recruiting) |

---

## Coaching Carousel

**When:** Offseason entry **Season 2+** ([`offseason.md`](offseason.md#coaching-carousel)). **Not** a numbered week in UI.

| Kind | Task (examples) | Blocks advance? |
|------|-----------------|-----------------|
| **Required** | Pending **head coach offer** accept/decline/negotiate | Yes — carousel **paused** |
| **Required** | **Contract extension** renegotiate or walk | Yes, when offered |
| **Required** | **Assistant hire** approvals; defend vs poach when triggered | Yes, when queued |
| **Optional** | **Sim carousel** (background league churn) | No — user may sim then resolve cards |

---

## Offseason

| Week | Required (dashboard) | Optional (dashboard) |
|------|----------------------|----------------------|
| **1 — Players Leaving** | Resolve **mandatory** declare/grad flows as engine queues them; **Pro days** for draft-bound when sheet open | — |
| **1 — layout** | **Roster / Transfer** screen — **not** duplicated as optional cards | Encourage transfer / convince stay on dedicated tabs only |
| **2–5 — Recruiting & Portal** | None | **Portal optional tasks** — [table](transfer-portal.md#ux--dashboard-optional-tasks-portal-only) |
| **2–5 — HS/JUCO** | None (year-long spend; never blocks advance) | **Recruiting optional tasks** — [triggers](recruiting-engine.md#in-season-dashboard-tasks) |
| **6 — Signing Day** | **View Recruiting Rankings** · **Change positions** on incoming class | — |
| **7 — Summer Workouts** | None | **Review workout report** (sheet) |
| **8 — Cuts & Roster Lock** | **Roster ≤ 15** scholarships before advance | Walk-on tryouts if surfaced |
| **9 — Fundraising** | None unless upgrade purchase is framed as required (tune in UI) | Fundraising events, facility upgrades, naming |
| **10 — Conference Offers** | **Season 2+:** respond to **realignment offer cards** | — |
| **Season 1** | Week 10 **skipped** | — |

---

## End of Season

| Kind | Task | Blocks advance? |
|------|------|-----------------|
| **Required** | **Season review** acknowledged → **Enter offseason** ([`season-review.md`](season-review.md#advance)) | Yes |

No recruiting, games, or schedule edits this week.

---

## Preseason

**Single week** — **8 required** tasks; **Advance / Start season** disabled until all complete.

**Authoritative checklist:** [`preseason.md`](preseason.md#task-checklist) (Incoming Class Seed through Schedule RS).

**Optional:** None on dashboard gate (redshirt picks live inside Workouts / roster review).

---

## In-season (exhibitions → postseason)

| Kind | Task (examples) | When |
|------|-----------------|------|
| **Optional** | **Play / Sim vs {opponent}** | User game scheduled this week (including exhibitions, RS, conf tourney, March) |
| **Optional** | **Spend recruiting points** ({N} available) | Year-long cycle open + drip unspent |
| **Optional** | Watchlist / Top 60 / rival-commit nudges | Triggers in [`recruiting-engine.md`](recruiting-engine.md#in-season-dashboard-tasks) |
| **Optional** | **Coach skill points** (Sunday) | Sunday in RS when points granted ([`game-design.md`](game-design.md)) |
| **Optional** | **Injury** follow-up (lineup, medical redshirt edge) | When injury alert surfaces |
| **Optional** | **Strategy / rotation** edits | Before sim when prep incomplete (product may nudge) |
| **Required** | — | **Bye week:** none. **User game week:** games are **optional**, not required ([`game-frame.md`](game-frame.md#dashboard-task-gate)) |

**Portal:** No in-season portal tasks ([`transfer-portal.md`](transfer-portal.md)).

---

## Detail by domain

| Domain | Required tasks | Optional tasks |
|--------|----------------|----------------|
| **Games / sim** | — | [`game-frame.md`](game-frame.md#dashboard-task-gate) |
| **HS/JUCO recruiting** | Preseason seed; OS W5 sign lock (phase) | [`recruiting-engine.md`](recruiting-engine.md#in-season-dashboard-tasks) |
| **Transfer portal** | Week 1 tabs (screen) | OS W2–5 [`transfer-portal.md`](transfer-portal.md#ux--dashboard-optional-tasks-portal-only) |
| **Coach** | Carousel staffing cards | Sunday points, upgrades in preseason |
| **News** | — | Headlines on **News** tab only — not tasks ([`news-hub.md`](news-hub.md#surfaces)) |

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-09 | Master index — consolidates game-frame gate + domain task tables |
