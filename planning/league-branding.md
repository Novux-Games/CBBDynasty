# League branding — conferences & teams (reference mapping)

**Product:** [Dynasty: College Basketball](branding.md) · default **universe** names for identity packs.
**Membership:** **361 fully certified** D-I men's programs · **32 conferences** · **2026–27** ([`2026-NCAA-Directory.md`](2026-NCAA-Directory.md)).
**Not in this file:** **4** provisional reclass members (365 total field) — [Transition programs](#provisional-transition-programs) (inclusion TBD).
**Purpose:** Keep **real-world reference** alongside **ship-safe display** names for a **shippable** default pack.
**Geography:** Every program has **`homeState`** + **`homeCity`** (reference campus). **`homeState`** drives **[recruiting pipeline](recruiting-engine.md)**; **`homeCity`** is stored for schedule/travel/display and pack fidelity.
**Related:** [`game-design.md`](game-design.md#identity-packs--save-model), [`branding.md`](branding.md), [`recruiting-engine.md`](recruiting-engine.md).
**Last updated:** 2025-11-08

---

## Contents

1. [Conferences](#conferences)
2. [Teams by conference](#teams-by-conference)
3. [Geography & recruiting](#geography--recruiting)
4. [Provisional / transition programs](#provisional-transition-programs)
5. [National body & tournaments (TBD)](#national-body--tournaments-tbd)
6. [Pack authoring notes](#pack-authoring-notes)
7. [Document history](#document-history)

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

Columns: `teamId`, real reference, universe name, **`homeCity`**, **`homeState`**.

### Northern Atlantic Collegiate Conference

*Real league: America East Conference* · `conf-nac` · **9 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-nac-001` | Albany Great Danes | Albany Great Danes | Albany | NY |
| `team-conf-nac-002` | Binghamton Bearcats | Binghamton Bearcats | Binghamton | NY |
| `team-conf-nac-003` | Bryant Bulldogs | Bryant Mastiffs | Smithfield | RI |
| `team-conf-nac-004` | Maine Black Bears | Maine Black Bears | Orono | ME |
| `team-conf-nac-005` | New Hampshire Wildcats | New Hampshire Lynx | Durham | NH |
| `team-conf-nac-006` | NJIT Highlanders | Newark Tech Highlanders | Newark | NJ |
| `team-conf-nac-007` | UMBC Retrievers | UMBC Retrievers | Baltimore | MD |
| `team-conf-nac-008` | UMass Lowell River Hawks | Bay State Lowell River Hawks | Lowell | MA |
| `team-conf-nac-009` | Vermont Catamounts | Vermont Catamounts | Burlington | VT |

### American Unified Collegiate Conference

*Real league: American Athletic Conference (AAC)* · `conf-aucc` · **13 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-aucc-001` | Charlotte 49ers | Charlotte 49ers | Charlotte | NC |
| `team-conf-aucc-002` | East Carolina Pirates | East Carolina Pirates | Greenville | NC |
| `team-conf-aucc-003` | Florida Atlantic Owls (FAU) | Florida Atlantic Owls | Boca Raton | FL |
| `team-conf-aucc-004` | Memphis Tigers | Memphis Cougars | Memphis | TN |
| `team-conf-aucc-005` | North Texas Mean Green | North Texas Mean Green | Denton | TX |
| `team-conf-aucc-006` | Rice Owls | Rice Owls | Houston | TX |
| `team-conf-aucc-007` | South Florida Bulls (USF) | South Florida Stampede | Tampa | FL |
| `team-conf-aucc-008` | Temple Owls | Temple Owls | Philadelphia | PA |
| `team-conf-aucc-009` | Tulane Green Wave | Tulane Green Wave | New Orleans | LA |
| `team-conf-aucc-010` | Tulsa Golden Hurricane | Tulsa Golden Hurricane | Tulsa | OK |
| `team-conf-aucc-011` | UAB Blazers | Birmingham Metro Blazers | Birmingham | AL |
| `team-conf-aucc-012` | UTSA Roadrunners | South Alamo Roadrunners | San Antonio | TX |
| `team-conf-aucc-013` | Wichita State Shockers | Wichita State Shockers | Wichita | KS |

### Colonial Atlantic Ten

*Real league: Atlantic 10 Conference (A-10)* · `conf-cat10` · **14 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-cat10-001` | Davidson Wildcats | Davidson Lynx | Davidson | NC |
| `team-conf-cat10-002` | Dayton Flyers | Dayton Flyers | Dayton | OH |
| `team-conf-cat10-003` | Duquesne Dukes | Duquesne Dukes | Pittsburgh | PA |
| `team-conf-cat10-004` | Fordham Rams | Fordham Rams | Bronx | NY |
| `team-conf-cat10-005` | George Mason Patriots | George Mason Patriots | Fairfax | VA |
| `team-conf-cat10-006` | George Washington Revolutionaries | George Washington Revolutionaries | Washington | DC |
| `team-conf-cat10-007` | La Salle Explorers | La Salle Explorers | Philadelphia | PA |
| `team-conf-cat10-008` | Loyola Chicago Ramblers | Loyola Chicago Ramblers | Chicago | IL |
| `team-conf-cat10-009` | Rhode Island Rams | Rhode Island Rams | Kingston | RI |
| `team-conf-cat10-010` | Richmond Spiders | Richmond Spiders | Richmond | VA |
| `team-conf-cat10-011` | Saint Joseph's Hawks | Saint Joseph's Falcons | Philadelphia | PA |
| `team-conf-cat10-012` | Saint Louis Billikens | Saint Louis Billikens | St. Louis | MO |
| `team-conf-cat10-013` | St. Bonaventure Bonnies | Saint Bonaventure Bonnies | St. Bonaventure | NY |
| `team-conf-cat10-014` | VCU Rams | VCU Rams | Richmond | VA |

### Atlantic Crown Conference

*Real league: Atlantic Coast Conference (ACC)* · `conf-acrown` · **18 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-acrown-001` | Boston College Eagles | Boston College Hawks | Chestnut Hill | MA |
| `team-conf-acrown-002` | California Golden Bears (Cal) | Bay Golden Bruins | Berkeley | CA |
| `team-conf-acrown-003` | Clemson Tigers | Clemson Cougars | Clemson | SC |
| `team-conf-acrown-004` | Duke Blue Devils | Crown University Sovereigns | Durham | NC |
| `team-conf-acrown-005` | Florida State Seminoles | Florida State Seminoles | Tallahassee | FL |
| `team-conf-acrown-006` | Georgia Tech Yellow Jackets | Georgia Tech Yellow Jackets | Atlanta | GA |
| `team-conf-acrown-007` | Louisville Cardinals | Louisville Scarlets | Louisville | KY |
| `team-conf-acrown-008` | Miami Hurricanes | Miami Hurricanes | Coral Gables | FL |
| `team-conf-acrown-009` | NC State Wolfpack | NC State Wolfpack | Raleigh | NC |
| `team-conf-acrown-010` | North Carolina Tar Heels | Tar River Commonwealth | Chapel Hill | NC |
| `team-conf-acrown-011` | Notre Dame Fighting Irish | Notre Dame Fighting Irish | Notre Dame | IN |
| `team-conf-acrown-012` | Pittsburgh Panthers | Pittsburgh Pumas | Pittsburgh | PA |
| `team-conf-acrown-013` | SMU Mustangs | Dallas Metro Mustangs | Dallas | TX |
| `team-conf-acrown-014` | Stanford Cardinal | Stanford Cardinal | Stanford | CA |
| `team-conf-acrown-015` | Syracuse Orange | Syracuse Orange | Syracuse | NY |
| `team-conf-acrown-016` | Virginia Cavaliers | Virginia Cavaliers | Charlottesville | VA |
| `team-conf-acrown-017` | Virginia Tech Hokies | Virginia Tech Hokies | Blacksburg | VA |
| `team-conf-acrown-018` | Wake Forest Demon Deacons | Wake Forest Demon Deacons | Winston-Salem | NC |

### Atlantic Sun Collegiate Union

*Real league: Atlantic Sun Conference (ASUN)* · `conf-ascu` · **7 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-ascu-001` | Bellarmine Knights | Bellarmine Knights | Louisville | KY |
| `team-conf-ascu-002` | Florida Gulf Coast Eagles (FGCU) | Florida Gulf Coast Hawks | Fort Myers | FL |
| `team-conf-ascu-003` | Jacksonville Dolphins | Jacksonville Dolphins | Jacksonville | FL |
| `team-conf-ascu-004` | Lipscomb Bisons | Lipscomb Bisons | Nashville | TN |
| `team-conf-ascu-005` | North Florida Ospreys | North Florida Ospreys | Jacksonville | FL |
| `team-conf-ascu-006` | Queens Royals | Queens Royals | Charlotte | NC |
| `team-conf-ascu-007` | Stetson Hatters | Stetson Hatters | DeLand | FL |

### Central Twelve Conference

*Real league: Big 12 Conference* · `conf-c12` · **16 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-c12-001` | Arizona Wildcats | Arizona Lynx | Tucson | AZ |
| `team-conf-c12-002` | Arizona State Sun Devils | Arizona State Sun Devils | Tempe | AZ |
| `team-conf-c12-003` | Baylor Bears | Baylor Bruins | Waco | TX |
| `team-conf-c12-004` | BYU Cougars | Provo Metro Cougars | Provo | UT |
| `team-conf-c12-005` | Cincinnati Bearcats | Cincinnati Bearcats | Cincinnati | OH |
| `team-conf-c12-006` | Colorado Buffaloes | Colorado Buffaloes | Boulder | CO |
| `team-conf-c12-007` | Houston Cougars | Houston Cougars | Houston | TX |
| `team-conf-c12-008` | Iowa State Cyclones | Iowa State Cyclones | Ames | IA |
| `team-conf-c12-009` | Kansas Jayhawks | Jaydale Commonwealth | Lawrence | KS |
| `team-conf-c12-010` | Kansas State Wildcats | Kansas State Lynx | Manhattan | KS |
| `team-conf-c12-011` | Oklahoma State Cowboys | Oklahoma State Cowboys | Stillwater | OK |
| `team-conf-c12-012` | TCU Horned Frogs | TCU Horned Frogs | Fort Worth | TX |
| `team-conf-c12-013` | Texas Tech Red Raiders | Texas Tech Red Raiders | Lubbock | TX |
| `team-conf-c12-014` | UCF Knights | Central Florida Metro Knights | Orlando | FL |
| `team-conf-c12-015` | Utah Utes | Utah Utes | Salt Lake City | UT |
| `team-conf-c12-016` | West Virginia Mountaineers | West Virginia Highlanders | Morgantown | WV |

### Eastern Premier Conference

*Real league: Big East Conference* · `conf-epc` · **11 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-epc-001` | Butler Bulldogs | Butler Mastiffs | Indianapolis | IN |
| `team-conf-epc-002` | Creighton Bluejays | Creighton Bluejays | Omaha | NE |
| `team-conf-epc-003` | DePaul Blue Demons | DePaul Blue Demons | Chicago | IL |
| `team-conf-epc-004` | Georgetown Hoyas | Georgetown Hoyas | Washington | DC |
| `team-conf-epc-005` | Marquette Golden Eagles | Marquette Golden Eagles | Milwaukee | WI |
| `team-conf-epc-006` | Providence Friars | Providence Friars | Providence | RI |
| `team-conf-epc-007` | Seton Hall Pirates | Seton Hall Pirates | South Orange | NJ |
| `team-conf-epc-008` | St. John's Red Storm | Saint John's Red Storm | Queens | NY |
| `team-conf-epc-009` | UConn Huskies | Storrs Sled Dogs | Storrs | CT |
| `team-conf-epc-010` | Villanova Wildcats | Main Line Lynx | Villanova | PA |
| `team-conf-epc-011` | Xavier Musketeers | Xavier Musketeers | Cincinnati | OH |

### Northern Sky Conference

*Real league: Big Sky Conference* · `conf-nsc` · **11 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-nsc-001` | Eastern Washington Eagles | Eastern Washington Hawks | Cheney | WA |
| `team-conf-nsc-002` | Idaho Vandals | Idaho Vandals | Moscow | ID |
| `team-conf-nsc-003` | Idaho State Bengals | Idaho State Bengals | Pocatello | ID |
| `team-conf-nsc-004` | Montana Grizzlies | Montana Grizzlies | Missoula | MT |
| `team-conf-nsc-005` | Montana State Bobcats | Montana State Bobcats | Bozeman | MT |
| `team-conf-nsc-006` | Northern Arizona Lumberjacks | Northern Arizona Lumberjacks | Flagstaff | AZ |
| `team-conf-nsc-007` | Northern Colorado Bears | Northern Colorado Bruins | Greeley | CO |
| `team-conf-nsc-008` | Portland State Vikings | Portland State Vikings | Portland | OR |
| `team-conf-nsc-009` | Southern Utah Thunderbirds | Southern Utah Thunderbirds | Cedar City | UT |
| `team-conf-nsc-010` | Utah Tech Trailblazers | Utah Tech Trailblazers | St. George | UT |
| `team-conf-nsc-011` | Weber State Wildcats | Weber State Lynx | Ogden | UT |

### Piedmont South Conference

*Real league: Big South Conference* · `conf-psc` · **9 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-psc-001` | Charleston Southern Buccaneers | Charleston Southern Privateers | Charleston | SC |
| `team-conf-psc-002` | Gardner-Webb Runnin' Bulldogs | Gardner-Webb Runnin' Bulldogs | Boiling Springs | NC |
| `team-conf-psc-003` | High Point Panthers | High Point Pumas | High Point | NC |
| `team-conf-psc-004` | Longwood Lancers | Longwood Lancers | Farmville | VA |
| `team-conf-psc-005` | Presbyterian Blue Hose | Presbyterian Blue Hose | Clinton | SC |
| `team-conf-psc-006` | Radford Highlanders | Radford Highlanders | Radford | VA |
| `team-conf-psc-007` | UNC Asheville Bulldogs | North Cape Asheville Mastiffs | Asheville | NC |
| `team-conf-psc-008` | USC Upstate Spartans | Southern California Metro Upstate Hoplites | Spartanburg | SC |
| `team-conf-psc-009` | Winthrop Eagles | Winthrop Hawks | Rock Hill | SC |

### Great Ten Conference

*Real league: Big Ten Conference* · `conf-g10` · **18 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-g10-001` | Illinois Fighting Illini | Illinois Fighting Illini | Champaign | IL |
| `team-conf-g10-002` | Indiana Hoosiers | Indiana Hoosiers | Bloomington | IN |
| `team-conf-g10-003` | Iowa Hawkeyes | Iowa Hawkeyes | Iowa City | IA |
| `team-conf-g10-004` | Maryland Terrapins | Maryland Terrapins | College Park | MD |
| `team-conf-g10-005` | Michigan Wolverines | Michigan Wolverines | Ann Arbor | MI |
| `team-conf-g10-006` | Michigan State Spartans | Michigan State Hoplites | East Lansing | MI |
| `team-conf-g10-007` | Minnesota Golden Gophers | Minnesota Golden Gophers | Minneapolis | MN |
| `team-conf-g10-008` | Nebraska Cornhuskers | Nebraska Cornhuskers | Lincoln | NE |
| `team-conf-g10-009` | Northwestern Wildcats | Northwestern Lynx | Evanston | IL |
| `team-conf-g10-010` | Ohio State Buckeyes | Ohio State Buckeyes | Columbus | OH |
| `team-conf-g10-011` | Oregon Ducks | Oregon Ducks | Eugene | OR |
| `team-conf-g10-012` | Penn State Nittany Lions | Penn State Nittany Lions | University Park | PA |
| `team-conf-g10-013` | Purdue Boilermakers | Purdue Boilermakers | West Lafayette | IN |
| `team-conf-g10-014` | Rutgers Scarlet Knights | Rutgers Scarlet Knights | Piscataway | NJ |
| `team-conf-g10-015` | UCLA Bruins | Westwood Harbor Guardians | Los Angeles | CA |
| `team-conf-g10-016` | USC Trojans | South Bay Argonauts | Los Angeles | CA |
| `team-conf-g10-017` | Washington Huskies | Washington Huskies | Seattle | WA |
| `team-conf-g10-018` | Wisconsin Badgers | Wisconsin Badgers | Madison | WI |

### Golden West Conference

*Real league: Big West Conference* · `conf-gwc` · **12 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-gwc-001` | Cal Baptist Lancers | Cal Baptist Lancers | Riverside | CA |
| `team-conf-gwc-002` | Cal Poly Mustangs | Cal Poly Mustangs | San Luis Obispo | CA |
| `team-conf-gwc-003` | Cal State Bakersfield Roadrunners | Cal State Bakersfield Roadrunners | Bakersfield | CA |
| `team-conf-gwc-004` | Cal State Fullerton Titans | Cal State Fullerton Titans | Fullerton | CA |
| `team-conf-gwc-005` | Cal State Northridge Matadors (CSUN) | Cal State Northridge Matadors | Northridge | CA |
| `team-conf-gwc-006` | Long Beach State Beach | Long Beach State Beach | Long Beach | CA |
| `team-conf-gwc-007` | Sacramento State Hornets | Sacramento State Hornets | Sacramento | CA |
| `team-conf-gwc-008` | UC Irvine Anteaters | UC Irvine Anteaters | Irvine | CA |
| `team-conf-gwc-009` | UC Riverside Highlanders | UC Riverside Highlanders | Riverside | CA |
| `team-conf-gwc-010` | UC San Diego Tritons | UC San Diego Tritons | La Jolla | CA |
| `team-conf-gwc-011` | UC Santa Barbara Gauchos | UC Santa Barbara Gauchos | Santa Barbara | CA |
| `team-conf-gwc-012` | Utah Valley Wolverines | Utah Valley Wolverines | Orem | UT |

### Coastal Colonial Association

*Real league: Coastal Athletic Association (CAA)* · `conf-cca` · **13 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-cca-001` | Campbell Fighting Camels | Campbell Fighting Camels | Buies Creek | NC |
| `team-conf-cca-002` | College of Charleston Cougars | College of Charleston Cougars | Charleston | SC |
| `team-conf-cca-003` | Drexel Dragons | Drexel Dragons | Philadelphia | PA |
| `team-conf-cca-004` | Elon Phoenix | Elon Phoenix | Elon | NC |
| `team-conf-cca-005` | Hampton Pirates | Hampton Pirates | Hampton | VA |
| `team-conf-cca-006` | Hofstra Pride | Hofstra Pride | Hempstead | NY |
| `team-conf-cca-007` | Monmouth Hawks | Monmouth Falcons | West Long Branch | NJ |
| `team-conf-cca-008` | North Carolina A&T Aggies | North Carolina A&T Aggies | Greensboro | NC |
| `team-conf-cca-009` | Northeastern Huskies | Northeastern Huskies | Boston | MA |
| `team-conf-cca-010` | Stony Brook Seawolves | Stony Brook Seawolves | Stony Brook | NY |
| `team-conf-cca-011` | Towson Tigers | Towson Cougars | Towson | MD |
| `team-conf-cca-012` | UNC Wilmington Seahawks (UNCW) | North Cape Wilmington Seahawks | Wilmington | NC |
| `team-conf-cca-013` | William & Mary Tribe | William & Mary Tribe | Williamsburg | VA |

### United Continental Conference

*Real league: Conference USA (C-USA)* · `conf-ucc` · **10 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-ucc-001` | Delaware Fightin' Blue Hens | Delaware Fightin' Blue Hens | Newark | DE |
| `team-conf-ucc-002` | FIU Panthers | Miami-Dade Metro Pumas | Miami | FL |
| `team-conf-ucc-003` | Jacksonville State Gamecocks | Jacksonville State Gamecocks | Jacksonville | AL |
| `team-conf-ucc-004` | Kennesaw State Owls | Kennesaw State Owls | Kennesaw | GA |
| `team-conf-ucc-005` | Liberty Flames | Liberty Flames | Lynchburg | VA |
| `team-conf-ucc-006` | Middle Tennessee Blue Raiders (MTSU) | Middle Tennessee Blue Raiders | Murfreesboro | TN |
| `team-conf-ucc-007` | Missouri State Bears | Missouri State Bruins | Springfield | MO |
| `team-conf-ucc-008` | New Mexico State Aggies | New Mexico State Aggies | Las Cruces | NM |
| `team-conf-ucc-009` | Sam Houston Bearkats | Sam Houston Bearkats | Huntsville | TX |
| `team-conf-ucc-010` | Western Kentucky Hilltoppers (WKU) | Western Kentucky Hilltoppers | Bowling Green | KY |

### Lakefront Horizon League

*Real league: Horizon League* · `conf-lhl` · **12 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-lhl-001` | Cleveland State Vikings | Cleveland State Vikings | Cleveland | OH |
| `team-conf-lhl-002` | Detroit Mercy Titans | Detroit Mercy Titans | Detroit | MI |
| `team-conf-lhl-003` | Green Bay Phoenix | Green Bay Phoenix | Green Bay | WI |
| `team-conf-lhl-004` | IU Indy Jaguars | IU Indy Jaguars | Indianapolis | IN |
| `team-conf-lhl-005` | Milwaukee Panthers | Milwaukee Pumas | Milwaukee | WI |
| `team-conf-lhl-006` | Northern Illinois Huskies (NIU) | Northern Illinois Huskies | DeKalb | IL |
| `team-conf-lhl-007` | Northern Kentucky Norse | Northern Kentucky Norse | Highland Heights | KY |
| `team-conf-lhl-008` | Oakland Golden Grizzlies | Oakland Golden Grizzlies | Rochester | MI |
| `team-conf-lhl-009` | Purdue Fort Wayne Mastodons | Purdue Fort Wayne Mastodons | Fort Wayne | IN |
| `team-conf-lhl-010` | Robert Morris Colonials | Robert Morris Colonials | Moon Township | PA |
| `team-conf-lhl-011` | Wright State Raiders | Wright State Rangers | Dayton | OH |
| `team-conf-lhl-012` | Youngstown State Penguins | Youngstown State Penguins | Youngstown | OH |

### Founding Eight League

*Real league: Ivy League* · `conf-fel` · **8 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-fel-001` | Brown Bears | Brown Bruins | Providence | RI |
| `team-conf-fel-002` | Columbia Lions | Columbia Monarchs | New York | NY |
| `team-conf-fel-003` | Cornell Big Red | Cornell Big Red | Ithaca | NY |
| `team-conf-fel-004` | Dartmouth Big Green | Dartmouth Big Green | Hanover | NH |
| `team-conf-fel-005` | Harvard Crimson | Harvard Crimson | Cambridge | MA |
| `team-conf-fel-006` | Penn Quakers | Penn Quakers | Philadelphia | PA |
| `team-conf-fel-007` | Princeton Tigers | Princeton Cougars | Princeton | NJ |
| `team-conf-fel-008` | Yale Bulldogs | Yale Mastiffs | New Haven | CT |

### Metro Collegiate Conference

*Real league: Metro Conference (formerly MAAC)* · `conf-metro` · **13 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-metro-001` | Canisius Golden Griffins | Canisius Golden Griffins | Buffalo | NY |
| `team-conf-metro-002` | Fairfield Stags | Fairfield Stags | Fairfield | CT |
| `team-conf-metro-003` | Iona Gaels | Iona Gaels | New Rochelle | NY |
| `team-conf-metro-004` | Manhattan Jaspers | Manhattan Jaspers | Riverdale | NY |
| `team-conf-metro-005` | Marist Red Foxes | Marist Red Foxes | Poughkeepsie | NY |
| `team-conf-metro-006` | Merrimack Warriors | Merrimack Sentinels | North Andover | MA |
| `team-conf-metro-007` | Mount St. Mary's Mountaineers | Mount Saint Mary's Highlanders | Emmitsburg | MD |
| `team-conf-metro-008` | Niagara Purple Eagles | Niagara Purple Eagles | Niagara University | NY |
| `team-conf-metro-009` | Quinnipiac Bobcats | Quinnipiac Bobcats | Hamden | CT |
| `team-conf-metro-010` | Rider Broncs | Rider Broncs | Lawrenceville | NJ |
| `team-conf-metro-011` | Sacred Heart Pioneers | Sacred Heart Pioneers | Fairfield | CT |
| `team-conf-metro-012` | Saint Peter's Peacocks | Saint Peter's Peacocks | Jersey City | NJ |
| `team-conf-metro-013` | Siena Saints | Siena Saints | Loudonville | NY |

### Midland American Circuit

*Real league: Mid-American Conference (MAC)* · `conf-mac` · **12 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-mac-001` | Akron Zips | Akron Zips | Akron | OH |
| `team-conf-mac-002` | Ball State Cardinals | Ball State Scarlets | Muncie | IN |
| `team-conf-mac-003` | Bowling Green Falcons | Bowling Green Falcons | Bowling Green | OH |
| `team-conf-mac-004` | Buffalo Bulls | Buffalo Stampede | Buffalo | NY |
| `team-conf-mac-005` | Central Michigan Chippewas | Central Michigan Chippewas | Mount Pleasant | MI |
| `team-conf-mac-006` | Eastern Michigan Eagles | Eastern Michigan Hawks | Ypsilanti | MI |
| `team-conf-mac-007` | Kent State Golden Flashes | Kent State Golden Flashes | Kent | OH |
| `team-conf-mac-008` | Miami (OH) RedHawks | Miami RedHawks | Oxford | OH |
| `team-conf-mac-009` | Ohio Bobcats | Ohio Bobcats | Athens | OH |
| `team-conf-mac-010` | Toledo Rockets | Toledo Rockets | Toledo | OH |
| `team-conf-mac-011` | UMass Minutemen | Bay State Minutemen | Amherst | MA |
| `team-conf-mac-012` | Western Michigan Broncos | Western Michigan Broncos | Kalamazoo | MI |

### Mid-Eastern Alliance Conference

*Real league: Mid-Eastern Athletic Conference (MEAC)* · `conf-meac` · **8 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-meac-001` | Coppin State Eagles | Coppin State Hawks | Baltimore | MD |
| `team-conf-meac-002` | Delaware State Hornets | Delaware State Hornets | Dover | DE |
| `team-conf-meac-003` | Howard Bison | Howard Bison | Washington | DC |
| `team-conf-meac-004` | Maryland Eastern Shore Hawks (UMES) | Maryland Eastern Shore Falcons | Princess Anne | MD |
| `team-conf-meac-005` | Morgan State Bears | Morgan State Bruins | Baltimore | MD |
| `team-conf-meac-006` | Norfolk State Spartans | Norfolk State Hoplites | Norfolk | VA |
| `team-conf-meac-007` | North Carolina Central Eagles | North Carolina Central Hawks | Durham | NC |
| `team-conf-meac-008` | South Carolina State Bulldogs | South Carolina State Mastiffs | Orangeburg | SC |

### Prairie Valley Conference

*Real league: Missouri Valley Conference (MVC)* · `conf-pvc` · **11 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-pvc-001` | Belmont Bruins | Belmont Bruins | Nashville | TN |
| `team-conf-pvc-002` | Bradley Braves | Bradley Braves | Peoria | IL |
| `team-conf-pvc-003` | Drake Bulldogs | Drake Mastiffs | Des Moines | IA |
| `team-conf-pvc-004` | Evansville Purple Aces | Evansville Purple Aces | Evansville | IN |
| `team-conf-pvc-005` | Illinois State Redbirds | Illinois State Redbirds | Normal | IL |
| `team-conf-pvc-006` | Indiana State Sycamores | Indiana State Sycamores | Terre Haute | IN |
| `team-conf-pvc-007` | Murray State Racers | Murray State Racers | Murray | KY |
| `team-conf-pvc-008` | Northern Iowa Panthers (UNI) | Northern Iowa Pumas | Cedar Falls | IA |
| `team-conf-pvc-009` | Southern Illinois Salukis | Southern Illinois Salukis | Carbondale | IL |
| `team-conf-pvc-010` | UIC Flames | UIC Flames | Chicago | IL |
| `team-conf-pvc-011` | Valparaiso Beacons | Valparaiso Beacons | Valparaiso | IN |

### Rocky Mountain West Conference

*Real league: Mountain West Conference (MW)* · `conf-rmwc` · **10 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-rmwc-001` | Air Force Falcons | Air Force Falcons | Colorado Springs | CO |
| `team-conf-rmwc-002` | Grand Canyon Antelopes | Grand Canyon Antelopes | Phoenix | AZ |
| `team-conf-rmwc-003` | Hawaiʻi Rainbow Warriors | Honolulu Rainbow Guard | Honolulu | HI |
| `team-conf-rmwc-004` | Nevada Wolf Pack | Nevada Wolf Pack | Reno | NV |
| `team-conf-rmwc-005` | New Mexico Lobos | New Mexico Lobos | Albuquerque | NM |
| `team-conf-rmwc-006` | San José State Spartans | San José State Hoplites | San Jose | CA |
| `team-conf-rmwc-007` | UC Davis Aggies | UC Davis Aggies | Davis | CA |
| `team-conf-rmwc-008` | UNLV Runnin' Rebels | Las Vegas Metro Runnin' Rebels | Las Vegas | NV |
| `team-conf-rmwc-009` | UTEP Miners | El Paso Metro Miners | El Paso | TX |
| `team-conf-rmwc-010` | Wyoming Cowboys | Wyoming Cowboys | Laramie | WY |

### Northeastern Exchange Conference

*Real league: Northeast Conference (NEC)* · `conf-nec` · **7 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-nec-001` | Central Connecticut Blue Devils | Central Connecticut Blue Devils | New Britain | CT |
| `team-conf-nec-002` | Chicago State Cougars | Chicago State Cougars | Chicago | IL |
| `team-conf-nec-003` | Fairleigh Dickinson Knights (FDU) | Fairleigh Dickinson Knights | Teaneck | NJ |
| `team-conf-nec-004` | Le Moyne Dolphins | Le Moyne Dolphins | Syracuse | NY |
| `team-conf-nec-005` | LIU Sharks | LIU Sharks | Brooklyn | NY |
| `team-conf-nec-006` | Stonehill Skyhawks | Stonehill Skyhawks | Easton | MA |
| `team-conf-nec-007` | Wagner Seahawks | Wagner Seahawks | Staten Island | NY |

### Ohio Valley Alliance

*Real league: Ohio Valley Conference (OVC)* · `conf-ova` · **9 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-ova-001` | Eastern Illinois Panthers | Eastern Illinois Pumas | Charleston | IL |
| `team-conf-ova-002` | Lindenwood Lions | Lindenwood Monarchs | St. Charles | MO |
| `team-conf-ova-003` | Morehead State Eagles | Morehead State Hawks | Morehead | KY |
| `team-conf-ova-004` | SIU Edwardsville Cougars (SIUE) | SIU Edwardsville Cougars | Edwardsville | IL |
| `team-conf-ova-005` | Southeast Missouri State Redhawks (SEMO) | Southeast Missouri State Redhawks | Cape Girardeau | MO |
| `team-conf-ova-006` | Southern Indiana Screaming Eagles | Southern Indiana Screaming Hawks | Evansville | IN |
| `team-conf-ova-007` | Tennessee State Tigers | Tennessee State Cougars | Nashville | TN |
| `team-conf-ova-008` | UT Martin Skyhawks | UT Martin Skyhawks | Martin | TN |
| `team-conf-ova-009` | Western Illinois Leathernecks | Western Illinois Leathernecks | Macomb | IL |

### Pacific Rim Twelve

*Real league: Pac-12 Conference* · `conf-pr12` · **9 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-pr12-001` | Boise State Broncos | Boise State Broncos | Boise | ID |
| `team-conf-pr12-002` | Colorado State Rams | Colorado State Rams | Fort Collins | CO |
| `team-conf-pr12-003` | Fresno State Bulldogs | Fresno State Mastiffs | Fresno | CA |
| `team-conf-pr12-004` | Gonzaga Bulldogs | Spokane River Mastiffs | Spokane | WA |
| `team-conf-pr12-005` | Oregon State Beavers | Oregon State Beavers | Corvallis | OR |
| `team-conf-pr12-006` | San Diego State Aztecs | San Diego State Aztecs | San Diego | CA |
| `team-conf-pr12-007` | Texas State Bobcats | Texas State Bobcats | San Marcos | TX |
| `team-conf-pr12-008` | Utah State Aggies | Utah State Aggies | Logan | UT |
| `team-conf-pr12-009` | Washington State Cougars | Washington State Cougars | Pullman | WA |

### Patriot Scholar League

*Real league: Patriot League* · `conf-psl` · **10 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-psl-001` | American Eagles | American Hawks | Washington | DC |
| `team-conf-psl-002` | Army Black Knights | Army Black Knights | West Point | NY |
| `team-conf-psl-003` | Boston University Terriers | Boston University Terriers | Boston | MA |
| `team-conf-psl-004` | Bucknell Bison | Bucknell Bison | Lewisburg | PA |
| `team-conf-psl-005` | Colgate Raiders | Colgate Rangers | Hamilton | NY |
| `team-conf-psl-006` | Holy Cross Crusaders | Holy Cross Crusaders | Worcester | MA |
| `team-conf-psl-007` | Lafayette Leopards | Lafayette Leopards | Easton | PA |
| `team-conf-psl-008` | Lehigh Mountain Hawks | Lehigh Mountain Hawks | Bethlehem | PA |
| `team-conf-psl-009` | Loyola Maryland Greyhounds | Loyola Maryland Greyhounds | Baltimore | MD |
| `team-conf-psl-010` | Navy Midshipmen | Navy Midshipmen | Annapolis | MD |

### Southeastern Dominion Conference

*Real league: Southeastern Conference (SEC)* · `conf-sedc` · **16 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-sedc-001` | Alabama Crimson Tide | Alabama Crimson Tide | Tuscaloosa | AL |
| `team-conf-sedc-002` | Arkansas Razorbacks | Arkansas Razorbacks | Fayetteville | AR |
| `team-conf-sedc-003` | Auburn Tigers | Auburn Cougars | Auburn | AL |
| `team-conf-sedc-004` | Florida Gators | Florida Gators | Gainesville | FL |
| `team-conf-sedc-005` | Georgia Bulldogs | Georgia Mastiffs | Athens | GA |
| `team-conf-sedc-006` | Kentucky Wildcats | Bluegrass Commonwealth Lynx | Lexington | KY |
| `team-conf-sedc-007` | LSU Tigers | LSU Cougars | Baton Rouge | LA |
| `team-conf-sedc-008` | Mississippi State Bulldogs | Mississippi State Mastiffs | Starkville | MS |
| `team-conf-sedc-009` | Missouri Tigers | Missouri Cougars | Columbia | MO |
| `team-conf-sedc-010` | Oklahoma Sooners | Oklahoma Sooners | Norman | OK |
| `team-conf-sedc-011` | Ole Miss Rebels | Ole Miss Rebels | Oxford | MS |
| `team-conf-sedc-012` | South Carolina Gamecocks | South Carolina Gamecocks | Columbia | SC |
| `team-conf-sedc-013` | Tennessee Volunteers | Tennessee Volunteers | Knoxville | TN |
| `team-conf-sedc-014` | Texas Longhorns | Texas Longhorns | Austin | TX |
| `team-conf-sedc-015` | Texas A&M Aggies | Texas A&M Aggies | College Station | TX |
| `team-conf-sedc-016` | Vanderbilt Commodores | Vanderbilt Commodores | Nashville | TN |

### Southern Collegiate Federation

*Real league: Southern Conference (SoCon)* · `conf-scf` · **11 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-scf-001` | Chattanooga Mocs | Chattanooga Mocs | Chattanooga | TN |
| `team-conf-scf-002` | East Tennessee State Buccaneers (ETSU) | East Tennessee State Privateers | Johnson City | TN |
| `team-conf-scf-003` | Furman Paladins | Furman Paladins | Greenville | SC |
| `team-conf-scf-004` | Mercer Bears | Mercer Bruins | Macon | GA |
| `team-conf-scf-005` | Samford Bulldogs | Samford Mastiffs | Birmingham | AL |
| `team-conf-scf-006` | Tennessee Tech Golden Eagles | Tennessee Tech Golden Eagles | Cookeville | TN |
| `team-conf-scf-007` | The Citadel Bulldogs | The Citadel Mastiffs | Charleston | SC |
| `team-conf-scf-008` | UNC Greensboro Spartans (UNCG) | North Cape Greensboro Hoplites | Greensboro | NC |
| `team-conf-scf-009` | VMI Keydets | VMI Keydets | Lexington | VA |
| `team-conf-scf-010` | Western Carolina Catamounts | Western Carolina Catamounts | Cullowhee | NC |
| `team-conf-scf-011` | Wofford Terriers | Wofford Terriers | Spartanburg | SC |

### Gulf Coastland Conference

*Real league: Southland Conference* · `conf-gcc` · **12 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-gcc-001` | East Texas A&M Lions | East Texas A&M Monarchs | Commerce | TX |
| `team-conf-gcc-002` | Houston Christian Huskies | Houston Christian Huskies | Houston | TX |
| `team-conf-gcc-003` | Incarnate Word Cardinals (UIW) | Incarnate Word Scarlets | San Antonio | TX |
| `team-conf-gcc-004` | Lamar Cardinals | Lamar Scarlets | Beaumont | TX |
| `team-conf-gcc-005` | McNeese Cowboys | McNeese Cowboys | Lake Charles | LA |
| `team-conf-gcc-006` | New Orleans Privateers | New Orleans Privateers | New Orleans | LA |
| `team-conf-gcc-007` | Nicholls Colonels | Nicholls Colonels | Thibodaux | LA |
| `team-conf-gcc-008` | Northwestern State Demons | Northwestern State Demons | Natchitoches | LA |
| `team-conf-gcc-009` | Southeastern Louisiana Lions | Southeastern Louisiana Monarchs | Hammond | LA |
| `team-conf-gcc-010` | Stephen F. Austin Lumberjacks (SFA) | Stephen F. Austin Lumberjacks | Nacogdoches | TX |
| `team-conf-gcc-011` | Texas A&M-Corpus Christi Islanders | Texas A&M-Corpus Christi Islanders | Corpus Christi | TX |
| `team-conf-gcc-012` | UT Rio Grande Valley Vaqueros (UTRGV) | UT Rio Grande Valley Vaqueros | Edinburg | TX |

### Southwest Collegiate Athletic League

*Real league: Southwestern Athletic Conference (SWAC)* · `conf-scal` · **12 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-scal-001` | Alabama A&M Bulldogs | Alabama A&M Mastiffs | Normal | AL |
| `team-conf-scal-002` | Alabama State Hornets | Alabama State Hornets | Montgomery | AL |
| `team-conf-scal-003` | Alcorn State Braves | Alcorn State Braves | Lorman | MS |
| `team-conf-scal-004` | Arkansas-Pine Bluff Golden Lions (UAPB) | Arkansas-Pine Bluff Golden Lions | Pine Bluff | AR |
| `team-conf-scal-005` | Bethune-Cookman Wildcats | Bethune-Cookman Lynx | Daytona Beach | FL |
| `team-conf-scal-006` | Florida A&M Rattlers (FAMU) | Florida A&M Rattlers | Tallahassee | FL |
| `team-conf-scal-007` | Grambling State Tigers | Grambling State Cougars | Grambling | LA |
| `team-conf-scal-008` | Jackson State Tigers | Jackson State Cougars | Jackson | MS |
| `team-conf-scal-009` | Mississippi Valley State Delta Devils | Mississippi Valley State Delta Devils | Itta Bena | MS |
| `team-conf-scal-010` | Prairie View A&M Panthers | Prairie View A&M Pumas | Prairie View | TX |
| `team-conf-scal-011` | Southern Jaguars | Southern Jaguars | Baton Rouge | LA |
| `team-conf-scal-012` | Texas Southern Tigers | Texas Southern Cougars | Houston | TX |

### Prairie Summit League

*Real league: Summit League* · `conf-summit` · **8 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-summit-001` | Kansas City Roos | Kansas City Roos | Kansas City | MO |
| `team-conf-summit-002` | North Dakota Fighting Hawks | North Dakota Fighting Hawks | Grand Forks | ND |
| `team-conf-summit-003` | North Dakota State Bison | North Dakota State Bison | Fargo | ND |
| `team-conf-summit-004` | Omaha Mavericks | Omaha Mavericks | Omaha | NE |
| `team-conf-summit-005` | Oral Roberts Golden Eagles | Oral Roberts Golden Eagles | Tulsa | OK |
| `team-conf-summit-006` | South Dakota Coyotes | South Dakota Coyotes | Vermillion | SD |
| `team-conf-summit-007` | South Dakota State Jackrabbits | South Dakota State Jackrabbits | Brookings | SD |
| `team-conf-summit-008` | St. Thomas Tommies | Saint Thomas Tommies | St. Paul | MN |

### Solar Belt Conference

*Real league: Sun Belt Conference* · `conf-sbc` · **14 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-sbc-001` | Appalachian State Mountaineers | Appalachian State Highlanders | Boone | NC |
| `team-conf-sbc-002` | Arkansas State Red Wolves | Arkansas State Red Wolves | Jonesboro | AR |
| `team-conf-sbc-003` | Coastal Carolina Chanticleers | Coastal Carolina Chanticleers | Conway | SC |
| `team-conf-sbc-004` | Georgia Southern Eagles | Georgia Southern Hawks | Statesboro | GA |
| `team-conf-sbc-005` | Georgia State Panthers | Georgia State Pumas | Atlanta | GA |
| `team-conf-sbc-006` | James Madison Dukes (JMU) | James Madison Dukes | Harrisonburg | VA |
| `team-conf-sbc-007` | Louisiana Ragin' Cajuns | Louisiana Ragin' Cajuns | Lafayette | LA |
| `team-conf-sbc-008` | Louisiana-Monroe Warhawks (ULM) | Louisiana-Monroe Warhawks | Monroe | LA |
| `team-conf-sbc-009` | Louisiana Tech Bulldogs | Louisiana Tech Mastiffs | Ruston | LA |
| `team-conf-sbc-010` | Marshall Thundering Herd | Marshall Thundering Herd | Huntington | WV |
| `team-conf-sbc-011` | Old Dominion Monarchs (ODU) | Old Dominion Monarchs | Norfolk | VA |
| `team-conf-sbc-012` | South Alabama Jaguars | South Alabama Jaguars | Mobile | AL |
| `team-conf-sbc-013` | Southern Miss Golden Eagles | Southern Miss Golden Eagles | Hattiesburg | MS |
| `team-conf-sbc-014` | Troy Trojans | Troy Trojans | Troy | AL |

### United Athletic Collegiate

*Real league: United Athletic Conference (UAC) (formerly WAC)* · `conf-uac` · **8 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-uac-001` | Abilene Christian Wildcats | Abilene Christian Lynx | Abilene | TX |
| `team-conf-uac-002` | Austin Peay Governors | Austin Peay Governors | Clarksville | TN |
| `team-conf-uac-003` | Central Arkansas Bears | Central Arkansas Bruins | Conway | AR |
| `team-conf-uac-004` | Eastern Kentucky Colonels | Eastern Kentucky Colonels | Richmond | KY |
| `team-conf-uac-005` | Little Rock Trojans | Little Rock Trojans | Little Rock | AR |
| `team-conf-uac-006` | North Alabama Lions | North Alabama Monarchs | Florence | AL |
| `team-conf-uac-007` | Tarleton State Texans | Tarleton State Texans | Stephenville | TX |
| `team-conf-uac-008` | UT Arlington Mavericks | UT Arlington Mavericks | Arlington | TX |

### Western Coast Collegiate

*Real league: West Coast Conference (WCC)* · `conf-wcc` · **10 teams**

| `teamId` | Real (reference) | Universe (default pack) | `homeCity` | `homeState` |
|----------|-------------------|-------------------------|------------|-------------|
| `team-conf-wcc-001` | Denver Pioneers | Denver Pioneers | Denver | CO |
| `team-conf-wcc-002` | Loyola Marymount Lions (LMU) | Loyola Marymount Monarchs | Los Angeles | CA |
| `team-conf-wcc-003` | Pacific Tigers | Pacific Cougars | Stockton | CA |
| `team-conf-wcc-004` | Pepperdine Waves | Pepperdine Waves | Malibu | CA |
| `team-conf-wcc-005` | Portland Pilots | Portland Pilots | Portland | OR |
| `team-conf-wcc-006` | Saint Mary's Gaels | Saint Mary's Gaels | Moraga | CA |
| `team-conf-wcc-007` | San Diego Toreros | San Diego Toreros | San Diego | CA |
| `team-conf-wcc-008` | San Francisco Dons | San Francisco Dons | San Francisco | CA |
| `team-conf-wcc-009` | Santa Clara Broncos | Santa Clara Broncos | Santa Clara | CA |
| `team-conf-wcc-010` | Seattle U Redhawks | Seattle U Redhawks | Seattle | WA |

---

## Geography & recruiting

| Rule | Detail |
|------|--------|
| **`homeCity`** | Campus city on every program; pack + save field; schedule/travel/display |
| **`homeState`** | US state / DC; **recruiting pipeline** (in-state / regional bias on recruits) |
| **Recruits** | `homeState` (and city when seeded) for geography; pipeline uses **state** |
| **Colors / logos** | Pack overlay; separate from city/state |

---

## Provisional / transition programs

Omitted from the **361** certified default pack. Still scheduled in the real **365** field per directory; **dynasty inclusion TBD**.

| Conference | Real (reference) | Transition status |
|------------|------------------|-------------------|
| Northeast Conference (NEC) (`conf-nec`) | Mercyhurst Lakers | Mid-transition (Year 3) |
| Northeast Conference (NEC) (`conf-nec`) | New Haven Chargers | Early-transition (Year 2) |
| Atlantic Sun Conference (ASUN) (`conf-ascu`) | West Florida Argonauts | Early-transition (Year 1) |
| United Athletic Conference (UAC) (formerly WAC) (`conf-uac`) | West Georgia Wolves | Mid-transition (Year 3) |

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
| **`homeCity` / `homeState`** | Required on every team in default pack JSON |
| **Colors & logos** | Pack JSON overlay |
| **Realignment** | Metro (ex-MAAC), UAC (ex-WAC), Pac-12 remnant, MW + UC Davis, Big West + Utah Valley, etc. reflected in this snapshot |
| **Source of truth (reference)** | [`2026-NCAA-Directory.md`](2026-NCAA-Directory.md) — certification tier copy |
| **Regeneration** | `python3 planning/tools/generate_league_branding.py` after membership edits |

**Certified pack count:** 361 teams · 32 conferences · +4 transition (excluded).

---

## Document history

| Date | Change |
|------|--------|
| 2025-11-08 | Initial conference + team mapping |
| 2025-11-08 | **Rebuild:** 2026–27 D-I list — 32 conferences, Metro/UAC, membership moves |
| 2025-11-08 | Geography: `homeState` (recruiting) + `homeCity` on all programs |
| 2025-11-08 | Align 361 certified; link directory; exclude 4 transition programs |
