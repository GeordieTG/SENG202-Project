package seng202.team5.models.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChargerAndConnectorTest {
    /**
     * Creates a default charger that can be used for tests.
     * @return The charger with default values
     */
    public Charger defaultNoConnectorCharger() {
        Location location = new Location(43, 140);
        return new Charger(location, "UC Charging Station", "Geordie", "Geordie", "77 Ilam Road", true, 100, true, 100, true, 2);
    }

    /**
     * Returns a default connector with count amount of connectors
     * @param count the amount of connectors of the given type
     */
    private Connector defaultConnector(int count) {
        return new Connector("AC", "Cool Socket", 50, "Maybe Operative", count);
    }

    /**
     * Tests that when a connector is added with a count of 1, the connector is in the list
     */
    @Test
    void testAddConnectorToArrayList() {
        Charger charger = defaultNoConnectorCharger();
        charger.addConnector(new Connector());
        assertEquals(1, charger.getConnectorList().size());
    }


    /**
     * Tests that the counter for the number of connectors increases within the charger as a connector is added.
     */
    @Test
    void testAddingConnectorChangesChargerConnectorCount() {
        Charger charger = defaultNoConnectorCharger();
        charger.addConnector(defaultConnector(5));
        charger.addConnector(defaultConnector(2));
        assertEquals(7, charger.getNumberOfConnectors());
    }

    /**
     *  If you had two connectors of the same type then it should still be stored as one connector in the list
     */
    @Test
    void testAddDuplicateConnectorMergesCounts() {
        Charger charger = defaultNoConnectorCharger();
        charger.addConnector(defaultConnector(2));
        charger.addConnector(defaultConnector(2));
        assertEquals(4, charger.getNumberOfConnectors());
    }

    /**
     * If you have 2 duplicate connectors added they are added as a single connector to the array list
     */
    @Test
    void testAddDuplicateConnectorMergesInList() {
        Charger charger = defaultNoConnectorCharger();
        charger.addConnector(defaultConnector(2));
        charger.addConnector(defaultConnector(2));
        assertEquals(1, charger.getConnectorList().size());
    }

    /**
     * If a connector is removed the connector count of the charger should reduce
     */
    @Test
    void testDeleteConnectorAltersCount() {
        Charger charger = defaultNoConnectorCharger();
        charger.addConnector(defaultConnector(3));
        charger.removeConnector(defaultConnector(1));
        assertEquals(0, charger.getNumberOfConnectors());
    }

    /**
     * If a connector is removed the connector list should represent the lack of the connector
     */
    @Test
    void testDeleteConnectorAltersList() {
        Charger charger = defaultNoConnectorCharger();
        charger.addConnector(defaultConnector(3));
        charger.removeConnector(defaultConnector(3));
        assertEquals(0, charger.getConnectorList().size());
    }

    /**
     * If some but not all of a connectors count is deleted, it should be represented in the chargers count
     */
    @Test
    void testDeleteSomeButNotAllConnectorsOfOneType() {
        Charger charger = defaultNoConnectorCharger();
        charger.addConnector(defaultConnector(3));
        charger.removeConnector(defaultConnector(2), 2);
        assertEquals(1, charger.getNumberOfConnectors());
    }

    /**
     * If some but not all of a connectors count is delted, it should still be in the connector list
     */
    @Test
    void testDeleteSomeButNotAllConnectorsOfOneTypeDoesntAffectList() {
        Charger charger = defaultNoConnectorCharger();
        charger.addConnector(defaultConnector(3));
        charger.removeConnector(defaultConnector(2), 2);
        assertEquals(1, charger.getConnectorList().size());
    }

    /**
     * If you try to remove a charger not in the list an error should be thrown
     */
    @Test
    void testDeleteANonExistingConnector() {
        Charger charger = defaultNoConnectorCharger();
        charger.addConnector(defaultConnector(2));
        Connector missingConnector = defaultConnector(2);
        missingConnector.setStatus("Error");
        try {
            charger.removeConnector(missingConnector);
            fail();
        } catch (ArrayIndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    /**
     * Test that you can't delete a count higher than the one that currently exists
     */
    @Test
    void testDeleteWhereCountIsHigherThanPresent() {
        Charger charger = defaultNoConnectorCharger();
        charger.addConnector(defaultConnector(2));
        try {
            charger.removeConnector(defaultConnector(3), 3);
            fail();
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    /**
     * test to check that different connectors don't merge in list
     */
    @Test
    void addTwoConnectorsToList() {
        Charger charger = defaultNoConnectorCharger();
        charger.addConnector(defaultConnector(2));
        Connector connector = defaultConnector(2);
        connector.setStatus("Lol");
        charger.addConnector(connector);
        assertEquals(2, charger.getConnectorList().size());
    }

    /**
     * Testing whether a charger with a connector will be formatted into the correct syntax. This belongs in the
     * charger CSV writer test as it is making sure we are writing the connectors in the proper syntax.
     */
    @Test
    public void ConnectorToStringTest() {
        try {
            Connector connector = new Connector("{AC, 22 kW, Type 2 Socketed, Status: Operative, Count:1}");
            assertEquals("{AC, 22 kW, Type 2 Socketed, Status: Operative, Count:1}", connector.toString(connector));
        } catch (Exception ignored) {fail();}
    }


    /**
     * Testing whether the toString will write proper strings of the connectors when a
     * charger has multiple connectors.
     */
    @Test
    public void MultipleConnectorToStringTest() {
        try {
            Connector connector1 = new Connector("{DC, 22 kW, Type 2 Socketed, Status: Operative, Count:1}");
            Connector connector2 = new Connector("{AC, 32 kW, Type 1 Tethered, Status: Operative, Count:4}");
            String writtenString = connector1.toString(connector1) + "," + connector2.toString(connector2);
            assertEquals("{DC, 22 kW, Type 2 Socketed, Status: Operative, Count:1},{AC, 32 kW, " +
                    "Type 1 Tethered, Status: Operative, Count:4}", writtenString);
        } catch (Exception ignored) {fail();}
    }
}
