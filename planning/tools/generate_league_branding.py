#!/usr/bin/env python3
"""Generate planning/league-branding.md — 2026–27 D-I men's membership (reference ↔ universe)."""
from __future__ import annotations

import re
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from team_geography_2026 import GEO_BY_CONF, HOME_STATES_BY_CONF

# Real conference name, universe display name, confId, short code
CONFERENCES = [
    ("America East Conference", "Northern Atlantic Collegiate Conference", "conf-nac", "NAC"),
    ("American Athletic Conference (AAC)", "American Unified Collegiate Conference", "conf-aucc", "AUCC"),
    ("Atlantic 10 Conference (A-10)", "Colonial Atlantic Ten", "conf-cat10", "CAT10"),
    ("Atlantic Coast Conference (ACC)", "Atlantic Crown Conference", "conf-acrown", "ACROWN"),
    ("Atlantic Sun Conference (ASUN)", "Atlantic Sun Collegiate Union", "conf-ascu", "ASC-U"),
    ("Big 12 Conference", "Central Twelve Conference", "conf-c12", "C12"),
    ("Big East Conference", "Eastern Premier Conference", "conf-epc", "EPC"),
    ("Big Sky Conference", "Northern Sky Conference", "conf-nsc", "NSC"),
    ("Big South Conference", "Piedmont South Conference", "conf-psc", "PSC"),
    ("Big Ten Conference", "Great Ten Conference", "conf-g10", "G10"),
    ("Big West Conference", "Golden West Conference", "conf-gwc", "GWC"),
    ("Coastal Athletic Association (CAA)", "Coastal Colonial Association", "conf-cca", "CCA"),
    ("Conference USA (C-USA)", "United Continental Conference", "conf-ucc", "UCC"),
    ("Horizon League", "Lakefront Horizon League", "conf-lhl", "LHL"),
    ("Ivy League", "Founding Eight League", "conf-fel", "FEL"),
    ("Metro Conference (formerly MAAC)", "Metro Collegiate Conference", "conf-metro", "METRO"),
    ("Mid-American Conference (MAC)", "Midland American Circuit", "conf-mac", "MAC"),
    ("Mid-Eastern Athletic Conference (MEAC)", "Mid-Eastern Alliance Conference", "conf-meac", "MEAC"),
    ("Missouri Valley Conference (MVC)", "Prairie Valley Conference", "conf-pvc", "PVC"),
    ("Mountain West Conference (MW)", "Rocky Mountain West Conference", "conf-rmwc", "RMWC"),
    ("Northeast Conference (NEC)", "Northeastern Exchange Conference", "conf-nec", "NEC"),
    ("Ohio Valley Conference (OVC)", "Ohio Valley Alliance", "conf-ova", "OVA"),
    ("Pac-12 Conference", "Pacific Rim Twelve", "conf-pr12", "PR12"),
    ("Patriot League", "Patriot Scholar League", "conf-psl", "PSL"),
    ("Southeastern Conference (SEC)", "Southeastern Dominion Conference", "conf-sedc", "SEDC"),
    ("Southern Conference (SoCon)", "Southern Collegiate Federation", "conf-scf", "SCF"),
    ("Southland Conference", "Gulf Coastland Conference", "conf-gcc", "GCC"),
    ("Southwestern Athletic Conference (SWAC)", "Southwest Collegiate Athletic League", "conf-scal", "SCAL"),
    ("Summit League", "Prairie Summit League", "conf-summit", "PSL"),
    ("Sun Belt Conference", "Solar Belt Conference", "conf-sbc", "SBC"),
    ("United Athletic Conference (UAC) (formerly WAC)", "United Athletic Collegiate", "conf-uac", "UAC"),
    ("West Coast Conference (WCC)", "Western Coast Collegiate", "conf-wcc", "WCC"),
]

