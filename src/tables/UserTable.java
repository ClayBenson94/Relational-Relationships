package tables;

import java.sql.Connection;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import helpers.CSVHelper;
import helpers.SQLHelper;
import objects.RelationshipController;
import objects.User;

public class UserTable {

  public static void createUserTable(Connection conn) {
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

    SQLHelper.execute(conn, query);
  }

  public static boolean addUser(Connection conn, User user) {
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

    return SQLHelper.execute(conn, query);
  }

  public static void updateUser(Connection conn, User user) {
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

    SQLHelper.execute(conn, query);
  }

  /**
   * Reads a cvs file for data and adds to the user table
   * @param conn: database connection to work with
   */
  public static void populateFromCSV(Connection conn){
    ArrayList<User> users = new ArrayList<User>();

    CSVHelper reader = new CSVHelper();
    reader.openCSV("resources/users.csv");
    while (reader.readRow()) {
      DateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
      java.util.Date date = null;
    try {
      date = format.parse(reader.currentRow.get(5));
    } catch (ParseException e) {
      e.printStackTrace();
    }

    Date sqlDate = null;
    if (date != null) {
      sqlDate = new Date(date.getTime());
    }
    users.add(new User(reader.currentRow.get(0), reader.currentRow.get(1), reader.currentRow.get(2),
            reader.currentRow.get(3), reader.currentRow.get(4), sqlDate,
            RelationshipController.getGender(reader.currentRow.get(6)),
            RelationshipController.getSexuality(reader.currentRow.get(7)),
            Integer.parseInt(reader.currentRow.get(8)), Integer.parseInt(reader.currentRow.get(9)),
            Integer.parseInt(reader.currentRow.get(10)),
            RelationshipController.getSexuality(reader.currentRow.get(11))));
    }
    reader.closeCSV();

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
    SQLHelper.execute(conn, sb.toString());
    }
}
