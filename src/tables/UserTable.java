package tables;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import helpers.CSVHelper;
import helpers.DateHelper;
import helpers.SQLHelper;
import objects.RelationshipController;
import objects.User;
import objects.UserPreferences;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

public class UserTable {

    public static void createUserTable(Connection conn) {
        String query = "CREATE TABLE user("
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
                + "is_admin BOOLEAN DEFAULT FALSE,"
                + "PRIMARY KEY (username),"
                + "FOREIGN KEY (location) REFERENCES location(zip_code),"
                + "CHECK (gender IN ('Male', 'Female')),"
                + "CHECK (sexuality IN ('Heterosexual', 'Homosexual')),"
                + ");";

        SQLHelper.execute(conn, query);
    }

    public static boolean addUser(Connection conn, User user) {
        String query = "INSERT INTO user "
                + "VALUES (\'" + user.getUsername()
                + "\',\'" + user.getPassword()
                + "\',\'" + user.getName()
                + "\',\'" + user.getBio()
                + "\',\'" + user.getEmail()
                + "\',\'" + user.getDob()
                + "\',\'" + user.getGender()
                + "\',\'" + user.getSexuality()
                + "\'," + user.getLocation()
                + "," + user.getUserPreferences().getPreferredAgeMin()
                + "," + user.getUserPreferences().getPreferredAgeMax()
                + ",\'" + user.getUserPreferences().getPreferredSexuality()
                + "\',\'" + user.getIsAdmin() + "\');";

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
                + "\',is_admin=\'" + user.getIsAdmin()
                + "\' WHERE username=\'" + user.getUsername() + "\';";

        SQLHelper.execute(conn, query);
    }