MEMBERSHIP: list[tuple[str, list[str]]] = [
    ("conf-nac", [
        "Albany Great Danes", "Binghamton Bearcats", "Bryant Bulldogs", "Maine Black Bears",
        "New Hampshire Wildcats", "NJIT Highlanders", "UMBC Retrievers", "UMass Lowell River Hawks",
        "Vermont Catamounts",
    ]),
    ("conf-aucc", [
        "Charlotte 49ers", "East Carolina Pirates", "Florida Atlantic Owls (FAU)", "Memphis Tigers",
        "North Texas Mean Green", "Rice Owls", "South Florida Bulls (USF)", "Temple Owls",
        "Tulane Green Wave", "Tulsa Golden Hurricane", "UAB Blazers", "UTSA Roadrunners",
        "Wichita State Shockers",
    ]),
    ("conf-cat10", [
        "Davidson Wildcats", "Dayton Flyers", "Duquesne Dukes", "Fordham Rams", "George Mason Patriots",
        "George Washington Revolutionaries", "La Salle Explorers", "Loyola Chicago Ramblers",
        "Rhode Island Rams", "Richmond Spiders", "Saint Joseph's Hawks", "Saint Louis Billikens",
        "St. Bonaventure Bonnies", "VCU Rams",
    ]),
    ("conf-acrown", [
        "Boston College Eagles", "California Golden Bears (Cal)", "Clemson Tigers", "Duke Blue Devils",
        "Florida State Seminoles", "Georgia Tech Yellow Jackets", "Louisville Cardinals", "Miami Hurricanes",
        "NC State Wolfpack", "North Carolina Tar Heels", "Notre Dame Fighting Irish", "Pittsburgh Panthers",
        "SMU Mustangs", "Stanford Cardinal", "Syracuse Orange", "Virginia Cavaliers", "Virginia Tech Hokies",
        "Wake Forest Demon Deacons",
    ]),
    ("conf-ascu", [
        "Bellarmine Knights", "Florida Gulf Coast Eagles (FGCU)", "Jacksonville Dolphins", "Lipscomb Bisons",
        "North Florida Ospreys", "Queens Royals", "Stetson Hatters", "West Florida Argonauts",
    ]),
    ("conf-c12", [
        "Arizona Wildcats", "Arizona State Sun Devils", "Baylor Bears", "BYU Cougars", "Cincinnati Bearcats",
        "Colorado Buffaloes", "Houston Cougars", "Iowa State Cyclones", "Kansas Jayhawks", "Kansas State Wildcats",
        "Oklahoma State Cowboys", "TCU Horned Frogs", "Texas Tech Red Raiders", "UCF Knights", "Utah Utes",
        "West Virginia Mountaineers",
    ]),
    ("conf-epc", [
        "Butler Bulldogs", "Creighton Bluejays", "DePaul Blue Demons", "Georgetown Hoyas",
        "Marquette Golden Eagles", "Providence Friars", "Seton Hall Pirates", "St. John's Red Storm",
        "UConn Huskies", "Villanova Wildcats", "Xavier Musketeers",
    ]),
    ("conf-nsc", [
        "Eastern Washington Eagles", "Idaho Vandals", "Idaho State Bengals", "Montana Grizzlies",
        "Montana State Bobcats", "Northern Arizona Lumberjacks", "Northern Colorado Bears",
        "Portland State Vikings", "Southern Utah Thunderbirds", "Utah Tech Trailblazers", "Weber State Wildcats",
    ]),
    ("conf-psc", [
        "Charleston Southern Buccaneers", "Gardner-Webb Runnin' Bulldogs", "High Point Panthers",
        "Longwood Lancers", "Presbyterian Blue Hose", "Radford Highlanders", "UNC Asheville Bulldogs",
        "USC Upstate Spartans", "Winthrop Eagles",
    ]),
    ("conf-g10", [
        "Illinois Fighting Illini", "Indiana Hoosiers", "Iowa Hawkeyes", "Maryland Terrapins",
        "Michigan Wolverines", "Michigan State Spartans", "Minnesota Golden Gophers", "Nebraska Cornhuskers",
        "Northwestern Wildcats", "Ohio State Buckeyes", "Oregon Ducks", "Penn State Nittany Lions",
        "Purdue Boilermakers", "Rutgers Scarlet Knights", "UCLA Bruins", "USC Trojans", "Washington Huskies",
        "Wisconsin Badgers",
    ]),
    ("conf-gwc", [
        "Cal Baptist Lancers", "Cal Poly Mustangs", "Cal State Bakersfield Roadrunners",
        "Cal State Fullerton Titans", "Cal State Northridge Matadors (CSUN)", "Long Beach State Beach",
        "Sacramento State Hornets", "UC Irvine Anteaters", "UC Riverside Highlanders", "UC San Diego Tritons",
        "UC Santa Barbara Gauchos", "Utah Valley Wolverines",
    ]),
    ("conf-cca", [
        "Campbell Fighting Camels", "College of Charleston Cougars", "Drexel Dragons", "Elon Phoenix",
        "Hampton Pirates", "Hofstra Pride", "Monmouth Hawks", "North Carolina A&T Aggies",
        "Northeastern Huskies", "Stony Brook Seawolves", "Towson Tigers", "UNC Wilmington Seahawks (UNCW)",
        "William & Mary Tribe",
    ]),
    ("conf-ucc", [
        "Delaware Fightin' Blue Hens", "FIU Panthers", "Jacksonville State Gamecocks", "Kennesaw State Owls",
        "Liberty Flames", "Middle Tennessee Blue Raiders (MTSU)", "Missouri State Bears",
        "New Mexico State Aggies", "Sam Houston Bearkats", "Western Kentucky Hilltoppers (WKU)",
    ]),
    ("conf-lhl", [
        "Cleveland State Vikings", "Detroit Mercy Titans", "Green Bay Phoenix", "IU Indy Jaguars",
        "Milwaukee Panthers", "Northern Illinois Huskies (NIU)", "Northern Kentucky Norse",
        "Oakland Golden Grizzlies", "Purdue Fort Wayne Mastodons", "Robert Morris Colonials",
        "Wright State Raiders", "Youngstown State Penguins",
    ]),
    ("conf-fel", [
        "Brown Bears", "Columbia Lions", "Cornell Big Red", "Dartmouth Big Green", "Harvard Crimson",
        "Penn Quakers", "Princeton Tigers", "Yale Bulldogs",
    ]),
    ("conf-metro", [
        "Canisius Golden Griffins", "Fairfield Stags", "Iona Gaels", "Manhattan Jaspers", "Marist Red Foxes",
        "Merrimack Warriors", "Mount St. Mary's Mountaineers", "Niagara Purple Eagles", "Quinnipiac Bobcats",
        "Rider Broncs", "Sacred Heart Pioneers", "Saint Peter's Peacocks", "Siena Saints",
    ]),
    ("conf-mac", [
        "Akron Zips", "Ball State Cardinals", "Bowling Green Falcons", "Buffalo Bulls",
        "Central Michigan Chippewas", "Eastern Michigan Eagles", "Kent State Golden Flashes",
        "Miami (OH) RedHawks", "Ohio Bobcats", "Toledo Rockets", "UMass Minutemen", "Western Michigan Broncos",
    ]),
    ("conf-meac", [
        "Coppin State Eagles", "Delaware State Hornets", "Howard Bison", "Maryland Eastern Shore Hawks (UMES)",
        "Morgan State Bears", "Norfolk State Spartans", "North Carolina Central Eagles",
        "South Carolina State Bulldogs",
    ]),
    ("conf-pvc", [
        "Belmont Bruins", "Bradley Braves", "Drake Bulldogs", "Evansville Purple Aces",
        "Illinois State Redbirds", "Indiana State Sycamores", "Murray State Racers",
        "Northern Iowa Panthers (UNI)", "Southern Illinois Salukis", "UIC Flames", "Valparaiso Beacons",
    ]),
    ("conf-rmwc", [
        "Air Force Falcons", "Grand Canyon Antelopes", "Hawaiʻi Rainbow Warriors", "Nevada Wolf Pack",
        "New Mexico Lobos", "San José State Spartans", "UC Davis Aggies", "UNLV Runnin' Rebels",
        "UTEP Miners", "Wyoming Cowboys",
    ]),
    ("conf-nec", [
        "Central Connecticut Blue Devils", "Chicago State Cougars", "Fairleigh Dickinson Knights (FDU)",
        "Le Moyne Dolphins", "LIU Sharks", "Mercyhurst Lakers", "New Haven Chargers",
        "Stonehill Skyhawks", "Wagner Seahawks",
    ]),
    ("conf-ova", [
        "Eastern Illinois Panthers", "Lindenwood Lions", "Morehead State Eagles", "SIU Edwardsville Cougars (SIUE)",
        "Southeast Missouri State Redhawks (SEMO)", "Southern Indiana Screaming Eagles", "Tennessee State Tigers",
        "UT Martin Skyhawks", "Western Illinois Leathernecks",
    ]),
    ("conf-pr12", [
        "Boise State Broncos", "Colorado State Rams", "Fresno State Bulldogs", "Gonzaga Bulldogs",
        "Oregon State Beavers", "San Diego State Aztecs", "Texas State Bobcats", "Utah State Aggies",
        "Washington State Cougars",
    ]),
    ("conf-psl", [
        "American Eagles", "Army Black Knights", "Boston University Terriers", "Bucknell Bison",
        "Colgate Raiders", "Holy Cross Crusaders", "Lafayette Leopards", "Lehigh Mountain Hawks",
        "Loyola Maryland Greyhounds", "Navy Midshipmen",
    ]),
    ("conf-sedc", [
        "Alabama Crimson Tide", "Arkansas Razorbacks", "Auburn Tigers", "Florida Gators", "Georgia Bulldogs",
        "Kentucky Wildcats", "LSU Tigers", "Mississippi State Bulldogs", "Missouri Tigers", "Oklahoma Sooners",
        "Ole Miss Rebels", "South Carolina Gamecocks", "Tennessee Volunteers", "Texas Longhorns",
        "Texas A&M Aggies", "Vanderbilt Commodores",
    ]),
    ("conf-scf", [
        "Chattanooga Mocs", "East Tennessee State Buccaneers (ETSU)", "Furman Paladins", "Mercer Bears",
        "Samford Bulldogs", "Tennessee Tech Golden Eagles", "The Citadel Bulldogs",
        "UNC Greensboro Spartans (UNCG)", "VMI Keydets", "Western Carolina Catamounts", "Wofford Terriers",
    ]),
    ("conf-gcc", [
        "East Texas A&M Lions", "Houston Christian Huskies", "Incarnate Word Cardinals (UIW)", "Lamar Cardinals",
        "McNeese Cowboys", "New Orleans Privateers", "Nicholls Colonels", "Northwestern State Demons",
        "Southeastern Louisiana Lions", "Stephen F. Austin Lumberjacks (SFA)",
        "Texas A&M-Corpus Christi Islanders", "UT Rio Grande Valley Vaqueros (UTRGV)",
    ]),
    ("conf-scal", [
        "Alabama A&M Bulldogs", "Alabama State Hornets", "Alcorn State Braves",
        "Arkansas-Pine Bluff Golden Lions (UAPB)", "Bethune-Cookman Wildcats", "Florida A&M Rattlers (FAMU)",
        "Grambling State Tigers", "Jackson State Tigers", "Mississippi Valley State Delta Devils",
        "Prairie View A&M Panthers", "Southern Jaguars", "Texas Southern Tigers",
    ]),
    ("conf-summit", [
        "Kansas City Roos", "North Dakota Fighting Hawks", "North Dakota State Bison", "Omaha Mavericks",
        "Oral Roberts Golden Eagles", "South Dakota Coyotes", "South Dakota State Jackrabbits", "St. Thomas Tommies",
    ]),
    ("conf-sbc", [
        "Appalachian State Mountaineers", "Arkansas State Red Wolves", "Coastal Carolina Chanticleers",
        "Georgia Southern Eagles", "Georgia State Panthers", "James Madison Dukes (JMU)", "Louisiana Ragin' Cajuns",
        "Louisiana-Monroe Warhawks (ULM)", "Louisiana Tech Bulldogs", "Marshall Thundering Herd",
        "Old Dominion Monarchs (ODU)", "South Alabama Jaguars", "Southern Miss Golden Eagles", "Troy Trojans",
    ]),
    ("conf-uac", [
        "Abilene Christian Wildcats", "Austin Peay Governors", "Central Arkansas Bears", "Eastern Kentucky Colonels",
        "Little Rock Trojans", "North Alabama Lions", "Tarleton State Texans", "UT Arlington Mavericks",
        "West Georgia Wolves",
    ]),
    ("conf-wcc", [
        "Denver Pioneers", "Loyola Marymount Lions (LMU)", "Pacific Tigers", "Pepperdine Waves",
        "Portland Pilots", "Saint Mary's Gaels", "San Diego Toreros", "San Francisco Dons",
        "Santa Clara Broncos", "Seattle U Redhawks",
    ]),
]

