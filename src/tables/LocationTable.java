package tables;

import helpers.CSVHelper;
import helpers.SQLHelper;
import objects.Location;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Allows for creation of location table and adding locations.
 */ 
public class LocationTable {

    /**
     * Creates the location table.
     * 
     * @param conn the connection to the database
     */ 
    public static void createLocationTable(Connection conn) {
        String query = "CREATE TABLE location("
                + "zip_code int(5) PRIMARY KEY ,"
                + "city VARCHAR(16),"
                + "state VARCHAR(2),"
                + ");";

        SQLHelper.execute(conn, query);
    }

    /**
     * Adds a location to the location table.
     * 
     * @param conn the connection to the database
     * @param zipCode the zip code of the location
     * @param city the city to add
     * @param state the state to add
     */ 
    public static void createLocation(Connection conn, int zipCode, String city, String state) {
        String query = "INSERT INTO location VALUES (\'" + zipCode + "\',\'" + city + "\',\'" + state + "\');";
        SQLHelper.execute(conn, query);
    }

    /**
     * Reads a cvs file for data and adds to the location table
     *
     * @param conn: database connection to work with
     */
    public static void populateFromCSV(Connection conn) {
        ArrayList<Location> locations = new ArrayList<Location>();

        CSVHelper reader = new CSVHelper();

        reader.openCSV("resources/csv/cities.csv");
        while (reader.readRow()) {
            locations.add(new Location(Integer.parseInt(reader.currentRow.get(0)), reader.currentRow.get(1),
                    reader.currentRow.get(2)));
        }
        reader.closeCSV();

        StringBuilder sb = new StringBuilder();

        sb.append("INSERT INTO location (zip_code, city, state) VALUES");

        // ********* This code has to be left, it is more efficient for bulk updates ********
        for (int i = 0; i < locations.size(); i++) {
            Location loc = locations.get(i);
            sb.append(String.format("(%d,'%s','%s')",
                    loc.getZipCode(), loc.getCity(), loc.getState()));
            if (i != locations.size() - 1) {
                sb.append(",");
            } else {
                sb.append(";");
            }
        }
        SQLHelper.execute(conn, sb.toString());
    }

    /**
     * Checks if a provided zip code exists in the table.
     *
     * @param conn: database connection to work with
     * @param zipCode the zipcode to check for
     * @return true or false
     */
    public static boolean isValidZip(Connection conn, Integer zipCode) {
        String query = "SELECT 1 FROM location WHERE zip_code = " + zipCode + ";";

        ResultSet resultSet = SQLHelper.executeQuery(conn, query);
        try {
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Returns a location object from the table using a provided zip code.
     *
     * @param conn: database connection to work with
     * @param zipCode the zipcode to check for
     * @return the location object
     */
    public static Location getInformationViaZip(Connection conn, Integer zipCode) {
        Location location = null;
        try {
            String query = "SELECT * FROM location WHERE zip_code = " + zipCode + ";";

            ResultSet resultSet = SQLHelper.executeQuery(conn, query);
            if (resultSet.next()) {
                location = new Location(resultSet.getInt("zip_code"), resultSet.getString("state"),
                        resultSet.getString("city"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return location;
    }
}
