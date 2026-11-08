# Adaptive app icons — Dynasty College Basketball

Android **adaptive icon** layers (108×108 dp). Brand background: `#181a25`.

**Source of truth:** [`../app-logo.svg`](../app-logo.svg) only — not Novux studio marks.

| File | Role |
|------|------|
| `adaptive-icon-background.svg` | Launcher **background** (`#181a25`) |
| `adaptive-icon-foreground.svg` | **Icon only** — 158×158 raster layer embedded in `app-logo.svg` |
| `adaptive-icon-canvas.svg` | **Preview** — `#181a25` + full logo composite from `app-logo.svg` (765×726 layer) |
| `generate-from-app-logo.py` | Regenerate foreground/canvas/PNGs after `app-logo.svg` changes |

**PNG** (432×432): `*.png` alongside each SVG for Android `res/` import.

Extracted reference PNGs: `app-logo-extract-icon-158.png`, `app-logo-extract-full-765x726.png`.