# D-II → D-I reclass — in pack, **`postseasonEligible: false`** until 4-year transition completes.
# Bootstrap: transition year as of **Season 1** (in-game) = calendar **2026–27** (new dynasty start).
TRANSITION_BY_NAME: dict[str, tuple[int, str]] = {
    "Mercyhurst Lakers": (3, "Mid-transition"),
    "New Haven Chargers": (2, "Early-transition"),
    "West Florida Argonauts": (1, "Early-transition"),
    "West Georgia Wolves": (3, "Mid-transition"),
}

CERTIFIED_COUNT = 361
POSTSEASON_ELIGIBLE_LABEL = "Yes"
POSTSEASON_INELIGIBLE_LABEL = "**No** (reclass)"

TEAM_OVERRIDES: dict[str, str] = {
    "Duke Blue Devils": "Crown University Sovereigns",
    "North Carolina Tar Heels": "Tar River Commonwealth",
    "Kentucky Wildcats": "Bluegrass Commonwealth Lynx",
    "Kansas Jayhawks": "Jaydale Commonwealth",
    "UCLA Bruins": "Westwood Harbor Guardians",
    "USC Trojans": "South Bay Argonauts",
    "Gonzaga Bulldogs": "Spokane River Mastiffs",
    "Villanova Wildcats": "Main Line Lynx",
    "UConn Huskies": "Storrs Sled Dogs",
    "Hawaiʻi Rainbow Warriors": "Honolulu Rainbow Guard",
    "California Golden Bears (Cal)": "Bay Golden Bruins",
}

