#!/usr/bin/env python3
"""Build assets/pack/embedded-default.json from league branding source."""
from __future__ import annotations

import hashlib
import json
import math
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from generate_league_branding import (  # noqa: E402
    CONFERENCES,
    MEMBERSHIP,
    TRANSITION_BY_NAME,
    universe_team,
)

PACK_ID = "embedded-default"
RULESET_YEAR = 2026
MEMBERSHIP_YEAR = "2026-27"

# Typical conference-game totals (structural; schedule generator refines).
LEAGUE_GAMES_BY_CONF: dict[str, int] = {
    "conf-g10": 20,
    "conf-sedc": 18,
    "conf-c12": 18,
    "conf-acrown": 20,
    "conf-epc": 20,
    "conf-cat10": 18,
    "conf-aucc": 18,
    "conf-wcc": 18,
    "conf-rmwc": 18,
    "conf-gwc": 16,
    "conf-mac": 16,
    "conf-sbc": 16,
    "conf-metro": 18,
    "conf-pvc": 18,
    "conf-nsc": 16,
    "conf-cca": 14,
    "conf-ucc": 16,
    "conf-scf": 20,
    "conf-gcc": 16,
    "conf-psl": 16,
    "conf-fel": 14,
    "conf-meac": 14,
    "conf-scal": 16,
    "conf-ova": 16,
    "conf-nec": 14,
    "conf-nac": 16,
    "conf-psc": 16,
    "conf-lhl": 18,
    "conf-pr12": 16,
    "conf-summit": 16,
    "conf-uac": 16,
    "conf-ascu": 14,
}


def default_league_games(conf_id: str, team_count: int) -> int:
    if conf_id in LEAGUE_GAMES_BY_CONF:
        return LEAGUE_GAMES_BY_CONF[conf_id]
    return min(20, max(14, team_count * 2 - 2))


def bracket_for(team_count: int) -> tuple[int, list[dict]]:
    size = 2 ** math.ceil(math.log2(max(team_count, 2)))
    rounds: list[dict] = []
    games = size // 2
    labels = ["R1", "QF", "SF", "F", "F2"]
    idx = 0
    while games >= 1:
        rounds.append({"name": labels[min(idx, len(labels) - 1)], "games": games})
        games //= 2
        idx += 1
        if games == 0 and idx > 1:
            break
    return size, rounds


def colors_for(team_id: str) -> dict[str, str]:
    digest = hashlib.sha256(team_id.encode()).digest()
    primary = f"#{digest[0]:02x}{digest[1]:02x}{digest[2]:02x}"
    secondary = f"#{digest[3]:02x}{digest[4]:02x}{digest[5]:02x}"
    return {"primary": primary, "secondary": secondary}


def short_name(display: str, conf_short: str, index: int) -> str:
    parts = display.split()
    if len(parts) >= 2:
        letters = "".join(p[0] for p in parts[:3]).upper()
        if len(letters) >= 2:
            return letters[:4]
    return f"{conf_short[:3].upper()}{index:02d}"


def build_pack() -> dict:
    conf_by_id = {c[2]: c for c in CONFERENCES}
    conferences: list[dict] = []
    teams: list[dict] = []
    templates: list[dict] = []

    for conf_id, team_reals in MEMBERSHIP:
        _real, uni_display, _, conf_short = conf_by_id[conf_id]
        team_count = len(team_reals)
        tpl_id = f"tpl-{conf_id}"
        bracket_size, rounds = bracket_for(team_count)
        templates.append(
            {
                "id": tpl_id,
                "bracketSize": bracket_size,
                "seeds": team_count,
                "byes": [],
                "reseedingAfterRound": False,
                "sitePolicy": "neutral-primary",
                "qualificationPolicy": "all-members",
                "rounds": rounds,
            }
        )
        conferences.append(
            {
                "id": conf_id,
                "displayName": uni_display,
                "leagueGameCount": default_league_games(conf_id, team_count),
                "standingsTiebreakers": [
                    "h2h",
                    "h2h-point-diff",
                    "record-vs-top-teams",
                ],
                "tournamentTemplateId": tpl_id,
                "ncaaAutoBidSlot": 1,
            }
        )

        for idx, real in enumerate(team_reals, 1):
            team_id, display, state, city = universe_team(real, conf_id, idx)
            reclass = TRANSITION_BY_NAME.get(real)
            teams.append(
                {
                    "id": team_id,
                    "conferenceId": conf_id,
                    "displayName": display,
                    "shortName": short_name(display, conf_short, idx),
                    "homeCity": city,
                    "homeState": state,
                    "postseasonEligible": reclass is None,
                    "reclassTransitionYear": reclass[0] if reclass else None,
                    "colors": colors_for(team_id),
                    "arenaBaseName": f"{city} Arena",
                    "logoUri": None,
                }
            )

    return {
        "packVersion": 1,
        "packId": PACK_ID,
        "membershipYear": MEMBERSHIP_YEAR,
        "disclaimerAcceptedRequired": True,
        "rulesetYear": RULESET_YEAR,
        "conferences": conferences,
        "teams": teams,
        "tournamentTemplates": templates,
    }


def main() -> None:
    pack = build_pack()
    assert len(pack["conferences"]) == 32, len(pack["conferences"])
    assert len(pack["teams"]) == 365, len(pack["teams"])

    root = Path(__file__).resolve().parents[2]
    out_paths = [
        root / "assets" / "pack" / "embedded-default.json",
        root / "android" / "app" / "src" / "main" / "assets" / "pack" / "embedded-default.json",
    ]
    text = json.dumps(pack, indent=2, sort_keys=False)
    text += "\n"
    for path in out_paths:
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(text, encoding="utf-8")
        print(f"Wrote {path} ({len(text)} bytes)")


if __name__ == "__main__":
    main()
