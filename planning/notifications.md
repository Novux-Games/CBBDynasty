# Notifications & alerts

**Context:** Android push + in-app **notification feed** on the Dashboard. **Tasks** (required/optional cards) are the **action** entry; notifications **surface** events and deep-link — [`dashboard-tasks.md`](dashboard-tasks.md). **News** is editorial/headline product — [`news-hub.md`](news-hub.md).

**Portal calendar (lock):** Transfers **only** in offseason — Week 1 entry, Weeks 2–5 portal recruiting — [`transfer-portal.md`](transfer-portal.md). **No** in-season portal notifications.

**Last updated:** 2026-11-09 · **Status:** Draft — product decisions below.

---

## Contents

1. [Two surfaces](#two-surfaces)
2. [Design locks (already settled)](#design-locks-already-settled)
3. [Event catalog (draft)](#event-catalog-draft)
4. [Deep links](#deep-links)
5. [Automation interaction](#automation-interaction)
6. [Open decisions — need from you](#open-decisions--need-from-you)
7. [Cross-links](#cross-links)

---

## Two surfaces

| Surface | Role |
|---------|------|
| **In-app feed** | Scrollable list on **Dashboard** ([`game-frame.md`](game-frame.md#notifications)); persists until dismissed or superseded |
| **Android system notification** | OS tray + lock screen; tap opens app at deep link |

**Principle:** High-signal events get **both** (configurable). Low-signal may be **feed-only**. **Dashboard optional tasks** duplicate some events with explicit CTAs — user may complete via task **or** feed tap (same destination).

---

## Design locks (already settled)

| Rule | Source |
|------|--------|
| Tasks carry **actions**; notifications **inform** + link | [`dashboard-tasks.md`](dashboard-tasks.md) |
| Recruiting commits / headlines also in News | [`news-hub.md`](news-hub.md) |
| User player injury → notify + optional **Review injury** task | [`injuries.md`](injuries.md#news--league) |
| **Sunday** coach skill points → notification (medium) | [`game-design.md`](game-design.md#notifications-android) |
| Coaching carousel / contract / firing → notify; **pause** until resolved | [`offseason.md`](offseason.md), [`coach-card.md`](coach-card.md) |
| Signing Day week 6 → offseason notification after | [`recruiting-engine.md`](recruiting-engine.md) |
| Selection Sunday → high priority | [`game-design.md`](game-design.md) |
| **No** in-season portal / transfer alerts | [`transfer-portal.md`](transfer-portal.md) |

---

## Event catalog (draft)

Priority tiers from [`game-design.md`](game-design.md#notifications-android) unless overridden below.

### High

| Event | Feed | Push (default TBD) | Task parallel? | Deep link |
|-------|------|--------------------|----------------|-----------|
| HS/JUCO **commit** to user school | Yes | TBD | Optional spend nudge | Recruiting hub → recruit |
| HS/JUCO **decommit** from user school | Yes | TBD | — | Recruiting hub |
| **Portal** transfer **signed** (user school) | Yes | TBD | OS W2–5 optional tasks | Recruiting → Portal |
| **Job offer** / **fired** / extension critical | Yes | TBD | Required carousel cards | Coach card → Contract |
| **Selection Sunday** field revealed | Yes | TBD | — | News / March Mayhem |
| User player **injury** (any severity) | Yes | TBD | Optional **Review injury** | Strategy / player card |

### Medium

| Event | Feed | Push (default TBD) | Task parallel? | Deep link |
|-------|------|--------------------|----------------|-----------|
| **Sunday coach skill points** available | Yes | TBD | Optional dashboard nudge | Coach card |
| **Game day** tip (user has game this week) | Yes | Opt-in | Optional **Play / Sim** | Game preview |
| Rival **portal flip** (user was recruiting) | Yes | TBD | — | Recruiting / News |
| **Medical redshirt** eligibility | Yes | TBD | Optional **Medical RS?** | Postgame / player card |
| **Signing Day** (week 6) recap | Yes | TBD | Required if unsigned | Signing Day UI |

### Low

| Event | Feed | Push (default TBD) | Deep link |
|-------|------|--------------------|-----------|
| Fundraising / facility upgrade complete | Yes | Feed-only | School / offseason week 9 |
| Poll / NET big mover (user team) | Yes | Feed-only | Rankings |
| League injury headline (not user player) | Yes | Feed-only | News → Stories |

### Explicitly out of scope (v1)

| Event | Why |
|-------|-----|
| In-season **portal entry** or midyear transfer | No in-season portal — design lock |
| Per-possession sim events | Watch mode is live UI |

---

## Deep links

| Destination | From |
|-------------|------|
| Game preview | Game day, last game recap |
| Strategy hub | Injury review, lineup incomplete |
| Recruiting hub (Recommended) | Commit, visit refund, portal |
| Coach card (Contract / skills) | Carousel, Sunday points |
| News tab / News hub | League headlines, Selection Sunday |
| Postseason / March Mayhem | Bracket round, upset |

**Cold start:** Tap push → load dynasty → navigate with back stack to Dashboard under target screen.

---

## Automation interaction

When **sim-week** or **auto-maintain recruiting** runs ([`game-frame.md`](game-frame.md#advance-week-confirmation-sim-week)):

| Question | Draft behavior (confirm) |
|----------|-------------------------|
| Commits during auto-sim | Batch into **one** feed summary per advance vs one line per commit? |
| Suppress push if user confirmed advance? | TBD |
| Injury on user player in auto-sim | Always feed + optional push; task still optional |

---

## Open decisions — need from you

Answer these to move this doc to **Done** and update [`planning-notes.md`](planning-notes.md) + [`BUILD_CHECKLIST.md`](../docs/BUILD_CHECKLIST.md).

### A. Push vs feed defaults

1. **Global default:** Are **push notifications on** for new dynasties, or **opt-in** until the user enables in Settings?
2. **Per-category toggles:** Match [`game-design.md`](game-design.md) table exactly, or add **Injuries**, **Recruiting**, **Carousel**, **Game day** channel groups?
3. **Game day:** Confirm **opt-in only** (as game-design says) — default off?

### B. Duplication with tasks & news

4. When an **optional dashboard task** exists (e.g. injury review), do we still **push**, or **feed-only** to avoid triple alert (task + feed + push)?
5. Do **News headlines** also create feed rows, or News tab only (no Dashboard feed duplicate)?

### C. Timing & batching

6. **Week advance:** Single digest notification (“Week 12 sim complete — 2 injuries, 1 commit”) vs separate lines?
7. **Quiet hours / DND:** Respect Android system only, or in-app “no pushes 11pm–8am” default?

### D. Priority events

8. **Injuries:** Push for **all** user injuries, or **moderate/major only** (minors feed-only)?
9. **Portal:** Notify on **every** user signing during Weeks 2–5, or only **5★ / Top 40** targets?
10. **Carousel:** Push on **every** AI league coaching change, or **user-involved only**?

### E. Copy & identity

11. Notification title pattern: **“{School nick}”** vs **“Dynasty”** generic prefix?
12. Should catastrophic events (fired, eliminated from NCBA) use **full-screen takeover** on next open, or feed-only?

---

## Cross-links

| Doc | Hook |
|-----|------|
| [`game-design.md`](game-design.md#mobile-ux-automation--notifications) | Legacy priority table (superseded by this doc when Done) |
| [`game-frame.md`](game-frame.md) | Dashboard feed placement |
| [`dashboard-tasks.md`](dashboard-tasks.md) | Task vs notify boundary |
| [`injuries.md`](injuries.md) | Injury + medical RS |
| [`recruiting-engine.md`](recruiting-engine.md) | Commits, Signing Day |
| [`transfer-portal.md`](transfer-portal.md) | Offseason-only portal |
| [`coach-card.md`](coach-card.md) | Contract / Sunday points |
| [`offseason.md`](offseason.md) | Carousel pause |

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-09 | Initial draft; portal in-season removed; event catalog + open questions |