MASCOT_SWAP = {
    "Wildcats": "Lynx", "Bulldogs": "Mastiffs", "Tigers": "Cougars", "Bears": "Bruins",
    "Eagles": "Hawks", "Panthers": "Pumas", "Cardinals": "Scarlets", "Hawks": "Falcons",
    "Lions": "Monarchs", "Warriors": "Sentinels", "Raiders": "Rangers", "Spartans": "Hoplites",
    "Bulls": "Stampede", "Buccaneers": "Privateers", "Mountaineers": "Highlanders",
}

MULTI_MASCOTS = sorted([
    "Blue Devils", "Tar Heels", "Fighting Irish", "Golden Bears", "Yellow Jackets", "Blue Demons",
    "Golden Eagles", "Red Storm", "Sun Devils", "Horned Frogs", "Red Raiders", "Fighting Illini",
    "Golden Gophers", "Nittany Lions", "Scarlet Knights", "Fighting Camels", "Fighting Hawks",
    "Blue Hens", "Blue Raiders", "Rainbow Warriors", "Wolf Pack", "Runnin' Rebels", "Red Flash",
    "Golden Hurricane", "Mean Green", "Green Wave", "River Hawks", "Black Bears", "Great Danes",
    "Demon Deacons", "Crimson Tide", "Golden Flashes", "Red Wolves", "Ragin' Cajuns", "Thundering Herd",
    "Golden Grizzlies", "Big Red", "Big Green", "Red Foxes", "Purple Eagles", "Purple Aces",
    "Blue Hose", "Runnin' Bulldogs", "Golden Lions", "Delta Devils", "Mountain Hawks", "Fighting Illini",
], key=len, reverse=True)

