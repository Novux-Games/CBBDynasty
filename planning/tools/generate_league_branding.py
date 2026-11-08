#!/usr/bin/env python3
"""Generate planning/league-branding.md team tables from reference membership."""
from __future__ import annotations

import re
from pathlib import Path

# fmt: off
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
    ("Metro Atlantic Athletic Conference (MAAC)", "Metro Atlantic Collegiate Alliance", "conf-maca", "MACA"),
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
    ("Summit League", "Prairie Summit League", "conf-psl", "PSL"),
    ("Sun Belt Conference", "Solar Belt Conference", "conf-sbc", "SBC"),
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
        "Tulane Green Wave", "Tulsa Golden Hurricane", "UAB Blazers", "UTSA Roadrunners", "Wichita State Shockers",
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
        "North Florida Ospreys", "Queens Royals", "Stetson Hatters", "West Georgia Wolves",
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
        "Sacramento State Hornets", "UC Davis Aggies", "UC Irvine Anteaters", "UC Riverside Highlanders",
        "UC San Diego Tritons", "UC Santa Barbara Gauchos",
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
        "Cleveland State Vikings", "Detroit Mercy Titans", "Green Bay Phoenix", "IU Indy Jaguars (formerly IUPUI)",
        "Milwaukee Panthers", "Northern Illinois Huskies (NIU)", "Northern Kentucky Norse",
        "Oakland Golden Grizzlies", "Purdue Fort Wayne Mastodons", "Robert Morris Colonials",
        "Wright State Raiders", "Youngstown State Penguins",
    ]),
    ("conf-fel", [
        "Brown Bears", "Columbia Lions", "Cornell Big Red", "Dartmouth Big Green", "Harvard Crimson",
        "Penn Quakers", "Princeton Tigers", "Yale Bulldogs",
    ]),
    ("conf-maca", [
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
        "New Mexico Lobos", "San José State Spartans", "UNLV Runnin' Rebels", "UTEP Miners", "Wyoming Cowboys",
    ]),
    ("conf-nec", [
        "Central Connecticut Blue Devils", "Chicago State Cougars", "Fairleigh Dickinson Knights (FDU)",
        "Le Moyne Dolphins", "LIU Sharks", "Mercyhurst Lakers", "New Haven Chargers",
        "Saint Francis (PA) Red Flash", "Stonehill Skyhawks", "Wagner Seahawks",
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
        "Samford Bulldogs", "The Citadel Bulldogs", "Tennessee Tech Golden Eagles", "UNC Greensboro Spartans (UNCG)",
        "VMI Keydets", "Western Carolina Catamounts", "Wofford Terriers",
    ]),
    ("conf-gcc", [
        "East Texas A&M Lions (formerly Texas A&M–Commerce)", "Houston Christian Huskies",
        "Incarnate Word Cardinals (UIW)", "Lamar Cardinals", "New Orleans Privateers", "Nicholls Colonels",
        "Northwestern State Demons", "McNeese Cowboys", "Southeastern Louisiana Lions",
        "Stephen F. Austin Lumberjacks (SFA)", "Texas A&M-Corpus Christi Islanders", "UT Rio Grande Valley Vaqueros (UTRGV)",
    ]),
    ("conf-scal", [
        "Alabama A&M Bulldogs", "Alabama State Hornets", "Alcorn State Braves",
        "Arkansas-Pine Bluff Golden Lions (UAPB)", "Bethune-Cookman Wildcats", "Florida A&M Rattlers (FAMU)",
        "Grambling State Tigers", "Jackson State Tigers", "Mississippi Valley State Delta Devils",
        "Prairie View A&M Panthers", "Southern Jaguars", "Texas Southern Tigers",
    ]),
    ("conf-psl", [
        "Kansas City Roos", "North Dakota Fighting Hawks", "North Dakota State Bison", "Omaha Mavericks",
        "Oral Roberts Golden Eagles", "South Dakota Coyotes", "South Dakota State Jackrabbits", "St. Thomas Tommies",
    ]),
    ("conf-sbc", [
        "Appalachian State Mountaineers", "Arkansas State Red Wolves", "Coastal Carolina Chanticleers",
        "Georgia Southern Eagles", "Georgia State Panthers", "James Madison Dukes (JMU)", "Louisiana Ragin' Cajuns",
        "Louisiana-Monroe Warhawks (ULM)", "Louisiana Tech Bulldogs", "Marshall Thundering Herd",
        "Old Dominion Monarchs (ODU)", "South Alabama Jaguars", "Southern Miss Golden Eagles", "Troy Trojans",
    ]),
    ("conf-wcc", [
        "Denver Pioneers", "Loyola Marymount Lions (LMU)", "Pacific Tigers", "Pepperdine Waves",
        "Portland Pilots", "Saint Mary's Gaels", "San Diego Toreros", "San Francisco Dons",
        "Santa Clara Broncos", "Seattle U Redhawks",
    ]),
]
# fmt: on

