# League branding — conferences & teams (reference mapping)

**Product:** [Dynasty: College Basketball](branding.md) · default **universe** names for identity packs.
**Membership:** **Active 2026–27** NCAA Division I men's basketball — **32 conferences**. This table enumerates **363** programs from the reference list below; headline **365** — reconcile **2** slots on next alignment pass (verify omissions vs. NCAA directory).
**Purpose:** Keep **real-world reference** alongside **ship-safe display** names so schedules, geography, colors, and tournament fidelity stay accurate.
**Related:** [`game-design.md`](game-design.md#identity-packs--save-model), [`branding.md`](branding.md).
**Last updated:** 2025-11-08
**Status:** v2 membership rebuild; universe names from generator + flagship overrides — polish colors/cities in pack JSON.

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
| 16 | Metro Conference (formerly MAAC) | **Metro Collegiate Conference** | `conf-metro` | METRO |
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
| 31 | United Athletic Conference (UAC) (formerly WAC) | **United Athletic Collegiate** | `conf-uac` | UAC |
| 32 | West Coast Conference (WCC) | **Western Coast Collegiate** | `conf-wcc` | WCC |

---

## Teams by conference

Columns: stable `teamId`, real program (editor reference), universe display name, geography anchor for pack data (city / colors — **flesh out in JSON**).

### Northern Atlantic Collegiate Conference

*Real league: America East Conference* · `conf-nac` · **9 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-nac-001` | Albany Great Danes | Albany Metro Great Danes | Albany, NY |
| `team-conf-nac-002` | Binghamton Bearcats | Binghamton Bearcats | Binghamton region — align city/market in pack JSON |
| `team-conf-nac-003` | Bryant Bulldogs | Bryant Mastiffs | Bryant region — align city/market in pack JSON |
| `team-conf-nac-004` | Maine Black Bears | Maine Black Bears | Maine region — align city/market in pack JSON |
| `team-conf-nac-005` | New Hampshire Wildcats | New Hampshire Lynx | New region — align city/market in pack JSON |
| `team-conf-nac-006` | NJIT Highlanders | Newark Tech Highlanders | NJIT region — align city/market in pack JSON |
| `team-conf-nac-007` | UMBC Retrievers | UMBC Retrievers | UMBC region — align city/market in pack JSON |
| `team-conf-nac-008` | UMass Lowell River Hawks | Bay State Lowell River Hawks | UMass region — align city/market in pack JSON |
| `team-conf-nac-009` | Vermont Catamounts | Vermont Catamounts | Vermont region — align city/market in pack JSON |

### American Unified Collegiate Conference

*Real league: American Athletic Conference (AAC)* · `conf-aucc` · **13 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-aucc-001` | Charlotte 49ers | Charlotte Metro 49ers | Charlotte, NC |
| `team-conf-aucc-002` | East Carolina Pirates | East Carolina Pirates | East region — align city/market in pack JSON |
| `team-conf-aucc-003` | Florida Atlantic Owls (FAU) | Florida Atlantic Owls | Florida region — align city/market in pack JSON |
| `team-conf-aucc-004` | Memphis Tigers | Memphis Metro Cougars | Memphis, TN |
| `team-conf-aucc-005` | North Texas Mean Green | North Texas Mean Green | North region — align city/market in pack JSON |
| `team-conf-aucc-006` | Rice Owls | Rice Owls | Rice region — align city/market in pack JSON |
| `team-conf-aucc-007` | South Florida Bulls (USF) | South Florida Stampede | South region — align city/market in pack JSON |
| `team-conf-aucc-008` | Temple Owls | Temple Owls | Temple region — align city/market in pack JSON |
| `team-conf-aucc-009` | Tulane Green Wave | Tulane Green Wave | Tulane region — align city/market in pack JSON |
| `team-conf-aucc-010` | Tulsa Golden Hurricane | Tulsa Golden Hurricane | Tulsa region — align city/market in pack JSON |
| `team-conf-aucc-011` | UAB Blazers | Birmingham Metro Blazers | UAB region — align city/market in pack JSON |
| `team-conf-aucc-012` | UTSA Roadrunners | South Alamo Roadrunners | UTSA region — align city/market in pack JSON |
| `team-conf-aucc-013` | Wichita State Shockers | Wichita State Shockers | Wichita region — align city/market in pack JSON |

### Colonial Atlantic Ten

*Real league: Atlantic 10 Conference (A-10)* · `conf-cat10` · **14 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-cat10-001` | Davidson Wildcats | Davidson Lynx | Davidson region — align city/market in pack JSON |
| `team-conf-cat10-002` | Dayton Flyers | Dayton Flyers | Dayton region — align city/market in pack JSON |
| `team-conf-cat10-003` | Duquesne Dukes | Duquesne Dukes | Duquesne region — align city/market in pack JSON |
| `team-conf-cat10-004` | Fordham Rams | Fordham Rams | Fordham region — align city/market in pack JSON |
| `team-conf-cat10-005` | George Mason Patriots | George Mason Patriots | George region — align city/market in pack JSON |
| `team-conf-cat10-006` | George Washington Revolutionaries | George Washington Revolutionaries | George region — align city/market in pack JSON |
| `team-conf-cat10-007` | La Salle Explorers | La Salle Explorers | La region — align city/market in pack JSON |
| `team-conf-cat10-008` | Loyola Chicago Ramblers | Loyola Chicago Ramblers | Loyola region — align city/market in pack JSON |
| `team-conf-cat10-009` | Rhode Island Rams | Rhode Island Rams | Rhode region — align city/market in pack JSON |
| `team-conf-cat10-010` | Richmond Spiders | Richmond Spiders | Richmond region — align city/market in pack JSON |
| `team-conf-cat10-011` | Saint Joseph's Hawks | Saint Joseph's Falcons | Saint region — align city/market in pack JSON |
| `team-conf-cat10-012` | Saint Louis Billikens | Saint Louis Billikens | Saint region — align city/market in pack JSON |
| `team-conf-cat10-013` | St. Bonaventure Bonnies | Saint Bonaventure Bonnies | St. region — align city/market in pack JSON |
| `team-conf-cat10-014` | VCU Rams | VCU Rams | VCU region — align city/market in pack JSON |

### Atlantic Crown Conference

*Real league: Atlantic Coast Conference (ACC)* · `conf-acrown` · **18 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-acrown-001` | Boston College Eagles | Boston College Hawks | Boston region — align city/market in pack JSON |
| `team-conf-acrown-002` | California Golden Bears (Cal) | Bay Golden Bruins | Berkeley, CA |
| `team-conf-acrown-003` | Clemson Tigers | Clemson Cougars | Clemson region — align city/market in pack JSON |
| `team-conf-acrown-004` | Duke Blue Devils | Crown University Sovereigns | Durham, NC |
| `team-conf-acrown-005` | Florida State Seminoles | Florida State Seminoles | Florida region — align city/market in pack JSON |
| `team-conf-acrown-006` | Georgia Tech Yellow Jackets | Georgia Tech Yellow Jackets | Georgia region — align city/market in pack JSON |
| `team-conf-acrown-007` | Louisville Cardinals | Louisville Scarlets | Louisville region — align city/market in pack JSON |
| `team-conf-acrown-008` | Miami Hurricanes | Miami Hurricanes | Miami region — align city/market in pack JSON |
| `team-conf-acrown-009` | NC State Wolfpack | NC State Wolfpack | NC region — align city/market in pack JSON |
| `team-conf-acrown-010` | North Carolina Tar Heels | Tar River Commonwealth | Chapel Hill, NC |
| `team-conf-acrown-011` | Notre Dame Fighting Irish | Notre Dame Metro Fighting Irish | Notre Dame, IN |
| `team-conf-acrown-012` | Pittsburgh Panthers | Pittsburgh Pumas | Pittsburgh region — align city/market in pack JSON |
| `team-conf-acrown-013` | SMU Mustangs | Dallas Metro Mustangs | SMU region — align city/market in pack JSON |
| `team-conf-acrown-014` | Stanford Cardinal | Stanford Cardinal | Stanford region — align city/market in pack JSON |
| `team-conf-acrown-015` | Syracuse Orange | Syracuse Orange | Syracuse region — align city/market in pack JSON |
| `team-conf-acrown-016` | Virginia Cavaliers | Virginia Cavaliers | Virginia region — align city/market in pack JSON |
| `team-conf-acrown-017` | Virginia Tech Hokies | Virginia Tech Hokies | Virginia region — align city/market in pack JSON |
| `team-conf-acrown-018` | Wake Forest Demon Deacons | Wake Forest Demon Deacons | Wake region — align city/market in pack JSON |

### Atlantic Sun Collegiate Union

*Real league: Atlantic Sun Conference (ASUN)* · `conf-ascu` · **8 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-ascu-001` | Bellarmine Knights | Bellarmine Knights | Bellarmine region — align city/market in pack JSON |
| `team-conf-ascu-002` | Florida Gulf Coast Eagles (FGCU) | Florida Gulf Coast Hawks | Florida region — align city/market in pack JSON |
| `team-conf-ascu-003` | Jacksonville Dolphins | Jacksonville Dolphins | Jacksonville region — align city/market in pack JSON |
| `team-conf-ascu-004` | Lipscomb Bisons | Lipscomb Bisons | Lipscomb region — align city/market in pack JSON |
| `team-conf-ascu-005` | North Florida Ospreys | North Florida Ospreys | North region — align city/market in pack JSON |
| `team-conf-ascu-006` | Queens Royals | Queens Royals | Queens region — align city/market in pack JSON |
| `team-conf-ascu-007` | Stetson Hatters | Stetson Hatters | Stetson region — align city/market in pack JSON |
| `team-conf-ascu-008` | West Florida Argonauts (D-II transition) | West Florida Argonauts | West region — align city/market in pack JSON |

### Central Twelve Conference

*Real league: Big 12 Conference* · `conf-c12` · **16 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-c12-001` | Arizona Wildcats | Arizona Lynx | Arizona region — align city/market in pack JSON |
| `team-conf-c12-002` | Arizona State Sun Devils | Arizona State Sun Devils | Arizona region — align city/market in pack JSON |
| `team-conf-c12-003` | Baylor Bears | Baylor Bruins | Baylor region — align city/market in pack JSON |
| `team-conf-c12-004` | BYU Cougars | Provo Metro Cougars | BYU region — align city/market in pack JSON |
| `team-conf-c12-005` | Cincinnati Bearcats | Cincinnati Bearcats | Cincinnati region — align city/market in pack JSON |
| `team-conf-c12-006` | Colorado Buffaloes | Colorado Buffaloes | Colorado region — align city/market in pack JSON |
| `team-conf-c12-007` | Houston Cougars | Houston Cougars | Houston region — align city/market in pack JSON |
| `team-conf-c12-008` | Iowa State Cyclones | Iowa State Cyclones | Iowa region — align city/market in pack JSON |
| `team-conf-c12-009` | Kansas Jayhawks | Jaydale Commonwealth | Lawrence, KS |
| `team-conf-c12-010` | Kansas State Wildcats | Kansas State Lynx | Lawrence, KS |
| `team-conf-c12-011` | Oklahoma State Cowboys | Oklahoma State Cowboys | Oklahoma region — align city/market in pack JSON |
| `team-conf-c12-012` | TCU Horned Frogs | TCU Horned Frogs | TCU region — align city/market in pack JSON |
| `team-conf-c12-013` | Texas Tech Red Raiders | Texas Tech Red Raiders | Austin, TX |
| `team-conf-c12-014` | UCF Knights | Central Florida Metro Knights | UCF region — align city/market in pack JSON |
| `team-conf-c12-015` | Utah Utes | Utah Utes | Utah region — align city/market in pack JSON |
| `team-conf-c12-016` | West Virginia Mountaineers | West Virginia Highlanders | West region — align city/market in pack JSON |

### Eastern Premier Conference

*Real league: Big East Conference* · `conf-epc` · **11 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-epc-001` | Butler Bulldogs | Butler Mastiffs | Butler region — align city/market in pack JSON |
| `team-conf-epc-002` | Creighton Bluejays | Creighton Bluejays | Creighton region — align city/market in pack JSON |
| `team-conf-epc-003` | DePaul Blue Demons | DePaul Blue Demons | DePaul region — align city/market in pack JSON |
| `team-conf-epc-004` | Georgetown Hoyas | Georgetown Hoyas | Georgetown region — align city/market in pack JSON |
| `team-conf-epc-005` | Marquette Golden Eagles | Marquette Golden Eagles | Marquette region — align city/market in pack JSON |
| `team-conf-epc-006` | Providence Friars | Providence Friars | Providence region — align city/market in pack JSON |
| `team-conf-epc-007` | Seton Hall Pirates | Seton Hall Pirates | Seton region — align city/market in pack JSON |
| `team-conf-epc-008` | St. John's Red Storm | Saint John's Red Storm | St. region — align city/market in pack JSON |
| `team-conf-epc-009` | UConn Huskies | Storrs Sled Dogs | Storrs, CT |
| `team-conf-epc-010` | Villanova Wildcats | Main Line Lynx | Villanova, PA |
| `team-conf-epc-011` | Xavier Musketeers | Xavier Musketeers | Xavier region — align city/market in pack JSON |

### Northern Sky Conference

*Real league: Big Sky Conference* · `conf-nsc` · **11 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-nsc-001` | Eastern Washington Eagles | Eastern Washington Hawks | Eastern region — align city/market in pack JSON |
| `team-conf-nsc-002` | Idaho Vandals | Idaho Vandals | Idaho region — align city/market in pack JSON |
| `team-conf-nsc-003` | Idaho State Bengals | Idaho State Bengals | Idaho region — align city/market in pack JSON |
| `team-conf-nsc-004` | Montana Grizzlies | Montana Grizzlies | Montana region — align city/market in pack JSON |
| `team-conf-nsc-005` | Montana State Bobcats | Montana State Bobcats | Montana region — align city/market in pack JSON |
| `team-conf-nsc-006` | Northern Arizona Lumberjacks | Northern Arizona Lumberjacks | Northern region — align city/market in pack JSON |
| `team-conf-nsc-007` | Northern Colorado Bears | Northern Colorado Bruins | Northern region — align city/market in pack JSON |
| `team-conf-nsc-008` | Portland State Vikings | Portland State Vikings | Portland region — align city/market in pack JSON |
| `team-conf-nsc-009` | Southern Utah Thunderbirds | Southern Utah Thunderbirds | Southern region — align city/market in pack JSON |
| `team-conf-nsc-010` | Utah Tech Trailblazers | Utah Tech Trailblazers | Utah region — align city/market in pack JSON |
| `team-conf-nsc-011` | Weber State Wildcats | Weber State Lynx | Weber region — align city/market in pack JSON |

### Piedmont South Conference

*Real league: Big South Conference* · `conf-psc` · **9 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-psc-001` | Charleston Southern Buccaneers | Charleston Southern Privateers | Charleston region — align city/market in pack JSON |
| `team-conf-psc-002` | Gardner-Webb Runnin' Bulldogs | Gardner-Webb Runnin' Bulldogs | Gardner-Webb region — align city/market in pack JSON |
| `team-conf-psc-003` | High Point Panthers | High Point Pumas | High region — align city/market in pack JSON |
| `team-conf-psc-004` | Longwood Lancers | Longwood Lancers | Longwood region — align city/market in pack JSON |
| `team-conf-psc-005` | Presbyterian Blue Hose | Presbyterian Blue Hose | Presbyterian region — align city/market in pack JSON |
| `team-conf-psc-006` | Radford Highlanders | Radford Highlanders | Radford region — align city/market in pack JSON |
| `team-conf-psc-007` | UNC Asheville Bulldogs | North Cape Asheville Mastiffs | UNC region — align city/market in pack JSON |
| `team-conf-psc-008` | USC Upstate Spartans | Southern California Metro Upstate Hoplites | USC region — align city/market in pack JSON |
| `team-conf-psc-009` | Winthrop Eagles | Winthrop Hawks | Winthrop region — align city/market in pack JSON |

### Great Ten Conference

*Real league: Big Ten Conference* · `conf-g10` · **18 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-g10-001` | Illinois Fighting Illini | Illinois Fighting Illini | Illinois region — align city/market in pack JSON |
| `team-conf-g10-002` | Indiana Hoosiers | Indiana Hoosiers | Indiana region — align city/market in pack JSON |
| `team-conf-g10-003` | Iowa Hawkeyes | Iowa Hawkeyes | Iowa region — align city/market in pack JSON |
| `team-conf-g10-004` | Maryland Terrapins | Maryland Terrapins | Maryland region — align city/market in pack JSON |
| `team-conf-g10-005` | Michigan Wolverines | Michigan Wolverines | Michigan region — align city/market in pack JSON |
| `team-conf-g10-006` | Michigan State Spartans | Michigan State Hoplites | Michigan region — align city/market in pack JSON |
| `team-conf-g10-007` | Minnesota Golden Gophers | Minnesota Golden Gophers | Minnesota region — align city/market in pack JSON |
| `team-conf-g10-008` | Nebraska Cornhuskers | Nebraska Cornhuskers | Nebraska region — align city/market in pack JSON |
| `team-conf-g10-009` | Northwestern Wildcats | Northwestern Lynx | Northwestern region — align city/market in pack JSON |
| `team-conf-g10-010` | Ohio State Buckeyes | Ohio State Buckeyes | Ohio region — align city/market in pack JSON |
| `team-conf-g10-011` | Oregon Ducks | Oregon Ducks | Oregon region — align city/market in pack JSON |
| `team-conf-g10-012` | Penn State Nittany Lions | Penn State Nittany Lions | Penn region — align city/market in pack JSON |
| `team-conf-g10-013` | Purdue Boilermakers | Purdue Boilermakers | Purdue region — align city/market in pack JSON |
| `team-conf-g10-014` | Rutgers Scarlet Knights | Rutgers Scarlet Knights | Rutgers region — align city/market in pack JSON |
| `team-conf-g10-015` | UCLA Bruins | Westwood Harbor Guardians | Los Angeles, CA |
| `team-conf-g10-016` | USC Trojans | South Bay Argonauts | Los Angeles, CA |
| `team-conf-g10-017` | Washington Huskies | Washington Huskies | Washington region — align city/market in pack JSON |
| `team-conf-g10-018` | Wisconsin Badgers | Wisconsin Badgers | Wisconsin region — align city/market in pack JSON |

### Golden West Conference

*Real league: Big West Conference* · `conf-gwc` · **11 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-gwc-001` | Cal Baptist Lancers | Cal Baptist Lancers | Cal region — align city/market in pack JSON |
| `team-conf-gwc-002` | Cal Poly Mustangs | Cal Poly Mustangs | Cal region — align city/market in pack JSON |
| `team-conf-gwc-003` | Cal State Bakersfield Roadrunners | Cal State Bakersfield Roadrunners | Cal region — align city/market in pack JSON |
| `team-conf-gwc-004` | Cal State Fullerton Titans | Cal State Fullerton Titans | Cal region — align city/market in pack JSON |
| `team-conf-gwc-005` | Cal State Northridge Matadors (CSUN) | Cal State Northridge Matadors | Cal region — align city/market in pack JSON |
| `team-conf-gwc-006` | Long Beach State Beach | Long Beach State Beach | Long region — align city/market in pack JSON |
| `team-conf-gwc-007` | UC Irvine Anteaters | UC Irvine Anteaters | UC region — align city/market in pack JSON |
| `team-conf-gwc-008` | UC Riverside Highlanders | UC Riverside Highlanders | UC region — align city/market in pack JSON |
| `team-conf-gwc-009` | UC San Diego Tritons | UC San Diego Tritons | UC region — align city/market in pack JSON |
| `team-conf-gwc-010` | UC Santa Barbara Gauchos | UC Santa Barbara Gauchos | UC region — align city/market in pack JSON |
| `team-conf-gwc-011` | Utah Valley Wolverines | Utah Valley Wolverines | Utah region — align city/market in pack JSON |

### Coastal Colonial Association

*Real league: Coastal Athletic Association (CAA)* · `conf-cca` · **13 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-cca-001` | Campbell Fighting Camels | Campbell Fighting Camels | Campbell region — align city/market in pack JSON |
| `team-conf-cca-002` | College of Charleston Cougars | College of Charleston Cougars | College region — align city/market in pack JSON |
| `team-conf-cca-003` | Drexel Dragons | Drexel Dragons | Drexel region — align city/market in pack JSON |
| `team-conf-cca-004` | Elon Phoenix | Elon Phoenix | Elon region — align city/market in pack JSON |
| `team-conf-cca-005` | Hampton Pirates | Hampton Pirates | Hampton region — align city/market in pack JSON |
| `team-conf-cca-006` | Hofstra Pride | Hofstra Pride | Hofstra region — align city/market in pack JSON |
| `team-conf-cca-007` | Monmouth Hawks | Monmouth Falcons | Monmouth region — align city/market in pack JSON |
| `team-conf-cca-008` | North Carolina A&T Aggies | North Carolina A&T Aggies | North region — align city/market in pack JSON |
| `team-conf-cca-009` | Northeastern Huskies | Northeastern Huskies | Northeastern region — align city/market in pack JSON |
| `team-conf-cca-010` | Stony Brook Seawolves | Stony Brook Seawolves | Stony region — align city/market in pack JSON |
| `team-conf-cca-011` | Towson Tigers | Towson Cougars | Towson region — align city/market in pack JSON |
| `team-conf-cca-012` | UNC Wilmington Seahawks (UNCW) | North Cape Wilmington Seahawks | UNC region — align city/market in pack JSON |
| `team-conf-cca-013` | William & Mary Tribe | William & Mary Tribe | William region — align city/market in pack JSON |

### United Continental Conference

*Real league: Conference USA (C-USA)* · `conf-ucc` · **10 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-ucc-001` | Delaware Fightin' Blue Hens | Delaware Fightin' Blue Hens | Delaware region — align city/market in pack JSON |
| `team-conf-ucc-002` | FIU Panthers | Miami-Dade Metro Pumas | FIU region — align city/market in pack JSON |
| `team-conf-ucc-003` | Jacksonville State Gamecocks | Jacksonville State Gamecocks | Jacksonville region — align city/market in pack JSON |
| `team-conf-ucc-004` | Kennesaw State Owls | Kennesaw State Owls | Kennesaw region — align city/market in pack JSON |
| `team-conf-ucc-005` | Liberty Flames | Liberty Flames | Liberty region — align city/market in pack JSON |
| `team-conf-ucc-006` | Middle Tennessee Blue Raiders (MTSU) | Middle Tennessee Blue Raiders | Middle region — align city/market in pack JSON |
| `team-conf-ucc-007` | Missouri State Bears | Missouri State Bruins | Missouri region — align city/market in pack JSON |
| `team-conf-ucc-008` | New Mexico State Aggies | New Mexico State Aggies | New region — align city/market in pack JSON |
| `team-conf-ucc-009` | Sam Houston Bearkats | Sam Houston Bearkats | Sam region — align city/market in pack JSON |
| `team-conf-ucc-010` | Western Kentucky Hilltoppers (WKU) | Western Kentucky Hilltoppers | Western region — align city/market in pack JSON |

### Lakefront Horizon League

*Real league: Horizon League* · `conf-lhl` · **12 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-lhl-001` | Cleveland State Vikings | Cleveland State Vikings | Cleveland region — align city/market in pack JSON |
| `team-conf-lhl-002` | Detroit Mercy Titans | Detroit Mercy Titans | Detroit region — align city/market in pack JSON |
| `team-conf-lhl-003` | Green Bay Phoenix | Green Bay Phoenix | Green region — align city/market in pack JSON |
| `team-conf-lhl-004` | IU Indy Jaguars | IU Indy Jaguars | IU region — align city/market in pack JSON |
| `team-conf-lhl-005` | Milwaukee Panthers | Milwaukee Pumas | Milwaukee region — align city/market in pack JSON |
| `team-conf-lhl-006` | Northern Illinois Huskies (NIU) | Northern Illinois Huskies | Northern region — align city/market in pack JSON |
| `team-conf-lhl-007` | Northern Kentucky Norse | Northern Kentucky Norse | Northern region — align city/market in pack JSON |
| `team-conf-lhl-008` | Oakland Golden Grizzlies | Oakland Golden Grizzlies | Oakland region — align city/market in pack JSON |
| `team-conf-lhl-009` | Purdue Fort Wayne Mastodons | Purdue Fort Wayne Mastodons | Purdue region — align city/market in pack JSON |
| `team-conf-lhl-010` | Robert Morris Colonials | Robert Morris Colonials | Robert region — align city/market in pack JSON |
| `team-conf-lhl-011` | Wright State Raiders | Wright State Rangers | Wright region — align city/market in pack JSON |
| `team-conf-lhl-012` | Youngstown State Penguins | Youngstown State Penguins | Youngstown region — align city/market in pack JSON |

### Founding Eight League

*Real league: Ivy League* · `conf-fel` · **8 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-fel-001` | Brown Bears | Brown Bruins | Brown region — align city/market in pack JSON |
| `team-conf-fel-002` | Columbia Lions | Columbia Monarchs | Columbia region — align city/market in pack JSON |
| `team-conf-fel-003` | Cornell Big Red | Cornell Big Red | Cornell region — align city/market in pack JSON |
| `team-conf-fel-004` | Dartmouth Big Green | Dartmouth Big Green | Dartmouth region — align city/market in pack JSON |
| `team-conf-fel-005` | Harvard Crimson | Harvard Crimson | Harvard region — align city/market in pack JSON |
| `team-conf-fel-006` | Penn Quakers | Penn Quakers | Penn region — align city/market in pack JSON |
| `team-conf-fel-007` | Princeton Tigers | Princeton Cougars | Princeton region — align city/market in pack JSON |
| `team-conf-fel-008` | Yale Bulldogs | Yale Mastiffs | Yale region — align city/market in pack JSON |

### Metro Collegiate Conference

*Real league: Metro Conference (formerly MAAC)* · `conf-metro` · **13 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-metro-001` | Canisius Golden Griffins | Canisius Golden Griffins | Canisius region — align city/market in pack JSON |
| `team-conf-metro-002` | Fairfield Stags | Fairfield Stags | Fairfield region — align city/market in pack JSON |
| `team-conf-metro-003` | Iona Gaels | Iona Gaels | Iona region — align city/market in pack JSON |
| `team-conf-metro-004` | Manhattan Jaspers | Manhattan Jaspers | Manhattan region — align city/market in pack JSON |
| `team-conf-metro-005` | Marist Red Foxes | Marist Red Foxes | Marist region — align city/market in pack JSON |
| `team-conf-metro-006` | Merrimack Warriors | Merrimack Sentinels | Merrimack region — align city/market in pack JSON |
| `team-conf-metro-007` | Mount St. Mary's Mountaineers | Mount Saint Mary's Highlanders | Mount region — align city/market in pack JSON |
| `team-conf-metro-008` | Niagara Purple Eagles | Niagara Purple Eagles | Niagara region — align city/market in pack JSON |
| `team-conf-metro-009` | Quinnipiac Bobcats | Quinnipiac Bobcats | Quinnipiac region — align city/market in pack JSON |
| `team-conf-metro-010` | Rider Broncs | Rider Broncs | Rider region — align city/market in pack JSON |
| `team-conf-metro-011` | Sacred Heart Pioneers | Sacred Heart Pioneers | Sacred region — align city/market in pack JSON |
| `team-conf-metro-012` | Saint Peter's Peacocks | Saint Peter's Peacocks | Saint region — align city/market in pack JSON |
| `team-conf-metro-013` | Siena Saints | Siena Saints | Siena region — align city/market in pack JSON |

### Midland American Circuit

*Real league: Mid-American Conference (MAC)* · `conf-mac` · **12 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-mac-001` | Akron Zips | Akron Zips | Akron region — align city/market in pack JSON |
| `team-conf-mac-002` | Ball State Cardinals | Ball State Scarlets | Ball region — align city/market in pack JSON |
| `team-conf-mac-003` | Bowling Green Falcons | Bowling Green Falcons | Bowling region — align city/market in pack JSON |
| `team-conf-mac-004` | Buffalo Bulls | Buffalo Stampede | Buffalo region — align city/market in pack JSON |
| `team-conf-mac-005` | Central Michigan Chippewas | Central Michigan Chippewas | Central region — align city/market in pack JSON |
| `team-conf-mac-006` | Eastern Michigan Eagles | Eastern Michigan Hawks | Eastern region — align city/market in pack JSON |
| `team-conf-mac-007` | Kent State Golden Flashes | Kent State Golden Flashes | Kent region — align city/market in pack JSON |
| `team-conf-mac-008` | Miami (OH) RedHawks | Miami RedHawks | Miami region — align city/market in pack JSON |
| `team-conf-mac-009` | Ohio Bobcats | Ohio Bobcats | Ohio region — align city/market in pack JSON |
| `team-conf-mac-010` | Toledo Rockets | Toledo Rockets | Toledo region — align city/market in pack JSON |
| `team-conf-mac-011` | UMass Minutemen | Bay State Minutemen | UMass region — align city/market in pack JSON |
| `team-conf-mac-012` | Western Michigan Broncos | Western Michigan Broncos | Western region — align city/market in pack JSON |

### Mid-Eastern Alliance Conference

*Real league: Mid-Eastern Athletic Conference (MEAC)* · `conf-meac` · **8 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-meac-001` | Coppin State Eagles | Coppin State Hawks | Coppin region — align city/market in pack JSON |
| `team-conf-meac-002` | Delaware State Hornets | Delaware State Hornets | Delaware region — align city/market in pack JSON |
| `team-conf-meac-003` | Howard Bison | Howard Bison | Howard region — align city/market in pack JSON |
| `team-conf-meac-004` | Maryland Eastern Shore Hawks (UMES) | Maryland Eastern Shore Falcons | Maryland region — align city/market in pack JSON |
| `team-conf-meac-005` | Morgan State Bears | Morgan State Bruins | Morgan region — align city/market in pack JSON |
| `team-conf-meac-006` | Norfolk State Spartans | Norfolk State Hoplites | Norfolk region — align city/market in pack JSON |
| `team-conf-meac-007` | North Carolina Central Eagles | North Carolina Central Hawks | North region — align city/market in pack JSON |
| `team-conf-meac-008` | South Carolina State Bulldogs | South Carolina State Mastiffs | South region — align city/market in pack JSON |

### Prairie Valley Conference

*Real league: Missouri Valley Conference (MVC)* · `conf-pvc` · **11 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-pvc-001` | Belmont Bruins | Belmont Bruins | Belmont region — align city/market in pack JSON |
| `team-conf-pvc-002` | Bradley Braves | Bradley Braves | Bradley region — align city/market in pack JSON |
| `team-conf-pvc-003` | Drake Bulldogs | Drake Mastiffs | Drake region — align city/market in pack JSON |
| `team-conf-pvc-004` | Evansville Purple Aces | Evansville Purple Aces | Evansville region — align city/market in pack JSON |
| `team-conf-pvc-005` | Illinois State Redbirds | Illinois State Redbirds | Illinois region — align city/market in pack JSON |
| `team-conf-pvc-006` | Indiana State Sycamores | Indiana State Sycamores | Indiana region — align city/market in pack JSON |
| `team-conf-pvc-007` | Murray State Racers | Murray State Racers | Murray region — align city/market in pack JSON |
| `team-conf-pvc-008` | Northern Iowa Panthers (UNI) | Northern Iowa Pumas | Northern region — align city/market in pack JSON |
| `team-conf-pvc-009` | Southern Illinois Salukis | Southern Illinois Salukis | Southern region — align city/market in pack JSON |
| `team-conf-pvc-010` | UIC Flames | UIC Flames | UIC region — align city/market in pack JSON |
| `team-conf-pvc-011` | Valparaiso Beacons | Valparaiso Beacons | Valparaiso region — align city/market in pack JSON |

### Rocky Mountain West Conference

*Real league: Mountain West Conference (MW)* · `conf-rmwc` · **10 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-rmwc-001` | Air Force Falcons | Air Force Falcons | Air region — align city/market in pack JSON |
| `team-conf-rmwc-002` | Grand Canyon Antelopes | Grand Canyon Antelopes | Grand region — align city/market in pack JSON |
| `team-conf-rmwc-003` | Hawaiʻi Rainbow Warriors | Honolulu Rainbow Guard | Honolulu, HI |
| `team-conf-rmwc-004` | Nevada Wolf Pack | Nevada Wolf Pack | Nevada region — align city/market in pack JSON |
| `team-conf-rmwc-005` | New Mexico Lobos | New Mexico Lobos | New region — align city/market in pack JSON |
| `team-conf-rmwc-006` | San José State Spartans | San José State Hoplites | San region — align city/market in pack JSON |
| `team-conf-rmwc-007` | UC Davis Aggies | UC Davis Aggies | UC region — align city/market in pack JSON |
| `team-conf-rmwc-008` | UNLV Runnin' Rebels | Las Vegas Metro Runnin' Rebels | UNLV region — align city/market in pack JSON |
| `team-conf-rmwc-009` | UTEP Miners | El Paso Metro Miners | UTEP region — align city/market in pack JSON |
| `team-conf-rmwc-010` | Wyoming Cowboys | Wyoming Cowboys | Wyoming region — align city/market in pack JSON |

### Northeastern Exchange Conference

*Real league: Northeast Conference (NEC)* · `conf-nec` · **9 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-nec-001` | Central Connecticut Blue Devils | Central Connecticut Blue Devils | Central region — align city/market in pack JSON |
| `team-conf-nec-002` | Chicago State Cougars | Chicago State Cougars | Chicago region — align city/market in pack JSON |
| `team-conf-nec-003` | Fairleigh Dickinson Knights (FDU) | Fairleigh Dickinson Knights | Fairleigh region — align city/market in pack JSON |
| `team-conf-nec-004` | Le Moyne Dolphins | Le Moyne Dolphins | Le region — align city/market in pack JSON |
| `team-conf-nec-005` | LIU Sharks | LIU Sharks | LIU region — align city/market in pack JSON |
| `team-conf-nec-006` | Mercyhurst Lakers | Mercyhurst Lakers | Mercyhurst region — align city/market in pack JSON |
| `team-conf-nec-007` | New Haven Chargers | New Haven Chargers | New region — align city/market in pack JSON |
| `team-conf-nec-008` | Stonehill Skyhawks | Stonehill Skyhawks | Stonehill region — align city/market in pack JSON |
| `team-conf-nec-009` | Wagner Seahawks | Wagner Seahawks | Wagner region — align city/market in pack JSON |

### Ohio Valley Alliance

*Real league: Ohio Valley Conference (OVC)* · `conf-ova` · **8 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-ova-001` | Eastern Illinois Panthers | Eastern Illinois Pumas | Eastern region — align city/market in pack JSON |
| `team-conf-ova-002` | Lindenwood Lions | Lindenwood Monarchs | Lindenwood region — align city/market in pack JSON |
| `team-conf-ova-003` | Morehead State Eagles | Morehead State Hawks | Morehead region — align city/market in pack JSON |
| `team-conf-ova-004` | SIU Edwardsville Cougars (SIUE) | SIU Edwardsville Cougars | SIU region — align city/market in pack JSON |
| `team-conf-ova-005` | Southeast Missouri State Redhawks (SEMO) | Southeast Missouri State Redhawks | Southeast region — align city/market in pack JSON |
| `team-conf-ova-006` | Southern Indiana Screaming Eagles | Southern Indiana Screaming Hawks | Southern region — align city/market in pack JSON |
| `team-conf-ova-007` | UT Martin Skyhawks | UT Martin Skyhawks | UT region — align city/market in pack JSON |
| `team-conf-ova-008` | Western Illinois Leathernecks | Western Illinois Leathernecks | Western region — align city/market in pack JSON |

### Pacific Rim Twelve

*Real league: Pac-12 Conference* · `conf-pr12` · **9 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-pr12-001` | Boise State Broncos | Boise State Broncos | Boise region — align city/market in pack JSON |
| `team-conf-pr12-002` | Colorado State Rams | Colorado State Rams | Colorado region — align city/market in pack JSON |
| `team-conf-pr12-003` | Fresno State Bulldogs | Fresno State Mastiffs | Fresno region — align city/market in pack JSON |
| `team-conf-pr12-004` | Gonzaga Bulldogs | Spokane River Mastiffs | Spokane, WA |
| `team-conf-pr12-005` | Oregon State Beavers | Oregon State Beavers | Oregon region — align city/market in pack JSON |
| `team-conf-pr12-006` | San Diego State Aztecs | San Diego State Aztecs | San region — align city/market in pack JSON |
| `team-conf-pr12-007` | Texas State Bobcats | Texas State Bobcats | Austin, TX |
| `team-conf-pr12-008` | Utah State Aggies | Utah State Aggies | Utah region — align city/market in pack JSON |
| `team-conf-pr12-009` | Washington State Cougars | Washington State Cougars | Washington region — align city/market in pack JSON |

### Patriot Scholar League

*Real league: Patriot League* · `conf-psl` · **10 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-psl-001` | American Eagles | American Hawks | American region — align city/market in pack JSON |
| `team-conf-psl-002` | Army Black Knights | Army Black Knights | Army region — align city/market in pack JSON |
| `team-conf-psl-003` | Boston University Terriers | Boston University Terriers | Boston region — align city/market in pack JSON |
| `team-conf-psl-004` | Bucknell Bison | Bucknell Bison | Bucknell region — align city/market in pack JSON |
| `team-conf-psl-005` | Colgate Raiders | Colgate Rangers | Colgate region — align city/market in pack JSON |
| `team-conf-psl-006` | Holy Cross Crusaders | Holy Cross Crusaders | Holy region — align city/market in pack JSON |
| `team-conf-psl-007` | Lafayette Leopards | Lafayette Leopards | Lafayette region — align city/market in pack JSON |
| `team-conf-psl-008` | Lehigh Mountain Hawks | Lehigh Mountain Hawks | Lehigh region — align city/market in pack JSON |
| `team-conf-psl-009` | Loyola Maryland Greyhounds | Loyola Maryland Greyhounds | Loyola region — align city/market in pack JSON |
| `team-conf-psl-010` | Navy Midshipmen | Navy Midshipmen | Navy region — align city/market in pack JSON |

### Southeastern Dominion Conference

*Real league: Southeastern Conference (SEC)* · `conf-sedc` · **16 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-sedc-001` | Alabama Crimson Tide | Alabama Crimson Tide | Alabama region — align city/market in pack JSON |
| `team-conf-sedc-002` | Arkansas Razorbacks | Arkansas Razorbacks | Arkansas region — align city/market in pack JSON |
| `team-conf-sedc-003` | Auburn Tigers | Auburn Cougars | Auburn region — align city/market in pack JSON |
| `team-conf-sedc-004` | Florida Gators | Florida Gators | Florida region — align city/market in pack JSON |
| `team-conf-sedc-005` | Georgia Bulldogs | Georgia Mastiffs | Georgia region — align city/market in pack JSON |
| `team-conf-sedc-006` | Kentucky Wildcats | Bluegrass Commonwealth Lynx | Lexington, KY |
| `team-conf-sedc-007` | LSU Tigers | LSU Cougars | LSU region — align city/market in pack JSON |
| `team-conf-sedc-008` | Mississippi State Bulldogs | Mississippi State Mastiffs | Mississippi region — align city/market in pack JSON |
| `team-conf-sedc-009` | Missouri Tigers | Missouri Cougars | Missouri region — align city/market in pack JSON |
| `team-conf-sedc-010` | Oklahoma Sooners | Oklahoma Sooners | Oklahoma region — align city/market in pack JSON |
| `team-conf-sedc-011` | Ole Miss Rebels | Ole Miss Rebels | Ole region — align city/market in pack JSON |
| `team-conf-sedc-012` | South Carolina Gamecocks | South Carolina Gamecocks | South region — align city/market in pack JSON |
| `team-conf-sedc-013` | Tennessee Volunteers | Tennessee Volunteers | Tennessee region — align city/market in pack JSON |
| `team-conf-sedc-014` | Texas Longhorns | Austin Metro Longhorns | Austin, TX |
| `team-conf-sedc-015` | Texas A&M Aggies | Texas A&M Aggies | Austin, TX |
| `team-conf-sedc-016` | Vanderbilt Commodores | Vanderbilt Commodores | Vanderbilt region — align city/market in pack JSON |

### Southern Collegiate Federation

*Real league: Southern Conference (SoCon)* · `conf-scf` · **11 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-scf-001` | Chattanooga Mocs | Chattanooga Mocs | Chattanooga region — align city/market in pack JSON |
| `team-conf-scf-002` | East Tennessee State Buccaneers (ETSU) | East Tennessee State Privateers | East region — align city/market in pack JSON |
| `team-conf-scf-003` | Furman Paladins | Furman Paladins | Furman region — align city/market in pack JSON |
| `team-conf-scf-004` | Mercer Bears | Mercer Bruins | Mercer region — align city/market in pack JSON |
| `team-conf-scf-005` | Samford Bulldogs | Samford Mastiffs | Samford region — align city/market in pack JSON |
| `team-conf-scf-006` | Tennessee Tech Golden Eagles | Tennessee Tech Golden Eagles | Tennessee region — align city/market in pack JSON |
| `team-conf-scf-007` | The Citadel Bulldogs | The Citadel Mastiffs | The region — align city/market in pack JSON |
| `team-conf-scf-008` | UNC Greensboro Spartans (UNCG) | North Cape Greensboro Hoplites | UNC region — align city/market in pack JSON |
| `team-conf-scf-009` | VMI Keydets | VMI Keydets | VMI region — align city/market in pack JSON |
| `team-conf-scf-010` | Western Carolina Catamounts | Western Carolina Catamounts | Western region — align city/market in pack JSON |
| `team-conf-scf-011` | Wofford Terriers | Wofford Terriers | Wofford region — align city/market in pack JSON |

### Gulf Coastland Conference

*Real league: Southland Conference* · `conf-gcc` · **12 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-gcc-001` | East Texas A&M Lions | East Texas A&M Monarchs | East region — align city/market in pack JSON |
| `team-conf-gcc-002` | Houston Christian Huskies | Houston Christian Huskies | Houston region — align city/market in pack JSON |
| `team-conf-gcc-003` | Incarnate Word Cardinals (UIW) | Incarnate Word Scarlets | Incarnate region — align city/market in pack JSON |
| `team-conf-gcc-004` | Lamar Cardinals | Lamar Scarlets | Lamar region — align city/market in pack JSON |
| `team-conf-gcc-005` | McNeese Cowboys | McNeese Cowboys | McNeese region — align city/market in pack JSON |
| `team-conf-gcc-006` | New Orleans Privateers | New Orleans Privateers | New region — align city/market in pack JSON |
| `team-conf-gcc-007` | Nicholls Colonels | Nicholls Colonels | Nicholls region — align city/market in pack JSON |
| `team-conf-gcc-008` | Northwestern State Demons | Northwestern State Demons | Northwestern region — align city/market in pack JSON |
| `team-conf-gcc-009` | Southeastern Louisiana Lions | Southeastern Louisiana Monarchs | Southeastern region — align city/market in pack JSON |
| `team-conf-gcc-010` | Stephen F. Austin Lumberjacks (SFA) | Stephen F. Austin Lumberjacks | Stephen region — align city/market in pack JSON |
| `team-conf-gcc-011` | Texas A&M-Corpus Christi Islanders | Texas A&M-Corpus Christi Islanders | Austin, TX |
| `team-conf-gcc-012` | UT Rio Grande Valley Vaqueros (UTRGV) | UT Rio Grande Valley Vaqueros | UT region — align city/market in pack JSON |

### Southwest Collegiate Athletic League

*Real league: Southwestern Athletic Conference (SWAC)* · `conf-scal` · **12 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-scal-001` | Alabama A&M Bulldogs | Alabama A&M Mastiffs | Alabama region — align city/market in pack JSON |
| `team-conf-scal-002` | Alabama State Hornets | Alabama State Hornets | Alabama region — align city/market in pack JSON |
| `team-conf-scal-003` | Alcorn State Braves | Alcorn State Braves | Alcorn region — align city/market in pack JSON |
| `team-conf-scal-004` | Arkansas-Pine Bluff Golden Lions (UAPB) | Arkansas-Pine Bluff Golden Lions | Arkansas-Pine region — align city/market in pack JSON |
| `team-conf-scal-005` | Bethune-Cookman Wildcats | Bethune-Cookman Lynx | Bethune-Cookman region — align city/market in pack JSON |
| `team-conf-scal-006` | Florida A&M Rattlers (FAMU) | Florida A&M Rattlers | Florida region — align city/market in pack JSON |
| `team-conf-scal-007` | Grambling State Tigers | Grambling State Cougars | Grambling region — align city/market in pack JSON |
| `team-conf-scal-008` | Jackson State Tigers | Jackson State Cougars | Jackson region — align city/market in pack JSON |
| `team-conf-scal-009` | Mississippi Valley State Delta Devils | Mississippi Valley State Delta Devils | Mississippi region — align city/market in pack JSON |
| `team-conf-scal-010` | Prairie View A&M Panthers | Prairie View A&M Pumas | Prairie region — align city/market in pack JSON |
| `team-conf-scal-011` | Southern Jaguars | Southern Jaguars | Southern region — align city/market in pack JSON |
| `team-conf-scal-012` | Texas Southern Tigers | Texas Southern Cougars | Austin, TX |

### Prairie Summit League

*Real league: Summit League* · `conf-summit` · **8 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-summit-001` | Kansas City Roos | Kansas City Roos | Lawrence, KS |
| `team-conf-summit-002` | North Dakota Fighting Hawks | North Dakota Fighting Hawks | North region — align city/market in pack JSON |
| `team-conf-summit-003` | North Dakota State Bison | North Dakota State Bison | North region — align city/market in pack JSON |
| `team-conf-summit-004` | Omaha Mavericks | Omaha Mavericks | Omaha region — align city/market in pack JSON |
| `team-conf-summit-005` | Oral Roberts Golden Eagles | Oral Roberts Golden Eagles | Oral region — align city/market in pack JSON |
| `team-conf-summit-006` | South Dakota Coyotes | South Dakota Coyotes | South region — align city/market in pack JSON |
| `team-conf-summit-007` | South Dakota State Jackrabbits | South Dakota State Jackrabbits | South region — align city/market in pack JSON |
| `team-conf-summit-008` | St. Thomas Tommies | Saint Thomas Tommies | St. region — align city/market in pack JSON |

### Solar Belt Conference

*Real league: Sun Belt Conference* · `conf-sbc` · **14 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-sbc-001` | Appalachian State Mountaineers | Appalachian State Highlanders | Appalachian region — align city/market in pack JSON |
| `team-conf-sbc-002` | Arkansas State Red Wolves | Arkansas State Red Wolves | Arkansas region — align city/market in pack JSON |
| `team-conf-sbc-003` | Coastal Carolina Chanticleers | Coastal Carolina Chanticleers | Coastal region — align city/market in pack JSON |
| `team-conf-sbc-004` | Georgia Southern Eagles | Georgia Southern Hawks | Georgia region — align city/market in pack JSON |
| `team-conf-sbc-005` | Georgia State Panthers | Georgia State Pumas | Georgia region — align city/market in pack JSON |
| `team-conf-sbc-006` | James Madison Dukes (JMU) | James Madison Dukes | James region — align city/market in pack JSON |
| `team-conf-sbc-007` | Louisiana Ragin' Cajuns | Louisiana Ragin' Cajuns | Louisiana region — align city/market in pack JSON |
| `team-conf-sbc-008` | Louisiana-Monroe Warhawks (ULM) | Louisiana-Monroe Warhawks | Louisiana-Monroe region — align city/market in pack JSON |
| `team-conf-sbc-009` | Louisiana Tech Bulldogs | Louisiana Tech Mastiffs | Louisiana region — align city/market in pack JSON |
| `team-conf-sbc-010` | Marshall Thundering Herd | Marshall Thundering Herd | Marshall region — align city/market in pack JSON |
| `team-conf-sbc-011` | Old Dominion Monarchs (ODU) | Old Dominion Monarchs | Old region — align city/market in pack JSON |
| `team-conf-sbc-012` | South Alabama Jaguars | South Alabama Jaguars | South region — align city/market in pack JSON |
| `team-conf-sbc-013` | Southern Miss Golden Eagles | Southern Miss Golden Eagles | Southern region — align city/market in pack JSON |
| `team-conf-sbc-014` | Troy Trojans | Troy Trojans | Troy region — align city/market in pack JSON |

### United Athletic Collegiate

*Real league: United Athletic Conference (UAC) (formerly WAC)* · `conf-uac` · **9 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-uac-001` | Abilene Christian Wildcats | Abilene Christian Lynx | Abilene region — align city/market in pack JSON |
| `team-conf-uac-002` | Austin Peay Governors | Austin Peay Governors | Austin region — align city/market in pack JSON |
| `team-conf-uac-003` | Central Arkansas Bears | Central Arkansas Bruins | Central region — align city/market in pack JSON |
| `team-conf-uac-004` | Eastern Kentucky Colonels | Eastern Kentucky Colonels | Eastern region — align city/market in pack JSON |
| `team-conf-uac-005` | Little Rock Trojans | Little Rock Trojans | Little region — align city/market in pack JSON |
| `team-conf-uac-006` | North Alabama Lions | North Alabama Monarchs | North region — align city/market in pack JSON |
| `team-conf-uac-007` | Tarleton State Texans | Tarleton State Texans | Tarleton region — align city/market in pack JSON |
| `team-conf-uac-008` | UT Arlington Mavericks | UT Arlington Mavericks | UT region — align city/market in pack JSON |
| `team-conf-uac-009` | West Georgia Wolves | West Georgia Wolves | West region — align city/market in pack JSON |

### Western Coast Collegiate

*Real league: West Coast Conference (WCC)* · `conf-wcc` · **10 teams**

| `teamId` | Real (reference) | Universe (default pack) | Geography anchor |
|----------|-------------------|-------------------------|------------------|
| `team-conf-wcc-001` | Denver Pioneers | Denver Pioneers | Denver region — align city/market in pack JSON |
| `team-conf-wcc-002` | Loyola Marymount Lions (LMU) | Loyola Marymount Monarchs | Loyola region — align city/market in pack JSON |
| `team-conf-wcc-003` | Pacific Tigers | Pacific Cougars | Pacific region — align city/market in pack JSON |
| `team-conf-wcc-004` | Pepperdine Waves | Pepperdine Waves | Pepperdine region — align city/market in pack JSON |
| `team-conf-wcc-005` | Portland Pilots | Portland Pilots | Portland region — align city/market in pack JSON |
| `team-conf-wcc-006` | Saint Mary's Gaels | Saint Mary's Gaels | Saint region — align city/market in pack JSON |
| `team-conf-wcc-007` | San Diego Toreros | San Diego Toreros | San region — align city/market in pack JSON |
| `team-conf-wcc-008` | San Francisco Dons | San Francisco Dons | San region — align city/market in pack JSON |
| `team-conf-wcc-009` | Santa Clara Broncos | Santa Clara Broncos | Santa region — align city/market in pack JSON |
| `team-conf-wcc-010` | Seattle U Redhawks | Seattle U Redhawks | Seattle region — align city/market in pack JSON |

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
| **Realignment** | Metro (ex-MAAC), UAC (ex-WAC), Pac-12 remnant, MW + UC Davis, Big West + Utah Valley, etc. reflected in this snapshot |
| **Regeneration** | `python3 planning/tools/generate_league_branding.py` after membership edits |

**Membership count (this file):** 363 teams · 32 conferences.

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Initial conference + team mapping |
| 2025-11-08 | **Rebuild:** 2026–27 D-I list — 32 conferences, Metro/UAC, membership moves |
