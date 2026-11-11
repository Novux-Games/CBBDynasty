# MEGA build checklist — full product (no deferrals)

**Product:** Dynasty: College Basketball · **Studio:** Novux Games  
**Authority:** Every row traces to a **locked** decision in [`planning/`](../planning/). This checklist **supersedes** the coarse phase table in [`BUILD_CHECKLIST.md`](BUILD_CHECKLIST.md) for honest ship tracking.

**Policy (from [`planning/planning-notes.md`](../planning/planning-notes.md))**

- We are building the **full game**, not a scaled-down v1, MVP, or “ships later” slice.
- **Nothing is deferred** on this list: `MISSING`, `FAIL`, `GAP`, `PARTIAL`, `STUB`, and `WRONG` all mean **work remains**.
- Mark **`[x]`** only when the item’s **Status** is `DONE` / `SHIP` / `PASS` in the audit shard and **Done when** is satisfied on device.

---

## Audit methodology (2026-11-11)

| Shard | Planning sources | Items (approx.) |
|-------|------------------|-----------------|
| [`MEGA_BUILD_CHECKLIST_AUDIT/game-design.md`](MEGA_BUILD_CHECKLIST_AUDIT/game-design.md) | `game-design.md` | 70 |
| [`MEGA_BUILD_CHECKLIST_AUDIT/recruiting-transfer-start.md`](MEGA_BUILD_CHECKLIST_AUDIT/recruiting-transfer-start.md) | `recruiting-engine.md`, `transfer-portal.md`, `game-start.md` | 81 |
| [`MEGA_BUILD_CHECKLIST_AUDIT/sim-strategy-lineup-injuries.md`](MEGA_BUILD_CHECKLIST_AUDIT/sim-strategy-lineup-injuries.md) | `simulation-engine.md`, `strategy-schemes.md`, `positions-and-lineup.md`, `injuries.md` | 85 |
| [`MEGA_BUILD_CHECKLIST_AUDIT/frame-dashboard-time.md`](MEGA_BUILD_CHECKLIST_AUDIT/frame-dashboard-time.md) | `game-frame.md`, `dashboard-tasks.md`, `time-system.md`, `offseason.md`, `preseason.md` | 84 |
| [`MEGA_BUILD_CHECKLIST_AUDIT/coach-player-ui.md`](MEGA_BUILD_CHECKLIST_AUDIT/coach-player-ui.md) | `coach-card.md`, `player-cards.md`, `player-archetypes.md`, `player-loyalty-promises.md`, `game-start.md` | 95 |
| [`MEGA_BUILD_CHECKLIST_AUDIT/postseason-awards-history.md`](MEGA_BUILD_CHECKLIST_AUDIT/postseason-awards-history.md) | `postseason.md`, `postseason-frame.md`, `selection-engine.md`, `awards.md`, `season-review.md`, `history-hub.md` | 106 |
| [`MEGA_BUILD_CHECKLIST_AUDIT/news-shell-branding.md`](MEGA_BUILD_CHECKLIST_AUDIT/news-shell-branding.md) | `news-hub.md`, `notifications.md`, `in-season-systems.md`, `app-shell.md`, `branding.md` | 87 |
| [`MEGA_BUILD_CHECKLIST_AUDIT/league-world-pack.md`](MEGA_BUILD_CHECKLIST_AUDIT/league-world-pack.md) | `league-branding.md`, `ncba-branding.md`, `2026-NCAA-Directory.md`, pack | 82 |
| [`MEGA_BUILD_CHECKLIST_AUDIT/integration-playtest-regression.md`](MEGA_BUILD_CHECKLIST_AUDIT/integration-playtest-regression.md) | Playtest, perf, persistence, nav, release/R8 | 170 |

**Total granular IDs:** **722** feature audit (`MEGA-*-*-NNN` across planning shards) + **170** QA matrix (`MEGA-QA-*` in integration shard).  
**Sub-agent audits:** Composer-backed deep reads (Sonnet quota exhausted during run; audits are static code + spec trace, not emulator QA).

**How to use**

1. Pick a shard (or planning doc) and work **P0 → P1 → P2**.
2. When an item ships, update its row in the shard (`Status` → `DONE`/`PASS`) and add **`[x]`** beside the ID in the shard’s summary table if present.
3. Keep [`BUILD_CHECKLIST.md`](BUILD_CHECKLIST.md) phase index in sync when a **whole topic** in `planning-notes.md` actually becomes true on device.

---

## Executive summary — why the old checklist lied

The phase checklist marked **hundreds** of steps `[x]` while the codebase still uses:

- **Demo/stub data paths** (`RecruitingClassSeed.demoPool`, default prestige 40) on live UI.
- **Placeholder tabs** (`recruiting_placeholder_hs_juco`, portal placeholder).
- **Thin shells** (coach card without tabs/portraits; player card without Stats/Games/History).
- **Possession sim** that passes smoke tests but not the depth in `simulation-engine.md` (narration, fouls, clutch, tuning surfaces).

**Do not treat open testing as “feature complete.”** Use this MEGA list until gaps trend to zero.

---

## P0 — Ship blockers (start here)

Consolidated from audits + your playtest notes. Full evidence lives in shards.

