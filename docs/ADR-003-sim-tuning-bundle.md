# ADR-003: Simulation tuning bundle (possession engine)

**Status:** Accepted  
**Date:** 2026-11-09  
**Context:** [simulation-engine](../planning/simulation-engine.md) · [ADR-002](ADR-002-balance-ruleset-bundle.md) · [ADR-001](ADR-001-dynasty-save-backend.md)

## Decision

Ship **possession-sim tuning** in a **separate embedded JSON bundle** from balance/selection ([ADR-002](ADR-002-balance-ruleset-bundle.md)). Same persistence rules: **frozen at dynasty create**, **no import**, **no player-facing version copy**, **no per-season file ladder**.

| Bundle | ADR | Examples |
|--------|-----|----------|
| Balance | ADR-002 | NET weights, quads, polls, job security, scholarship cap |
| Sim tuning | ADR-003 | Pace bands, foul rates, shot distributions, shot-clock time-used tables, home/neutral modifiers |

## Artifact layout

```
assets/sim/sim-tuning.json
assets/sim/README.md

android/app/src/main/assets/sim/
  sim-tuning.json
```

**Bundle id (frozen on dynasty):** `simTuningId` = `"sim-tuning"` (stem).  
**Revision:** `schemaVersion` inside JSON.

### JSON shape (conceptual)

```json
{
  "simTuningId": "sim-tuning",
  "schemaVersion": 1,
  "possessionsPerGameTarget": { "min": 60, "max": 70 },
  "shotClock": {
    "full": 30,
    "offensiveRebound": 20
  },
  "timeUsedSeconds": {
    "quickTurnover": [3, 6],
    "quickShot": [8, 14],
    "foulStoppage": [12, 18]
  },
  "eventWeights": { },
  "homeNeutralModifiers": { }
}
```

[`simulation-engine.md`](../planning/simulation-engine.md) describes **behavior**; this file holds **numbers**. Empty `eventWeights` is allowed in early schema versions until possession branches land — loader validates known keys per `schemaVersion`.

## Load & validate

- **`SimTuningCodec`** in `:core` (future): parse + validate bands (min ≤ max, positive rates).
- **`SimEngine`** reads tuning once per `simulateGame` call (or cached per pinned `simTuningId` + version for the dynasty).

Balance and sim bundles are **paired in content** for the 2026–27 era but **versioned independently** (`balanceSchemaVersion` ≠ `simSchemaVersion`).

## Dynasty persistence (freeze at create)

On bootstrap, store:

- `simTuningId`
- `simSchemaVersion`

Load pinned revision for all games in that dynasty. App update policy matches [ADR-002](ADR-002-balance-ruleset-bundle.md) (bugfix migrate vs balance/feel for new dynasties only).

## Customization & import

- **No sim tuning import** for players.
- Identity pack import does not include possession tables.

## Consequences

- Room columns alongside ADR-002 fields on `dynasty_metadata`.
- `SimTuningRepository` in `data/` when sim Phase 10 starts.
- CI: validate `sim-tuning.json` in `:core:test` when codec exists.
- Regen/copy workflow documented in `assets/sim/README.md` (parallel to pack).

## Alternatives considered

- **Merged `ruleset.json`:** Rejected — sim tuning will churn faster during playtesting; split reduces merge conflict noise.
- **Hard-coded Kotlin tuning:** Rejected for ship — contradicts simulation-engine “balance lives in data files.”
