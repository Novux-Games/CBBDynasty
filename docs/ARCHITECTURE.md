# Architecture (scaffold)

**Game:** Dynasty: College Basketball · **Studio:** Novux Games

Planning remains authoritative in [`planning/`](../planning/). This doc describes the **code** layout only.

## Modules

| Module | Role |
|--------|------|
| **`android/app`** | Jetpack Compose UI, navigation, Android shell ([`app-shell.md`](../planning/app-shell.md), [`game-start.md`](../planning/game-start.md)) |
| **`android/core`** | JVM Kotlin — calendar phases, constants, future sim/dynasty logic (testable without device) |

## Package map (`app`)

| Package | Planned growth |
|---------|----------------|
| `ui.shell` | Studio, splash, home menu |
| `ui.navigation` | Pre-dynasty + future dynasty routes |
| `ui.theme` | `#181a25` brand ([`branding.md`](../planning/branding.md)) |
| `data/settings` | DataStore app prefs (skip studio, audio stubs) |
| `data` | Local saves, identity packs (TBD) |
| `domain` | Week advance, offseason/preseason state machines (TBD) |

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

## Next engineering tracks

**Step-by-step checklist:** [`BUILD_CHECKLIST.md`](BUILD_CHECKLIST.md) (phases 0–16).

High level: calendar/saves → possession sim → dynasty systems → full league → March — [`game-design.md`](../planning/game-design.md#engineering-milestones).
