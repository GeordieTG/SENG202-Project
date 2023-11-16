package seng202.team5.models.csvInteraction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * A class that contains the common functionality of the CSV readers and writers
 */
public class CSVCommon {

    /**
     * A string representing the currently used file
     */
    protected String fileDirectory;

    /**
     * A scanner created at initialization that is reading the file shown in the file directory
     */
    protected Scanner scanner= null;

    /**
     * The FileWriter used by writer CSV Parsers
     */
    protected FileWriter fileWriter = null;

    /**
     * The BufferedWriter used by writer CSV parsers
     */
    protected BufferedWriter bufferWriter = null;

    /**
     * The PrintWriter used by writer CSV parsers
     */
    protected PrintWriter printWriter = null;

    /**
     * A Boolean to differentiate readers from writers
     */
    private final Boolean reader;

    /**
     * The remaining cells to read
     */
    protected String remainingCellString;

    /**
     * A constructor that is used by all subclasses
     * @param reader A boolean to represent if the parser is going to read or write
     *               to a file
     */
    protected CSVCommon (Boolean reader) {
        this.fileDirectory = null;
        this.reader = reader;
    }

    /**
     * Gets the file directory
     * @return Returns a string to represent the currently active file directory
     */
    public String getFileDirectory() {
        return fileDirectory;
    }

    /**
     * Gets the scanner
     * @return Returns the scanner associated with the current File
     */
    public Scanner getScanner() {
        return scanner;
    }

    /**
     * Gets the remaining cell string
     * @return Returns the remaining line's worth of information from a reader
     */
    public String getRemainingCellString() {
        return remainingCellString;
    }

    /**
     * Gets the print writer
     * @return Returns the print writer that is attached to a writer
     */
    public PrintWriter getPrintWriter() {
        return printWriter;
    }

    /**
     * Calls the application logger
     */
    private static final Logger log = LogManager.getLogger();

    /**
     * Whenever a new line is read this string contains the contents of the line, so it can be processed
     * by next Cell
     * @param remainingCellString The string for the next line of the CSV
     */
    public void setRemainingCellString(String remainingCellString) {
        this.remainingCellString = remainingCellString;
    }

    /**
     * Changes the file directory of the Reader/Writer. If it is a reader it also has the
     * added functionality of changing the scanner to be looking at the new file
     * @param fileDirectory The new directory for the CSV reader to look at
     * @throws IOException Thrown if the new directory is not available
     */
    public void selectFile(String fileDirectory) throws IOException {
        if (this.fileDirectory == null) {
            this.fileDirectory = fileDirectory;
        } else if (!this.fileDirectory.equals(fileDirectory)){
            csvClose();
        } else {
            return;
        } // Already active file therefore do nothing

        if (reader) {
            try {
                if (fileDirectory.equals("BaseData")) {
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    InputStream is = classLoader.getResourceAsStream("localCSVData/chargerData.csv");
                    assert is != null;
                    this.scanner = new Scanner(is);
                } else {
                    this.scanner = new Scanner(new File(fileDirectory));
                }
            } catch (FileNotFoundException e) {
                throw new IOException("File not found");
            }
        } else {
            try {
                this.fileDirectory = fileDirectory;
                this.fileWriter = new FileWriter(fileDirectory, false);
                this.bufferWriter = new BufferedWriter(fileWriter);
                this.printWriter = new PrintWriter(bufferWriter);
            } catch(Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * A function that is responsible for abstracting the reading of the next cell in a CSV file based on
     * comma splitting and if there are commas within the cell it is wrapped in quotation mark.
     * It also handles removing the cell that is returned from the string
     * Note : With cells surrounded by quotation marks, the quotation marks are removed from the cell.
     * eg: "123 abc street, Christchurch" -> 123 abc street, Christchurch
     * @return Returns a string value of the next cell
     * @throws IllegalArgumentException Thrown when this.remainingCellString is empty
     * @throws IllegalStateException Thrown if nextCell is called before the remainingCellString is set
     */
    protected String nextCell()
            throws IllegalArgumentException, IllegalStateException {
        if (remainingCellString == null) {
            throw new IllegalStateException("No line is currently being read :"
                    + " Check that remainingCellString is "
                    + "set before calling nextCell");
        }
        if (remainingCellString.length() == 0) {
            throw new IllegalArgumentException(
                    "Can't get cell from an empty string");
        }
        if (remainingCellString.charAt(0) == '\"') {
            final int chunksToSplit = 3;
            String[] stringBreaker = remainingCellString
                    .split("\"", chunksToSplit);
            remainingCellString = stringBreaker[2].substring(1);
            return stringBreaker[1];
        } else if (remainingCellString.charAt(0) == '[') {
            return jsonNextCell();
        }
        String[] stringBreaker = remainingCellString.split(",", 2);
        if (stringBreaker.length > 1) {
            remainingCellString = stringBreaker[1];
        } else {
            remainingCellString = "";
        }
        return stringBreaker[0];
    }

    /**
     * Gets the next cell worth of data given that the next part of remaining string is a JSON object
     */
    private String jsonNextCell() {
        int index = 1; // The current index within the remaining cell string
        int counter = 1; // Counting the depth of [ brackets
        while (index < remainingCellString.length()) {
            if (remainingCellString.charAt(index) == '[') {
                counter++;
            } else if (remainingCellString.charAt(index) == ']') {
                counter--;
                if (counter == 0) {
                    String returnString = remainingCellString.substring(0, index + 1);
                    remainingCellString = remainingCellString.substring(index + 2);
                    return returnString;
                }
            }
            index++;
        }
        throw new IllegalArgumentException("JSON with missing ] bracket");
    }

    /**
     * Method to take list of strings and format them.
     * ready to put in a complex cell format.
     * Format : "{attribute 1, attribute 2, ...}, {attribute 1, attribute2, ...} ..."
     * @param input A list of strings to be encapsulated with the complex CSV cell format.
     * @return final formatted string.
     */
    public String complexCellFormat(final ArrayList<String> input) {
        int i = 0;
        StringBuilder result = new StringBuilder();
        while (i < input.size()) {
            if (i == input.size() - 1 && i == 0) {
                result.append("\"{").append(input.get(i)).append("}\"");
            } else if (i == input.size() - 1) {
                result.append("{").append(input.get(i)).append("}\"");
            } else if (i == 0) {
                result.append("\"{").append(input.get(i)).append("},");
            } else {
                result.append("{").append(input.get(i)).append("},");
            }
            i += 1;
        }

        if (input.size() == 0) {
            result.append("\"\"");
        }
        return result.toString();
    }

    /**
     * CSV Close is responsible for freeing all the
     * variables that shouldn't be assigned anymore.
     * etc. FileDirectory would reset, Scanner and writers
     * would be freed up to make the file available for other applications.
     */
    public void csvClose() {
        this.fileDirectory = null;
        if (scanner != null) {
            scanner.close();
            scanner = null;
        }
        if (printWriter != null) {
            printWriter.close();
            try {
                bufferWriter.close();
                fileWriter.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            printWriter = null;
            bufferWriter = null;
            fileWriter = null;
        }
    }
}
