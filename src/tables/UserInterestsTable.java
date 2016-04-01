package tables;

import objects.Interest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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

  public static boolean addInterestToUser(Connection conn, String username, Interest interest) {
    String interestName = interest.getName();
    try {
      String query = "INSERT INTO user_interests "
        + "VALUES (username=\'" + username + "\',"
        + "interest=\'" + interestName + "\'"
        + ");";

      Statement stmt = conn.createStatement();
      return stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static ArrayList<Interest> getUserInterests(Connection conn, String username) {
    ArrayList<Interest> returnList = new ArrayList<Interest>();

    try {
      String query = "SELECT interest,category,interest_desc FROM user_interests "
        + "inner join interests on user_interests.interest=interests.interest_name "
        + "WHERE username=\'" + username + "\';";

      Statement stmt = conn.createStatement();
      ResultSet resultSet = stmt.executeQuery(query);
      while (resultSet.next()) {
        Interest interest = new Interest(resultSet.getString("username"), resultSet.getString("interest_desc"), resultSet.getString("category"));
        returnList.add(interest);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return returnList;
  }
}
