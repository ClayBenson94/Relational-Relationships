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

/**
 * Allows for the creation of the UserInterests table and adding user interests.
 */ 
public class UserInterestsTable {

    /**
     * Creates the UserInterest table.
     * @param conn the connection to the database
     */ 
    public static void createUserInterestTable(Connection conn) {
        String query = "CREATE TABLE user_interests("
                + "username VARCHAR(20),"
                + "interest VARCHAR(20),"
                + "PRIMARY KEY (username,interest),"
                + "FOREIGN KEY (username) REFERENCES user(username) ON DELETE CASCADE,"
                + "FOREIGN KEY (interest) REFERENCES interests(interest_name) ON DELETE CASCADE"
                + ");";

        SQLHelper.execute(conn, query);
    }

    /**
     * Adds an interest to a user.
     * 
     * @param conn the connection to the database
     * @param username the username receiving the interest
     * @param interest the interest to give the user
     * @return true or false
     */
    public static boolean addInterestToUser(Connection conn, String username, Interest interest) {
        String interestName = interest.getName();
        String query = "INSERT INTO user_interests "
                + "VALUES (username=\'" + username + "\',"
                + "interest=\'" + interestName + "\'"
                + ");";

        return SQLHelper.execute(conn, query);
    }

    /**
     * Adds an interest to a user.
     * 
     * @param conn the connection to the database
     * @param username the username receiving the interest
     * @param interest the interest to give the user
     * @return true or false
     */
    public static boolean addInterestToUserWithCheck(Connection conn, String username, Interest interest) {
        String interestName = interest.getName();
        String query = "INSERT INTO user_interests (username, interest) "
                + "SELECT \'" + username + "\', "
                + "\'" + interestName + "\' "
                + "FROM dual WHERE NOT EXISTS "
                + "( SELECT 1 "
                + "FROM user_interests "
                + "WHERE interest = \'" + interestName + "\'"
                + ");";

        return SQLHelper.execute(conn, query);
    }

    /**
     * Deletes a UserInterest.
     * 
     * @param conn the connection to the database
     * @param username the username with the interest
     * @param interest the interest the user has
     */
    public static void deleteUserInterest(Connection conn, User user, Interest interest) {
        String name = interest.getName();
        String query = "DELETE FROM user_interests "
                + "WHERE username=\'" + user.getUsername() + "\' AND interest=\'" + name + "\';";

        SQLHelper.execute(conn, query);
    }

    /**
     * Returns a list of user interests for a given user.
     * 
     * @param conn the connection to the database
     * @param username the username to return interests for
     * @return arraylist of Interests for the user
     */
    public static ArrayList<Interest> getUserInterests(Connection conn, String username) {
        ArrayList<Interest> returnList = new ArrayList<>();

        String query = "SELECT interest,category,interest_desc FROM user_interests "
                + "INNER JOIN interests ON user_interests.interest=interests.interest_name "
                + "WHERE username=\'" + username + "\' order by category;";

        ResultSet resultSet = SQLHelper.executeQuery(conn, query);

        try {
            while (resultSet.next()) {
                Interest interest = new Interest(resultSet.getString("interest"), resultSet.getString("interest_desc"),
                        resultSet.getString("category"));
                returnList.add(interest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnList;
    }
  
    /**
     * Populates the user_interests table with data from
     * the userinterests.csv file in resources/csv/
     * 
     * @param conn the connection to the database
     */
    public static void populateFromCSV(Connection conn) {
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
