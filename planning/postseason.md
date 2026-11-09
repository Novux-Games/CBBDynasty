# Postseason — conference through national brackets

**Authoritative** Selection Sunday logic, Opening Round, **NCBA**, **NCI**, and **National Heritage Classic**. Universe display names: [`ncba-branding.md`](ncba-branding.md). Conference templates: [`game-design.md`](game-design.md#conference-tournaments). UI shell (planned): [`game-frame.md`](game-frame.md) → `postseason-frame.md`.

**Last updated:** 2025-11-09

---

## Contents

1. [Calendar order](#calendar-order)
2. [Season-end census (365 teams)](#season-end-census-365-teams)
3. [Postseason eligibility (reclass)](#postseason-eligibility-reclass)
4. [Conference tournaments](#conference-tournaments)
5. [Selection Sunday](#selection-sunday)
6. [NCBA Men's BB Tournament](#ncba-mens-bb-tournament)
7. [NCI](#nci--national-college-invitational)
8. [National Heritage Classic](#national-heritage-classic)
9. [Poll rank vs seeds](#poll-rank-vs-seeds-in-season-vs-march)
10. [Cross-links](#cross-links)

---

## Calendar order

| Phase | When | Field |
|-------|------|--------|
| **Conference tournaments** | Early March | 32 conferences — champions feed NCBA |
| **Selection Sunday** | After conference finals | Locks NCBA / NCI / Heritage fields |
| **NCBA Opening Round** | After Selection Sunday | 24 at-large teams → 12 games → 12 winners |
| **NCBA main bracket** | March Mayhem | 64 teams → National Championship |
| **NCI** | **Parallel** with NCBA on calendar (same March window) | 32 teams |
| **National Heritage Classic** | **Parallel** with NCBA / NCI | 16 teams |

League sim batches non-user games; user rounds use same possession engine as regular season.

---

## Season-end census (365 teams)

Each season, exactly **124** programs receive a **national** postseason assignment; the other **241** end the year without one (still **365** active D-I teams in the default pack).

| National postseason | Teams |
|---------------------|------:|
| **NCBA** Men's BB Tournament | 76 |
| **NCI** | 32 |
| **National Heritage Classic** (HC) | 16 |
| **Total national postseason** | **124** |
| **No national postseason** | **241** |

Selection Sunday + NCI/Heritage invites partition **postseason-eligible** certified teams (reclass may occupy **Heritage** slots only; NCBA/NCI counts stay **76** + **32** invitees from eligible pool).

### When the other 241 stop playing

| Last game | Who |
|-----------|-----|
| **Conference tournament** exit (or conf title game loss) | Teams that **qualified** for their conference bracket |
| **Regular season** finale | Teams **not** selected for the conference tournament |

**Conference bracket membership** is **not** uniform: each `TournamentTemplate` defines who qualifies — e.g. **all** conference members, **top N** by standings, or **min seed** cutoff (pack data per real **2026–27** format). The sim sets `seasonComplete` when a program’s last allowed game is played.

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

- One **TournamentTemplate** per conference (size, byes, sites, **qualification policy**) — **2026–27** real formats in default pack.
- **`qualificationPolicy`** (per template): which teams enter — **all members**, **top N**, or **above cutoff** (programs below cutoff end on **regular-season** final).
- Winner = **conference tournament champion** (distinct from regular-season title).
- Output: **32** champion slots feeding Selection Sunday (eligible teams only for NCBA bid purposes).

---

## Selection Sunday

Runs after all **32** conference champions are known. Uses **NCBA NET** order — **all 365** teams **`NET #1`–`#365`** ([`selection-engine.md`](selection-engine.md)). **Media / Coaches** polls may publish for news; they **do not** set the field. **NET rank is not a tournament seed** until Selection Sunday assigns **NCBA / NCI / HC** seeds ([`postseason-frame.md`](postseason-frame.md)).

### Terminology (read this once)

| Phrase in spec | Means |
|----------------|--------|
| **Conference tournament champion** | Won the conference bracket → **auto-bid** |
| **Non–conference-tournament champion** | Did **not** win the conference tournament → **at-large** candidate (resume rank among eligible non-champs) |

Opening Round is **only** for at-large teams. **All 32** auto-bids skip Opening Round.

### Step 1 — Auto-bids (32)

**All 32** conference tournament champions receive an **auto-bid** to the NCBA Men's BB Tournament (`postseasonEligible: true` only).

Reclass (or other ineligible) **conference tournament champion:** program keeps the **title**; **NCBA auto-bid passes to the conference tournament runner-up** (eligible team only). If runner-up is also ineligible, fall back to next eligible finisher per conference template (document in pack).

### Step 2 — At-large into NCBA (44)

Among postseason-eligible teams that are **not** conference tournament champions, take the **44 highest** resume ranks into the **76-team** NCBA event:

| Tier | Count | Rule |
|------|-------|------|
| **Highest-ranked** non–conference-tournament champions | **20** | **Automatic pass** into the main bracket — **no** Opening Round; share **seeds 1–52** with all **32** autos |
| **Next** highest-ranked non–conference-tournament champions | **24** | **Opening Round** — **12** games; **12** advance to fill the round of 64 |

### Step 3 — Seeds 1–52

**Who:** same **52** teams that skip Opening Round (**32** autos + **20** direct at-large) — see [`selection-engine.md`](selection-engine.md#seeds-152-ncba-main-bracket).

**How:** **not** NET rank. Sort all **52** by **Seed Index** (net efficiency, quads, School hub **strength of schedule** `sosRating` **0–99**, form, record quality). Best → **seed 1**, worst in pool → **seed 52**. A strong at-large can be **seed 4**; a weak conference champion can be **seed 52** while still holding an auto-bid. See [`selection-engine.md`](selection-engine.md).

### Step 4 — NCBA field (**76** teams)

| Bucket | Count |
|--------|-------|
| Auto-bids (conference tournament champions) | 32 |
| At-large — direct (non-champions) | 20 |
| At-large — Opening Round (non-champions) | 24 |
| **Total** | **76** |

**Round of 64:** **52** teams on seeds **1–52** + **12** Opening Round winners (**seeds 53–64**) = **64**.

**12** Opening Round losers are eliminated.

### Step 5 — NCI (32)

Among postseason-eligible teams **not** selected for NCBA, invite the **32 highest NET ranks** → **National College Invitational**.

### Step 6 — National Heritage Classic (16)

Among postseason-eligible teams **not** in NCBA or NCI, invite the **16 highest NET ranks** → **National Heritage Classic** (CBI analog).

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
| Participants | **24** teams — the **next** highest-ranked **non–conference-tournament champions** after the top **20** at-large |
| Games | **12** |
| Advance | **12** winners → **seeds 53–64** in NCBA **Round 1** |
| Skip Opening | All **32** auto-bids + top **20** at-large |

**Pairing (24 teams, 12 games):** Sort Opening invitees by **NET rank** within the bubble group (best = 1). Pair **1 vs 24**, **2 vs 23**, … **12 vs 13** (1vN high-low). Winners receive **seeds 53–64** assigned on Selection Sunday (`openingRoundPairing` in pack may add neutral sites only).

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

## NET / polls vs seeds (in-season vs March)

| Phase | What the user sees |
|-------|---------------------|
| **Regular season / conf tourneys** | **NET `#1`–`#365`**; Media & Coaches **T25** callouts — **not** tournament seeds |
| **Selection Sunday onward** | **Tournament-labeled seeds** — never a bare “#1 seed” |

**Seed display (design lock):** Each national event has its **own** seed ladder. UI always prefixes the tournament short label ([`postseason-frame.md`](postseason-frame.md#tournament-seed-labels)):

| Event | Label | Seeds |
|-------|-------|-------|
| NCBA Men's BB Tournament | **NCBA** | **#1–#64** |
| National College Invitational | **NCI** | **#1–#32** |
| National Heritage Classic | **HC** | **#1–#16** |

**NCI #1** and **HC #1** are independent of **NCBA #1** — three different #1 seeds can exist the same night. NCI/HC lines use **Seed Index** among that event’s invitees only ([`selection-engine.md`](selection-engine.md)).

**March UI:** **[March Mayhem](postseason-frame.md#march-mayhem-hub)** hub (hamburger) monitors **NCBA · NCI · HC** brackets and games after Selection Sunday.

No in-season UI for projected NCBA / NCI / Heritage. **NET** is the résumé tracker; human polls are flavor. **Auto-bids and national invites** use **final NET** after conference tournaments ([`game-frame.md`](game-frame.md)).

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
| 2025-11-09 | NCBA **76**: 32 auto + 20/24 non-champion at-large; seeds 1–52 |
| 2025-11-09 | Terminology: non–conf-tourney champion = at-large (not champ Opening) |
| 2025-11-09 | Census: 124 national (76+32+16) vs 241 season-end; conf qual policy |
| 2025-11-09 | Auto pass → runner-up; poll=rank; parallel NCI/HC; Opening 1v24…12v13 |
| 2025-11-09 | Removed tier color chips from UI spec |
