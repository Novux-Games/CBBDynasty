# Play pre-launch matrix (smoke)

Manual and CI smoke paths before Play open testing. Primary gate: **MEGA-QA-119** (API 34 emulator).

## MEGA-QA-119 — Pixel 6 API 34 x86_64

| Step | Action | Pass criteria |
|------|--------|----------------|
| 1 | Install `:app:assembleDebug` on **Pixel 6**, **API 34**, **google_apis**, **x86_64** | APK installs without error |
| 2 | Cold launch → dismiss studio intro if shown → **Tap to Play** → home | No crash; home menu visible |
| 3 | **New game** → complete wizard → **Sign** | Dynasty shell/offseason loads; save slot occupied |
| 4 | Advance through **offseason / signing** as required → **preseason** → **Start season** | Enters in-season shell |
| 5 | Confirm header shows **Regular Season week 1** (or exhibition then RS per schedule) | `CalendarPhase.RegularSeason` week index ≥ 1 |

**Automation (BOT):**

- GitHub Actions: [`.github/workflows/android-emulator-api34-smoke.yml`](../.github/workflows/android-emulator-api34-smoke.yml) — `connectedDebugAndroidTest` on API 34.
- JVM path check: `PlayPreLaunchSmokePathTest` — documents milestone order to RS W1.

**Local:**

```bash
bash scripts/install_android_sdk.sh
bash scripts/install_android_emulator_api34.sh
cd android && ./gradlew :app:assembleDebug
# Start AVD, then:
./gradlew :app:connectedDebugAndroidTest
```

## MEGA-QA-127 — Physical device (Android 13+, 6 GB)

See [`PHYSICAL_DEVICE_SMOKE.md`](PHYSICAL_DEVICE_SMOKE.md) for the retail device script (new game → four RS sim weeks).

**Automation (BOT):** `FourRegularSeasonWeeksDeviceSmokeTest` mirrors league sim + week checkpoint for four RS weeks.