| ID | Topic | Finding (short) |
|----|-------|-----------------|
| MEGA-RECR-005 | Recruit pool | Hub/AI/signing use **120-player demo pool**, not persisted `IncomingClassSeedRepository` (2k–4k). |
| MEGA-RECR-015 | Points economy | Weekly pool uses **default prestige 40**, not chosen school tier. |
| MEGA-RECR-027 | Point spend | DM/call/house spend **does not move** interest meter (`spendDelta` unused). |
| MEGA-RECR-035–036 | Recruiting UI | HS/JUCO and **Portal tabs are placeholders**. |
| MEGA-RECR-042 | Dashboard | In-season **optional recruiting tasks** not emitted (`DashboardTaskGate`). |
| MEGA-RECR-053 | Calendar | Season 1 recruit cycle vs **Offseason W1** bootstrap mismatch. |
| MEGA-COACH-016–017 | Coach avatars | **No portrait renderer**; coach card has no avatar strip. |
| MEGA-COACH-031 | Coach card | **No tabs** (Overview · Contract · History · HOF · Draft). |
| MEGA-COACH-060–069 | Player cards | **No tabbed card** (Ratings/Stats/Games/History missing). |
| MEGA-GDD-027–029 | GDD recruiting | Year-long recruiting/portal **Partial** — placeholders dominate UX. |
| MEGA-GDD-041 | League | Pack **tiebreaker stack** not applied in standings. |
| MEGA-SIM-* | Simulation | See shard: multiple **GAP/WIRE** on watch depth, foul strategy, rotation minutes UI. |

*(See each shard for complete P0/P1 tables — recruiting alone lists 10+ P0 `FAIL` rows.)*

---

## Your reported issues → audit IDs

| Symptom | Audit mapping |
|---------|----------------|
| No coach avatars | `MEGA-COACH-011`–`018`, `016` portrait pipeline **MISSING** |
| Sim bare / boring | `MEGA-SIM-*` shard (possession copy, clutch, scheme modifiers, rotation UX) |
| Recruiting points buggy | `MEGA-RECR-015`, `022`, `027` (prestige, global spend, interest) |
| Recruits not generating | `MEGA-RECR-001`–`008`, `005` (demo pool vs seeder) |
| Tasks disappear | `MEGA-RECR-042`, `MEGA-FRAME-*` dashboard gate / optional task wiring |
| Recruiting stuck signing day → preseason | `MEGA-RECR-038`, `051–052`, `MEGA-FRAME-062` (+ nav audit in frame shard) |
| Team vs player rankings confusion | `MEGA-RECR-055`–`056` (preseason RankingsReview = **team** polls; signing task copy overlap) |

---

## Planning doc → shard index

| Planning doc | Primary shard |
|--------------|----------------|
| `game-design.md` | `game-design.md` |
| `recruiting-engine.md` | `recruiting-transfer-start.md` |
| `transfer-portal.md` | `recruiting-transfer-start.md` |
| `game-start.md` | `recruiting-transfer-start.md`, `coach-player-ui.md` |
| `simulation-engine.md` | `sim-strategy-lineup-injuries.md` |
| `strategy-schemes.md` | `sim-strategy-lineup-injuries.md` |
| `positions-and-lineup.md` | `sim-strategy-lineup-injuries.md` |
| `injuries.md` | `sim-strategy-lineup-injuries.md` |
| `game-frame.md` | `frame-dashboard-time.md` |
| `dashboard-tasks.md` | `frame-dashboard-time.md` |
| `time-system.md` | `frame-dashboard-time.md` |
| `offseason.md` | `frame-dashboard-time.md` |
| `preseason.md` | `frame-dashboard-time.md` |
| `coach-card.md` | `coach-player-ui.md` |
| `player-cards.md` | `coach-player-ui.md` |
| `player-archetypes.md` | `coach-player-ui.md` |
| `player-loyalty-promises.md` | `coach-player-ui.md` |
| `postseason.md` | `postseason-awards-history.md` |
| `postseason-frame.md` | `postseason-awards-history.md` |
| `selection-engine.md` | `postseason-awards-history.md` |
| `awards.md` | `postseason-awards-history.md` |
| `season-review.md` | `postseason-awards-history.md` |
| `history-hub.md` | `postseason-awards-history.md` |
| `news-hub.md` | `news-shell-branding.md` |
| `notifications.md` | `news-shell-branding.md` |
| `in-season-systems.md` | `news-shell-branding.md` |
| `app-shell.md` | `news-shell-branding.md` |
| `branding.md` | `news-shell-branding.md` |
| `league-branding.md` | `league-world-pack.md` |
| `ncba-branding.md` | `league-world-pack.md` |
| `2026-NCAA-Directory.md` | `league-world-pack.md` (membership reference) |

---

## Workstreams (recommended parallel teams)

1. **Recruiting truth path** — kill `demoPool` on UI; wire prestige; build HS/JUCO/Portal tabs; interest spend; class persistence.  
2. **Coach & player presentation** — avatar renderer + card tab shells per `coach-card.md` / `player-cards.md`.  
3. **Sim depth** — possession narrative, gameplan knobs, watch mode pacing, box score richness (`simulation-engine.md`).  
4. **Dashboard & calendar** — task gate completeness, phase transitions, no orphan screens between signing/preseason.  
5. **League & postseason** — tiebreakers, batch sim quality, March Mayhem UI completeness.  
6. **Performance** — lag on recruiting hub / week advance (profile + fix; add to shard as discovered).

---

## Document maintenance

| Event | Action |
|-------|--------|
| Planning **design lock** changes | Update planning doc + add/adjust rows in the matching **shard** + this index. |
| Feature actually ships | Set shard row to `DONE`/`PASS`; only then consider flipping a phase row in `BUILD_CHECKLIST.md`. |
| New playtest bug | Add row with new `MEGA-???-NNN` in the correct shard; link here under **Your reported issues** if user-visible. |
| Release gate | **No open P0** in shards; P1 backlog explicit and accepted or cleared. |

### Document history

| Date | Change |
|------|--------|
| 2026-11-11 | Initial MEGA checklist — 722 items, 8 planning-aligned audit shards; supersedes honest tracking vs old `[x]` phase table |
