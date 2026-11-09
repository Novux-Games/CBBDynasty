#!/usr/bin/env python3
"""Composite splash/studio preview PNGs with explicit Y positions (no overlapping text)."""

from __future__ import annotations

import datetime
from pathlib import Path

from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "Public-Assets" / "screen-previews"
W, H = 1080, 2400
BG = (0x18, 0x1A, 0x25)
MUTED = (0xA8, 0xAE, 0xB8)
ON_BG = (0xE8, 0xEA, 0xED)
ACCENT = (0x6B, 0x9F, 0xFF)


def load_font(bold: bool = False, size: int = 36) -> ImageFont.FreeTypeFont:
    name = "DejaVuSans-Bold.ttf" if bold else "DejaVuSans.ttf"
    try:
        return ImageFont.truetype(name, size)
    except OSError:
        return ImageFont.load_default()


def paste_centered(canvas: Image.Image, overlay: Image.Image, top: int) -> int:
    x = (canvas.width - overlay.width) // 2
    canvas.paste(overlay, (x, top), overlay if overlay.mode == "RGBA" else None)
    return top + overlay.height


def draw_centered_text(
    draw: ImageDraw.ImageDraw,
    y: int,
    text: str,
    font: ImageFont.FreeTypeFont,
    fill: tuple[int, int, int],
) -> int:
    bbox = draw.textbbox((0, 0), text, font=font)
    text_w = bbox[2] - bbox[0]
    text_h = bbox[3] - bbox[1]
    x = (W - text_w) // 2
    draw.text((x, y), text, font=font, fill=fill)
    return y + text_h


def render_studio(year: int) -> None:
    canvas = Image.new("RGB", (W, H), BG)
    draw = ImageDraw.Draw(canvas)

    lockup = Image.open(
        ROOT / "android/app/src/main/res/drawable-nodpi/novux_studio_lockup.png",
    ).convert("RGBA")
    target_w = 860
    scale = target_w / lockup.width
    lockup = lockup.resize(
        (target_w, int(lockup.height * scale)),
        Image.Resampling.LANCZOS,
    )

    block_h = (
        lockup.height
        + 48
        + 40
        + 16
        + 36
    )
    y = (H - block_h) // 2 - 80
    y = paste_centered(canvas, lockup, y)
    y += 48

    font_copy = load_font(size=34)
    font_url = load_font(size=32)
    font_hint = load_font(size=36)

    y = draw_centered_text(
        draw, y, f"© {year} Novux Games", font_copy, MUTED
    )
    y += 16
    draw_centered_text(draw, y, "novux.dev", font_url, ACCENT)

    hint_y = H - 160
    draw_centered_text(draw, hint_y, "Tap to continue", font_hint, MUTED)

    out = OUT / "studio-screen.png"
    canvas.save(out, optimize=True)
    print(f"Wrote {out}")


def render_splash(year: int) -> None:
    canvas = Image.new("RGB", (W, H), BG)
    draw = ImageDraw.Draw(canvas)

    logo = Image.open(ROOT / "Public-Assets/app-logo.png").convert("RGBA")
    target_w = 440
    scale = target_w / logo.width
    logo = logo.resize(
        (target_w, int(logo.height * scale)),
        Image.Resampling.LANCZOS,
    )

    title_font = load_font(bold=True, size=46)
    cta_font = load_font(size=50)
    footer_font = load_font(size=28)

    title_gap = 128
    title_bbox = draw.textbbox((0, 0), "Dynasty: College Basketball", font=title_font)
    title_h = title_bbox[3] - title_bbox[1]
    cta_h = 50
    mid_block = logo.height + title_gap + title_h + 48 + cta_h
    y = (H // 2) - (mid_block // 2) - 60

    y = paste_centered(canvas, logo, y)
    y += title_gap
    y = draw_centered_text(
        draw, y, "Dynasty: College Basketball", title_font, ON_BG
    )
    y += 48
    draw_centered_text(draw, y, "Tap to Play", cta_font, ACCENT)

    footer_y = H - 200
    footer_y = draw_centered_text(
        draw, footer_y, f"© {year} Novux Games", footer_font, MUTED
    )
    footer_y += 10
    draw_centered_text(
        draw, footer_y, "Game Director: Austin Buhl", footer_font, MUTED
    )

    out = OUT / "splash-screen.png"
    canvas.save(out, optimize=True)
    print(f"Wrote {out}")


def main() -> None:
    year = datetime.date.today().year
    OUT.mkdir(parents=True, exist_ok=True)
    render_studio(year)
    render_splash(year)


if __name__ == "__main__":
    main()
