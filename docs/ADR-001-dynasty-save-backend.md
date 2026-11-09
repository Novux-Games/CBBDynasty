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

JSON files stay appropriate for **pack import** and optional **user export/backup** of a dynasty (future checklist item), not for continuous in-game writes.

## Scope of persistence

| Layer | Storage | Notes |
|-------|---------|--------|
| **App settings** | DataStore | Already implemented (`AppSettingsRepository`) |
| **Identity pack** | JSON file / asset | Validated at import; `packId` on dynasty |
| **Dynasty save** | Room | Metadata, calendar, programs, people, results, archives |
| **Display cache** | Room or derived | Rebuilt on pack swap per GDD |

## Week-advance semantics (unchanged from GDD)

- **Before** week advance: in-week sim results may live in session / staging tables or rows marked provisional.
- **On** week advance: single transaction promotes the week to **committed** state (locked results, standings, stats).
- **After** advance: no replay without sandbox/cheat flags.

## Save slots

- Multiple dynasties as separate **dynasty roots** (`dynastyId` UUID) in one DB file, or one DB per slot — **default: one `dynasty.db`**, table `dynasty_slot` with `slotIndex`, `dynastyId`, `lastPlayedAt`, summary fields for Load/Continue UI.
- Final slot count enforced in app layer (TBD in game-start; typically 3–10).

## Schema versioning

- Room `version` + **migrations** for every schema change.
- `dynastySchemaVersion` on each dynasty row for one-off data migrations if needed.

## Corruption & recovery

- Room integrity + try/catch on load; corrupt slot surfaces error UI ([game-start](../planning/game-start.md), checklist 2.5).
- Optional **export dynasty to JSON** for backup (not required for ADR acceptance).

## Consequences

- Add `:database` module (Room, KSP) when implementing 2.2+.
- `:core` keeps pure models and slot/dynasty APIs; Android module owns DAOs.
- CI continues to run `:core:test` without instrumented DB until android tests exist.

## Alternatives considered

- **One JSON file per save slot:** Simple backups but poor fit for weekly full-league commits and query-heavy features.
- **Hybrid (JSON staging + SQLite):** Rejected; duplicate source of truth.
