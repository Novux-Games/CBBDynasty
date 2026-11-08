# League branding — conferences & teams (reference mapping)

**Product:** [Dynasty: College Basketball](branding.md) · default **universe** names for identity packs.
**Membership:** **Active 2026–27** NCAA Division I men's basketball — **32 conferences**, **363 teams** (per user reference list).
**Purpose:** Keep **real-world reference** alongside **ship-safe display** names. **Geography in sim:** **`homeState` only** (US state / DC) — used for **[recruiting pipeline](recruiting-engine.md)** (in-state / regional bias). Not city, market, or travel distance in v1.
**Related:** [`game-design.md`](game-design.md#identity-packs--save-model), [`branding.md`](branding.md), [`recruiting-engine.md`](recruiting-engine.md).
**Last updated:** 2025-11-08
**Status:** v2 membership; universe names + `homeState`; colors/logos in pack JSON (not geography fields).

---

## Contents

1. [Conferences](#conferences)
2. [Teams by conference](#teams-by-conference)
3. [Geography & recruiting](#geography--recruiting)
4. [National body & tournaments (TBD)](#national-body--tournaments-tbd)
5. [Pack authoring notes](#pack-authoring-notes)
6. [Document history](#document-history)

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

Columns: stable `teamId`, real program (editor reference), universe display name, **`homeState`** (recruiting pipeline).

### Northern Atlantic Collegiate Conference

*Real league: America East Conference* · `conf-nac` · **9 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-nac-001` | Albany Great Danes | Albany Great Danes | NY |
| `team-conf-nac-002` | Binghamton Bearcats | Binghamton Bearcats | NY |
| `team-conf-nac-003` | Bryant Bulldogs | Bryant Mastiffs | RI |
| `team-conf-nac-004` | Maine Black Bears | Maine Black Bears | ME |
| `team-conf-nac-005` | New Hampshire Wildcats | New Hampshire Lynx | NH |
| `team-conf-nac-006` | NJIT Highlanders | Newark Tech Highlanders | NJ |
| `team-conf-nac-007` | UMBC Retrievers | UMBC Retrievers | MD |
| `team-conf-nac-008` | UMass Lowell River Hawks | Bay State Lowell River Hawks | MA |
| `team-conf-nac-009` | Vermont Catamounts | Vermont Catamounts | VT |

### American Unified Collegiate Conference

*Real league: American Athletic Conference (AAC)* · `conf-aucc` · **13 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-aucc-001` | Charlotte 49ers | Charlotte 49ers | NC |
| `team-conf-aucc-002` | East Carolina Pirates | East Carolina Pirates | NC |
| `team-conf-aucc-003` | Florida Atlantic Owls (FAU) | Florida Atlantic Owls | FL |
| `team-conf-aucc-004` | Memphis Tigers | Memphis Cougars | TN |
| `team-conf-aucc-005` | North Texas Mean Green | North Texas Mean Green | TX |
| `team-conf-aucc-006` | Rice Owls | Rice Owls | TX |
| `team-conf-aucc-007` | South Florida Bulls (USF) | South Florida Stampede | FL |
| `team-conf-aucc-008` | Temple Owls | Temple Owls | PA |
| `team-conf-aucc-009` | Tulane Green Wave | Tulane Green Wave | LA |
| `team-conf-aucc-010` | Tulsa Golden Hurricane | Tulsa Golden Hurricane | OK |
| `team-conf-aucc-011` | UAB Blazers | Birmingham Metro Blazers | AL |
| `team-conf-aucc-012` | UTSA Roadrunners | South Alamo Roadrunners | TX |
| `team-conf-aucc-013` | Wichita State Shockers | Wichita State Shockers | KS |

### Colonial Atlantic Ten

*Real league: Atlantic 10 Conference (A-10)* · `conf-cat10` · **14 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-cat10-001` | Davidson Wildcats | Davidson Lynx | NC |
| `team-conf-cat10-002` | Dayton Flyers | Dayton Flyers | OH |
| `team-conf-cat10-003` | Duquesne Dukes | Duquesne Dukes | PA |
| `team-conf-cat10-004` | Fordham Rams | Fordham Rams | NY |
| `team-conf-cat10-005` | George Mason Patriots | George Mason Patriots | VA |
| `team-conf-cat10-006` | George Washington Revolutionaries | George Washington Revolutionaries | DC |
| `team-conf-cat10-007` | La Salle Explorers | La Salle Explorers | PA |
| `team-conf-cat10-008` | Loyola Chicago Ramblers | Loyola Chicago Ramblers | IL |
| `team-conf-cat10-009` | Rhode Island Rams | Rhode Island Rams | RI |
| `team-conf-cat10-010` | Richmond Spiders | Richmond Spiders | VA |
| `team-conf-cat10-011` | Saint Joseph's Hawks | Saint Joseph's Falcons | PA |
| `team-conf-cat10-012` | Saint Louis Billikens | Saint Louis Billikens | MO |
| `team-conf-cat10-013` | St. Bonaventure Bonnies | Saint Bonaventure Bonnies | NY |
| `team-conf-cat10-014` | VCU Rams | VCU Rams | VA |

### Atlantic Crown Conference

*Real league: Atlantic Coast Conference (ACC)* · `conf-acrown` · **18 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-acrown-001` | Boston College Eagles | Boston College Hawks | MA |
| `team-conf-acrown-002` | California Golden Bears (Cal) | Bay Golden Bruins | CA |
| `team-conf-acrown-003` | Clemson Tigers | Clemson Cougars | SC |
| `team-conf-acrown-004` | Duke Blue Devils | Crown University Sovereigns | NC |
| `team-conf-acrown-005` | Florida State Seminoles | Florida State Seminoles | FL |
| `team-conf-acrown-006` | Georgia Tech Yellow Jackets | Georgia Tech Yellow Jackets | GA |
| `team-conf-acrown-007` | Louisville Cardinals | Louisville Scarlets | KY |
| `team-conf-acrown-008` | Miami Hurricanes | Miami Hurricanes | FL |
| `team-conf-acrown-009` | NC State Wolfpack | NC State Wolfpack | NC |
| `team-conf-acrown-010` | North Carolina Tar Heels | Tar River Commonwealth | NC |
| `team-conf-acrown-011` | Notre Dame Fighting Irish | Notre Dame Fighting Irish | IN |
| `team-conf-acrown-012` | Pittsburgh Panthers | Pittsburgh Pumas | PA |
| `team-conf-acrown-013` | SMU Mustangs | Dallas Metro Mustangs | TX |
| `team-conf-acrown-014` | Stanford Cardinal | Stanford Cardinal | CA |
| `team-conf-acrown-015` | Syracuse Orange | Syracuse Orange | NY |
| `team-conf-acrown-016` | Virginia Cavaliers | Virginia Cavaliers | VA |
| `team-conf-acrown-017` | Virginia Tech Hokies | Virginia Tech Hokies | VA |
| `team-conf-acrown-018` | Wake Forest Demon Deacons | Wake Forest Demon Deacons | NC |

### Atlantic Sun Collegiate Union

*Real league: Atlantic Sun Conference (ASUN)* · `conf-ascu` · **8 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-ascu-001` | Bellarmine Knights | Bellarmine Knights | KY |
| `team-conf-ascu-002` | Florida Gulf Coast Eagles (FGCU) | Florida Gulf Coast Hawks | FL |
| `team-conf-ascu-003` | Jacksonville Dolphins | Jacksonville Dolphins | FL |
| `team-conf-ascu-004` | Lipscomb Bisons | Lipscomb Bisons | TN |
| `team-conf-ascu-005` | North Florida Ospreys | North Florida Ospreys | FL |
| `team-conf-ascu-006` | Queens Royals | Queens Royals | NC |
| `team-conf-ascu-007` | Stetson Hatters | Stetson Hatters | FL |
| `team-conf-ascu-008` | West Florida Argonauts (D-II transition) | West Florida Argonauts | FL |

### Central Twelve Conference

*Real league: Big 12 Conference* · `conf-c12` · **16 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-c12-001` | Arizona Wildcats | Arizona Lynx | AZ |
| `team-conf-c12-002` | Arizona State Sun Devils | Arizona State Sun Devils | AZ |
| `team-conf-c12-003` | Baylor Bears | Baylor Bruins | TX |
| `team-conf-c12-004` | BYU Cougars | Provo Metro Cougars | UT |
| `team-conf-c12-005` | Cincinnati Bearcats | Cincinnati Bearcats | OH |
| `team-conf-c12-006` | Colorado Buffaloes | Colorado Buffaloes | CO |
| `team-conf-c12-007` | Houston Cougars | Houston Cougars | TX |
| `team-conf-c12-008` | Iowa State Cyclones | Iowa State Cyclones | IA |
| `team-conf-c12-009` | Kansas Jayhawks | Jaydale Commonwealth | KS |
| `team-conf-c12-010` | Kansas State Wildcats | Kansas State Lynx | KS |
| `team-conf-c12-011` | Oklahoma State Cowboys | Oklahoma State Cowboys | OK |
| `team-conf-c12-012` | TCU Horned Frogs | TCU Horned Frogs | TX |
| `team-conf-c12-013` | Texas Tech Red Raiders | Texas Tech Red Raiders | TX |
| `team-conf-c12-014` | UCF Knights | Central Florida Metro Knights | FL |
| `team-conf-c12-015` | Utah Utes | Utah Utes | UT |
| `team-conf-c12-016` | West Virginia Mountaineers | West Virginia Highlanders | WV |

### Eastern Premier Conference

*Real league: Big East Conference* · `conf-epc` · **11 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-epc-001` | Butler Bulldogs | Butler Mastiffs | IN |
| `team-conf-epc-002` | Creighton Bluejays | Creighton Bluejays | NE |
| `team-conf-epc-003` | DePaul Blue Demons | DePaul Blue Demons | IL |
| `team-conf-epc-004` | Georgetown Hoyas | Georgetown Hoyas | DC |
| `team-conf-epc-005` | Marquette Golden Eagles | Marquette Golden Eagles | WI |
| `team-conf-epc-006` | Providence Friars | Providence Friars | RI |
| `team-conf-epc-007` | Seton Hall Pirates | Seton Hall Pirates | NJ |
| `team-conf-epc-008` | St. John's Red Storm | Saint John's Red Storm | NY |
| `team-conf-epc-009` | UConn Huskies | Storrs Sled Dogs | CT |
| `team-conf-epc-010` | Villanova Wildcats | Main Line Lynx | PA |
| `team-conf-epc-011` | Xavier Musketeers | Xavier Musketeers | OH |

### Northern Sky Conference

*Real league: Big Sky Conference* · `conf-nsc` · **11 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-nsc-001` | Eastern Washington Eagles | Eastern Washington Hawks | WA |
| `team-conf-nsc-002` | Idaho Vandals | Idaho Vandals | ID |
| `team-conf-nsc-003` | Idaho State Bengals | Idaho State Bengals | ID |
| `team-conf-nsc-004` | Montana Grizzlies | Montana Grizzlies | MT |
| `team-conf-nsc-005` | Montana State Bobcats | Montana State Bobcats | MT |
| `team-conf-nsc-006` | Northern Arizona Lumberjacks | Northern Arizona Lumberjacks | AZ |
| `team-conf-nsc-007` | Northern Colorado Bears | Northern Colorado Bruins | CO |
| `team-conf-nsc-008` | Portland State Vikings | Portland State Vikings | OR |
| `team-conf-nsc-009` | Southern Utah Thunderbirds | Southern Utah Thunderbirds | UT |
| `team-conf-nsc-010` | Utah Tech Trailblazers | Utah Tech Trailblazers | UT |
| `team-conf-nsc-011` | Weber State Wildcats | Weber State Lynx | UT |

### Piedmont South Conference

*Real league: Big South Conference* · `conf-psc` · **9 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-psc-001` | Charleston Southern Buccaneers | Charleston Southern Privateers | SC |
| `team-conf-psc-002` | Gardner-Webb Runnin' Bulldogs | Gardner-Webb Runnin' Bulldogs | NC |
| `team-conf-psc-003` | High Point Panthers | High Point Pumas | NC |
| `team-conf-psc-004` | Longwood Lancers | Longwood Lancers | VA |
| `team-conf-psc-005` | Presbyterian Blue Hose | Presbyterian Blue Hose | SC |
| `team-conf-psc-006` | Radford Highlanders | Radford Highlanders | VA |
| `team-conf-psc-007` | UNC Asheville Bulldogs | North Cape Asheville Mastiffs | NC |
| `team-conf-psc-008` | USC Upstate Spartans | Southern California Metro Upstate Hoplites | SC |
| `team-conf-psc-009` | Winthrop Eagles | Winthrop Hawks | SC |

### Great Ten Conference

*Real league: Big Ten Conference* · `conf-g10` · **18 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-g10-001` | Illinois Fighting Illini | Illinois Fighting Illini | IL |
| `team-conf-g10-002` | Indiana Hoosiers | Indiana Hoosiers | IN |
| `team-conf-g10-003` | Iowa Hawkeyes | Iowa Hawkeyes | IA |
| `team-conf-g10-004` | Maryland Terrapins | Maryland Terrapins | MD |
| `team-conf-g10-005` | Michigan Wolverines | Michigan Wolverines | MI |
| `team-conf-g10-006` | Michigan State Spartans | Michigan State Hoplites | MI |
| `team-conf-g10-007` | Minnesota Golden Gophers | Minnesota Golden Gophers | MN |
| `team-conf-g10-008` | Nebraska Cornhuskers | Nebraska Cornhuskers | NE |
| `team-conf-g10-009` | Northwestern Wildcats | Northwestern Lynx | IL |
| `team-conf-g10-010` | Ohio State Buckeyes | Ohio State Buckeyes | OH |
| `team-conf-g10-011` | Oregon Ducks | Oregon Ducks | OR |
| `team-conf-g10-012` | Penn State Nittany Lions | Penn State Nittany Lions | PA |
| `team-conf-g10-013` | Purdue Boilermakers | Purdue Boilermakers | IN |
| `team-conf-g10-014` | Rutgers Scarlet Knights | Rutgers Scarlet Knights | NJ |
| `team-conf-g10-015` | UCLA Bruins | Westwood Harbor Guardians | CA |
| `team-conf-g10-016` | USC Trojans | South Bay Argonauts | CA |
| `team-conf-g10-017` | Washington Huskies | Washington Huskies | WA |
| `team-conf-g10-018` | Wisconsin Badgers | Wisconsin Badgers | WI |

### Golden West Conference

*Real league: Big West Conference* · `conf-gwc` · **11 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-gwc-001` | Cal Baptist Lancers | Cal Baptist Lancers | CA |
| `team-conf-gwc-002` | Cal Poly Mustangs | Cal Poly Mustangs | CA |
| `team-conf-gwc-003` | Cal State Bakersfield Roadrunners | Cal State Bakersfield Roadrunners | CA |
| `team-conf-gwc-004` | Cal State Fullerton Titans | Cal State Fullerton Titans | CA |
| `team-conf-gwc-005` | Cal State Northridge Matadors (CSUN) | Cal State Northridge Matadors | CA |
| `team-conf-gwc-006` | Long Beach State Beach | Long Beach State Beach | CA |
| `team-conf-gwc-007` | UC Irvine Anteaters | UC Irvine Anteaters | CA |
| `team-conf-gwc-008` | UC Riverside Highlanders | UC Riverside Highlanders | CA |
| `team-conf-gwc-009` | UC San Diego Tritons | UC San Diego Tritons | CA |
| `team-conf-gwc-010` | UC Santa Barbara Gauchos | UC Santa Barbara Gauchos | CA |
| `team-conf-gwc-011` | Utah Valley Wolverines | Utah Valley Wolverines | UT |

### Coastal Colonial Association

*Real league: Coastal Athletic Association (CAA)* · `conf-cca` · **13 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-cca-001` | Campbell Fighting Camels | Campbell Fighting Camels | NC |
| `team-conf-cca-002` | College of Charleston Cougars | College of Charleston Cougars | SC |
| `team-conf-cca-003` | Drexel Dragons | Drexel Dragons | PA |
| `team-conf-cca-004` | Elon Phoenix | Elon Phoenix | NC |
| `team-conf-cca-005` | Hampton Pirates | Hampton Pirates | VA |
| `team-conf-cca-006` | Hofstra Pride | Hofstra Pride | NY |
| `team-conf-cca-007` | Monmouth Hawks | Monmouth Falcons | NJ |
| `team-conf-cca-008` | North Carolina A&T Aggies | North Carolina A&T Aggies | NC |
| `team-conf-cca-009` | Northeastern Huskies | Northeastern Huskies | MA |
| `team-conf-cca-010` | Stony Brook Seawolves | Stony Brook Seawolves | NY |
| `team-conf-cca-011` | Towson Tigers | Towson Cougars | MD |
| `team-conf-cca-012` | UNC Wilmington Seahawks (UNCW) | North Cape Wilmington Seahawks | NC |
| `team-conf-cca-013` | William & Mary Tribe | William & Mary Tribe | VA |

### United Continental Conference

*Real league: Conference USA (C-USA)* · `conf-ucc` · **10 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-ucc-001` | Delaware Fightin' Blue Hens | Delaware Fightin' Blue Hens | DE |
| `team-conf-ucc-002` | FIU Panthers | Miami-Dade Metro Pumas | FL |
| `team-conf-ucc-003` | Jacksonville State Gamecocks | Jacksonville State Gamecocks | AL |
| `team-conf-ucc-004` | Kennesaw State Owls | Kennesaw State Owls | GA |
| `team-conf-ucc-005` | Liberty Flames | Liberty Flames | VA |
| `team-conf-ucc-006` | Middle Tennessee Blue Raiders (MTSU) | Middle Tennessee Blue Raiders | TN |
| `team-conf-ucc-007` | Missouri State Bears | Missouri State Bruins | MO |
| `team-conf-ucc-008` | New Mexico State Aggies | New Mexico State Aggies | NM |
| `team-conf-ucc-009` | Sam Houston Bearkats | Sam Houston Bearkats | TX |
| `team-conf-ucc-010` | Western Kentucky Hilltoppers (WKU) | Western Kentucky Hilltoppers | KY |

### Lakefront Horizon League

*Real league: Horizon League* · `conf-lhl` · **12 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-lhl-001` | Cleveland State Vikings | Cleveland State Vikings | OH |
| `team-conf-lhl-002` | Detroit Mercy Titans | Detroit Mercy Titans | MI |
| `team-conf-lhl-003` | Green Bay Phoenix | Green Bay Phoenix | WI |
| `team-conf-lhl-004` | IU Indy Jaguars | IU Indy Jaguars | IN |
| `team-conf-lhl-005` | Milwaukee Panthers | Milwaukee Pumas | WI |
| `team-conf-lhl-006` | Northern Illinois Huskies (NIU) | Northern Illinois Huskies | IL |
| `team-conf-lhl-007` | Northern Kentucky Norse | Northern Kentucky Norse | KY |
| `team-conf-lhl-008` | Oakland Golden Grizzlies | Oakland Golden Grizzlies | MI |
| `team-conf-lhl-009` | Purdue Fort Wayne Mastodons | Purdue Fort Wayne Mastodons | IN |
| `team-conf-lhl-010` | Robert Morris Colonials | Robert Morris Colonials | PA |
| `team-conf-lhl-011` | Wright State Raiders | Wright State Rangers | OH |
| `team-conf-lhl-012` | Youngstown State Penguins | Youngstown State Penguins | OH |

### Founding Eight League

*Real league: Ivy League* · `conf-fel` · **8 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-fel-001` | Brown Bears | Brown Bruins | RI |
| `team-conf-fel-002` | Columbia Lions | Columbia Monarchs | NY |
| `team-conf-fel-003` | Cornell Big Red | Cornell Big Red | NY |
| `team-conf-fel-004` | Dartmouth Big Green | Dartmouth Big Green | NH |
| `team-conf-fel-005` | Harvard Crimson | Harvard Crimson | MA |
| `team-conf-fel-006` | Penn Quakers | Penn Quakers | PA |
| `team-conf-fel-007` | Princeton Tigers | Princeton Cougars | NJ |
| `team-conf-fel-008` | Yale Bulldogs | Yale Mastiffs | CT |

### Metro Collegiate Conference

*Real league: Metro Conference (formerly MAAC)* · `conf-metro` · **13 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-metro-001` | Canisius Golden Griffins | Canisius Golden Griffins | NY |
| `team-conf-metro-002` | Fairfield Stags | Fairfield Stags | CT |
| `team-conf-metro-003` | Iona Gaels | Iona Gaels | NY |
| `team-conf-metro-004` | Manhattan Jaspers | Manhattan Jaspers | NY |
| `team-conf-metro-005` | Marist Red Foxes | Marist Red Foxes | NY |
| `team-conf-metro-006` | Merrimack Warriors | Merrimack Sentinels | MA |
| `team-conf-metro-007` | Mount St. Mary's Mountaineers | Mount Saint Mary's Highlanders | MD |
| `team-conf-metro-008` | Niagara Purple Eagles | Niagara Purple Eagles | NY |
| `team-conf-metro-009` | Quinnipiac Bobcats | Quinnipiac Bobcats | CT |
| `team-conf-metro-010` | Rider Broncs | Rider Broncs | NJ |
| `team-conf-metro-011` | Sacred Heart Pioneers | Sacred Heart Pioneers | CT |
| `team-conf-metro-012` | Saint Peter's Peacocks | Saint Peter's Peacocks | NJ |
| `team-conf-metro-013` | Siena Saints | Siena Saints | NY |

### Midland American Circuit

*Real league: Mid-American Conference (MAC)* · `conf-mac` · **12 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-mac-001` | Akron Zips | Akron Zips | OH |
| `team-conf-mac-002` | Ball State Cardinals | Ball State Scarlets | IN |
| `team-conf-mac-003` | Bowling Green Falcons | Bowling Green Falcons | OH |
| `team-conf-mac-004` | Buffalo Bulls | Buffalo Stampede | NY |
| `team-conf-mac-005` | Central Michigan Chippewas | Central Michigan Chippewas | MI |
| `team-conf-mac-006` | Eastern Michigan Eagles | Eastern Michigan Hawks | MI |
| `team-conf-mac-007` | Kent State Golden Flashes | Kent State Golden Flashes | OH |
| `team-conf-mac-008` | Miami (OH) RedHawks | Miami RedHawks | OH |
| `team-conf-mac-009` | Ohio Bobcats | Ohio Bobcats | OH |
| `team-conf-mac-010` | Toledo Rockets | Toledo Rockets | OH |
| `team-conf-mac-011` | UMass Minutemen | Bay State Minutemen | MA |
| `team-conf-mac-012` | Western Michigan Broncos | Western Michigan Broncos | MI |

### Mid-Eastern Alliance Conference

*Real league: Mid-Eastern Athletic Conference (MEAC)* · `conf-meac` · **8 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-meac-001` | Coppin State Eagles | Coppin State Hawks | MD |
| `team-conf-meac-002` | Delaware State Hornets | Delaware State Hornets | DE |
| `team-conf-meac-003` | Howard Bison | Howard Bison | DC |
| `team-conf-meac-004` | Maryland Eastern Shore Hawks (UMES) | Maryland Eastern Shore Falcons | MD |
| `team-conf-meac-005` | Morgan State Bears | Morgan State Bruins | MD |
| `team-conf-meac-006` | Norfolk State Spartans | Norfolk State Hoplites | VA |
| `team-conf-meac-007` | North Carolina Central Eagles | North Carolina Central Hawks | NC |
| `team-conf-meac-008` | South Carolina State Bulldogs | South Carolina State Mastiffs | SC |

### Prairie Valley Conference

*Real league: Missouri Valley Conference (MVC)* · `conf-pvc` · **11 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-pvc-001` | Belmont Bruins | Belmont Bruins | TN |
| `team-conf-pvc-002` | Bradley Braves | Bradley Braves | IL |
| `team-conf-pvc-003` | Drake Bulldogs | Drake Mastiffs | IA |
| `team-conf-pvc-004` | Evansville Purple Aces | Evansville Purple Aces | IN |
| `team-conf-pvc-005` | Illinois State Redbirds | Illinois State Redbirds | IL |
| `team-conf-pvc-006` | Indiana State Sycamores | Indiana State Sycamores | IN |
| `team-conf-pvc-007` | Murray State Racers | Murray State Racers | KY |
| `team-conf-pvc-008` | Northern Iowa Panthers (UNI) | Northern Iowa Pumas | IA |
| `team-conf-pvc-009` | Southern Illinois Salukis | Southern Illinois Salukis | IL |
| `team-conf-pvc-010` | UIC Flames | UIC Flames | IL |
| `team-conf-pvc-011` | Valparaiso Beacons | Valparaiso Beacons | IN |

### Rocky Mountain West Conference

*Real league: Mountain West Conference (MW)* · `conf-rmwc` · **10 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-rmwc-001` | Air Force Falcons | Air Force Falcons | CO |
| `team-conf-rmwc-002` | Grand Canyon Antelopes | Grand Canyon Antelopes | AZ |
| `team-conf-rmwc-003` | Hawaiʻi Rainbow Warriors | Honolulu Rainbow Guard | HI |
| `team-conf-rmwc-004` | Nevada Wolf Pack | Nevada Wolf Pack | NV |
| `team-conf-rmwc-005` | New Mexico Lobos | New Mexico Lobos | NM |
| `team-conf-rmwc-006` | San José State Spartans | San José State Hoplites | CA |
| `team-conf-rmwc-007` | UC Davis Aggies | UC Davis Aggies | CA |
| `team-conf-rmwc-008` | UNLV Runnin' Rebels | Las Vegas Metro Runnin' Rebels | NV |
| `team-conf-rmwc-009` | UTEP Miners | El Paso Metro Miners | TX |
| `team-conf-rmwc-010` | Wyoming Cowboys | Wyoming Cowboys | WY |

### Northeastern Exchange Conference

*Real league: Northeast Conference (NEC)* · `conf-nec` · **9 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-nec-001` | Central Connecticut Blue Devils | Central Connecticut Blue Devils | CT |
| `team-conf-nec-002` | Chicago State Cougars | Chicago State Cougars | IL |
| `team-conf-nec-003` | Fairleigh Dickinson Knights (FDU) | Fairleigh Dickinson Knights | NJ |
| `team-conf-nec-004` | Le Moyne Dolphins | Le Moyne Dolphins | NY |
| `team-conf-nec-005` | LIU Sharks | LIU Sharks | NY |
| `team-conf-nec-006` | Mercyhurst Lakers | Mercyhurst Lakers | PA |
| `team-conf-nec-007` | New Haven Chargers | New Haven Chargers | CT |
| `team-conf-nec-008` | Stonehill Skyhawks | Stonehill Skyhawks | MA |
| `team-conf-nec-009` | Wagner Seahawks | Wagner Seahawks | NY |

### Ohio Valley Alliance

*Real league: Ohio Valley Conference (OVC)* · `conf-ova` · **8 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-ova-001` | Eastern Illinois Panthers | Eastern Illinois Pumas | IL |
| `team-conf-ova-002` | Lindenwood Lions | Lindenwood Monarchs | MO |
| `team-conf-ova-003` | Morehead State Eagles | Morehead State Hawks | KY |
| `team-conf-ova-004` | SIU Edwardsville Cougars (SIUE) | SIU Edwardsville Cougars | IL |
| `team-conf-ova-005` | Southeast Missouri State Redhawks (SEMO) | Southeast Missouri State Redhawks | MO |
| `team-conf-ova-006` | Southern Indiana Screaming Eagles | Southern Indiana Screaming Hawks | IN |
| `team-conf-ova-007` | UT Martin Skyhawks | UT Martin Skyhawks | TN |
| `team-conf-ova-008` | Western Illinois Leathernecks | Western Illinois Leathernecks | IL |

### Pacific Rim Twelve

*Real league: Pac-12 Conference* · `conf-pr12` · **9 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-pr12-001` | Boise State Broncos | Boise State Broncos | ID |
| `team-conf-pr12-002` | Colorado State Rams | Colorado State Rams | CO |
| `team-conf-pr12-003` | Fresno State Bulldogs | Fresno State Mastiffs | CA |
| `team-conf-pr12-004` | Gonzaga Bulldogs | Spokane River Mastiffs | WA |
| `team-conf-pr12-005` | Oregon State Beavers | Oregon State Beavers | OR |
| `team-conf-pr12-006` | San Diego State Aztecs | San Diego State Aztecs | CA |
| `team-conf-pr12-007` | Texas State Bobcats | Texas State Bobcats | TX |
| `team-conf-pr12-008` | Utah State Aggies | Utah State Aggies | UT |
| `team-conf-pr12-009` | Washington State Cougars | Washington State Cougars | WA |

### Patriot Scholar League

*Real league: Patriot League* · `conf-psl` · **10 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-psl-001` | American Eagles | American Hawks | DC |
| `team-conf-psl-002` | Army Black Knights | Army Black Knights | NY |
| `team-conf-psl-003` | Boston University Terriers | Boston University Terriers | MA |
| `team-conf-psl-004` | Bucknell Bison | Bucknell Bison | PA |
| `team-conf-psl-005` | Colgate Raiders | Colgate Rangers | NY |
| `team-conf-psl-006` | Holy Cross Crusaders | Holy Cross Crusaders | MA |
| `team-conf-psl-007` | Lafayette Leopards | Lafayette Leopards | PA |
| `team-conf-psl-008` | Lehigh Mountain Hawks | Lehigh Mountain Hawks | PA |
| `team-conf-psl-009` | Loyola Maryland Greyhounds | Loyola Maryland Greyhounds | MD |
| `team-conf-psl-010` | Navy Midshipmen | Navy Midshipmen | MD |

### Southeastern Dominion Conference

*Real league: Southeastern Conference (SEC)* · `conf-sedc` · **16 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-sedc-001` | Alabama Crimson Tide | Alabama Crimson Tide | AL |
| `team-conf-sedc-002` | Arkansas Razorbacks | Arkansas Razorbacks | AR |
| `team-conf-sedc-003` | Auburn Tigers | Auburn Cougars | AL |
| `team-conf-sedc-004` | Florida Gators | Florida Gators | FL |
| `team-conf-sedc-005` | Georgia Bulldogs | Georgia Mastiffs | GA |
| `team-conf-sedc-006` | Kentucky Wildcats | Bluegrass Commonwealth Lynx | KY |
| `team-conf-sedc-007` | LSU Tigers | LSU Cougars | LA |
| `team-conf-sedc-008` | Mississippi State Bulldogs | Mississippi State Mastiffs | MS |
| `team-conf-sedc-009` | Missouri Tigers | Missouri Cougars | MO |
| `team-conf-sedc-010` | Oklahoma Sooners | Oklahoma Sooners | OK |
| `team-conf-sedc-011` | Ole Miss Rebels | Ole Miss Rebels | MS |
| `team-conf-sedc-012` | South Carolina Gamecocks | South Carolina Gamecocks | SC |
| `team-conf-sedc-013` | Tennessee Volunteers | Tennessee Volunteers | TN |
| `team-conf-sedc-014` | Texas Longhorns | Texas Longhorns | TX |
| `team-conf-sedc-015` | Texas A&M Aggies | Texas A&M Aggies | TX |
| `team-conf-sedc-016` | Vanderbilt Commodores | Vanderbilt Commodores | TN |

### Southern Collegiate Federation

*Real league: Southern Conference (SoCon)* · `conf-scf` · **11 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-scf-001` | Chattanooga Mocs | Chattanooga Mocs | TN |
| `team-conf-scf-002` | East Tennessee State Buccaneers (ETSU) | East Tennessee State Privateers | TN |
| `team-conf-scf-003` | Furman Paladins | Furman Paladins | SC |
| `team-conf-scf-004` | Mercer Bears | Mercer Bruins | GA |
| `team-conf-scf-005` | Samford Bulldogs | Samford Mastiffs | AL |
| `team-conf-scf-006` | Tennessee Tech Golden Eagles | Tennessee Tech Golden Eagles | TN |
| `team-conf-scf-007` | The Citadel Bulldogs | The Citadel Mastiffs | SC |
| `team-conf-scf-008` | UNC Greensboro Spartans (UNCG) | North Cape Greensboro Hoplites | NC |
| `team-conf-scf-009` | VMI Keydets | VMI Keydets | VA |
| `team-conf-scf-010` | Western Carolina Catamounts | Western Carolina Catamounts | NC |
| `team-conf-scf-011` | Wofford Terriers | Wofford Terriers | SC |

### Gulf Coastland Conference

*Real league: Southland Conference* · `conf-gcc` · **12 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-gcc-001` | East Texas A&M Lions | East Texas A&M Monarchs | TX |
| `team-conf-gcc-002` | Houston Christian Huskies | Houston Christian Huskies | TX |
| `team-conf-gcc-003` | Incarnate Word Cardinals (UIW) | Incarnate Word Scarlets | TX |
| `team-conf-gcc-004` | Lamar Cardinals | Lamar Scarlets | TX |
| `team-conf-gcc-005` | McNeese Cowboys | McNeese Cowboys | LA |
| `team-conf-gcc-006` | New Orleans Privateers | New Orleans Privateers | LA |
| `team-conf-gcc-007` | Nicholls Colonels | Nicholls Colonels | LA |
| `team-conf-gcc-008` | Northwestern State Demons | Northwestern State Demons | LA |
| `team-conf-gcc-009` | Southeastern Louisiana Lions | Southeastern Louisiana Monarchs | LA |
| `team-conf-gcc-010` | Stephen F. Austin Lumberjacks (SFA) | Stephen F. Austin Lumberjacks | TX |
| `team-conf-gcc-011` | Texas A&M-Corpus Christi Islanders | Texas A&M-Corpus Christi Islanders | TX |
| `team-conf-gcc-012` | UT Rio Grande Valley Vaqueros (UTRGV) | UT Rio Grande Valley Vaqueros | TX |

### Southwest Collegiate Athletic League

*Real league: Southwestern Athletic Conference (SWAC)* · `conf-scal` · **12 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-scal-001` | Alabama A&M Bulldogs | Alabama A&M Mastiffs | AL |
| `team-conf-scal-002` | Alabama State Hornets | Alabama State Hornets | AL |
| `team-conf-scal-003` | Alcorn State Braves | Alcorn State Braves | MS |
| `team-conf-scal-004` | Arkansas-Pine Bluff Golden Lions (UAPB) | Arkansas-Pine Bluff Golden Lions | AR |
| `team-conf-scal-005` | Bethune-Cookman Wildcats | Bethune-Cookman Lynx | FL |
| `team-conf-scal-006` | Florida A&M Rattlers (FAMU) | Florida A&M Rattlers | FL |
| `team-conf-scal-007` | Grambling State Tigers | Grambling State Cougars | LA |
| `team-conf-scal-008` | Jackson State Tigers | Jackson State Cougars | MS |
| `team-conf-scal-009` | Mississippi Valley State Delta Devils | Mississippi Valley State Delta Devils | MS |
| `team-conf-scal-010` | Prairie View A&M Panthers | Prairie View A&M Pumas | TX |
| `team-conf-scal-011` | Southern Jaguars | Southern Jaguars | LA |
| `team-conf-scal-012` | Texas Southern Tigers | Texas Southern Cougars | TX |

### Prairie Summit League

*Real league: Summit League* · `conf-summit` · **8 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-summit-001` | Kansas City Roos | Kansas City Roos | MO |
| `team-conf-summit-002` | North Dakota Fighting Hawks | North Dakota Fighting Hawks | ND |
| `team-conf-summit-003` | North Dakota State Bison | North Dakota State Bison | ND |
| `team-conf-summit-004` | Omaha Mavericks | Omaha Mavericks | NE |
| `team-conf-summit-005` | Oral Roberts Golden Eagles | Oral Roberts Golden Eagles | OK |
| `team-conf-summit-006` | South Dakota Coyotes | South Dakota Coyotes | SD |
| `team-conf-summit-007` | South Dakota State Jackrabbits | South Dakota State Jackrabbits | SD |
| `team-conf-summit-008` | St. Thomas Tommies | Saint Thomas Tommies | MN |

### Solar Belt Conference

*Real league: Sun Belt Conference* · `conf-sbc` · **14 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-sbc-001` | Appalachian State Mountaineers | Appalachian State Highlanders | NC |
| `team-conf-sbc-002` | Arkansas State Red Wolves | Arkansas State Red Wolves | AR |
| `team-conf-sbc-003` | Coastal Carolina Chanticleers | Coastal Carolina Chanticleers | SC |
| `team-conf-sbc-004` | Georgia Southern Eagles | Georgia Southern Hawks | GA |
| `team-conf-sbc-005` | Georgia State Panthers | Georgia State Pumas | GA |
| `team-conf-sbc-006` | James Madison Dukes (JMU) | James Madison Dukes | VA |
| `team-conf-sbc-007` | Louisiana Ragin' Cajuns | Louisiana Ragin' Cajuns | LA |
| `team-conf-sbc-008` | Louisiana-Monroe Warhawks (ULM) | Louisiana-Monroe Warhawks | LA |
| `team-conf-sbc-009` | Louisiana Tech Bulldogs | Louisiana Tech Mastiffs | LA |
| `team-conf-sbc-010` | Marshall Thundering Herd | Marshall Thundering Herd | WV |
| `team-conf-sbc-011` | Old Dominion Monarchs (ODU) | Old Dominion Monarchs | VA |
| `team-conf-sbc-012` | South Alabama Jaguars | South Alabama Jaguars | AL |
| `team-conf-sbc-013` | Southern Miss Golden Eagles | Southern Miss Golden Eagles | MS |
| `team-conf-sbc-014` | Troy Trojans | Troy Trojans | AL |

### United Athletic Collegiate

*Real league: United Athletic Conference (UAC) (formerly WAC)* · `conf-uac` · **9 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-uac-001` | Abilene Christian Wildcats | Abilene Christian Lynx | TX |
| `team-conf-uac-002` | Austin Peay Governors | Austin Peay Governors | TN |
| `team-conf-uac-003` | Central Arkansas Bears | Central Arkansas Bruins | AR |
| `team-conf-uac-004` | Eastern Kentucky Colonels | Eastern Kentucky Colonels | KY |
| `team-conf-uac-005` | Little Rock Trojans | Little Rock Trojans | AR |
| `team-conf-uac-006` | North Alabama Lions | North Alabama Monarchs | AL |
| `team-conf-uac-007` | Tarleton State Texans | Tarleton State Texans | TX |
| `team-conf-uac-008` | UT Arlington Mavericks | UT Arlington Mavericks | TX |
| `team-conf-uac-009` | West Georgia Wolves | West Georgia Wolves | GA |

### Western Coast Collegiate

*Real league: West Coast Conference (WCC)* · `conf-wcc` · **10 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeState` |
|----------|-------------------|-------------------------|-------------|
| `team-conf-wcc-001` | Denver Pioneers | Denver Pioneers | CO |
| `team-conf-wcc-002` | Loyola Marymount Lions (LMU) | Loyola Marymount Monarchs | CA |
| `team-conf-wcc-003` | Pacific Tigers | Pacific Cougars | CA |
| `team-conf-wcc-004` | Pepperdine Waves | Pepperdine Waves | CA |
| `team-conf-wcc-005` | Portland Pilots | Portland Pilots | OR |
| `team-conf-wcc-006` | Saint Mary's Gaels | Saint Mary's Gaels | CA |
| `team-conf-wcc-007` | San Diego Toreros | San Diego Toreros | CA |
| `team-conf-wcc-008` | San Francisco Dons | San Francisco Dons | CA |
| `team-conf-wcc-009` | Santa Clara Broncos | Santa Clara Broncos | CA |
| `team-conf-wcc-010` | Seattle U Redhawks | Seattle U Redhawks | WA |

---

## Geography & recruiting

| Rule | Detail |
|------|--------|
| **`homeState`** | Two-letter state (or DC) on each team row; stored on program in save/pack |
| **Recruiting** | HS/JUCO recruits tagged with home state; interest/pipeline weights for in-state and regional proximity |
| **Not in v1** | City, metro, mileage, or arena address for sim logic |
| **Colors / logos** | Pack overlay only — unrelated to `homeState` |

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
| **`homeState`** | Required for default pack; drives recruiting pipeline only |
| **Colors & logos** | Pack JSON overlay; not geography fields |
| **Realignment** | Metro (ex-MAAC), UAC (ex-WAC), Pac-12 remnant, MW + UC Davis, Big West + Utah Valley, etc. reflected in this snapshot |
| **Regeneration** | `python3 planning/tools/generate_league_branding.py` after membership edits |

**Membership count (this file):** 363 teams · 32 conferences.

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Initial conference + team mapping |
| 2025-11-08 | **Rebuild:** 2026–27 D-I list — 32 conferences, Metro/UAC, membership moves |
| 2025-11-08 | Geography = `homeState` only (recruiting pipeline) |
