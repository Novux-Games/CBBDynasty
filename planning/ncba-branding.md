# Universe branding — NCBA & postseason

**Default pack display names** for the national body, March postseason, and early-season events. Real NCAA/NIT/Maui names stay **reference-only** in [`league-branding.md`](league-branding.md) and [`2026-NCAA-Directory.md`](2026-NCAA-Directory.md); simulation uses stable IDs + this overlay.

**Last updated:** 2025-11-08

---

## National body

| Real (reference) | Universe (default pack) |
|------------------|-------------------------|
| NCAA | **NCBA** — **National College Basketball Association** |

---

## March postseason branding

| Real (reference) | Universe (default pack) |
|------------------|-------------------------|
| March Madness | **March Mayhem** |
| NCAA tournament (bracket) | **NCBA Men's BB Tournament** — **76 teams** |
| NIT | **NCI** — **National College Invitational** — **32 teams** |

### NCBA Men's BB Tournament — field

| Slot | Count |
|------|-------|
| Conference champions (auto bids) | 32 |
| At-large | 44 |
| **Total** | **76** |

### NCI field

**Next 32** programs by **resume order** among postseason-eligible teams **not** in the 76-team NCBA field (ranks **77–108** on the same selection list). No separate “invitational committee” — automatic after Selection Sunday ordering.

Reclass programs (`postseasonEligible: false`) are excluded from both fields.

### NCBA round names (UI + news)

| Structural phase | Universe label |
|------------------|----------------|
| First Four–style play-in (76 → 64) | **Opening Round** |
| Round of 64 | **Round 1** |
| Round of 32 | **Round 2** |
| Sweet 16 | **Super 16** |
| Elite 8 | **Great 8** |
| Final Four | **Championship 4** |
| Title game | **National Championship** |

Pack keys (example): `round.opening`, `round.1` … `round.nationalChampionship`.

---

## Early-season holiday events (regular season)

**When:** Early **November–December** (non-conference window).  
**Purpose:** Quad 1/2 résumé wins; neutral/island sites.  
**Design lock:** Each program enters **at most one** early-season event tournament per **regular season**. This does **not** limit conference tournaments or **NCBA / NCI** postseason.

Each event counts toward the **32-game** regular-season cap (**up to 4 games** in that event). A team cannot join two catalog events in the same season.

| Real (reference) | Universe (default pack) | Pack template id (example) |
|------------------|---------------------------|----------------------------|
| Maui Invitational | **The Island Classic** | `evt-island-classic` |
| Battle 4 Atlantis | **The Caribbean Showdown** | `evt-caribbean-showdown` |
| Champions Classic–style marquee opener (high-major doubleheader / round-robin — **not** tied to specific real schools) | **The Blue Blood Tip-Off** | `evt-blue-blood-tipoff` |
| Feast Week–style multi-team bracket | **The Desert Shootout** | `evt-desert-shootout` |

**Scheduling:** User (and AI) opt in during **preseason scheduling**; bracket injected into calendar. Solver enforces **one event** + cap compliance.

---

## Cross-links

| System | Doc |
|--------|-----|
| Selection, opening round, 76→64 | [`game-design.md`](game-design.md#postseason--selection) |
| Regular-season caps & calendar | [`game-design.md`](game-design.md#scheduling--regular-season) |
| Coach achievement labels | [`coach-card.md`](coach-card.md#achievements) |
| App title (Novux) | [`branding.md`](branding.md) |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | NCBA/NCI/March Mayhem; round names; four early-season events; one event per season |
