# Postseason — conference through national brackets

**Authoritative** Selection Sunday logic, Opening Round, **NCBA**, **NCI**, and **National Heritage Classic**. Universe display names: [`ncba-branding.md`](ncba-branding.md). Conference templates: [`game-design.md`](game-design.md#conference-tournaments). UI shell (planned): [`game-frame.md`](game-frame.md) → `postseason-frame.md`.

**Last updated:** 2025-11-08

---

## Contents

1. [Calendar order](#calendar-order)
2. [Postseason eligibility (reclass)](#postseason-eligibility-reclass)
3. [Conference tournaments](#conference-tournaments)
4. [Selection Sunday](#selection-sunday)
5. [NCBA Men's BB Tournament](#ncba-mens-bb-tournament)
6. [NCI](#nci--national-college-invitational)
7. [National Heritage Classic](#national-heritage-classic)
8. [Resume & UI tiers](#resume--ui-tiers)
9. [Cross-links](#cross-links)

---

## Calendar order

| Phase | When | Field |
|-------|------|--------|
| **Conference tournaments** | Early March | 32 conferences — champions feed NCBA |
| **Selection Sunday** | After conference finals | Locks NCBA / NCI / Heritage fields |
| **NCBA Opening Round** | After Selection Sunday | 24 conference champs → 12 games → 12 winners |
| **NCBA main bracket** | March Mayhem | 64 teams → National Championship |
| **NCI** | Overlaps / follows per template | 32 teams |
| **National Heritage Classic** | Per template | 16 teams |

League sim batches non-user games; user rounds use same possession engine as regular season.

---

## Postseason eligibility (reclass)

Programs with `postseasonEligible: false` (D-II→D-I reclass) **may**:

| Stage | Eligible? |
|-------|-----------|
| **Conference tournament** | **Yes** — full bracket participation |
| **National Heritage Classic** | **Yes** — if selected (top 16 among remaining eligible + reclass in pool) |
| **NCI** | **No** |
| **NCBA Men's BB Tournament** | **No** — no auto bid, no at-large |

If a reclass program **wins** its conference tournament, it is the **conference champion** for trophies/history, but the **NCBA automatic bid** passes per that conference’s `TournamentTemplate` (next **postseason-eligible** finisher / seed policy). Reclass teams are **not** counted among the **32** NCBA auto bids.

Certified programs use normal rules below.

---

## Conference tournaments

- One **TournamentTemplate** per conference (size, byes, sites) — **2026–27** real formats in default pack.
- Winner = **conference tournament champion** (distinct from regular-season title).
- Output: **32** champion slots feeding Selection Sunday (eligible teams only for NCBA bid purposes).

---

## Selection Sunday

Runs after all **32** conference champions are known. Uses the **national composite resume list** (same order as dashboard **Current ranking** — [`game-frame.md`](game-frame.md)).

### Step 1 — NCBA automatic qualifiers (32)

Lock **32** conference tournament champions with `postseasonEligible: true` (reclass winners trigger bid pass-over per conference rules; replacement team becomes an NCBA auto team if they are a conf tourney participant and eligible).

### Step 2 — At-large (44)

From postseason-eligible teams **not** among the **32** conference champions, select the **44 highest** resume ranks → **NCBA at-large** invitees.

| At-large tier | Count | Role |
|---------------|-------|------|
| **Top at-large** | **20** | Locked into **seeds 1–52** (with all 32 autos) — **no** Opening Round |
| **Bubble at-large** | **24** | **Opening Round** only — **12** games; **12** winners earn **seeds 53–64** |

### Step 3 — Seeds 1–52

Assign **seeds 1–52** across the **52** teams that skip Opening Round:

| In seed pool | Count |
|--------------|-------|
| Conference champions (all **32** autos) | 32 |
| Top **20** at-large | 20 |

**Seeding rule:** Resume / S-curve places **every** auto and top at-large on lines **1–52**. A weak conference champion can land **seed 52** (e.g. 15–15 resume, 19–15 after winning a small conference) while stronger at-large teams sit higher — autos are guaranteed a **slot**, not a **courtesy rank**.

All **32** conference champions **skip** Opening Round and enter **NCBA Round 1** on their assigned seed (even **52**).

### Step 4 — NCBA field check (**76** teams)

| Bucket | Count |
|--------|-------|
| Conference champions | 32 |
| At-large (direct, seeds 1–52) | 20 |
| At-large (Opening Round) | 24 |
| **Total in NCBA event** | **76** |

**Round 1 (round of 64):** **52** teams with seeds **1–52** + **12** Opening Round winners (seeds **53–64**) = **64**.

Opening Round **losers** (12) are **eliminated** from NCBA.

### Step 5 — NCI (32)

Among postseason-eligible teams **not** selected for NCBA, invite the **32 highest** resume ranks → **National College Invitational**.

### Step 6 — National Heritage Classic (16)

Among postseason-eligible teams **not** in NCBA or NCI, invite the **16 highest** resume ranks → **National Heritage Classic** (CBI analog).

Reclass programs **compete for Heritage** selection but are **never** in the NCBA or NCI pools.

### Bubble UI

- **Last teams in / out** for at-large (20-line direct) and **Opening Round** (24 invite / 12 advance).
- **NCI cut** (team 32 vs 33).
- **Heritage cut** (team 16 vs 17).
- Show when reclass bid pass-over flips an auto bid to another school.

---

## NCBA Men's BB Tournament

**Brand:** March Mayhem · **NCBA Men's BB Tournament**.

### Opening Round

| Rule | Value |
|------|--------|
| Participants | **24** at-large teams (ranks **21–44** among NCBA at-large selections) — **not** conference champions |
| Games | **12** |
| Advance | **12** winners → **seeds 53–64** in NCBA **Round 1** |
| Skip Opening | All **32** autos + top **20** at-large (**seeds 1–52**) |

Pairing by resume (e.g. 21 vs 44, 22 vs 43, … — `openingRoundPairing` in pack). Winners inherit **53–64** seed lines assigned on Selection Sunday.

### Main bracket (64 → champion)

| Structural | Universe label |
|------------|----------------|
| Round of 64 | **Round 1** |
| Round of 32 | **Round 2** |
| Sweet 16 | **Super 16** |
| Elite 8 | **Great 8** |
| Final Four | **Championship 4** |
| Title game | **National Championship** |

- **4 regions**, S-curve, conference conflict rules (avoid same conference until **Super 16** when possible).
- Opening winners keep **Selection Sunday** seed lines.

---

## NCI — National College Invitational

| Rule | Value |
|------|--------|
| **Field** | **32** teams — highest resume among eligible teams **not** in NCBA |
| **Rounds (universe labels)** | **Round 1** → **Round 2** → **Quarterfinals** → **Semifinals** → **Finals** |
| **Structure** | 32 → 16 → 8 → 4 → 2 → champion (single-elimination) |

Reclass programs: **not eligible**.

---

## National Heritage Classic

| Rule | Value |
|------|--------|
| **Reference** | College Basketball Invitational (CBI) |
| **Field** | **16** teams — highest resume among eligible teams **not** in NCBA or NCI |
| **Rounds** | **Round 1** → **Quarterfinals** → **Semifinals** → **Finals** |
| **Structure** | 16 → 8 → 4 → 2 → champion |

**Reclass programs:** **eligible** if they fall in the top 16 of this pool.

Coach **Achievements** track W–L and titles per bracket ([`coach-card.md`](coach-card.md)).

---

## Resume & UI tiers

Projected tiers on dashboard **Current ranking** chip ([`game-frame.md`](game-frame.md)) map to Selection Sunday targets:

| Color | Projected tier |
|-------|----------------|
| **Green** | NCBA field (auto, top-20 at-large, or Opening Round invite) |
| **Yellow** | NCI (not NCBA) |
| **Amber** (optional) | Heritage only |
| **Red** | None of the above, or reclass blocked from NCBA/NCI |

During conference tournament week, **green** may reflect **auto bid path** even when raw resume rank is outside the top 64.

---

## Cross-links

| Topic | Doc |
|-------|-----|
| Round display names | [`ncba-branding.md`](ncba-branding.md) |
| GDD summary | [`game-design.md`](game-design.md#postseason--selection) |
| Reclass years | [`league-branding.md`](league-branding.md#provisional-transition-programs) |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Initial postseason spec: Selection Sunday, champ Opening Round, NCI, Heritage, reclass |
| 2025-11-09 | NCBA **76**: 32 auto + 20 at-large (1–52) + 24 at-large Opening → 64 bracket |
