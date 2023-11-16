package seng202.team5.models.csvInteraction;

import org.junit.jupiter.api.Test;
import seng202.team5.models.data.Charger;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the Charger CSV reader
 */
public class ChargerCSVReaderTest {

    /**
     * String representation of csv path
     */
    final String singleValidChargerCSV = "src/test/resources/testCSVData/singleValidChargerTest.csv";

    /**
     * String representation of a CSV path for a charger with no connectors
     */
    final String noConnectorChargerCSV = "src/test/resources/testCSVData/noConnectorChargerTest.csv";

    /**
     * String representation of a CSV path for a charger with multiple connectors
     */
    final String multipleConnectorChargerCSV = "src/test/resources/testCSVData/multipleConnectorCharger.csv";

    /**
     * String representation of a CSV path for a charger with an invalid connector.
     */
    final String invalidConnectorCSV = "src/test/resources/testCSVData/invalidConnectorCharger.csv";

    final String halswellChargerCSV = "src/test/resources/testCSVData/halswellCSV.csv";

    /**
     * Tests that when a ChargerCSVReader is opened that it isn't automatically
     * set to a certain file. This is due to the singleton property and file
     * should be opened through readAllChargers(String fileDirectory)
     */
    @Test
    public void testInitialiseReader() {
        ChargerCSVReader chargerReader = new ChargerCSVReader();
        assertTrue(chargerReader.getScanner() == null && chargerReader.getFileDirectory() == null);
    }

    /**
     * Aimed to test the readNextCharger functionality of reading a singular charger from a charger list
     * Uses a single lined file to achieve this (singleValidChargerTest.csv)
     */
    @Test
    public void testSingleLoadFromCSV() {
        try {
            ChargerCSVReader chargerReader = new ChargerCSVReader();
            ArrayList<Charger> chargerList = chargerReader.readAllChargers(singleValidChargerCSV);
            assertEquals(1, chargerList.size());
        } catch (Exception e) {fail();}
    }

    /**
     * Tests that when given an invalid address the charger isn't saved
     * Only tests posX as the others mimic the input method
     */
    @Test
    public void testInvalidLocationSubmission() {
        ArrayList<Charger> chargers = null;
        try {
            ChargerCSVReader csvReader = new ChargerCSVReader();
            chargers = csvReader.readAllChargers("src/test/resources/testCSVData/invalidLocationTest");
            assertEquals(0, chargers.size());
        } catch (Exception ignored) {fail();}
    }

    /**
     * Test that an address with commas is still accepted as a valid address
     */
    @Test
    public void testCommaInAddressIsValid() {
        try {
            ChargerCSVReader csvReader = new ChargerCSVReader();
            ArrayList<Charger> charger = csvReader.readAllChargers(singleValidChargerCSV);
            assertEquals("4 Kitchener Dr, Mount Cook National Park 7999, New Zealand", charger.get(0).getAddress());
        } catch (Exception ignored) {fail();}
    }

    /**
     * When the max time limit is set as Unlimited, check that the value that
     * is put in the charger is max int
     */
    @Test
    public void testUnlimitedIsAcceptedValue() {
        try {
            ChargerCSVReader csvReader = new ChargerCSVReader();
            ArrayList<Charger> chargers = csvReader.readAllChargers(singleValidChargerCSV);
            assertEquals(Integer.MAX_VALUE, chargers.get(0).getMaxTimeLimit());
        } catch (Exception ignored) {fail();}
    }


    /**
     * Testing to see that when the CSVReader reads a  charger that the total
     * amount of connectors will equal the amount specified in the CSV.
     */
    @Test
    public void testConnectorsListIsAcceptedValue() {
        try {
            ChargerCSVReader csvReader = new ChargerCSVReader();
            ArrayList<Charger> chargers = csvReader.readAllChargers(singleValidChargerCSV);
            assertEquals(1, chargers.get(0).getNumberOfConnectors());
        } catch (Exception ignored) {fail();}
    }

    /**
     * When the CSVReader has a charger with no connector seeing if the ChargerCSVReader will go through
     * the for loop or not (i.e. add a charger with non-existent information or not).
     */
    @Test
    public void testConnectorsListIsNothing() {
        try {
            ChargerCSVReader csvReader = new ChargerCSVReader();
            ArrayList<Charger> chargers = csvReader.readAllChargers(noConnectorChargerCSV);
            assertEquals(0, chargers.get(0).getNumberOfConnectors());
        } catch (Exception ignored) {fail();}
    }

    /**
     * Testing to see if the CSVReader reads a charger with multiple connectors correctly.
     */
    @Test
    public void testMultipleConnectorCharger() {
        try {
            ChargerCSVReader csvReader = new ChargerCSVReader();
            ArrayList<Charger> chargers = csvReader.readAllChargers(multipleConnectorChargerCSV);
            assertEquals(4, chargers.get(0).getNumberOfConnectors());
        } catch (Exception ignored) {fail();}
    }

    /**
     * Testing to see if the CSVReader will add a connector with invalid information.
     */
    @Test
    public void testInvalidConnectorCharger() {
        try {
            ChargerCSVReader csvReader = new ChargerCSVReader();
            ArrayList<Charger> chargers = csvReader.readAllChargers(invalidConnectorCSV);
            assertEquals(0, chargers.get(0).getNumberOfConnectors());
        } catch (Exception ignored) {fail();}
    }

    /**
     * Testing to see if the CSVReader will remain consistent when reading multiple chargers.
     */
    @Test
    public void testMultipleChargersConnectorInformation() {
        try {
            ChargerCSVReader csvReader = new ChargerCSVReader();
            ArrayList<Charger> chargers = csvReader.readAllChargers(invalidConnectorCSV);
            assertEquals(0, chargers.get(0).getNumberOfConnectors());
        } catch (Exception ignored) {fail();}
    }

    @Test
    public void testHalswellCharger() {
        try {
            ChargerCSVReader csvReader = new ChargerCSVReader();
            ArrayList<Charger> chargers = csvReader.readAllChargers(halswellChargerCSV);
            assertEquals(4, chargers.get(0).getNumberOfConnectors());
        } catch (Exception ignored) {fail();}
    }
}