CONF_BY_ID = {c[2]: c for c in CONFERENCES}
CONF_REAL_BY_ID = {m[0]: m[1] for m in MEMBERSHIP}

# Duplicate conf-psl for Summit and Patriot - fix Summit id
MEMBERSHIP_FIXED: list[tuple[str, list[str]]] = []
for cid, teams in MEMBERSHIP:
    if cid == "conf-psl" and "Kansas City Roos" in teams:
        cid = "conf-summit"
    MEMBERSHIP_FIXED.append((cid, teams))

# Add missing summit conference row
CONFERENCES = [c if c[2] != "conf-psl" else c for c in CONFERENCES]
# Patriot stays conf-psl, Summit needs conf-summit
CONFERENCES = [
    c if c[2] != "conf-psl" or c[3] == "PSL" and "Patriot" in c[0]
    else c
    for c in CONFERENCES
]
# Rebuild conference list - we have duplicate conf-psl in CONFERENCES for Patriot and Summit
# Original CONFERENCES index 23 Patriot conf-psl, index 28 Summit conf-psl
CONFERENCES[28] = ("Summit League", "Prairie Summit League", "conf-summit", "PSL")

TEAM_OVERRIDES: dict[str, tuple[str, str]] = {
    "California Golden Bears (Cal)": ("Bay Golden Bruins", "Berkeley, CA"),
    "Duke Blue Devils": ("Crown University Sovereigns", "Durham, NC"),
    "North Carolina Tar Heels": ("Tar River Commonwealth", "Chapel Hill, NC"),
    "Kentucky Wildcats": ("Bluegrass Commonwealth Lynx", "Lexington, KY"),
    "Kansas Jayhawks": ("Jaydale Commonwealth", "Lawrence, KS"),
    "UCLA Bruins": ("Westwood Harbor Guardians", "Los Angeles, CA"),
    "USC Trojans": ("South Bay Argonauts", "Los Angeles, CA"),
    "Gonzaga Bulldogs": ("Spokane River Mastiffs", "Spokane, WA"),
    "Villanova Wildcats": ("Main Line Lynx", "Villanova, PA"),
    "UConn Huskies": ("Storrs Sled Dogs", "Storrs, CT"),
    "Hawaiʻi Rainbow Warriors": ("Honolulu Rainbow Guard", "Honolulu, HI"),
}

