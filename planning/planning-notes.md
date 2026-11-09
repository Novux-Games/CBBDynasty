# Planning follow-ups (tracker)

Quick pointers so we don't re-ask settled items. **Details live in linked docs.**

| Topic | Status | Doc |
|-------|--------|-----|
| O/D schemes + halftime | **Done** | [`strategy-schemes.md`](strategy-schemes.md) |
| Positions / default lineup | **Done** | [`positions-and-lineup.md`](positions-and-lineup.md) |
| Archetypes + draft #1–60 | **Done** | [`player-archetypes.md`](player-archetypes.md) |
| Game preview | **Ratings + RPI**, no GP required | [`game-frame.md`](game-frame.md) |
| Watch sim | **2s** possession delay; 3–30 game clock | [`strategy-schemes.md`](strategy-schemes.md) |
| Conference tournaments | **Real formats** | [`game-design.md`](game-design.md) |
| News | Headlines; Top 60 recruit board; dynamic draft | [`game-frame.md`](game-frame.md), [`recruiting-engine.md`](recruiting-engine.md) |
| Coach card + School Staff | **Done** | [`coach-card.md`](coach-card.md) |
| Recruiting engine | **Done** | [`recruiting-engine.md`](recruiting-engine.md) |
| Loyalty & promises | **Done** | [`player-loyalty-promises.md`](player-loyalty-promises.md) |
| Dynasty shell (all phases) | **Done** — one chrome, dashboard task gate, week advance only | [`game-frame.md`](game-frame.md) |
| League branding / membership | **365** programs; 4 reclass no postseason until year 4 | [`league-branding.md`](league-branding.md), [`2026-NCAA-Directory.md`](2026-NCAA-Directory.md) |
| NIL on player card | **Overview valuation** (recruiting commit) | [`player-cards.md`](player-cards.md) |
| NIL annual pool | **Done** — conference × prestige; SEC flagship **~$9.7M–$13.2M** | [`game-design.md`](game-design.md#annual-collective-pool-design-lock) |
| RS schedule cap | **32** games; **28** builder slots only with 4-game event | [`game-design.md`](game-design.md#scheduling--regular-season), [`preseason.md`](preseason.md#8--schedule-regular-season-games) |
| Transfer portal | **Done** (UX News/tasks **open**) | [`transfer-portal.md`](transfer-portal.md) |
| AI recruiting | **Done** — Recommended parity; **hidden** from UI | [`recruiting-engine.md`](recruiting-engine.md#ai-recruiting) |
| Recruit rankings UI | **Top 60 full list** — not T25-style | [`recruiting-engine.md`](recruiting-engine.md#national-rankings-top-60) |
| GOAT / History depth | **Specced** (computed GOAT list) | [`game-frame.md`](game-frame.md#history-hub) |
| Notifications | Use game-design mobile UX list | [`game-design.md`](game-design.md) |
| App updates vs saves | **Rule:** ship migrations; old slots keep working | [`ADR-001`](../docs/ADR-001-dynasty-save-backend.md#app-updates--save-compatibility), [`ADR-002`](../docs/ADR-002-balance-ruleset-bundle.md) |

---

## Engineering rule — app updates must not break saves

**Policy:** Every store / APK update is **backward compatible** with existing dynasty slots on device. Players who update **keep** their careers; we **migrate** data when the DB shape changes — never “wipe and resync” as the default.

| Requirement | Meaning |
|-------------|---------|
| **Load after update** | Occupied slots open; calendar + metadata remain coherent (repair only when already corrupt — checklist 2.5) |
| **Room changes** | Every schema bump ships a **Migration** (or tested destructive path only for empty slots — never for occupied rows without migration) |
| **Tuning / balance** | Existing dynasties keep **pinned** balance + sim revisions from create ([ADR-002](../docs/ADR-002-balance-ruleset-bundle.md)); app updates do **not** silently re-roll NET or sim math mid-career |
| **Shipped asset retention** | While any save references `balanceSchemaVersion` / `simSchemaVersion` **N**, the APK keeps the JSON (or loader mapping) needed to load **N** |
| **Forward-only features** | New columns default safely; new systems attach without rewriting history |
| **No player copy** | Migrations are silent — no “save version upgraded” UI unless we add optional release notes |

**Not promised:** hand-edited exports, slots already broken before update, or infinite support for ancient `dynastySchemaVersion` without export — see ADR-001 for export fallback.

**CI (target):** migration tests + load fixture save across version bump before release.

---

## Gameplan — nothing else needed from you (for now)

Pre-game: schemes, rotation, usage. Halftime: same knobs. Coach **default** schemes on coach card Overview; per-game prep unchanged in Strategy hub.

**Strategy / rotation timing (locked):** edit before sim + halftime; replay with new prep before week advance; after week advance, that week’s results final, prep for next games.

---

## “Still on the list” — what that meant

**Shell:** No separate offseason/preseason **layout** docs — [`game-frame.md`](game-frame.md) is the single UI shell; [`offseason.md`](offseason.md) / [`preseason.md`](preseason.md) define **tasks** that gate **Advance week** on the Dashboard.

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Tracker from user follow-up batch |
| 2025-11-08 | NIL on Overview; clarify future docs vs backlog |
| 2025-11-08 | Coach card + School Staff |
| 2025-11-08 | recruiting-engine.md |
| 2025-11-08 | Full-product restore pass (staff, walk-ons, gameplan, secondary postseason) |
| NCBA / NCI / early events | **Done** | [`ncba-branding.md`](ncba-branding.md) |
| Postseason selection | **Done** | [`postseason.md`](postseason.md) |
| Selection engine (poll + seed) | **Done** | [`selection-engine.md`](selection-engine.md) |
| Postseason UI (March Mayhem) | **Done** | [`postseason-frame.md`](postseason-frame.md) |
| Game start / new dynasty | **Done** | [`game-start.md`](game-start.md) |
| Offseason calendar | **Done** | [`offseason.md`](offseason.md) |
| Preseason setup | **Done** | [`preseason.md`](preseason.md) |
| Time system (weeks/byes) | **Done** | [`time-system.md`](time-system.md) |
| End of Season week | **Done** | [`season-review.md`](season-review.md) |
| App shell (studio / splash) | **Done** | [`app-shell.md`](app-shell.md) |
| Implementation build order | **Done** | [`docs/BUILD_CHECKLIST.md`](../docs/BUILD_CHECKLIST.md) |
| 2026-11-09 | App-update save compatibility rule | [`ADR-001`](../docs/ADR-001-dynasty-save-backend.md#app-updates--save-compatibility) |
| 2026-11-09 | Unified shell + week advance + dashboard task gate | [`game-frame.md`](game-frame.md) |
