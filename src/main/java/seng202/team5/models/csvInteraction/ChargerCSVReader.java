package seng202.team5.models.csvInteraction;


import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import seng202.team5.models.data.Charger;
import seng202.team5.models.data.Connector;
import seng202.team5.models.data.Location;
import static java.lang.Integer.MAX_VALUE;

/**
 * A class for reading CSV files containing Charger station data
 */
public class ChargerCSVReader extends CSVCommon  {


    /**
     * Constructor for creating the Charger CSV Parser for the application
     */
    public ChargerCSVReader() {
        super(true);
    }

    /**
     * Calls the application logger
     */
    private static final Logger log = LogManager.getLogger();

    /**
     * A function that returns a charger based on the next line of the CSV file
     * @return The charger described on the current line
     * @throws EOFException Thrown when there is no more data to be shown
     * @throws IllegalStateException Thrown if there is no scanner currently with the class
     */
    private Charger readNextCharger() throws EOFException, IllegalStateException, IllegalArgumentException {

        if (scanner == null)  {
            throw new IllegalStateException("There is no scanner currently active");
        }

        if (!scanner.hasNextLine())  {
            throw new EOFException("There is no more Chargers to be gathered");
        }
        // Grab the line for processing
        remainingCellString = scanner.nextLine();
        // posX and posY are only relevant to NZTA's own map so next 2 cells are irrelevant
        nextCell();
        nextCell();
        // We don't store ChargerID
        nextCell();
        Charger newCharger = new Charger();
        newCharger.setName(nextCell());
        newCharger.setOperator(nextCell());
        newCharger.setOwner(nextCell());
        newCharger.setAddress(nextCell());
        newCharger.setAlwaysOpen(Boolean.parseBoolean(nextCell()));
        newCharger.setCarparkCount(Integer.parseInt(nextCell()));
        newCharger.setCarparkNeedsPayment(Boolean.parseBoolean(nextCell()));
        String timeLimit = nextCell();
        if (timeLimit.equalsIgnoreCase("unlimited") || (timeLimit.equalsIgnoreCase("no limit"))) {
            newCharger.setMaxTimeLimit(MAX_VALUE);
        } else {
            newCharger.setMaxTimeLimit(Integer.parseInt(timeLimit));
        }
        newCharger.setTouristAttraction(Boolean.parseBoolean(nextCell()));
        float latitude = Float.parseFloat(nextCell());
        float longitude = Float.parseFloat(nextCell());
        newCharger.setLocation(new Location(latitude, longitude));

        newCharger.setPowerType(nextCell());
        // Skipping when the charger was first operational.
        nextCell();
        // Need to get the count so that we can go through the for loop correctly.
        int connectorsCount = Integer.parseInt(nextCell());
        String connectors = nextCell();
        String connectorRegex = ",(?=()*\\{)";
        String[] allConnectors = connectors.split(connectorRegex);

        for (int i = 0; i < connectorsCount;) {
            Connector connector = new Connector(allConnectors[i]);
            if (connector.getPowerType() != null && connector.getCount() > 0) {
                for (int j = 1; j < connector.getCount(); j++) {
                    newCharger.addConnector(connector);
                }
                newCharger.addConnector(connector);
                i += connector.getCount();
            } else {
                connectorsCount -= connector.getCount();
            }
        }
        String chargingCost = (nextCell()).toUpperCase();
        if (chargingCost.equals("TRUE") || chargingCost.equals("YES")) {
            newCharger.setHasChargingCost(true);
        } else if (chargingCost.equals("FALSE") || chargingCost.equals("NO")) {
            newCharger.setHasChargingCost(false);
        }
        return newCharger;
    }


    /**
     * Reads all the lines of the file and returns a ArrayList of chargers
     * @return The Arraylist of chargers that were in the file
     */
    public ArrayList<Charger> readAllChargers(String fileDirectory) throws IllegalStateException, IOException {
        selectFile(fileDirectory);
        if (this.fileDirectory == null || this.scanner == null) {
            throw new IllegalStateException("No file has been initialised");
        }

        ArrayList<Charger> chargerList = new ArrayList<>();

        // Removes the header line
        this.scanner.nextLine();

        while (true) {
            try {
                Charger tempCharger = readNextCharger();
                chargerList.add(tempCharger);
            } catch (EOFException e) { // CSV file is finished
                break;
            } catch (Exception e) { // The read of a charger failed and shouldn't be added
                log.warn(e.getMessage());
            }
        }
        csvClose();
        return chargerList;
    }

}
