package tables;

import objects.User;
import objects.Visit;

import java.sql.Connection;
import java.sql.ResultSet;
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

  public void createVisit(Connection conn, String visited, String vistor) {
    try {
      String query = "INSERT INTO visit VALUES (" + visited + ", " + vistor + ", " + System.currentTimeMillis() + ");";

      Statement stmt = conn.createStatement();
      stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<Visit> getVisitsForUser(Connection conn, User user) {
    ArrayList<Visit> userVisits = new ArrayList<>();
    try {
      String query = "SELECT * FROM visit WHERE visitor = \'" + user.getUsername() + "\';";

      Statement stmt = conn.createStatement();
      ResultSet resultSet = stmt.executeQuery(query);
      while (resultSet.next()) {
        Visit visit = new Visit(resultSet.getString("visitor"), resultSet.getLong("timestamp"));
        userVisits.add(visit);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return userVisits;
  }
}
