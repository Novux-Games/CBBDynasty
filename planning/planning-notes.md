# Planning tracker

Quick “is this decided?” pointers. **Full specs:** [`README.md`](README.md) · **GDD spine:** [`game-design.md`](game-design.md) · **Build order:** [`BUILD_CHECKLIST.md`](../docs/BUILD_CHECKLIST.md)

---

## Documentation rules

### Full product, not “v1”

Planning and player-facing copy describe **the game we are building**, not a staged MVP. Do **not** write “v1”, “not in v1”, “v2 feature”, or “ships later” for product scope. Say **design lock**, **excluded**, or name the behavior. Technical version fields (`balanceSchemaVersion`, `packVersion`, import format revision) stay as-is.

### Build checklist sync

Every **Done** topic below must map to step(s) in [`BUILD_CHECKLIST.md#locked-specs--build-steps-index`](../docs/BUILD_CHECKLIST.md#locked-specs--build-steps-index). Update checklist in the **same commit** as the planning change.

### App updates must not break saves

| Requirement | Meaning |
|-------------|---------|
| **Load after update** | Occupied slots open; calendar + metadata remain coherent (repair only when already corrupt — checklist 2.5) |
| **Room changes** | Every schema bump ships a **Migration** (or tested destructive path only for empty slots — never for occupied rows without migration) |
| **Tuning / balance** | Existing dynasties keep **pinned** balance + sim revisions from create ([ADR-002](../docs/ADR-002-balance-ruleset-bundle.md)); app updates do **not** silently re-roll NET or sim math mid-career |
| **Shipped asset retention** | While any save references `balanceSchemaVersion` / `simSchemaVersion` **N**, the APK keeps the JSON (or loader mapping) needed to load **N** |
| **Forward-only features** | New columns default safely; new systems attach without rewriting history |
| **No player copy** | Migrations are silent — no “save version upgraded” UI unless we add optional release notes |

**Not promised:** hand-edited exports, slots already broken before update, or infinite support for ancient `dynastySchemaVersion` without export — see [ADR-001](../docs/ADR-001-dynasty-save-backend.md#app-updates--save-compatibility).

**CI (target):** migration tests + load fixture save across version bump before release.

---

## Settled topics (by area)

### Core, shell & time

| Topic | Status | Doc |
|-------|--------|-----|
| Dynasty shell (all phases) | **Done** — one chrome, dashboard task gate, week advance only | [`game-frame.md`](game-frame.md) |
| App shell (studio / splash) | **Done** | [`app-shell.md`](app-shell.md) |
| Game start / new dynasty | **Done** | [`game-start.md`](game-start.md) |
| Time system (weeks/byes) | **Done** | [`time-system.md`](time-system.md) |
| Offseason calendar | **Done** | [`offseason.md`](offseason.md) |
| Preseason setup | **Done** | [`preseason.md`](preseason.md) |
| End of Season week | **Done** | [`season-review.md`](season-review.md) |
| Dashboard tasks (master index) | **Done** | [`dashboard-tasks.md`](dashboard-tasks.md) |
| Notifications | **Done** | [`notifications.md`](notifications.md) |
| News hub (portal) | **Done** | [`news-hub.md`](news-hub.md) |
| Conference tournaments | **Real formats** | [`game-design.md`](game-design.md) |
| News | Headlines; Top 60 recruit board; dynamic draft | [`game-frame.md`](game-frame.md), [`recruiting-engine.md`](recruiting-engine.md) |

### Coach, strategy, players

| Topic | Status | Doc |
|-------|--------|-----|
| O/D schemes + halftime | **Done** | [`strategy-schemes.md`](strategy-schemes.md) |
| Watch sim | **2s** possession delay; 3–30 game clock | [`strategy-schemes.md`](strategy-schemes.md) |
| Game preview | **Ratings + RPI**, no GP required | [`game-frame.md`](game-frame.md) |
| Positions / default lineup | **Done** | [`positions-and-lineup.md`](positions-and-lineup.md) |
| Coach card + School Staff | **Done** | [`coach-card.md`](coach-card.md) |
| Archetypes + draft #1–60 | **Done** | [`player-archetypes.md`](player-archetypes.md) |
| NIL on player card | **Overview valuation** (recruiting commit) | [`player-cards.md`](player-cards.md) |
| Loyalty & promises | **Done** | [`player-loyalty-promises.md`](player-loyalty-promises.md) |
| Injuries & medical redshirt | **Done** | [`injuries.md`](injuries.md) |
| Discipline (conduct + academic) | **Done** | [`in-season-systems.md`](in-season-systems.md) |
| Chemistry & fatigue | **Done** | [`in-season-systems.md`](in-season-systems.md) |

### Recruiting & economy

| Topic | Status | Doc |
|-------|--------|-----|
| Recruiting engine | **Done** | [`recruiting-engine.md`](recruiting-engine.md) |
| Recruit interest model | **Done** — weights proposed in JSON | [`recruiting-engine.md#interest--offers`](recruiting-engine.md#interest--offers) → **6.21** |
| Recruiting interest open questions | **Resolved** | [`recruiting-engine.md#resolved-decisions-was-open-questions`](recruiting-engine.md#resolved-decisions-was-open-questions) |
| Recruiting point actions | **Done** | [`recruiting-engine.md#action-costs-design-lock`](recruiting-engine.md#action-costs-design-lock) → **6.17–6.20** |
| Recruiting dashboard tasks | **Done** (triggers) | [`recruiting-engine.md#in-season-dashboard-tasks`](recruiting-engine.md#in-season-dashboard-tasks) |
| AI recruiting | **Done** — Recommended parity; **hidden** from UI | [`recruiting-engine.md#ai-recruiting`](recruiting-engine.md#ai-recruiting) |
| Recruit rankings UI | **Top 60 full list** — not T25-style | [`recruiting-engine.md#national-rankings-top-60`](recruiting-engine.md#national-rankings-top-60) |
| Transfer portal | **Done** | [`transfer-portal.md`](transfer-portal.md) |
| Portal dashboard tasks | **Done** | [`transfer-portal.md`](transfer-portal.md#ux--dashboard-optional-tasks-portal-only) |
| NIL annual pool | **Done** | [`game-design.md#annual-collective-pool-design-lock`](game-design.md#annual-collective-pool-design-lock) |
| NIL Budget / deal depth | **Done** — **3× ask** cap | [`game-design.md#nil-budget-workflow-design-lock`](game-design.md#nil-budget-workflow-design-lock) |
| RS schedule cap | **32** games; **28** builder + 4-game event | [`game-design.md#scheduling--regular-season`](game-design.md#scheduling--regular-season), [`preseason.md`](preseason.md) |

### Postseason, history & world

| Topic | Status | Doc |
|-------|--------|-----|
| Postseason selection | **Done** | [`postseason.md`](postseason.md) |
| Selection engine (poll + seed) | **Done** | [`selection-engine.md`](selection-engine.md) |
| Postseason UI (March Mayhem) | **Done** | [`postseason-frame.md`](postseason-frame.md) |
| NCBA / NCI / early events | **Done** | [`ncba-branding.md`](ncba-branding.md) |
| GOAT / History depth | **Done** | [`history-hub.md`](history-hub.md) |
| Season win contribution (GOAT input) | **Done** — hidden; sums to team wins | [`simulation-engine.md#season-win-contribution-design-lock`](simulation-engine.md#season-win-contribution-design-lock) |
| Player awards (**Team MVP**, **NCBA MVP**, All-*) | **Done** — simple UI labels; win contribution hidden | [`awards.md`](awards.md) |
| League branding / membership | **365** programs; reclass postseason rule | [`league-branding.md`](league-branding.md), [`2026-NCAA-Directory.md`](2026-NCAA-Directory.md) |

### Engineering meta

| Topic | Status | Doc |
|-------|--------|-----|
| Implementation build order | **Done** | [`BUILD_CHECKLIST.md`](../docs/BUILD_CHECKLIST.md) |
| App updates vs saves | **Rule** | [`ADR-001`](../docs/ADR-001-dynasty-save-backend.md#app-updates--save-compatibility) |

---

## Gameplan (locked)

Pre-game: schemes, rotation, usage. Halftime: same knobs. Coach **default** schemes on coach card Overview; per-game prep unchanged in Strategy hub.

**Strategy / rotation timing:** edit before sim + halftime; replay with new prep before week advance; after week advance, that week’s results final, prep for next games.

**Shell note:** No separate offseason/preseason **layout** docs — [`game-frame.md`](game-frame.md) is the single UI shell; [`offseason.md`](offseason.md) / [`preseason.md`](preseason.md) define **tasks** that gate **Advance week** on the Dashboard.

---

## Still tuning (numbers / small gaps)

Listed in [`README.md#still-tuning-not-missing-specs`](README.md#still-tuning-not-missing-specs). Includes recruit interest JSON weights, Recommended bands, URL pack signing. **Season win contribution (GOAT)** — **Done** in [`simulation-engine.md#season-win-contribution-design-lock`](simulation-engine.md#season-win-contribution-design-lock).

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Tracker from user follow-up batch |
| 2025-11-08 | NIL on Overview; coach card; recruiting-engine; full-product restore pass |
| 2026-11-09 | Unified shell; BUILD_CHECKLIST sync; notifications Done; no in-season portal |
| 2026-11-09 | Interest model + resolved open questions; in-season systems; NIL Budget |
| 2026-11-09 | **Planning index** — [`README.md`](README.md); tracker regrouped |
