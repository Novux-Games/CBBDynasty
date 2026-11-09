# Recruiting engine

**Context:** HS/JUCO pools **seeded in preseason** (Top 60 all season); **year-long recruiting cycle** (spend preseason → signing at offseason week 5); **in-season week advance = games only**.  
**Companion:** Offseason calendar [`offseason.md`](offseason.md), NIL [`game-design.md`](game-design.md#nil--program-economy), player display [`player-cards.md`](player-cards.md), archetypes [`player-archetypes.md`](player-archetypes.md).  
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
| **Preseason → Signing (offseason week 5)** | Full **Recruiting** hub — scout, spend points/NIL, soft commits in-season; **hard sign** at cycle close |
| **After signing close** | Board read-only for enrolled class; next class seeds next **preseason** |

**New dynasty:** **Season 1** runs the same **preseason seed** before the first exhibitions so **Top 60** is available from tipoff of year one.

---

## Year-long recruiting cycle (design lock)

**Goal:** Recruiting feels like a **full-year job** (class revealed in preseason, relationships built during the season, signing after the year) **without** slowing week-to-week play.

### Speed contract (with [`game-frame.md`](game-frame.md))

| Rule | Detail |
|------|--------|
| **In-season advance gate** | **Scheduled games only** — not recruiting |
| **Recruiting is optional each week** | Skip the hub for weeks; still advance after games |
| **AI on week advance** | Non-user schools spend their weekly drip and move interest/commits when you advance |
| **Automation** | Optional **auto-maintain board** (game-design) burns drip on watchlist — user sets caps |

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
| **Cycle closes** | Offseason **week 5** (**Recruiting week 4** — signing) |
| **Portal sub-cycle** | Entrants from offseason **week 1**; portal sub-tab spend when window is open ([`offseason.md`](offseason.md)) — same hub, not a second app |

### Weekly economy (replaces “4 weeks only” allowance)

- **Annual recruiting-point budget** set at cycle open (prestige, grades, last year’s results — same drivers as [Budget setup](#budget-setup-annual)).
- **Every week advance** while the cycle is open (preseason week through offseason week 5):
  - Grant **weekly recruiting-point drip** (total over the year ≈ former 4-week sprint totals).
  - **50% carryover** of unspent points to the next week (unchanged).
  - **NIL** remains **annual pool**; allocate anytime before hard sign.
- **Signing crunch:** Offseason weeks **2–5** apply a **multiplier** on the weekly drip (e.g. **1.5×**) so the natural “closing period” still matters without being the only time you can act.

**Tunable default (starting point for balance JSON):**

| Segment | Weeks (approx.) | Drip weight |
|---------|-----------------|-------------|
| Preseason | 1 | 1.0× |
| Exhibition + RS + byes | ~35 | 1.0× |
| Postseason + End of Season | ~8+1 | 1.0× (no games ≠ no drip) |
| Offseason W1 | 1 | 0.5× (retention focus; portal opens) |
| Offseason W2–W5 | 4 | 1.5× crunch |

User who touches recruiting **3–5 min every other week** keeps pace; user who ignores it until crunch can still close in offseason W2–W5 with crunch multiplier + banked carryover (if any).

### Commits

| Type | When |
|------|------|
| **Soft commit** | Anytime interest + offer thresholds met (including **in-season**) |
| **Hard commit / sign** | **Signing close** at end of offseason **week 5** (binding; NIL snapshot) |
| **Decommit** | Allowed before hard sign; prestige hit per existing rules |

Headlines in **News** for blue-chip commits league-wide; your targets surface as dashboard **notifications**, not **advance blockers**.

### What does *not* change

- Star bands, pool sizes, no visits, Top 60 fixed at seed, combined HS/JUCO board, portal sub-tab, walk-ons after main window.
- **Four recruiting week indices (R1–R4)** still map to **offseason weeks 2–5** for copy, NIL peaks, and crunch multiplier — they are **not** the only weeks you can spend.

### Transfer portal

| When | What |
|------|------|
| **Offseason transfer window** (established window per ruleset — aligns with portal **cycle 1** in game-design week 1) | Players enter portal; stipends/scholarships clear on departure |
| **Recruiting week 1** | Portal entrants become **recruitable** on the **Transfer Portal** sub-tab (not mixed into HS/JUCO list) |

Portal star bands use the **portal pool only** (see [Star distribution](#star-distribution)); portal 5★ count is **not** reduced because HS had forty 5★s.

### Signing period (R1–R4)

**Naming:** R1–R4 = offseason weeks **2–5** — **signing crunch** (1.5× drip), not the only weeks you can spend ([year-long cycle](#year-long-recruiting-cycle-design-lock)).

| Recruiting week | Offseason anchor | Highlights |
|-----------------|----------------------|------------|
| **R1** | Offseason **week 2** | Portal **sub-tab** peak; **NBA draft results**; scholarships open after draft |
| **R2** | Offseason **week 3** | Peak offers; NIL pushes |
| **R3** | Offseason **week 4** | Commit wave; head-to-head resolution |
| **R4** | Offseason **week 5** | **Signing close**; hard commits lock |

Week 1 offseason = **retention** on your roster ([`offseason.md`](offseason.md#week-1--players-leaving)). During **in-season**, recruiting **never** gates **Advance week** ([`game-frame.md`](game-frame.md#dashboard-task-gate)).

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
| **NIL valuation** | Recruit’s **ask** in abstract dollars or deal points (visible — drives offer UX) |

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

Mapping A–F → points is data-driven JSON; **better letter → higher prestige** and **higher weekly recruiting points cap**.

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
| **Recruiting points** | Calls, scouting upgrades, persistent interest gains (no campus visit system) |
| **NIL dollars** (or **deal points** — one global format) | Allocated offers toward a recruit’s **valuation** / ask |

### Budget setup (annual)

Annual pools set at **recruiting phase start** (and previewable when class seeds):

| Driver | Behavior |
|--------|----------|
| **School grades → prestige** | Sets **base** recruiting points / NIL pool |
| **Prior season performance** | Wins, tournament, fundraising — **raises or lowers** next year’s budget vs baseline |
| **Academic / facility tiers** | Multipliers per game-design formulas (recruiting lounge, grade multiplier) |
| **Retention split** | Optional % of NIL reserved for returning players before recruit offers (game-design soft cap) |

### Weekly allowance

See [Year-long recruiting cycle](#year-long-recruiting-cycle-design-lock): drip **each week advance** while the cycle is open, with **signing crunch** multiplier in offseason weeks 2–5.

**Carryover (design lock):** at week end, **50%** of **unspent recruiting points** roll into the next week’s pool (rounded down). The other 50% expires. NIL unspent does **not** roll week-to-week unless tied to an annual pool remainder rule in NIL doc (annual pool cap only).

Coach upgrades may increase **base drip** or annual pool at cycle open.

---

## Interest & offers

**Interest meter (0–100)** per target, influenced by:

| Factor | Weight (tunable) |
|--------|----------------|
| **programPrestige** (0–99) | Baseline |
| **School grade letters** on recruit’s priority dimensions | e.g. facilities-focused recruit cares about Facilities grade |
| **NIL offered** vs recruit **valuation** | Underfunding stalls interest |
| **Coach recruiting** + staff | Charisma / point efficiency |
| **Home state / region** | In-state & nearby-state pipeline vs program `homeState` |
| **Playing time pitch** | Optional action |
| **Recent success** | Tourney run, NET / Media rank |

**Recruiting points** spent on: scout deeper (sharpen letters), call, scholarship offer, priority bump. **No campus visits** (no official/unofficial visit slots or visit actions).

**NIL** committed at offer time writes toward signing number on [`player-cards.md`](player-cards.md) Overview when hard commit lands.

---

## Recruiting UI

### Entry

**Hamburger → Recruiting** (same dynasty shell — [`game-frame.md`](game-frame.md)). Optional **dashboard notification** when a watchlist recruit commits; **never** an advance blocker in-season.

### Tabs

| Tab | Content |
|-----|---------|
| **All (HS + JUCO)** | Combined pool, filters |
| **High school** | HS filter only |
| **Junior college** | JUCO filter only |
| **Transfer portal** | Portal-only sub-tab |
| **Board / Targets** | User watchlist |
| **Committed** | Your class |
| **Lost** | Decommits / chose elsewhere |

### Filters & actions

| Filter | |
|--------|---|
| Position, stars, **home state**, HS vs JUCO, archetype fit, NIL ask tier | |

| Action | Spends |
|--------|--------|
| Scout | Recruiting points |
| Call | Points |
| NIL bump | NIL dollars |
| Scholarship offer | Points |
| Drop | — |

Tap recruit → **recruit sheet** (preview): stars, letters, NIL ask, interest, actions — **not** full player card until commit.

---

## National rankings (Top 60)

| List | Source | When |
|------|--------|------|
| **Top 60 recruits** | Hidden ratings across **seeded HS + JUCO** (portal excluded) | **Fixed at preseason seed** — visible **all regular season** + offseason; order **does not** update during the 4 recruiting weeks |
| **News hub → Recruits** | Same Top 60 reference | [`game-frame.md`](game-frame.md) — in-season |

Distinct from **Top 60 draft** pool (in-season / pro-day dynamic) — recruit Top 60 is **incoming class** oriented.

**Portal Top 40** — ranked list inside the portal sub-tab (pool-local percentiles); **never** mixed into HS/JUCO Top 60.

---

## AI recruiting

- AI programs spend **same** recruiting points + NIL rules from prestige/budget.
- Prioritize **stars**, **position need**, **archetype**, **NIL** for high asks.
- User sees **headline commits** in News for blue-chips.
- Portal AI resolves across weeks 1–4 after entrants list stabilizes.

Batch sim between user sessions for non-user offers/commits.

---

## Commitments & signing

| Type | Rule |
|------|------|
| **Soft commit** | Reserves interest; can decommit (prestige hit) |
| **Hard commit** | Locks scholarship; NIL snapshot stored |
| **Signing** | End of **recruiting week 4** — binding for HS/JUCO |
| **Portal** | Commit when offer accepted; roster attach per portal ruleset |

After signing, recruits appear on **Committed** and (when appropriate) next season roster preview; uncommitted HS/JUCO remain in sim as open or walk-on fodder for AI.

---

## Walk-ons

Authoritative calendar: [`game-design.md`](game-design.md#walk-ons). Recruiting UX:

| Element | Behavior |
|---------|----------|
| **Entry** | Offseason notification after **recruiting week 4** |
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
| Offseason weeks 2–5 | Signing crunch (R1–R4); 1.5× weekly drip |

---

## Design locks (summary)

| Topic | Decision |
|-------|----------|
| **3★ / 2★** | **20–50%** = 3★, **50–100%** = 2★ (with 5★ / 4★ bands above) |
| **Recruiting point carryover** | **50%** of unused points to next week; rest expires |
| **Visits** | **None** — points-only actions |
| **Top 60 (recruits)** | **Fixed** after class seed |
| **HS class size** | **Uniform random 2,000–4,000** per season |
| **Year-long cycle** | Spend preseason → signing (OS week 5); **games-only** advance gate in-season |
| **Signing crunch** | **1.5×** weekly drip offseason weeks 2–5 (tunable in balance bundle) |

## Open / TBD

| Topic | Notes |
|-------|--------|
| Drip weights & annual point totals | Tune in `balance-2026.json` when loaders ship |
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
| 2026-11-09 | Year-long recruiting cycle; in-season games-only advance gate; signing crunch weeks |
