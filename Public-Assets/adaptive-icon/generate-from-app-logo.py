#!/usr/bin/env python3
"""Build adaptive launcher assets from Public-Assets/app-logo.svg.

Foreground: crop the top icon mark from the composed logo (same art as app-logo).
Canvas: full app-logo lockup on #181a25 at proper scale (not a shrunk raster stamp).
"""
from __future__ import annotations

import base64
import io
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
APP_LOGO = ROOT / "app-logo.svg"
OUT = Path(__file__).resolve().parent
BRAND_BG = (0x18, 0x1A, 0x25, 255)

# Icon <image> 158x158 with matrix(1.414557, 0, 0, 1.414557, 500, 0) in app-logo.svg
ICON_CROP = (465, 0, 760, 260)  # viewBox px padding around computed bounds
EXPORT_PX = 432


def png_to_data_uri(png: bytes) -> str:
    return "data:image/png;base64," + base64.b64encode(png).decode("ascii")


def main() -> None:
    import cairosvg
    from PIL import Image

    png_bytes = cairosvg.svg2png(
        url=str(APP_LOGO),
        output_width=1224,
        output_height=1500,
        background_color="none",
    )
    full = Image.open(io.BytesIO(png_bytes)).convert("RGBA")

    icon = full.crop(ICON_CROP)
    iw, ih = icon.size
    side = max(iw, ih)
    square = Image.new("RGBA", (side, side), (0, 0, 0, 0))
    square.paste(icon, ((side - iw) // 2, (side - ih) // 2))
    foreground = square.resize((EXPORT_PX, EXPORT_PX), Image.Resampling.LANCZOS)
    foreground.save(OUT / "adaptive-icon-foreground.png")

    margin = int(EXPORT_PX * 0.06)
    avail = EXPORT_PX - 2 * margin
    scale = min(avail / full.width, avail / full.height)
    nw, nh = int(full.width * scale), int(full.height * scale)
    resized = full.resize((nw, nh), Image.Resampling.LANCZOS)
    canvas = Image.new("RGBA", (EXPORT_PX, EXPORT_PX), BRAND_BG)
    canvas.paste(resized, ((EXPORT_PX - nw) // 2, (EXPORT_PX - nh) // 2), resized)
    canvas.save(OUT / "adaptive-icon-canvas.png")

    fg_uri = png_to_data_uri((OUT / "adaptive-icon-foreground.png").read_bytes())
    cv_uri = png_to_data_uri((OUT / "adaptive-icon-canvas.png").read_bytes())

    (OUT / "adaptive-icon-foreground.svg").write_text(
        f"""<?xml version="1.0" encoding="UTF-8"?>
<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 108 108" width="108" height="108">
  <image width="108" height="108" preserveAspectRatio="xMidYMid meet" xlink:href="{fg_uri}"/>
</svg>
""",
        encoding="utf-8",
    )
    (OUT / "adaptive-icon-canvas.svg").write_text(
        f"""<?xml version="1.0" encoding="UTF-8"?>
<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 108 108" width="108" height="108">
  <image width="108" height="108" preserveAspectRatio="xMidYMid meet" xlink:href="{cv_uri}"/>
</svg>
""",
        encoding="utf-8",
    )

    cairosvg.svg2png(
        url=str(OUT / "adaptive-icon-background.svg"),
        write_to=str(OUT / "adaptive-icon-background.png"),
        output_width=EXPORT_PX,
        output_height=EXPORT_PX,
    )
    print("OK from", APP_LOGO)


if __name__ == "__main__":
    main()
