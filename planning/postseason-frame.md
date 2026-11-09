# Postseason frame — conference play & March Mayhem

**When this applies:** **Conference tournaments** through **national postseason** (not regular-season shell defaults). Rules: [`postseason.md`](postseason.md). Branding: [`ncba-branding.md`](ncba-branding.md). In-season chrome: [`game-frame.md`](game-frame.md).

**Last updated:** 2025-11-09

---

## Contents

1. [Shell modes](#shell-modes)
2. [Tournament seed labels](#tournament-seed-labels)
3. [March Mayhem hub](#march-mayhem-hub)
4. [Conference tournaments (UI)](#conference-tournaments-ui)
5. [Selection Sunday moment](#selection-sunday-moment)
6. [Cross-links](#cross-links)

---

## Shell modes

| Phase | Primary navigation |
|-------|-------------------|
| **Conference tournaments** | **Calendar** + conf bracket overlays; **Rankings** still show poll `#NN` |
| **Selection Sunday → April** | **March Mayhem** hub (national monitor) + **Calendar** for your next tip |

Bottom nav unchanged (**Roster · Calendar · Dashboard · News**). **March Mayhem** appears in the **hamburger** hub list once Selection Sunday publishes brackets (stays through last national game).

---

## Tournament seed labels

**Design lock:** Postseason **seeds are per tournament**, not global. A program is never shown as bare **“#1 seed”** — always **`{tournament} #{seed}`**.

| Tournament | UI short label | Seed range | Assignment |
|------------|----------------|------------|------------|
| **NCBA Men's BB Tournament** | **NCBA** | **1–64** | [`selection-engine.md`](selection-engine.md) — Seed Index + Opening lines |
| **National College Invitational** | **NCI** | **1–32** | Seed Index among NCI invitees (best → **NCI #1**) |
| **National Heritage Classic** | **HC** | **1–16** | Seed Index among HC invitees (best → **HC #1**) |

**Examples:** **NCBA #1** (national favorite in main bracket), **NCI #1** (best résumé in NCI field), **HC #1** (Heritage top line). All three can exist in the same season on **different** schools — labels prevent collision in news, brackets, and School hub.

### Display rules

| Surface | Rule |
|---------|------|
| **Brackets** | Seed chip on every team: `NCBA #12`, `NCI #5`, `HC #3` |
| **Dashboard** | If user’s team is in a national event: row **Postseason** → e.g. `NCBA #4 · East` (region when NCBA) |
| **School hub → Overview** | During March: show **poll `#NN`** (if still published) **and** tournament line(s) only for events that team joined |
| **News / notifications** | Headlines use short label: *“Shock: #15 NCBA upset”*, *“NCI #1 falls in Quarterfinals”* |
| **Game preview** | Header: opponent + `NCBA #7` vs `NCBA #10` (or NCI/HC) |

**Poll rank** stays **`#NN`** (365-team list). **Never** replace poll rank with seed on Rankings hub during March.

### Save shape (per team, season)

```json
{
  "nationalPostseason": {
    "tournamentId": "NCBA",
    "seed": 4,
    "regionId": "east"
  }
}
```

Only **one** national tournament per team per season. Omitted when program did not receive a national invite.

---

## March Mayhem hub

**Brand lock:** Hub title **March Mayhem** ([`ncba-branding.md`](ncba-branding.md)). Subtitle in UI may read *NCBA · NCI · Heritage*.

**Purpose:** Single place to **watch all three national brackets**, follow scores, and jump into **your** games — parallel tournaments on the calendar ([`postseason.md`](postseason.md)).

### Entry

- **Hamburger → March Mayhem** (visible **Selection Sunday** through **National Championship** / NCI Finals / Heritage Finals).
- **Dashboard** teaser card when any national game is scheduled today → opens hub filtered to *your* event first.
- Optional **pin** on hub icon when user’s team is alive in any bracket.

### Top-level layout

```
┌─────────────────────────────────────────┐
│  March Mayhem                            │
│  [ Overview ] [ NCBA ] [ NCI ] [ HC ]   │  ← tournament sub-tabs
├─────────────────────────────────────────┤
│  (bracket / schedule / results)         │
└─────────────────────────────────────────┘
```

| Sub-tab | Content |
|---------|---------|
| **Overview** | **All three** mini-brackets or progress rails; **Opening Round** (NCBA only) when active; today’s games across events; Selection Sunday recap chip |
| **NCBA** | Full **76-team** event tree: **Opening Round** → **Round 1** (64) → … → **National Championship**; four regions; seeds **`NCBA #1`–`#64`** |
| **NCI** | **32-team** bracket; seeds **`NCI #1`–`#32`** |
| **HC** | **16-team** Heritage bracket; seeds **`HC #1`–`#16`** |

### Interaction

| Action | Behavior |
|--------|----------|
| Tap game | Box score, recap, **Sim** if user’s program (same flow as regular season) |
| Tap team | School hub (read-only) |
| Filter | *My team only* toggle on each sub-tab |
| **NCBA Opening Round** | Dedicated strip above main bracket until **12** winners advance |

**Calendar** still lists **your** tips in chronological order; March Mayhem is the **bracket-centric** view.

---

## Conference tournaments (UI)

Before Selection Sunday:

- Brackets live under **Calendar** and **School → Schedule** (conference context).
- Teams show **poll `#NN`** only — **no** NCBA/NCI/HC seed labels until Selection Sunday.
- Conf-champion notification deep-links to **postseason** news, not March Mayhem (hub hidden until national field is set).

---

## Selection Sunday moment

1. **Reveal screen** (one-time or News flagship): NCBA field (**76**), NCI (**32**), Heritage (**16**) with **`NCBA #`**, **`NCI #`**, **`HC #`** assignments.
2. Unlock **March Mayhem** hub.
3. Dashboard **Postseason** row updates from poll-only to **tournament seed line** for user’s school if invited.

---

## Cross-links

| Topic | Doc |
|-------|-----|
| Cuts, Opening Round, parallel play | [`postseason.md`](postseason.md) |
| NCBA seed math | [`selection-engine.md`](selection-engine.md) |
| Round display names | [`ncba-branding.md`](ncba-branding.md) |

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-09 | Initial frame: tournament seed labels, March Mayhem hub |
