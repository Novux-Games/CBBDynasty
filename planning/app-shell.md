# App shell — studio screen & splash

**Platform:** Android · pre-menu flow only (no dynasty loaded).  
**Branding:** [`branding.md`](branding.md) · **Menu:** [`game-start.md`](game-start.md#home-screen).

**Last updated:** 2025-11-09

---

## Contents

1. [Launch flow](#launch-flow)
2. [Studio screen (Novux Games)](#studio-screen-novux-games)
3. [Splash loading screen](#splash-loading-screen)
4. [Loading & errors](#loading--errors)
5. [Cross-links](#cross-links)

---

## Launch flow

Cold start sequence:

```
App process start
      │
      ▼
┌─────────────────┐
│  Studio screen  │  Novux Games (publisher identity)
└────────┬────────┘
         │ auto (≈2s) or tap to skip
         ▼
┌─────────────────┐
│ Splash / load   │  Game logo + title + Tap to Play
└────────┬────────┘
         │ tap + assets/saves ready
         ▼
┌─────────────────┐
│  Home menu      │  Continue · New · Load · Settings · Credits
└─────────────────┘
```

**Resume from background:** Skip **Studio screen**; optionally show abbreviated splash if reload needed, else straight to **Home** or in-dynasty shell if save was active.

---

## Studio screen (Novux Games)

**Purpose:** A **publisher-first** beat — visually distinct from the game splash. Establishes **Novux Games**, not the CBB product.

| Element | Spec |
|---------|------|
| **Background** | **`#181a25`** full bleed ([`branding.md`](branding.md#color)) |
| **Mark** | **`Public-Assets/Novux_Logos/`** or **`Novux_Icons/`** — primary studio lockup (vector preferred) |
| **Motion** | Subtle fade-in / optional short logo sting (ship TBD) |
| **Text** | **© {current year} Novux Games** + **novux.dev** below lockup; tap hint below that |
| **Input** | **Tap anywhere** to skip after min display time |

**Design lock:** Do **not** use the **Dynasty: College Basketball** app logo as the hero on this screen — studio identity only.

| When shown |
|------------|
| Every **cold start** (before splash), unless user enables **Skip studio intro** in Settings |
| **Credits** → **Visit Novux Games** may replay this screen (optional) |

**Duration:** Default **~2.0s** auto-advance to splash; tap skips after **0.5s** minimum (avoid accidental skip on load).

---

## Splash loading screen

**Purpose:** Game identity + lightweight load gate before the **home menu**.

| Element | Spec |
|---------|------|
| **Background** | **`#181a25`** |
| **Logo** | [`Public-Assets/app-logo.svg`](../Public-Assets/app-logo.svg) (or raster derived) — centered, clear space per branding |
| **Title** | **Dynasty: College Basketball** (full official title) |
| **CTA** | **Tap to Play** — primary prompt when ready |
| **Loading** | Optional progress indicator while default pack / save index loads **before** CTA enables |

### Interaction

| State | UI |
|-------|-----|
| **Loading** | Logo + title visible; **Tap to Play** hidden or disabled; spinner / slim progress bar |
| **Ready** | **Tap to Play** appears (pulse or underline); single tap → **Home menu** ([`game-start.md`](game-start.md)) |
| **First launch** | Same flow; no save → **Continue** hidden on home |

**Design lock:** Splash is **not** the studio screen — no Novux Games hero logo here (small **© Novux Games** footer line is OK).

### Layout (wire)

```
┌─────────────────────────┐
│                         │
│      [ app-logo ]       │
│                         │
│ Dynasty: College        │
│      Basketball         │
│                         │
│    ( loading… )         │  ← only while booting
│                         │
│     Tap to Play         │  ← when ready
│                         │
│   © {year} Novux Games  │
│   Game Director line    │  ← Austin Buhl / austinbuhltech.com
└─────────────────────────┘
```

---

## Loading & errors

| Case | Behavior |
|------|----------|
| **Pack / asset fail** | Stay on splash; error copy + **Retry** (no home until critical assets OK) |
| **Save index corrupt** | Allow **Tap to Play** → home; **Load** shows repair/delete per [`game-start.md`](game-start.md) |
| **Mid-dynasty resume** | Not this doc — app restores last route after home **Continue** |

Boot work on splash: mount default identity pack, enumerate local saves, apply Settings (audio, skip studio flag).

---

## Cross-links

| Topic | Doc |
|-------|-----|
| Colors & logos | [`branding.md`](branding.md) |
| Home menu | [`game-start.md`](game-start.md) |
| In-dynasty UI | [`game-frame.md`](game-frame.md) |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-09 | Studio screen + splash Tap to Play flow |