def strip_paren(name: str) -> str:
    return re.sub(r"\s*\([^)]*\)", "", name).strip()


def split_team(real: str) -> tuple[str, str]:
    real_clean = strip_paren(real)
    for m in MULTI_MASCOTS:
        if real_clean.endswith(m):
            return real_clean[: -len(m)].strip(), m
    parts = real_clean.split()
    if len(parts) < 2:
        return real_clean, ""
    return " ".join(parts[:-1]), parts[-1]


def campus_geo(conf_id: str, idx: int) -> tuple[str, str]:
    rows = GEO_BY_CONF[conf_id]
    if idx < 1 or idx > len(rows):
        raise IndexError(f"geo index for {conf_id} #{idx}")
    return rows[idx - 1]


def universe_team(real: str, conf_id: str, idx: int) -> tuple[str, str, str, str]:
    state, city = campus_geo(conf_id, idx)
    if real in TEAM_OVERRIDES:
        return f"team-{conf_id}-{idx:03d}", TEAM_OVERRIDES[real], state, city

    school, mascot = split_team(real)
    school_clean = strip_paren(school)
    new_mascot = MASCOT_SWAP.get(mascot, mascot)

    tokens = school_clean.split()
    uni_school = school_clean
    metro_prefix = {
        "UNC": "North Cape", "UMass": "Bay State", "UTSA": "South Alamo", "UAB": "Birmingham Metro",
        "UCF": "Central Florida Metro", "USC": "Southern California Metro", "UCLA": "Los Angeles Metro",
        "NJIT": "Newark Tech", "FIU": "Miami-Dade Metro", "BYU": "Provo Metro", "SMU": "Dallas Metro",
        "UTEP": "El Paso Metro", "UNLV": "Las Vegas Metro", "UConn": "Storrs Metro",
    }
    if tokens and tokens[0] in metro_prefix:
        rest = " ".join(tokens[1:])
        uni_school = metro_prefix[tokens[0]] + (f" {rest}" if rest else "")
    elif school_clean.replace("St.", "Saint") != school_clean:
        uni_school = school_clean.replace("St.", "Saint")

    universe = f"{uni_school} {new_mascot}".strip()
    return f"team-{conf_id}-{idx:03d}", universe, state, city


