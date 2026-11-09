# ADR-002: Balance ruleset bundle (selection, polls, coach, calendar constants)

**Status:** Accepted  
**Date:** 2026-11-09  
**Context:** [ADR-001](ADR-001-dynasty-save-backend.md) · [selection-engine](../planning/selection-engine.md) · [coach-card](../planning/coach-card.md) · [game-design](../planning/game-design.md)  
**Companion:** [ADR-003](ADR-003-sim-tuning-bundle.md) (possession engine tables)

## Decision

Ship **game-balance tuning** in a dedicated **embedded JSON bundle**, separate from **identity packs**. Do **not** import balance files from URL/TXT (same as ADR-003). Do **not** advance or swap balance files by **dynasty season index** — one pinned revision per dynasty for its entire career.

| Layer | Role |
|-------|------|
| **Identity pack** | `teamId`, conferences, templates, display, `rulesetYear` **label** (era the league snapshot targets) |
| **Balance bundle** | NET / SOS / quads / polls / seed weights, job-security weights, portal & scholarship **constants** |
| **Sim tuning bundle** | Possession tables — [ADR-003](ADR-003-sim-tuning-bundle.md) |

## Why split from the pack (option B)

- Packs remain the **identity mod surface** (names, colors, bracket templates) without dragging megabytes of coefficients.
- Balance and sim can **version and hotfix on different cadences** (two files, two `schemaVersion`s).
- `:core` loads both bundles once per dynasty session; formulas in planning docs **cite JSON**, not duplicate tables.

## What we are *not* doing

| Idea | Rejected because |
|------|----------------|
| **`ruleset-2026.json` … `ruleset-2030.json` per dynasty year** | Fictional universe; same competitive rules for season 1 and season 20 unless we author a **one-off game event**, not automatic calendar math |
| **`effectiveYear = pack.rulesetYear + (seasonIndex - 1)`** | Adds lookup complexity with no current design need |
| **Ruleset import** | No player URL/TXT for balance; updates ship with the app |
| **Player-facing version strings** | No “balance v2” copy in UI — ids are for save header, export manifest, support |

`rulesetYear` on `IdentityPack` stays metadata (“2026–27 world”). It does **not** select a different balance file each season.

## Artifact layout

**Repo (source of truth):**

```
assets/balance/balance-2026.json          # canonical balance bundle
assets/balance/README.md

android/app/src/main/assets/balance/      # APK mirror (same as pack pipeline)
  balance-2026.json
```

**Bundle id (frozen on dynasty):** `balanceRulesetId` = `"balance-2026"` (filename stem).  
**Revision inside file:** `schemaVersion` (int, bump when JSON shape or default weights change).

### JSON shape (conceptual)

```json
{
  "balanceRulesetId": "balance-2026",
  "schemaVersion": 1,
  "labelMembershipYear": "2026-27",
  "selection": {
    "netWeights": { "nem": 0.32, "sos": 0.25, "qrs": 0.22, "form": 0.12, "rq": 0.09 },
    "quadCutoffs": { "q1Max": 50, "q2Max": 150, "q3Max": 280 },
    "quadPoints": { "q1Win": 4.0, "q2Win": 2.5, "q3Win": 1.0, "q4Win": 0.3, "q1Loss": -1.0, "q2Loss": -0.5 },
    "sosVenueWeights": { "home": 1.0, "neutral": 1.05, "road": 1.1 },
    "mediaPollWeights": { },
    "coachesPollWeights": { },
    "seedWeights": { }
  },
  "coach": {
    "jobSecurityWeights": { }
  },
  "calendar": {
    "scholarshipLimit": 15,
    "rosterCap": 15,
    "portalNotes": "constants only — behavior in game-design / offseason"
  }
}
```

Planning doc **formulas** stay in [`selection-engine.md`](../planning/selection-engine.md); **numeric defaults** live in `balance-2026.json`. When they diverge, **JSON wins** and planning links here.

