# Branding — Dynasty: College Basketball

**Last updated:** 2025-11-08

---

## Product identity

| Field | Value |
|-------|--------|
| **Game title** | **Dynasty: College Basketball** |
| **Repo / code name** | `CBBDynasty` (workspace) |
| **Studio** | **Novux Games** |
| **Studio URL** | [https://novux.dev](https://novux.dev) |

Use **Dynasty: College Basketball** on store listings, splash, and in-app title bar unless a shorter label is required (e.g. “Dynasty CBB” for icon-safe text — TBD).

---

## Logo & assets

| Asset | Path | Use |
|-------|------|-----|
| **Primary app logo** | [`Public-Assets/app-logo.svg`](../Public-Assets/app-logo.svg) | In-app header, marketing, **game splash** |
| **Splash screen reference** | [`Public-Assets/screen-previews/splash-screen.png`](../Public-Assets/screen-previews/splash-screen.png) | Marketing / QA mock (1080×2400) |
| **Novux logos** | `Public-Assets/Novux_Logos/` | **Studio screen** lockups (`2.svg` = light on `#181a25`) |
| **Studio screen reference** | [`Public-Assets/screen-previews/studio-screen.png`](../Public-Assets/screen-previews/studio-screen.png) | Marketing / QA mock (1080×2400) |
| **Novux icons** | `Public-Assets/Novux_Icons/` | Studio mark variants |

**Implementation notes**

- Prefer **vector** `app-logo.svg` for scalable UI; generate raster mipmaps for Android as needed.
- Keep logo **clear space** ≥ ½ logo height on marketing; min touch target unrelated (UI chrome).

---

## Color

| Role | Hex | Use |
|------|-----|-----|
| **App / adaptive icon background** | `#181a25` | Android adaptive icon background layer, default **game chrome** backdrop (menus, loading, empty states) |

**`#181a25`** — dark blue-gray; primary **brand field**. Pair with light text and accent colors when UI palette doc exists.

Suggested contrast (for implementation QA):

- Body text on `#181a25`: **#FFFFFF** or **#E8EAED** primary; **#A8AEB8** secondary.
- Accent / CTA: define in UI theme doc (not locked here).

---

## Adaptive icon (Android)

Android adaptive layers in [`Public-Assets/adaptive-icon/`](../Public-Assets/adaptive-icon/):

| File | Role |
|------|------|
| `adaptive-icon-background.svg` | Background layer — `#181a25` |
| `adaptive-icon-foreground.png` | Foreground layer — transparent logo (432×432) |
| `adaptive-icon-canvas.png` | `#181A25` + `adaptive-icon-foreground.png` composited (logo on brand background only) |

---

## Legal / attribution

- Repository: **open code / closed art** — [`docs/LICENSING.md`](../docs/LICENSING.md), [`Public-Assets/LICENSE`](../Public-Assets/LICENSE).
- Splash footer + Home **Credits**: **© Novux Games**, [novux.dev](https://novux.dev) (`CreditsScreen`).
- Game is **unofficial** college basketball sim (see [`game-design.md`](game-design.md) identity packs).

---

## Related planning

| Doc | Link |
|-----|------|
| Game systems | [`game-design.md`](game-design.md) |
| In-season UI | [`game-frame.md`](game-frame.md) |
| League names (real ↔ universe) | [`league-branding.md`](league-branding.md) · reference [`2026-NCAA-Directory.md`](2026-NCAA-Directory.md) |
| NCBA / March Mayhem / early events | [`ncba-branding.md`](ncba-branding.md) |
| Postseason brackets & Selection Sunday | [`postseason.md`](postseason.md) |
| Studio & splash | [`app-shell.md`](app-shell.md) |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Initial branding: title, logo path, studio, `#181a25` |
| 2025-11-08 | Official title: **Dynasty: College Basketball** |
| 2025-11-08 | Link `ncba-branding.md` |
| 2025-11-09 | Novux Games; `app-shell.md` studio vs splash |
