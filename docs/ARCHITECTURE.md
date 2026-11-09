# Architecture

**Game:** Dynasty: College Basketball · **Studio:** Novux Games

Planning remains authoritative in [`planning/`](../planning/). This doc describes the **code** layout.

## Modules

| Module | Role |
|--------|------|
| **`android/app`** | Jetpack Compose UI, navigation, Android shell ([`app-shell.md`](../planning/app-shell.md), [`game-start.md`](../planning/game-start.md)) |
| **`android/core`** | JVM Kotlin — calendar phases, save slot models, constants, simulation/dynasty logic (JUnit, no device) |
| **`android/database`** | Room / SQLite — save slots table ([ADR-001](ADR-001-dynasty-save-backend.md)) |

## Package map (`app`)

| Package | Role |
|---------|------|
| `ui.shell` | Studio, splash, home, load, settings, credits |
| `ui.navigation` | Pre-dynasty routes; dynasty shell routes as they ship |
| `ui.theme` | `#181a25` brand ([`branding.md`](../planning/branding.md)) |
| `data/settings` | DataStore app preferences |
| `data/save` | `SaveSlotRepository`, active session holder |
| `data` | Identity pack import (JSON) — in progress |
| `domain` | Week advance, calendar state machines — in progress |

## Package map (`database`)

| Package | Role |
|---------|------|
| `save` | `SaveSlotEntity`, DAO, mappers |

## Build

```bash
cd android
./gradlew :core:test          # CI-friendly (no SDK)
./gradlew :app:assembleDebug    # requires Android SDK + platform 35
```

## CI (GitHub Actions)

| Workflow | Trigger | Task |
|----------|---------|------|
| [`core-test.yml`](../.github/workflows/core-test.yml) | PRs and pushes to `main` | `:core:test` |
| [`android-assemble-debug.yml`](../.github/workflows/android-assemble-debug.yml) | Manual dispatch; tags `v*` | `:app:assembleDebug` + APK artifact |

## Engineering order

**Checklist:** [`BUILD_CHECKLIST.md`](BUILD_CHECKLIST.md) (phases 0–16).

Tracks: calendar/saves → possession sim → dynasty systems → full league → March — [`game-design.md`](../planning/game-design.md#engineering-milestones).
