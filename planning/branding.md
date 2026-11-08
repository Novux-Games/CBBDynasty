# Branding — Dynasty College Basketball

**Last updated:** 2025-11-08

---

## Product identity

| Field | Value |
|-------|--------|
| **Game title** | **Dynasty College Basketball** |
| **Repo / code name** | `CBBDynasty` (workspace) |
| **Studio** | **Novux Studios** |
| **Studio URL** | [https://novux.dev](https://novux.dev) |

Use **Dynasty College Basketball** on store listings, splash, and in-app title bar unless a shorter label is required (e.g. “Dynasty CBB” for icon-safe text — TBD).

---

## Logo & assets

| Asset | Path | Use |
|-------|------|-----|
| **Primary app logo** | [`Public-Assets/app-logo.svg`](../Public-Assets/app-logo.svg) | In-app header, marketing, splash source |
| **Novux logos** | `Public-Assets/Novux_Logos/` | Studio lockups |
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

| Layer | Spec |
|-------|------|
| **Background** | Solid `#181a25` |
| **Foreground** | `app-logo.svg` (or exported foreground PNG/SVG safe zone) |
| **Shape** | System mask (circle / squircle); keep critical logo art in center **66%** safe zone |

---

## Legal / attribution

- Splash or settings: **“© Novux Studios”** + link to [novux.dev](https://novux.dev) (exact copy TBD with counsel).
- Game is **unofficial** college basketball sim (see [`game-design.md`](game-design.md) identity packs).

---

## Related planning

| Doc | Link |
|-----|------|
| Game systems | [`game-design.md`](game-design.md) |
| In-season UI | [`game-frame.md`](game-frame.md) |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Initial branding: title, logo path, studio, `#181a25` |