MASCOT_SWAP = {
    "Bulls": "Stampede",
    "Wildcats": "Lynx", "Bulldogs": "Mastiffs", "Tigers": "Cougars", "Bears": "Bruins",
    "Eagles": "Hawks", "Panthers": "Pumas", "Cardinals": "Scarlets", "Hawks": "Falcons",
    "Lions": "Monarchs", "Warriors": "Sentinels", "Raiders": "Rangers", "Spartans": "Hoplites",
    "Buckeyes": "Chestnuts", "Hoosiers": "Pioneers", "Volunteers": "Regiments",
    "Crimson Tide": "Scarlet Surge", "Razorbacks": "Backwoods Boars", "Seminoles": "Lakeward Tribe",
    "Tar Heels": "Pine Soles", "Blue Devils": "Azure Vipers", "Fighting Irish": "Emerald Guard",
    "Wolfpack": "Timber Pack", "Yellow Jackets": "Gold Jackets", "Hurricanes": "Cyclones",
    "Mountaineers": "Highlanders", "Rebels": "Renegades", "Aggies": "Rustics", "Cowboys": "Wranglers",
    "Mustangs": "Stallions", "Broncos": "Mustangs", "Ducks": "Drakes", "Beavers": "Carpenters",
    "Bruins": "Guardians", "Trojans": "Argonauts", "Huskies": "Sled Dogs", "Gauchos": "Rancheros",
    "Anteaters": "Aardvarks", "Titans": "Colossi", "Matadors": "Lancers", "Roadrunners": "Sprinters",
    "Beach": "Shore", "Catamounts": "Mountain Cats", "Great Danes": "Tall Hounds",
    "Bearcats": "Ringtails", "Black Bears": "Ursines", "Highlanders": "Clansmen", "Retrievers": "Fetchers",
    "River Hawks": "Stream Hawks", "49ers": "Prospectors", "Pirates": "Corsairs", "Owls": "Raptors",
    "Mean Green": "Mean Teal", "Green Wave": "Green Surge", "Golden Hurricane": "Golden Gale",
    "Blazers": "Foragers", "Shockers": "Jolters", "Flyers": "Aviators", "Dukes": "Barons",
    "Rams": "Bighorns", "Patriots": "Minutemen", "Revolutionaries": "Independents", "Explorers": "Voyagers",
    "Ramblers": "Wayfarers", "Spiders": "Widows", "Billikens": "Icons", "Bonnies": "Bonfires",
    "Demon Deacons": "Spirit Callers", "Cardinal": "Crimson", "Orange": "Citrus", "Cavaliers": "Lancers",
    "Hokies": "Techmen", "Knights": "Squires", "Dolphins": "Mariners", "Bisons": "Buffaloes",
    "Ospreys": "Fishers", "Royals": "Crown", "Hatters": "Milliners", "Wolves": "Pack",
    "Sun Devils": "Solar Imps", "Jayhawks": "Jaybirds", "Horned Frogs": "Spiked Toads",
    "Red Raiders": "Crimson Riders", "Bluejays": "Azure Jays", "Blue Demons": "Indigo Imps",
    "Hoyas": "Stonework", "Friars": "Brothers", "Red Storm": "Crimson Squall", "Musketeers": "Muskets",
    "Vandals": "Outlaws", "Bengals": "Stripes", "Grizzlies": "Grays", "Bobcats": "Lynxes",
    "Lumberjacks": "Timbermen", "Vikings": "Norse", "Thunderbirds": "Stormbirds", "Trailblazers": "Pathfinders",
    "Runnin' Bulldogs": "Running Mastiffs", "Blue Hose": "Blue Stockings", "Lancers": "Spearmen",
    "Fighting Illini": "Fighting Lines", "Terrapins": "Diamondbacks", "Wolverines": "Gluttons",
    "Golden Gophers": "Golden Burrowers", "Cornhuskers": "Harvesters", "Nittany Lions": "Nittany Cats",
    "Boilermakers": "Forgehands", "Scarlet Knights": "Scarlet Riders", "Seawolves": "Sea Wolves",
    "Tribe": "Nation", "Gamecocks": "Barnbirds", "Flames": "Embers", "Blue Raiders": "Blue Riders",
    "Bearkats": "Bear Cats", "Hilltoppers": "Summiteers", "Vikings": "Norse", "Titans": "Colossi",
    "Phoenix": "Firebirds", "Jaguars": "Panthers", "Norse": "Vikings", "Golden Grizzlies": "Golden Grays",
    "Mastodons": "Megafauna", "Colonials": "Settlers", "Penguins": "Auks", "Big Red": "Big Crimson",
    "Big Green": "Big Forest", "Crimson": "Scarlet", "Quakers": "Friends", "Golden Griffins": "Golden Gryphons",
    "Stags": "Bucks", "Gaels": "Clans", "Jaspers": "Quartz", "Red Foxes": "Red Vulpes",
    "Purple Eagles": "Purple Hawks", "Bobcats": "Wildcats", "Broncs": "Stallions", "Peacocks": "Fancies",
    "Saints": "Halos", "Zips": "Fasteners", "Falcons": "Raptors", "Chippewas": "Chip Clans",
    "Golden Flashes": "Gold Bolts", "RedHawks": "Red Hawks", "Bobcats": "Lynxes", "Minutemen": "Muskets",
    "Broncos": "Stallions", "Bison": "Buffalo", "Spartans": "Hoplites", "Bruins": "Bears",
    "Braves": "Warriors", "Purple Aces": "Purple Cards", "Redbirds": "Cardinals", "Sycamores": "Trees",
    "Racers": "Sprinters", "Salukis": "Hounds", "Flames": "Blaze", "Beacons": "Signals",
    "Rainbow Warriors": "Rainbow Guard", "Wolf Pack": "Timberwolves", "Lobos": "Wolves",
    "Runnin' Rebels": "Running Renegades", "Miners": "Diggers", "Cowboys": "Wranglers",
    "Cougars": "Pumas", "Red Flash": "Red Burst", "Skyhawks": "Cloud Hawks", "Seahawks": "Sea Hawks",
    "Leathernecks": "Leather Guards", "Redhawks": "Crimson Hawks", "Leopards": "Cats", "Mountain Hawks": "Peak Hawks",
    "Greyhounds": "Hounds", "Midshipmen": "Sailors", "Crimson Tide": "Crimson Surge",
    "Gamecocks": "Strutters", "Commodores": "Captains", "Mocs": "Chattans", "Paladins": "Defenders",
    "Citadel": "Stronghold", "Golden Eagles": "Golden Hawks", "Keydets": "Cadets", "Terriers": "Hounds",
    "Privateers": "Freebooters", "Colonels": "Officers", "Demons": "Imps", "Lumberjacks": "Axemen",
    "Islanders": "Isles", "Vaqueros": "Ranch Hands", "Hornets": "Stingers", "Braves": "Warriors",
    "Golden Lions": "Golden Monarchs", "Rattlers": "Buzztails", "Delta Devils": "Delta Fiends",
    "Jaguars": "Panthers", "Roo": "Kangas", "Fighting Hawks": "Fighting Kites", "Jackrabbits": "Hares",
    "Tommies": "Saints", "Chanticleers": "Rooster", "Red Wolves": "Crimson Wolves", "Ragin' Cajuns": "Ragin' Roux",
    "Warhawks": "Warbirds", "Thundering Herd": "Stampede", "Monarchs": "Crown", "Golden Eagles": "Golden Hawks",
    "Pioneers": "Trailblazers", "Toreros": "Bullfighters", "Dons": "Lords", "Gaels": "Highlanders",
}

