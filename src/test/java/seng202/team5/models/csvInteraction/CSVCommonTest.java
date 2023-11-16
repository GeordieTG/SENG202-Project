package seng202.team5.models.csvInteraction;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Handles all the test cases for CSVCommon class.
 */
public class CSVCommonTest {

// --Commented out by Inspection START (16/10/22, 6:40 PM):
//    /**
//     * String representation of csv path
//     */
//    String invalidDirectory = "";
// --Commented out by Inspection STOP (16/10/22, 6:40 PM)
    /**
     * String representation of csv path
     */
    final String emptyCSV = "src/test/resources/testCSVData/emptyCSV.csv";
// --Commented out by Inspection START (16/10/22, 6:40 PM):
//    /**
//     * String representation of csv path
//     */
//    String chargerTestCSV = "src/test/resources/testCSVData/singleValidChargerTest.csv";
// --Commented out by Inspection STOP (16/10/22, 6:40 PM)

    /**
     * Checks that when a reader subclass is created with an invalid directory an error is thrown
     */
    @Test
    public void testInvalidFileReturnsErrorWithReader() {
        Exception exception = assertThrows(IOException.class, () -> {
            CSVCommon csvCommon = new CSVCommon(true);
            csvCommon.selectFile("Invalid File Name");
        });
    }

    /**
     * Checks that when passed a valid directory a new reader CSV is created
     */
    @Test
    public void testValidReaderConstruction() {
        boolean successful = true;
        try {
            CSVCommon currentCSVReader = new CSVCommon(true);
            currentCSVReader.selectFile(emptyCSV);
        } catch (IOException e) {
            successful = false;
        }
        assertTrue(successful);
    }

    /**
     * Checks that when passed a valid directory a new reader CSV is created
     */
    @Test
    public void testValidWriterConstruction() {
        try {
            CSVCommon currentCSVWriter = new CSVCommon(false);
            currentCSVWriter.selectFile(emptyCSV);
        } catch (IOException e) {
            fail();
        }
        assertTrue(true);
    }

    /**
     * Checks that when a writer is created, there is no allocation of a scanner
     */
    @Test public void testWriterHasNoScanner() {
        CSVCommon currentCSVWriter = null;
        try {
            currentCSVWriter = new CSVCommon(false);
            currentCSVWriter.selectFile(emptyCSV);
        } catch (IOException ignored) {
            fail();
        }
        assertNull(currentCSVWriter.getScanner());
    }


    /**
     * Checks for successful file change for readers
     * 1. The file directory must change to the new directory
     * 2. A new scanner would be created for the new file
     */
    @Test
    public void testChangeFileChangesDirectoryAndScannerWithReader() {
        CSVCommon currentCSVReader = null;
        Scanner initialScanner = null;
        Scanner finalScanner = null;
        try {
            currentCSVReader = new CSVCommon(true);
            initialScanner = currentCSVReader.getScanner();
            currentCSVReader.selectFile(emptyCSV);
            finalScanner = currentCSVReader.getScanner();
        } catch (IOException e) {
            fail();
        }
        assertTrue(currentCSVReader.getFileDirectory().equals(emptyCSV) && initialScanner != finalScanner);
    }

    /**
     * Checks that if the select file changes to the file that is already in use with the reader the scanner doesn't reset
     */
    @Test
    public void testChangeFileToSameFileWithReader() {
        CSVCommon currentCSVReader = null;
        Scanner initialScanner = null;
        Scanner finalScanner = null;

        try {
            currentCSVReader = new CSVCommon(true);
            currentCSVReader.selectFile(emptyCSV);
            initialScanner = currentCSVReader.getScanner();
            currentCSVReader.selectFile(emptyCSV);
            finalScanner = currentCSVReader.getScanner();
        } catch (IOException e) {
            fail();
        }
        assertEquals(initialScanner, finalScanner);
    }

    /**
     * Tests that a Reader is closed properly with the csvClose() function
     */
    @Test
    public void testReaderCloseWithCSVClose() {
        try {
            CSVCommon csvCommon = new CSVCommon(true);
            csvCommon.selectFile(emptyCSV);
            csvCommon.csvClose();
            assertTrue(csvCommon.getScanner() == null && csvCommon.getFileDirectory() == null);
        } catch (Exception ignored) {
            fail();
        }
    }

    /**
     * Tests that a writer is closed properly with csvClose()
     */
    @Test
    public void testWriterCloseWithCSVClose() {
        try {
            CSVCommon csvCommon = new CSVCommon(false);
            csvCommon.selectFile(emptyCSV);
            csvCommon.csvClose();
            assertTrue(csvCommon.getFileDirectory() == null && csvCommon.getPrintWriter() == null);
        } catch (Exception ignored) {fail();}
    }
}
