import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class VisitTable {

  public static void createVisitTable(Connection conn) {
    try {
      String query = "DROP TABLE visit;"
        + "CREATE TABLE visit("
        + "visited VARCHAR(255),"
        + "visitor VARCHAR(255),"
        + "timestamp BIGINT UNSIGNED,"
        + "PRIMARY KEY (visited, visitor),"
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
