# In-app notifications (Dashboard feed)

**Context:** Alerts are **in-app rows** on the **Dashboard notification feed** ([`game-frame.md`](game-frame.md#notifications)) — **not** OS push. Players run a text-first dynasty in-session; tap a row → deep link.

**Not the same as:**

| Surface | Role |
|---------|------|
| **Dashboard tasks** | Required / optional **checklist cards** with explicit actions — [`dashboard-tasks.md`](dashboard-tasks.md) |
| **This feed** | Informational **alerts** about what changed (injury, commit, carousel card, etc.) |
| **News → Stories** | League **editorial** headlines — [`news-hub.md`](news-hub.md). **Never** duplicated as notification feed rows |

**Portal (lock):** Offseason only — no in-season transfer alerts — [`transfer-portal.md`](transfer-portal.md).

**Last updated:** 2026-11-09 · **Status:** Design lock (in-app only).

---

## Contents

1. [Design locks](#design-locks)
2. [Defaults & settings](#defaults--settings)
3. [Event catalog](#event-catalog)
4. [Tasks vs feed (no triple spam)](#tasks-vs-feed-no-triple-spam)
5. [Week advance & sim-week](#week-advance--sim-week)
6. [Deep links](#deep-links)
7. [Excluded](#excluded)
8. [Design locks (product decisions)](#design-locks-product-decisions)
9. [Cross-links](#cross-links)

---

## Design locks

| Rule | Detail |
|------|--------|
| **In-app only** | **No** Android tray, lock screen, or background push |
| **Default on** | New dynasty: notification **feed visible** on Dashboard (per-category mute in Settings may ship later) |
| **Stories ≠ notifications** | Commits, upsets, league injuries, bracketology → **News** only |
| **User-school focus** | Feed emphasizes **your program**; not a dump of 365-team league churn |
| **Portal** | Offseason Weeks 1–5 events only |
| **Tasks** | Separate UI; see [duplication rules](#tasks-vs-feed-no-triple-spam) |

---

## Defaults & settings

| Setting | Default |
|---------|---------|
| Show notification feed on Dashboard | **On** |
| OS push | **Off** / not implemented |

---

## Event catalog

**Priority** = sort weight in feed (newest first within tier). All rows are **in-app** only.

### High (top of feed)

| Event | Feed row | Task also? | Tap → |
|-------|----------|------------|-------|
| User player **injury** | Yes | Optional **Review injury** | Strategy / player |
| **Team discipline** sheet opened | Yes | **Required** parallel task | Discipline UI |
| **Commit** to user school (**HS, JUCO, or portal**) | Yes — **every** sign | Optional recruiting nudge | Recruiting → recruit / Portal tab |
| **Decommit** from user school | Yes | — | Recruiting |
| Carousel: **offer / fired / must-sign extension** | Yes | **Required** task when gated | Coach → Contract |
| **Selection Sunday** (user in field) | Yes | — | March Mayhem / preview |
| **Fired** or **national postseason eliminated** | Feed row + **blocking modal** on next Dashboard open — acknowledge before other UI | Required carousel / career tasks as applicable | Coach / career flow |

### Medium

| Event | Feed row | Task also? | Tap → |
|-------|----------|------------|-------|
| **Sunday coach skill points** | Yes | Optional dashboard nudge | Coach card |
| **Game this week** | Yes | Optional **Play / Sim** | Game preview |
| **Medical redshirt** eligible | Yes | Optional **Medical RS?** | Player / postgame |
| **Signing Day** (unsigned class) | Yes | Required if hard gate | Signing Day |
| **Signing Day** complete recap | Yes | — | Roster / recruiting |

**Game this week:** Plain copy e.g. *“Week 14 — at Kentucky”* when the user has a scheduled game that week (not a phone alert).

### Low

| Event | Feed row | Tap → |
|-------|----------|-------|
| Fundraising / facility complete | Yes | School / offseason |
| Large NET / poll move (**user team**) | Yes | Rankings |

### Major national records (league)

| Event | Feed row | Tap → |
|-------|----------|-------|
| **Major national record** | Yes — criteria in [`history-hub.md`](history-hub.md#major-record-feed-eligible) | Player card / History |

### News only (no feed row)

| Event | Where |
|-------|--------|
| League injuries, upsets, portal steals elsewhere | **News → Stories** |
| Top 60 / portal board updates | **News hub** |
| Bracketology chatter | **News** |
| Routine national record (marginal #1) | **News** only |

---

## Tasks vs feed (no triple spam)

| Case | Rule |
|------|------|
| Optional **task** + same event | **Both** allowed — task = action chip; feed = one-line **alert** (different copy) |
| **Required** task | Feed row **optional**; required card is enough — prefer **feed skip** if redundant |
| Injury | Feed always for user player; task optional |

---

## Week advance & sim-week

After **Advance week** (or sim-week confirm), append feed rows for **user-relevant** outcomes that happened during resolution:

| Behavior | Lock |
|----------|------|
| **One row per event** | e.g. separate lines for injury + commit — **not** one merged paragraph |
| **League sim noise** | **No** feed rows — goes to News batch if headline-worthy |
| **AI-only carousel** | **No** feed unless user HC affected |
| Sim-week summary dialog | Confirm dialog lists sim results; feed persists same facts after dismiss |

---

## Deep links

Tap feed row → navigate with Dashboard under back stack:

| Destination | Examples |
|-------------|----------|
| Game preview | Game this week, recap |
| Strategy | Injury, lineup |
| Recruiting hub | Commit, portal |
| Coach card | Points, contract |
| Signing Day | Unsigned class |
| March Mayhem | Selection Sunday |

---

## Excluded

| Item | Notes |
|------|--------|
| **OS push** | Product uses in-app feed only |
| In-season portal | Design lock |
| Stories in notification feed | Design lock |
| Per-possession watch events | Live sim UI |

---

## Design locks (product decisions)

| Decision | Lock |
|----------|------|
| **Commits** | **Every** sign to the user program (**HS, JUCO, portal**) → feed row |
| **Fired / eliminated** | **Option B:** **blocking modal** on first Dashboard open after the event; persists until dismissed; feed row also logged for history |
| **Major national record** | Feed row + News — [`history-hub.md`](history-hub.md) |

Modal copy is short; primary CTA routes to the same destination as the parallel **required** task (carousel / career / postseason) when one exists.

---

## Cross-links

| Doc | Hook |
|-----|------|
| [`game-frame.md`](game-frame.md#notifications) | Feed placement |
| [`dashboard-tasks.md`](dashboard-tasks.md) | Task cards |
| [`news-hub.md`](news-hub.md) | Stories |
| [`injuries.md`](injuries.md) | Injury alert + task |
| [`recruiting-engine.md`](recruiting-engine.md) | Commits, Signing Day |
| [`transfer-portal.md`](transfer-portal.md) | Offseason portal |
| [`coach-card.md`](coach-card.md) | Contract, Sunday points |
| [`game-design.md`](game-design.md#mobile-ux-automation--notifications) | Automation toggles (unchanged) |

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-09 | Initial draft (incorrectly included OS push) |
| 2026-11-09 | **In-app only**; no push; Stories separate; defaults on |
| 2026-11-09 | All user-school commits (HS/JUCO/portal) → feed row |
| 2026-11-09 | Fired / postseason eliminated → blocking modal + feed row |
| 2026-11-09 | Major national records → feed + News |
