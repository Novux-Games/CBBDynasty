"""Campus geography per team (state + city), parallel to MEMBERSHIP order in generate_league_branding.py."""

# Each entry: (homeState, homeCity) — real campus city for reference pack; universe display separate.
GEO_BY_CONF: dict[str, list[tuple[str, str]]] = {
    "conf-nac": [
        ("NY", "Albany"), ("NY", "Binghamton"), ("RI", "Smithfield"), ("ME", "Orono"),
        ("NH", "Durham"), ("NJ", "Newark"), ("MD", "Baltimore"), ("MA", "Lowell"), ("VT", "Burlington"),
    ],
    "conf-aucc": [
        ("NC", "Charlotte"), ("NC", "Greenville"), ("FL", "Boca Raton"), ("TN", "Memphis"),
        ("TX", "Denton"), ("TX", "Houston"), ("FL", "Tampa"), ("PA", "Philadelphia"),
        ("LA", "New Orleans"), ("OK", "Tulsa"), ("AL", "Birmingham"), ("TX", "San Antonio"), ("KS", "Wichita"),
    ],
    "conf-cat10": [
        ("NC", "Davidson"), ("OH", "Dayton"), ("PA", "Pittsburgh"), ("NY", "Bronx"),
        ("VA", "Fairfax"), ("DC", "Washington"), ("PA", "Philadelphia"), ("IL", "Chicago"),
        ("RI", "Kingston"), ("VA", "Richmond"), ("PA", "Philadelphia"), ("MO", "St. Louis"),
        ("NY", "St. Bonaventure"), ("VA", "Richmond"),
    ],
    "conf-acrown": [
        ("MA", "Chestnut Hill"), ("CA", "Berkeley"), ("SC", "Clemson"), ("NC", "Durham"),
        ("FL", "Tallahassee"), ("GA", "Atlanta"), ("KY", "Louisville"), ("FL", "Coral Gables"),
        ("NC", "Raleigh"), ("NC", "Chapel Hill"), ("IN", "Notre Dame"), ("PA", "Pittsburgh"),
        ("TX", "Dallas"), ("CA", "Stanford"), ("NY", "Syracuse"), ("VA", "Charlottesville"),
        ("VA", "Blacksburg"), ("NC", "Winston-Salem"),
    ],
    "conf-ascu": [
        ("KY", "Louisville"), ("FL", "Fort Myers"), ("FL", "Jacksonville"), ("TN", "Nashville"),
        ("FL", "Jacksonville"), ("NC", "Charlotte"), ("FL", "DeLand"),
    ],
    "conf-c12": [
        ("AZ", "Tucson"), ("AZ", "Tempe"), ("TX", "Waco"), ("UT", "Provo"),
        ("OH", "Cincinnati"), ("CO", "Boulder"), ("TX", "Houston"), ("IA", "Ames"),
        ("KS", "Lawrence"), ("KS", "Manhattan"), ("OK", "Stillwater"), ("TX", "Fort Worth"),
        ("TX", "Lubbock"), ("FL", "Orlando"), ("UT", "Salt Lake City"), ("WV", "Morgantown"),
    ],
    "conf-epc": [
        ("IN", "Indianapolis"), ("NE", "Omaha"), ("IL", "Chicago"), ("DC", "Washington"),
        ("WI", "Milwaukee"), ("RI", "Providence"), ("NJ", "South Orange"), ("NY", "Queens"),
        ("CT", "Storrs"), ("PA", "Villanova"), ("OH", "Cincinnati"),
    ],
    "conf-nsc": [
        ("WA", "Cheney"), ("ID", "Moscow"), ("ID", "Pocatello"), ("MT", "Missoula"),
        ("MT", "Bozeman"), ("AZ", "Flagstaff"), ("CO", "Greeley"), ("OR", "Portland"),
        ("UT", "Cedar City"), ("UT", "St. George"), ("UT", "Ogden"),
    ],
    "conf-psc": [
        ("SC", "Charleston"), ("NC", "Boiling Springs"), ("NC", "High Point"), ("VA", "Farmville"),
        ("SC", "Clinton"), ("VA", "Radford"), ("NC", "Asheville"), ("SC", "Spartanburg"), ("SC", "Rock Hill"),
    ],
    "conf-g10": [
        ("IL", "Champaign"), ("IN", "Bloomington"), ("IA", "Iowa City"), ("MD", "College Park"),
        ("MI", "Ann Arbor"), ("MI", "East Lansing"), ("MN", "Minneapolis"), ("NE", "Lincoln"),
        ("IL", "Evanston"), ("OH", "Columbus"), ("OR", "Eugene"), ("PA", "University Park"),
        ("IN", "West Lafayette"), ("NJ", "Piscataway"), ("CA", "Los Angeles"), ("CA", "Los Angeles"),
        ("WA", "Seattle"), ("WI", "Madison"),
    ],
    "conf-gwc": [
        ("CA", "Riverside"), ("CA", "San Luis Obispo"), ("CA", "Bakersfield"), ("CA", "Fullerton"),
        ("CA", "Northridge"), ("CA", "Long Beach"), ("CA", "Sacramento"), ("CA", "Irvine"),
        ("CA", "Riverside"), ("CA", "La Jolla"), ("CA", "Santa Barbara"), ("UT", "Orem"),
    ],
    "conf-cca": [
        ("NC", "Buies Creek"), ("SC", "Charleston"), ("PA", "Philadelphia"), ("NC", "Elon"),
        ("VA", "Hampton"), ("NY", "Hempstead"), ("NJ", "West Long Branch"), ("NC", "Greensboro"),
        ("MA", "Boston"), ("NY", "Stony Brook"), ("MD", "Towson"), ("NC", "Wilmington"), ("VA", "Williamsburg"),
    ],
    "conf-ucc": [
        ("DE", "Newark"), ("FL", "Miami"), ("AL", "Jacksonville"), ("GA", "Kennesaw"),
        ("VA", "Lynchburg"), ("TN", "Murfreesboro"), ("MO", "Springfield"), ("NM", "Las Cruces"),
        ("TX", "Huntsville"), ("KY", "Bowling Green"),
    ],
    "conf-lhl": [
        ("OH", "Cleveland"), ("MI", "Detroit"), ("WI", "Green Bay"), ("IN", "Indianapolis"),
        ("WI", "Milwaukee"), ("IL", "DeKalb"), ("KY", "Highland Heights"), ("MI", "Rochester"),
        ("IN", "Fort Wayne"), ("PA", "Moon Township"), ("OH", "Dayton"), ("OH", "Youngstown"),
    ],
    "conf-fel": [
        ("RI", "Providence"), ("NY", "New York"), ("NY", "Ithaca"), ("NH", "Hanover"),
        ("MA", "Cambridge"), ("PA", "Philadelphia"), ("NJ", "Princeton"), ("CT", "New Haven"),
    ],
    "conf-metro": [
        ("NY", "Buffalo"), ("CT", "Fairfield"), ("NY", "New Rochelle"), ("NY", "Riverdale"),
        ("NY", "Poughkeepsie"), ("MA", "North Andover"), ("MD", "Emmitsburg"), ("NY", "Niagara University"),
        ("CT", "Hamden"), ("NJ", "Lawrenceville"), ("CT", "Fairfield"), ("NJ", "Jersey City"),
        ("NY", "Loudonville"),
    ],
    "conf-mac": [
        ("OH", "Akron"), ("IN", "Muncie"), ("OH", "Bowling Green"), ("NY", "Buffalo"),
        ("MI", "Mount Pleasant"), ("MI", "Ypsilanti"), ("OH", "Kent"), ("OH", "Oxford"),
        ("OH", "Athens"), ("OH", "Toledo"), ("MA", "Amherst"), ("MI", "Kalamazoo"),
    ],
    "conf-meac": [
        ("MD", "Baltimore"), ("DE", "Dover"), ("DC", "Washington"), ("MD", "Princess Anne"),
        ("MD", "Baltimore"), ("VA", "Norfolk"), ("NC", "Durham"), ("SC", "Orangeburg"),
    ],
    "conf-pvc": [
        ("TN", "Nashville"), ("IL", "Peoria"), ("IA", "Des Moines"), ("IN", "Evansville"),
        ("IL", "Normal"), ("IN", "Terre Haute"), ("KY", "Murray"), ("IA", "Cedar Falls"),
        ("IL", "Carbondale"), ("IL", "Chicago"), ("IN", "Valparaiso"),
    ],
    "conf-rmwc": [
        ("CO", "Colorado Springs"), ("AZ", "Phoenix"), ("HI", "Honolulu"), ("NV", "Reno"),
        ("NM", "Albuquerque"), ("CA", "San Jose"), ("CA", "Davis"), ("NV", "Las Vegas"),
        ("TX", "El Paso"), ("WY", "Laramie"),
    ],
    "conf-nec": [
        ("CT", "New Britain"), ("IL", "Chicago"), ("NJ", "Teaneck"), ("NY", "Syracuse"),
        ("NY", "Brooklyn"), ("MA", "Easton"), ("NY", "Staten Island"),
    ],
    "conf-ova": [
        ("IL", "Charleston"), ("MO", "St. Charles"), ("KY", "Morehead"), ("IL", "Edwardsville"),
        ("MO", "Cape Girardeau"), ("IN", "Evansville"), ("TN", "Nashville"), ("TN", "Martin"),
        ("IL", "Macomb"),
    ],
    "conf-pr12": [
        ("ID", "Boise"), ("CO", "Fort Collins"), ("CA", "Fresno"), ("WA", "Spokane"),
        ("OR", "Corvallis"), ("CA", "San Diego"), ("TX", "San Marcos"), ("UT", "Logan"), ("WA", "Pullman"),
    ],
    "conf-psl": [
        ("DC", "Washington"), ("NY", "West Point"), ("MA", "Boston"), ("PA", "Lewisburg"),
        ("NY", "Hamilton"), ("MA", "Worcester"), ("PA", "Easton"), ("PA", "Bethlehem"),
        ("MD", "Baltimore"), ("MD", "Annapolis"),
    ],
    "conf-sedc": [
        ("AL", "Tuscaloosa"), ("AR", "Fayetteville"), ("AL", "Auburn"), ("FL", "Gainesville"),
        ("GA", "Athens"), ("KY", "Lexington"), ("LA", "Baton Rouge"), ("MS", "Starkville"),
        ("MO", "Columbia"), ("OK", "Norman"), ("MS", "Oxford"), ("SC", "Columbia"),
        ("TN", "Knoxville"), ("TX", "Austin"), ("TX", "College Station"), ("TN", "Nashville"),
    ],
    "conf-scf": [
        ("TN", "Chattanooga"), ("TN", "Johnson City"), ("SC", "Greenville"), ("GA", "Macon"),
        ("AL", "Birmingham"), ("TN", "Cookeville"), ("SC", "Charleston"), ("NC", "Greensboro"),
        ("VA", "Lexington"), ("NC", "Cullowhee"), ("SC", "Spartanburg"),
    ],
    "conf-gcc": [
        ("TX", "Commerce"), ("TX", "Houston"), ("TX", "San Antonio"), ("TX", "Beaumont"),
        ("LA", "Lake Charles"), ("LA", "New Orleans"), ("LA", "Thibodaux"), ("LA", "Natchitoches"),
        ("LA", "Hammond"), ("TX", "Nacogdoches"), ("TX", "Corpus Christi"), ("TX", "Edinburg"),
    ],
    "conf-scal": [
        ("AL", "Normal"), ("AL", "Montgomery"), ("MS", "Lorman"), ("AR", "Pine Bluff"),
        ("FL", "Daytona Beach"), ("FL", "Tallahassee"), ("LA", "Grambling"), ("MS", "Jackson"),
        ("MS", "Itta Bena"), ("TX", "Prairie View"), ("LA", "Baton Rouge"), ("TX", "Houston"),
    ],
    "conf-summit": [
        ("MO", "Kansas City"), ("ND", "Grand Forks"), ("ND", "Fargo"), ("NE", "Omaha"),
        ("OK", "Tulsa"), ("SD", "Vermillion"), ("SD", "Brookings"), ("MN", "St. Paul"),
    ],
    "conf-sbc": [
        ("NC", "Boone"), ("AR", "Jonesboro"), ("SC", "Conway"), ("GA", "Statesboro"),
        ("GA", "Atlanta"), ("VA", "Harrisonburg"), ("LA", "Lafayette"), ("LA", "Monroe"),
        ("LA", "Ruston"), ("WV", "Huntington"), ("VA", "Norfolk"), ("AL", "Mobile"),
        ("MS", "Hattiesburg"), ("AL", "Troy"),
    ],
    "conf-uac": [
        ("TX", "Abilene"), ("TN", "Clarksville"), ("AR", "Conway"), ("KY", "Richmond"),
        ("AR", "Little Rock"), ("AL", "Florence"), ("TX", "Stephenville"), ("TX", "Arlington"),
    ],
    "conf-wcc": [
        ("CO", "Denver"), ("CA", "Los Angeles"), ("CA", "Stockton"), ("CA", "Malibu"),
        ("OR", "Portland"), ("CA", "Moraga"), ("CA", "San Diego"), ("CA", "San Francisco"),
        ("CA", "Santa Clara"), ("WA", "Seattle"),
    ],
}

# Back-compat: state-only lists derived from GEO
HOME_STATES_BY_CONF: dict[str, list[str]] = {
    cid: [s for s, _ in rows] for cid, rows in GEO_BY_CONF.items()
}
