# Selection engine — poll, Selection Sunday, seeding

**Authoritative** formulas for the national poll (**365** teams), **NCBA / NCI / Heritage** invites, and **NCBA seeds 1–64**. Postseason rules: [`postseason.md`](postseason.md). UI: **T25** = poll ranks **#1–#25** ([`game-frame.md`](game-frame.md)).

**Last updated:** 2025-11-09

---

## Contents

1. [Two outputs (don’t conflate)](#two-outputs-dont-conflate)
2. [Team indices (inputs)](#team-indices-inputs)
3. [National poll (weekly)](#national-poll-weekly)
4. [Selection Sunday — who gets in](#selection-sunday--who-gets-in)
5. [Seeds 1–52 (NCBA main bracket)](#seeds-152-ncba-main-bracket)
6. [Seeds 53–64 & regions](#seeds-5364--regions)
7. [Tuning & saves](#tuning--saves)
8. [Cross-links](#cross-links)

---

## Two outputs (don’t conflate)

| Output | When | What it decides |
|--------|------|------------------|
| **Poll rank** `#1`–`#365` | Weekly (plus **final** after conf tourneys) | **Membership** cuts: which non-champions make NCBA (20 / 24), NCI (32), Heritage (16) |
| **NCBA seed** `1`–`64` | Selection Sunday (after field locked) | **Bracket placement** only — strength order among teams already in the event |

**Design lock:** At-large **berths** follow **final poll rank** among **non–conference-tournament champions**. **Seeds** follow **Seed Index** among the **52** teams that skip Opening Round (and pre-defined lines for **53–64**).

A team can be **poll #8** (at-large) and **NCBA seed 4**, while a conference champion is **poll #140** but **NCBA seed 52**. Auto-bid guarantees **a berth** and a slot in the **1–52** seed sort; it does **not** guarantee a good seed.

---

## Team indices (inputs)

Recomputed when the weekly poll runs (and frozen as **final indices** at Selection Sunday). Built from **week-finalized** games ([`simulation-engine.md`](simulation-engine.md) box scores).

### School hub parity

The **same saved fields** drive School hub **Info** and this engine ([`game-frame.md`](game-frame.md#school-hub)):

| School hub label | Save field | Range |
|------------------|------------|-------|
| **Strength of schedule** | `sosRating` | **0–99** (integer UI) |
| **Prestige** | `programPrestige` | **0–99** (integer UI) |

**`sosRating`** is a first-class input to **poll** and **Seed Index** — not a hidden duplicate scale. Players who read **SOS 82** on a school card are seeing the number that selection math uses.

### Derived & display indices

| Index | Symbol | Range | Definition |
|-------|--------|-------|------------|
| **Strength of schedule** | `sosRating` | **0–99** | Opponents’ strength (opponent `NEM` network, 2-pass); clamped **0–99** for display |
| **Net efficiency margin** | `NEM` | approx −30..+30 | Points per 100 possessions (off eff − def eff), season to date |
| **Quad record score** | `QRS` | unbounded | Weighted quad wins minus weighted quad losses (see below) |
| **Form** | `FORM` | 0–100 | Last **10** games `NEM` blended with season `NEM` (60% L10 / 40% season) |
| **Record quality** | `RQ` | 0–100 | Win% adjusted for schedule (`win% * 0.6 + (sosRating/99) * 0.4`) |
| **Program prestige** | `programPrestige` | **0–99** | Pack baseline + dynasty drift — **poll voter bias only** (not seeding) |

### Quad buckets (per game)

Classify each D-I game at tip using opponent **poll rank at game time** (or frozen SOS proxy if unranked):

| Bucket | Opponent poll rank |
|--------|---------------------|
| **Q1** | #1–#50 |
| **Q2** | #51–#150 |
| **Q3** | #151–#280 |
| **Q4** | #281–#365 |

**`QRS` weights** (tunable `ruleset.quadWeights`):

| Event | Points |
|-------|--------|
| Q1 win | +4.0 |
| Q2 win | +2.5 |
| Q3 win | +1.0 |
| Q4 win | +0.3 |
| Q1 loss | −1.0 |
| Q2 loss | −0.5 |
| Q3/Q4 loss | 0 |

---

## National poll (weekly)

**Goal:** Sim voters produce one ordering of **all 365** teams — same list as AP-style poll ([`game-frame.md`](game-frame.md)).

### Poll score (per team)

Normalize `NEM`, `QRS`, `FORM`, `RQ` to **0–100** league percentiles each week. Use **raw 0–99** hub values for schedule and prestige:

```
sosTerm   = sosRating / 99
prestigeTerm = programPrestige / 99   // very slight voter bias — not merit

pollScore =
    0.30 * NEM_norm
  + 0.24 * sosTerm
  + 0.20 * QRS_norm
  + 0.13 * FORM_norm
  + 0.10 * RQ_norm
  + 0.03 * prestigeTerm
```

**`sosRating`** at **24%** is intentionally heavy — it matches the School hub SOS players already track. **`prestigeTerm`** at **3%** models real poll inertia (names, history) without overriding results; blue bloods nudge up, low-prestige overachievers nudge down, slightly.

**Sort** all **365** teams by `pollScore` **descending**; ties break `NEM` → `sosRating` → `teamId`.

**Poll rank** = position in that sort (**#1** = best). **T25** = ranks **#1–#25**.

**Inertia (optional polish):** `pollScore += 0.03 * (52 - previousPollRank)` capped ±1.5 so ranks don’t whip week-to-week.

Weights live in pack `ruleset.pollWeights` (defaults above).

---

## Selection Sunday — who gets in

Freeze **final poll ranks** after all conference tournaments (one more poll pass on final stats is allowed — same formula).

### NCBA (76)

1. **Auto-bids:** **32** eligible conference tournament champions (reclass pass-over → **runner-up** per [`postseason.md`](postseason.md)).
2. **Non-champions only:** sort by **final poll rank** ascending.
3. **Direct at-large (20):** poll ranks **#1..#20** among non-champions → skip Opening Round.
4. **Opening at-large (24):** next **#21..#44** among non-champions → **12** games, **12** winners join bracket.
5. **Eliminated from NCBA:** non-champions below that cut (unless already a champ).

*Champions are not re-sorted for these cuts — they are already in via auto-bid regardless of poll rank.*

### NCI (32) & Heritage (16)

Among **postseason-eligible** teams **not** on the NCBA list, take the **32** best **final poll ranks** → NCI, then the next **16** → Heritage (reclass eligible for Heritage only — [`postseason.md`](postseason.md)).

---

## Seeds 1–52 (NCBA main bracket)

### Who is in the pool

Exactly **52** teams that **skip Opening Round**:

| Role | Count |
|------|-------|
| Conference tournament champion (auto) | 32 |
| Top **20** at-large (non-champions) | 20 |

**Not** “seeds 1–32 = champs, 33–52 = at-large.” All **52** compete on **one** strength ladder.

### Seed Index (merit for seeding only)

```
seedIndex =
    0.34 * NEM_norm
  + 0.24 * QRS_norm
  + 0.22 * (sosRating / 99)
  + 0.12 * FORM_norm
  + 0.08 * RQ_norm
```

(No `programPrestige` — seeding is merit-only. **`sosRating`** same field as School hub.)

Sort the **52** teams by **`seedIndex` descending**. Assign **NCBA seeds 1–52** in that order (**1** = best team in pool).

**Example:** 28–3 at-large with strong `QRS` → **seed 5**. 19–15 small-conference champion → **seed 52**. Both are in the same sort; auto-bid only ensured the champ’s **berth**.

### Opening Round teams (seeds 53–64)

The **24** Opening invitees get **provisional** seed lines **53–64** before games (sorted by `seedIndex` within the 24; assign **53** to best, **64** to worst). **Pairing:** poll order within the Opening pool — **1 vs 24**, **2 vs 23**, … **12 vs 13** ([`postseason.md`](postseason.md)). **Winners** assume the provisional seed of that game’s slot; losers leave the event.

---

## Seeds 53–64 & regions

After seeds **1–64** are set:

1. **S-curve** assign **64** teams to **4 regions** (classic 1-16-8-9 style distribution).
2. **Conference conflict:** swap within seed lines when possible so same conference avoids meeting before **Super 16** ([`ncba-branding.md`](ncba-branding.md)).
3. Persist `regionId`, `seed`, `bracketSlot` on each NCBA entrant.

NCI / Heritage use separate bracket templates (no shared seed list with NCBA).

---

## Tuning & saves

| Artifact | Location |
|----------|----------|
| Default weights | Pack `ruleset.pollWeights`, `ruleset.seedWeights`, `ruleset.quadWeights` |
| Weekly poll snapshot | Dynasty save: `pollRankByTeamId`, `pollScore`, `sosRating`, week id |
| Selection Sunday snapshot | `selectionSundayYear`, NCBA/NCI/Heritage lists, `seedByTeamId`, frozen `NEM`/`QRS`/`sosRating`/… |

---

## Cross-links

| Topic | Doc |
|-------|-----|
| Field size, Opening Round, parallel brackets | [`postseason.md`](postseason.md) |
| Underlying metrics mention | [`game-design.md`](game-design.md#net-style-résumé-selection) |
| Round labels | [`ncba-branding.md`](ncba-branding.md) |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-09 | Initial engine: pollScore, seedIndex, poll vs seed split, seeds 1–52 sort |
| 2025-11-09 | School hub `sosRating` 0–99 + 3% `programPrestige` poll bias |
