package tables;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import objects.User;

public class UserTable {

  public static void createUserTable(Connection conn) {
    try {
      String query = "CREATE TABLE user("
        + "username VARCHAR(255),"
        + "password VARCHAR(255),"
        + "name VARCHAR(255),"
        + "bio LONGVARCHAR,"
        + "email VARCHAR(255),"
        + "dob BIGINT UNSIGNED,"
        + "gender VARCHAR(50),"
        + "sexuality VARCHAR(50),"
        + "location INT(5),"
        + "preferred_age_min INT(5),"
        + "preferred_age_max INT(5),"
        + "preferred_sexuality VARCHAR(50),"
        + "PRIMARY KEY (username),"
        + "FOREIGN KEY (location) REFERENCES location(zip_code),"
        + "CHECK (gender IN ('Male', 'Female')),"
        + "CHECK (sexuality IN ('Heterosexual', 'Homosexual')),"
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

  public static void updateUser(Connection conn, User user) {
    try {
      String query = "UPDATE user "
        + "SET password=" + user.getPassword()
        + ",name=" + user.getName()
        + ",bio=" + user.getBio()
        + ",email=" + user.getEmail()
        + ",dob=" + user.getDob()
        + ",gender=" + user.getGender()
        + ",sexuality=" + user.getSexuality()
        + ",location=" + user.getLocation()
        + ",preferred_age_min=" + user.getUserPreferences().getPreferredAgeMin()
        + ",preferred_age_max=" + user.getUserPreferences().getPreferredAgeMax()
        + ",preferred_sexuality=" + user.getUserPreferences().getPreferredSexuality()
        + " WHERE username=" + user.getUsername() +";";

      Statement stmt = conn.createStatement();
      stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