ANCHOR_HINTS = {
    "Albany": "Albany, NY", "Duke": "Durham, NC", "North Carolina": "Chapel Hill, NC",
    "Kansas": "Lawrence, KS", "Kentucky": "Lexington, KY", "UCLA": "Los Angeles, CA",
    "Gonzaga": "Spokane, WA", "Villanova": "Villanova, PA", "UConn": "Storrs, CT",
    "Hawaiʻi": "Honolulu, HI", "Notre Dame": "Notre Dame, IN", "Texas": "Austin, TX",
}


def strip_paren(name: str) -> str:
    return re.sub(r"\s*\([^)]*\)", "", name).strip()


def split_team(real: str) -> tuple[str, str]:
    real = strip_paren(real)
    parts = real.split()
    if len(parts) < 2:
        return real, ""
    # multi-word mascots
    multis = [
        "Blue Devils", "Tar Heels", "Fighting Irish", "Golden Bears", "Yellow Jackets", "Blue Demons",
        "Golden Eagles", "Red Storm", "Sun Devils", "Horned Frogs", "Red Raiders", "Fighting Illini",
        "Golden Gophers", "Nittany Lions", "Scarlet Knights", "Fighting Camels", "Fighting Hawks",
        "Blue Hens", "Blue Raiders", "Rainbow Warriors", "Wolf Pack", "Runnin' Rebels", "Red Flash",
        "Golden Hurricane", "Mean Green", "Green Wave", "River Hawks", "Black Bears", "Great Danes",
        "Demon Deacons", "Crimson Tide", "Golden Flashes", "Red Wolves", "Ragin' Cajuns", "Thundering Herd",
        "Golden Grizzlies", "Big Red", "Big Green", "Red Foxes", "Purple Eagles", "Purple Aces",
        "Blue Hose", "Runnin' Bulldogs", "Golden Lions", "Delta Devils", "Mountain Hawks",
    ]
    for m in sorted(multis, key=len, reverse=True):
        if real.endswith(m):
            return real[: -len(m)].strip(), m
    return " ".join(parts[:-1]), parts[-1]


