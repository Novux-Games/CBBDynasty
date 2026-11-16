#!/usr/bin/env python3
"""Compose Android adaptive icon layers from Public-Assets/adaptive-icon/.

Inputs (do not regenerate from app-logo):
  - adaptive-icon-foreground.png  — authored transparent logo layer (432×432, 100%)
  - adaptive-icon-background.svg  — brand fill (#181a25), full 108×108 dp layer

Outputs:
  - adaptive-icon-canvas.png      — QA preview (full background + scaled foreground)
  - android/.../ic_launcher_foreground.png — shipped foreground (logo at 70% scale)

Foreground scales to 70%; background does not — keeps the mark inside the adaptive safe zone.
"""
from __future__ import annotations

import argparse
import io
import sys
from pathlib import Path

CANVAS_PX = 432
FOREGROUND_SCALE = 0.7
BRAND_BG_HEX = "#181a25"
BRAND_BG_RGBA = (0x18, 0x1A, 0x25, 0xFF)


def repo_root() -> Path:
    return Path(__file__).resolve().parents[2]


def raster_background(svg_path: Path, px: int):
    from PIL import Image

    if svg_path.is_file():
        try:
            import cairosvg
        except ImportError:
            pass
        else:
            text = svg_path.read_text(encoding="utf-8")
            if BRAND_BG_HEX.lower() in text.lower():
                return Image.new("RGBA", (px, px), BRAND_BG_RGBA)
            png = cairosvg.svg2png(url=str(svg_path), output_width=px, output_height=px)
            return Image.open(io.BytesIO(png)).convert("RGBA")
    return Image.new("RGBA", (px, px), BRAND_BG_RGBA)


def scale_foreground_layer(foreground, scale: float):
    from PIL import Image

    nw = max(1, int(round(foreground.width * scale)))
    nh = max(1, int(round(foreground.height * scale)))
    scaled = foreground.resize((nw, nh), Image.Resampling.LANCZOS)
    layer = Image.new("RGBA", (CANVAS_PX, CANVAS_PX), (0, 0, 0, 0))
    ox = (CANVAS_PX - nw) // 2
    oy = (CANVAS_PX - nh) // 2
    layer.paste(scaled, (ox, oy), scaled)
    return layer


def compose(icon_dir: Path, android_fg: Path) -> None:
    try:
        from PIL import Image
    except ImportError as err:
        raise SystemExit(
            "Missing deps for icon generation. Run: pip install pillow\n"
            f"({err})",
        ) from err

    source_fg = icon_dir / "adaptive-icon-foreground.png"
    bg_svg = icon_dir / "adaptive-icon-background.svg"
    if not source_fg.is_file():
        raise SystemExit(f"Missing authored foreground: {source_fg}")

    authored = Image.open(source_fg).convert("RGBA")
    shipped_fg = scale_foreground_layer(authored, FOREGROUND_SCALE)

    background = raster_background(bg_svg, CANVAS_PX)
    canvas = Image.alpha_composite(background, shipped_fg)
    canvas_path = icon_dir / "adaptive-icon-canvas.png"
    canvas.save(canvas_path)

    android_fg.parent.mkdir(parents=True, exist_ok=True)
    shipped_fg.save(android_fg)

    print(f"Foreground scale: {FOREGROUND_SCALE:.0%} (background {CANVAS_PX}px)")
    print(f"Wrote {canvas_path}")
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
    icon_dir = root / "Public-Assets/adaptive-icon"
    android_fg = root / "android/app/src/main/res/drawable-nodpi/ic_launcher_foreground.png"

    if args.check:
        import tempfile

        with tempfile.TemporaryDirectory() as tmp:
            tmp_fg = Path(tmp) / "ic_launcher_foreground.png"
            compose(icon_dir, tmp_fg)
            if not android_fg.exists() or android_fg.read_bytes() != tmp_fg.read_bytes():
                print(
                    "Android ic_launcher_foreground.png is out of date. "
                    "Run: python3 planning/tools/generate_adaptive_icon.py",
                    file=sys.stderr,
                )
                return 1
        print("Brand icon assets in sync.")
        return 0

    compose(icon_dir, android_fg)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())