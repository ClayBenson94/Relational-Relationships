package tables;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import objects.User;

public class LikesTable {

  public static void createLikesTable(Connection conn) {
    try {
      String query = "CREATE TABLE likes("
        + "sender VARCHAR(255),"
        + "receiver VARCHAR(255),"
        + "timestamp BIGINT UNSIGNED,"
        + "PRIMARY KEY (sender, receiver),"
        + "FOREIGN KEY (sender) REFERENCES user(username),"
        + "FOREIGN KEY (receiver) REFERENCES user(username)"
        + ");";

      Statement stmt = conn.createStatement();
      stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void createLike(Connection conn, User sender, User receiver) {
    try {
      String query = "INSERT INTO likes VALUES ("
        + sender.getUsername() + ","
        + receiver.getUsername() + ","
        + System.currentTimeMillis() + ");";


      Statement stmt = conn.createStatement();
      stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static ResultSet getLikesForUser(Connection conn, User user) {
    ResultSet resultSet = null;
    try {
      String query = "SELECT receiver, timestamp FROM likes WHERE sender=" + user.getUsername() + ";";
      Statement stmt = conn.createStatement();
      resultSet = stmt.executeQuery(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return resultSet;
  }
}
