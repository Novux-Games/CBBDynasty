# Positions & lineup

**Used in:** [`game-frame.md`](game-frame.md) (Strategy → rotation), [`simulation-engine.md`](simulation-engine.md), [`player-cards.md`](player-cards.md).  
**Last updated:** 2025-11-08

---

## Listed position (player card)

Every player has one **listed position**:

| Code | Label |
|------|--------|
| **PG** | Point guard |
| **SG** | Shooting guard |
| **SF** | Small forward |
| **PF** | Power forward |
| **C** | Center |

Shown on player card header and roster lists. **Archetypes** ([`player-archetypes.md`](player-archetypes.md)) follow listed position (guard / wing / big families).

---

## Game slots vs listed position

A game **starting five** fills five **slots**: PG, SG, SF, PF, C.

| Slot | Who can play it |
|------|-----------------|
| **PG** | Listed **PG** or **SG** |
| **SG** | Listed **SG** or **PG** |
| **SF** | Listed **SF** or **PF** |
| **PF** | Listed **PF** or **SF** |
| **C** | Listed **C** only |

**Interchange pairs**

- **Backcourt:** PG ↔ SG  
- **Frontcourt:** SF ↔ PF  
- **Center:** no interchange (unless a future rule adds “small-ball C” — not in scope)

A player in a slot may be **out of listed position** (e.g. listed PG at SG) for that game; sim and minutes still use their ratings.

---

## Default starters algorithm

**Reset to default** in Strategy hub runs this logic:

1. Consider top **10** rotation players by OVR (or full roster if ≤10).
2. Assign exactly one player to each slot **PG, SG, SF, PF, C** such that:
   - eligibility rules above are met;
   - **sum of OVR of the five starters is maximized** (same as picking the best legal starting five).

3. Tie-break: higher OVR at listed position, then player ID stable order.

### Example (your case)

| Player | Listed | OVR |
|--------|--------|-----|
| A | PG | 91 |
| B | PG | 84 |
| C | SG | 72 |
| (others fill SF, PF, C) | | |

- C cannot beat A/B for PG/SG backcourt alone at SG if we only put C at SG (72).
- Best backcourt: **PG slot = A (91)**, **SG slot = B (84)** — listed PG at SG is allowed.
- **C (72)** moves to bench (or sixth man), not default SG.

Low-OVR player at his **listed** slot can lose the start to a **higher OVR interchange** player even if the latter’s list is PG/SG or SF/PF pair.

---

## User override

- User may set **any legal** assignment in Rotation (drag slot or pick from eligible list per slot).
- Invalid: e.g. listed **C** only at **PF** — **not allowed** without PF/SF list (centers stay C slot only).
- Invalid: SF listed player at **PG** — not allowed.

Overrides apply to **next sim**; week replay rules unchanged ([`game-design.md`](game-design.md)).

---

## Rotation & bench

- **Starters** on roster tab = current default or user five for next game.
- **Reserves / bench:** everyone else; sub logic in sim uses minutes bands from Strategy ([`game-frame.md`](game-frame.md)).

---

## Sim engine

[`simulation-engine.md`](simulation-engine.md) lineup validation:

```
slotEligible(player, slot):
  PG  if player.position in {PG, SG}
  SG  if player.position in {SG, PG}
  SF  if player.position in {SF, PF}
  PF  if player.position in {PF, SF}
  C   if player.position == C
```

Matchups and archetype nudges use **slot played** optional for scheme (e.g. “defend their SG”) — TBD in strategy doc.

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | PG/SG/SF/PF/C + interchange + default starter algorithm |