    /**
     * Delete a user from the database
     * @param conn - the connection object to the db
     * @param username - the username to delete
     * @param activeAdminUsername - if an admin is deleting accounts, pass his username, otherwise pass ""
     * @return A message indicating what happened
     */
    public static String deleteUser(Connection conn, String username, String activeAdminUsername) {

        if (username.equals(activeAdminUsername)){
            return "You cannot delete your own account here, please go to preferences";
        }

        String countQuery = "SELECT count(*) FROM user WHERE username='" + username + "\';";
        ResultSet resultSet = SQLHelper.executeQuery(conn, countQuery);

        try {
            if (resultSet.next()){
                if (resultSet.getInt("Count(*)") == 1){
                    String query =  "DELETE FROM user WHERE username=\'" + username + "\';";
                    SQLHelper.execute(conn, query);

                    return "User: " + username + " was deleted";
                } else {
                    return "User: " + username + " was not found";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "User: " + username + " was not deleted";
    }

    public static ArrayList<User> search(Connection conn, String zipCode, int offset, User activeUser) {
        ArrayList<User> returnList = new ArrayList<>();
        String username = activeUser.getUsername();
        if (!zipCode.equals("")) {

            User curUser;

            String sexualityString;
            switch (activeUser.getSexuality()) {
                case Heterosexual: sexualityString = " AND sexuality = 'Heterosexual'";
                    break;
                case Homosexual: sexualityString = " AND sexuality = 'Homosexual'";
                    break;
                default: sexualityString = " AND sexuality = 'BAD_SEXUALITY'";
                    break;
            }

            String genderString = " AND gender = ";
            switch (activeUser.getGender()) {
                case Male: genderString +=
                        activeUser.getSexuality()== RelationshipController.Sexuality.Heterosexual ? "'Female'" : "'Male'";
                    break;
                case Female: genderString +=
                        activeUser.getSexuality()== RelationshipController.Sexuality.Heterosexual ? "'Male'" : "'Female'";
                    break;
                default: genderString = " AND gender = 'BAD_GENDER'";
                    break;
            }

            //Filter out ages
            int ageMin, ageMax;
            UserPreferences activePrefs = activeUser.getUserPreferences();
            ageMin = activePrefs.getPreferredAgeMin();
            ageMax = activePrefs.getPreferredAgeMax();

            GregorianCalendar dobMax = new GregorianCalendar();
            GregorianCalendar dobMin = new GregorianCalendar();
            dobMax.add(Calendar.YEAR, -ageMax);
            dobMin.add(Calendar.YEAR, -ageMin);

            String queryDobMin, queryDobMax;
            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
            queryDobMax = timeFormat.format(dobMax.getTime());
            queryDobMin = timeFormat.format(dobMin.getTime());

            String ageString = " AND dob > \'" + queryDobMax + "\' AND dob < \'" + queryDobMin + "\'";

            String allUsers = "SELECT username AS username " +
                "FROM user AS A WHERE location=" + zipCode + " AND username <> '" + username + "' ";

            String interestMatches = "SELECT username, count(username) C " +
                "FROM user_interests " +
                "WHERE interest IN (SELECT interest FROM user_interests WHERE username='" + username + "') " +
                "AND username IN (" + allUsers + ") " +
                "GROUP BY username";

            String query = "SELECT A.*, B.C FROM user A " +
                "LEFT JOIN ("+interestMatches+") B " +
                "ON A.username = B.username " +
                "WHERE A.location=" + zipCode + " AND A.username <> \'" + username + "\' " + sexualityString + genderString + ageString + " " +
                "ORDER BY B.C DESC, A.name " +
                "LIMIT " + RelationshipController.OFFSET_COUNT + " OFFSET " + offset + ";";

            ResultSet resultSet = SQLHelper.executeQuery(conn, query);
            try {
                while (resultSet.next()) {

                    RelationshipController.Gender myGender;
                    RelationshipController.Sexuality mySexuality, preferredSexuality;

                    //Create some user properties
                    myGender = RelationshipController.getGender(resultSet.getString("gender"));
                    mySexuality = RelationshipController.getSexuality(resultSet.getString("sexuality"));
                    preferredSexuality = RelationshipController.getSexuality(resultSet.getString("preferred_sexuality"));
                    Date sqlDate = DateHelper.dateStringToSQLDate("yyyy-MM-dd", resultSet.getString("dob"));

                    curUser = new User(resultSet.getString("username"),
                            resultSet.getString("password"),
                            resultSet.getString("name"),
                            resultSet.getString("bio"),
                            resultSet.getString("email"),
                            sqlDate,
                            myGender,
                            mySexuality,
                            resultSet.getInt("location"),
                            resultSet.getInt("preferred_age_min"),
                            resultSet.getInt("preferred_age_max"),
                            preferredSexuality,
                            resultSet.getBoolean("is_admin")
                    );
                    returnList.add(curUser);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return returnList;
    }

    public static boolean isValidLogin(Connection conn, String username, String password) {
        String query = "SELECT password FROM user WHERE username = \'" + username + "\';";
        ResultSet resultSet = SQLHelper.executeQuery(conn, query);

        try {
            if (resultSet.next()) {
                String DBPassword = resultSet.getString("password");
                return password.equals(DBPassword);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Reads a cvs file for data and adds to the user table
     *
     * @param conn: database connection to work with
     */
    public static void populateFromCSV(Connection conn) {
        ArrayList<User> users = new ArrayList<User>();

        CSVHelper reader = new CSVHelper();
        reader.openCSV("resources/csv/users.csv");
        while (reader.readRow()) {
            Date sqlDate = DateHelper.dateStringToSQLDate("MM/dd/yyyy", reader.currentRow.get(5));
            users.add(new User(
                    reader.currentRow.get(0),
                    reader.currentRow.get(1),
                    reader.currentRow.get(2),
                    reader.currentRow.get(3),
                    reader.currentRow.get(4),
                    sqlDate,
                    RelationshipController.getGender(reader.currentRow.get(6)),
                    RelationshipController.getSexuality(reader.currentRow.get(7)),
                    Integer.parseInt(reader.currentRow.get(8)),
                    Integer.parseInt(reader.currentRow.get(9)),
                    Integer.parseInt(reader.currentRow.get(10)),
                    RelationshipController.getSexuality(reader.currentRow.get(11)),
                    Boolean.parseBoolean(reader.currentRow.get(12))));
            }
            reader.closeCSV();

            StringBuilder sb = new StringBuilder();

            sb.append("INSERT INTO user (username, password, name, bio, email, dob, gender, sexuality," +
                    " location, preferred_age_min, preferred_age_max, preferred_sexuality, is_admin) VALUES");

            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                sb.append(String.format("('%s','%s','%s','%s','%s','%s','%s','%s',%d,%d,%d,'%s', '%s')",
                        user.getUsername(),
                        user.getPassword(),
                        user.getName(),
                        user.getBio(),
                        user.getEmail(),
                        user.getDob(),
                        user.getGender(),
                        user.getSexuality(),
                        user.getLocation(),
                        user.getUserPreferences().getPreferredAgeMin(),
                        user.getUserPreferences().getPreferredAgeMax(),
                        user.getUserPreferences().getPreferredSexuality(),
                        user.getIsAdmin()));
                if (i != users.size() - 1) {
                    sb.append(",");
                } else {
                    sb.append(";");
                }
            }
            SQLHelper.execute(conn, sb.toString());
        }

    public static boolean isAvailableUsername(Connection conn, String username) {
        String query = "SELECT 1 FROM user WHERE username = \'" + username + "\';";
        ResultSet resultSet = SQLHelper.executeQuery(conn, query);

        try {
            if (resultSet.next()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static User getUserObject(Connection conn, String username) {
        String query = "SELECT * FROM user WHERE username = \'" + username + "\';";
        ResultSet resultSet = SQLHelper.executeQuery(conn, query);

        try {
            if (resultSet.next()) {
                return new User(resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("name"),
                        resultSet.getString("bio"), resultSet.getString("email"), resultSet.getDate("dob"),
                        RelationshipController.getGender(resultSet.getString("gender")),
                        RelationshipController.getSexuality(resultSet.getString("sexuality")),
                        resultSet.getInt("location"), resultSet.getInt("preferred_age_min"),
                        resultSet.getInt("preferred_age_max"),
                        RelationshipController.getSexuality(resultSet.getString("preferred_sexuality")),
                        resultSet.getBoolean("is_admin"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