Poll weight objects may start empty in v1 of the file and fill from the documented defaults in codegen or a follow-up edit — loader validates required keys before NET runs.

## Load & validate

- **`BalanceRulesetCodec`** in `:core` (future): parse JSON, validate ranges (weights sum ~1 where required, quad cutoffs ordered).
- **Ship default:** app embeds `balance-2026.json`; only one balance bundle until product adds another file (e.g. `balance-experimental.json` for internal QA — not wired to wizard).

## Dynasty persistence (freeze at create)

When a dynasty is bootstrapped ([`game-start.md`](../planning/game-start.md) sign contract):

1. Read **current shipped** `balanceRulesetId` + `schemaVersion` from embedded assets (the defaults the build ships).
2. Store on dynasty row (Room `dynasty_metadata`, export manifest):
   - `balanceRulesetId` (string)
   - `balanceSchemaVersion` (int)

For the life of that save, engines load **that** id + version from assets (or from onboarded migration table if an old revision is kept for compatibility). **New app balance changes apply only to new dynasties** unless policy below applies.

**Not shown to players** in season review, GOAT, or credits.

### App updates

| Change | Policy |
|--------|--------|
| **Bug fix** (wrong formula, crash) | May migrate existing saves with explicit migration; note in release notes |
| **Balance / feel** (NET weights, etc.) | New dynasties only; existing keep frozen `balanceSchemaVersion` |
| **Optional later** | “Apply latest balance” on load with strong confirm — not required now |

## Pack swap mid-dynasty

Swapping identity pack **does not** change `balanceRulesetId` / `balanceSchemaVersion` on the save. Display overlay rebuilds; committee math stays pinned unless we add a deliberate “era change” feature later.

## Customization

- **Mods:** identity pack import only — not balance JSON.
- **Developers:** edit `assets/balance/balance-2026.json`, bump `schemaVersion`, ship new APK.

## Recruiting interest (proposed keys)

Interest factor weights are **balance** data, not possession-sim tuning ([ADR-003](ADR-003-sim-tuning-bundle.md)). Behavior and the proposed numbers live in [`recruiting-engine.md`](../planning/recruiting-engine.md#base-weights): `recruiting.interest.baseWeights`, `priorityMultiplier`, `starTierShifts`, `proximityScores`, `factorCurves`, and action `interestDelta`s.

**Not in `balance-2026.json` yet.** Shipped NIL keys stay as they are: `nil.interestNilRatioExponent`, `nil.askByStarBand`, `nil.maxOfferMultiplierOfAsk` (**3×**). The interest spec does not change ask bands, the cap, or the annual pool — weights only change how sensitive the meter is. When `recruiting.interest.*` lands in this bundle, bump `schemaVersion`. Planning keeps the behavior; if the doc and JSON diverge, **JSON wins**.

## Consequences

- ~~Implement `balanceRulesetId` + `balanceSchemaVersion` on `DynastyMetadata`~~ — Room `dynasty_metadata` v4 (`MIGRATION_3_4`); pinned in `DynastyBootstrap.newDynasty`.
- Implement `simTuningId` + `simSchemaVersion` on same row (ADR-003).
- Implement `BalanceRulesetRepository` (mirror `PackRepository` pattern): load pinned revision for active dynasty.
- Update [`selection-engine.md`](../planning/selection-engine.md) §Tuning to reference this ADR and file path (not `pack ruleset.*`).
- CI (future): `:core:test` validates `balance-2026.json` on every PR.

## Alternatives considered

- **Coefficients inside identity pack:** Rejected — couples mods to balance and bloats pack schema.
- **Kotlin-only constants:** Rejected for ship game — hard to tune, no single canonical data file.
- **Single merged ruleset file with sim:** Rejected — [ADR-003](ADR-003-sim-tuning-bundle.md) split for cadence and ownership.
