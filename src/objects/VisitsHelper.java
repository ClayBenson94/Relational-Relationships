package objects;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class VisitsHelper {

  public void createVisit(Connection conn, String visited, String vistor) {
    try {
      String query = "INSERT INTO visit VALUES (" + visited + ", " + vistor + ", " + System.currentTimeMillis() + ")";

      Statement stmt = conn.createStatement();
      stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<User> getVisitsForUser(Connection conn, User user) {
    try {
      String query = "SELECT * FROM visit WHERE visitor = " + user.getUsername();

      Statement stmt = conn.createStatement();
      stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return null;
  }
}
