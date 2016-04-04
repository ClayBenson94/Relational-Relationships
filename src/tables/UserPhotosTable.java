package tables;

import objects.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserPhotosTable {

  public static void createUserTable(Connection conn) {
    try {
      String query = "CREATE TABLE user_photos("
        + "username VARCHAR(20),"
        + "photo_url VARCHAR(1024),"
        + "PRIMARY KEY (username, photo_url),"
        + "FOREIGN KEY (username) REFERENCES user(username),"
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

  public static boolean addPhotoToUser(Connection conn, User user, String photoURL) {
    try {
      String query = "INSERT INTO user_photos "
        + "VALUES (username=\'" + user.getUsername()
        + "\',photo_url=\'" + photoURL + "\');";

      Statement stmt = conn.createStatement();
      return stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static void deleteUserPhoto(Connection conn, User user, String photoURL) {
    try {
      String query = "DELETE FROM user_photos "
        + "WHERE username=\'" + user.getUsername() + "\' AND photo_url=\'" + photoURL +"\';";

      Statement stmt = conn.createStatement();
      stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static ArrayList<String> getUserPhotos(Connection conn, User user) {

    ArrayList<String> userPhotos = new ArrayList<String>();

    try {
      String query = "SELECT * FROM user_photos "
              + "WHERE username=\'" + user.getUsername() +"\';";

      Statement stmt = conn.createStatement();
      ResultSet resultSet = stmt.executeQuery(query);

      while (resultSet.next()){
        // 0 is the username, 1 is the photo url
        userPhotos.add(resultSet.getString(1));
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  return userPhotos;
  }
}
