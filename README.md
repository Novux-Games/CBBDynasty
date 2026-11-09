# Dynasty: College Basketball

**Studio:** [Novux Games](https://novux.dev)

Text-based college basketball dynasty sim for Android. Planning docs live in [`planning/`](planning/) — start with [`planning/game-design.md`](planning/game-design.md) and [`planning/branding.md`](planning/branding.md). Offseason recruiting: [`planning/recruiting-engine.md`](planning/recruiting-engine.md).

**Logo:** `Public-Assets/app-logo.svg` · **Brand background:** `#181a25`

## Android app

Kotlin · Jetpack Compose · `:app` + `:core` + `:database`. See [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md).

```bash
cd android && ./gradlew :core:test
```

Open `android/` in Android Studio for emulator builds (`:app:assembleDebug`).

**Implementation order:** [`docs/BUILD_CHECKLIST.md`](docs/BUILD_CHECKLIST.md)

## License

**Open code, closed art:** Apache 2.0 for source and planning docs; logos and
`Public-Assets/` remain © Novux Games (all rights reserved). Details:
[`docs/LICENSING.md`](docs/LICENSING.md).
