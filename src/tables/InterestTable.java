package tables;

import helpers.CSVHelper;
import objects.Interest;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class InterestTable {

  public static void createInterestTable(Connection conn) {
    try {
      String query = "DROP TABLE IF EXISTS interests;CREATE TABLE interests("
        + "interest_name VARCHAR(20),"
        + "PRIMARY KEY (interest_name),"
        + "interest_desc VARCHAR(200),"
        + "category VARCHAR(20),"
        + "FOREIGN KEY (category) REFERENCES interest_categories(category_name)"
        + ");";

      /**
       * Create a query and execute
       */
      Statement stmt = conn.createStatement();
      stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static boolean createInterest(Connection conn, Interest interest) {
    String name, description, category;
    name = interest.getName();
    description = interest.getDescription();
    category = interest.getCategory();

    try {
      String query = "INSERT INTO interests "
        + "VALUES (interest_name=\'" + name + "\',"
        + "interest_desc=\'" + description + "\',"
        + "category=\'" + category + "\'"
        + ");";

      Statement stmt = conn.createStatement();
      return stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
  public static boolean populateFromCSV(Connection conn) {
    CSVHelper reader = new CSVHelper();

    reader.openCSV("resources/interests.csv");
    while (reader.readRow()) {
        try {
            String query = "INSERT INTO interest"
                    + "VALUES (\'" + reader.currentRow.get(0)
                    + "\', \'" + reader.currentRow.get(1) + "\', \'" + reader.currentRow.get(2) + "\');";
            Statement stmt = conn.createStatement();
            stmt.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    reader.closeCSV();

    return true;
    }
}
