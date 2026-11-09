# Selection engine — NET, human polls, Selection Sunday, seeding

**Authoritative** formulas for **NCBA NET Rankings** (**365** teams), **Media** & **Coaches** polls (human sim voters), **NCBA / NCI / Heritage** invites, and **NCBA seeds 1–64**. Postseason rules: [`postseason.md`](postseason.md). UI: [`game-frame.md`](game-frame.md).

**Last updated:** 2025-11-09

---

## Contents

1. [Three ranking systems (don’t conflate)](#three-ranking-systems-dont-conflate)
2. [Team indices (inputs)](#team-indices-inputs)
3. [NCBA NET rankings (weekly)](#ncba-net-rankings-weekly)
4. [Media poll & Coaches poll (weekly)](#media-poll--coaches-poll-weekly)
5. [Selection Sunday — who gets in](#selection-sunday--who-gets-in)
6. [Seeds 1–52 (NCBA main bracket)](#seeds-152-ncba-main-bracket)
7. [Seeds 53–64 & regions](#seeds-5364--regions)
8. [Tuning & saves](#tuning--saves)
9. [Cross-links](#cross-links)

---

## Three ranking systems (don’t conflate)

| System | UI name (default pack) | Range | Driver | Primary use |
|--------|------------------------|-------|--------|-------------|
| **NCBA NET** | **NCBA NET Rankings** | `#1`–`#365` | **`netScore`** — formula only (no voter emotion) | **Selection Sunday** cuts, **quad** buckets, **Opening Round** order, **seed index** inputs |
| **Media poll** | **Media Poll** | `#1`–`#365` (public **Top 25** callout) | Sim **media** voters — similar inputs, **more** prestige / inertia / narrative | News, prestige, fan expectation, **T25 (Media)** |
| **Coaches poll** | **Coaches Poll** | `#1`–`#365` (public **Top 25** callout) | Sim **coaches** — similar inputs, **different** bias weights than media | News, respect, **T25 (Coaches)** |

**Design lock:** **Resume / committee logic = NET only.** Media and Coaches **never** grant bids or set seeds. They can disagree with NET (e.g. Media **#12**, NET **#28**) — that’s intentional.

| Output | When | What it decides |
|--------|------|------------------|
| **NET rank** `#1`–`#365` | Weekly (+ **final** after conf tourneys) | **Membership** cuts: NCBA at-large (20 / 24), NCI (32), Heritage (16) |
| **Media / Coaches rank** | Weekly | **Flavor & UI** only (headlines, T25 chips, AD mood hooks — not selection) |
| **NCBA seed** `1`–`64` | Selection Sunday | **Bracket placement** among teams already in the event |

A team can be **NET #8** (at-large) and **NCBA seed 4**, while a conference champion is **NET #140** but **NCBA seed 52**. Media might still show that champ **unranked in T25** or **#22** after a conf tourney run — **NET** ranks the full **365** for cuts.

---

## Team indices (inputs)

Recomputed each ranking pass (weekly NET + human polls; frozen **final NET** at Selection Sunday). Built from **week-finalized** games ([`simulation-engine.md`](simulation-engine.md) box scores).

### School hub parity

The **same saved fields** drive School hub **Info** and this engine ([`game-frame.md`](game-frame.md#school-hub)):

| School hub label | Save field | Range |
|------------------|------------|-------|
| **Strength of schedule** | `sosRating` | **0–99** (integer UI) |
| **Prestige** | `programPrestige` | **0–99** (integer UI) |

**`sosRating`** is a first-class input to **NET**, human polls, and **Seed Index** — not a hidden duplicate scale. Players who read **SOS 82** on a school card are seeing the number that **NET** and selection use.

**There is only one SOS per school:** **your** schedule strength. Poll and seeding read **`team.sosRating`** — they do **not** look up some other team’s hub SOS when evaluating you.

### How `sosRating` is computed (your schedule)

**Plain language:** Strength of schedule is **how hard your slate was** — the average quality of **every opponent you actually played**, with a little extra weight for road games and big spots.

**Recipe (weekly refresh after games finalize):**

1. For **each completed game** on **your** schedule, take that **opponent’s team strength** at tip (use opponent’s season `NEM` to date; frozen at game time in save for audit).
2. Optionally multiply by **venue weight** (`selection.sosVenueWeights` in balance bundle — e.g. road 1.1, neutral 1.05, home 1.0) so the same opponent counts slightly harder away from home.
3. **Schedule strength (raw)** = weighted mean of those opponent strengths across **all** your D-I games (non-D-I exhibitions excluded unless pack says otherwise).
4. Map raw league distribution to **0–99** (percentile or min–max in pack) → store as **`sosRating`** → show on School hub **Info**.

**What we are *not* doing:**

| Wrong idea | Why |
|------------|-----|
| “Use the opponent’s **SOS** rating” | That’s **their** schedule, not yours — unrelated to how hard **your** year was. |
| A second hidden SOS for selection only | **One** field: hub display = NET = seeds. |

Iterative “network” tweaks (opponent strength depends on who *they* played) can nudge opponent `NEM` in step 1 — that still rolls up into **your** single `sosRating`, not a separate voter-facing stat.

### Derived & display indices

| Index | Symbol | Range | Definition |
|-------|--------|-------|------------|
| **Strength of schedule** | `sosRating` | **0–99** | **Your** schedule difficulty — mean opponent strength on **your** results ([above](#how-sosrating-is-computed-your-schedule)) |
| **Net efficiency margin** | `NEM` | approx −30..+30 | Points per 100 possessions (off eff − def eff), season to date |
| **Quad record score** | `QRS` | unbounded | Weighted quad wins minus weighted quad losses (see below) |
| **Form** | `FORM` | 0–100 | Last **10** games `NEM` blended with season `NEM` (60% L10 / 40% season) |
| **Record quality** | `RQ` | 0–100 | Win% adjusted for schedule (`win% * 0.6 + (sosRating/99) * 0.4`) |
| **Program prestige** | `programPrestige` | **0–99** | Pack baseline + dynasty drift — **human polls only** (not NET or seeding) |

### Quad buckets (per game)

Classify each D-I game at tip using opponent **NET rank at game time** (not Media/Coaches):

| Bucket | Opponent NET rank |
|--------|---------------------|
| **Q1** | #1–#50 |
| **Q2** | #51–#150 |
| **Q3** | #151–#280 |
| **Q4** | #281–#365 |

**`QRS` weights** (balance bundle `selection.quadPoints` / cutoffs in [`balance-2026.json`](../assets/balance/balance-2026.json)):

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

## NCBA NET rankings (weekly)

**Goal:** Universe **committee / résumé** ordering of **all 365** teams — **deterministic formula**, no voter prestige or narrative.

### `netScore` (per team)

Normalize `NEM`, `QRS`, `FORM`, `RQ` to **0–100** league percentiles each week. Use **raw** School hub schedule:

```
sosTerm = sosRating / 99

netScore =
    0.32 * NEM_norm
  + 0.25 * sosTerm
  + 0.22 * QRS_norm
  + 0.12 * FORM_norm
  + 0.09 * RQ_norm
```

**No** `programPrestige`. **No** prior-week inertia (NET moves strictly with results).

**Sort** all **365** teams by `netScore` **descending**; ties break `NEM` → `sosRating` → `teamId`.

**NET rank** = position in that sort (**#1** = best). Shown as **NCBA NET #NN** in UI.

Weights: balance bundle `selection.netWeights` (defaults in JSON; must match formulas above).

---

## Media poll & Coaches poll (weekly)

**Goal:** Sim **human** voters — **similar** ingredients to NET, plus **emotion** (prestige, inertia, streak narrative). **Full 365** ranked internally; **Top 25** callouts published per poll ([`game-frame.md`](game-frame.md)).

Shared normalized inputs: same `NEM_norm`, `QRS_norm`, `FORM_norm`, `RQ_norm`, `sosTerm`, and **`netScore`** as anchor.

```
prestigeTerm = programPrestige / 99

mediaPollScore =
    0.72 * (netScore / netScoreLeagueMax)   // rescale netScore to ~0–100 band for mix
  + 0.10 * prestigeTerm * 100
  + 0.08 * mediaInertia
  + 0.06 * FORM_norm                       // recency bias — voters overweight hot teams
  + 0.04 * narrativeBoostMedia             // upset wins, headline momentum (capped)

coachesPollScore =
    0.78 * (netScore / netScoreLeagueMax)
  + 0.06 * prestigeTerm * 100
  + 0.10 * coachesInertia                  // coaches stickier than media
  + 0.04 * FORM_norm
  + 0.02 * narrativeBoostCoaches
```

**Inertia:** `mediaInertia` / `coachesInertia` = `k * (priorPollRank - 183)` capped ±3.0 (pull toward last week’s poll position — **poll-specific**, not NET).

**`narrativeBoost*`** (0–2 pts): optional week events (e.g. won at **NET #3**, buzzer beater on national TV) — **human polls only**.

**Sort** each poll separately → **mediaRank** `#1–#365`, **coachesRank** `#1–#365`.

Weights: `selection.mediaPollWeights`, `selection.coachesPollWeights` in balance bundle.

**Examples of divergence:** Mid-major on NET **#34** but Media **#19** after a ranked win; blue blood NET **#18** but Coaches **#8** on reputation.

---

## Selection Sunday — who gets in

Freeze **final NET ranks** after all conference tournaments (one **NET** refresh on final stats; human polls may publish for news but **do not** change the field).

### NCBA (76)

1. **Auto-bids:** **32** eligible conference tournament champions (reclass pass-over → **runner-up** per [`postseason.md`](postseason.md)).
2. **Non-champions only:** sort by **final NET rank** ascending.
3. **Direct at-large (20):** NET **#1..#20** among non-champions → skip Opening Round.
4. **Opening at-large (24):** next NET **#21..#44** among non-champions → **12** games, **12** winners join bracket.
5. **Eliminated from NCBA:** non-champions below that cut (unless already a champ).

*Champions are not re-sorted for these cuts — they are already in via auto-bid regardless of NET rank.*

### NCI (32) & Heritage (16)

Among **postseason-eligible** teams **not** on the NCBA list, take the **32** best **final NET ranks** → NCI, then the next **16** → Heritage (reclass eligible for Heritage only — [`postseason.md`](postseason.md)).

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

The **24** Opening invitees get **provisional** seed lines **53–64** before games (sorted by `seedIndex` within the 24; assign **53** to best, **64** to worst). **Pairing:** **NET** order within the Opening pool — **1 vs 24**, **2 vs 23**, … **12 vs 13** ([`postseason.md`](postseason.md)). **Winners** assume the provisional seed of that game’s slot; losers leave the event.

---

## Seeds 53–64 & regions

After seeds **1–64** are set:

1. **S-curve** assign **64** teams to **4 regions** (classic 1-16-8-9 style distribution).
2. **Conference conflict:** swap within seed lines when possible so same conference avoids meeting before **Super 16** ([`ncba-branding.md`](ncba-branding.md)).
3. Persist `regionId`, `seed`, `bracketSlot` on each NCBA entrant.

### NCI & Heritage seeds

Same **`seedIndex`** formula as NCBA, scoped to each invite list only:

| Event | Label | Sort pool | Seeds |
|-------|-------|-----------|-------|
| NCI | **NCI** | 32 NCI invitees | **NCI #1** (best index) … **NCI #32** |
| Heritage | **HC** | 16 HC invitees | **HC #1** … **HC #16** |

No cross-tournament seed rank (NCBA #8 is unrelated to NCI #8). UI rules: [`postseason-frame.md`](postseason-frame.md#tournament-seed-labels).

NCI / Heritage use separate bracket templates (no shared seed list with NCBA).

---

## Tuning & saves

| Artifact | Location |
|----------|----------|
| Default weights | [`assets/balance/balance-2026.json`](../assets/balance/balance-2026.json) — [ADR-002](../docs/ADR-002-balance-ruleset-bundle.md) (`selection.*`, `coach.jobSecurityWeights`) |
| Weekly snapshots | `netRankByTeamId`, `netScore`, `mediaRankByTeamId`, `coachesRankByTeamId`, `sosRating`, week id |
| Selection Sunday snapshot | `selectionSundayYear`, NCBA/NCI/Heritage lists, `nationalPostseason{tournamentId, seed, regionId?}` per team, frozen indices |

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
| 2025-11-09 | Clarify SOS = your schedule average, not opponent’s hub SOS |
| 2025-11-09 | Split NCBA NET vs Media & Coaches human polls |