def main() -> None:
    out = Path(__file__).resolve().parents[1] / "league-branding.md"
    conf_by_id = {c[2]: c for c in CONFERENCES}
    team_total = sum(len(t) for _, t in MEMBERSHIP)

    lines: list[str] = [
        "# League branding — conferences & teams (reference mapping)",
        "",
        "**Product:** [Dynasty: College Basketball](branding.md) · default **universe** names for identity packs.",
        f"**Membership:** **{team_total}** active D-I programs · **{len(CONFERENCES)} conferences** · **2026–27** ([`2026-NCAA-Directory.md`](2026-NCAA-Directory.md)) "
        f"({CERTIFIED_COUNT} certified + {len(TRANSITION_BY_NAME)} in reclass transition).",
        "**Purpose:** Keep **real-world reference** alongside **ship-safe display** names for a **shippable** default pack.",
        "**Geography:** Every program has **`homeState`** + **`homeCity`** (reference campus). **`homeState`** drives **[recruiting pipeline](recruiting-engine.md)**; **`homeCity`** is stored for schedule/travel/display and pack fidelity.",
        "**Related:** [`game-design.md`](game-design.md#identity-packs--save-model), [`branding.md`](branding.md), [`recruiting-engine.md`](recruiting-engine.md).",
        "**Last updated:** 2025-11-08",
        "",
        "---",
        "",
        "## Contents",
        "",
        "1. [Conferences](#conferences)",
        "2. [Teams by conference](#teams-by-conference)",
        "3. [Geography & recruiting](#geography--recruiting)",
        "4. [Provisional / transition programs](#provisional-transition-programs)",
        "5. [National body & tournaments](#national-body--tournaments)",
        "6. [Pack authoring notes](#pack-authoring-notes)",
        "7. [Document history](#document-history)",
        "",
        "---",
        "",
        "## Conferences",
        "",
        "| # | Real (reference) | Universe (default pack) | `confId` | Short |",
        "|---|------------------|-------------------------|----------|-------|",
    ]

    for i, (real, uni, cid, short) in enumerate(CONFERENCES, 1):
        lines.append(f"| {i} | {real} | **{uni}** | `{cid}` | {short} |")

    lines.extend([
        "",
        "---",
        "",
        "## Teams by conference",
        "",
        "Columns: `teamId`, real reference, universe name, **`homeCity`**, **`homeState`**, **postseason** (reclass programs ineligible until transition complete).",
        "",
    ])

    for conf_id, teams in MEMBERSHIP:
        if conf_id not in GEO_BY_CONF:
            raise KeyError(conf_id)
        if len(teams) != len(GEO_BY_CONF[conf_id]):
            raise ValueError(
                f"{conf_id}: {len(teams)} teams vs {len(GEO_BY_CONF[conf_id])} geo rows"
            )

    for conf_id, teams in MEMBERSHIP:
        real_conf, uni_conf, _, _ = conf_by_id[conf_id]
        lines.append(f"### {uni_conf}")
        lines.append("")
        lines.append(f"*Real league: {real_conf}* · `{conf_id}` · **{len(teams)} teams**")
        lines.append("")
        lines.append("| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` | Postseason |")
        lines.append("|----------|-------------------|-------------------------|------------|-------------|------------|")
        for idx, real in enumerate(teams, 1):
            tid, uni, state, city = universe_team(real, conf_id, idx)
            postseason = POSTSEASON_INELIGIBLE_LABEL if real in TRANSITION_BY_NAME else POSTSEASON_ELIGIBLE_LABEL
            lines.append(f"| `{tid}` | {real} | {uni} | {city} | {state} | {postseason} |")
        lines.append("")

    lines.extend([
        "---",
        "",
        "## Geography & recruiting",
        "",
        "| Rule | Detail |",
        "|------|--------|",
        "| **`homeCity`** | Campus city on every program; pack + save field; schedule/travel/display |",
        "| **`homeState`** | US state / DC; **recruiting pipeline** (in-state / regional bias on recruits) |",
        "| **Recruits** | `homeState` (and city when seeded) for geography; pipeline uses **state** |",
        "| **Colors / logos** | Pack overlay; separate from city/state |",
        "",
        "---",
        "",
        "## Provisional / transition programs",
        "",
        "All **4** are **in the default pack** and play **regular season** / conference schedules. They are **ineligible for postseason** (conference tournament + national bracket) until they **complete the 4-year** D-II→D-I reclass window — see [`game-design.md`](game-design.md) postseason gates.",
        "",
        "**Season clock:** **Season 1** in dynasty = **2026–27** calendar year. Values below are **`reclassTransitionYear` at new-dynasty start** (Season 1); each **offseason** increments for programs still in reclass.",
        "",
        "| Real (reference) | `reclassTransitionYear` (Season 1) | `postseasonEligible` (Season 1) |",
        "|------------------|-----------------------------------|--------------------------------|",
    ])
    for name, (year, label) in sorted(TRANSITION_BY_NAME.items(), key=lambda x: x[0]):
        lines.append(f"| {name} | Year {year} ({label}) | `false` |")
    lines.extend([
        "",
        "After a program completes **transition year 4** in offseason, set `postseasonEligible: true` for the **next** in-game season (e.g. Mercyhurst/West Georgia at year 3 in Season 1 → eligible starting **Season 3**).",
    ])
    lines.extend([
        "",
        "---",
        "",
        "## National body & tournaments",
        "",
        "| Real (reference) | Universe (default pack) | Notes |",
        "|-------------------|-------------------------|--------|",
        "| NCAA | **National Collegiate Basketball Association (NCBA)** | Pack may override display strings |",
        "| NCAA tournament / March Madness | **National Championship** / **March Classic** | 76-team field — [`game-design.md`](game-design.md#postseason--selection) |",
        "| NIT | **Collegiate Invitational** | 32-team secondary field — [`game-design.md`](game-design.md#secondary-national-bracket-nit-style) |",
        "",
        "---",
        "",
        "## Pack authoring notes",
        "",
        "| Rule | Detail |",
        "|------|--------|",
        "| **IDs** | Simulation uses `confId` / `teamId` only; display strings from pack overlay |",
        "| **`homeCity` / `homeState`** | Required on every team in default pack JSON |",
        "| **Colors & logos** | Pack JSON overlay |",
        "| **Realignment** | Metro (ex-MAAC), UAC (ex-WAC), Pac-12 remnant, MW + UC Davis, Big West + Utah Valley, etc. reflected in this snapshot |",
        "| **Source of truth (reference)** | [`2026-NCAA-Directory.md`](2026-NCAA-Directory.md) — certification tier copy |",
        "| **Regeneration** | `python3 planning/tools/generate_league_branding.py` after membership edits |",
        "",
        f"**Pack count:** {team_total} teams · {len(CONFERENCES)} conferences · {CERTIFIED_COUNT} postseason-eligible · {len(TRANSITION_BY_NAME)} reclass (no postseason until transition complete).",
        "",
        "---",
        "",
        "## Document history",
        "",
        "| Date | Change |",
        "|------|--------|",
        "| 2025-11-08 | Initial conference + team mapping |",
        "| 2025-11-08 | **Rebuild:** 2026–27 D-I list — 32 conferences, Metro/UAC, membership moves |",
        "| 2025-11-08 | Geography: `homeState` (recruiting) + `homeCity` on all programs |",
        "| 2025-11-08 | Align 361 certified; link directory; exclude 4 transition programs |",
        "| 2025-11-08 | Include 365 in pack; 4 reclass ineligible for postseason until year 4 done |",
        "| 2025-11-08 | Reclass years labeled Season 1 (2026–27) bootstrap |",
        "",
    ])

    out.write_text("\n".join(lines), encoding="utf-8")
    print(f"Wrote {out} — {len(CONFERENCES)} conferences, {team_total} teams")
    if team_total != 365:
        print(f"WARNING: expected 365 active teams, got {team_total}")


if __name__ == "__main__":
    main()
