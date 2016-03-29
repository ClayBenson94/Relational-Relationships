package tables;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UserInterestsTable {

  public static void createUserInterestTable(Connection conn) {
    try {
      String query = "CREATE TABLE user_interests("
        + "username VARCHAR(20),"
        + "interest VARCHAR(20),"
        + "PRIMARY KEY (username,interest),"
        + "FOREIGN KEY (username) REFERENCES user(username),"
        + "FOREIGN KEY (interest) REFERENCES interests(interest_name)"
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

  public static boolean addInterestToUser(Connection conn, String username, String interest) {
    try {
      String query = "INSERT INTO user_interests "
        + "VALUES (username=\'" + username + "\',"
        + "interest=\'" + interest + "\'"
        + ");";

      Statement stmt = conn.createStatement();
      return stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }
}
