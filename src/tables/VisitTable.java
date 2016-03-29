package tables;

import objects.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

  public void createVisit(Connection conn, String visited, String visitor) {
    try {
      String query = "INSERT INTO visit VALUES (" + visited + ", " + visitor + ", " + System.currentTimeMillis() + ");";

      Statement stmt = conn.createStatement();
      stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<User> getVisitsForUser(Connection conn, User user) {
    try {
      String query = "SELECT * FROM visit WHERE visitor = " + user.getUsername() + ";";

      Statement stmt = conn.createStatement();
      stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // TODO Make this return an actual array list
    return null;
  }
}
