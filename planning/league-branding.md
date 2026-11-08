# League branding — conferences & teams (reference mapping)

**Product:** [Dynasty: College Basketball](branding.md) · default **universe** names for identity packs.
**Purpose:** Keep **real-world reference** alongside **ship-safe display** names so schedules, geography, colors, and tournament fidelity stay accurate.
**Related:** [`game-design.md`](game-design.md#identity-packs--save-model), [`branding.md`](branding.md).
**Last updated:** 2025-11-08  
**Status:** v1 — full **356-team** mapping; universe names are ship-safe defaults (generator + selective overrides). **Next:** per-team primary/secondary colors, markets, arena labels in pack JSON; polish blue-blood display names.

---

## Contents

1. [Conferences](#conferences)
2. [Teams by conference](#teams-by-conference)
3. [National body & tournaments (TBD)](#national-body--tournaments-tbd)
4. [Pack authoring notes](#pack-authoring-notes)
5. [Document history](#document-history)

---

## Conferences

| # | Real (reference) | Universe (default pack) | `confId` | Short |
|---|------------------|-------------------------|----------|-------|
| 1 | America East Conference | **Northern Atlantic Collegiate Conference** | `conf-nac` | NAC |
| 2 | American Athletic Conference (AAC) | **American Unified Collegiate Conference** | `conf-aucc` | AUCC |
| 3 | Atlantic 10 Conference (A-10) | **Colonial Atlantic Ten** | `conf-cat10` | CAT10 |
| 4 | Atlantic Coast Conference (ACC) | **Atlantic Crown Conference** | `conf-acrown` | ACROWN |
| 5 | Atlantic Sun Conference (ASUN) | **Atlantic Sun Collegiate Union** | `conf-ascu` | ASC-U |
| 6 | Big 12 Conference | **Central Twelve Conference** | `conf-c12` | C12 |
| 7 | Big East Conference | **Eastern Premier Conference** | `conf-epc` | EPC |
| 8 | Big Sky Conference | **Northern Sky Conference** | `conf-nsc` | NSC |
| 9 | Big South Conference | **Piedmont South Conference** | `conf-psc` | PSC |
| 10 | Big Ten Conference | **Great Ten Conference** | `conf-g10` | G10 |
| 11 | Big West Conference | **Golden West Conference** | `conf-gwc` | GWC |
| 12 | Coastal Athletic Association (CAA) | **Coastal Colonial Association** | `conf-cca` | CCA |
| 13 | Conference USA (C-USA) | **United Continental Conference** | `conf-ucc` | UCC |
| 14 | Horizon League | **Lakefront Horizon League** | `conf-lhl` | LHL |
| 15 | Ivy League | **Founding Eight League** | `conf-fel` | FEL |
| 16 | Metro Atlantic Athletic Conference (MAAC) | **Metro Atlantic Collegiate Alliance** | `conf-maca` | MACA |
| 17 | Mid-American Conference (MAC) | **Midland American Circuit** | `conf-mac` | MAC |
| 18 | Mid-Eastern Athletic Conference (MEAC) | **Mid-Eastern Alliance Conference** | `conf-meac` | MEAC |
| 19 | Missouri Valley Conference (MVC) | **Prairie Valley Conference** | `conf-pvc` | PVC |
| 20 | Mountain West Conference (MW) | **Rocky Mountain West Conference** | `conf-rmwc` | RMWC |
| 21 | Northeast Conference (NEC) | **Northeastern Exchange Conference** | `conf-nec` | NEC |
| 22 | Ohio Valley Conference (OVC) | **Ohio Valley Alliance** | `conf-ova` | OVA |
| 23 | Pac-12 Conference | **Pacific Rim Twelve** | `conf-pr12` | PR12 |
| 24 | Patriot League | **Patriot Scholar League** | `conf-psl` | PSL |
| 25 | Southeastern Conference (SEC) | **Southeastern Dominion Conference** | `conf-sedc` | SEDC |
| 26 | Southern Conference (SoCon) | **Southern Collegiate Federation** | `conf-scf` | SCF |
| 27 | Southland Conference | **Gulf Coastland Conference** | `conf-gcc` | GCC |
| 28 | Southwestern Athletic Conference (SWAC) | **Southwest Collegiate Athletic League** | `conf-scal` | SCAL |
| 29 | Summit League | **Prairie Summit League** | `conf-summit` | PSL |
| 30 | Sun Belt Conference | **Solar Belt Conference** | `conf-sbc` | SBC |
| 31 | West Coast Conference (WCC) | **Western Coast Collegiate** | `conf-wcc` | WCC |

---

## Teams by conference

Columns: stable `teamId`, real program (editor reference), universe display name, geography anchor for pack data (city / colors / market — **flesh out in JSON**).

### Northern Atlantic Collegiate Conference

*Real league: America East Conference* · `conf-nac`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-nac-001` | Albany Great Danes | Albany Metro Tall Hounds | Albany, NY |
| `team-conf-nac-002` | Binghamton Bearcats | Binghamton Ringtails | Binghamton region (align to real) |
| `team-conf-nac-003` | Bryant Bulldogs | Bryant Mastiffs | Bryant region (align to real) |
| `team-conf-nac-004` | Maine Black Bears | Maine Ursines | Maine region (align to real) |
| `team-conf-nac-005` | New Hampshire Wildcats | New Hampshire Lynx | New region (align to real) |
| `team-conf-nac-006` | NJIT Highlanders | Newark Tech Clansmen | NJIT region (align to real) |
| `team-conf-nac-007` | UMBC Retrievers | UMBC Fetchers | UMBC region (align to real) |
| `team-conf-nac-008` | UMass Lowell River Hawks | Bay State Lowell Stream Hawks | UMass region (align to real) |
| `team-conf-nac-009` | Vermont Catamounts | Vermont Mountain Cats | Vermont region (align to real) |

### American Unified Collegiate Conference

*Real league: American Athletic Conference (AAC)* · `conf-aucc`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-aucc-001` | Charlotte 49ers | Charlotte Prospectors | Charlotte region (align to real) |
| `team-conf-aucc-002` | East Carolina Pirates | East Carolina Corsairs | East region (align to real) |
| `team-conf-aucc-003` | Florida Atlantic Owls (FAU) | Florida Atlantic Raptors | Florida region (align to real) |
| `team-conf-aucc-004` | Memphis Tigers | Memphis Cougars | Memphis region (align to real) |
| `team-conf-aucc-005` | North Texas Mean Green | North Texas Mean Teal | North region (align to real) |
| `team-conf-aucc-006` | Rice Owls | Rice Raptors | Rice region (align to real) |
| `team-conf-aucc-007` | South Florida Bulls (USF) | South Florida Stampede | South region (align to real) |
| `team-conf-aucc-008` | Temple Owls | Temple Raptors | Temple region (align to real) |
| `team-conf-aucc-009` | Tulane Green Wave | Tulane Green Surge | Tulane region (align to real) |
| `team-conf-aucc-010` | Tulsa Golden Hurricane | Tulsa Golden Gale | Tulsa region (align to real) |
| `team-conf-aucc-011` | UAB Blazers | Birmingham Metro Foragers | UAB region (align to real) |
| `team-conf-aucc-012` | UTSA Roadrunners | South Alamo Sprinters | UTSA region (align to real) |
| `team-conf-aucc-013` | Wichita State Shockers | Wichita State Jolters | Wichita region (align to real) |

### Colonial Atlantic Ten

*Real league: Atlantic 10 Conference (A-10)* · `conf-cat10`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-cat10-001` | Davidson Wildcats | Davidson Lynx | Davidson region (align to real) |
| `team-conf-cat10-002` | Dayton Flyers | Dayton Aviators | Dayton region (align to real) |
| `team-conf-cat10-003` | Duquesne Dukes | Duquesne Barons | Duquesne region (align to real) |
| `team-conf-cat10-004` | Fordham Rams | Fordham Bighorns | Fordham region (align to real) |
| `team-conf-cat10-005` | George Mason Patriots | George Mason Minutemen | George region (align to real) |
| `team-conf-cat10-006` | George Washington Revolutionaries | George Washington Independents | George region (align to real) |
| `team-conf-cat10-007` | La Salle Explorers | La Salle Voyagers | La region (align to real) |
| `team-conf-cat10-008` | Loyola Chicago Ramblers | Loyola Chicago Wayfarers | Loyola region (align to real) |
| `team-conf-cat10-009` | Rhode Island Rams | Rhode Island Bighorns | Rhode region (align to real) |
| `team-conf-cat10-010` | Richmond Spiders | Richmond Widows | Richmond region (align to real) |
| `team-conf-cat10-011` | Saint Joseph's Hawks | Saint Joseph's Falcons | Saint region (align to real) |
| `team-conf-cat10-012` | Saint Louis Billikens | Saint Louis Icons | Saint region (align to real) |
| `team-conf-cat10-013` | St. Bonaventure Bonnies | Saint Bonaventure Bonfires | St. region (align to real) |
| `team-conf-cat10-014` | VCU Rams | VCU Bighorns | VCU region (align to real) |

### Atlantic Crown Conference

*Real league: Atlantic Coast Conference (ACC)* · `conf-acrown`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-acrown-001` | Boston College Eagles | Boston College Hawks | Boston region (align to real) |
| `team-conf-acrown-002` | California Golden Bears (Cal) | Bay Golden Bruins | Berkeley, CA |
| `team-conf-acrown-003` | Clemson Tigers | Clemson Cougars | Clemson region (align to real) |
| `team-conf-acrown-004` | Duke Blue Devils | Crown University Sovereigns | Durham, NC |
| `team-conf-acrown-005` | Florida State Seminoles | Florida State Lakeward Tribe | Florida region (align to real) |
| `team-conf-acrown-006` | Georgia Tech Yellow Jackets | Georgia Tech Gold Jackets | Georgia region (align to real) |
| `team-conf-acrown-007` | Louisville Cardinals | Louisville Scarlets | Louisville region (align to real) |
| `team-conf-acrown-008` | Miami Hurricanes | Miami Cyclones | Miami region (align to real) |
| `team-conf-acrown-009` | NC State Wolfpack | NC State Timber Pack | NC region (align to real) |
| `team-conf-acrown-010` | North Carolina Tar Heels | Tar River Commonwealth | Chapel Hill, NC |
| `team-conf-acrown-011` | Notre Dame Fighting Irish | Notre Dame Metro Emerald Guard | Notre Dame, IN |
| `team-conf-acrown-012` | Pittsburgh Panthers | Pittsburgh Pumas | Pittsburgh region (align to real) |
| `team-conf-acrown-013` | SMU Mustangs | SMU Stallions | SMU region (align to real) |
| `team-conf-acrown-014` | Stanford Cardinal | Stanford Crimson | Stanford region (align to real) |
| `team-conf-acrown-015` | Syracuse Orange | Syracuse Citrus | Syracuse region (align to real) |
| `team-conf-acrown-016` | Virginia Cavaliers | Virginia Lancers | Virginia region (align to real) |
| `team-conf-acrown-017` | Virginia Tech Hokies | Virginia Tech Techmen | Virginia region (align to real) |
| `team-conf-acrown-018` | Wake Forest Demon Deacons | Wake Forest Spirit Callers | Wake region (align to real) |

### Atlantic Sun Collegiate Union

*Real league: Atlantic Sun Conference (ASUN)* · `conf-ascu`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-ascu-001` | Bellarmine Knights | Bellarmine Squires | Bellarmine region (align to real) |
| `team-conf-ascu-002` | Florida Gulf Coast Eagles (FGCU) | Florida Gulf Coast Hawks | Florida region (align to real) |
| `team-conf-ascu-003` | Jacksonville Dolphins | Jacksonville Mariners | Jacksonville region (align to real) |
| `team-conf-ascu-004` | Lipscomb Bisons | Lipscomb Buffaloes | Lipscomb region (align to real) |
| `team-conf-ascu-005` | North Florida Ospreys | North Florida Fishers | North region (align to real) |
| `team-conf-ascu-006` | Queens Royals | Queens Crown | Queens region (align to real) |
| `team-conf-ascu-007` | Stetson Hatters | Stetson Milliners | Stetson region (align to real) |
| `team-conf-ascu-008` | West Georgia Wolves | West Georgia Pack | West region (align to real) |

### Central Twelve Conference

*Real league: Big 12 Conference* · `conf-c12`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-c12-001` | Arizona Wildcats | Arizona Lynx | Arizona region (align to real) |
| `team-conf-c12-002` | Arizona State Sun Devils | Arizona State Solar Imps | Arizona region (align to real) |
| `team-conf-c12-003` | Baylor Bears | Baylor Bruins | Baylor region (align to real) |
| `team-conf-c12-004` | BYU Cougars | BYU Pumas | BYU region (align to real) |
| `team-conf-c12-005` | Cincinnati Bearcats | Cincinnati Ringtails | Cincinnati region (align to real) |
| `team-conf-c12-006` | Colorado Buffaloes | Colorado Buffaloes | Colorado region (align to real) |
| `team-conf-c12-007` | Houston Cougars | Houston Pumas | Houston region (align to real) |
| `team-conf-c12-008` | Iowa State Cyclones | Iowa State Cyclones | Iowa region (align to real) |
| `team-conf-c12-009` | Kansas Jayhawks | Jaydale Commonwealth | Lawrence, KS |
| `team-conf-c12-010` | Kansas State Wildcats | Kansas State Lynx | Lawrence, KS |
| `team-conf-c12-011` | Oklahoma State Cowboys | Oklahoma State Wranglers | Oklahoma region (align to real) |
| `team-conf-c12-012` | TCU Horned Frogs | TCU Spiked Toads | TCU region (align to real) |
| `team-conf-c12-013` | Texas Tech Red Raiders | Texas Tech Crimson Riders | Austin, TX |
| `team-conf-c12-014` | UCF Knights | Central Florida Metro Squires | UCF region (align to real) |
| `team-conf-c12-015` | Utah Utes | Utah Utes | Utah region (align to real) |
| `team-conf-c12-016` | West Virginia Mountaineers | West Virginia Highlanders | West region (align to real) |

### Eastern Premier Conference

*Real league: Big East Conference* · `conf-epc`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-epc-001` | Butler Bulldogs | Butler Mastiffs | Butler region (align to real) |
| `team-conf-epc-002` | Creighton Bluejays | Creighton Azure Jays | Creighton region (align to real) |
| `team-conf-epc-003` | DePaul Blue Demons | DePaul Indigo Imps | DePaul region (align to real) |
| `team-conf-epc-004` | Georgetown Hoyas | Georgetown Stonework | Georgetown region (align to real) |
| `team-conf-epc-005` | Marquette Golden Eagles | Marquette Golden Hawks | Marquette region (align to real) |
| `team-conf-epc-006` | Providence Friars | Providence Brothers | Providence region (align to real) |
| `team-conf-epc-007` | Seton Hall Pirates | Seton Hall Corsairs | Seton region (align to real) |
| `team-conf-epc-008` | St. John's Red Storm | Saint John's Crimson Squall | St. region (align to real) |
| `team-conf-epc-009` | UConn Huskies | Storrs Sled Dogs | Storrs, CT |
| `team-conf-epc-010` | Villanova Wildcats | Main Line Lynx | Villanova, PA |
| `team-conf-epc-011` | Xavier Musketeers | Xavier Muskets | Xavier region (align to real) |

### Northern Sky Conference

*Real league: Big Sky Conference* · `conf-nsc`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-nsc-001` | Eastern Washington Eagles | Eastern Washington Hawks | Eastern region (align to real) |
| `team-conf-nsc-002` | Idaho Vandals | Idaho Outlaws | Idaho region (align to real) |
| `team-conf-nsc-003` | Idaho State Bengals | Idaho State Stripes | Idaho region (align to real) |
| `team-conf-nsc-004` | Montana Grizzlies | Montana Grays | Montana region (align to real) |
| `team-conf-nsc-005` | Montana State Bobcats | Montana State Lynxes | Montana region (align to real) |
| `team-conf-nsc-006` | Northern Arizona Lumberjacks | Northern Arizona Axemen | Northern region (align to real) |
| `team-conf-nsc-007` | Northern Colorado Bears | Northern Colorado Bruins | Northern region (align to real) |
| `team-conf-nsc-008` | Portland State Vikings | Portland State Norse | Portland region (align to real) |
| `team-conf-nsc-009` | Southern Utah Thunderbirds | Southern Utah Stormbirds | Southern region (align to real) |
| `team-conf-nsc-010` | Utah Tech Trailblazers | Utah Tech Pathfinders | Utah region (align to real) |
| `team-conf-nsc-011` | Weber State Wildcats | Weber State Lynx | Weber region (align to real) |

### Piedmont South Conference

*Real league: Big South Conference* · `conf-psc`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-psc-001` | Charleston Southern Buccaneers | Charleston Southern Buccaneers | Charleston region (align to real) |
| `team-conf-psc-002` | Gardner-Webb Runnin' Bulldogs | Gardner-Webb Running Mastiffs | Gardner-Webb region (align to real) |
| `team-conf-psc-003` | High Point Panthers | High Point Pumas | High region (align to real) |
| `team-conf-psc-004` | Longwood Lancers | Longwood Spearmen | Longwood region (align to real) |
| `team-conf-psc-005` | Presbyterian Blue Hose | Presbyterian Blue Stockings | Presbyterian region (align to real) |
| `team-conf-psc-006` | Radford Highlanders | Radford Clansmen | Radford region (align to real) |
| `team-conf-psc-007` | UNC Asheville Bulldogs | North Cape Asheville Mastiffs | UNC region (align to real) |
| `team-conf-psc-008` | USC Upstate Spartans | Southern California Metro Upstate Hoplites | USC region (align to real) |
| `team-conf-psc-009` | Winthrop Eagles | Winthrop Hawks | Winthrop region (align to real) |

### Great Ten Conference

*Real league: Big Ten Conference* · `conf-g10`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-g10-001` | Illinois Fighting Illini | Illinois Fighting Lines | Illinois region (align to real) |
| `team-conf-g10-002` | Indiana Hoosiers | Indiana Pioneers | Indiana region (align to real) |
| `team-conf-g10-003` | Iowa Hawkeyes | Iowa Hawkeyes | Iowa region (align to real) |
| `team-conf-g10-004` | Maryland Terrapins | Maryland Diamondbacks | Maryland region (align to real) |
| `team-conf-g10-005` | Michigan Wolverines | Michigan Gluttons | Michigan region (align to real) |
| `team-conf-g10-006` | Michigan State Spartans | Michigan State Hoplites | Michigan region (align to real) |
| `team-conf-g10-007` | Minnesota Golden Gophers | Minnesota Golden Burrowers | Minnesota region (align to real) |
| `team-conf-g10-008` | Nebraska Cornhuskers | Nebraska Harvesters | Nebraska region (align to real) |
| `team-conf-g10-009` | Northwestern Wildcats | Northwestern Lynx | Northwestern region (align to real) |
| `team-conf-g10-010` | Ohio State Buckeyes | Ohio State Chestnuts | Ohio region (align to real) |
| `team-conf-g10-011` | Oregon Ducks | Oregon Drakes | Oregon region (align to real) |
| `team-conf-g10-012` | Penn State Nittany Lions | Penn State Nittany Cats | Penn region (align to real) |
| `team-conf-g10-013` | Purdue Boilermakers | Purdue Forgehands | Purdue region (align to real) |
| `team-conf-g10-014` | Rutgers Scarlet Knights | Rutgers Scarlet Riders | Rutgers region (align to real) |
| `team-conf-g10-015` | UCLA Bruins | Westwood Harbor Guardians | Los Angeles, CA |
| `team-conf-g10-016` | USC Trojans | South Bay Argonauts | Los Angeles, CA |
| `team-conf-g10-017` | Washington Huskies | Washington Sled Dogs | Washington region (align to real) |
| `team-conf-g10-018` | Wisconsin Badgers | Wisconsin Badgers | Wisconsin region (align to real) |

### Golden West Conference

*Real league: Big West Conference* · `conf-gwc`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-gwc-001` | Cal Baptist Lancers | Cal Baptist Spearmen | Cal region (align to real) |
| `team-conf-gwc-002` | Cal Poly Mustangs | Cal Poly Stallions | Cal region (align to real) |
| `team-conf-gwc-003` | Cal State Bakersfield Roadrunners | Cal Commonwealth Bakersfield Sprinters | Cal region (align to real) |
| `team-conf-gwc-004` | Cal State Fullerton Titans | Cal Commonwealth Fullerton Colossi | Cal region (align to real) |
| `team-conf-gwc-005` | Cal State Northridge Matadors (CSUN) | Cal Commonwealth Northridge Lancers | Cal region (align to real) |
| `team-conf-gwc-006` | Long Beach State Beach | Long Beach State Shore | Long region (align to real) |
| `team-conf-gwc-007` | Sacramento State Hornets | Sacramento State Stingers | Sacramento region (align to real) |
| `team-conf-gwc-008` | UC Davis Aggies | UC Davis Rustics | UC region (align to real) |
| `team-conf-gwc-009` | UC Irvine Anteaters | UC Irvine Aardvarks | UC region (align to real) |
| `team-conf-gwc-010` | UC Riverside Highlanders | UC Riverside Clansmen | UC region (align to real) |
| `team-conf-gwc-011` | UC San Diego Tritons | UC San Diego Tritons | UC region (align to real) |
| `team-conf-gwc-012` | UC Santa Barbara Gauchos | UC Santa Barbara Rancheros | UC region (align to real) |

### Coastal Colonial Association

*Real league: Coastal Athletic Association (CAA)* · `conf-cca`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-cca-001` | Campbell Fighting Camels | Campbell Fighting Camels | Campbell region (align to real) |
| `team-conf-cca-002` | College of Charleston Cougars | College of Charleston Pumas | College region (align to real) |
| `team-conf-cca-003` | Drexel Dragons | Drexel Dragons | Drexel region (align to real) |
| `team-conf-cca-004` | Elon Phoenix | Elon Firebirds | Elon region (align to real) |
| `team-conf-cca-005` | Hampton Pirates | Hampton Corsairs | Hampton region (align to real) |
| `team-conf-cca-006` | Hofstra Pride | Hofstra Pride | Hofstra region (align to real) |
| `team-conf-cca-007` | Monmouth Hawks | Monmouth Falcons | Monmouth region (align to real) |
| `team-conf-cca-008` | North Carolina A&T Aggies | North Carolina AandT Rustics | North region (align to real) |
| `team-conf-cca-009` | Northeastern Huskies | Northeastern Sled Dogs | Northeastern region (align to real) |
| `team-conf-cca-010` | Stony Brook Seawolves | Stony Brook Sea Wolves | Stony region (align to real) |
| `team-conf-cca-011` | Towson Tigers | Towson Cougars | Towson region (align to real) |
| `team-conf-cca-012` | UNC Wilmington Seahawks (UNCW) | North Cape Wilmington Sea Hawks | UNC region (align to real) |
| `team-conf-cca-013` | William & Mary Tribe | William and Mary Nation | William region (align to real) |

### United Continental Conference

*Real league: Conference USA (C-USA)* · `conf-ucc`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-ucc-001` | Delaware Fightin' Blue Hens | Delaware Fightin' Blue Hens | Delaware region (align to real) |
| `team-conf-ucc-002` | FIU Panthers | Miami-Dade Metro Pumas | FIU region (align to real) |
| `team-conf-ucc-003` | Jacksonville State Gamecocks | Jacksonville State Strutters | Jacksonville region (align to real) |
| `team-conf-ucc-004` | Kennesaw State Owls | Kennesaw State Raptors | Kennesaw region (align to real) |
| `team-conf-ucc-005` | Liberty Flames | Liberty Blaze | Liberty region (align to real) |
| `team-conf-ucc-006` | Middle Tennessee Blue Raiders (MTSU) | Middle Tennessee Blue Riders | Middle region (align to real) |
| `team-conf-ucc-007` | Missouri State Bears | Missouri State Bruins | Missouri region (align to real) |
| `team-conf-ucc-008` | New Mexico State Aggies | New Mexico State Rustics | New region (align to real) |
| `team-conf-ucc-009` | Sam Houston Bearkats | Sam Houston Bear Cats | Sam region (align to real) |
| `team-conf-ucc-010` | Western Kentucky Hilltoppers (WKU) | Western Kentucky Summiteers | Western region (align to real) |

### Lakefront Horizon League

*Real league: Horizon League* · `conf-lhl`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-lhl-001` | Cleveland State Vikings | Cleveland State Norse | Cleveland region (align to real) |
| `team-conf-lhl-002` | Detroit Mercy Titans | Detroit Mercy Colossi | Detroit region (align to real) |
| `team-conf-lhl-003` | Green Bay Phoenix | Green Bay Firebirds | Green region (align to real) |
| `team-conf-lhl-004` | IU Indy Jaguars (formerly IUPUI) | IU Indy Panthers | IU region (align to real) |
| `team-conf-lhl-005` | Milwaukee Panthers | Milwaukee Pumas | Milwaukee region (align to real) |
| `team-conf-lhl-006` | Northern Illinois Huskies (NIU) | Northern Illinois Sled Dogs | Northern region (align to real) |
| `team-conf-lhl-007` | Northern Kentucky Norse | Northern Kentucky Vikings | Northern region (align to real) |
| `team-conf-lhl-008` | Oakland Golden Grizzlies | Oakland Golden Grays | Oakland region (align to real) |
| `team-conf-lhl-009` | Purdue Fort Wayne Mastodons | Purdue Fort Wayne Megafauna | Purdue region (align to real) |
| `team-conf-lhl-010` | Robert Morris Colonials | Robert Morris Settlers | Robert region (align to real) |
| `team-conf-lhl-011` | Wright State Raiders | Wright State Rangers | Wright region (align to real) |
| `team-conf-lhl-012` | Youngstown State Penguins | Youngstown State Auks | Youngstown region (align to real) |

### Founding Eight League

*Real league: Ivy League* · `conf-fel`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-fel-001` | Brown Bears | Brown Bruins | Brown region (align to real) |
| `team-conf-fel-002` | Columbia Lions | Columbia Monarchs | Columbia region (align to real) |
| `team-conf-fel-003` | Cornell Big Red | Cornell Big Crimson | Cornell region (align to real) |
| `team-conf-fel-004` | Dartmouth Big Green | Dartmouth Big Forest | Dartmouth region (align to real) |
| `team-conf-fel-005` | Harvard Crimson | Harvard Scarlet | Harvard region (align to real) |
| `team-conf-fel-006` | Penn Quakers | Penn Friends | Penn region (align to real) |
| `team-conf-fel-007` | Princeton Tigers | Princeton Cougars | Princeton region (align to real) |
| `team-conf-fel-008` | Yale Bulldogs | Yale Mastiffs | Yale region (align to real) |

### Metro Atlantic Collegiate Alliance

*Real league: Metro Atlantic Athletic Conference (MAAC)* · `conf-maca`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-maca-001` | Canisius Golden Griffins | Canisius Golden Griffins | Canisius region (align to real) |
| `team-conf-maca-002` | Fairfield Stags | Fairfield Bucks | Fairfield region (align to real) |
| `team-conf-maca-003` | Iona Gaels | Iona Highlanders | Iona region (align to real) |
| `team-conf-maca-004` | Manhattan Jaspers | Manhattan Quartz | Manhattan region (align to real) |
| `team-conf-maca-005` | Marist Red Foxes | Marist Red Vulpes | Marist region (align to real) |
| `team-conf-maca-006` | Merrimack Warriors | Merrimack Sentinels | Merrimack region (align to real) |
| `team-conf-maca-007` | Mount St. Mary's Mountaineers | Mount Saint Mary's Highlanders | Mount region (align to real) |
| `team-conf-maca-008` | Niagara Purple Eagles | Niagara Purple Hawks | Niagara region (align to real) |
| `team-conf-maca-009` | Quinnipiac Bobcats | Quinnipiac Lynxes | Quinnipiac region (align to real) |
| `team-conf-maca-010` | Rider Broncs | Rider Stallions | Rider region (align to real) |
| `team-conf-maca-011` | Sacred Heart Pioneers | Sacred Heart Trailblazers | Sacred region (align to real) |
| `team-conf-maca-012` | Saint Peter's Peacocks | Saint Peter's Fancies | Saint region (align to real) |
| `team-conf-maca-013` | Siena Saints | Siena Halos | Siena region (align to real) |

### Midland American Circuit

*Real league: Mid-American Conference (MAC)* · `conf-mac`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-mac-001` | Akron Zips | Akron Fasteners | Akron region (align to real) |
| `team-conf-mac-002` | Ball State Cardinals | Ball State Scarlets | Ball region (align to real) |
| `team-conf-mac-003` | Bowling Green Falcons | Bowling Green Raptors | Bowling region (align to real) |
| `team-conf-mac-004` | Buffalo Bulls | Buffalo Stampede | Buffalo region (align to real) |
| `team-conf-mac-005` | Central Michigan Chippewas | Central Michigan Chip Clans | Central region (align to real) |
| `team-conf-mac-006` | Eastern Michigan Eagles | Eastern Michigan Hawks | Eastern region (align to real) |
| `team-conf-mac-007` | Kent State Golden Flashes | Kent State Gold Bolts | Kent region (align to real) |
| `team-conf-mac-008` | Miami (OH) RedHawks | Miami Red Hawks | Miami region (align to real) |
| `team-conf-mac-009` | Ohio Bobcats | Ohio Lynxes | Ohio region (align to real) |
| `team-conf-mac-010` | Toledo Rockets | Toledo Rockets | Toledo region (align to real) |
| `team-conf-mac-011` | UMass Minutemen | Bay State Muskets | UMass region (align to real) |
| `team-conf-mac-012` | Western Michigan Broncos | Western Michigan Stallions | Western region (align to real) |

### Mid-Eastern Alliance Conference

*Real league: Mid-Eastern Athletic Conference (MEAC)* · `conf-meac`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-meac-001` | Coppin State Eagles | Coppin State Hawks | Coppin region (align to real) |
| `team-conf-meac-002` | Delaware State Hornets | Delaware State Stingers | Delaware region (align to real) |
| `team-conf-meac-003` | Howard Bison | Howard Buffalo | Howard region (align to real) |
| `team-conf-meac-004` | Maryland Eastern Shore Hawks (UMES) | Maryland Eastern Shore Falcons | Maryland region (align to real) |
| `team-conf-meac-005` | Morgan State Bears | Morgan State Bruins | Morgan region (align to real) |
| `team-conf-meac-006` | Norfolk State Spartans | Norfolk State Hoplites | Norfolk region (align to real) |
| `team-conf-meac-007` | North Carolina Central Eagles | North Carolina Central Hawks | North region (align to real) |
| `team-conf-meac-008` | South Carolina State Bulldogs | South Carolina State Mastiffs | South region (align to real) |

### Prairie Valley Conference

*Real league: Missouri Valley Conference (MVC)* · `conf-pvc`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-pvc-001` | Belmont Bruins | Belmont Bears | Belmont region (align to real) |
| `team-conf-pvc-002` | Bradley Braves | Bradley Warriors | Bradley region (align to real) |
| `team-conf-pvc-003` | Drake Bulldogs | Drake Mastiffs | Drake region (align to real) |
| `team-conf-pvc-004` | Evansville Purple Aces | Evansville Purple Cards | Evansville region (align to real) |
| `team-conf-pvc-005` | Illinois State Redbirds | Illinois State Cardinals | Illinois region (align to real) |
| `team-conf-pvc-006` | Indiana State Sycamores | Indiana State Trees | Indiana region (align to real) |
| `team-conf-pvc-007` | Murray State Racers | Murray State Sprinters | Murray region (align to real) |
| `team-conf-pvc-008` | Northern Iowa Panthers (UNI) | Northern Iowa Pumas | Northern region (align to real) |
| `team-conf-pvc-009` | Southern Illinois Salukis | Southern Illinois Hounds | Southern region (align to real) |
| `team-conf-pvc-010` | UIC Flames | UIC Blaze | UIC region (align to real) |
| `team-conf-pvc-011` | Valparaiso Beacons | Valparaiso Signals | Valparaiso region (align to real) |

### Rocky Mountain West Conference

*Real league: Mountain West Conference (MW)* · `conf-rmwc`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-rmwc-001` | Air Force Falcons | Air Force Raptors | Air region (align to real) |
| `team-conf-rmwc-002` | Grand Canyon Antelopes | Grand Canyon Antelopes | Grand region (align to real) |
| `team-conf-rmwc-003` | Hawaiʻi Rainbow Warriors | Honolulu Rainbow Guard | Honolulu, HI |
| `team-conf-rmwc-004` | Nevada Wolf Pack | Nevada Timberwolves | Nevada region (align to real) |
| `team-conf-rmwc-005` | New Mexico Lobos | New Mexico Wolves | New region (align to real) |
| `team-conf-rmwc-006` | San José State Spartans | San José State Hoplites | San region (align to real) |
| `team-conf-rmwc-007` | UNLV Runnin' Rebels | UNLV Running Renegades | UNLV region (align to real) |
| `team-conf-rmwc-008` | UTEP Miners | UTEP Diggers | UTEP region (align to real) |
| `team-conf-rmwc-009` | Wyoming Cowboys | Wyoming Wranglers | Wyoming region (align to real) |

### Northeastern Exchange Conference

*Real league: Northeast Conference (NEC)* · `conf-nec`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-nec-001` | Central Connecticut Blue Devils | Central Connecticut Azure Vipers | Central region (align to real) |
| `team-conf-nec-002` | Chicago State Cougars | Chicago State Pumas | Chicago region (align to real) |
| `team-conf-nec-003` | Fairleigh Dickinson Knights (FDU) | Fairleigh Dickinson Squires | Fairleigh region (align to real) |
| `team-conf-nec-004` | Le Moyne Dolphins | Le Moyne Mariners | Le region (align to real) |
| `team-conf-nec-005` | LIU Sharks | LIU Sharks | LIU region (align to real) |
| `team-conf-nec-006` | Mercyhurst Lakers | Mercyhurst Lakers | Mercyhurst region (align to real) |
| `team-conf-nec-007` | New Haven Chargers | New Haven Chargers | New region (align to real) |
| `team-conf-nec-008` | Saint Francis (PA) Red Flash | Saint Francis Red Burst | Saint region (align to real) |
| `team-conf-nec-009` | Stonehill Skyhawks | Stonehill Cloud Hawks | Stonehill region (align to real) |
| `team-conf-nec-010` | Wagner Seahawks | Wagner Sea Hawks | Wagner region (align to real) |

### Ohio Valley Alliance

*Real league: Ohio Valley Conference (OVC)* · `conf-ova`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-ova-001` | Eastern Illinois Panthers | Eastern Illinois Pumas | Eastern region (align to real) |
| `team-conf-ova-002` | Lindenwood Lions | Lindenwood Monarchs | Lindenwood region (align to real) |
| `team-conf-ova-003` | Morehead State Eagles | Morehead State Hawks | Morehead region (align to real) |
| `team-conf-ova-004` | SIU Edwardsville Cougars (SIUE) | SIU Edwardsville Pumas | SIU region (align to real) |
| `team-conf-ova-005` | Southeast Missouri State Redhawks (SEMO) | Southeast Missouri State Crimson Hawks | Southeast region (align to real) |
| `team-conf-ova-006` | Southern Indiana Screaming Eagles | Southern Indiana Screaming Hawks | Southern region (align to real) |
| `team-conf-ova-007` | Tennessee State Tigers | Tennessee State Cougars | Tennessee region (align to real) |
| `team-conf-ova-008` | UT Martin Skyhawks | UT Martin Cloud Hawks | UT region (align to real) |
| `team-conf-ova-009` | Western Illinois Leathernecks | Western Illinois Leather Guards | Western region (align to real) |

### Pacific Rim Twelve

*Real league: Pac-12 Conference* · `conf-pr12`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-pr12-001` | Boise State Broncos | Boise State Stallions | Boise region (align to real) |
| `team-conf-pr12-002` | Colorado State Rams | Colorado State Bighorns | Colorado region (align to real) |
| `team-conf-pr12-003` | Fresno State Bulldogs | Fresno State Mastiffs | Fresno region (align to real) |
| `team-conf-pr12-004` | Gonzaga Bulldogs | Spokane River Mastiffs | Spokane, WA |
| `team-conf-pr12-005` | Oregon State Beavers | Oregon State Carpenters | Oregon region (align to real) |
| `team-conf-pr12-006` | San Diego State Aztecs | San Diego State Aztecs | San region (align to real) |
| `team-conf-pr12-007` | Texas State Bobcats | Texas State Lynxes | Austin, TX |
| `team-conf-pr12-008` | Utah State Aggies | Utah State Rustics | Utah region (align to real) |
| `team-conf-pr12-009` | Washington State Cougars | Washington State Pumas | Washington region (align to real) |

### Patriot Scholar League

*Real league: Patriot League* · `conf-psl`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-psl-001` | American Eagles | American Hawks | American region (align to real) |
| `team-conf-psl-002` | Army Black Knights | Army Black Squires | Army region (align to real) |
| `team-conf-psl-003` | Boston University Terriers | Boston University Hounds | Boston region (align to real) |
| `team-conf-psl-004` | Bucknell Bison | Bucknell Buffalo | Bucknell region (align to real) |
| `team-conf-psl-005` | Colgate Raiders | Colgate Rangers | Colgate region (align to real) |
| `team-conf-psl-006` | Holy Cross Crusaders | Holy Cross Crusaders | Holy region (align to real) |
| `team-conf-psl-007` | Lafayette Leopards | Lafayette Cats | Lafayette region (align to real) |
| `team-conf-psl-008` | Lehigh Mountain Hawks | Lehigh Peak Hawks | Lehigh region (align to real) |
| `team-conf-psl-009` | Loyola Maryland Greyhounds | Loyola Maryland Hounds | Loyola region (align to real) |
| `team-conf-psl-010` | Navy Midshipmen | Navy Sailors | Navy region (align to real) |

### Southeastern Dominion Conference

*Real league: Southeastern Conference (SEC)* · `conf-sedc`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-sedc-001` | Alabama Crimson Tide | Alabama Crimson Surge | Alabama region (align to real) |
| `team-conf-sedc-002` | Arkansas Razorbacks | Arkansas Backwoods Boars | Arkansas region (align to real) |
| `team-conf-sedc-003` | Auburn Tigers | Auburn Cougars | Auburn region (align to real) |
| `team-conf-sedc-004` | Florida Gators | Florida Gators | Florida region (align to real) |
| `team-conf-sedc-005` | Georgia Bulldogs | Georgia Mastiffs | Georgia region (align to real) |
| `team-conf-sedc-006` | Kentucky Wildcats | Bluegrass Commonwealth Lynx | Lexington, KY |
| `team-conf-sedc-007` | LSU Tigers | LSU Cougars | LSU region (align to real) |
| `team-conf-sedc-008` | Mississippi State Bulldogs | Mississippi State Mastiffs | Mississippi region (align to real) |
| `team-conf-sedc-009` | Missouri Tigers | Missouri Cougars | Missouri region (align to real) |
| `team-conf-sedc-010` | Oklahoma Sooners | Oklahoma Sooners | Oklahoma region (align to real) |
| `team-conf-sedc-011` | Ole Miss Rebels | Ole Miss Renegades | Ole region (align to real) |
| `team-conf-sedc-012` | South Carolina Gamecocks | South Carolina Strutters | South region (align to real) |
| `team-conf-sedc-013` | Tennessee Volunteers | Tennessee Regiments | Tennessee region (align to real) |
| `team-conf-sedc-014` | Texas Longhorns | Austin Metro Longhorns | Austin, TX |
| `team-conf-sedc-015` | Texas A&M Aggies | Texas AandM Rustics | Austin, TX |
| `team-conf-sedc-016` | Vanderbilt Commodores | Vanderbilt Captains | Vanderbilt region (align to real) |

### Southern Collegiate Federation

*Real league: Southern Conference (SoCon)* · `conf-scf`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-scf-001` | Chattanooga Mocs | Chattanooga Chattans | Chattanooga region (align to real) |
| `team-conf-scf-002` | East Tennessee State Buccaneers (ETSU) | East Tennessee State Buccaneers | East region (align to real) |
| `team-conf-scf-003` | Furman Paladins | Furman Defenders | Furman region (align to real) |
| `team-conf-scf-004` | Mercer Bears | Mercer Bruins | Mercer region (align to real) |
| `team-conf-scf-005` | Samford Bulldogs | Samford Mastiffs | Samford region (align to real) |
| `team-conf-scf-006` | The Citadel Bulldogs | The Citadel Mastiffs | The region (align to real) |
| `team-conf-scf-007` | Tennessee Tech Golden Eagles | Tennessee Tech Golden Hawks | Tennessee region (align to real) |
| `team-conf-scf-008` | UNC Greensboro Spartans (UNCG) | North Cape Greensboro Hoplites | UNC region (align to real) |
| `team-conf-scf-009` | VMI Keydets | VMI Cadets | VMI region (align to real) |
| `team-conf-scf-010` | Western Carolina Catamounts | Western Carolina Mountain Cats | Western region (align to real) |
| `team-conf-scf-011` | Wofford Terriers | Wofford Hounds | Wofford region (align to real) |

### Gulf Coastland Conference

*Real league: Southland Conference* · `conf-gcc`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-gcc-001` | East Texas A&M Lions (formerly Texas A&M–Commerce) | East Texas AandM Monarchs | East region (align to real) |
| `team-conf-gcc-002` | Houston Christian Huskies | Houston Christian Sled Dogs | Houston region (align to real) |
| `team-conf-gcc-003` | Incarnate Word Cardinals (UIW) | Incarnate Word Scarlets | Incarnate region (align to real) |
| `team-conf-gcc-004` | Lamar Cardinals | Lamar Scarlets | Lamar region (align to real) |
| `team-conf-gcc-005` | New Orleans Privateers | New Orleans Freebooters | New region (align to real) |
| `team-conf-gcc-006` | Nicholls Colonels | Nicholls Officers | Nicholls region (align to real) |
| `team-conf-gcc-007` | Northwestern State Demons | Northwestern State Imps | Northwestern region (align to real) |
| `team-conf-gcc-008` | McNeese Cowboys | McNeese Wranglers | McNeese region (align to real) |
| `team-conf-gcc-009` | Southeastern Louisiana Lions | Southeastern Louisiana Monarchs | Southeastern region (align to real) |
| `team-conf-gcc-010` | Stephen F. Austin Lumberjacks (SFA) | Stephen F. Austin Axemen | Stephen region (align to real) |
| `team-conf-gcc-011` | Texas A&M-Corpus Christi Islanders | Texas AandM-Corpus Christi Isles | Austin, TX |
| `team-conf-gcc-012` | UT Rio Grande Valley Vaqueros (UTRGV) | UT Rio Grande Valley Ranch Hands | UT region (align to real) |

### Southwest Collegiate Athletic League

*Real league: Southwestern Athletic Conference (SWAC)* · `conf-scal`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-scal-001` | Alabama A&M Bulldogs | Alabama AandM Mastiffs | Alabama region (align to real) |
| `team-conf-scal-002` | Alabama State Hornets | Alabama State Stingers | Alabama region (align to real) |
| `team-conf-scal-003` | Alcorn State Braves | Alcorn State Warriors | Alcorn region (align to real) |
| `team-conf-scal-004` | Arkansas-Pine Bluff Golden Lions (UAPB) | Arkansas-Pine Bluff Golden Monarchs | Arkansas-Pine region (align to real) |
| `team-conf-scal-005` | Bethune-Cookman Wildcats | Bethune-Cookman Lynx | Bethune-Cookman region (align to real) |
| `team-conf-scal-006` | Florida A&M Rattlers (FAMU) | Florida AandM Buzztails | Florida region (align to real) |
| `team-conf-scal-007` | Grambling State Tigers | Grambling State Cougars | Grambling region (align to real) |
| `team-conf-scal-008` | Jackson State Tigers | Jackson State Cougars | Jackson region (align to real) |
| `team-conf-scal-009` | Mississippi Valley State Delta Devils | Mississippi Valley State Delta Fiends | Mississippi region (align to real) |
| `team-conf-scal-010` | Prairie View A&M Panthers | Prairie View AandM Pumas | Prairie region (align to real) |
| `team-conf-scal-011` | Southern Jaguars | Southern Panthers | Southern region (align to real) |
| `team-conf-scal-012` | Texas Southern Tigers | Texas Southern Cougars | Austin, TX |

### Prairie Summit League

*Real league: Summit League* · `conf-summit`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-summit-001` | Kansas City Roos | Kansas City Roos | Lawrence, KS |
| `team-conf-summit-002` | North Dakota Fighting Hawks | North Dakota Fighting Kites | North region (align to real) |
| `team-conf-summit-003` | North Dakota State Bison | North Dakota State Buffalo | North region (align to real) |
| `team-conf-summit-004` | Omaha Mavericks | Omaha Mavericks | Omaha region (align to real) |
| `team-conf-summit-005` | Oral Roberts Golden Eagles | Oral Roberts Golden Hawks | Oral region (align to real) |
| `team-conf-summit-006` | South Dakota Coyotes | South Dakota Coyotes | South region (align to real) |
| `team-conf-summit-007` | South Dakota State Jackrabbits | South Dakota State Hares | South region (align to real) |
| `team-conf-summit-008` | St. Thomas Tommies | Saint Thomas Saints | St. region (align to real) |

### Solar Belt Conference

*Real league: Sun Belt Conference* · `conf-sbc`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-sbc-001` | Appalachian State Mountaineers | Appalachian State Highlanders | Appalachian region (align to real) |
| `team-conf-sbc-002` | Arkansas State Red Wolves | Arkansas State Crimson Wolves | Arkansas region (align to real) |
| `team-conf-sbc-003` | Coastal Carolina Chanticleers | Coastal Carolina Rooster | Coastal region (align to real) |
| `team-conf-sbc-004` | Georgia Southern Eagles | Georgia Southern Hawks | Georgia region (align to real) |
| `team-conf-sbc-005` | Georgia State Panthers | Georgia State Pumas | Georgia region (align to real) |
| `team-conf-sbc-006` | James Madison Dukes (JMU) | James Madison Barons | James region (align to real) |
| `team-conf-sbc-007` | Louisiana Ragin' Cajuns | Louisiana Ragin' Roux | Louisiana region (align to real) |
| `team-conf-sbc-008` | Louisiana-Monroe Warhawks (ULM) | Louisiana-Monroe Warbirds | Louisiana-Monroe region (align to real) |
| `team-conf-sbc-009` | Louisiana Tech Bulldogs | Louisiana Tech Mastiffs | Louisiana region (align to real) |
| `team-conf-sbc-010` | Marshall Thundering Herd | Marshall Stampede | Marshall region (align to real) |
| `team-conf-sbc-011` | Old Dominion Monarchs (ODU) | Old Dominion Crown | Old region (align to real) |
| `team-conf-sbc-012` | South Alabama Jaguars | South Alabama Panthers | South region (align to real) |
| `team-conf-sbc-013` | Southern Miss Golden Eagles | Southern Miss Golden Hawks | Southern region (align to real) |
| `team-conf-sbc-014` | Troy Trojans | Troy Argonauts | Troy region (align to real) |

### Western Coast Collegiate

*Real league: West Coast Conference (WCC)* · `conf-wcc`

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-wcc-001` | Denver Pioneers | Denver Trailblazers | Denver region (align to real) |
| `team-conf-wcc-002` | Loyola Marymount Lions (LMU) | Loyola Marymount Monarchs | Loyola region (align to real) |
| `team-conf-wcc-003` | Pacific Tigers | Pacific Cougars | Pacific region (align to real) |
| `team-conf-wcc-004` | Pepperdine Waves | Pepperdine Waves | Pepperdine region (align to real) |
| `team-conf-wcc-005` | Portland Pilots | Portland Pilots | Portland region (align to real) |
| `team-conf-wcc-006` | Saint Mary's Gaels | Saint Mary's Highlanders | Saint region (align to real) |
| `team-conf-wcc-007` | San Diego Toreros | San Diego Bullfighters | San region (align to real) |
| `team-conf-wcc-008` | San Francisco Dons | San Francisco Lords | San region (align to real) |
| `team-conf-wcc-009` | Santa Clara Broncos | Santa Clara Stallions | Santa region (align to real) |
| `team-conf-wcc-010` | Seattle U Redhawks | Seattle U Crimson Hawks | Seattle region (align to real) |

---

## National body & tournaments (TBD)

| Real (reference) | Universe (planned) | Notes |
|-------------------|-------------------|--------|
| NCAA | **National Collegiate Basketball Association (NCBA)** or **Dynasty Collegiate Association (DCA)** | Final label TBD |
| NCAA tournament / March Madness | **National Championship** / **March Classic** | Bracket structure per game-design |
| NIT | **Secondary National Invitational** | Tie to coach-card secondary postseason |

---

## Pack authoring notes

| Rule | Detail |
|------|--------|
| **IDs** | Simulation uses `confId` / `teamId` only; display strings from pack overlay |
| **Colors & logos** | Map from real reference in pack JSON; universe name does not block hex fidelity |
| **This doc** | Source of truth for **default pack** naming; optional import packs may swap display only |
| **Regeneration** | Re-run `planning/tools/generate_league_branding.py` after membership edits; hand-tune flagships in overrides (future) |

**Membership count (this file):** 356 teams · 31 conferences.

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Initial conference + team mapping from 2026–27 D-I membership list |