def universe_team(real: str, conf_id: str, idx: int) -> tuple[str, str, str]:
    if real in TEAM_OVERRIDES:
        uni, anchor = TEAM_OVERRIDES[real]
        return f"team-{conf_id}-{idx:03d}", uni, anchor
    school, mascot = split_team(real)
    school_clean = strip_paren(school)
    mascot_key = mascot
    new_mascot = MASCOT_SWAP.get(mascot_key, MASCOT_SWAP.get(mascot, None))
    if not new_mascot:
        new_mascot = mascot  # keep mascot family when no swap defined

    # Institution twist: add "State" or swap first token
    tokens = school_clean.split()
    uni_school = school_clean
    if tokens and tokens[0] in ("UNC", "UMass", "UTSA", "UAB", "UCF", "USC", "UCLA", "NJIT", "FIU"):
        uni_school = {"UNC": "North Cape", "UMass": "Bay State", "UTSA": "South Alamo", "UAB": "Birmingham Metro",
                      "UCF": "Central Florida Metro", "USC": "Southern California Metro",
                      "UCLA": "Los Angeles Metro", "NJIT": "Newark Tech", "FIU": "Miami-Dade Metro"}.get(
            tokens[0], tokens[0] + " Metro"
        ) + (" " + " ".join(tokens[1:]) if len(tokens) > 1 else "")
    elif "State" in school_clean and "Metro" not in uni_school and not school_clean.endswith("State"):
        uni_school = school_clean.replace("State", "Commonwealth", 1)
    elif school_clean in ANCHOR_HINTS:
        city = ANCHOR_HINTS[school_clean].split(",")[0]
        uni_school = f"{city} Metro"
    else:
        uni_school = school_clean.replace("St.", "Saint").replace("&", "and")

    universe = f"{uni_school} {new_mascot}".strip()
    short = "".join(w[0] for w in uni_school.split()[:3]).upper()[:4] or f"T{idx:03d}"

    anchor = ANCHOR_HINTS.get(school_clean.split()[0], ANCHOR_HINTS.get(school_clean, "TBD — pack geography"))
    if anchor == "TBD — pack geography" and school_clean.split():
        anchor = f"{school_clean.split()[0]} region (align to real)"

    team_id = f"team-{conf_id}-{idx:03d}"
    return team_id, universe, anchor


