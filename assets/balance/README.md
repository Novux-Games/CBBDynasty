# Balance ruleset (`balance-2026`)

Embedded **game-balance** data (NET, polls, quads, coach/calendar constants) — **not** identity packs.

**ADR:** [`docs/ADR-002-balance-ruleset-bundle.md`](../../docs/ADR-002-balance-ruleset-bundle.md)  
**Formulas:** [`planning/selection-engine.md`](../../planning/selection-engine.md)  
**Sim tables:** [`assets/sim/`](../sim/) · [ADR-003](../../docs/ADR-003-sim-tuning-bundle.md)

Ship mirror:

- `android/app/src/main/assets/balance/balance-2026.json`

When weights change, bump `schemaVersion` in this file. **New dynasties** pick up the build’s embedded revision; existing saves keep the revision frozen at create (see ADR-002).
