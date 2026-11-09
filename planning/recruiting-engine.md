# Recruiting engine

**Context:** HS/JUCO pools **seeded in preseason** (Top 60 all season); **year-long recruiting cycle**; **optional** in-season recruiting **dashboard tasks**; **sim-week** advance when user skips games/recruiting ([`game-frame.md`](game-frame.md#advance-week-confirmation-sim-week)).  
**Companion:** Offseason calendar [`offseason.md`](offseason.md), NIL [`game-design.md`](game-design.md#nil--program-economy), loyalty [`player-loyalty-promises.md`](player-loyalty-promises.md), player display [`player-cards.md`](player-cards.md), archetypes [`player-archetypes.md`](player-archetypes.md), balance bundle [ADR-002](../docs/ADR-002-balance-ruleset-bundle.md).  
**Last updated:** 2026-11-09

---

## Contents

1. [Timeline & seeding](#timeline--seeding) — includes [year-long cycle](#year-long-recruiting-cycle-design-lock)
2. [Pools & scale](#pools--scale)
3. [Star distribution](#star-distribution)
4. [Hidden ratings & letter grades](#hidden-ratings--letter-grades)
5. [Program prestige & school grades](#program-prestige--school-grades)
6. [Resources: recruiting points & NIL](#resources-recruiting-points--nil)
7. [Interest & offers](#interest--offers)
   1. [Goal](#goal)
   2. [Two surfaces](#two-surfaces)
   3. [Factor catalog](#factor-catalog)
   4. [Base weights](#base-weights)
   5. [Top priorities](#top-priorities)
   6. [Star-tier weight shifts](#star-tier-weight-shifts)
   7. [Scoring](#scoring)
   8. [Spend → interest (points system)](#spend--interest-points-system)
   9. [Promises & trust](#promises--trust)
   10. [Feed into `signLikelihood`](#feed-into-signlikelihood)
   11. [Data model](#data-model)
   12. [Worked examples](#worked-examples)
   13. [Balance vs sim](#balance-vs-sim)
   14. [Tuning knobs](#tuning-knobs)
   15. [Conflicts / non-overrides](#conflicts--non-overrides)
   16. [Open questions](#open-questions)
8. [Recruiting UI](#recruiting-ui)
9. [National rankings (Top 60)](#national-rankings-top-60)
10. [AI recruiting](#ai-recruiting)
11. [Commitments & signing](#commitments--signing)
12. [Walk-ons](#walk-ons)
13. [Cross-links](#cross-links)
14. [Open / TBD](#open--tbd)

---

## Timeline & seeding

### Class seeding (HS + JUCO)

| When | What |
|------|------|
| **Preseason** (task **Incoming Class Seed** — [`preseason.md`](preseason.md)) | Generate **HS** and **JUCO** pools for the **next enrolling class**; roll hidden ratings; assign **stars**; compute **Top 60** national order |

Seeding in **preseason** — not offseason — so **Top 60 recruits** live in **News** for the entire **regular season**. **Recruiting spend** is a **year-long cycle** ([below](#year-long-recruiting-cycle-design-lock)), not a single offseason burst.

| Phase | HS/JUCO access |
|-------|----------------|
| **Preseason → Signing Day (offseason week 6)** | Full **Recruiting** hub — spend points/NIL, soft commits in-season; **hard sign** on **Signing Day** week after **R4** |
| **After signing close** | Board read-only for enrolled class; next class seeds next **preseason** |

**New dynasty:** **Season 1** runs the same **preseason seed** before the first exhibitions so **Top 60** is available from tipoff of year one.

---

## Year-long recruiting cycle (design lock)

**Goal:** Recruiting feels like a **full-year job** (class revealed in preseason, relationships built during the season, signing after the year) **without** slowing week-to-week play.

### Speed contract (with [`game-frame.md`](game-frame.md))

| Rule | Detail |
|------|--------|
| **Dashboard tasks** | Any in-season recruiting action the user **can/should** take appears as an **optional** dashboard task ([below](#in-season-dashboard-tasks)) |
| **Advance gate** | Recruiting **never** a **required** task — user may advance without opening the hub |
| **Sim week on advance** | Unfinished optional work (unplayed games, unspent drip) → [`game-frame.md`](game-frame.md#advance-week-confirmation-sim-week); **AI** runs user-school recruiting + game sim if user confirms |
| **League AI on advance** | Non-user schools spend drip and move interest/commits when any school advances the week |
| **Automation** | **Auto-maintain board** (game-design) used for user school when advancing without manual recruiting |

### Cycle arc (one enrolling class)

```
Preseason (class seed)
    → Exhibitions + RS weeks 1–32 + conf/postseason
    → End of Season
    → Offseason W1 (retention; portal entries)
    → Offseason W2–W5  ← Signing crunch (× point multiplier still applies)
    → Offseason W5 close = SIGNING DAY (hard commits lock)
    → … later weeks roster/facilities …
    → Next preseason = next class seed
```

| Anchor | Calendar |
|--------|----------|
| **Cycle opens** | Preseason **Incoming Class Seed** completes — pools + Top 60 published |
| **Cycle closes** | Offseason **week 6** — **Signing Day** (after **R4** on week 5) |
| **Portal sub-cycle** | Entrants from offseason **week 1**; portal sub-tab spend when window is open ([`offseason.md`](offseason.md)) — same hub, not a second app |

### Weekly economy

- **Weekly recruiting points** = **fixed for the entire recruiting cycle** (preseason seed → signing close) — same grant every week advance while the cycle is open. See [Prestige tier weekly pool](#prestige-tier-weekly-pool-design-lock).
- **50% carryover** of unspent points to the next week; rest expires.
- **NIL** remains **annual pool** (separate economy); allocate anytime before hard sign.
- **Signing crunch:** Offseason weeks **2–5** apply **1.5×** on the **tier weekly pool** (not a different base).

Next cycle’s weekly pool is set at **preseason** from **`programPrestige`** (and grade-driven prestige deltas) frozen at **[End of Season](season-review.md)** recap — not mid-cycle.

### Commits

| Type | When |
|------|------|
| **Soft commit** | Anytime interest + offer thresholds met (including **in-season**) |
| **Hard commit / sign** | **Signing Day** at end of offseason **week 6** (binding; NIL snapshot) |
| **Decommit** | Allowed before hard sign; prestige hit per existing rules |

Headlines in **News** for blue-chip commits league-wide; your targets also spawn **optional dashboard recruiting tasks** when action is recommended (see [In-season dashboard tasks](#in-season-dashboard-tasks)).

### In-season dashboard tasks

**Master index:** [`dashboard-tasks.md`](dashboard-tasks.md). While the [year-long cycle](#year-long-recruiting-cycle-design-lock) is open, the dashboard **must** list **optional** recruiting tasks when any trigger fires (same card pattern as games):

| Trigger (examples) | Task copy (examples) |
|--------------------|-------------------------|
| **Weekly drip** granted, points still unspent | “Spend recruiting points ({N} available)” → Recruiting hub **Recommended** tab |
| **Watchlist** recruit crossed interest threshold | “Offer {name}” / “Re-engage {name}” |
| **New Top 60 movement** on your board | “Review {name} — #{rank}” |
| **Rival commit** on a shared target | “Respond — {name} trending to {school}” |
| **Portal** (in window) | Portal sub-tab equivalents when entrants match filters |

**Complete** when user performs the suggested action or opens hub and spends/applies changes for that nudge; **dismiss** only where product allows without blocking advance (e.g. “Skip this week” hides nudge until next drip — tune in UI pass).

**Offseason / preseason:** Same task pattern; phase docs may mark specific recruiting obligations **Required** (e.g. retention week 1, signing close).

### What does *not* change

- Star bands, pool sizes, **official visits** (RS → **R4**), Top 60 fixed at seed, combined HS/JUCO board, portal sub-tab, walk-ons after **Signing Day**.
- **Four recruiting week indices (R1–R4)** still map to **offseason weeks 2–5** for copy, NIL peaks, and crunch multiplier — they are **not** the only weeks you can spend.

### Transfer portal

**Authoritative:** [`transfer-portal.md`](transfer-portal.md) — one offseason cycle, **no in-season** entries, shared points/NIL budget, Week 1 **Roster** departures tab.

| When | What |
|------|------|
| **Offseason Week 1** | National **entries**; retention / cuts resolve who is in the pool |
| **Offseason Weeks 2–5** | **Transfer Portal** sub-tab — recruit/sign (not mixed into HS/JUCO) |

Portal star bands on **final entrants** only; **Portal Top 40** locked at **start of Offseason Week 2** ([`transfer-portal.md`](transfer-portal.md)).

### Signing period (R1–R4)

**Naming:** R1–R4 = offseason weeks **2–5** — **signing crunch** (1.5× drip), not the only weeks you can spend ([year-long cycle](#year-long-recruiting-cycle-design-lock)).

| Recruiting week | Offseason anchor | Highlights |
|-----------------|----------------------|------------|
| **R1** | Offseason **week 2** | Portal **sub-tab** peak; **NBA draft results**; scholarships open after draft |
| **R2** | Offseason **week 3** | Peak offers; NIL pushes |
| **R3** | Offseason **week 4** | Commit wave; head-to-head resolution |
| **R4** | Offseason **week 5** | Last **signing crunch** week; **official visits** close; last chance to soft-commit before Signing Day |
| **Signing Day** | Offseason **week 6** | **Hard sign**; class locks; [Signing Day tasks](#signing-day-offseason-week-6) |

Week 1 offseason = **retention** on your roster ([`offseason.md`](offseason.md#week-1--players-leaving)). In-season recruiting uses **optional** dashboard tasks only ([`game-frame.md`](game-frame.md#dashboard-task-gate)).

---

## Pools & scale

Each **preseason**, generate independent pools for the incoming class (same counts every year):

| Pool | Count | Mixed in UI? |
|------|-------|----------------|
| **High school** | **Uniform random integer 2,000–4,000** per season (not tied to year, prestige, or user) | Yes — single **HS + JUCO** board |
| **Junior college** | **+1,500** (fixed add-on) | Same board as HS |
| **Transfer portal** | **All entrants** that window (varies by year/sim) | **Separate sub-tab** only |

**Walk-ons** — see [Walk-ons](#walk-ons) (all schools; tryouts after main recruiting window).

**Geography:** Programs store **`homeCity`** + **`homeState`** ([`league-branding.md`](league-branding.md#geography--recruiting)). **Recruiting pipeline** keys off **`homeState`** (in-state / regional bias). Cities stay on the school record for travel, schedule, and UI; national pool is primary.

---

## Star distribution

Stars are assigned **inside each pool** from hidden rating percentiles. **Do not** share percentile ranks across pools.

### Percentile rules (per pool)

| Stars | Percentile band (within that pool) | Example @ 2,000 HS |
|-------|-------------------------------------|---------------------|
| **5★** | Top **2%** | ~40 players |
| **4★** | Next **18%** (cumulatively top **20%**, excluding 5★ band) | ~360 players |
| **3★** | Next **30%** (20%–50%) | ~600 players |
| **2★** | Remainder **50%–100%** | ~1,000 players |

**Design lock:** no 1★ band — every seeded HS/JUCO/portal player carries 2★–5★. The 20–50% / 50–100% split matches real-world “most offers are 3★–2★” without a long tail of unrated prospects.

**JUCO (1,500):** same bands → ~30 5★, ~270 4★, etc., independent of HS counts.

**Portal:** same **2% / 20% / …** table applied only to players who entered that cycle’s portal pool.

### UI composition

| Tab | Shows |
|-----|--------|
| **High school & JUCO** | Combined list; badge indicates **HS** vs **JUCO** |
| **Transfer portal** | Portal only; transfer badge; prior school & class |

**Transfers** always receive a star label like HS/JUCO recruits (from portal pool percentiles).

---

## Hidden ratings & letter grades

### Design lock

| Rule | Detail |
|------|--------|
| **True attributes** | Full 0–99 (and archetype tags) exist in save data from seeding |
| **Recruit UI** | **No** public 0–99 on board or unscoped card — show **letter grades A–F** instead |
| **Reveal** | **Scouting** (recruiting points) + commits raise fidelity (e.g. `B+` → firmer band; post-commit full [`player-cards.md`](player-cards.md) ratings next season) |

### Letter grade rows (examples)

Scout report shows category letters, not integers:

| Category | Examples on recruit sheet |
|----------|---------------------------|
| Overall | **A–F** chip |
| Offense / Defense | Letter |
| Shooting / Handling / Athleticism | Letter |
| Potential | Letter (may stay fuzzy longer) |
| **NIL ask** | Recruit’s **`nilAskUsd`** /yr (visible — drives **Assign NIL** on recruit sheet) |

Archetype fit may show as text + letter **fit** grade once scouted.

**Portal / JUCO:** often more film (slightly higher starting scout accuracy) — tunable.

---

## Program prestige & school grades

### School grade inputs

Each program carries **letter grades A–F** on dimensions (examples):

| Dimension | Affects |
|-----------|---------|
| **Facilities** | Campus appeal, recruit interest baseline |
| **Academics** | APR/GPA story, some recruit tags |
| **Conference prestige** | National schedule / brand |
| **Program prestige** (historical success) | Baseline interest |
| **Arena / recruiting lounge** | Recruiting efficiency |

(Additional sinks from [`game-design.md`](game-design.md) facilities table — arena, practice, strength, medical, academic center, recruiting lounge.)

### Prestige (0–99)

Composite **`programPrestige`** for recruiting:

```
programPrestige = f(facilities, academics, conferencePrestige, programHistory, …)
→ display 0–99
```

**Prestige (0–99)** is the **primary** driver for **which weekly recruiting pool** applies. Letter grades move prestige (and other systems) via season performance and upgrades — they do **not** maintain a separate weekly-points formula in UI.

### Coach & staff

| Input | Effect |
|-------|--------|
| **Head coach recruiting rating** | Interest bump, efficiency of recruiting point spend |
| **Coach skill tree** (recruiting / NIL charm) | From [`coach-card.md`](coach-card.md) / upgrades |
| **Assistant recruiting grades** | Staff grade → recruiting point efficiency |

---

## Resources: recruiting points & NIL

Two spend tracks while the **recruiting cycle** is open:

| Resource | Use |
|----------|-----|
| **Recruiting points** | **Relationship & evaluation labor** — scout, contact, offer, pitch (below) |
| **NIL dollars** (`usd_annual`) | **Money on the table** toward a recruit’s **`nilAskUsd`** — **not** paid from the weekly points pool — authoritative UX: [`game-design.md`](game-design.md#nil-budget-workflow-design-lock) |

### Weekly points accounting (design lock)

| Rule | Detail |
|------|--------|
| **Tier grant** | Each week while the cycle is open, add that cycle’s **fixed** prestige-tier **weekly pool** — the **assigned** pool size **does not change** week-to-week until the next cycle (see [below](#prestige-tier-weekly-pool-design-lock)) |
| **Carryover** | **50%** of **unspent** points at week end roll forward; other 50% expires ([carryover](#carryover)) |
| **Default spend** | Points spent on an action **stay spent** for that week unless the action is **[refunded](#refunded-actions-week-reservation)** |
| **Refunded actions** | Cost counts only in the **week you pay**; at **week advance**, if the action is still active (offer out, visit scheduled, etc.), those points **return** to your next week’s spendable balance |

**Example:** Scholarship offer **150** in **Week 1**. You spend your entire pool that week. The **150** is **refunded** when Week 1 closes while the offer remains → you can spend that **150** again in Week 2 (and every later week the offer stays open, unless you revoke the offer).

### Action costs (design lock)

Authoritative costs: [`balance-2026.json`](../assets/balance/balance-2026.json) → `recruiting.actions`. **NIL** is separate ([annual pool](#nil-annual)).

| Action | Cost (pts) | Refunded? | Limits |
|--------|------------|-----------|--------|
| **Scholarship offer** | **150** | **Yes** — week reservation while offer stands | Revoke offer → reservation ends |
| **DM** | **50** | No | Repeatable |
| **Phone call** | **100** | No | Repeatable |
| **Send the house** | **500** | No | Big attention swing; use sparingly |
| **Scout their game** | **50** | **Yes** — same week-reservation rule | **Once per recruit per season**; **regular season only** — sharpens letter grades / fit on sheet |
| **Official visit** | **50 / 100 / 150** by [visit distance](#official-visit-distance) | **Yes** — week reservation while visit pending/completed that week | **Once per recruit per season**; window **RS start → end of R4** (through offseason **week 5**) |
| **Bonus points** | **Spend remainder** (any amount ≤ unspent balance) | No | Dump leftover weekly points into **attention** on one target — primary edge for **blue bloods** once headline actions are exhausted |

**Coach promise** (at offer / commit): **0** recruiting points — [`player-loyalty-promises.md`](player-loyalty-promises.md).

**Not purchased with recruiting points:** **NIL $**, **Board / Targets** (free), **Recommended** browsing (free). **Soft/hard commits** are outcomes when interest + offer + NIL align.

#### Refunded actions (week reservation)

1. On spend, subtract cost from **this week’s** spendable pool immediately.
2. If the player **ends the week at 0** spendable but still has an active refunded action, the engine still treats the reservation as spent **for that week only**.
3. On **week advance**, for each active refunded reservation, **credit the cost back** before applying the next week’s tier grant (+ carryover).

#### Official visit distance

| Band | Cost | Examples (tunable) |
|------|------|---------------------|
| **Local** | **50** | In-state or adjacent pipeline state |
| **Regional** | **100** | Same broad region, not local |
| **Remote** | **150** | Cross-country / low geography fit |

#### Bonus points

- User allocates **all or part** of remaining **weekly** points to one recruit after other actions.
- Drives **marginal interest** and simulates volume recruiting; high-prestige programs with large pools lean on this after DM/call/house.
- Interest delta per point tunes in balance JSON (`recruiting.actions.bonusPoints`).

### Signing Day (offseason week 6)

**After R4** (offseason **week 5**) — dedicated **Signing Day** phase inside the dynasty shell ([`offseason.md`](offseason.md#week-6--signing-day)).

| Rule | Detail |
|------|--------|
| **Hard sign** | All **soft commits** finalize; unsigned targets leave the class (AI signs elsewhere) |
| **Recruiting points** | **No** new weekly tier grant for new chasing — administrate the class |
| **Required dashboard tasks** | **View Recruiting Rankings** (full Top 60 / class context) · **Change positions** on incoming class ([quick roster editor](#signing-day-position-editor)) |
| **After advance** | Cycle closed for that class; walk-on window may open ([walk-ons](#walk-ons)) |

#### Signing Day position editor

Loads the **customized roster engine** for incoming commits (and portal signees already on the class list): quick **position** changes (PG–C / flex) before roster lock — same validation as [`positions-and-lineup.md`](positions-and-lineup.md), streamlined UI for bulk edits.

### Prestige tier weekly pool (design lock)

**Authoritative numbers** live in [`balance-2026.json`](../assets/balance/balance-2026.json) → `recruiting.prestigeTiers` (pinned at dynasty create — [ADR-002](../docs/ADR-002-balance-ruleset-bundle.md)).

| Prestige tier | Prestige range | Weekly points pool | Realistic capacity |
|---------------|----------------|--------------------|--------------------|
| **Low-Major** | 0 – 25 | **1,000 – 1,250** | **2–3** core targets |
| **Mid-Major** | 26 – 50 | **1,500 – 2,000** | **3–5** core targets |
| **High-Major** | 51 – 74 | **2,500 – 3,250** | **5–7** core targets |
| **National Contender** | 75 – 89 | **3,500 – 4,250** | **7–9** national targets |
| **Blue Blood** | 90 – 99 | **4,500 – 5,000** | Full national board |

| Rule | Detail |
|------|--------|
| **Fixed per cycle** | `weeklyRecruitingPoints` is computed **once** when the cycle opens (preseason **Incoming Class Seed** completes) and is **unchanged** every week until signing close — including through **End of Season** and offseason signing weeks |
| **Exact value in band** | **Linear map** of `programPrestige` inside the tier’s min–max prestige → pool min–max (integer, round down) unless balance JSON specifies a step table |
| **When it changes** | **[End of Season](season-review.md)** applies **prestige** and **grade** recap for the year; the **next** cycle’s weekly pool is derived from **post-recap** `programPrestige` (preview on review screen) |
| **Dynasty start** | First cycle uses prestige at **game start** / entering **Season 1** preseason |
| **Realistic capacity** | Product guidance for **[Recommended](#recommended-tab-default)** and UI copy — **not** a hard cap on offers or commits |
| **Spend efficiency** | Coach / staff / **recruiting lounge** may still improve **points-per-action** — not the weekly pool ([`coach-card.md`](coach-card.md)) |

**Grant timing:** Each **week advance** while the cycle is open → add **weekly pool** (+ carryover from prior week) × **1.5** if offseason weeks **2–5**.

### NIL (annual)

**Annual collective $** — conference tier × prestige; blue blood **90–99** on power leagues **~$9.7M–$13.2M**. Authoritative: [`game-design.md`](game-design.md#annual-collective-pool-design-lock). Separate from recruiting **points**.

| Driver | Behavior |
|--------|----------|
| **Ledger** | `nilPoolAnnualUsd`, committed (signed deals), reserved (open offers), **remaining** — one **NIL Budget** strip in Recruiting hub |

### Carryover

**Carryover (design lock):** at week end, **50%** of **unspent recruiting points** roll into the next week’s pool (rounded down). The other 50% expires. NIL unspent does **not** roll week-to-week unless tied to an annual pool remainder rule in NIL doc (annual pool cap only).

---

## Interest & offers

**Status:** Approved (2026-11-09). Fills the factor weights this section left as “Weight (tunable).” Those numbers are **proposals** for `balance-2026.json` → `recruiting.interest.*` ([ADR-002](../docs/ADR-002-balance-ruleset-bundle.md#recruiting-interest-proposed-keys)). This spec does **not** edit the bundle. When the doc and JSON diverge, **JSON wins**.

### Goal

Every recruit uses the **same factor catalog**, but **not** one fixed weight vector. Per-recruit **top priorities** (and a light **star-tier** shift) change how much each factor matters. That keeps recruiting readable without a single solvable formula.

This section does **not** replace weekly points, NIL ask/offer math, Soft/Hard commit rules, or Recommended badges. It specifies how **interest** is computed and how it plugs into those systems.

### Two surfaces

| Surface | When | Spend? | UI |
|---------|------|--------|-----|
| **`baselineInterest`** | Class seed + week refresh | **Zero user spend** (school state only) | Feeds **`signLikelihood`** / Recommended sort |
| **`interest` (meter)** | Live while cycle open | Includes offers, NIL $, points actions, visits, promise | Recruit sheet meter **0–100** |

```
interest = clamp0_100( baselineInterest + spendDelta + promiseDelta )
signLikelihood ≈ f(baselineInterest, prestigeVsStarFloor, competitionProxy, rosterNeed)
```

**Design lock (existing):** `signLikelihood` stays **badge-only** (Likely / Possible / Reach / Long shot) — never shown as 0–99 ([`signLikelihood`](#signlikelihood-per-recruit--your-school)).

**NIL** committed at offer time still writes the signing number on [`player-cards.md`](player-cards.md) Overview when the hard commit lands ([NIL Budget](game-design.md#nil-budget-workflow-design-lock)).

### Factor catalog

Six factors. Each scores **0–1** for (recruit × school), then weighted.

| Id | Meaning | School / recruit inputs (existing where noted) |
|----|---------|-----------------------------------------------|
| **`nil`** | Money fit | `nilOfferUsd` vs `nilAskUsd` ([Interest & signing](game-design.md#interest--signing)); at baseline use `nilPoolAnnualUsd` vs ask as a **capacity** proxy |
| **`playingTimeFit`** | Depth chart / freshman minutes / style fit | Roster hole at position, projected minutes band, archetype vs scheme ([`player-archetypes.md`](player-archetypes.md), [`strategy-schemes.md`](strategy-schemes.md)) |
| **`prestigeWinning`** | Brand + recent winning | `programPrestige` (0–99), recent tourney / NET / Media form, conference strength |
| **`coachRelationship`** | Effort & pitch | Coach + staff recruiting grades; **spend** actions (DM / call / house / bonus / visit); optional promise bump |
| **`proximity`** | Geography | Recruit home vs program `homeState` — **tiered** (not linear miles) |
| **`campusLife`** | Facilities / academics / campus | School letter grades the recruit cares about (Facilities, Academics, Arena / lounge — [Program prestige & school grades](#program-prestige--school-grades)) |

#### Proximity tiers (design proposal)

| Band | Score (default) | Rule of thumb |
|------|-----------------|---------------|
| **In-state** | **1.0** | Same `homeState` as program |
| **Pipeline / adjoining** | **0.65** | Adjoining or declared pipeline states ([`league-branding.md`](league-branding.md#geography--recruiting)) |
| **Regional** | **0.35** | Same broad region, not pipeline |
| **Far** | **0.10** | Everything else |

Exact state→region tables → balance JSON (not hard-coded in UI).

### Base weights

**Default vector** (sums to **1.0**) — proposed starting point for `balance-2026.json` → `recruiting.interest.baseWeights`:

| Factor | Weight |
|--------|--------|
| `nil` | **0.25** |
| `playingTimeFit` | **0.20** |
| `prestigeWinning` | **0.20** |
| `coachRelationship` | **0.15** |
| `proximity` | **0.10** |
| `campusLife` | **0.10** |

**Not a silent override:** the drivers above were already “Weight (tunable)” without numbers. This table **fills** that gap. If product picks different bases, change JSON only.

Point actions (Send the house, bonus points) are **not** a seventh weight. They move **`spendDelta`** through `coachRelationship` ([Spend → interest](#spend--interest-points-system)).

### Top priorities

#### Generation

| Rule | Detail |
|------|--------|
| **Count** | Each recruit rolls **1–2** `topPriorities` at class seed (portal entrants re-roll or inherit — [Open](#open-questions)) |
| **Effect** | Each listed priority **×≈2** on that factor’s weight; **renormalize** so weights still sum to 1 |
| **Two priorities** | Apply both doubles, then renormalize once |
| **Display** | Recruit sheet chips (e.g. “Wants to stay close to home”) — **hidden** until scout depth **or** first contact (DM / call / house / visit / offer) — same reveal family as loyalty tendency ([`player-loyalty-promises.md`](player-loyalty-promises.md#loyalty-rating)) |

#### Priority → factor map

| Priority id | Label (UI) | Boosts factor |
|-------------|------------|---------------|
| `stay_home` | Stay close to home | `proximity` |
| `follow_money` | Follow the money | `nil` |
| `start_now` | Start right away | `playingTimeFit` |
| `chase_title` | Chase a title | `prestigeWinning` |
| `coach_bond` | Trust the coach | `coachRelationship` |
| `campus_fit` | Campus / academics first | `campusLife` |

Aligns with existing copy “school grade letters on recruit’s **priority dimensions**” — priorities **are** those dimensions, made explicit.

#### Renormalize (pseudo)

```
w = baseWeights
for p in recruit.topPriorities:
  w[factor(p)] *= priorityMultiplier   // default 2.0
w = w / sum(w)
```

### Star-tier weight shifts

After priority renormalize, apply a **small** star-band nudge (then renormalize again):

| Stars | Shift (proposed) |
|-------|------------------|
| **5★** | `nil` **+0.08**, `prestigeWinning` **+0.04**; pull from `playingTimeFit` / `proximity` |
| **4★** | `nil` **+0.04**; mild |
| **3★** | near base |
| **2★** | `playingTimeFit` **+0.06**, `proximity` **+0.04**; pull from `nil` / `prestigeWinning` |

Rationale: blue-chip asks command the bag; lower stars care more about minutes and home. **Does not** change `nilAskUsd` bands (`nil.askByStarBand` in [`balance-2026.json`](../assets/balance/balance-2026.json)) or the **3×** offer cap — only **how much** NIL fit moves the meter.

### Scoring

```
weighted = Σ (w_i × score_i)          // 0–1
baselineInterest = round(100 × weighted)
```

#### Factor score sketches (tunable curves in JSON)

| Factor | 0–1 sketch |
|--------|------------|
| **`nil`** (live) | `0` if no $ and high ask; else `(nilOfferUsd / nilAskUsd) ^ interestNilRatioExponent` clamped 0–1 (shipped `nil.interestNilRatioExponent`); ≥ ask → **1.0** ([Interest & signing](game-design.md#interest--signing)); over-ask up to **3×** may add a tiny surplus (cap in JSON) |
| **`nil`** (baseline) | Capacity: `min(1, nilPoolAnnualUsd / (nilAskUsd × capacityDivisor))` — so Recommended does not assume the pool is already allocated |
| **`playingTimeFit`** | Blend: open scholarship + position depth rank + scheme/archetype fit letters |
| **`prestigeWinning`** | Map `programPrestige` vs recruit’s implied floor + recent success bump |
| **`coachRelationship`** (baseline) | Coach/staff recruiting grades only (0 spend) |
| **`coachRelationship`** (live) | Baseline + spent-point attention curve + visit completed + promise offered |
| **`proximity`** | Tier table above |
| **`campusLife`** | Average (or min) of letter grades on the recruit’s campus-related dims → 0–1 |

### Spend → interest (points system)

Recruiting **points** buy **labor** ([action costs](#action-costs-design-lock)); they do **not** replace NIL $.

| Action | Interest channel |
|--------|------------------|
| DM / Phone / Send the house / Bonus points | `spendDelta` via `coachRelationship` (and small global attention) — deltas in `recruiting.actions.*.interestDelta` |
| Official visit | Larger `coachRelationship` + slight `campusLife` bump when visit completes |
| Scout their game | **No** direct interest (reveals grades / priorities) |
| Scholarship offer | Gate for soft commit; small relationship bump; enables promise picker |
| Assign NIL | Updates **`nil`** score only |

**Refunded** reservations (offer / visit / scout) do **not** reverse interest when points refund at week boundary — interest is about the **relationship state**, not the weekly ledger.

Soft/hard commit thresholds stay as today: interest + offer (+ NIL alignment) → soft; Signing Day → hard.

### Promises & trust

| Phase | Rule |
|-------|------|
| **At offer / soft commit** | Choosing a promise ([`player-loyalty-promises.md`](player-loyalty-promises.md#coach-promises)) → small **`promiseDelta`** on interest (especially if priority is `coach_bond` or `start_now` with a Minutes/Role promise). **0** recruiting points (existing lock). |
| **After hard sign** | Promise is **career-long** at school; **Kept / Broken** drives **loyalty**, portal intent, declare odds — **not** the pre-sign interest meter |
| **Broken promise** | **Do not** model as a recruiting-interest penalty on *unsigned* targets. Trust hit is **loyalty** post-enroll ([loyalty seed & drift](player-loyalty-promises.md#seed--drift)). Soft-commit **decommit** already has prestige rules — keep those; optional tiny interest floor reset on re-engage ([Open](#open-questions)) |
| **NIL underfund after sign** | Existing separate trust/loyalty path in game-design — out of scope for pre-sign weights |

### Feed into `signLikelihood`

Existing Recommended drivers stay; this model supplies a cleaner **`baselineInterest`** input.

| Driver (existing) | How this doc maps |
|-------------------|-------------------|
| Prestige vs recruit tier / Top 60 | Soft **floor**: even high `baselineInterest` cannot exceed Long shot if prestige gap > band (keep elite-hide rule on Recommended) |
| Home state / region | `proximity` factor |
| School grades on priority dims | `campusLife` (+ priority boost) |
| Roster need | Part of `playingTimeFit` **and** sort tie-break (unchanged) |
| NIL ask vs annual pool | Baseline `nil` capacity score |
| Coach + staff | Baseline `coachRelationship` |
| Competition proxy | **Post** interest: reduce `signLikelihood` when many higher-prestige schools share pipeline — **not** a 7th interest weight |

```
signLikelihoodRaw = baselineInterest
                    - prestigeGapPenalty
                    - competitionProxyPenalty
                    + rosterNeedBonus
→ badge via recruiting.recommended.bands
```

Refresh: class seed + week advance + when the user changes offer / NIL / board (existing). **Coach-point spend does not enter this formula.**

### Data model

**On recruit (class / portal pool record):**

| Field | Type | Notes |
|-------|------|-------|
| `topPriorities` | `enum[1..2]` | From priority id table |
| `homeState` | string | Existing geography |
| `nilAskUsd` | int | Existing |
| `stars` | 2–5 | Existing (no 1★ band in HS/JUCO/portal pools) |
| `prioritiesRevealed` | bool | After scout threshold or first contact |

**Per (recruit × school) runtime:**

| Field | Type | Notes |
|-------|------|-------|
| `factorScores` | map factor→0..1 | Optional debug / AI |
| `effectiveWeights` | map factor→float | After priority + star shift |
| `baselineInterest` | 0–100 | Recommended input |
| `interest` | 0–100 | Sheet meter |
| `signLikelihood` | 0–1 or internal | Badge only in UI |
| `spendDelta` / `promiseDelta` | number | Live additives |

Loyalty `personality` tags may **bias** which priorities roll — do not duplicate as a second weight system.

### Worked examples

#### A — 2★ in-state, priorities `stay_home` + `start_now`, Mid-Major (prestige 40)

1. Base weights → boost `proximity` & `playingTimeFit` ×2 → renormalize.
2. Star shift: nudge toward minutes/home.
3. Scores: proximity **1.0**, playingTimeFit **0.8** (hole at his position), prestigeWinning **0.35**, nil capacity **0.7** (low ask), coach baseline **0.5**, campus **0.55**.
4. **`baselineInterest` ~ high 60s–70s** → badge **Likely / Possible** — Recommended surfaces him.
5. User spends DM + offer + meets ask → `interest` climbs; soft commit available.

#### B — 5★ remote, priority `follow_money`, Low-Major (prestige 22)

1. Weights skew hard to **`nil`** (+ 5★ nil shift).
2. Proximity **0.10**, prestigeWinning crushed by floor, nil capacity **~0** (ask ≫ pool).
3. **`baselineInterest` low** + prestige gap → **Long shot**; hidden from Recommended unless pipeline exception.
4. Even “Send the house” cannot fully erase prestige floor — points help `coachRelationship` but Recommended stay honest.

### Balance vs sim

| Belongs in **balance bundle** ([ADR-002](../docs/ADR-002-balance-ruleset-bundle.md) → [`balance-2026.json`](../assets/balance/balance-2026.json)) | Belongs in **sim tuning** ([ADR-003](../docs/ADR-003-sim-tuning-bundle.md)) |
|--------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------|
| `recruiting.interest.baseWeights` | **Nothing here** — possession pace/fouls/shots only |
| `recruiting.interest.priorityMultiplier` | |
| `recruiting.interest.starTierShifts` | |
| `recruiting.interest.proximityScores` | |
| `recruiting.interest.factorCurves` / action `interestDelta`s | |
| `recruiting.recommended.bands` + prestige–star floor (already in [Open / TBD](#open--tbd)) | |
| Existing `nil.interestNilRatioExponent`, `nil.askByStarBand`, `nil.maxOfferMultiplierOfAsk`, prestige tiers, action costs | |

Planning docs keep **behavior**; JSON keeps **numbers**. When they diverge, **JSON wins** (ADR-002). The `recruiting.interest.*` keys above are **proposals** — not added to the bundle by this spec.

### Tuning knobs

| Knob | Default proposal | Effect |
|------|------------------|--------|
| `baseWeights.*` | table above | Global feel |
| `priorityMultiplier` | **2.0** | How spiky personalities feel |
| `starTierShifts` | table above | 5★ money vs 2★ home/minutes |
| `proximityScores` | 1.0 / 0.65 / 0.35 / 0.10 | Pipeline strength |
| Action `interestDelta` | TBD per action | Points → meter responsiveness |
| Prestige–star floor | TBD | Protects Recommended honesty |
| `competitionProxy` strength | TBD | Crowded boards |
| Promise interest bump | small | Pitch flavor without points cost |

Freeze with dynasty via existing `balanceRulesetId` / `balanceSchemaVersion`.

### Conflicts / non-overrides

| Existing lock | This spec |
|---------------|-----------|
| Interest factors listed, weights “tunable” | Supplies **proposed** weights — not a silent change to other locks |
| `signLikelihood` at **zero spend** | **Coach points spend** affects **`interest` only**; baseline/Recommended ignore spend |
| Promise = **0 pts**, one per tenure, locks on hard commit | Promise may bump interest at offer; **broken** promise → **loyalty**, not pre-sign interest |
| NIL ask / 3× cap / annual pool | Unchanged; weights only change **sensitivity** |
| Prestige tier → **weekly points pool** | Unchanged — pool size ≠ interest weight |
| Facilities grades → prestige composite | Still true; `campusLife` uses grades **directly** for interest, prestige stays its own factor |
| AI uses same Recommended rules | AI should use this same weight pipeline |

### Open questions

| # | Question | Suggestion |
|---|----------|------------|
| 1 | Portal: new `topPriorities` or carry from prior school personality? | Re-roll 1 priority biased by entry drivers (minutes/broken promise) |
| 2 | Exact scout depth to reveal priorities? | Same threshold as loyalty tendency letter |
| 3 | Soft-commit decommit → interest reset? | Drop to `baselineInterest + small residual` |
| 4 | Overpaying NIL (up to 3×) — interest > full credit? | Cap at 1.0 factor score; surplus is roster flexibility only |
| 5 | Should `chase_title` read **conference tourney** success for mid-majors or only NCBA/NCI? | Include conf titles + national — mid-majors need a winning story |
| 6 | Merge this section into `recruiting-engine.md` § Interest & offers, or keep a companion file under `planning/`? | **Resolved** — merged here, with short pointers from loyalty, game-design NIL, and ADR-002 |

---

## Recruiting UI

### Entry

**Hamburger → Recruiting** (always when cycle open) plus **optional dashboard tasks** ([In-season dashboard tasks](#in-season-dashboard-tasks)). User commits → **in-app feed** ([`notifications.md`](notifications.md)); league headlines → **News** only; tasks carry the **action** entry.

**Default landing:** **Recommended** tab ([below](#recommended-tab-default)) — not All / national lists.

### Tabs

| Tab | Default? | Content |
|-----|----------|---------|
| **Recommended** | **Yes** — hub opens here | Prospects **most likely to sign** with your program ([Recommended tab](#recommended-tab-default)) |
| **All (HS + JUCO)** | | Full combined pool, filters |
| **High school** | | HS filter only |
| **Junior college** | | JUCO filter only |
| **Transfer portal** | Opens on **Recommended** (portal) | Portal pool; portal has its own recommended sort |
| **Board / Targets** | | User watchlist |
| **Committed** | | Your class |
| **Lost** | | Decommits / chose elsewhere |

### Recommended tab (default)

**Design lock:** Help every program — especially **low prestige** — find **winnable** targets instead of scrolling **5★** / **Top 60** prospects they will almost never land.

| UI | Detail |
|----|--------|
| **Placement** | First tab left; **selected** whenever user opens Recruiting from hamburger, dashboard task, or deep link |
| **List size** | **~50** rows (tunable); paginate or “Show more reaches” for browse mode |
| **Row** | Name, stars, position, home state, letter grades (as scouted), **`signLikelihood` badge**, optional “In state” / “Position need” chips |
| **Badges** | **Likely** (high), **Possible**, **Reach**, **Long shot** — from `signLikelihood` bands in balance JSON |
| **Top 60 filter** | National **#1–#5** (and similar elite band) appear on **Recommended** only when `signLikelihood` clears a minimum **or** recruit is **in-state / adjoining pipeline** with strong geography lift — otherwise **hidden** on Recommended (still findable under **All** as explicit **reach**) |

#### `signLikelihood` (per recruit × your school)

Computed when class seeds and **refreshed** on week advance (and when user changes offers / NIL / board). **Not** shown as 0–99 — only the **badge** + sort order.

**Inputs** (same family as [Interest & offers](#interest--offers), evaluated at **zero user spend**). Point spend moves the sheet meter only — see [Two surfaces](#two-surfaces) and [Feed into `signLikelihood`](#feed-into-signlikelihood):

| Driver | Effect |
|--------|--------|
| **`programPrestige` (0–99) vs recruit tier** | Star band + national percentile / Top 60 rank set a **prestige floor**; gap penalizes likelihood (e.g. **prestige &lt; 40** → top national recruits are **long shots** unless geography saves them) |
| **Home state / region** | In-state & pipeline states boost; OOT low-prestige matchups penalize |
| **School grade letters** on recruit priority dims | Facilities-first recruit × weak Facilities → lower |
| **Roster need** | Open scholarship + position hole → bump |
| **NIL ask vs your annual pool** | High ask without means → penalize |
| **Coach + staff recruiting** | Shifts efficiency, not star ceiling |
| **Competition proxy** | How many higher-prestige schools share state/region on same archetype (lightweight estimate — no full sim) |

**Sort:** `signLikelihood` **desc**, then **position need**, then **in-state**, then stars.

**Low-prestige UX copy (examples):** empty-state never shames — e.g. “**12 likely targets** in your pipeline · **3** in-state **4★**” with links to filters.

#### Portal Recommended

Transfer **portal** sub-tab uses the **same** default tab pattern: **Recommended** (entrants you can realistically land), separate sort pool from HS/JUCO.

#### Product ties

| System | Use |
|--------|-----|
| **Dashboard tasks** | Deep link → **Recommended** (not raw All) |
| **Auto-maintain board** | Prefer top of **Recommended** within user caps ([`game-design.md`](game-design.md)) |
| **AI user school** on sim-week | Spend drip on **Recommended** order unless user set a watchlist |

### Filters & actions

| Filter | |
|--------|---|
| Position, stars, **home state**, HS vs JUCO, archetype fit, NIL ask tier | |

| Action | Spends | See |
|--------|--------|-----|
| DM / Phone call / Send the house | Recruiting points | [Action costs](#action-costs-design-lock) |
| Scholarship offer (+ optional **promise**) | Recruiting points (refunded) | [`player-loyalty-promises.md`](player-loyalty-promises.md) |
| Scout their game / Official visit | Recruiting points (refunded) | [Limits](#action-costs-design-lock) |
| Bonus points | Remaining weekly points | |
| **Assign NIL** | **NIL $** only; max **3× ask**, capped by **remaining** budget | Not points — [`game-design.md`](game-design.md#nil-budget-workflow-design-lock) |
| Drop target / revoke offer | — | Ends refunded reservation |

Tap recruit → **recruit sheet** (preview): stars, letters, **NIL ask**, interest, actions — **not** full player card until commit.

### NIL Budget strip (recruiting hub)

| UI | Detail |
|----|--------|
| **Placement** | Top of Recruiting hub (all tabs); mirrors **game-start** **NIL budget** preview |
| **Copy** | `NIL Budget: $X remaining of $Y` |
| **Drill-down** | Optional sheet: committed / reserved breakdown by player |

Per recruit **Assign NIL** lives on the recruit sheet (not a separate economy screen).

---

## National rankings (Top 60)

**Not team polls:** **Media / Coaches T25** ranks **programs** ([`game-frame.md`](game-frame.md#rankings-on-dashboard)). **Recruit rankings** are a **national player list** — **not** cropped to 25.

| List | Source | When |
|------|--------|------|
| **Top 60 recruits** | Best **60** in the seeded **HS + JUCO** class by hidden rating (portal excluded) | **Fixed at preseason seed** — show **#1–#60** complete (no “top 25 only” UI) |
| **News hub → Recruits** | Same **full Top 60** table | All regular season + offseason |
| **Recruiting hub → All** | **Entire** national pool (thousands) — stars, filters, scout; **Top 60** badge on ranked players | Cycle open |

Distinct from **Top 60 draft** pool (in-season / pro-day dynamic) — recruit Top 60 is **incoming class** oriented.

**Portal Top 40** — ranked list inside the portal sub-tab (pool-local percentiles); **never** mixed into HS/JUCO Top 60.

---

## AI recruiting

**Design lock:** Non-user schools use the **same rules as the user** — prestige-tier **weekly points**, **annual NIL $** ([`game-design.md`](game-design.md#annual-collective-pool-design-lock)), the same [interest weight pipeline](#interest--offers), and the same **`signLikelihood` / Recommended** ordering ([Recommended tab](#recommended-tab-default)). There is no separate “AI cheat list.”

### Visibility (player)

| Rule | Detail |
|------|--------|
| **Your board only** | **Recruiting hub** (Recommended, All, Board, etc.) is **user program only** — no UI to open another school’s targets, watchlist, or spend |
| **Other programs** | AI recruiting runs **off-screen** on week advance; you learn outcomes via **News** (commits, decommits, headlines), not rival boards |
| **School hub (others)** | View opponent programs — roster, schedule, stats — **no** Recruiting tab when scouting another school ([`game-frame.md`](game-frame.md#school-hub)) |

### Per-school Recommended (AI)

Each program each week:

1. Build **Recommended** ordering for HS/JUCO (and portal sub-tab when open) from that school’s prestige, pipeline, roster holes, and `nilAsk` vs pool.
2. Respect **realistic capacity** from prestige tier ([weekly pool table](#prestige-tier-weekly-pool-design-lock)) — e.g. Low-Major focuses on **2–3** core targets, Blue Blood runs a **full national** board.
3. Spend **weekly points** on **offer → DM / call → house → bonus** down the list; **scout game / visit** when windows allow; allocate **NIL $** when interest stalls on high asks.

### When it runs

| Trigger | Behavior |
|---------|----------|
| **Week advance** (league) | Every non-user school runs one **Recommended pass** for that week (after user confirm on sim-week) |
| **Between sessions** | Optional batch catch-up for schools not advanced yet — same logic |
| **Signing crunch** | Same loop; **1.5×** weekly points pool |
| **User sim-week** | User school uses **auto-maintain** or this same Recommended pass if player skipped the hub ([`game-frame.md`](game-frame.md#advance-week-confirmation-sim-week)) |

### Outputs

| Output | Rule |
|--------|------|
| **Commits** | Soft/hard commit rules identical to user |
| **News** | Headlines for blue-chip and upset commits league-wide |
| **Portal** | Entrants get a **fresh Recommended** pool for portal signers only |

AI uses the same [action costs](#action-costs-design-lock) and refund rules as the user.

---

## Commitments & signing

| Type | Rule |
|------|------|
| **Soft commit** | Reserves interest; can decommit (prestige hit) |
| **Hard commit** | Locks scholarship; NIL snapshot stored |
| **Signing** | **Signing Day** — offseason **week 6** after **R4** — binding for HS/JUCO |
| **Portal** | Commit when offer accepted; roster attach per portal ruleset |

After signing, recruits appear on **Committed** and (when appropriate) next season roster preview; uncommitted HS/JUCO remain in sim as open or walk-on fodder for AI.

---

## Walk-ons

Authoritative calendar: [`game-design.md`](game-design.md#walk-ons). Recruiting UX:

| Element | Behavior |
|---------|----------|
| **Entry** | In-app feed row after **Signing Day** (week 6) |
| **Board** | Small list (5–15) of **local** prospects; letter grades like HS pool |
| **Actions** | Offer walk-on spot (no scholarship); requires open roster slot |
| **Limit** | Max **2** new walk-ons per season |

---

## Cross-links

| From | To |
|------|-----|
| Seed class | News Top 60, Recruiting filters |
| Hard commit | Player card NIL + roster |
| Prestige | School hub grades, fundraising |
| Coach recruiting | [`coach-card.md`](coach-card.md) ratings |
| Archetype | [`player-archetypes.md`](player-archetypes.md) hidden tag |
| Offseason weeks 2–5 | Signing crunch (R1–R4); 1.5× tier weekly pool |
| Signing Day week 6 | Hard sign + rankings + position editor |
| Interest model | [Interest & offers](#interest--offers); NIL fit [`game-design.md`](game-design.md#interest--signing); promises [`player-loyalty-promises.md`](player-loyalty-promises.md#coach-promises) |
| Balance numbers | [ADR-002](../docs/ADR-002-balance-ruleset-bundle.md) — proposed `recruiting.interest.*` |

---

## Design locks (summary)

| Topic | Decision |
|-------|----------|
| **3★ / 2★** | **20–50%** = 3★, **50–100%** = 2★ (with 5★ / 4★ bands above) |
| **Recruiting point carryover** | **50%** of unused points to next week; rest expires |
| **Official visits** | **50 / 100 / 150** by distance; refunded; once per recruit; RS → R4 |
| **Action costs** | DM 50, call 100, house 500, offer 150, scout game 50 — see [table](#action-costs-design-lock) |
| **Top 60 (recruits)** | **Fixed** after class seed; UI shows **#1–#60**, not top-25 crop |
| **AI recruiting visibility** | **Off-screen** — user sees **own** hub only |
| **HS class size** | **Uniform random 2,000–4,000** per season |
| **Year-long cycle** | Spend preseason → **Signing Day** (OS week 6); **optional** in-season dashboard tasks |
| **Sim week** | Advance may auto-sim games + AI user recruiting after confirmation — [`game-frame.md`](game-frame.md#advance-week-confirmation-sim-week) |
| **Weekly recruiting pool** | **Fixed** per cycle from prestige tier table; recap sets **next** cycle |
| **Signing crunch** | **1.5×** tier weekly pool offseason weeks 2–5 |
| **Recommended tab** | **Default** hub tab; `signLikelihood` sort; elite reaches hidden unless realistic |
| **Interest model** | Six factors; proposed weights in [Base weights](#base-weights). `signLikelihood` is **zero spend**. Broken promises are loyalty hits. Points pool ≠ interest weights. NIL bands and **3×** cap unchanged |

## Open / TBD

| Topic | Notes |
|-------|--------|
| Tier interpolation edge cases | `balance-2026.json` → `recruiting.prestigeTiers` |
| `signLikelihood` bands & prestige–star curve | Tune in balance bundle `recruiting.recommended` |
| Interest deltas per action | `recruiting.actions.*` in balance JSON |
| Interest factor weights | **Proposed** — [Base weights](#base-weights); not written into `balance-2026.json` by this spec |
| Interest open questions | [Open questions](#open-questions) |
| ~~Offseason frame UI~~ | **Resolved** — tasks in [`offseason.md`](offseason.md) + shell in [`game-frame.md`](game-frame.md) |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Initial recruiting engine from user spec |
| 2025-11-08 | Design locks: star bands, 50% carryover, no visits, fixed Top 60, uniform HS count |
| 2025-11-08 | Rename v1 locks section → design locks; scrub v1 wording |
| 2025-11-08 | Walk-ons + Portal Top 40 full product specs |
| 2025-11-08 | HS/JUCO seed in preseason; Top 60 during regular season |
| 2025-11-08 | Recruiting pipeline uses homeState; schools also store homeCity |
| 2026-11-09 | Year-long recruiting cycle; signing crunch weeks |
| 2026-11-09 | Optional in-season recruiting dashboard tasks; sim-week advance confirmation |
| 2026-11-09 | Recommended tab default; signLikelihood for low-prestige fit |
| 2026-11-09 | Prestige tier weekly recruiting pool; fixed per cycle; recap sets next year |
| 2026-11-09 | Action cost table, refunded reservations, official visits, bonus points, Signing Day week 6 |
| 2026-11-09 | Recruit interest model: factor weights, priorities, star shifts, `signLikelihood` feed |
