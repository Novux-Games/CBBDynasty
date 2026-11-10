# Play Store listing — Dynasty: College Basketball

**Canonical copy:** [`PlayStoreListingCopy.kt`](../android/core/src/main/kotlin/dev/novux/dynasty/cbb/core/legal/PlayStoreListingCopy.kt) (CI-tested)  
**Branding:** [`planning/branding.md`](../planning/branding.md)  
**Privacy:** [`PRIVACY_POLICY.md`](PRIVACY_POLICY.md)

## Listing fields

| Field | Value |
|-------|--------|
| **App name** | `PlayStoreListingCopy.LISTING_TITLE` — **Dynasty: College Basketball** |
| **Short description** | `PlayStoreListingCopy.SHORT_DESCRIPTION` |
| **Full description** | `PlayStoreListingCopy.FULL_DESCRIPTION` |
| **Privacy policy URL** | `PlayStoreListingCopy.PRIVACY_POLICY_URL` |
| **Category** | Games → Simulation |
| **Content rating** | Complete IARC questionnaire (no real-money gambling; fictional sports) |

## Offline & legal (must appear in listing or “Data safety”)

| Topic | Copy source |
|-------|-------------|
| Offline play | `PlayStoreListingCopy.OFFLINE_SUMMARY` |
| Unofficial / no endorsement | `PlayStoreListingCopy.UNOFFICIAL_DISCLAIMER` |
| Design lock | **No implied NCAA or school endorsement** ([`game-design.md`](../planning/game-design.md)) |

In-app mirrors: `CreditsScreen` strings `credits_offline_play`, `credits_legal_disclaimer`, `credits_privacy_policy`.

## Graphic assets

| Asset | Spec | Repo source |
|-------|------|-------------|
| **App icon** | Adaptive icon layers | [`Public-Assets/adaptive-icon/`](../Public-Assets/adaptive-icon/) → `android/app/src/main/res/` (checklist **1.5**) |
| **Feature graphic** | 1024×500 PNG/JPG | Compose: `#181a25` background + [`Public-Assets/app-logo.svg`](../Public-Assets/app-logo.svg) + title lockup (export at ship) |
| **Phone screenshots** | Min 2; 16:9 or device frames | [`Public-Assets/screen-previews/splash-screen.png`](../Public-Assets/screen-previews/splash-screen.png), [`studio-screen.png`](../Public-Assets/screen-previews/studio-screen.png); add in-dynasty shots before submission |
| **Promo video** | Optional | — |

## Data safety form (Google Play)

Declare:

- **No data collected** for core offline play (align with `PRIVACY_POLICY.md`).
- **Optional network** for user-initiated pack URL import only.
- Dynasty files **not shared** with third parties by the app.

## Pre-submission checklist

- [ ] Host privacy policy at `PRIVACY_POLICY_URL` (or update constant + this doc).
- [ ] Short description ≤ 80 characters (`PlayStoreListingCopyTest`).
- [ ] Feature graphic exported to `docs/store/exports/` or Play Console upload buffer (not committed if large binaries preferred).
- [ ] Store listing proofread for banned endorsement language (no “official NCAA”).

## Document history

| Date | Change |
|------|--------|
| 2026-11-10 | Initial listing copy, asset map, privacy cross-link (checklist **16.5**) |
