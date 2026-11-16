#!/usr/bin/env python3
"""Build Android adaptive icon layers from Public-Assets/app-logo.png.

The shipped app-logo.svg is a C2PA export with an opaque artboard — not suitable for
a transparent adaptive *foreground*. The PNG is the raster master (same art as splash).

Authoritative outputs: Public-Assets/adaptive-icon/
Android sync: android/app/src/main/res/drawable-nodpi/ic_launcher_foreground.png

Spec: planning/branding.md § Adaptive icon (Android) — foreground fits ~66dp safe zone on 108dp layer.
"""
from __future__ import annotations

import argparse
import shutil
import sys
from pathlib import Path

import numpy as np

CANVAS_PX = 432
SAFE_ZONE_RATIO = 0.58  # central mark vs 108dp adaptive canvas (~66dp safe diameter)
BRAND_BG = (0x18, 0x1A, 0x25, 0xFF)
# Corners of app-logo.png match adaptive background; knock out so only the mark remains.
KNOCKOUT_RGB = (24, 26, 37)
KNOCKOUT_TOLERANCE = 40


def repo_root() -> Path:
    return Path(__file__).resolve().parents[2]


def knock_out_brand_background(image) -> None:
    """In-place: brand fill → transparent; keep white/gold logo strokes."""
    from PIL import Image

    if image.mode != "RGBA":
        image = image.convert("RGBA")
    arr = np.array(image).astype(np.int16)
    rgb = arr[:, :, :3]
    target = np.array(KNOCKOUT_RGB, dtype=np.int16)
    match = np.abs(rgb - target).max(axis=2) <= KNOCKOUT_TOLERANCE
    alpha = arr[:, :, 3]
    alpha[(match) & (alpha > 0)] = 0
    arr[:, :, 3] = alpha
    image.paste(Image.fromarray(arr.astype(np.uint8), "RGBA"))


def crop_to_content(image, min_alpha: int = 10, pad_ratio: float = 0.02):
    from PIL import Image

    arr = np.array(image.convert("RGBA"))
    ys, xs = np.where(arr[:, :, 3] > min_alpha)
    if len(xs) == 0:
        raise SystemExit("Logo mark has no visible pixels after background knock-out")
    span = max(xs.max() - xs.min(), ys.max() - ys.min())
    pad = max(4, int(pad_ratio * span))
    left = max(0, int(xs.min()) - pad)
    top = max(0, int(ys.min()) - pad)
    right = min(image.width, int(xs.max()) + pad + 1)
    bottom = min(image.height, int(ys.max()) + pad + 1)
    return image.crop((left, top, right, bottom))


def fit_mark_in_safe_zone(mark, safe_px: int):
    from PIL import Image

    cw, ch = mark.size
    scale = min(safe_px / cw, safe_px / ch)
    nw = max(1, int(round(cw * scale)))
    nh = max(1, int(round(ch * scale)))
    return mark.resize((nw, nh), Image.Resampling.LANCZOS)


def generate(logo_png: Path, out_dir: Path, android_fg: Path) -> None:
    try:
        from PIL import Image
    except ImportError as err:
        raise SystemExit(
            "Missing deps for icon generation. Run: pip install pillow\n"
            f"({err})",
        ) from err

    mark = Image.open(logo_png).convert("RGBA")
    knock_out_brand_background(mark)
    mark = crop_to_content(mark)

    safe_px = int(CANVAS_PX * SAFE_ZONE_RATIO)
    mark = fit_mark_in_safe_zone(mark, safe_px)

    foreground = Image.new("RGBA", (CANVAS_PX, CANVAS_PX), (0, 0, 0, 0))
    ox = (CANVAS_PX - mark.width) // 2
    oy = (CANVAS_PX - mark.height) // 2
    foreground.paste(mark, (ox, oy), mark)

    out_dir.mkdir(parents=True, exist_ok=True)
    fg_path = out_dir / "adaptive-icon-foreground.png"
    foreground.save(fg_path)

    preview_bg = Image.new("RGBA", (CANVAS_PX, CANVAS_PX), BRAND_BG)
    preview = Image.alpha_composite(preview_bg, foreground)
    preview.save(out_dir / "adaptive-icon-canvas.png")

    android_fg.parent.mkdir(parents=True, exist_ok=True)
    shutil.copy2(fg_path, android_fg)
    print(f"Wrote {fg_path}")
    print(f"Wrote {out_dir / 'adaptive-icon-canvas.png'}")
    print(f"Synced {android_fg}")


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument(
        "--check",
        action="store_true",
        help="Regenerate to a temp file and compare to android foreground (exit 1 if mismatch)",
    )
    args = parser.parse_args()
    root = repo_root()
    logo_png = root / "Public-Assets/app-logo.png"
    out_dir = root / "Public-Assets/adaptive-icon"
    android_fg = root / "android/app/src/main/res/drawable-nodpi/ic_launcher_foreground.png"
    if not logo_png.is_file():
        print(f"Missing {logo_png}", file=sys.stderr)
        return 1
    if args.check:
        import tempfile

        with tempfile.TemporaryDirectory() as tmp:
            tmp_fg = Path(tmp) / "fg.png"
            generate(logo_png, Path(tmp), tmp_fg)
            if not android_fg.exists() or android_fg.read_bytes() != tmp_fg.read_bytes():
                print(
                    "Android ic_launcher_foreground.png is out of date. "
                    "Run: python3 planning/tools/generate_adaptive_icon.py",
                    file=sys.stderr,
                )
                return 1
        print("Brand icon assets in sync.")
        return 0
    generate(logo_png, out_dir, android_fg)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())