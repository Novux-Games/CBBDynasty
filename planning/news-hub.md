# News hub — product spec

**Context:** League narrative layer — **News-heavy** portal ([`transfer-portal.md`](transfer-portal.md#ux--news-design-lock)), HS commits, polls, bracketology, injuries. **Recruiting hub** stays the **action** surface; News is **awareness + drill-in**.  
**Shell:** [`game-frame.md`](game-frame.md) — dashboard **News** tab (shallow) + hamburger **News** hub (full).  
**Last updated:** 2026-11-09

---

## Contents

1. [Surfaces](#surfaces)
2. [Stories feed](#stories-feed)
3. [Portal tab (transfer window)](#portal-tab-transfer-window)
4. [Recruits tab](#recruits-tab)
5. [Draft tab](#draft-tab)
6. [Story detail & articles](#story-detail--articles)
7. [Drill-in & deep links](#drill-in--deep-links)
8. [Cadence & generation](#cadence--generation)
9. [What News does not show](#what-news-does-not-show)
10. [Cross-links](#cross-links)

---

## Surfaces

| Surface | Depth | Role |
|---------|-------|------|
| **Dashboard → News** (bottom nav) | **Shallow** — top **N** headlines (tunable, e.g. 8–12) | Ticker glance; portal + recruiting pins during window |
| **Hamburger → News hub** | **Full** — tabs below | Read, filter, trackers |

**Phase visibility**

| Phase | Portal tab in hub |
|-------|-------------------|
| **Offseason Week 1** | **Yes** — entry headlines, your departures |
| **Offseason Weeks 2–5** | **Yes** — peak tracker + Top 40 |
| **Elsewhere** | **Hidden** (Stories may still reference past portal as archive headlines in History) |

---

## Stories feed

**Default tab** in News hub. Reverse-chronological **headlines** + date chip; tap → [story detail](#story-detail--articles).

### Headline types (portal — News-heavy)

| Type | When | Example line |
|------|------|----------------|
| **Portal entry** | Player **enters** national pool | “{Player} enters transfer portal ({Prior School})” |
| **Portal commit** | Hard commit to a school | “{Player} commits to {School}” |
| **Portal decommit** | Soft flip before sign | “{Player} decommits from {School}” |
| **Portal flip** | Commit change | “{Player} flips from {A} to {B}” |
| **Notable entrant** | 5★ / Top-40 band enters | “Five-star {Player} in transfer portal” |

**Your school:** Same templates — user-initiated **encourage transfer** generates an **entry** headline like any other program.

### Headline types (recruiting — HS/JUCO)

| Type | When |
|------|------|
| **HS/JUCO commit** | Soft or hard commit (league-wide blue-chips emphasized) |
| **Top 60 story** | Optional spotlight on national recruit rank movement **at seed only** (list itself static) |

### Other headline types (existing / brief)

Upsets, NET/poll movement, injuries, bracketology (Feb), tournament, coaching carousel, awards — same feed, mixed sort by recency + priority weight (portal + user-school boosted during window).

---

## Portal tab (transfer window)

**Design lock:** National **read-only** portal product in News — **not** a rival recruiting board ([`recruiting-engine.md`](recruiting-engine.md#visibility-player)).

### Layout (sections)

| Section | Content |
|---------|---------|
| **Portal Top 40** | **Full `#1`–`#40`** — **locked at start of Offseason Week 2** ([`transfer-portal.md`](transfer-portal.md)); prior school + class; stars; **no entry reason** |
| **Recent commits** | Rolling list (e.g. last 25) — player, destination, stars |
| **Recent entrants** | Rolling list — player, prior school (entries in last X days/week) |
| **Filters** (optional row) | Conference of **prior school**, stars, position, **your pipeline states** (highlights entrants from home region) |

### Row interactions

| Tap | Destination |
|-----|----------------|
| Player name | **Read-only** portal profile sheet — letters as scouted nationally (default: scout accuracy = “media” band, not your spend depth) |
| School name | **School hub** (view-only) |
| **Add to board** (if offered) | Deep link → **Recruiting → Portal → Recommended** with player focused — only when cycle open |

**No** offer/commit actions on News tab — always via Recruiting hub.

### Week 1 nuance

Before national Top 40 locks: show **entry ticker** + **your Transfer-tab** players as stories; Top 40 block may show **“Publishes Week 2”** placeholder or partial “early movers” list until lock — prefer **placeholder** until Week 2.

---

## Recruits tab

Unchanged product lock ([`game-frame.md`](game-frame.md#news-hub-full)):

- National **Top 60** HS/JUCO — **`#1`–`#60` complete**
- Fixed at preseason seed; **not** portal

---

## Draft tab

- **Top 60** pro pool — **dynamic** through season / pro days ([`player-archetypes.md`](player-archetypes.md))

---

## Story detail & articles

Reuse **mini-article** pattern ([`preseason.md`](preseason.md#preseason-report--game-reports)):

| Field | Portal story |
|-------|----------------|
| **Headline** | Feed line |
| **Body** | 2–4 paragraphs — prior school, class, stars/letters (public), destination on commits |
| **Pull quote** | Optional flavor |
| **Footer** | Link **View in Portal** / **Open Recruiting** when window active |

**No** internal reason text (minutes, promise broken) in article body.

---

## Drill-in & deep links

| From | To |
|------|-----|
| Dashboard headline | Story detail **or** direct hub link for typed CTAs (“Respond in Recruiting”) |
| Portal tab → Add to board | Recruiting hub, portal sub-tab |
| Rival commit on **your** watchlist | Recruiting hub (optional future dashboard task — [`transfer-portal.md`](transfer-portal.md#ux--dashboard-follow-up)) |
| Commit to **your** school | Roster preview next season or Committed sub-tab |

---

## Cadence & generation

| Event | Feed |
|-------|------|
| **Week advance** (offseason 1–5) | Batch portal AI commits/entries → headlines for that week |
| **User encourage transfer** | Immediate **entry** story |
| **User portal sign** | **Commit** story (your school) |
| **Sim-week** user portal AI | Same generators as league |

**Priority:** During Weeks **2–5**, portal headlines **float above** generic poll noise on dashboard shallow feed (weighted sort, not separate app).

---

## What News does not show

| Hidden | Why |
|--------|-----|
| Other schools’ **watchlists / Recommended** | AI recruiting hidden |
| **Why** player entered portal | Design lock |
| Full **considering** pool (1k–1.4k) | Only **entrants** + stories |

---

## Cross-links

| Doc | Hook |
|-----|------|
| [`transfer-portal.md`](transfer-portal.md) | Calendar, Top 40 lock, News-heavy |
| [`recruiting-engine.md`](recruiting-engine.md) | Action hub, Top 60 recruits |
| [`game-frame.md`](game-frame.md) | Chrome, shallow dashboard News |

---

## Document history

| Date | Change |
|------|--------|
| 2026-11-09 | Initial News hub deep dive — Portal tab + Stories taxonomy |
