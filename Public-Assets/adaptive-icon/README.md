# Adaptive app icons

**Source:** [`../app-logo.svg`](../app-logo.svg) only.

| Asset | What it is |
|-------|------------|
| `adaptive-icon-background.svg` | Solid `#181a25` (Android background layer) |
| `adaptive-icon-foreground.svg` / `.png` | **Icon mark only** — cropped from the top icon layer in `app-logo.svg`, scaled to fill the foreground layer |
| `adaptive-icon-canvas.svg` / `.png` | **Full logo lockup** on `#181a25`, scaled to fit the square (~6% margin) — store / preview, not a tiny pasted bitmap |

Regenerate after logo changes:

```bash
python3 Public-Assets/adaptive-icon/generate-from-app-logo.py
```

Do **not** use Novux studio icons or hand-place small PNGs in the corner of a 108dp canvas.
