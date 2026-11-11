# Dynasty: College Basketball

**Studio:** [Novux Games](https://novux.dev)

Text-based college basketball dynasty sim for Android. **Planning:** [`planning/README.md`](planning/README.md) (full index) · spine [`planning/game-design.md`](planning/game-design.md) · tracker [`planning/planning-notes.md`](planning/planning-notes.md).

**Logo:** `Public-Assets/app-logo.svg` · **Brand background:** `#181a25`

## Android app

Kotlin · Jetpack Compose · `:app` + `:core` + `:database`. See [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md).

```bash
cd android && ./gradlew :core:test
```

Open `android/` in Android Studio for emulator builds (`:app:assembleDebug`).

**Play Store AAB (CI):** [`docs/ANDROID_RELEASE_CI.md`](docs/ANDROID_RELEASE_CI.md) — GitHub Actions `bundleRelease` after upload-keystore secrets are set.

**Implementation order:** [`docs/BUILD_CHECKLIST.md`](docs/BUILD_CHECKLIST.md)

## License

**Open code, closed art:** Apache 2.0 for source and planning docs; logos and
`Public-Assets/` remain © Novux Games (all rights reserved). Details:
[`docs/LICENSING.md`](docs/LICENSING.md).
