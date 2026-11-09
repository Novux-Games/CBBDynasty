# Universe branding — NCBA & postseason

**Default pack display names** for the national body, March postseason, and early-season events. Real NCAA/NIT/Maui names stay **reference-only** in [`league-branding.md`](league-branding.md) and [`2026-NCAA-Directory.md`](2026-NCAA-Directory.md); simulation uses stable IDs + this overlay.

**Selection logic:** [`postseason.md`](postseason.md).

**Last updated:** 2025-11-08

---

## National body

| Real (reference) | Universe (default pack) |
|------------------|-------------------------|
| NCAA | **NCBA** — **National College Basketball Association** |
| NET | **NCBA NET Rankings** — formula résumé (`netScore`; selection) |
| AP / Coaches polls | **Media Poll** · **Coaches Poll** — human sim voters (not selection) |

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

**Seeds 1–52:** all **32** conference tournament champions + **20** highest-ranked **non–conference-tournament champions**. **Opening Round:** next **24** non-champions, **12** games → **seeds 53–64** ([`postseason.md`](postseason.md)).

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

### Tournament seed UI labels

Postseason **seeds** always show **tournament + number** (never ambiguous “#1 seed”):

| Event | Short label | Example |
|-------|-------------|---------|
| NCBA Men's BB Tournament | **NCBA** | `NCBA #4` |
| National College Invitational | **NCI** | `NCI #1` |
| National Heritage Classic | **HC** | `HC #12` |

**March Mayhem** hub title for the national postseason monitor ([`postseason-frame.md`](postseason-frame.md)).

---

## Early-season holiday events (regular season)

**When:** Early **November–December** (non-conference window).  
**Purpose:** Quad 1/2 résumé wins; neutral/island sites.  
**Design lock:** Each program enters **at most one** early-season event tournament per **regular season**. This does **not** limit conference tournaments or national postseason brackets.

Each event counts toward the **32-game** regular-season cap. A team cannot join two catalog events in the same season. **Calendar footprint** (weeks on RS calendar + game spots): [`time-system.md`](time-system.md#early-season-event-tournaments--byes).

| Real (reference) | Universe (default pack) | Pack template id | Teams | RS weeks | RS games |
|------------------|---------------------------|------------------|-------|----------|----------|
| Maui Invitational | **The Island Classic** | `evt-island-classic` | **16** | **4** | **4** |
| Battle 4 Atlantis | **The Caribbean Showdown** | `evt-caribbean-showdown` | 8 | 3 | 3 |
| Champions Classic–style marquee opener | **The Blue Blood Tip-Off** | `evt-blue-blood-tipoff` | **4** | **2** | **2** |
| Feast Week–style multi-team bracket | **The Desert Shootout** | `evt-desert-shootout` | **16** | **4** | **4** |

**Scheduling:** **Preseason → Tournament Invites** task ([`preseason.md`](preseason.md#6--tournament-invites)) — prestige gates invites; accept **one** before regular-season scheduling. Accepting reserves **4** RS games and caps other RS at **28**. AI resolves same rules in preseason sim.

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
