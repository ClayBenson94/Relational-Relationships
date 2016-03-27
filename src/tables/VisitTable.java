package tables;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class VisitTable {

  public static void createVisitTable(Connection conn) {
    try {
      String query = "DROP TABLE visit;"
        + "CREATE TABLE visit("
        + "visited VARCHAR(20),"
        + "visitor VARCHAR(20),"
        + "timestamp BIGINT UNSIGNED,"
        + "PRIMARY KEY (visited, visitor),"
        + "FOREIGN KEY (visited) REFERENCES user(username),"
        + "FOREIGN KEY (visitor) REFERENCES user(username),"
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
}
