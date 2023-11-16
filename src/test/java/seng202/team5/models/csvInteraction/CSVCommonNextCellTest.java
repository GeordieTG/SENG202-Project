package seng202.team5.models.csvInteraction;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains all the testing for the nextCell function within CSVCommon
 */
public class CSVCommonNextCellTest {

// --Commented out by Inspection START (16/10/22, 6:39 PM):
//    /**
//     * CSV Tables that are used for testing
//     * All of which are located within resources/testCSVData
//     */
//    String invalidDirectory = "";
// --Commented out by Inspection STOP (16/10/22, 6:39 PM)
    /**
     * String representation of csv path
     */
    final String emptyCSV = "src/test/resources/testCSVData/emptyCSV.csv";
// --Commented out by Inspection START (16/10/22, 6:39 PM):
//    /**
//     * String representation of csv path
//     */
//    String chargerTestCSV = "src/test/resources/testCSVData/singleValidChargerTest.csv";
// --Commented out by Inspection STOP (16/10/22, 6:39 PM)

    /**
     * Whenever nextCell is called, there is an expectation that the readNext() function of the subclasses
     * has added the new string to the CSVCommon.remainingCellString var. If it hasn't then IllegalStateException
     * should be thrown
     */
    @Test
    public void testErrorThrownOnNextCellWithoutSelectingString() {
        try {
            final CSVCommon csvCommon = new CSVCommon(true);
            csvCommon.selectFile(emptyCSV);
            Exception exception = assertThrows(IllegalStateException.class, csvCommon::nextCell);
        } catch (Exception ignored) {
            fail();
        }
    }

    /**
     * If the nextCell function is passed an empty string then an error should be thrown indicating that no
     * cell can be retrieved
     */
    @Test
    public void testErrorThrownOnNextCellEmptyString() {
        try {
            final CSVCommon csvCommon = new CSVCommon(true);
            csvCommon.selectFile(emptyCSV);
            csvCommon.setRemainingCellString("");
            Exception exception = assertThrows(IllegalArgumentException.class, csvCommon::nextCell);
        } catch (Exception ignored) {
            fail();
        }
    }

    /**
     * If the string is a single cell, It checks that the input string is changed to an empty string
     */
    @Test
    public void testFinalCellCreatesEmptyInputString() {
        try {
            CSVCommon csvCommon = new CSVCommon(true);
            csvCommon.selectFile(emptyCSV);
            csvCommon.setRemainingCellString("testCell");
            csvCommon.nextCell();
            assertEquals("", csvCommon.getRemainingCellString());
        } catch (Exception ignored) {fail();}
    }

    /**
     * When there is more than one cell in the list check that the first cell is returned correctly
     */
    @Test
    public void testNextCellReturnsCell() {
        try {
            CSVCommon csvCommon = new CSVCommon(true);
            csvCommon.selectFile(emptyCSV);
            csvCommon.setRemainingCellString("First Cell, Second Cell");
            String goalCell = csvCommon.nextCell();
            assertEquals("First Cell", goalCell);
        } catch (Exception ignored) {fail();}
    }

    /**
     * When a cell is popped from the remainingCellString check that the remaining is the original string minus
     * the removed cell
     */
    @Test
    public void testNextCellPopsCell() {
        try {
            CSVCommon csvCommon = new CSVCommon(true);
            csvCommon.selectFile(emptyCSV);
            csvCommon.setRemainingCellString("First Cell,Second Cell");
            String tempString = csvCommon.nextCell();
            assertEquals("Second Cell", csvCommon.getRemainingCellString());
        } catch (Exception ignored) {fail();}
    }

    /**
     * Checks that cells with commas embedded and quotation marks around are removed as a single cell
     */
    @Test
    public void testCommaInvolvedCellPops() {
        try {
            CSVCommon csvCommon = new CSVCommon(true);
            csvCommon.selectFile(emptyCSV);
            csvCommon.setRemainingCellString("\"Complicated, Cell\",SecondCell");
            String goalCell = csvCommon.nextCell();
            assertEquals("Complicated, Cell", goalCell);
        } catch (Exception ignored) {fail();}
    }

    /**
     * Checks that with comma cells the cell is popped correctly and the rest of the string is correct
     */
    @Test
    public void testCommaCellPopsWithCorrectRemainingCellString() {
        try {
            CSVCommon csvCommon = new CSVCommon(true);
            csvCommon.selectFile(emptyCSV);
            csvCommon.setRemainingCellString("\"Complicated, cell\",SecondCell");
            String tempString = csvCommon.nextCell();
            assertEquals("SecondCell", csvCommon.getRemainingCellString());
        } catch (Exception ignored) {fail();}
    }

    /**
     * Checks that with 3 or more simple cells that there isn't excess splitting
     */
    @Test
    public void testForExcessSplittingWithSimplecells() {
        try {
            CSVCommon csvCommon = new CSVCommon(true);
            csvCommon.selectFile(emptyCSV);
            csvCommon.setRemainingCellString("First,Second,Third");
            String tempString = csvCommon.nextCell();
            assertEquals("Second,Third", csvCommon.getRemainingCellString());
        } catch (Exception ignored) {fail();}
    }

    /**
     * Checks that with 3 or more complicated ells that there isn't excess splitting
     */
    @Test
    public void testForExcessSplittingWithCommaIncludingCells() {
        try {
            CSVCommon csvCommon = new CSVCommon(true);
            csvCommon.selectFile(emptyCSV);
            csvCommon.setRemainingCellString("\"First,Cell\",Second,Third");
            String tempString = csvCommon.nextCell();
            assertEquals("Second,Third", csvCommon.getRemainingCellString());
        } catch (Exception ignored) {fail();}
    }

    /**
     * Test to see if nextCell() can deal with Json objects
     */
    @Test
    public void testWithJson() {
        try {
            CSVCommon csvCommon = new CSVCommon(true);
            csvCommon.selectFile("src/test/resources/testCSVData/jsonTest.csv");
            csvCommon.setRemainingCellString("[test],third");
            assertEquals("[test]", csvCommon.nextCell());
            assertEquals("third", csvCommon.getRemainingCellString());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Test that nesting brackets doesn't affect the Json next cell
     */
    @Test
    public void testJsonWithEmbeddedBrackets() {
        try {
            CSVCommon csvCommon = new CSVCommon(true);
            csvCommon.selectFile("src/test/resources/testCSVData/jsonTest.csv");
            csvCommon.setRemainingCellString("[[test]],second");
            assertEquals("[[test]]", csvCommon.nextCell());
            assertEquals("second", csvCommon.getRemainingCellString());
        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Test as above but a bit more complicated just to be safe
     */
    @Test
    public void testJsonWithMoreEmbeddedBrackets() {
        try {
            CSVCommon csvCommon = new CSVCommon(true);
            csvCommon.setRemainingCellString("first,[[][][[,]]],second");
            csvCommon.nextCell();
            assertEquals("[[][][[,]]]", csvCommon.nextCell());
            assertEquals("second", csvCommon.getRemainingCellString());
        } catch (Exception ignored) {fail();}
    }
}
