# Universe branding — NCBA & postseason

**Default pack display names** for the national body, March postseason, and early-season events. Real NCAA/NIT/Maui names stay **reference-only** in [`league-branding.md`](league-branding.md) and [`2026-NCAA-Directory.md`](2026-NCAA-Directory.md); simulation uses stable IDs + this overlay.

**Selection logic:** [`postseason.md`](postseason.md).

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
| NCAA tournament (bracket) | **NCBA Men's BB Tournament** |
| NIT | **NCI** — **National College Invitational** |
| CBI | **National Heritage Classic** |

### NCBA Men's BB Tournament — field

| Slot | Count |
|------|-------|
| Conference tournament champions (auto) | 32 |
| At-large (direct — seeds **1–52** with autos) | 20 |
| At-large (Opening Round bubble) | 24 → **12** winners |
| **Total in event** | **76** teams |
| **Round 1 (round of 64)** | **64** teams (52 + 12) |

**Seeds 1–52:** all **32** autos + **20** highest at-large. **Opening Round:** **24** bubble at-large, **12** games, winners take **seeds 53–64**.

### NCI field

**32** teams — highest resume among postseason-eligible programs **not** selected for NCBA ([`postseason.md`](postseason.md)).

### National Heritage Classic field

**16** teams — highest resume among eligible programs **not** in NCBA or NCI. **Reclass** programs may be selected.

### NCBA round names (UI + news)

| Structural phase | Universe label |
|------------------|----------------|
| Conference champ play-in (24 → 12) | **Opening Round** |
| Round of 64 | **Round 1** |
| Round of 32 | **Round 2** |
| Sweet 16 | **Super 16** |
| Elite 8 | **Great 8** |
| Final Four | **Championship 4** |
| Title game | **National Championship** |

### NCI round names

**Round 1** → **Round 2** → **Quarterfinals** → **Semifinals** → **Finals**

### Heritage Classic round names

**Round 1** → **Quarterfinals** → **Semifinals** → **Finals**

Pack keys (example): `round.opening`, `round.1` … `round.nationalChampionship`, `nci.*`, `heritage.*`.

---

## Early-season holiday events (regular season)

**When:** Early **November–December** (non-conference window).  
**Purpose:** Quad 1/2 résumé wins; neutral/island sites.  
**Design lock:** Each program enters **at most one** early-season event tournament per **regular season**. This does **not** limit conference tournaments or national postseason brackets.

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
| Selection Sunday & brackets | [`postseason.md`](postseason.md) |
| Regular-season caps & calendar | [`game-design.md`](game-design.md#scheduling--regular-season) |
| Coach achievement labels | [`coach-card.md`](coach-card.md#achievements) |
| App title (Novux) | [`branding.md`](branding.md) |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | NCBA/NCI/March Mayhem; round names; four early-season events; one event per season |
| 2025-11-08 | Heritage Classic; NCBA 64 + champ Opening Round; link `postseason.md` |
