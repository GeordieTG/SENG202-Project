package seng202.team5.models.csvInteraction;

import org.junit.jupiter.api.Test;
import seng202.team5.exceptions.NotEnteredException;
import seng202.team5.models.data.Charger;
import seng202.team5.models.data.Connector;
import seng202.team5.models.data.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit test for the Charger CSV writer
 * These simply test if the functions run without errors
 * Output was tested by manually looking at the output in the csv file and verifying if it was correct
 */
public class ChargerCSVWriterTest {

    /**
     * Default charger used for testing
     * @return default charger
     */
    public Charger defaultCharger() {
        Charger charger = new Charger();
        charger.setLocation(new Location(0,0));
        Connector connector = new Connector();
        ArrayList<Connector> connectors = new ArrayList<>(List.of(connector));
        charger.setConnectorList(connectors);
        return charger;
    }

    /**
     * Test whether the CSVWriter works for one charger
     */
    @Test
    public void defaultOneChargerTest() throws NotEnteredException {

        Charger charger = defaultCharger();
        ArrayList<Charger> chargers = new ArrayList<>(List.of(charger));

        ChargerCSVWriter writer = new ChargerCSVWriter();
        writer.saveRecord(chargers, "src/main/resources/localCSVData/writingTest.csv");
    }

    /**
     * Test whether the CSVWriter works for multiple chargers
     */
    @Test
    public void defaultMulitpleChargerTest() throws NotEnteredException {
        Charger charger1 = defaultCharger();
        Charger charger2 = defaultCharger();

        ArrayList<Charger> chargers = new ArrayList<>(Arrays.asList(charger1, charger2));

        ChargerCSVWriter writer = new ChargerCSVWriter();
        writer.saveRecord(chargers, "src/main/resources/localCSVData/writingTest.csv");
    }

    /**
     * Test whether the CSVWriter works for with empty arraylists
     */
    @Test
    public void EmptyArrayListTest() throws NotEnteredException {
        Charger charger = defaultCharger();
        ArrayList<Connector> connectors = new ArrayList<>();
        charger.setConnectorList(connectors);
        ArrayList<Charger> chargers = new ArrayList<>(List.of(charger));

        ChargerCSVWriter writer = new ChargerCSVWriter();
        writer.saveRecord(chargers, "src/main/resources/localCSVData/writingTest.csv");
    }
}