#!/usr/bin/env python3
"""Regenerate adaptive icon SVG/PNG from Public-Assets/app-logo.svg."""
from __future__ import annotations

import base64
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
APP_LOGO = ROOT / "app-logo.svg"
OUT = Path(__file__).resolve().parent


def parse_images(svg: str) -> list[tuple[int, int, str]]:
    imgs: list[tuple[int, int, str]] = []
    for m in re.finditer(r"<image\s+(.*?)/>", svg, re.DOTALL):
        tag = m.group(1)
        w = int(re.search(r'width="(\d+)"', tag).group(1))
        h = int(re.search(r'height="(\d+)"', tag).group(1))
        b64 = re.search(r'xlink:href="data:image/png;base64,([^"]+)"', tag).group(1)
        imgs.append((w, h, b64))
    return imgs


def main() -> None:
    svg = APP_LOGO.read_text(encoding="utf-8", errors="ignore")
    imgs = parse_images(svg)
    if len(imgs) < 3:
        raise SystemExit(f"Expected embedded images in {APP_LOGO}")

    # First 158x158 = icon layer; first 765x726 = full mark from app-logo.svg
    icon_b64 = imgs[0][2]
    full_b64 = imgs[2][2]

    (OUT / "app-logo-extract-icon-158.png").write_bytes(base64.b64decode(icon_b64))
    (OUT / "app-logo-extract-full-765x726.png").write_bytes(base64.b64decode(full_b64))

    fg = f"""<?xml version="1.0" encoding="UTF-8"?>
<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 108 108" width="108" height="108">
  <image xlink:href="data:image/png;base64,{icon_b64}" x="18" y="18" width="72" height="72" preserveAspectRatio="xMidYMid meet"/>
</svg>
"""

    w, h = 765, 726
    scale = min(92 / w, 92 / h)
    nw, nh = w * scale, h * scale
    x, y = (108 - nw) / 2, (108 - nh) / 2
    cv = f"""<?xml version="1.0" encoding="UTF-8"?>
<svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 108 108" width="108" height="108">
  <rect width="108" height="108" fill="#181a25"/>
  <image xlink:href="data:image/png;base64,{full_b64}" x="{x:.4f}" y="{y:.4f}" width="{nw:.4f}" height="{nh:.4f}" preserveAspectRatio="xMidYMid meet"/>
</svg>
"""

    (OUT / "adaptive-icon-foreground.svg").write_text(fg, encoding="utf-8")
    (OUT / "adaptive-icon-canvas.svg").write_text(cv, encoding="utf-8")

    try:
        import cairosvg

        for name in (
            "adaptive-icon-background",
            "adaptive-icon-foreground",
            "adaptive-icon-canvas",
        ):
            cairosvg.svg2png(
                url=str(OUT / f"{name}.svg"),
                write_to=str(OUT / f"{name}.png"),
                output_width=432,
                output_height=432,
            )
    except ImportError:
        print("cairosvg not installed; SVG only")

    print("Generated from", APP_LOGO)


if __name__ == "__main__":
    main()
