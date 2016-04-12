package tables;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import helpers.CSVHelper;
import helpers.DateHelper;
import helpers.SQLHelper;
import objects.RelationshipController;
import objects.User;

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
            + ",preferred_sexuality=\'" + user.getUserPreferences().getPreferredSexuality() + "\'"
            + ",is_admin =\'" + user.getIsAdmin() + "\');";

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
            + "\',is_admin=\'"
            + "\' WHERE username=\'" + user.getUsername() + "\';";

        SQLHelper.execute(conn, query);
    }

    public static ArrayList<User> search(Connection conn, String zipCode, User excludeUser) {
        ArrayList<User> returnList = new ArrayList<>();
        if (!zipCode.equals("")) {

            User curUser;

            String query = "SELECT * FROM user WHERE location = " + zipCode + " AND username NOT = \'" + excludeUser.getUsername() + "\';";
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
            " location, preferred_age_min, preferred_age_max, preferred_sexuality) VALUES");

        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            sb.append(String.format("('%s','%s','%s','%s','%s','%s','%s','%s',%d,%d,%d,'%s')",
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
                user.getUserPreferences().getPreferredSexuality()));
            if (i != users.size() - 1) {
                sb.append(",");
            } else {
                sb.append(";");
            }
        }
        SQLHelper.execute(conn, sb.toString());
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
