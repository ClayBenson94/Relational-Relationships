package tables;

import helpers.CSVHelper;
import helpers.SQLHelper;
import objects.Interest;
import objects.RelationshipController;
import objects.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.StringJoiner;

public class UserInterestsTable {

  public static void createUserInterestTable(Connection conn) {
    String query = "CREATE TABLE user_interests("
      + "username VARCHAR(20),"
      + "interest VARCHAR(20),"
      + "PRIMARY KEY (username,interest),"
      + "FOREIGN KEY (username) REFERENCES user(username),"
      + "FOREIGN KEY (interest) REFERENCES interests(interest_name)"
      + ");";

    SQLHelper.execute(conn, query);
  }

  public static boolean addInterestToUser(Connection conn, String username, Interest interest) {
    String interestName = interest.getName();
    String query = "INSERT INTO user_interests "
      + "VALUES (username=\'" + username + "\',"
      + "interest=\'" + interestName + "\'"
      + ");";

    return SQLHelper.execute(conn, query);
  }

  public static ArrayList<Interest> getUserInterests(Connection conn, String username) {
    ArrayList<Interest> returnList = new ArrayList<>();

    String query = "SELECT interest,category,interest_desc FROM user_interests "
      + "INNER JOIN interests ON user_interests.interest=interests.interest_name "
      + "WHERE username=\'" + username + "\' order by category;";

    ResultSet resultSet = SQLHelper.executeQuery(conn, query);

    try{
      while (resultSet.next()) {
        Interest interest = new Interest(resultSet.getString("username"), resultSet.getString("interest_desc"),
                resultSet.getString("category"));
        returnList.add(interest);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return returnList;
    }

  public static void populateFromCSV(Connection conn){
    Set<String> userInterests = new HashSet<>();
    CSVHelper reader = new CSVHelper();
    reader.openCSV("resources/csv/userinterests.csv");
    StringBuilder sb = new StringBuilder();
    sb.append("INSERT INTO user_interests (username, interest) VALUES ");

    while (reader.readRow()) {
      userInterests.add(String.format("('%s','%s')", reader.currentRow.get(0), reader.currentRow.get(1)));
    }
    reader.closeCSV();

    sb.append(String.join(",", userInterests));
    sb.append(";");

    SQLHelper.execute(conn, sb.toString());
  }
}
