# Recruiting engine

**Context:** Offseason **4-week** recruiting period; HS/JUCO pools seeded earlier in offseason so national boards can publish.  
**Companion:** Offseason calendar [`game-design.md`](game-design.md#offseason-authoritative-calendar), NIL [`game-design.md`](game-design.md#nil--program-economy), player display [`player-cards.md`](player-cards.md), archetypes [`player-archetypes.md`](player-archetypes.md).  
**Last updated:** 2025-11-08

---

## Contents

1. [Timeline & seeding](#timeline--seeding)
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
12. [Cross-links](#cross-links)
13. [Open / TBD](#open--tbd)

---

## Timeline & seeding

### Class seeding (HS + JUCO)

| When | What |
|------|------|
| **Early offseason** (before recruiting week 1) | Generate **HS** and **JUCO** pools for the incoming class; roll hidden true ratings; assign **stars** per pool rules; build **national recruit rankings** (feeds News **Top 60** and in-engine boards) |

Seeding in offseason is what allows **Top 60 recruit rankings** to exist before the player spends recruiting weeks (preview in News hub, filters on board).

### Transfer portal

| When | What |
|------|------|
| **Offseason transfer window** (established window per ruleset — aligns with portal **cycle 1** in game-design week 1) | Players enter portal; stipends/scholarships clear on departure |
| **Recruiting week 1** | Portal entrants become **recruitable** on the **Transfer Portal** sub-tab (not mixed into HS/JUCO list) |

Portal star bands use the **portal pool only** (see [Star distribution](#star-distribution)); portal 5★ count is **not** reduced because HS had forty 5★s.

### Four-week recruiting period

| Recruiting week | Offseason anchor (v1) | Highlights |
|-----------------|----------------------|------------|
| **1** | Offseason week 6 | Portal class **opens** for offers; **NBA draft results** post (opens scholarships); HS/JUCO board live |
| **2** | Offseason week 7 | Full spend of weekly recruiting points / NIL |
| **3** | Offseason week 8 | Peak visits & commits |
| **4** | Offseason week 9 | Signing close; hard commits lock |

**No in-season HS/JUCO recruiting window** in v1 (portal in-season remains ruleset-toggled in game-design).

User advances **week** (or day, if enabled) inside recruiting like other offseason phases.

---

## Pools & scale

Each **season**, generate independent pools:

| Pool | Count | Mixed in UI? |
|------|-------|----------------|
| **High school** | **Random integer 2,000–4,000** (uniform or weighted — tunable) | Yes — single **HS + JUCO** board |
| **Junior college** | **+1,500** (fixed add-on) | Same board as HS |
| **Transfer portal** | **All entrants** that window (varies by year/sim) | **Separate sub-tab** only |

**Walk-ons** (local tryouts) optional v1 — low-prestige schools; see game-design if enabled.

Regional flavor (home state bias on interest) optional; national pool is primary.

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
| **Reveal** | Scouting actions / visits / commits raise fidelity (e.g. `B+` → firmer band, then post-commit full [`player-cards.md`](player-cards.md) ratings next season) |

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
| **Facilities** | Visit wow, recruit interest baseline |
| **Academics** | APR/GPA story, some recruit tags |
| **Conference prestige** | National schedule / brand |
| **Program prestige** (historical success) | Baseline interest |
| **Arena / recruiting lounge** | Visit efficiency |

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
| **Assistant recruiting grades** | Staff grade → visit efficiency |

---

## Resources: recruiting points & NIL

Two spend tracks each **recruiting week**:

| Resource | Use |
|----------|-----|
| **Recruiting points** | Calls, scouting upgrades, visits, persistent interest gains |
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

Each of **4 recruiting weeks**, user receives:

- **Recruiting points** to spend (unused points may **expire** at week end or **bank** partially — **TBD** in [Open](#open--tbd))
- Ability to assign / adjust **NIL dollars** toward targets (subject to annual NIL cap)

Coach upgrades purchased during offseason may increase **week 1** totals or per-week drip.

---

## Interest & offers

**Interest meter (0–100)** per target, influenced by:

| Factor | Weight (tunable) |
|--------|----------------|
| **programPrestige** (0–99) | Baseline |
| **School grade letters** on recruit’s priority dimensions | e.g. facilities-focused recruit cares about Facilities grade |
| **NIL offered** vs recruit **valuation** | Underfunding stalls interest |
| **Coach recruiting** + staff | Charisma / visit efficiency |
| **Distance / conference** | Recruit preferences |
| **Playing time pitch** | Optional action |
| **Recent success** | Tourney run, poll rank |

**Recruiting points** spent on: scout deeper (sharpen letters), call, visit, scholarship offer, priority bump.

**NIL** committed at offer time writes toward signing number on [`player-cards.md`](player-cards.md) Overview when hard commit lands.

---

## Recruiting UI

### Entry

Offseason shell (dedicated frame TBD) or hub from dashboard notification — **Recruiting** opens 4-week board.

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
| Position, stars, state/region, HS vs JUCO, archetype fit, NIL ask tier | |

| Action | Spends |
|--------|--------|
| Scout | Recruiting points |
| Call | Points |
| Visit (unofficial / official) | Points (+ slot if official — **TBD**) |
| NIL bump | NIL dollars |
| Scholarship offer | Points + slot |
| Drop | — |

Tap recruit → **recruit sheet** (preview): stars, letters, NIL ask, interest, actions — **not** full player card until commit.

---

## National rankings (Top 60)

| List | Source | When |
|------|--------|------|
| **Top 60 recruits** | Hidden ratings across **seeded HS + JUCO** (portal excluded until entered) | After seeding; updates lightly as commits reshuffle perception (**TBD**) |
| **News hub → Recruits** | Same Top 60 reference | [`game-frame.md`](game-frame.md) |

Distinct from **Top 60 draft** pool (in-season / pro-day dynamic) — recruit Top 60 is **incoming class** oriented.

Portal entrants can appear on a **portal rank** list inside portal tab (pool-local percentiles), not mixed into HS/JUCO Top 60 unless design later adds “reclass” row (**defer**).

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

## Cross-links

| From | To |
|------|-----|
| Seed class | News Top 60, Recruiting filters |
| Hard commit | Player card NIL + roster |
| Prestige | School hub grades, fundraising |
| Coach recruiting | [`coach-card.md`](coach-card.md) ratings |
| Archetype | [`player-archetypes.md`](player-archetypes.md) hidden tag |
| Offseason week 6–9 | This 4-week window |

---

## Open / TBD

Items not locked in this pass — confirm when you want them pinned:

| Topic | Question |
|-------|----------|
| **3★ / 2★ bands** | Confirm 20–50% = 3★, 50–100% = 2★ or adjust |
| **Unused recruiting points** | Expire weekly vs bank to next week |
| **Official visit slots** | Per-week cap from game-design vs simplified points-only |
| **Top 60 updates** | Static after seed vs bump on scouting “breakout” |
| **HS pool RNG** | Uniform 2K–4K vs prestige-weighted class size |
| **Offseason frame UI** | Full shell doc (companion to `game-frame.md`) |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Initial recruiting engine from user spec |
