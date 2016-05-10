package helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * A Class built to parse CSV's
 */
public class CSVHelper {

    private Scanner scanner;
    public ArrayList<String> currentRow;

    /**
     * Open the given csv filepath
     * @param filePath - the filepath to a csv
     */
    public void openCSV(String filePath) {
        try {
            scanner = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Read the contents of a csv row into an ArrayList and return it
     * @return An ArrayList containing the contents of the current csv row
     */
    public boolean readRow() {
        ArrayList<String> returnList = new ArrayList<>();
        String[] splitList;
        boolean success;
        success = scanner.hasNextLine();
        if (success) {
            splitList = scanner.nextLine().split(",");
            returnList = new ArrayList<>(Arrays.asList(splitList));
        }
        currentRow = returnList;
        return success;
    }

    /**
     * Close the open csv file
     */
    public void closeCSV() {
        scanner.close();
    }
}
