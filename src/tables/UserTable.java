package tables;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import objects.RelationshipController;
import objects.User;

public class UserTable {

  public static void createUserTable(Connection conn) {
    try {
      String query = "DROP TABLE IF EXISTS user; CREATE TABLE user("
        + "username VARCHAR(20),"
        + "password VARCHAR(32),"
        + "name VARCHAR(255),"
        + "bio LONGVARCHAR,"
        + "email VARCHAR(255),"
        + "dob DATE,"
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

  public static boolean addUser(Connection conn, User user) {
    try {
      String query = "INSERT INTO user "
        + "VALUES (username=\'" + user.getUsername()
        + "\',password=\'" + user.getPassword()
        + "\',name=\'" + user.getName()
        + "\',bio=\'" + user.getBio()
        + "\',email=\'" + user.getEmail()
        + "\',dob=\'" + user.getDob()
        + "\',gender=\'" + user.getGender()
        + "\',sexuality=\'" + user.getSexuality()
        + "\',location=" + user.getLocation()
        + ",preferred_age_min=" + user.getUserPreferences().getPreferredAgeMin()
        + ",preferred_age_max=" + user.getUserPreferences().getPreferredAgeMax()
        + ",preferred_sexuality=\'" + user.getUserPreferences().getPreferredSexuality() + "\');";

      Statement stmt = conn.createStatement();
      return stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public static void updateUser(Connection conn, User user) {
    try {
      String query = "UPDATE user "
        + "SET password=\'" + user.getPassword()
        + "\',name=\'" + user.getName()
        + "\',bio=\'" + user.getBio()
        + "\',email=\'" + user.getEmail()
        + "\',dob=\'" + user.getDob()
        + "\',gender=\'" + user.getGender()
        + "\',sexuality=\'" + user.getSexuality()
        + "\',location=\'" + user.getLocation()
        + "\',preferred_age_min=\'" + user.getUserPreferences().getPreferredAgeMin()
        + "\',preferred_age_max=\'" + user.getUserPreferences().getPreferredAgeMax()
        + "\',preferred_sexuality=\'" + user.getUserPreferences().getPreferredSexuality()
        + "\' WHERE username=\'" + user.getUsername() + "\';";

      Statement stmt = conn.createStatement();
      stmt.execute(query);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Reads a cvs file for data and adds to the user table
   * @param conn: database connection to work with
   * @throws SQLException
   */
  public static void populateFromCSV(Connection conn){
    ArrayList<User> users = new ArrayList<User>();
    try {
      ClassLoader classloader = Thread.currentThread().getContextClassLoader();
      InputStream is = classloader.getResourceAsStream("users.csv");
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      String line;
      while((line = br.readLine()) != null){
        String[] split = line.split(",");
          DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
          java.util.Date date = null;
          try {
              date = format.parse(split[5]);
          } catch (ParseException e) {
              e.printStackTrace();
          }

          java.sql.Date sqlDate = new java.sql.Date(date.getTime());
          users.add(new User(split[0], split[1], split[2], split[3], split[4], sqlDate,
                  RelationshipController.getGender(split[6]), RelationshipController.getSexuality(split[7]),
                  Integer.parseInt(split[8]), Integer.parseInt(split[9]), Integer.parseInt(split[10]),
                  RelationshipController.getSexuality(split[11])));

      }
      br.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    StringBuilder sb = new StringBuilder();

    sb.append("INSERT INTO user (username, password, name, bio, email, dob, gender, sexuality," +
            " location, preferred_age_min, preferred_age_max, preferred_sexuality) VALUES");

    for(int i = 0; i < users.size(); i++){
      User user = users.get(i);
      sb.append(String.format("('%s','%s','%s','%s','%s','%s','%s','%s',%d,%d,%d,'%s')",
              user.getUsername(), user.getPassword(), user.getName(), user.getBio(), user.getEmail(), user.getDob(),
              user.getGender(), user.getSexuality(), user.getLocation(), user.getUserPreferences().getPreferredAgeMin(),
              user.getUserPreferences().getPreferredAgeMax(), user.getUserPreferences().getPreferredSexuality()));
      if( i != users.size()-1){
        sb.append(",");
      }
      else{
        sb.append(";");
      }
    }
    try {
      Statement stmt = conn.createStatement();
      stmt.execute(sb.toString());
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
