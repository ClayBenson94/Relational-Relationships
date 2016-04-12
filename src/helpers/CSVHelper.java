package helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Clay on 4/4/2016.
 */
public class CSVHelper{

    private Scanner scanner;
    public ArrayList<String> currentRow;

    public void openCSV(String filePath) {
        try {
            scanner = new Scanner(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public boolean readRow() {
        ArrayList<String> returnList = new ArrayList<>();
        String[] splitList;
        boolean success;
        success = scanner.hasNextLine();
        if (success){
            splitList = scanner.nextLine().split(",");
            returnList = new ArrayList<>(Arrays.asList(splitList));
        }
        currentRow = returnList;
        return success;
    }

    public void closeCSV() {
        scanner.close();
    }

}