def main() -> None:
    out = Path(__file__).resolve().parents[1] / "league-branding.md"
    lines: list[str] = []
    lines.append("# League branding — conferences & teams (reference mapping)")
    lines.append("")
    lines.append("**Product:** [Dynasty: College Basketball](branding.md) · default **universe** names for identity packs.")
    lines.append("**Purpose:** Keep **real-world reference** alongside **ship-safe display** names so schedules, geography, colors, and tournament fidelity stay accurate.")
    lines.append("**Related:** [`game-design.md`](game-design.md#identity-packs--save-model), [`branding.md`](branding.md).")
    lines.append("**Last updated:** 2025-11-08")
    lines.append("")
    lines.append("---")
    lines.append("")
    lines.append("## Contents")
    lines.append("")
    lines.append("1. [Conferences](#conferences)")
    lines.append("2. [Teams by conference](#teams-by-conference)")
    lines.append("3. [National body & tournaments (TBD)](#national-body--tournaments-tbd)")
    lines.append("4. [Pack authoring notes](#pack-authoring-notes)")
    lines.append("5. [Document history](#document-history)")
    lines.append("")
    lines.append("---")
    lines.append("")
    lines.append("## Conferences")
    lines.append("")
    lines.append("| # | Real (reference) | Universe (default pack) | `confId` | Short |")
    lines.append("|---|------------------|-------------------------|----------|-------|")
    for i, (real, uni, cid, short) in enumerate(CONFERENCES, 1):
        lines.append(f"| {i} | {real} | **{uni}** | `{cid}` | {short} |")
    lines.append("")
    lines.append("---")
    lines.append("")
    lines.append("## Teams by conference")
    lines.append("")
    lines.append("Columns: stable `teamId`, real program (editor reference), universe display name, geography anchor for pack data (city / colors / market — **flesh out in JSON**).")
    lines.append("")

    conf_name_by_id = {c[2]: c for c in CONFERENCES}
    total = 0
    for conf_id, teams in MEMBERSHIP_FIXED:
        c = conf_name_by_id.get(conf_id)
        if not c:
            continue
        real_conf, uni_conf, _, _ = c
        lines.append(f"### {uni_conf}")
        lines.append("")
        lines.append(f"*Real league: {real_conf}* · `{conf_id}`")
        lines.append("")
        lines.append("| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |")
        lines.append("|----------|-------------------|-------------------------|------------------|")
        for idx, real in enumerate(teams, 1):
            tid, uni, anchor = universe_team(real, conf_id, idx)
            lines.append(f"| `{tid}` | {real} | {uni} | {anchor} |")
            total += 1
        lines.append("")

    lines.append("---")
    lines.append("")
    lines.append("## National body & tournaments (TBD)")
    lines.append("")
    lines.append("| Real (reference) | Universe (planned) | Notes |")
    lines.append("|-------------------|-------------------|--------|")
    lines.append("| NCAA | **National Collegiate Basketball Association (NCBA)** or **Dynasty Collegiate Association (DCA)** | Final label TBD |")
    lines.append("| NCAA tournament / March Madness | **National Championship** / **March Classic** | Bracket structure per game-design |")
    lines.append("| NIT | **Secondary National Invitational** | Tie to coach-card secondary postseason |")
    lines.append("")
    lines.append("---")
    lines.append("")
    lines.append("## Pack authoring notes")
    lines.append("")
    lines.append("| Rule | Detail |")
    lines.append("|------|--------|")
    lines.append("| **IDs** | Simulation uses `confId` / `teamId` only; display strings from pack overlay |")
    lines.append("| **Colors & logos** | Map from real reference in pack JSON; universe name does not block hex fidelity |")
    lines.append("| **This doc** | Source of truth for **default pack** naming; optional import packs may swap display only |")
    lines.append("| **Regeneration** | Re-run `planning/tools/generate_league_branding.py` after membership edits; hand-tune flagships in overrides (future) |")
    lines.append("")
    lines.append(f"**Membership count (this file):** {total} teams · {len(CONFERENCES)} conferences.")
    lines.append("")
    lines.append("---")
    lines.append("")
    lines.append("## Document history")
    lines.append("")
    lines.append("| Date | Change |")
    lines.append("|------|--------|")
    lines.append("| 2025-11-08 | Initial conference + team mapping from 2026–27 D-I membership list |")
    lines.append("")

    out.write_text("\n".join(lines), encoding="utf-8")
    print(f"Wrote {out} ({total} teams)")


if __name__ == "__main__":
    main()
