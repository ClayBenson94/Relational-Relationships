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

    Scanner scanner;

    public CSVHelper() {
        //Empty?
    }

    public void openCSV(String filePath) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filePath));
    }

    public ArrayList<String> readRow() {
        ArrayList<String> returnList = new ArrayList<>();
        String[] splitList;
        if (scanner.hasNext()){
            splitList = scanner.next().split(",");
            returnList = new ArrayList<String>(Arrays.asList(splitList));
        }
        return returnList;
    }

    public void closeCSV() {
        scanner.close();
    }

}
