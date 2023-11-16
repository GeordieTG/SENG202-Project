DROP TABLE IF EXISTS chargers;
--SPLIT
CREATE TABLE IF NOT EXISTS chargers (
    latitude REAL,
    longitude REAL,
    name TEXT,
    operator TEXT,
    owner TEXT,
    address TEXT,
    alwaysOpen INTEGER,
    carParkCount REAL,
    carParkNeedsPayment INTEGER,
    maxTimeLimit REAL,
    touristAttraction INTEGER,
    numberOfConnectors INTEGER,
    connectors TEXT,
    rating REAL,
    primary key (latitude, longitude));
--SPLIT
DROP TABLE IF EXISTS users;
--SPLIT
CREATE TABLE IF NOT EXISTS users (
    username TEXT PRIMARY KEY,
    password TEXT,
    first_name TEXT,
    last_name TEXT,
    email TEXT,
    favourites TEXT[],
    journeys TEXT[][],
    vehicles TEXT[][],
    totalCO2 REAL,
    totalDistance REAL);
