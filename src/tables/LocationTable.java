package tables;

import objects.Location;
import objects.Visit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class LocationTable {

  public static void createLocationTable(Connection conn) {
    try {
      String query = "DROP TABLE location; CREATE TABLE location("
        + "zip_code int(5) PRIMARY KEY ,"
        + "city VARCHAR(16),"
        + "state VARCHAR(2),"
        + ");";

      Statement stmt = conn.createStatement();
      stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void createLocation(Connection conn, int zipCode, String city, String state) {
    try {
      String query = "INSERT INTO location VALUES (\'" + zipCode + "\',\'" + city + "\',\'" + state + "\');";

      Statement stmt = conn.createStatement();
      stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Reads a cvs file for data and adds to the location table
   * @param conn: database connection to work with
   * @throws SQLException
   */
  public static void populateFromCSV(Connection conn){
    ArrayList<Location> locations = new ArrayList<Location>();
    try {
      ClassLoader classloader = Thread.currentThread().getContextClassLoader();
      InputStream is = classloader.getResourceAsStream("cities.csv");
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      String line;
      while((line = br.readLine()) != null){
        String[] split = line.split(",");
        locations.add(new Location(Integer.parseInt(split[0]), split[1], split[2]));
      }
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
      StringBuilder sb = new StringBuilder();

      sb.append("INSERT INTO location (zip_code, city, state) VALUES");

      for(int i = 0; i < locations.size(); i++){
          Location loc = locations.get(i);
          sb.append(String.format("(%d,%s,%s)",
                  loc.getZipCode(), loc.getCity(), loc.getState()));
          if( i != locations.size()-1){
              sb.append(",");
          }
          else{
              sb.append(";");
          }
      }
      try {
          Statement stmt = conn.createStatement();
          stmt.execute(sb.toString());
      } catch (SQLException e) {
          e.printStackTrace();
      }
  }
    public static Location getInformationViaZip(Connection conn, Integer zipCode){
        Location location = null;
        try {
            String query = "SELECT * FROM location WHERE zip_code = " + zipCode + ";";

            Statement stmt = conn.createStatement();
            ResultSet resultSet = stmt.executeQuery(query);
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
