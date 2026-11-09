# ADR-001: Dynasty save backend (SQLite / Room)

**Status:** Accepted  
**Date:** 2026-11-09  
**Context:** [Build checklist Phase 2.1](BUILD_CHECKLIST.md) · [game-design — save model](../planning/game-design.md#identity-packs--save-model)

## Decision

Persist **dynasty simulation state** in **SQLite** via **Android Room** in the `:database` module (or `android/app` data layer until the module exists). **Identity packs** remain **JSON** assets/imports — not stored as the dynasty source of truth.

## Why SQLite (not whole-save JSON files)

| Requirement | SQLite |
|-------------|--------|
| **365 programs** + league history | Normalized tables; partial updates on week advance |
| **Week advance = commit** | ACID transactions; no half-written multi‑MB JSON |
| **Save slots** | Indexed metadata rows (last played, school, season) |
| **NET / stats / archives** | Queryable without loading entire save into memory |
| **Pack swap** | Stable `teamId` / `confId` keys; display overlay separate from sim rows |

JSON stays appropriate for **identity pack import** and for **dynasty export files** (backup/share). **Runtime dynasty state is not edited as JSON** — only exported/imported through validated app flows.

## Scope of persistence

| Layer | Storage | Notes |
|-------|---------|--------|
| **App settings** | DataStore | Already implemented (`AppSettingsRepository`) |
| **Identity pack** | JSON file / asset | Validated at import; `packId` on dynasty |
| **Balance / sim tuning** | Embedded JSON | [ADR-002](ADR-002-balance-ruleset-bundle.md), [ADR-003](ADR-003-sim-tuning-bundle.md); pinned ids on dynasty at create |
| **Dynasty save** | Room | Metadata, calendar, programs, people, results, archives |
| **Display cache** | Room or derived | Rebuilt on pack swap per GDD |

## Week-advance semantics (unchanged from GDD)

- **Before** week advance: in-week sim results may live in session / staging tables or rows marked provisional.
- **On** week advance: single transaction promotes the week to **committed** state (locked results, standings, stats).
- **After** advance: no replay without sandbox/cheat flags.

## Save slots

- **10 fixed slots** (`slotIndex` **0–9**). UI shows all slots (empty or occupied) on **Load**; **Continue** uses the slot with the latest `lastPlayedAt`.
- Storage shape: one app-database file (e.g. `dynasty.db`) with `dynasty_slot` rows binding `slotIndex` → `dynastyId` + summary metadata for list UI (school, season, record, last played).
- Deleting a slot removes that dynasty’s rows (or marks tombstone + vacuum policy — implementation detail in 2.2).

## Export & import (dynasty)

- Players **can export** a dynasty to a **portable archive** (format defined in Phase 2.2+; likely SQLite slice or versioned blob + manifest with `dynastySchemaVersion`).
- **Import** restores into an **empty slot** or replaces after confirm — never merge two live dynasties blindly.
- Export supports **backup, device migration, sharing**, and **restoring** a dynasty on another device. If someone edits an export out of band, **import either succeeds (valid archive) or fails** with error UI (checklist 2.5) — we do not treat hand-edited saves as unsupported in principle.

## Customization & modding

- **Identity packs** (JSON/URL) are the **primary and intended mod surface** — realigned names/logos, conference display, tournament templates, disclaimers. This is where most “modding” lives; the game does not ship a separate mod pipeline beyond **pack import** (+ default embedded pack).
- **Dynasty saves** are not a second mod SDK. We do not plan editor tooling or a public save schema doc for cheat/mod authoring unless product needs change later.
- **In practice:** league/world customization → **packs**; career progress → **SQLite + export**. Nothing else is required outside the pack import engine for customization today.

## Schema versioning

- Room `version` + **migrations** for every schema change.
- `dynastySchemaVersion` on each dynasty row for one-off data migrations if needed.

## App updates & save compatibility

**Product rule** ([`planning/planning-notes.md`](../planning/planning-notes.md)): shipping a new build **must not invalidate** existing on-device dynasty slots. Updates **load old saves** and **migrate** when structure changes.

| Layer | On update |
|-------|-----------|
| **SQLite / Room** | `Migration` for every `version` bump; occupied slots never rely on uninstall or clear-data |
| **Dynasty data** | `dynastySchemaVersion` triggers one-off Kotlin migrations (fill defaults, backfill ids) |
| **Balance / sim tuning** | Saves keep **pinned** `balanceRulesetId` + `simTuningId` / schema versions ([ADR-002](ADR-002-balance-ruleset-bundle.md), [ADR-003](ADR-003-sim-tuning-bundle.md)); APK retains bundled JSON for revisions still referenced |
| **Identity pack** | `packId` on save; embedded or restored overlay — pack swap does not rewrite simulation rows |

**Possible?** Yes, with discipline: explicit migrations, frozen tuning per dynasty, and keeping old tuning assets (or explicit data migration) while saves reference them.

**Out of scope for “must work”:** saves already failing validation, manually corrupted exports, or unsupported ancient app versions after a documented floor (offer **export** before deprecation).

**Release gate (target):** open a fixture dynasty from prior DB version after migration in instrumented or integration test before tagging.

## Integrity & recovery

- **Primary:** transactional writes (slot + metadata + calendar) so normal play does not leave half saves.
- **Load:** `DynastySaveValidator` + optional calendar repair; unreadable slots show delete fallback ([game-start](../planning/game-start.md), checklist 2.5).
- User can delete slot or re-import from a prior **export** if one exists.

## Consequences

- **`:database`** module (Room, KSP) — save slots table shipped in Phase 2.2.
- `:core` keeps pure models and slot/dynasty APIs; Android module owns DAOs.
- CI continues to run `:core:test` without instrumented DB until android tests exist.

## Alternatives considered

- **One JSON file per save slot:** Simple backups but poor fit for weekly full-league commits and query-heavy features.
- **Hybrid (JSON staging + SQLite):** Rejected; duplicate source of truth.
