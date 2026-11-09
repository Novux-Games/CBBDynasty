# Game start — home screen, new dynasty, coach creation

**Platform:** Android · offline local saves  
**Before menu:** [`app-shell.md`](app-shell.md) — Novux **Studio screen** → game **Splash** → **Tap to Play** → home below.  
**After sign contract:** Dynasty bootstrap → preseason / Season 1 ([`game-design.md`](game-design.md)) → in-season shell ([`game-frame.md`](game-frame.md)).  
**Companion:** Coach profile [`coach-card.md`](coach-card.md), schemes [`strategy-schemes.md`](strategy-schemes.md), identity packs [`game-design.md`](game-design.md#identity-packs--save-model), league data [`league-branding.md`](league-branding.md).

**Last updated:** 2025-11-09

---

## Contents

1. [Home screen](#home-screen)
2. [Continue & Load](#continue--load)
3. [New game wizard (overview)](#new-game-wizard-overview)
4. [Step 1 — Coach info & appearance](#step-1--coach-info--appearance)
5. [Step 2 — Archetype](#step-2--archetype)
6. [Step 3 — Schemes](#step-3--schemes)
7. [Step 4 — New-game settings & import](#step-4--new-game-settings--import)
8. [Step 5 — Choose school](#step-5--choose-school)
9. [Step 6 — Sign contract & launch](#step-6--sign-contract--launch)
10. [Home Settings & Credits](#home-settings--credits)
11. [Save bootstrap](#save-bootstrap)
12. [Cross-links](#cross-links)

---

## Home screen

Cold start lands on the **title / home** screen (no bottom nav — pre-dynasty).

| Button | Visibility | Action |
|--------|------------|--------|
| **Continue** | **Hidden** if no save exists; else visible | Load **most recent** dynasty save → resume calendar position |
| **New** | Always | Start [new game wizard](#new-game-wizard-overview) |
| **Load** | Always | Save picker — **10 slots** (empty or occupied), metadata: school, season, record |
| **Settings** | Always | [App settings](#home-settings--credits) (not dynasty rules) |
| **Credits** | Always | Novux Games, OSS, disclaimer |

```
┌─────────────────────────┐
│      [App logo]         │
│  Dynasty: College BB    │
│                         │
│      [ Continue ]       │  ← only if saves exist
│      [ New ]            │
│      [ Load ]           │
│      [ Settings ]         │
│      [ Credits ]        │
└─────────────────────────┘
```

---

## Continue & Load

| Action | Behavior |
|--------|----------|
| **Continue** | Same as Load on **latest `lastPlayedAt`** save |
| **Load** | **10 slots**; show school display name, coach name, season year, W–L; tap → validate pack id → **`game-frame`** or offseason UI. **Export/import** dynasty archives per [ADR-001](../docs/ADR-001-dynasty-save-backend.md). |

Corrupt save → error sheet; offer delete slot.

### Save slots (implemented)

| Piece | Location |
|-------|----------|
| **10 slots**, Room `save_slots` | [`android/database`](../android/database/) |
| **List / load / delete / last played** | [`SaveSlotRepository`](../android/app/src/main/kotlin/dev/novux/dynasty/cbb/data/save/SaveSlotRepository.kt) |
| **Continue** | Latest `lastPlayedAtEpochMillis` among occupied slots |
| **Load UI** | [`LoadGameScreen`](../android/app/src/main/kotlin/dev/novux/dynasty/cbb/ui/shell/LoadGameScreen.kt) |
| **Models** | [`SaveSlotSummary`](../android/core/src/main/kotlin/dev/novux/dynasty/cbb/core/save/SaveSlotSummary.kt), [`SaveSlots.COUNT`](../android/core/src/main/kotlin/dev/novux/dynasty/cbb/core/save/SaveSlots.kt) |

New Game still creates dynasties in a later checklist step; occupied slots appear after dynasty creation or import.

---

## New game wizard (overview)

Linear flow with **Back** on each step. Progress indicator (steps 1–6).

```
New
  → 1 Coach info + appearance
  → 2 Archetype
  → 3 Offensive + defensive scheme
  → 4 New-game settings (+ optional identity import)
  → 5 Choose school (+ contract summary)
  → 6 Sign contract
  → Dynasty created → preseason / Season 1
```

**Defaults:** **Season 1** era **2026–27**; default identity pack unless import overrides ([`league-branding.md`](league-branding.md)).

---

## Step 1 — Coach info & appearance

### Identity fields

| Field | UI | Save |
|-------|-----|------|
| **First name** | Text | `coach.firstName` |
| **Last name** | Text | `coach.lastName` |
| **Suffix** | Optional dropdown: *None*, **Jr.**, **II**, **III**, **IV** | `coach.nameSuffix` |
| **Age** | Integer (e.g. **28–65** default band at create) | `coach.age` — shown on coach card ([`coach-card.md`](coach-card.md)) |
| **Alma mater** | Searchable school list from active pack (+ **None / N/A**) | `coach.almaMaterTeamId` nullable |

**Display name:** `First [Suffix] Last` (e.g. *Marcus Jr. Hayes*).

### Appearance (preset avatars)

Same **preset pipeline** as players ([`player-cards.md`](player-cards.md)) — no photo upload.

| Picker | Options (pack-driven presets) |
|--------|-------------------------------|
| **Skin color** | Preset swatches |
| **Hair color** | Preset swatches |
| **Hair style** | Preset list (short, bald, etc.) |
| **Beard style** | Preset list incl. **None** |

Stored as `avatarPresetId` + component ids for reproducible portrait on coach card.

---

## Step 2 — Archetype

**One** archetype at create — sets **starting skill tree** bias and **Sunday** auto-spend tendencies ([`game-design.md`](game-design.md#coaching-carousel--contracts)).

| Archetype | Identity | Starting bias |
|-----------|----------|----------------|
| **Recruiter** | Pipeline and portal | Recruiting, NIL charm |
| **Scheme Manager** | X’s and O’s | In-game tactics, scouting |
| **Developer** | Player growth | Player dev, summer gains |
| **Program Builder** | Facilities & culture | Portal retention, staff pitch |
| **Balanced** | No spike | Even baseline |

Archetype is **flavor + initial point allocation**; all trees remain unlockable over career.

---

## Step 3 — Schemes

Pick **default** offensive and defensive presets (season baseline; Strategy hub can change per game — [`strategy-schemes.md`](strategy-schemes.md)).

| Picker | Source |
|--------|--------|
| **Offensive scheme** | 7 presets (Balanced, Motion, Drive, Set, Early, Perimeter, Post) |
| **Defensive scheme** | 7 presets (Man, 2-3, 3-2, 1-3-1, Box-and-One, Press, Switching) |

Saved on coach entity; mirrored on coach card **Strategy** block.

---

## Step 4 — New-game settings & import

Distinct from [home Settings](#home-settings--credits) — these apply **only to this new dynasty**.

### Dynasty toggles

| Setting | Default | Behavior |
|---------|---------|----------|
| **Auto-upgrade** | **On** | Player attribute progressions apply automatically when earned from XP/events |
| **Auto-upgrade off** | — | Progressions apply only during **dedicated offseason development window** (recruiting/offseason weeks) — no mid-season silent bumps |
| **Unlimited age** | **Off** | Coach **retires at age 75** (end of that season); user offered **new coach same universe** (program continues under replacement) or exit to menu |
| **Unlimited age on** | — | No forced retirement at 75 |

Persist on save: `dynastySettings.autoUpgrade`, `dynastySettings.unlimitedCoachAge`.

### Identity import (optional)

| Method | Detail |
|--------|--------|
| **Import URL** | HTTPS fetch; size/signature rules per [`game-design.md`](game-design.md#identity-packs--save-model) |
| **Import TXT** | Single text file; validated then converted to pack overlay |

**Help (ⓘ):** Popup explains unofficial disclaimer, stable IDs, and links **Download template**.

**TXT template sections** (one file; line-oriented or simple blocks — exact grammar in ship template file):

| Section | Content |
|---------|---------|
| **Teams** | `teamId`, display name, conference id, city, state, prestige |
| **Conferences** | `confId`, display name, schedule rules refs |
| **Tournaments** | Conference / holiday / national template ids |
| **Branding** | NCBA, March Mayhem, NCI, Heritage Classic display strings ([`ncba-branding.md`](ncba-branding.md)) |

**Also supported:** ship default **JSON** pack import on same step (parity with GDD).

Invalid import → block **Next** with errors; dynasty starts with **embedded default pack** if user skips import.

---

## Step 5 — Choose school

### School list filter

| Mode | Rule |
|------|------|
| **Default** | Only programs with **`programPrestige ≤ 40`** selectable |
| **Unlock all** | Toolbar **Unlock** icon (top-right) toggles **all 365** programs for this wizard session (optional: remember in app prefs) |

List: conference filter, search, sort by prestige / name. Row shows logo, name, conference, prestige.

### School summary sheet

Tap school → bottom sheet / full screen **before** sign:

| Block | Content |
|-------|---------|
| **Prestige** | **0–99** |
| **Conference** | Display name |
| **Location** | **City, State** ([`league-branding.md`](league-branding.md)) |
| **Pipeline states** | **Home state** + pack **`pipelineStates`** (in-state / regional recruiting footprint) |
| **Contract** | **Years offered** (typical **4–6** at create), **salary** ($) |
| **Recruiting points** | Starting **weekly cap** from school grades + prestige tier |
| **NIL budget** | Annual pool **$** — conference + prestige scaled (e.g. SEC flagship band **~$9.7M–$13.2M** at high prestige; mid/low majors downscale on prestige curve) |
| **School demeanor** | **Patient** · **Neutral** · **Reactionary** — AD trust / extension temperament ([`coach-card.md`](coach-card.md#staff-school-hub)) |

**Recruiting / NIL numbers** are **preview** from pack tables; final values lock on sign.

Primary CTA: **Sign contract** (disabled until user reviews sheet once — optional friction).

---

## Step 6 — Sign contract & launch

1. **Signature panel:** Auto-render **script-style** signature from entered **first + last** (+ suffix); user confirms.
2. **Next** → create save file, bind `coachId` + `teamId`, apply schemes/archetype/settings/pack.
3. Transition → **Offseason Week 1 — Players Leaving** ([`offseason.md`](offseason.md)); no **Coaching Carousel** on Season 1. After weeks 1–8 → **preseason** → **[`game-frame.md`](game-frame.md)** in-season shell.

**Cannot return** to home without save unless user cancels before step 6 confirm.

---

## Home Settings & Credits

### Settings (home — app level)

Audio, notifications default, save backup location (local), text size, legal links — **do not** duplicate Auto-upgrade / Unlimited age (dynasty-only).

**Implemented:** DataStore [`AppSettings`](../android/core/src/main/kotlin/dev/novux/dynasty/cbb/core/settings/AppSettings.kt) — skip studio intro; SFX / music toggles and master volume (persisted; audio engine ships with in-game audio).

### Credits

Novux Games, third-party licenses, unofficial / no NCAA endorsement ([`branding.md`](branding.md)).

**Implemented:** Home → **Credits** — publisher, novux.dev, game director, copyright year (`CreditsScreen`).

---

## Save bootstrap

On successful sign:

| Field | Initial value |
|-------|----------------|
| `dynasty.startYear` | **2026–27** (Season 1) |
| `coach` | Wizard fields + archetype + avatar |
| `teamId` | Chosen program |
| `contract` | Years, salary, goals from school tier |
| `packId` | Default or imported |
| `dynastySettings` | Auto-upgrade, unlimited age |
| Program state | Pack prestige, demeanor, NIL pool, facilities baseline |

First **week advance** checkpoint rules unchanged ([`game-design.md`](game-design.md#simulation-engine-brief)).

---

## Cross-links

| Topic | Doc |
|-------|-----|
| In-season UI after start | [`game-frame.md`](game-frame.md) |
| Coach card fields | [`coach-card.md`](coach-card.md) |
| Schemes | [`strategy-schemes.md`](strategy-schemes.md) |
| Pack schema (JSON) | [`game-design.md`](game-design.md#identity-packs--save-model) |
| Recruiting / NIL economy | [`recruiting-engine.md`](recruiting-engine.md) |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-09 | Initial game start: home, wizard, school pick, import TXT, toggles |
