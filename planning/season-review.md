# End of Season week — review before offseason

**When:** After **national postseason week 7** calendar completes ([`time-system.md`](time-system.md#national-postseason-weeks)). **Before** [`offseason.md`](offseason.md) (Coaching Carousel on Season 2+ or Offseason Week 1).

**UI label (design lock):** **End of Season** — full-year wrap-up (postseason results + **final** rankings).

**Last updated:** 2025-11-09

---

## Contents

1. [Purpose](#purpose)
2. [Calendar placement](#calendar-placement)
3. [Review surfaces](#review-surfaces)
4. [Advance](#advance)
5. [Cross-links](#cross-links)

---

## Purpose

One **non-game** week for the user (and league) to **look back** after the full competitive year — regular season through March — before roster churn and the portal.

| Goal | Detail |
|------|--------|
| **Closure** | Final NET, Media, Coaches, conference standings frozen for the year |
| **Program impact** | How the year moved **team grades**, **program prestige**, facilities narrative |
| **Coach impact** | Prestige tier, job security snapshot, career record update |
| **League story** | Champion, upsets, bracket paths — digest + History hooks |

Everyone uses this week whether they played **0** or **7** postseason games ([`time-system.md`](time-system.md) — same **7** postseason weeks for the whole league).

---

## Calendar placement

```
… → Postseason Week 7 → END OF SEASON WEEK → Offseason (Carousel or Week 1)
```

**`calendarPhase`:** `season_review` → on advance → `offseason`.

**Selection Sunday** and brackets are **final** before this phase; no new games sim here.

---

## Review surfaces

Single scroll or tabbed **Season Review** content on the same dynasty shell ([`game-frame.md`](game-frame.md)) — **End of Season** phase label; bottom nav unchanged.

| Block | Content |
|-------|---------|
| **Final rankings** | **NCBA NET #1–#365**, **Media** & **Coaches** final order; T25 callouts ([`selection-engine.md`](selection-engine.md)) |
| **Your season line** | W–L, conf record, postseason finish (**NCBA / NCI / HC** label or “Season complete”) |
| **Team grades** | Facility / academic / program grade **delta** vs start of year (what changed and why) |
| **Program prestige** | Start → end **0–99** with short reason chips (wins, tourney, scandals if any) |
| **Next season recruiting** | **Weekly points pool** preview for the **upcoming** cycle from post-recap prestige tier ([`recruiting-engine.md`](recruiting-engine.md#prestige-tier-weekly-pool-design-lock)) — vs this season’s locked pool if still in the same signing cycle |
| **Coach** | Prestige snapshot, job security tier, contract years left ([`coach-card.md`](coach-card.md)) |
| **Awards** | **Team MVP**, conference/national POTY, All-Conference / All-America ([`awards.md`](awards.md)); link to News / History |
| **History hooks** | New **national HOF** inductees, **GOAT** list refresh, national records this year ([`history-hub.md`](history-hub.md)) |
| **League digest** | Mini articles: champion, biggest riser/faller in NET, headline upset |
| **Roster snapshot** | Departures preview (grads already known); optional link forward to offseason Week 1 |

**Read-only** — no recruiting, no schedule edits. User **marks reviewed** or simply **Continue**.

---

## Advance

**CTA:** **Enter offseason** (or **Begin offseason**).

| Next step | Rule |
|-----------|------|
| **Season 2+** | [`Coaching Carousel`](offseason.md#coaching-carousel) |
| **Season 1** | **Offseason Week 1** — Players Leaving (no carousel) |

Sets `seasonComplete: true` for archive; increments `seasonIndex`; rolls year on league objects per engine rules.

---

## Cross-links

| Topic | Doc |
|-------|-----|
| Week calendar | [`time-system.md`](time-system.md) |
| Offseason entry | [`offseason.md`](offseason.md) |
| History hub | [`history-hub.md`](history-hub.md) |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-09 | Initial End of Season week before offseason |
