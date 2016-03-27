package tables;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class LocationTable {

  public static void createLocationTable(Connection conn) {
    try {
      String query = "CREATE TABLE location("
        + "zip_code int(5) PRIMARY KEY,"
        + "city VARCHAR(16),"
        + "state VARCHAR(2);";

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
}
