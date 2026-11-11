# Google Play open testing — fresh install (MEGA-QA-135)

**Gate:** First-time install from the **open testing** track must cold-launch through studio (if enabled) and reach **splash / home** without crash.

Constants and checklist ids: `PlayOpenTestInstallPath` (`:core`).

## Play Console setup (once per release)

1. Upload signed AAB from [Android release bundle](../.github/workflows/android-bundle-release.yml) (workflow_dispatch or version tag).
2. **Testing → Open testing** → create release with the new AAB; roll out to testers.
3. Confirm **version code** increased vs prior open-test build ([MEGA-QA-136]).
4. Ensure at least one Gmail tester is on the open-testing list.

## Tester script (MEGA-QA-135)

| # | Step | Pass |
|---|------|------|
| 1 | Uninstall any existing `com.dynasty.cbb` sideload | Clean package state |
| 2 | Open Play Store link for open testing → **Install** | Install from Play (not APK) |
| 3 | Launch from launcher | No immediate crash |
| 4 | Complete **Novux Games** studio intro if shown | Reaches title / **Tap to Play** |
| 5 | **Tap to Play** | Home menu (New Game, Load, Settings) |

Capture screenshot or screen recording for release notes if anything fails.

## Automation (BOT) — binary readiness

CI cannot install from Play; it verifies the **same AAB-derived binary** is launchable:

```bash
bash scripts/build_release_universal_apk.sh
bash scripts/verify_play_fresh_install_launch.sh
```

Checks: `launchable-activity` = `MainActivity`, package `com.dynasty.cbb`.

Workflow: [`.github/workflows/android-release-universal-smoke.yml`](../.github/workflows/android-release-universal-smoke.yml).

## MEGA-QA-136 — Update over prior open-test build

| # | Step | Pass |
|---|------|------|
| 1 | Keep a dynasty in progress on **prior** open-test APK | Occupied slot |
| 2 | Publish new AAB with **`versionCode` >** `android/play_open_test_baseline_version_code.txt` | Play accepts upload |
| 3 | Play Store **Update** on tester device | Installs without parse/signing errors |
| 4 | Launch → **Continue** | Same dynasty loads; no migration crash |
| 5 | Sim week or open shell | Calendar/metadata coherent |

After each Play promote, bump `play_open_test_baseline_version_code.txt` to the **shipped** `versionCode`.

**Automation (BOT):**

- `scripts/verify_play_update_version_bump.sh`
- `:database:testDebugUnitTest` — `DynastyDatabaseMigrationRobolectricTest.playUpdateOccupiedSlot_fullMigrationChain_preservesDynastyIdForContinue`
- `:core:test` — `SaveLoadAfterUpdateFixtureTest`, `PlayUpdateOverPriorPathTest`

## Related

- Sideload parity: [`RELEASE_SIDELOAD_SMOKE.md`](RELEASE_SIDELOAD_SMOKE.md) (MEGA-QA-134)
- Pre-launch matrix: [`PLAY_PRE_LAUNCH_MATRIX.md`](PLAY_PRE_LAUNCH_MATRIX.md)
- Listing copy: [`PLAY_STORE_LISTING.md`](PLAY_STORE_LISTING.md)
