package tables;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import objects.Like;
import objects.User;

public class LikesTable {

  public static void createLikesTable(Connection conn) {
    try {
      String query = "CREATE TABLE likes("
        + "sender VARCHAR(20),"
        + "receiver VARCHAR(20),"
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
      String query = "INSERT INTO likes VALUES (\'"
        + sender.getUsername() + "\',\'"
        + receiver.getUsername() + "\',\'"
        + System.currentTimeMillis() + "\');";


      Statement stmt = conn.createStatement();
      stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Get who likes you
   * @param conn
   * @param username - The user to do the query for
   * @return An array list of usernames that like you
     */
  public static ArrayList<Like> getLikesForUser(Connection conn, String username) {
    ArrayList<Like> likes = new ArrayList<Like>();
    try {
      String query = "SELECT sender, timestamp FROM likes WHERE receiver=\'" + username + "\';";
      Statement stmt = conn.createStatement();
      ResultSet resultSet = stmt.executeQuery(query);

      while (resultSet.next()){
        Like like = new Like(resultSet.getString("sender"), resultSet.getLong("timestamp"));
        likes.add(like);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return likes;
  }
}
