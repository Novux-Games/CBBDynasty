# Recruiting engine

**Context:** HS/JUCO pools **seeded in preseason** (Top 60 all season); **year-long recruiting cycle**; **optional** in-season recruiting **dashboard tasks**; **sim-week** advance when user skips games/recruiting ([`game-frame.md`](game-frame.md#advance-week-confirmation-sim-week)).  
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
| **NIL dollars** (or **deal points** — one global format) | **Money on the table** toward a recruit’s **valuation** / ask — **not** paid from the weekly points pool |

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
| **Retention split** | Optional % of NIL reserved for returning players before recruit offers (game-design soft cap) |

### Carryover

**Carryover (design lock):** at week end, **50%** of **unspent recruiting points** roll into the next week’s pool (rounded down). The other 50% expires. NIL unspent does **not** roll week-to-week unless tied to an annual pool remainder rule in NIL doc (annual pool cap only).

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
| **Send the house / bonus points** | Large or remainder **point** actions ([Action costs](#action-costs-design-lock)) |
| **Recent success** | Tourney run, NET / Media rank |

**NIL** committed at offer time writes toward signing number on [`player-cards.md`](player-cards.md) Overview when hard commit lands.

---

## Recruiting UI

### Entry

**Hamburger → Recruiting** (always when cycle open) plus **optional dashboard tasks** ([In-season dashboard tasks](#in-season-dashboard-tasks)). Notifications duplicate headlines; tasks carry the **action** entry.

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

**Inputs** (same family as [Interest & offers](#interest--offers), evaluated at **zero user spend** unless noted):

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
| NIL allocation | **NIL $** only | Not points |
| Drop target / revoke offer | — | Ends refunded reservation |

Tap recruit → **recruit sheet** (preview): stars, letters, NIL ask, interest, actions — **not** full player card until commit.

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

**Design lock:** Non-user schools use the **same rules as the user** — prestige-tier **weekly points**, **annual NIL $** ([`game-design.md`](game-design.md#annual-collective-pool-design-lock)), and the same **`signLikelihood` / Recommended** ordering ([Recommended tab](#recommended-tab-default)). There is no separate “AI cheat list.”

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
| **Entry** | Offseason notification after **Signing Day** (week 6) |
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

## Open / TBD

| Topic | Notes |
|-------|--------|
| Tier interpolation edge cases | `balance-2026.json` → `recruiting.prestigeTiers` |
| `signLikelihood` bands & prestige–star curve | Tune in balance bundle `recruiting.recommended` |
| Interest deltas per action | `recruiting.actions.*` in balance JSON |
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
