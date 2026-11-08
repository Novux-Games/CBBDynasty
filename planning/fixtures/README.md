# Simulation regression fixtures (planning trace)

Engine tests live in `testdata/sim/fixtures/` when code exists. Spec: [`../simulation-engine.md`](../simulation-engine.md#regression-fixtures-14--decided).

| ID | Purpose |
|----|---------|
| `slugfest` | Low scoring, stat reconciliation |
| `shootout` | High pace, high totals |
| `foul_trouble` | PF / minutes sanity |
| `blowout` | Favorite–underdog margin |
| `upset_band` | Monte Carlo upset frequency guardrail |
| `ot_path` | Overtime resolution |

Player-facing game does not expose these scenarios.
