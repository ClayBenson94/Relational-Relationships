package tables;

import helpers.CSVHelper;
import helpers.SQLHelper;
import objects.Interest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class InterestTable {

    public static void createInterestTable(Connection conn) {
        String query = "CREATE TABLE interests("
                + "interest_name VARCHAR(20),"
                + "PRIMARY KEY (interest_name),"
                + "interest_desc VARCHAR(200),"
                + "category VARCHAR(20),"
                + "FOREIGN KEY (category) REFERENCES interest_categories(category_name)"
                + ");";

        SQLHelper.execute(conn, query);
    }

    public static boolean createInterest(Connection conn, Interest interest) {
        String name, description, category;
        name = interest.getName();
        description = interest.getDescription();
        category = interest.getCategory();

        String query = "INSERT INTO interests "
                + "VALUES (interest_name=\'" + name + "\',"
                + "interest_desc=\'" + description + "\',"
                + "category=\'" + category + "\'"
                + ");";

        return SQLHelper.execute(conn, query);
    }

    public static boolean createInterestWithCheck(Connection conn, Interest interest) {
        String name, description, category;
        name = interest.getName();
        description = interest.getDescription();
        category = interest.getCategory();

        String query = "INSERT INTO interests "
                + "(interest_name, interest_desc, category) "
                + "SELECT \'" + name + "\', "
                + "\'" + description + "\', "
                + "\'" + category + "\' "
                + "FROM dual WHERE NOT EXISTS "
                +  "( SELECT 1 "
                + "FROM interests "
                + "WHERE interest_name = \'" + name + "\' "
                + ");";

        return SQLHelper.execute(conn, query);
    }

    public static ArrayList<Interest> getInterests(Connection conn) {
        ArrayList<Interest> returnList = new ArrayList<>();

        String query = "SELECT interest_name,category,interest_desc FROM interests"
                + " order by category;";

        ResultSet resultSet = SQLHelper.executeQuery(conn, query);

        try {
            while (resultSet.next()) {
                Interest interest = new Interest(resultSet.getString("interest_name"), resultSet.getString("interest_desc"),
                        resultSet.getString("category"));
                returnList.add(interest);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnList;
    }

    public static boolean populateFromCSV(Connection conn) {
        CSVHelper reader = new CSVHelper();

        reader.openCSV("resources/csv/interests.csv");
        while (reader.readRow()) {
            String query = "INSERT INTO interests "
                    + "VALUES (\'" + reader.currentRow.get(0)
                    + "\', \'" + reader.currentRow.get(1) + "\', \'" + reader.currentRow.get(2) + "\');";
            SQLHelper.execute(conn, query);
        }
        reader.closeCSV();

        return true;
    }
}
