package seng202.team5.models.csvInteraction;

import seng202.team5.exceptions.NotEnteredException;
import seng202.team5.models.data.Charger;
import seng202.team5.models.data.Connector;
import java.util.ArrayList;

/**
 * Class to write charger data to a file
 */
public class ChargerCSVWriter extends CSVCommon {

    /**
     * A constructor that is used by all subclasses
     *
     */
    public ChargerCSVWriter() {
        super(false);
    }

    /**
     * Takes a charger object and adds its data to the given file
     * @param chargers The charger for which the next line of the file will be for
     */
    public void saveRecord(ArrayList<Charger> chargers, String fileDirectory) throws NotEnteredException {
        try {
            selectFile(fileDirectory);
            printWriter.println("X,Y,OBJECTID,name,operator,owner,address,is24Hours,carParkCount,hasCarparkCost" +
                    ",maxTimeLimit,hasTouristAttraction,latitude,longitude,currentType,dateFirstOperational," +
                    "numberOfConnectors,connectorsList,hasChargingCost");
            for (Charger charger : chargers) {
                String connectors = createConnectorListString(charger);
                printWriter.println("not used" + "," + "not used" + "," + "not used" + ",\"" + charger.getName() +
                        "\",\"" + charger.getOperator() + "\",\"" + charger.getOwner() + "\",\"" + charger.getAddress() +
                        "\"," + charger.isAlwaysOpen() + "," + charger.getCarParkCount() + "," +
                        charger.isCarParkNeedsPayment() + "," + charger.getMaxTimeLimit() + "," +
                        charger.isTouristAttraction() + "," + charger.getLocation().getLatitude() + "," +
                        charger.getLocation().getLongitude() + "," + charger.getCurrentType() + "," + charger.getDate() +
                        "," + charger.getNumberOfConnectors() + "," + connectors + "," + charger.isHasChargingCost());
            }
            csvClose();
        } catch(Exception E) {
            E.printStackTrace();
            throw new NotEnteredException("Error adding the Charger to the CSV file");
        }
    }

    /**
     *
     * @param charger the charger that's being added to the CSV
     * @return CSV formatted String of the chargers different connectors
     */
    private String createConnectorListString(Charger charger) {
        ArrayList<String> strings = new ArrayList<>();

        for (Connector connector : charger.getConnectorList()) {
            String string = connector.getPowerType() + "," + connector.getWattage() + "," + connector.getSocket() +
                    ", Status: " + connector.getStatus() + ", Count: " + connector.getCount();
            strings.add(string);
        }
        return complexCellFormat(strings);
    }
}
